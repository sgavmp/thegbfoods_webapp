package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import com.google.common.collect.Lists;

public class FeedForm {
	@NotBlank
	@NotEmpty
	private String name;
	private String dateFormat;
	//Por defecto Spanish
	private Language languaje = Language.SPANISH;
	private boolean isRSS = false;
	private boolean isAuto = false;
	private WebLevel type = WebLevel.yellow;
	private boolean actived = true;
	private Integer minRefresh = 120;
	private String selectorTitle;
	private String selectorContent;
	private String selectorPubDate;
	private boolean selectorTitleMeta;
	private boolean selectorContentMeta;
	private boolean selectorPubDateMeta;
	private FeedTypeEnum feedType;
	private List<FeedPlaceEnum> feedPlace;
	private CharsetEnum charSet = CharsetEnum.UTF8;
	private ExtractionType extractionType = ExtractionType.ARTICLE_EXTRACTOR;
	
	@URL
	@NotEmpty
	private String urlNews;//Url de la pagina de noticias o de rss
	private String newsLink;
	
	public FeedForm() {
		this.feedPlace=Lists.newArrayList(FeedPlaceEnum.general);
	}
	
	public FeedForm(Feed feed) {
		this.name=feed.getName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.urlNews=feed.getUrlNews();
		this.newsLink = feed.getNewsLink();
		this.type = feed.getType();
		this.isRSS = feed.isRSS();
		this.minRefresh = feed.getMinRefresh();
		this.selectorContent = feed.getSelectorContent();
		this.selectorContentMeta = feed.getSelectorContentMeta();
		this.selectorTitle = feed.getSelectorTitle();
		this.selectorTitleMeta = feed.getSelectorTitleMeta();
		this.selectorPubDate = feed.getSelectorPubDate();
		this.selectorPubDateMeta = feed.getSelectorPubDateMeta();
		this.actived = feed.isActived();
		this.charSet = feed.getCharSet();
		this.feedPlace = feed.getFeedPlace();
		this.feedType = feed.getFeedType();
		this.extractionType = feed.getExtractionType();
		this.isAuto = feed.getIsAuto();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public CharsetEnum getCharSet() {
		return charSet;
	}

	public void setCharSet(CharsetEnum charSet) {
		this.charSet = charSet;
	}

	public FeedTypeEnum getFeedType() {
		return feedType;
	}

	public void setFeedType(FeedTypeEnum feedType) {
		this.feedType = feedType;
	}

	public List<FeedPlaceEnum> getFeedPlace() {
		return feedPlace;
	}

	public void setFeedPlace(List<FeedPlaceEnum> feedPlace) {
		this.feedPlace = feedPlace;
	}

	public ExtractionType getExtractionType() {
		return extractionType;
	}

	public void setExtractionType(ExtractionType extractionType) {
		this.extractionType = extractionType;
	}

	public boolean getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}
	
	
	
}
