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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Entity
public class Feed extends BaseEntity {
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String codeName;
	private String name;
	private String urlSite;
	@ElementCollection(fetch=FetchType.EAGER)
	private List<PairValues> selectorHtml;
	@ElementCollection(fetch=FetchType.EAGER)
	private List<PairValues> selectorMeta;
	private String dateFormat;
	@Enumerated(EnumType.STRING)
	private Language languaje;
	private String lastNewsLink;
	@OneToMany(mappedBy="site", cascade=CascadeType.ALL,orphanRemoval=true)
	private List<Alert> listOfAlerts;
	private String urlNews;
	private boolean isRSS = true;
	
	//Solo sitios sin RSS
	@ElementCollection(fetch=FetchType.EAGER)
	private List<String> urlPages;
	private String newsLink;
	
	
	
	public Feed() {
		this.selectorHtml = new ArrayList<PairValues>();
		this.selectorMeta = new ArrayList<PairValues>();
	}
	
	public Feed(FeedForm feed) {
		this.name=feed.getName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.selectorHtml=feed.getSelectorHtml();
		this.selectorMeta=feed.getSelectorMeta();
		this.isRSS = feed.isRSS();
		this.urlSite=feed.getUrl();
		this.urlNews=feed.getUrlNews();
		this.urlPages=feed.getUrlPages();
		this.listOfAlerts= new ArrayList<Alert>();
		this.newsLink=feed.getNewsLink();
	}	

	public void changeValues(FeedForm feed) {
		this.name=feed.getName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.selectorHtml=feed.getSelectorHtml();
		this.selectorMeta=feed.getSelectorMeta();
		this.urlSite=feed.getUrl();
		this.urlNews=feed.getUrlNews();
		this.urlPages=feed.getUrlPages();
		this.isRSS = feed.isRSS();
		this.newsLink=feed.getNewsLink();
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlSite() {
		return urlSite;
	}

	public void setUrlSite(String urlSite) {
		this.urlSite = urlSite;
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

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Language getLanguaje() {
		return languaje;
	}

	public void setLanguaje(Language languaje) {
		this.languaje = languaje;
	}

	public String getLastNewsLink() {
		return lastNewsLink;
	}

	public void setLastNewsLink(String lastNewsLink) {
		this.lastNewsLink = lastNewsLink;
	}

	public List<Alert> getListOfAlerts() {
		return listOfAlerts;
	}

	public void setListOfAlerts(List<Alert> listOfAlerts) {
		this.listOfAlerts = listOfAlerts;
	}

	public String getUrlNews() {
		return urlNews;
	}

	public void setUrlNews(String urlNews) {
		this.urlNews = urlNews;
	}

	public boolean isRSS() {
		return isRSS;
	}

	public void setRSS(boolean isRSS) {
		this.isRSS = isRSS;
	}

	public List<String> getUrlPages() {
		return urlPages;
	}

	public void setUrlPages(List<String> urlPages) {
		this.urlPages = urlPages;
	}

	public String getNewsLink() {
		return newsLink;
	}

	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}
	
	
}
