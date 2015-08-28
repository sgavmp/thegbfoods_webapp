package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class AlertDetect extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "alertId")
	private Alert alert;
	@OneToMany(mappedBy="alert_detect",fetch = FetchType.EAGER)
	private List<NewsDetect> newsDetect;
	@Enumerated(EnumType.ORDINAL)
	private AlertLevel level = AlertLevel.yellow;
	@Column(name = "isCheck")
	private boolean check;

	public AlertDetect() {
		
	}

	public AlertLevel getLevel() {
		return level;
	}

	public void setLevel(AlertLevel level) {
		this.level = level;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public List<NewsDetect> getNewsDetect() {
		return newsDetect;
	}

	public void setNewsDetect(List<NewsDetect> newsDetect) {
		this.newsDetect = newsDetect;
	}
	
	public SortedMap<Date,List<NewsDetect>> getAllNewsDetectOrderByDate() {
		SortedMap<Date,List<NewsDetect>> alertasPorFecha = new TreeMap<Date, List<NewsDetect>>(Collections.reverseOrder());
		for (NewsDetect alert : newsDetect) {
			Date pubDate = alert.getDatePub();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<NewsDetect> alertas = new ArrayList<NewsDetect>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
}
