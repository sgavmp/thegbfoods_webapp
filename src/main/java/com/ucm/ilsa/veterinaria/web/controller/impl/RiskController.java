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

import com.google.common.collect.Lists;
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.impl.AlertDetectServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/risks")
public class RiskController extends BaseController {
	
	@Autowired
	private AlertDetectServiceImpl service;
	
	
	public RiskController() {
		this.menu = "Estadísticas de Riesgos";
	}
	
	@ModelAttribute("risks")
	public List<AlertDetect> getAllAlertsChecked() {
		return service.getAllAlertChecked();
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Risk alert) {
		model.addAttribute("alert", alert);
		return "oneAlert";
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return "alerts";
	}
	
}
