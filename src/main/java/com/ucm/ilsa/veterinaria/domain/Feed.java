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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String code;
	private String name;
	@Lob
	private String urlSite;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feed_selector_hmtl", joinColumns = @JoinColumn(name = "SITE_ID"))
	@OrderColumn
	private List<PairValues> selectorHtml;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feed_selector_meta", joinColumns = @JoinColumn(name = "SITE_ID"))
	@OrderColumn
	private List<PairValues> selectorMeta;
	private String dateFormat;
	@Enumerated(EnumType.STRING)
	private Language languaje;
	@Lob
	private String lastNewsLink = "";
	@Lob
	private String urlNews;
	private boolean isRSS = true;
	@Enumerated(EnumType.ORDINAL)
	private WebLevel type;
	private Integer numNewNews;
	private Timestamp ultimaRecuperacion;
	private boolean actived = false;
	private boolean accepted = false;
	private String comment;
	private Integer minRefresh;

	// Solo sitios sin RSS
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "feed_url_pages", joinColumns = @JoinColumn(name = "SITE_ID"))
	@OrderColumn
	@Lob
	private List<String> urlPages;
	private String newsLink;

	public Feed() {
		this.selectorHtml = new ArrayList<PairValues>();
		this.selectorMeta = new ArrayList<PairValues>();
	}

	public Feed(FeedForm feed) {
		this.name = feed.getName();
		this.dateFormat = feed.getDateFormat();
		this.languaje = feed.getLanguaje();
		this.selectorHtml = feed.getSelectorHtml();
		this.selectorMeta = feed.getSelectorMeta();
		this.isRSS = feed.getIsRSS();
		this.urlSite = feed.getUrl();
		this.urlNews = feed.getUrlNews();
		this.urlPages = feed.getUrlPages();
		this.newsLink = feed.getNewsLink();
		this.type = feed.getType();
		this.minRefresh = feed.getMinRefresh();
	}

	public void changeValues(FeedForm feed) {
		this.name = feed.getName();
		this.dateFormat = feed.getDateFormat();
		this.languaje = feed.getLanguaje();
		this.selectorHtml = feed.getSelectorHtml();
		this.selectorMeta = feed.getSelectorMeta();
		this.urlSite = feed.getUrl();
		this.urlNews = feed.getUrlNews();
		this.urlPages = feed.getUrlPages();
		this.isRSS = feed.getIsRSS();
		this.newsLink = feed.getNewsLink();
		this.type = feed.getType();
		this.minRefresh = feed.getMinRefresh();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public WebLevel getType() {
		return type;
	}

	public void setType(WebLevel type) {
		this.type = type;
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

	public Integer getNumNewNews() {
		return numNewNews;
	}

	public void setNumNewNews(Integer numNewNews) {
		this.numNewNews = numNewNews;
	}

	public Timestamp getUltimaRecuperacion() {
		return ultimaRecuperacion;
	}

	public void setUltimaRecuperacion(Timestamp ultimaRecuperacion) {
		this.ultimaRecuperacion = ultimaRecuperacion;
	}

	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getMinRefresh() {
		return minRefresh;
	}

	public void setMinRefresh(Integer minRefresh) {
		this.minRefresh = minRefresh;
	}
}
