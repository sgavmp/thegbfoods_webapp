package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.FeedRisk;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.service.FeedRiskService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/risks/feeds")
public class FeedRiskController extends BaseController {
	
	public FeedRiskController() {
		this.menu="Sitios de Riesgos Futuros";
	}
	
	@Autowired
	private FeedRiskService serviceFeed;
	@Autowired
	private RiskServiceImpl serviceAlert;
	
	
	@ModelAttribute("feeds")
	public List<FeedRisk> getAllFeeds() {
		return serviceFeed.getAllFeed();
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return "feeds";
	}
	
	@RequestMapping("/get/{codeName}")
	public String getAllNewsByFeed(Model model, @PathVariable ("codeName") FeedRisk feed) {
		model.addAttribute(feed);
		List<Risk> alertas = serviceAlert.getAllAlertActive();
		for (Risk alerta : alertas) {
			Iterator<NewsDetect> iterator = alerta.getNewsDetect().iterator();
			while(iterator.hasNext()) {
				NewsDetect news = iterator.next();
				if (!news.getSite().equals(feed)) {
					iterator.remove();
				}
			}
		}
		model.addAttribute("alertas", alertas);
		return "oneFeed";
	}
	
}
