package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

@Controller
@RequestMapping("/risks")
public class RiskController extends BaseController {
	
	private static String FOLDER = "risks";
	
	@Autowired
	private RiskServiceImpl service;
	
	@Autowired
	private NewsDetectRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private TopicManager topicManager;
	
	public RiskController() {
		this.menu = "Otras Alertas";
	}
	
	@ModelAttribute("alertsUncheck")
	public Set<Risk> getAllRisk() {
		return service.getAllAlertActive();
	}
	
	@ModelAttribute("folder")
	public String getFolder() {
		return FOLDER;
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Risk alert) {
		Risk alertActive = new Risk();
		alertActive.setId(alert.getId());
		alertActive.setTitle(alert.getTitle());
		alertActive.setNewsDetect(new HashSet<NewsDetect>());
		Risk alertHistory = new Risk();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
		alertHistory.setNewsDetect(new HashSet<NewsDetect>());
		Risk alertFalse = new Risk();
		alertFalse.setId(alert.getId());
		alertFalse.setTitle(alert.getTitle());
		alertFalse.setNewsDetect(new HashSet<NewsDetect>());
		for (NewsDetect news : alert.getNewsDetect()) {
			if (news.getFalPositive()) {
				alertFalse.getNewsDetect().add(news);
			} else if (news.getHistory()) {
				alertHistory.getNewsDetect().add(news);
			} else {
				alertActive.getNewsDetect().add(news);
			}
		}
		model.addAttribute("alert", alert);
		model.addAttribute("alertActive", alertActive);
		model.addAttribute("alertHistory", alertHistory);
		model.addAttribute("alertFalse", alertFalse);
		return "/" + FOLDER + "/oneAlert";
	}

	@RequestMapping("/detect")
	public String getMainRisks() {
		return "/" + FOLDER + "/alerts";
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", service.getAllAlert());
		model.addAttribute("term", new Alert());
		return "/" + FOLDER + "/words";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter,BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			model.addAttribute("error", "Hay un error en el formulario");
            return "/" + FOLDER + "/words";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "/" + FOLDER + "/words";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "/" + FOLDER + "/words";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "/" + FOLDER + "/words";
		}
		try {
			wordFilter = service.create(wordFilter);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error","Error al detectar posibles alertas con la alerta almacenada.");
			return "redirect:/alerts/risks/get/"+wordFilter.getTitle();
		}
		redirectAttributes.addFlashAttribute("info","Se ha a&ntilde;adido correctamente el filtro");
		return "redirect:/risks/get/"+wordFilter.getTitle();
	}
	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Risk word) {
		model.addAttribute("allWords", service.getAllAlert());
		model.addAttribute("term",word);
		return "/" + FOLDER + "/words";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter, @PathVariable ("id") Risk before,BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
        	model.addAttribute("error","Hay un error en el formulario");
            return "/" + FOLDER + "/words";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "/" + FOLDER + "/words";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "/" + FOLDER + "/words";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "/" + FOLDER + "/words";
		}
		service.update((Risk)wordFilter.bind(before));
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente el filtro.");
		return "redirect:/risks";
	}
	
	@RequestMapping(value = "/get/{id}/reset", method=RequestMethod.GET)
	public String resetNewsByFeed(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Risk word) {
		try {
			newsIndexService.resetAlert(word);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error","Error al detectar posibles alertas.");
			return "redirect:/get/"+word.getId();
		}
		redirectAttributes.addFlashAttribute("info","Se han reseteado las alertas detectadas.");
		return "redirect:/risks/get/"+word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") Risk word) {
		service.remove(word);
		return "redirect:/risks";
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/history", method=RequestMethod.GET)
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		boolean isFalse = news.getFalPositive(); 
		news.setHistory(true);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como pasada. Puedes visualizarla en el historial");
		return "redirect:/risks/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/false", method=RequestMethod.GET)
	public String setNewsDetectFalse(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(true);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como falso positivo.");
		return "redirect:/risks/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/active", method=RequestMethod.GET)
	public String setNewsDetectActive(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		boolean isFalse = news.getFalPositive();
		news.setHistory(false);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como activa.");
		return "redirect:/risks/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/remove", method=RequestMethod.GET)
	public String setNewsDetectRemove(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		boolean exist = true;
		do {
			exist = word.getNewsDetect().remove(news);
		} while (exist);
		service.update(word);
		repository.delete(news.getId());
		redirectAttributes.addFlashAttribute("info", "La noticia se ha borrado correctamente.");
		return "redirect:/risks/get/" + word.getId();
	}
	
}
