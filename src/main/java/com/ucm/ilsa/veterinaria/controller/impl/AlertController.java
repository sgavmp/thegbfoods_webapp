package com.ucm.ilsa.veterinaria.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.controller.BaseController;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;

@Controller
public class AlertController extends BaseController {
	
	//TODO cambiar por un servicio
	@Autowired
	private AlertRepository repository;
	
	@ModelAttribute("alerts")
	public List<Alert> getAllFeeds() {
		return repository.readAllByCheckIsFalseOrderByDatePubDesc();
	}
	
	@RequestMapping(value = {"/alerts/{idAlert}/check"})
	public String checkAlert(Model model, @PathVariable ("idAlert") Alert alert) {
		alert.setCheck(true);
		repository.save(alert);
		return "redirect:/alerts";
	}
	

	@RequestMapping(value = {"","/alerts"})
	public String getAllAlerts() {
		return "alerts";
	}
}
