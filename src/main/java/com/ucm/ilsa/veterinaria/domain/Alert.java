package com.ucm.ilsa.veterinaria.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Alert {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	@ManyToOne
	@JoinColumn(name="siteCodeName")
	private Feed site;
	private String title;
	private String link;
	private Date datePub;
	@CreatedDate
	private Date dateTratamiento;
	@Lob
	private String infoAlert;
	private String typeAlert;
	private AlertLevel level;

	public Alert() {
	
	}
	
	public Feed getSite() {
		return site;
	}
	public void setSite(Feed site) {
		this.site = site;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Date getDatePub() {
		return datePub;
	}
	public void setDatePub(Date datePub) {
		this.datePub = datePub;
	}
	public Date getDateTratamiento() {
		return dateTratamiento;
	}
	public void setDateTratamiento(Date dateTratamiento) {
		this.dateTratamiento = dateTratamiento;
	}
	public String getInfoAlert() {
		return infoAlert;
	}
	public void setInfoAlert(String infoAlert) {
		this.infoAlert = infoAlert;
	}
	public String getTypeAlert() {
		return typeAlert;
	}
	public void setTypeAlert(String typeAlert) {
		this.typeAlert = typeAlert;
	}
	public AlertLevel getLevel() {
		return level;
	}
	public void setLevel(AlertLevel level) {
		this.level = level;
	}

}
