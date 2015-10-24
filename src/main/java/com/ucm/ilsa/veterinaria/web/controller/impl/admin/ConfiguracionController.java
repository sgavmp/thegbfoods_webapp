package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;
import com.ucm.ilsa.veterinaria.service.FeedScraping;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/configuracion")
public class ConfiguracionController extends BaseController {
	
	@Autowired
	private ConfiguracionRepository configuracionService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private NewsCheckFeedService feedNewsCheckService;
	
	@Autowired
	private FeedScraping feedScraping;
	
	@Autowired
	private SchedulerService schedulerService;
	
	public ConfiguracionController() {
		this.menu = "Configuración";
	}
	
	@ModelAttribute("feeds")
	public List<Feed> getAllFeeds() {
		return feedService.getAllFeed();
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("conf", configuracionService.findOne("conf"));
		return "conf";
	}
	
	@RequestMapping(value="**", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Configuracion configuracion,BindingResult result) {
        if (result.hasErrors()) {
            return "conf";
        }
		configuracionService.save(configuracion);
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente la configuración");
		return "redirect:/admin/configuracion";
	}
	
	@RequestMapping(value="**", method=RequestMethod.POST, params={"testRegExp"})
	public String testRegExp(Model model, RedirectAttributes redirectAttributes, Configuracion configuracion,BindingResult result) {
        if (result.hasErrors()) {
            return "conf";
        }
		News news = feedScraping.getNewsFromSite(configuracion.getUrl(), configuracion.getFeed());
		if (news!=null) {
			Map<News,List<ResolvedLocation>> lugares = feedNewsCheckService.getLocations(Lists.newArrayList(news), configuracion.getTestReg());
			if (lugares.containsKey(news)) {
				model.addAttribute("lugares", lugares.get(news));
			}
		}
		model.addAttribute("conf", configuracionService.findOne("conf"));
		return "conf";
	}
	
	
	
}
