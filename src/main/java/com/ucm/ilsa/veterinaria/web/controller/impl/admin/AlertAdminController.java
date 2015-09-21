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
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.service.impl.AlertDetectServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/alerts")
public class AlertAdminController extends BaseController {
	
	@Autowired
	private AlertDetectServiceImpl service;
	
	
	public AlertAdminController() {
		this.menu = "Alertas activas";
	}
	
	@RequestMapping("/get/{idAlert}/check")
	public String checkAlert(Model model, RedirectAttributes redirectAttributes, @PathVariable ("idAlert") AlertDetect alert) {
		service.checkAlert(alert);
		redirectAttributes.addFlashAttribute("info","Alerta revisada");
		return "redirect:/alerts";
	}
	
	@RequestMapping("/get/{idAlert}/remove")
	public String removeAlert(Model model, RedirectAttributes redirectAttributes, @PathVariable ("idAlert") AlertDetect alert) {
		if (service.removeAlert(alert)) {
			redirectAttributes.addFlashAttribute("info", "Alerta borrada");
			return "redirect:/alerts";
		} else {
			redirectAttributes.addFlashAttribute("error","Antes de borrar una alerta tienes que borrar las noticias asociadas.");
			return "redirect:/alerts";
		}
	}

	

}
