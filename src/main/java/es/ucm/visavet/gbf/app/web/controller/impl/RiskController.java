package es.ucm.visavet.gbf.app.web.controller.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.neovisionaries.i18n.CountryCode;

import es.ucm.visavet.gbf.app.domain.Alert;
import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.NewsDetect;
import es.ucm.visavet.gbf.app.domain.Risk;
import es.ucm.visavet.gbf.app.domain.topic.TopicManager;
import es.ucm.visavet.gbf.app.repository.LocationRepository;
import es.ucm.visavet.gbf.app.repository.NewsDetectRepository;
import es.ucm.visavet.gbf.app.repository.StatisticsRepository;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import es.ucm.visavet.gbf.app.service.impl.RiskServiceImpl;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

@Controller
@RequestMapping("/risks")
public class RiskController extends BaseController {
		
	@Autowired
	private RiskServiceImpl service;
	
	@Autowired
	private NewsDetectRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private TopicManager topicManager;
	
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	public RiskController() {
		this.menu = "Otros Riesgos";
	}
	
	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		List<Location> listLocation = (List<Location>) locationRepository.findAllAfectRisk();
		for (Location loc : listLocation) {
			listCountries.put(
					CountryCode.getByCode(loc.getCountry().name())
							.getAlpha3(), "rgba(255,87,34,1)");
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		return listCountries;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreToday")
	public List<Object[]> getAllAlertsToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgDay(today.toString(),5);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreWeek")
	public List<Object[]> getAllAlertsWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreMonth")
	public List<Object[]> getAllAlertsMonth() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-31);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@ModelAttribute("folder")
	public String getFolder() {
		return "risks";
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable ("idAlert") Risk alert) {
		alert = service.setNewsLocation(alert);
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
		return "/alerts/oneAlert";
	}

	@RequestMapping("/detect")
	public String getMainRisks() {
		return "/alerts/alerts";
	}
	
	@RequestMapping("/list")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", service.getAllAlert());
		model.addAttribute("term", new Alert());
		return "/alerts/words";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter,BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			model.addAttribute("error", "Hay un error en el formulario");
            return "/alerts/formAlert";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle()+"test", topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getWords().getBytes()),"UTF-8"));
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
			wordFilter = service.create(wordFilter);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error","Error al detectar posibles alertas con la alerta almacenada.");
			return "redirect:/alerts/get/"+wordFilter.getId();
		}
		redirectAttributes.addFlashAttribute("info","Se ha añadido correctamente el filtro");
		return "redirect:/risks/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/create", method=RequestMethod.GET)
	public String getFormCreate(Model model) {
		model.addAttribute("term", new Risk());
		return "/alerts/formAlert";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Risk word) {
		model.addAttribute("term",word);
		return "/alerts/formAlert";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter, @PathVariable ("id") Risk before,BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
        	model.addAttribute("error","Hay un error en el formulario");
            return "/alerts/formAlert";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getWords().getBytes()),"UTF-8"));
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
		service.update((Risk)wordFilter.bind(before));
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente el filtro.");
		return "redirect:/risks/get/"+wordFilter.getId();
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
		return "redirect:/risks/list";
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/history", method=RequestMethod.GET)
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Risk word, @PathVariable("idNews") NewsDetect news) {
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
		news.setHistory(false);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como activa.");
		return "redirect:/risks/get/" + word.getId();
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats", method=RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat() {
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		for (Risk alert : service.getAllAlert()) {
			alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		}
		for (Object[] ob : statisticsRepository.getRisktStats()) {
			alerts.get(ob[1]).put(ob[2].toString(),ob);
		}
		return alerts;
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats/{alertId}", method=RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat(@PathVariable ("alertId") Risk alert) {
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		for (Object[] ob : statisticsRepository.getRisktStats(alert.getId().toString())) {
			alerts.get(ob[1]).put(ob[2].toString(),ob);
		}
		return alerts;
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
