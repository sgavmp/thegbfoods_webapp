package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/alerts")
public class AlertController extends BaseController {
	
	private static String FOLDER = "/alerts/";
	
	@Autowired
	private AlertServiceImpl service;
	
	public AlertController() {
		this.menu = "Alertas activas";
	}
	
	@ModelAttribute("alertsUncheck")
	public List<Alert> getAllAlertsUnchecked() {
		return service.getAllAlertActive();
	}
	
	@ModelAttribute("alertsCheck")
	public List<Alert> getAllAlertsChecked() {
		return service.getAllAlertWithHistory();
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Alert alert) {
		model.addAttribute("alert", alert);
		return FOLDER + "oneAlert";
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return FOLDER + "alerts";
	}
	
}
