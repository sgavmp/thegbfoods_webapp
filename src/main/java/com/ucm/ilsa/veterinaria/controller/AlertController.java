package com.ucm.ilsa.veterinaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;

@Controller
public class AlertController {
	
	//TODO cambiar por un servicio
	@Autowired
	private AlertRepository repository;
	
	@ModelAttribute("alerts")
	public List<Alert> getAllFeeds() {
		return Lists.newArrayList(repository.findAll());
	}
	

	@RequestMapping(value = {"","/alerts"})
	public String getAllAlerts() {
		return "alerts";
	}
}
