package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/alerts")
public class AlertAdminController extends BaseController {
	
	private static String FOLDER = "/alerts/";
	
	@Autowired
	private AlertServiceImpl service;
	
	@Autowired
	private NewsDetectRepository repository;
	
	@Autowired
	private StatisticsRepository statsRepository;
	
	public AlertAdminController() {
		this.menu = "Alertas Sanitarias Activas";
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/history", method=RequestMethod.GET)
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		boolean isFalse = news.getFalPositive(); 
		news.setHistory(true);
		news.setFalPositive(false);
		repository.save(news);
		if (isFalse) {//Se suma a las estadisticas si estaba marcado como falso positivo
			Statistics stat = statsRepository.findOne(new Date(news.getCreateDate().getTime()));
			stat.setNumAlerts(stat.getNumAlerts()+1);
			statsRepository.save(stat);
		}
		redirectAttributes.addFlashAttribute("info", "Se ha marcado la noticia como pasada. Puedes visualizarla en el historial");
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/false", method=RequestMethod.GET)
	public String setNewsDetectFalse(Model model, RedirectAttributes redirectAttributes, @PathVariable ("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(true);
		repository.save(news);
		Statistics stat = statsRepository.findOne(new Date(news.getCreateDate().getTime()));
		stat.setNumAlerts(stat.getNumAlerts()-1);
		statsRepository.save(stat);
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
		if (isFalse) {//Se suma a las estadisticas si estaba marcado como falso positivo
			Statistics stat = statsRepository.findOne(new Date(news.getCreateDate().getTime()));
			stat.setNumAlerts(stat.getNumAlerts()+1);
			statsRepository.save(stat);
		}
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
		Statistics stat = statsRepository.findOne(new Date(news.getCreateDate().getTime()));
		stat.setNumAlerts(stat.getNumAlerts()-1);
		statsRepository.save(stat);
		redirectAttributes.addFlashAttribute("info", "La noticia se ha borrado correctamente.");
		return "redirect:/alerts/get/" + word.getId();
	}

}
