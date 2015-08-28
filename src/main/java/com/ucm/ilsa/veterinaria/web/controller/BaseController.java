package com.ucm.ilsa.veterinaria.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.UserDetailsImpl;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.util.MD5Util;

public abstract class BaseController {
	
	private static long ONE_DAY = 86400000;
	
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	private static List<String> info = new ArrayList<String>();
	private static List<String> error = new ArrayList<String>();
	//Indica al template el menu que tiene que activar
	protected String menu="";
	
	
	@ModelAttribute("hoy")
	public Date getHoy() {
		return new Date();
	}
	
	@ModelAttribute("ayer")
	public Date getAyer() {
		Date ayer = new Date(new Date().getTime() - 24 * 3600 * 1000l );
		return ayer;
	}
	
	@ModelAttribute("avatar")
	public String getAvatarHash(@AuthenticationPrincipal UserDetailsImpl activeUser) {
		if (activeUser!= null)
			return "http://www.gravatar.com/avatar/" + MD5Util.md5Hex(activeUser.getEmail());
		else
			return "";
	}
	
	@ModelAttribute("menu")
	public String getMenuActive() {
		return menu;
	}
	
	@ModelAttribute("info")
	public List<String> getInfoMessage() {
		List<String> copy = Lists.newArrayList(info);
		info.clear();
		return copy;
	}
	
	@ModelAttribute("error")
	public List<String> getErrorMessage() {
		List<String> copy = Lists.newArrayList(error);
		error.clear();
		return copy;
	}
	
	@ModelAttribute("stat")
	public Statistics getStatisticsToday() {
		Statistics statistics = statisticsRepository.findOne(new java.sql.Date(System.currentTimeMillis()));
		if (statistics==null) {
			statistics = new Statistics(new java.sql.Date(System.currentTimeMillis()), 0, 0);
			statisticsRepository.save(statistics);
		}
		return statistics;
	}
	
	@ModelAttribute("graph")
	public List<Statistics> getGraphStat() {
		List<Statistics> graph = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		for (int i=0;i<7;i++) {
			java.sql.Date current = new java.sql.Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics==null) {
				statistics = new Statistics(current);
			}
			graph.add(statistics);
		}
		return graph;
	}
	
	public BaseController() {
		this.info = new ArrayList<String>();
		this.error = new ArrayList<String>();
	}
	
	public static void putInfoMessage(String message) {
		info.add(message);
	}
	
	public static void putErrorMessage(String message) {
		error.add(message);
	}

}
