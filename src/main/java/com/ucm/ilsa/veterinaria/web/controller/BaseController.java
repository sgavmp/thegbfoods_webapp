package com.ucm.ilsa.veterinaria.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;

public abstract class BaseController {
	
	private List<String> info;
	private List<String> error;
	//Indica al template el menu que tiene que activar
	protected String menu="";
	
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
	
	public BaseController() {
		this.info = new ArrayList<String>();
		this.error = new ArrayList<String>();
	}
	
	protected void putInfoMessage(String message) {
		this.info.add(message);
	}
	
	protected void putErrorMessage(String message) {
		this.error.add(message);
	}

}
