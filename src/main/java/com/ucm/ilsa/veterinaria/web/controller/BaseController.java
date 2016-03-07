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
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.UserDetailsImpl;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.util.MD5Util;

public abstract class BaseController {
	
	//Indica al template el menu que tiene que activar
	protected String menu="";
	
	
	@ModelAttribute("hoy")
	public Date getHoy() {
		return new Date();
	}
	
	@ModelAttribute("semana")
	public Date getSemana() {
		Date ayer = new Date(new Date().getTime() - 7 * 24 * 3600 * 1000l  );
		return ayer;
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

}
