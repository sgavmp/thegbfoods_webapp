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
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.repository.AlertDetectRepository;

@Service
public class AlertDetectServiceImpl {

	@Autowired
	private AlertDetectRepository repository;
	
	public List<AlertDetect> getAllAlertUnchecked() {
		return repository.readAllByCheckIsFalseOrderByCreateDateDesc();
	}
	
	public List<AlertDetect> getAllAlertChecked() {
		return repository.readAllByCheckIsTrueOrderByCreateDateDesc();
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<AlertDetect>> getAllAlertsUncheckedOrderByDate() {
		SortedMap<Date,List<AlertDetect>> alertasPorFecha = new TreeMap<Date, List<AlertDetect>>(Collections.reverseOrder());
		for (AlertDetect alert : repository.readAllByCheckIsFalseOrderByCreateDateDesc()) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<AlertDetect> alertas = new ArrayList<AlertDetect>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<AlertDetect>> getAllAlertsUncheckedByFeedOrderByDate(Feed feed) {
		SortedMap<Date,List<AlertDetect>> alertasPorFecha = new TreeMap<Date, List<AlertDetect>>(Collections.reverseOrder());
		for (AlertDetect alert : repository.readAllByCheckIsFalseOrderByCreateDateDesc()) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<AlertDetect> alertas = new ArrayList<AlertDetect>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<AlertDetect>> getAllAlertsCheckedOrderByDate() {
		SortedMap<Date,List<AlertDetect>> alertasPorFecha = new TreeMap<Date, List<AlertDetect>>(Collections.reverseOrder());
		for (AlertDetect alert : repository.readAllByCheckIsTrueOrderByCreateDateDesc()) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<AlertDetect> alertas = new ArrayList<AlertDetect>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<AlertDetect>> getAllAlertsCheckedOrderByFeedByDate(Feed feed) {
		SortedMap<Date,List<AlertDetect>> alertasPorFecha = new TreeMap<Date, List<AlertDetect>>(Collections.reverseOrder());
		for (AlertDetect alert : repository.readAllByCheckIsTrueOrderByCreateDateDesc()) {
			Date pubDate = alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<AlertDetect> alertas = new ArrayList<AlertDetect>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	public AlertDetect getAlertDetectActiveByAlert(Alert alert) {
		return repository.findByCheckIsFalseAndAlertOrderByCreateDateDesc(alert);
	}
	
	public void checkAlert(AlertDetect alert) {
		alert.setCheck(true);
		repository.save(alert);
	}
}
