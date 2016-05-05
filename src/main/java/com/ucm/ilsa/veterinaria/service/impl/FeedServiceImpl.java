package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.FeedPlaceEnum;
import com.ucm.ilsa.veterinaria.domain.FeedTypeEnum;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.FeedRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;
import com.ucm.ilsa.veterinaria.service.FeedScraping;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

@Service
public class FeedServiceImpl implements FeedService {
	
	@Autowired
	private FeedRepository repositoryFeed;
	@Autowired
	private FeedScraping scrapingFeed;
	@Autowired
	private NewsDetectRepository newsDetectRepository;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private NewsIndexService newsIndexService;

	@Override
	public List<News> scrapFeed(Feed feed, Date after, boolean withOutLimit) {
		return scrapingFeed.scrapNews(feed, after, withOutLimit);
	}
	
	public News testFeed(FeedForm feed) {
		News news = scrapingFeed.scrapOneNews(feed);
		return news;
	}

	@Override
	public List<Feed> getAllFeed() {
		return repositoryFeed.findAllByOrderByNameAsc();
	}

	@Override
	public Feed getFeedByCodeName(Long codeName) {
		return repositoryFeed.findOne(codeName);
	}

	@Override
	public Feed createFeed(Feed feed) {
		feed = repositoryFeed.save(feed);
		schedulerService.addFeedTask(feed);
		return feed;
	}

	@Override
	public boolean removeFeed(Feed feed) {
		schedulerService.removeFeedTask(feed);
		this.repositoryFeed.delete(feed);
		return !this.repositoryFeed.exists(feed.getId());
	}

	@Override
	public Feed updateFeed(Feed feed) {
		Feed feedU = repositoryFeed.save(feed);
		schedulerService.updateFeedTask(feed);
		if (feed.isUpdateIndex())
			try {
				newsIndexService.updateIndex(feed);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return feedU;
	}

	public SchedulerService getSchedulerService() {
		return schedulerService;
	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	public List<NewsDetect> findAllDistinctNewsDetectByFeedOrderByDatePub(Feed feed) {
		return newsDetectRepository.findAllDistinctBySiteOrderByDatePubDesc(feed);
	}
	
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state) {
		feed = repositoryFeed.findOne(feed.getId());
		feed.setState(state);
		Feed feedU = repositoryFeed.save(feed);
		return feedU;
	}

	@Override
	public List<String> createFeedAuto(String[] listUrl) {
		List<String> fail = Lists.newArrayList();
		String regex = "^(?<name>[A-Za-z0-9 ]{2,50})\\t(?<url>(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*)\\t(?<tipo>@T(general|periodico|revistaCientifica|revista|blogNutricional|blog|institucional)+)(?<lugar>(\\t(@L(general|españa|italia|rusia|holanda|alemania|inglaterra|portugal|francia|estadosunidos|india|marruecos)+))+)$";
		Pattern pattern = Pattern.compile(regex);
		for (String linea : listUrl) {
			Matcher matcher = pattern.matcher(linea);
			matcher.find();
			String name = matcher.group("name");
			String url = matcher.group("url");
			String tipo = matcher.group("tipo");
			String lugares = matcher.group("lugar");
			FeedTypeEnum typeE = FeedTypeEnum.valueOf(tipo.substring(2));
			List<FeedPlaceEnum> lugaresE = Lists.newArrayList();
			for (String lugaStep : lugares.split("\t")) {
				if (!lugaStep.trim().isEmpty())
					lugaresE.add(FeedPlaceEnum.valueOf(lugaStep.trim().substring(2)));
			}
			URL link = null;
			try {
				link = new URL(url);
			} catch (MalformedURLException e) {
				fail.add(url);
				continue;
			}
			Feed feed = new Feed();
			feed.setUrlNews(url);
			feed.setName(name);
			feed.setUrlSite(link.getProtocol() + "://" +link.getAuthority());
			feed.setAuto(true);
			feed.setFeedType(typeE);
			feed.setFeedPlace(lugaresE);
			this.createFeed(feed);
		}
		return fail;
	}

}
