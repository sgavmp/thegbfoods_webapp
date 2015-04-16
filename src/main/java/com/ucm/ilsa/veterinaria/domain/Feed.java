package com.ucm.ilsa.veterinaria.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Entity
public class Feed extends BaseEntity {
	
	@Id
	private String codeName;
	private String name;
	private String url;
	@ElementCollection(fetch=FetchType.EAGER)
	private List<PairValues> selectorHtml;
	@ElementCollection(fetch=FetchType.EAGER)
	private List<PairValues> selectorMeta;
	private String dateFormat;
	private Locale languaje;
	private boolean withRSS = false; //Por defecto no
	private String lastNewsLink;
	@OneToMany(mappedBy="site", cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Alert> listOfAlerts;
	
	//Solo sitios sin RSS
	private String urlNews;
	private String newsLink;
	
	//Solo sitios RSS
	private boolean useSelector = false;
	private String urlRSS;
	
	public Feed() {
		this.selectorHtml = new ArrayList<PairValues>();
		this.selectorMeta = new ArrayList<PairValues>();
	}
	
	public Feed(FeedForm feed) {
		this.name=feed.getName();
		this.codeName=feed.getCodeName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.selectorHtml=feed.getSelectorHtml();
		this.selectorMeta=feed.getSelectorMeta();
		if (this.selectorHtml.size()>0 || this.selectorMeta.size()>0) {
			this.useSelector=true;
		}
		this.url=feed.getUrl();
		if (!feed.getUrlNews().isEmpty()) {
			this.urlNews=feed.getUrlNews();
			this.newsLink=feed.getNewsLink();
			this.withRSS=false;
		} else {
			this.urlRSS=feed.getUrlRSS();
			this.withRSS=true;
		}
		this.useSelector=feed.isUseSelector();
		this.listOfAlerts= new ArrayList<Alert>();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrlRSS() {
		return urlRSS;
	}
	public void setUrlRSS(String urlRSS) {
		this.urlRSS = urlRSS;
	}
	public String getUrlNews() {
		return urlNews;
	}
	public void setUrlNews(String urlNews) {
		this.urlNews = urlNews;
	}

	public List<PairValues> getSelectorHtml() {
		return selectorHtml;
	}
	public void setSelectorHtml(List<PairValues> selectorHtml) {
		this.selectorHtml = selectorHtml;
	}

	public List<PairValues> getSelectorMeta() {
		return selectorMeta;
	}

	public void setSelectorMeta(List<PairValues> selectorMeta) {
		this.selectorMeta = selectorMeta;
	}

	public boolean isUseSelector() {
		return useSelector;
	}

	public void setUseSelector(boolean useSelector) {
		this.useSelector = useSelector;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Locale getLanguaje() {
		return languaje;
	}

	public void setLanguaje(Locale languaje) {
		this.languaje = languaje;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public boolean isWithRSS() {
		return withRSS;
	}

	public void setWithRSS(boolean withRSS) {
		this.withRSS = withRSS;
	}

	public String getLastNewsLink() {
		return lastNewsLink;
	}

	public void setLastNewsLink(String lastNewsLink) {
		this.lastNewsLink = lastNewsLink;
	}
	
	public String getNewsLink() {
		return newsLink;
	}

	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}

	public List<Alert> getListOfAlerts() {
		return listOfAlerts;
	}

	public void changeValues(FeedForm feed) {
		this.name=feed.getName();
		this.codeName=feed.getCodeName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.selectorHtml=feed.getSelectorHtml();
		this.selectorMeta=feed.getSelectorMeta();
		this.url=feed.getUrl();
		this.urlNews=feed.getUrlNews();
		this.urlRSS=feed.getUrlRSS();
		this.useSelector=feed.isUseSelector();
		this.withRSS=feed.isWithRSS();
	}
	
}
