package com.ucm.ilsa.veterinaria.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;

public abstract class BaseController {
	
	private List<String> info;
	private List<String> error;
	
	@ModelAttribute("info")
	public List<String> getInfoMessage() {
		List<String> copy = Lists.newArrayList(info);
		info.clear();
		copy.add("Prueba");
		copy.add("Prueba");
		return copy;
	}
	
	@ModelAttribute("error")
	public List<String> getErrorMessage() {
		List<String> copy = Lists.newArrayList(error);
		error.clear();
		copy.add("Prueba");
		copy.add("Prueba");
		return copy;
	}
	
	public BaseController() {
		this.info = new ArrayList<String>();
		this.error = new ArrayList<String>();
	}

}
