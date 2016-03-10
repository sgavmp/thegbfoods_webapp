package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

@Controller
@RequestMapping("/admin/words")
public class WordFilterController extends BaseController {
	
	private static String FOLDER = "/alerts/";
	
	@Autowired
	private AlertServiceImpl wordService;
	
	@Autowired
	private TopicManager topicManager;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	public WordFilterController() {
		this.menu = "Alertas Sanitarias";
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", wordService.getAllAlert());
		model.addAttribute("term", new Alert());
		return FOLDER + "words";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Alert wordFilter,BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
            return FOLDER + "words";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return FOLDER + "words";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return FOLDER + "words";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return FOLDER + "words";
		}
		try {
			wordService.create(wordFilter);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error","Error al detectar posibles alertas con la alerta almacenada.");
			return "redirect:/alerts/words";
		}
		redirectAttributes.addFlashAttribute("info","Se ha a&ntilde;adido correctamente el filtro");
		return "redirect:/alerts/words";
	}

	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Alert word) {
		model.addAttribute("allWords", wordService.getAllAlert());
		model.addAttribute("term",word);
		return FOLDER + "words";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Alert wordFilter, @PathVariable ("id") Alert before,BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
        	model.addAttribute("error","Hay un error en el formulario");
            return FOLDER + "words";
        }
        model.addAttribute("term", wordFilter);
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return FOLDER + "words";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return FOLDER + "words";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return FOLDER + "words";
		}
		wordFilter = wordService.update((Alert)wordFilter.bind(before));
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente el filtro.");
		return "redirect:/admin/words";
	}
	
	@RequestMapping(value = "/get/{id}/reset", method=RequestMethod.GET)
	public String resetNewsByFeed(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word) {
		try {
			newsIndexService.resetAlert(word);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error","Error al detectar posibles alertas con la alerta almacenada.");
			return "redirect:/alerts/get/"+word.getId();
		}
		redirectAttributes.addFlashAttribute("info","Se han reseteado las alertas detectadas.");
		return "redirect:/alerts/get/"+word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") Alert word) {
		wordService.remove(word);
		return "redirect:/admin/words";
	}
	
}
