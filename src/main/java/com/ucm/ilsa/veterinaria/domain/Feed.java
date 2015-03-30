package com.ucm.ilsa.veterinaria.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
public class Feed {
	
	@Id
	private String codeName;
	private String name;
	private String url;
	@CreatedDate
	private Date createDate;
	@LastModifiedDate
	private Date updateDate;
	@ElementCollection
	private List<String> atributes;
	@ElementCollection
	private Map<String,String> selectorHtml,selectorMeta;
	private Integer limitNews = 15; //Por defecto 15
	private String dateFormat;
	private Locale languaje;
	private boolean withRSS = false; //Por defecto no
	private String lastNewsLink;
	@OneToMany(mappedBy="site" )
	private List<Alert> listOfAlerts;
	
	//Solo sitios sin RSS
	private String urlNews;
	private String pageLink;
	private String newsSelector;
	private Integer newsPerPage;
	@ElementCollection
	private Map<String,String> selectorPagination;
	
	//Solo sitios RSS
	private boolean useSelector = false;
	private String urlRSS;
	
	public Feed() {
		this.selectorHtml = new HashMap<String, String>();
		this.selectorPagination = new HashMap<String, String>();
		this.selectorMeta = new HashMap<String, String>();
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Map<String, String> getSelectorHtml() {
		return selectorHtml;
	}
	public void setSelectorHtml(Map<String, String> selectorHtml) {
		this.selectorHtml = selectorHtml;
	}
	public Map<String, String> getSelectorPagination() {
		return selectorPagination;
	}
	public void setSelectorPagination(Map<String, String> selectorPagination) {
		this.selectorPagination = selectorPagination;
	}

	public List<String> getAtributes() {
		return atributes;
	}

	public void setAtributes(List<String> atributes) {
		this.atributes = atributes;
	}

	public Map<String, String> getSelectorMeta() {
		return selectorMeta;
	}

	public void setSelectorMeta(Map<String, String> selectorMeta) {
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

	public Integer getLimitNews() {
		return limitNews;
	}

	public void setLimitNews(Integer limitNews) {
		this.limitNews = limitNews;
	}

	public Integer getNewsPerPage() {
		return newsPerPage;
	}

	public void setNewsPerPage(Integer newsPerPage) {
		this.newsPerPage = newsPerPage;
	}

	public String getNewsSelector() {
		return newsSelector;
	}

	public void setNewsSelector(String newsSelector) {
		this.newsSelector = newsSelector;
	}

	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
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
	
}
