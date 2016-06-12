package es.ucm.visavet.gbf.app.util;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class GBFoodCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz|xml|json))$");

	private List<String> newLinks;

	public GBFoodCrawler() {
		newLinks = Lists.newArrayList();
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !FILTERS.matcher(href).matches()
				&& referringPage.getWebURL().getDomain()
						.equals(url.getDomain());// Only same domain
	}

	@Override
	public void visit(Page page) {
		if (page.getWebURL().getDepth() > 0) {
			String url = page.getWebURL().getURL();
			if (page.getParseData() instanceof HtmlParseData) {
				newLinks.add(url);
			}
		}
	}

	@Override
	public Object getMyLocalData() {
		return newLinks;
	}

}
