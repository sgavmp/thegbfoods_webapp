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

import sun.util.resources.CalendarData;

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
	public Map<Date,List<Alert>> getAllAlertsUnchecked() {
		return service.getAllAlertsUncheckedOrderByDate();
	}
	
	@ModelAttribute("alertsCheck")
	public Map<Date,List<Alert>> getAllAlertsChecked() {
		return service.getAllAlertsCheckedOrderByDate();
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
