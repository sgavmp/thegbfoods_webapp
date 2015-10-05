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
	private String dateFormat;
	//Por defecto Spanish
	private Language languaje = Language.SPANISH;
	private boolean isRSS = true;
	private WebLevel type = WebLevel.yellow;
	private boolean actived = false;
	private boolean accepted = true;
	private Integer minRefresh = 120;
	private String selectorTitle;
	private String selectorDescription;
	private String selectorContent;
	private String selectorPubDate;
	private boolean selectorTitleMeta;
	private boolean selectorDescriptionMeta;
	private boolean selectorContentMeta;
	private boolean selectorPubDateMeta;
	private CharsetEnum charSet = CharsetEnum.UTF8;
	
	@URL
	private String urlNews;//Url de la pagina de noticias o de rss
	
	private List<String> urlPages;
	private String newsLink;
	
	public FeedForm() {
		this.urlPages = new ArrayList<String>();
	}
	
	public FeedForm(SiteAbstract feed) {
		this.name=feed.getName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
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
		this.accepted = feed.isAccepted();
		this.actived = feed.isActived();
		this.charSet = feed.getCharSet();
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

	public boolean isActived() {
		return actived;
	}
	
	public boolean getActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public boolean isAccepted() {
		return accepted;
	}
	
	public boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public CharsetEnum getCharSet() {
		return charSet;
	}

	public void setCharSet(CharsetEnum charSet) {
		this.charSet = charSet;
	}

}
