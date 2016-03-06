package com.ucm.ilsa.veterinaria.domain;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.exolab.castor.types.DateTime;

@Entity
@Table(name = "ESTADISTICAS")
public class Statistics {

	@Id
	@Column(name = "FECHA")
	private Date date;
	@Column(name = "TOTAL")
	private Integer numNews = 0;
	@Column(name = "ALERTA")
	private Integer numAlerts = 0;
	@Column(name = "RIESGO")
	private Integer numRisks = 0;

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

	public Integer getNumRisks() {
		return numRisks;
	}

	public void setNumRisks(Integer numRisks) {
		this.numRisks = numRisks;
	}
	
	
}
