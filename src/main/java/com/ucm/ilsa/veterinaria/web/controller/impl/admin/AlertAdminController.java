package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/alerts")
public class AlertAdminController extends BaseController {
	
	private static String FOLDER = "/alerts/";
	
	@Autowired
	private AlertServiceImpl service;
	
	
	public AlertAdminController() {
		this.menu = "Alertas activas";
	}
	

}
