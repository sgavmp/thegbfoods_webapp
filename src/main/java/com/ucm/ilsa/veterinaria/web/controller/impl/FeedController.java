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
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.ExtractionType;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.FeedPlaceEnum;
import com.ucm.ilsa.veterinaria.domain.FeedTypeEnum;
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
		List<Feed> feeds = serviceFeed.getAllFeed();
		for (Feed feed : feeds) {
			feed.setNextExecution(schedulerService.getNextExecution(feed));
		}
		return feeds;
	}

	@RequestMapping("**")
	public String getAllAlerts() {
		return "/feeds/feeds";
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
		return "/feeds/oneFeed";
	}
	
	@ModelAttribute("enumFiabilidad")
	public WebLevel[] getValuesOfFiabilidad() {
		return WebLevel.values();
	}

	@ModelAttribute("enumLanguaje")
	public Language[] getValuesOfLanguaje() {
		return Language.values();
	}
	
	@ModelAttribute("enumType")
	public FeedTypeEnum[] getValuesOfType() {
		return FeedTypeEnum.values();
	}
	
	@ModelAttribute("enumPlace")
	public FeedPlaceEnum[] getValuesOfPlace() {
		return FeedPlaceEnum.values();
	}
	
	@ModelAttribute("enumExtraction")
	public ExtractionType[] getValuesOfExtraction() {
		return ExtractionType.values();
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
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String formNewFeed(Model model,
			RedirectAttributes redirectAttributes, @Valid FeedForm feed,
			BindingResult result) {
		if (result.hasErrors()) {
			return "/feeds/feedForm";
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
	
	@RequestMapping(value = "/masive", method = RequestMethod.GET)
	public String createMasiveFeed(Model model) {
		return "/feeds/masiveFeed";
	}
	
	@RequestMapping(value = "/masive", method = RequestMethod.POST)
	public String createMasiveFeed(@RequestParam(name="list") String list, Model model) {
		if (StringUtils.isEmpty(list))
			model.addAttribute("error", "Tienes que añadir una URL por línea.");
		String[] listURL = list.split("\r\n");
		String regex = "^(?<name>[A-Za-z0-9 ]{5,20})\\t(?<url>(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*)\\t(?<tipo>@T(general|periodico|revistaCientifica|revista|blogNutricional|blog|institucional)+)(?<lugar>(\\t(@L(general|españa|italia|rusia|holanda|alemania|inglaterra|portugal|francia|estadosunidos|india|marruecos)+))+)$";
		Pattern pattern = Pattern.compile(regex);
		for (String linea : listURL) {
			if (!pattern.matcher(linea).find())
			{
				model.addAttribute("error", "Hay errores en los datos introducidos..");
			}
		}
		List<String> urlFail = serviceFeed.createFeedAuto(listURL);
		if (!urlFail.isEmpty()) {
			model.addAttribute("error", "Tienes que añadir una URL por línea.");
			model.addAttribute("fail", urlFail);
		}
		model.addAttribute("info", "Se han creado todos los websites correctamente.");
		return "/feeds/masiveFeed";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm(feedP));
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.POST)
	public String saveFormEditFeed(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable("codeName") Feed feedP,
			@ModelAttribute(value = "feed") FeedForm feed,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/feeds/newsFeed";
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
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.GET)
	public String formTestFeed(Model model, @PathVariable("codeName") Feed feedP) {
		if (feedP.isRSS()) {
			feedP.setUrlNews("");
			feedP.setRSS(false);
		}
		model.addAttribute("feed", new FeedForm(feedP));
		return "/feeds/comprobarForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.POST, params = { "testFeed" })
	public @ResponseBody News testTestFeed(Model model,
			@ModelAttribute(value = "feed") FeedForm feed) {
		feed.setIsRSS(false);
		News news = serviceFeed.testFeed(feed);
		return news;
	}

	@RequestMapping(value = { "/get/{codeName}/edit", "/create" }, method = RequestMethod.POST, params = { "testFeed" })
	public String testFeed(Model model,
			@ModelAttribute(value = "feed") FeedForm feed) {
		News news = serviceFeed.testFeed(feed);
		model.addAttribute(news);
		return "/feeds/comprobarForm";
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
