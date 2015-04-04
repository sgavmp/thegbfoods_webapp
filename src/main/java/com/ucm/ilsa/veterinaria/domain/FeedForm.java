package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FeedForm {
	@NotBlank
	//@Pattern(regexp = "^[a-zA-Z ]+$")
	private String name;
	@NotEmpty
	@Pattern(regexp = "^[a-z]{3,10}$",message="Codigo de 3 a 10 letras en minusculas sin espacios.")
	private String codeName;
	@URL
	@NotEmpty
	private String url;
	private List<PairValues> selectorHtml,selectorMeta;
	private String dateFormat;
	private Locale languaje;
	private boolean withRSS; //Por defecto no
	
	//Solo sitios sin RSS
	@URL
	private String urlNews;
	private String newsLink;
	
	//Solo sitios RSS
	private boolean useSelector = false;
	@URL
	private String urlRSS;
	
	public FeedForm() {
		this.selectorHtml = new ArrayList<PairValues>();
		this.selectorMeta = new ArrayList<PairValues>();
	}
	
	public FeedForm(Feed feed) {
		this.name=feed.getName();
		this.codeName = feed.getCodeName();
		this.dateFormat=feed.getDateFormat();
		this.languaje=feed.getLanguaje();
		this.newsLink=feed.getNewsLink();
		this.selectorHtml=feed.getSelectorHtml();
		this.selectorMeta=feed.getSelectorMeta();
		this.url=feed.getUrl();
		this.urlNews=feed.getUrlNews();
		this.urlRSS=feed.getUrlRSS();
		this.useSelector=feed.isUseSelector();
		this.withRSS=feed.isWithRSS();
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

	public Locale getLanguaje() {
		return languaje;
	}

	public void setLanguaje(Locale languaje) {
		this.languaje = languaje;
	}

	public boolean isWithRSS() {
		return withRSS;
	}

	public void setWithRSS(boolean withRSS) {
		this.withRSS = withRSS;
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

	public boolean isUseSelector() {
		return useSelector;
	}

	public void setUseSelector(boolean useSelector) {
		this.useSelector = useSelector;
	}

	public String getUrlRSS() {
		return urlRSS;
	}

	public void setUrlRSS(String urlRSS) {
		this.urlRSS = urlRSS;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}
