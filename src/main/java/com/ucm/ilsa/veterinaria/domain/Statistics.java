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
	private Date fecha;
	@Column(name = "TOTAL")
	private Integer numNews = 0;
	@Column(name = "ALERTA")
	private Integer alertas = 0;
	@Column(name = "RIESGO")
	private Integer riesgos = 0;
	@Column(name = "NOTICIAS")
	private Integer noticias = 0;

	public Statistics() {

	}

	public Statistics(Date date) {
		this.fecha = date;
	}

	public Statistics(Date date, Integer alertDetectNum, Integer newsNum) {
		this.fecha = date;
		this.alertas = alertDetectNum;
		this.numNews = newsNum;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getAlertas() {
		return alertas;
	}

	public void setAlertas(Integer alertas) {
		this.alertas = alertas;
	}

	public Integer getRiesgos() {
		return riesgos;
	}

	public void setRiesgos(Integer riesgos) {
		this.riesgos = riesgos;
	}

	public Integer getNumNews() {
		return numNews;
	}

	public void setNumNews(Integer numNews) {
		this.numNews = numNews;
	}

	

	public Integer getNoticias() {
		return noticias;
	}

	public void setNoticias(Integer noticias) {
		this.noticias = noticias;
	}
	
	
}
