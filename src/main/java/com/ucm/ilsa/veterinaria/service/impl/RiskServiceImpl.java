package com.ucm.ilsa.veterinaria.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;
import com.ucm.ilsa.veterinaria.repository.RiskRepository;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

@Service
public class RiskServiceImpl {
	
	@Autowired
	private RiskRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private LocationRepository locationRepository;
	
	
	public Set<Risk> getAllAlert() {
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Risk getOneById(Long word) {
		return repository.findOne(word);
	}
	
	public Risk update(Risk word) {
		return repository.save(word);
	}
	
	public Risk create(Risk word) throws IOException {
		Risk risk = repository.save(word);
		risk.setNewsDetect(new HashSet<NewsDetect>());
		newsIndexService.resetAlert(risk);
		return risk;
	}
	
	public void remove(Risk word) {
		repository.delete(word);
	}
	
	public void remove(Long word) {
		repository.delete(word);
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<Risk>> getAllAlertsOrderByDate() {
		SortedMap<Date,List<Risk>> alertasPorFecha = new TreeMap<Date, List<Risk>>(Collections.reverseOrder());
		for (Risk alert : repository.findAll()) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<Risk> alertas = new ArrayList<Risk>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<Risk>> getAllAlertsByFeedOrderByDate(Feed feed) {
		SortedMap<Date,List<Risk>> alertasPorFecha = new TreeMap<Date, List<Risk>>(Collections.reverseOrder());
		for (Risk alert : repository.readAllDistinctByNewsDetectSite(feed)) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<Risk> alertas = new ArrayList<Risk>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	public Set<Risk> getAlertDetectActivatedAfter(Date date) {
		return repository.readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(date);
	}
	
	public Set<Risk> getAlertDetectSite(Feed feed) {
		return repository.readAllDistinctByNewsDetectSite(feed);
	}
	
	public Set<Risk> getAllAlertActive() {
		return repository.readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	}
	
	public Set<Risk> getAllAlertWithHistory() {
		return repository.readAllDistinctByNewsDetectHistoryTrue();
	}
	
	public Set<Risk> getAllAlertWithFalsePositive() {
		return repository.readAllDistinctByNewsDetectFalPositiveTrue();
	}

	public Risk setNewsLocation(Risk alert) {
		for (NewsDetect news : alert.getNewsDetect()) {
			news.setLocation(locationRepository.findAllByNews(news.getLink()));
		}
		return alert;
	}
}
