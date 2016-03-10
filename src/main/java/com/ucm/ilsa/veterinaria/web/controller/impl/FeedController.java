package com.ucm.ilsa.veterinaria.web.controller.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.Language;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.WebLevel;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/feeds")
public class FeedController extends BaseController {
	
	private static String FOLDER = "/feeds/";
	
	public FeedController() {
		this.menu="Sitios";
	}
	
	@Autowired
	private FeedService serviceFeed;
	@Autowired
	private AlertServiceImpl serviceAlert;
	@Autowired
	private SchedulerService schedulerService;
	
	
	@ModelAttribute("feeds")
	public List<Feed> getAllFeeds() {
		return serviceFeed.getAllFeed();
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return FOLDER + "feeds";
	}
	
	@RequestMapping("/get/{codeName}")
	public String getAllNewsByFeed(Model model, @PathVariable ("codeName") Feed feed) {
		model.addAttribute(feed);
		Set<Alert> alertas = serviceAlert.getAlertDetectSite(feed);
		for (Alert alerta : alertas) {
			Iterator<NewsDetect> iterator = alerta.getNewsDetect().iterator();
			while(iterator.hasNext()) {
				NewsDetect news = iterator.next();
				if (!news.getSite().equals(feed)) {
					iterator.remove();
				}
			}
		}
		model.addAttribute("alertas", alertas);
		return FOLDER + "oneFeed";
	}
	
	@ModelAttribute("enumFiabilidad")
	public WebLevel[] getValuesOfFiabilidad() {
		return WebLevel.values();
	}

	@ModelAttribute("enumLanguaje")
	public Language[] getValuesOfLanguaje() {
		return Language.values();
	}

	@RequestMapping("/get/{codeName}/update/ajax")
	public @ResponseBody String updateNewsByFeedAjax(Model model,
			@PathVariable("codeName") Feed feed) {
		schedulerService.startTask(feed);
		return "ok";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String formNewFeed(Model model) {
		model.addAttribute("feed", new FeedForm());
		model.addAttribute("nuevo", true);
		return FOLDER + "feedForm";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String formNewFeed(Model model,
			RedirectAttributes redirectAttributes, @Valid FeedForm feed,
			BindingResult result) {
		if (result.hasErrors()) {
			return FOLDER + "feedForm";
		}
		Feed feedP = new Feed(feed);
		feedP = serviceFeed.createFeed(feedP);
		if (feedP.getId() != null) {
			redirectAttributes.addFlashAttribute("info",
					"Sitio creado correctamente");
		} else {
			redirectAttributes.addFlashAttribute("error",
					"Ha ocurrido un error, vuelva a intentarlo más tarde.");
		}
		return "redirect:/feeds/get/" + feedP.getId() + "/edit";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm(feedP));
		return FOLDER + "feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.POST)
	public String saveFormEditFeed(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable("codeName") Feed feedP,
			@ModelAttribute(value = "feed") FeedForm feed,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return FOLDER + "newsFeed";
		}
		int version = feedP.getVersion();
		feedP.changeValues(feed);
		feedP = serviceFeed.updateFeed(feedP);
		model.addAttribute(new FeedForm(feedP));
		if (version < feedP.getVersion()) {
			redirectAttributes.addFlashAttribute("info",
					"Sitio actualizado correctamente");
		} else {
			redirectAttributes.addFlashAttribute("error",
					"Ha ocurrido un error, vuelva a intentarlo más tarde.");
		}
		return FOLDER + "feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.GET)
	public String formTestFeed(Model model, @PathVariable("codeName") Feed feedP) {
		if (feedP.isRSS()) {
			feedP.setUrlNews("");
			feedP.setRSS(false);
		}
		model.addAttribute("feed", new FeedForm(feedP));
		return FOLDER + "comprobarForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.POST, params = { "testFeed" })
	public @ResponseBody News testTestFeed(Model model,
			@ModelAttribute(value = "feed") FeedForm feed) {
		feed.setIsRSS(false);
		News news = serviceFeed.testFeed(feed);
		// model.addAttribute(feed);
		return news;
	}

	@RequestMapping(value = { "/get/{codeName}/edit", "/create" }, method = RequestMethod.POST, params = { "addPage" })
	public String addPageNews(Model model,
			@ModelAttribute(value = "feed") FeedForm feed) {
		feed.getUrlPages().add(new String());
		// model.addAttribute(feed);
		return FOLDER + "feedForm";
	}

	@RequestMapping(value = { "/get/{codeName}/edit", "/create" }, method = RequestMethod.POST, params = { "removePage" })
	public String removePageNews(Model model,
			@ModelAttribute(value = "feed") FeedForm feed,
			@RequestParam("removePage") Integer index) {
		feed.getUrlPages().remove(index.intValue());
		// model.addAttribute(feed);
		return FOLDER + "feedForm";
	}

	@RequestMapping(value = { "/get/{codeName}/edit", "/create" }, method = RequestMethod.POST, params = { "testFeed" })
	public @ResponseBody News testFeed(Model model,
			@ModelAttribute(value = "feed") FeedForm feed) {
		News news = serviceFeed.testFeed(feed);
		if (news != null) {
			if (news.getContent() != null) {
				if (news.getContent().length() > 500)
					news.setContent(news.getContent().substring(0, 500)
							.concat("..."));
			}
		}
		// model.addAttribute(feed);
		return news;
	}

	@RequestMapping("/get/{codeName}/remove")
	public String removeFeed(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable("codeName") Feed feed) {
		if (this.serviceFeed.removeFeed(feed)) {
			redirectAttributes.addFlashAttribute("info",
					"Se ha borrado correctamente la fuente " + feed.getName());
		} else {
			model.addAttribute("error",
					"No se ha borrado la fuente " + feed.getName());
			return "oneFeed";
		}
		return "redirect:/feeds";
	}

	@RequestMapping(value = "/get/{codeName}/check", method = RequestMethod.POST)
	public String checkNewsOnFeed(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable("codeName") Feed feed,
			@ModelAttribute(value = "link") String link) {
		if (link == "") {
			redirectAttributes.addFlashAttribute("error",
					"Debes de introducir la URL de una noticia del sitio.");
		} else
			try {
				if (!feed.linkIsFromSite(link)) {
					redirectAttributes
							.addFlashAttribute("error",
									"La noticia introducida no pertenece a este sitio.");
				} else {
					Set<AlertAbstract> alertas = serviceFeed
							.checkNewsLinkOnFeed(link, feed);
					model.addAttribute(feed);
					model.addAttribute("alertasDetectadas", alertas);
					return FOLDER + "oneFeed";
				}
			} catch (MalformedURLException e) {
				redirectAttributes.addFlashAttribute("error",
						"No es una URL valida.");
			}
		return "redirect:/feeds/get/" + feed.getId();
	}

	@RequestMapping("/update/all")
	public String updateAll(Model model, RedirectAttributes redirectAttributes) {
		schedulerService.startAllTask();
		redirectAttributes
				.addFlashAttribute(
						"info",
						"Se ha iniciado el proceso de actualización de los websites, esto puede llevar un rato.");
		return "redirect:/feeds";
	}
	
}
