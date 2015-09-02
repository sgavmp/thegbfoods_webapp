package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class FeedForm {
	@NotBlank
	private String name;
	@URL
	@NotEmpty
	private String url;//Url del sitio
	private List<PairValues> selectorHtml,selectorMeta;
	private String dateFormat;
	//Por defecto Spanish
	private Language languaje = Language.SPANISH;
	private boolean isRSS = true;
	private WebLevel type = WebLevel.yellow;
	private Integer minRefresh = 120;
	private String selectorTitle;
	private String selectorDescription;
	private String selectorContent;
	private String selectorPubDate;
	private boolean selectorTitleMeta;
	private boolean selectorDescriptionMeta;
	private boolean selectorContentMeta;
	private boolean selectorPubDateMeta;
	
	@URL
	private String urlNews;//Url de la pagina de noticias o de rss
	
	private List<String> urlPages;
	private String newsLink;
	
	public FeedForm() {
		this.selectorHtml = new ArrayList<PairValues>();
		this.selectorMeta = new ArrayList<PairValues>();
		this.urlPages = new ArrayList<String>();
	}
	
	public FeedForm(Feed feed) {
		this.name=feed.getName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.selectorHtml=feed.getSelectorHtml();
		this.selectorMeta=feed.getSelectorMeta();
		this.urlPages=feed.getUrlPages();
		this.url=feed.getUrlSite();
		this.urlNews=feed.getUrlNews();
		this.newsLink = feed.getNewsLink();
		this.type = feed.getType();
		this.isRSS = feed.isRSS();
		this.minRefresh = feed.getMinRefresh();
		this.selectorContent = feed.getSelectorContent();
		this.selectorContentMeta = feed.getSelectorContentMeta();
		this.selectorDescription = feed.getSelectorDescription();
		this.selectorDescriptionMeta = feed.getSelectorDescriptionMeta();
		this.selectorTitle = feed.getSelectorTitle();
		this.selectorTitleMeta = feed.getSelectorTitleMeta();
		this.selectorPubDate = feed.getSelectorPubDate();
		this.selectorPubDateMeta = feed.getSelectorPubDateMeta();
				
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

	public boolean getIsRSS() {
		return isRSS;
	}

	public void setIsRSS(boolean isRSS) {
		this.isRSS = isRSS;
	}

	public String getUrlNews() {
		return urlNews;
	}

	public void setUrlNews(String urlNews) {
		this.urlNews = urlNews;
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

	public WebLevel getType() {
		return type;
	}

	public void setType(WebLevel type) {
		this.type = type;
	}

	public Integer getMinRefresh() {
		return minRefresh;
	}

	public void setMinRefresh(Integer minRefresh) {
		this.minRefresh = minRefresh;
	}

	public String getSelectorTitle() {
		return selectorTitle;
	}

	public void setSelectorTitle(String selectorTitle) {
		this.selectorTitle = selectorTitle;
	}

	public String getSelectorDescription() {
		return selectorDescription;
	}

	public void setSelectorDescription(String selectorDescription) {
		this.selectorDescription = selectorDescription;
	}

	public String getSelectorContent() {
		return selectorContent;
	}

	public void setSelectorContent(String selectorContent) {
		this.selectorContent = selectorContent;
	}

	public String getSelectorPubDate() {
		return selectorPubDate;
	}

	public void setSelectorPubDate(String selectorPubDate) {
		this.selectorPubDate = selectorPubDate;
	}

	public boolean getSelectorTitleMeta() {
		return selectorTitleMeta;
	}

	public void setSelectorTitleMeta(boolean selectorTitleMeta) {
		this.selectorTitleMeta = selectorTitleMeta;
	}

	public boolean getSelectorDescriptionMeta() {
		return selectorDescriptionMeta;
	}

	public void setSelectorDescriptionMeta(boolean selectorDescriptionMeta) {
		this.selectorDescriptionMeta = selectorDescriptionMeta;
	}

	public boolean getSelectorContentMeta() {
		return selectorContentMeta;
	}

	public void setSelectorContentMeta(boolean selectorContentMeta) {
		this.selectorContentMeta = selectorContentMeta;
	}

	public boolean getSelectorPubDateMeta() {
		return selectorPubDateMeta;
	}

	public void setSelectorPubDateMeta(boolean selectorPubDateMeta) {
		this.selectorPubDateMeta = selectorPubDateMeta;
	}

	public void setRSS(boolean isRSS) {
		this.isRSS = isRSS;
	}

}
