package com.ucm.ilsa.veterinaria.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AlertAbstract extends BaseEntity {

	@Id @GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	protected String title;
	@NotNull
	@Lob
	private String words;
	@OneToMany(mappedBy = "alertDetect", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	protected List<NewsDetect> newsDetect;

	public AlertAbstract() {
		this.words = "";
	}

	public AlertAbstract(String word) {
		this.words = word;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String word) {
		this.words = word;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
	public int numNewsDetectInDate(int day, int month, int year) {
		Date start = new Date(year-1900,month-1,day);
		Date end = new Date(year-1900,month-1,day+1);
		int count = 0;
		for (NewsDetect news : newsDetect) {
			if (news.getDatePub().before(end) && ( news.getDatePub().after(start) || news.getDatePub().equals(start)))
				count++;
		}
		return count;
	}
	
	public int numNewsDetectInDateToday() {
		Date today = new Date(System.currentTimeMillis());
		Date start = new Date(today.getYear(),today.getMonth(),today.getDate());
		Date end = new Date(today.getYear(),today.getMonth(),today.getDate()+1);
		int count = 0;
		for (NewsDetect news : newsDetect) {
			if (news.getDatePub().before(end) && ( news.getDatePub().after(start) || news.getDatePub().compareTo(start)==0))
				count++;
		}
		return count;
	}
	
}
