package com.ucm.ilsa.veterinaria.business.event.tratamiento;

import java.util.Date;
import java.util.List;

import com.ucm.ilsa.veterinaria.business.event.Event;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public class FeedUpdateEvent extends Event {
	
	private Feed feed;
	private List<News> listNews;
	private Date date;
	
	public FeedUpdateEvent() {
	
	}

	public Feed getFeed() {
		return feed;
	}

	public void setFeed(Feed feed) {
		this.feed = feed;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<News> getListNews() {
		return listNews;
	}

	public void setListNews(List<News> listNews) {
		this.listNews = listNews;
	}
	
}
