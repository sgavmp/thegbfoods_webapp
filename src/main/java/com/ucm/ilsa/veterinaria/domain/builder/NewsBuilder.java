package com.ucm.ilsa.veterinaria.domain.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Language;
import com.ucm.ilsa.veterinaria.domain.News;

public class NewsBuilder {

	private News news;
	private String dateFormat;
	private Locale locale;

	public NewsBuilder(Feed feed) {
		this.news = new News();
		this.news.setSite(feed.getCode());
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

	public NewsBuilder setDescription(String description) {
		this.news.setDescription(description);
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

	public NewsBuilder setValueOf(String attribute, String value) {
		switch (attribute) {
		case "title":
			this.news.setTitle(value);
			break;
		case "description":
			this.news.setDescription(value);
			break;
		case "content":
			this.news.setContent(value);
			if (value.length()>200)
				this.news.setDescription(value.substring(0, 200).concat("..."));
			else
				this.news.setDescription(value);
			break;
		case "url":
			this.news.setUrl(value);
			break;
		case "pubDate":
			try {
				this.news.setPubDate(new SimpleDateFormat(dateFormat,
						this.locale).parse(value));
			} catch (ParseException ex) {
				this.news.setPubDate(new Date(System.currentTimeMillis()));
			}
			break;
		default:
			this.news.getOthers().put(attribute, value);
			break;
		}
		return this;
	}
}
