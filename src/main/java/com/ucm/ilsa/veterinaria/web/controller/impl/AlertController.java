package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.impl.AlertDetectServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/alerts")
public class AlertController extends BaseController {
	
	@Autowired
	private AlertDetectServiceImpl service;
	
	
	public AlertController() {
		this.menu = "Alertas activas";
	}
	
	@ModelAttribute("alertsUncheck")
	public List<AlertDetect> getAllAlertsUnchecked() {
		return service.getAllAlertUnchecked();
	}
	
	@ModelAttribute("alertsCheck")
	public List<AlertDetect> getAllAlertsChecked() {
		return service.getAllAlertChecked();
	}
	
	@RequestMapping("/get/{idAlert}/check")
	public String checkAlert(Model model, @PathVariable ("idAlert") AlertDetect alert) {
		service.checkAlert(alert);
		putInfoMessage("Alerta revisada");
		return "redirect:/alerts";
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return "alerts";
	}
	
}
