package com.ucm.ilsa.veterinaria.service.impl;

import java.util.List;

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
	
	public void checkAlert(Alert alert) {
		alert.setCheck(true);
		repository.save(alert);
	}
}
