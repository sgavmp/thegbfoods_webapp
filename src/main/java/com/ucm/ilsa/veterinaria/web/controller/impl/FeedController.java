package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.impl.AlertDetectServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/feeds")
public class FeedController extends BaseController {
	
	public FeedController() {
		this.menu="feeds";
	}
	
	@Autowired
	private FeedService serviceFeed;
	@Autowired
	private AlertDetectServiceImpl serviceAlert;
	
	
	@ModelAttribute("feeds")
	public List<Feed> getAllFeeds() {
		return serviceFeed.getAllFeed();
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return "feeds";
	}
	
	@RequestMapping("/get/{codeName}")
	public String getAllNewsByFeed(Model model, @PathVariable ("codeName") Feed feed) {
		model.addAttribute(feed);
		model.addAttribute("alertsCheck", serviceAlert.getAllAlertsCheckedOrderByFeedByDate(feed));
		model.addAttribute("alertsUncheck", serviceAlert.getAllAlertsUncheckedByFeedOrderByDate(feed));
		return "oneFeed";
	}
	
}
