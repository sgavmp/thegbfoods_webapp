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
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/alerts")
public class AlertController extends BaseController {
	
	public AlertController() {
		this.menu = "alerts";
	}
	
	@Autowired
	private AlertServiceImpl service;
	
	@ModelAttribute("alertsUncheck")
	public List<Alert> getAllAlertsUnchecked() {
		return service.getAllAlertUnchecked();
	}
	
	@ModelAttribute("alertsCheck")
	public List<Alert> getAllAlertsChecked() {
		return service.getAllAlertChecked();
	}
	
	@RequestMapping("/get/{idAlert}/check")
	public String checkAlert(Model model, @PathVariable ("idAlert") Alert alert) {
		service.checkAlert(alert);
		putInfoMessage("Alerta revisada");
		return "redirect:/alerts";
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return "alerts";
	}
}
