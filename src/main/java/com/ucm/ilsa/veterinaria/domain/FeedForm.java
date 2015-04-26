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
	private Fiabilidad fiabilidad = Fiabilidad.Baja;
	
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
		this.url=feed.getUrlSite();
		this.urlNews=feed.getUrlNews();
		this.newsLink = feed.getNewsLink();
		this.fiabilidad = feed.getFiabilidad();
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

	public boolean isRSS() {
		return isRSS;
	}

	public void setRSS(boolean isRSS) {
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

	public Fiabilidad getFiabilidad() {
		return fiabilidad;
	}

	public void setFiabilidad(Fiabilidad fiabilidad) {
		this.fiabilidad = fiabilidad;
	}
	
	
}
