package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/alerts")
public class AlertController extends BaseController {
	
	private static String FOLDER = "/alerts/";
	
	@Autowired
	private AlertServiceImpl service;
	
	public AlertController() {
		this.menu = "Alertas Sanitarias Activas";
	}
	
	@ModelAttribute("alertsUncheck")
	public Set<Alert> getAllAlertsUnchecked() {
		return service.getAllAlertActive();
	}
	
	@ModelAttribute("alertsCheck")
	public Set<Alert> getAllAlertsChecked() {
		return service.getAllAlertWithHistory();
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Alert alert) {
		Alert alertActive = new Alert();
		alertActive.setId(alert.getId());
		alertActive.setTitle(alert.getTitle());
		alertActive.setNewsDetect(new HashSet<NewsDetect>());
		Alert alertHistory = new Alert();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
		alertHistory.setNewsDetect(new HashSet<NewsDetect>());
		Alert alertFalse = new Alert();
		alertFalse.setId(alert.getId());
		alertFalse.setTitle(alert.getTitle());
		alertFalse.setNewsDetect(new HashSet<NewsDetect>());
		for (NewsDetect news : alert.getNewsDetect()) {
			if (news.getFalPositive()) {
				alertFalse.getNewsDetect().add(news);
			} else if (news.getHistory()) {
				alertHistory.getNewsDetect().add(news);
			} else {
				alertActive.getNewsDetect().add(news);
			}
		}
		model.addAttribute("alert", alert);
		model.addAttribute("alertActive", alertActive);
		model.addAttribute("alertHistory", alertHistory);
		model.addAttribute("alertFalse", alertFalse);
		return FOLDER + "oneAlert";
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return FOLDER + "alerts";
	}
	
}
