package com.ucm.ilsa.veterinaria.business.event.alerta;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.ucm.ilsa.veterinaria.business.event.Event;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;

public class GeoTagAlertEvent extends Event {
	
	private Feed feed;
	private Map<News,List<ResolvedLocation>> locations;
	private Date date;
	
	public GeoTagAlertEvent() {
	
	}
	
	public Feed getFeed() {
		return feed;
	}
	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	public Map<News, List<ResolvedLocation>> getLocations() {
		return locations;
	}
	public void setLocations(Map<News, List<ResolvedLocation>> locations) {
		this.locations = locations;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
