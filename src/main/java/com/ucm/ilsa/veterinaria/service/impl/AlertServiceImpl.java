package com.ucm.ilsa.veterinaria.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;

@Service
public class AlertServiceImpl {

	@Autowired
	private AlertRepository repository;
	
	public List<Alert> getAllAlertUnchecked() {
		return repository.readAllByCheckIsFalseOrderByDatePubDesc();
	}
	
	public List<Alert> getAllAlertUncheckedByFeed(Feed feed) {
		return repository.findAllByCheckIsFalseAndSiteOrderByDatePubDesc(feed);
	}
	
	public List<Alert> getAllAlertChecked() {
		return repository.readAllByCheckIsTrueOrderByDatePubDesc();
	}
	
	public List<Alert> getAllAlertCheckedByFeed(Feed feed) {
		return repository.findAllByCheckIsTrueAndSiteOrderByDatePubDesc(feed);
	}
	
	public SortedMap<Date,List<Alert>> getAllAlertsUncheckedOrderByDate() {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.readAllByCheckIsFalseOrderByDatePubDesc()) {
			Date pubDate = alert.getDatePub();
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
	
	public SortedMap<Date,List<Alert>> getAllAlertsUncheckedByFeedOrderByDate(Feed feed) {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.findAllByCheckIsFalseAndSiteOrderByDatePubDesc(feed)) {
			Date pubDate = alert.getDatePub();
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
	
	public SortedMap<Date,List<Alert>> getAllAlertsCheckedOrderByDate() {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.readAllByCheckIsTrueOrderByDatePubDesc()) {
			Date pubDate = alert.getDatePub();
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
	
	public SortedMap<Date,List<Alert>> getAllAlertsCheckedOrderByFeedByDate(Feed feed) {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.findAllByCheckIsTrueAndSiteOrderByDatePubDesc(feed)) {
			Date pubDate = alert.getDatePub();
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
	
	public void checkAlert(Alert alert) {
		alert.setCheck(true);
		repository.save(alert);
	}
}
