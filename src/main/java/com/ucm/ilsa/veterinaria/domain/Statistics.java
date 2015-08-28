package com.ucm.ilsa.veterinaria.domain;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.exolab.castor.types.DateTime;

@Entity
public class Statistics extends BaseEntity {
	
	@Id
	private Date date;
	private Integer numNews = 0;
	private Integer numAlerts = 0;
	
	public Statistics() {
		
	}
	
	public Statistics(Date date) {
		this.date = date;
	}
	
	public Statistics(Date date, Integer alertDetectNum, Integer newsNum) {
		this.date = date;
		this.numAlerts = alertDetectNum;
		this.numNews = newsNum;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getNumNews() {
		return numNews;
	}
	public void setNumNews(Integer numNews) {
		this.numNews = numNews;
	}
	public Integer getNumAlerts() {
		return numAlerts;
	}
	public void setNumAlerts(Integer numAlerts) {
		this.numAlerts = numAlerts;
	}
	
	public void increment(Integer numAlerts, Integer numNews) {
		this.numAlerts+=numAlerts;
		this.numNews+=numNews;
	}
}
