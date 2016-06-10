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
public class ScrapStatistics {

	@Id
	@Column(name = "FECHA")
	private Date date;
	@Column(name = "TOTAL")
	private Integer total = 0;

	public ScrapStatistics() {

	}

	public ScrapStatistics(Date date) {
		this.date = date;
	}

	public ScrapStatistics(Date date, Integer total) {
		this.date = date;
		this.total = total;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}