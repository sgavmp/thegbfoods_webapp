package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/risks")
public class RiskController extends BaseController {
	
	private static String FOLDER = "/risks/";
	
	@Autowired
	private RiskServiceImpl service;
	
	
	public RiskController() {
		this.menu = "Otras Alertas";
	}
	
	@ModelAttribute("alertsUncheck")
	public Set<Risk> getAllRisk() {
		return service.getAllAlertActive();
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Risk alert) {
		Risk alertActive = new Risk();
		alertActive.setId(alert.getId());
		alertActive.setTitle(alert.getTitle());
		alertActive.setNewsDetect(new HashSet<NewsDetect>());
		Risk alertHistory = new Risk();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
		alertHistory.setNewsDetect(new HashSet<NewsDetect>());
		Risk alertFalse = new Risk();
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
	public String getMainRisks() {
		return FOLDER + "alerts";
	}
	
}
