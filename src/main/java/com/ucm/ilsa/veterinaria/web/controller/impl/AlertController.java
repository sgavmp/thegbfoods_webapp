package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.repository.AlertRepository;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/alerts")
public class AlertController extends BaseController {
	
	public AlertController() {
		this.menu = "alerts";
	}
	
	//TODO cambiar por un servicio
	@Autowired
	private AlertRepository repository;
	
	@ModelAttribute("alerts")
	public List<Alert> getAllFeeds() {
		return repository.readAllByCheckIsFalseOrderByDatePubDesc();
	}
	
	@RequestMapping("/get/{idAlert}/check")
	public String checkAlert(Model model, @PathVariable ("idAlert") Alert alert) {
		alert.setCheck(true);
		repository.save(alert);
		putInfoMessage("Alerta revisada");
		return "redirect:/alerts";
	}
	

	@RequestMapping("**")
	public String getAllAlerts() {
		return "alerts";
	}
}
