package es.ucm.visavet.gbf.app.domain.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.Language;
import es.ucm.visavet.gbf.app.domain.News;

public class NewsBuilder {

	private News news;
	private String dateFormat;
	private Locale locale;

	public NewsBuilder(Feed feed) {
		this.news = new News();
		this.news.setContent("");
		this.news.setSite(feed.getId());
		this.dateFormat = feed.getDateFormat();
		if (feed.getLanguaje().equals(Language.SPANISH)) {
			this.locale = new Locale("es", "ES");
		} else {
			this.locale = Locale.ENGLISH;
		}
	}

	public News build() {
		return this.news;
	}

	public NewsBuilder setTitle(String title) {
		this.news.setTitle(title);
		return this;
	}

	public NewsBuilder setContent(String content) {
		this.news.setContent(content);
		return this;
	}

	public NewsBuilder setPubDate(Date date) {
		this.news.setPubDate(date);
		return this;
	}

	public NewsBuilder setUrl(String url) {
		this.news.setUrl(url);
		return this;
	}

	public NewsBuilder setPubDate(String date) {
		try {
		this.news.setPubDate(new SimpleDateFormat(dateFormat, this.locale)
				.parse(date));
		} catch (ParseException ex) {
			this.news.setPubDate(new Date(System.currentTimeMillis()));
		}
		return this;
	}
}
