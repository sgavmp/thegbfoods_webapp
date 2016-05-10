package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
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
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
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
@RequestMapping("/alerts")
public class AlertController extends BaseController {
	
	private static String FOLDER = "alerts";
	
	@Autowired
	private AlertServiceImpl service;
	
	@Autowired
	private NewsDetectRepository repository;
	
	@Autowired
	private TopicManager topicManager;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	public AlertController() {
		this.menu = "Alertas Sanitarias";
	}
	
	@ModelAttribute("folder")
	public String getFolder() {
		return FOLDER;
	}

	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		List<Location> listLocation = (List<Location>) locationRepository.findAllAfectAlert();
		for (Location loc : listLocation) {
			listCountries.put(
					CountryCode.getByCode(loc.getCountry().name())
							.getAlpha3(), "rgba(255,87,34,1)");
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		return listCountries;
	}
	
	@ModelAttribute("alertsScoreToday")
	public List<Object[]> getAllAlertsToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgDay(today.toString(),5);
		return lista;
	}
	
	@ModelAttribute("alertsScoreWeek")
	public List<Object[]> getAllAlertsWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@ModelAttribute("alertsScoreMonth")
	public List<Object[]> getAllAlertsMonth() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-31);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@ModelAttribute("alertsTrendsMonth")
	public List<Object[]> getAllAlertsTrends() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-31);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@RequestMapping("/list")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", service.getAllAlert());
		return "/alerts/words";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Alert wordFilter,BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
            return "/alerts/formAlert";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle()+"test", topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "/alerts/formAlert";
		}
		try {
			service.create(wordFilter);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error","Error al detectar posibles alertas con la alerta almacenada.");
			return "redirect:/alerts/get/"+wordFilter.getId();
		}
		redirectAttributes.addFlashAttribute("info","Se ha añadido correctamente el filtro");
		return "redirect:/alerts/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/create", method=RequestMethod.GET)
	public String getFormCreate(Model model) {
		model.addAttribute("term", new Alert());
		return "/alerts/formAlert";
	}

	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Alert word) {
		model.addAttribute("allWords", service.getAllAlert());
		model.addAttribute("term",word);
		return "/alerts/formAlert";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Alert wordFilter, @PathVariable ("id") Alert before,BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
        	model.addAttribute("error","Hay un error en el formulario");
            return "/alerts/formAlert";
        }
        model.addAttribute("term", wordFilter);
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "/alerts/formAlert";
		}
		wordFilter = service.update((Alert)wordFilter.bind(before));
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente el filtro.");
		return "redirect:/alerts/get/"+wordFilter.getId();
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
		service.remove(word);
		return "redirect:/alerts/list";
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Alert alert) {
		Alert alertActive = new Alert();
		alertActive.setId(alert.getId());
		alertActive.setTitle(alert.getTitle());
		alertActive.setNewsDetect(new HashSet<NewsDetect>());
		Alert alertHistory = new Alert();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
		alertHistory.setNewsDetect(new HashSet<NewsDetect>());
		Alert alertFalse = new Alert();
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
		return "/alerts/oneAlert";
	}

	@RequestMapping("/detect")
	public String getAllAlerts() {
		return "/alerts/alerts";
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/history", method=RequestMethod.GET)
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		boolean isFalse = news.getFalPositive(); 
		news.setHistory(true);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como pasada. Puedes visualizarla en el historial");
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/false", method=RequestMethod.GET)
	public String setNewsDetectFalse(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(true);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como falso positivo.");
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/active", method=RequestMethod.GET)
	public String setNewsDetectActive(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		boolean isFalse = news.getFalPositive(); 
		news.setHistory(false);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como activa.");
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/remove", method=RequestMethod.GET)
	public String setNewsDetectRemove(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		boolean exist = true;
		do {
			exist = word.getNewsDetect().remove(news);
		} while (exist);
		service.update(word);
		repository.delete(news.getId());
		redirectAttributes.addFlashAttribute("info", "La noticia se ha borrado correctamente.");
		return "redirect:/alerts/get/" + word.getId();
	}
	
}
