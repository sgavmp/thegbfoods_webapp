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
import com.ucm.ilsa.veterinaria.repository.AlertRepository;

@Service
public class AlertServiceImpl {
	
	@Autowired
	private AlertRepository repository;
	
	public List<Alert> getAllAlert() {
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Alert getOneById(Long word) {
		return repository.findOne(word);
	}
	
	public Alert update(Alert word) {
		return repository.save(word);
	}
	
	public Alert create(Alert word) {
		return repository.save(word);
	}
	
	public void remove(Alert word) {
		repository.delete(word);
	}
	
	public void remove(Long word) {
		repository.delete(word);
	}

	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<Alert>> getAllAlertsOrderByDate() {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.findAll()) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<Alert> alertas = new ArrayList<Alert>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<Alert>> getAllAlertsByFeedOrderByDate(Feed feed) {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.readAllDistinctByNewsDetectSite(feed)) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<Alert> alertas = new ArrayList<Alert>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	public List<Alert> getAlertDetectActivatedAfter(Date date) {
		return repository.readAllDistinctByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(date);
	}
	
	public List<Alert> getAlertDetectSite(Feed feed) {
		return repository.readAllDistinctByNewsDetectSite(feed);
	}
	
	public List<Alert> getAllAlertActive() {
		return repository.readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	}
	
	public List<Alert> getAllAlertWithHistory() {
		return repository.readAllDistinctByNewsDetectHistoryTrue();
	}
	
	public List<Alert> getAllAlertWithFalsePositive() {
		return repository.readAllDistinctByNewsDetectFalPositiveTrue();
	}
	
}