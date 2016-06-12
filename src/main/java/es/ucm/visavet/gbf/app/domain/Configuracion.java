package es.ucm.visavet.gbf.app.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Configuracion {
	@Id
	private String id = "conf";
//	@Lob
//	private String palabrasAlerta = "";
//	@Lob
//	private String paabrasLugar = "(^|\\s|\\,|\\.|\\-)(\\s*)(at|in|on|of|from|and|the|en|de)*(\\s)(?-i)([A-Z]\\w+)";
	private String pathIndexClavin;
	private String pathIndexNews;
//	private boolean usarPalabrasAlerta = false;
//	private boolean usarPalabrasLugar = false;
	private Double radiusNear = (double) 10;
	private Integer dayRisks = 10;
	@Transient
	private Feed feed;
	@Transient
	private String url;
	@Transient
	private boolean testReg = true;
	private boolean runService = false;
	private boolean runSearch = false;
	private boolean runClavin = false;
	
	public Configuracion() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getRadiusNear() {
		return radiusNear;
	}
	public void setRadiusNear(Double radiusNear) {
		this.radiusNear = radiusNear;
	}
	public Integer getDayRisks() {
		return dayRisks;
	}
	public void setDayRisks(Integer dayRisks) {
		this.dayRisks = dayRisks;
	}
	public Feed getFeed() {
		return feed;
	}
	public void setFeed(Feed feed) {
		this.feed = feed;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isTestReg() {
		return testReg;
	}
	public boolean getTestReg() {
		return testReg;
	}
	public void setTestReg(boolean testReg) {
		this.testReg = testReg;
	}
	public String getPathIndexClavin() {
		return pathIndexClavin;
	}
	public void setPathIndexClavin(String pathIndexClavin) {
		this.pathIndexClavin = pathIndexClavin;
	}
	public String getPathIndexNews() {
		return pathIndexNews;
	}
	public void setPathIndexNews(String pathIndexNews) {
		this.pathIndexNews = pathIndexNews;
	}
	public boolean isRunService() {
		return runService;
	}
	public void setRunService(boolean runService) {
		this.runService = runService;
	}
	public boolean getRunService() {
		return runService;
	}
	public boolean isRunClavin() {
		return runClavin;
	}
	public boolean getRunClavin() {
		return runClavin;
	}
	public void setRunClavin(boolean runClavin) {
		this.runClavin = runClavin;
	}
	public boolean isRunSearch() {
		return runSearch;
	}
	public void setRunSearch(boolean runSearch) {
		this.runSearch = runSearch;
	}
	
}
