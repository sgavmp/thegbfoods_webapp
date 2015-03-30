package com.ucm.ilsa.veterinaria.event.alerta;

import java.util.Date;
import java.util.List;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.event.Event;

public class TodoAlertEvent extends Event {
	
	private Feed feed;
	private List<News> news;
	private Date date;
	
	public TodoAlertEvent() {
	
	}

	public Feed getFeed() {
		return feed;
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
