package com.ucm.ilsa.veterinaria.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.repository.RiskRepository;

@Service
public class RiskServiceImpl {
	
	@Autowired
	private RiskRepository repository;
	
	public List<Risk> getAllAlert() {
		return Lists.newArrayList(repository.findAll());
	}
	
	public Risk getOneById(String word) {
		return repository.findOne(word);
	}
	
	public Risk update(Risk word) {
		return repository.save(word);
	}
	
	public Risk create(Risk word) {
		return repository.save(word);
	}
	
	public void remove(Risk word) {
		repository.delete(word);
	}
	
	public void remove(String word) {
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
	
	public List<Risk> getAlertDetectActivatedAfter(Date date) {
		return repository.readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(date);
	}
	
	public List<Risk> getAlertDetectSite(Feed feed) {
		return repository.readAllDistinctByNewsDetectSite(feed);
	}
	
	public List<Risk> getAllAlertActive() {
		return repository.readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	}
	
	public List<Risk> getAllAlertWithHistory() {
		return repository.readAllDistinctByNewsDetectHistoryTrue();
	}
	
	public List<Risk> getAllAlertWithFalsePositive() {
		return repository.readAllDistinctByNewsDetectFalPositiveTrue();
	}

}
