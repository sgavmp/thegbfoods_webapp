package com.ucm.ilsa.veterinaria.controller.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ucm.ilsa.veterinaria.controller.BaseController;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.service.FeedService;

@Controller
public class FeedController extends BaseController {
	@Autowired
	private FeedService serviceFeed;
	
	
	@ModelAttribute("feeds")
	public List<Feed> getAllFeeds() {
		return serviceFeed.getAllFeed();
	}

	@RequestMapping("/feeds")
	public String getAllAlerts() {
		return "feeds";
	}
	
	@RequestMapping("/feed/{codeName}/actualizar")
	public String updateNewsByFeed(Model model, @PathVariable ("codeName") Feed feed) {
		List<News> newsListAdd = serviceFeed.createOrUpdate(feed);
		model.addAttribute(feed);
		model.addAttribute(newsListAdd);
		return "newsFeed";
	}
	
	@RequestMapping("/feed/{codeName}")
	public String getAllNewsByFeed(Model model, @PathVariable ("codeName") Feed feed) {
		model.addAttribute(feed);
		return "newsFeed";
	}
	
}
