package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/risks")
public class RiskAdminController extends BaseController {
	
	private static String FOLDER = "/risks/";
	
	@Autowired
	private RiskServiceImpl wordService;
	
	public RiskAdminController() {
		this.menu = "Riesgos futuros";
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", wordService.getAllAlert());
		model.addAttribute("term", new Alert());
		return FOLDER + "words";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter,BindingResult result) {
        if (result.hasErrors()) {
            return FOLDER + "words";
        }
		wordService.create(wordFilter);
		redirectAttributes.addFlashAttribute("info","Se ha a&ntilde;adido correctamente el filtro");
		return "redirect:/admin/risks/words";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Risk word) {
		model.addAttribute("allWords", wordService.getAllAlert());
		model.addAttribute("term",word);
		return FOLDER + "words";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter, @PathVariable ("id") Risk before,BindingResult result) {
        if (result.hasErrors()) {
        	model.addAttribute("error","Hay un error en el formulario");
            return FOLDER + "words";
        }
		wordService.create(wordFilter);
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente el filtro.");
		return "redirect:/admin/risks/words";
	}
	
	@RequestMapping(value = "/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") Risk word) {
		wordService.remove(word);
		return "redirect:/admin/risks/words";
	}
	
}
