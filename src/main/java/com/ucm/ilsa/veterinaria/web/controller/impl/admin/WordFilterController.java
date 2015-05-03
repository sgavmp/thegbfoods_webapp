package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ucm.ilsa.veterinaria.domain.WordFilter;
import com.ucm.ilsa.veterinaria.service.impl.WordFilterServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/words")
public class WordFilterController extends BaseController {
	
	@Autowired
	private WordFilterServiceImpl wordService;
	
	public WordFilterController() {
		this.menu = "words";
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", wordService.getAllWordFilter());
		model.addAttribute("term", new WordFilter());
		return "words";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, WordFilter wordFilter,BindingResult result) {
        if (result.hasErrors()) {
            return "words";
        }
		wordService.create(wordFilter);
		putInfoMessage("Se ha a&ntilde;adido correctamente el filtro");
		return "redirect:/admin/words";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") WordFilter word) {
		model.addAttribute("allWords", wordService.getAllWordFilter());
		model.addAttribute("term",word);
		return "words";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, @Valid WordFilter wordFilter, @PathVariable ("id") WordFilter before,BindingResult result) {
        if (result.hasErrors()) {
        	putErrorMessage("Hay un error en el formulario");
            return "words";
        }
		wordService.create(wordFilter);
		putInfoMessage("Se ha actualizado correctamente el filtro.");
		return "redirect:/admin/words";
	}
	
	@RequestMapping(value = "/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") WordFilter word) {
		wordService.remove(word);
		return "redirect:/admin/words";
	}
	
}
