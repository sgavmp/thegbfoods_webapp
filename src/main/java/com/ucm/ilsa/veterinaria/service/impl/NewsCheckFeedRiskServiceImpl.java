package com.ucm.ilsa.veterinaria.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.extractor.AlphaExtractor;
import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.PointLocation;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.FeedRiskService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedRiskService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

@Service
public class NewsCheckFeedRiskServiceImpl implements NewsCheckFeedRiskService {

	@Autowired
	private NewsDetectRepository repositoryNewsDetect;

	@Autowired
	private RiskServiceImpl alertService;

	private ConfiguracionRepository configuracionRepository;

	@Autowired
	private FeedRiskService service;

	private final static Logger LOGGER = Logger
			.getLogger(NewsCheckFeedRiskServiceImpl.class);

	@Autowired
	public NewsCheckFeedRiskServiceImpl(
			ConfiguracionRepository configuracionRepository) {
		this.configuracionRepository = configuracionRepository;
		Configuracion configuracion = configuracionRepository.findOne("conf");
	}

	public void checkNews(List<News> listNews, FeedRisk feed) {
		LOGGER.info("Iniciada comprobacion de riesgos futuros: "
				+ feed.getName());
		Integer alertDetectNum = 0;
		List<Risk> alertas = alertService.getAllAlert();
		feed = service.setSateOfFeed(feed, UpdateStateEnum.DETECT_ALERTS);
		for (Risk alerta : alertas) {
			Map<News, List<String>> mapNewsDetect = new HashedMap();
			// Comprobamos todos los terminos y los almacenamos
			for (News news : listNews) {
				if (news != null) {
					List<String> terminos = Lists.newArrayList(alerta
							.getWords().split(","));
					terminos.add(alerta.getTitle());
					for (String word : terminos) {
						boolean caseSensitive = false;
						if (word.startsWith("\"") && word.endsWith("\"")) {
							word = word.replace("\"", "").trim();
							caseSensitive = true;
						} else {
							word = word.toLowerCase().trim();
						}
						String content = caseSensitive ? news.getContent()
								: news.getContent().toLowerCase();
						if (content.contains(" " + word + " ")
								|| content.startsWith(word + " ")
								|| content.endsWith(" " + word)) {
							if (mapNewsDetect.containsKey(news)) {
								mapNewsDetect.get(news).add(word);
							} else {
								List<String> wordsDetect = new ArrayList<String>();
								wordsDetect.add(word);
								mapNewsDetect.put(news, wordsDetect);
							}
						}
					}
				}
			}
			for (News news : mapNewsDetect.keySet()) {
				if (repositoryNewsDetect.findFirstByAlertDetectAndLink(
						alerta, news.getUrl()) != null) {
					continue;
				}
				List<String> wordsDetect = mapNewsDetect.get(news);
				NewsDetect newsDetect = new NewsDetect();
				newsDetect.setDatePub(news.getPubDate());
				newsDetect.setAlertDetect(alerta);
				newsDetect.setLink(news.getUrl());
				newsDetect.setSite(feed);
				newsDetect.setTitle(news.getTitle());
				newsDetect.setWordsDetect(wordsDetect);
				if (repositoryNewsDetect.findFirstByAlertDetectAndLink(
						alerta, newsDetect.getLink()) == null) {
					repositoryNewsDetect.save(newsDetect);
					alertDetectNum++;
					alerta.getNewsDetect().add(newsDetect);
				}
			}
			alertService.update(alerta);

		}
	}

	public List<Risk> checkNews(News news, FeedRisk feed) {
		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ feed.getName());
		List<Risk> alertas = alertService.getAllAlert();
		List<Risk> alertasDetectadas = new ArrayList<Risk>();
		for (Risk alerta : alertas) {
			Risk alertDetectActive = new Risk();
			alertDetectActive.setNewsDetect(new ArrayList<NewsDetect>());
			List<String> wordsDetect = new ArrayList();
			// Comprobamos todos los terminos y los almacenamos
			if (news != null) {
				List<String> terminos = Lists.newArrayList(alerta.getWords()
						.split(","));
				terminos.add(alerta.getTitle());
				String contentInLowerCase = news.getContent().toLowerCase();
				for (String word : terminos) {
					if (contentInLowerCase.contains(" " + word.toLowerCase()
							+ " ")) {
						wordsDetect.add(word);
					}
				}
			}
			// Si se han detectado palabras de la alerta en la noticia
			if (!wordsDetect.isEmpty()) {
				NewsDetect newsDetect = new NewsDetect();
				newsDetect.setDatePub(news.getPubDate());
				newsDetect.setAlertDetect(alertDetectActive);
				newsDetect.setLink(news.getUrl());
				newsDetect.setSite(feed);
				newsDetect.setTitle(news.getTitle());
				newsDetect.setWordsDetect(wordsDetect);
				alertDetectActive.getNewsDetect().add(newsDetect);
			}
			if (!alertDetectActive.getNewsDetect().isEmpty())
				alertasDetectadas.add(alertDetectActive);
		}
		return alertasDetectadas;
	}

}
