package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.util.Date;
import java.util.List;

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

import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.Fiabilidad;
import com.ucm.ilsa.veterinaria.domain.Language;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.domain.PlaceAlert;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.impl.PlaceAlertService;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	
	private static String PREFIX = "/admin/";
	@Autowired
	private FeedService serviceFeed;
	
	@Autowired
	private PlaceAlertService serviceLocation;
	
	
	public AdminController() {
		this.menu = "admin";
	}
	
	@ModelAttribute("enumFiabilidad")
	public Fiabilidad[] getValuesOfFiabilidad() {
		return Fiabilidad.values();
	}
	
	@RequestMapping("**")
	public String menuWeb(){
		return PREFIX.concat("main");
	}
	
	@RequestMapping("/locations")
	public String getAllLocations(Model model) {
		model.addAttribute("location", new PlaceAlert());
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/locations/create", method=RequestMethod.POST)
	public String createLocation(Model model, @Valid PlaceAlert location,BindingResult result) {
        if (result.hasErrors()) {
            return "locations";
        }
		serviceLocation.createLocation(location);
		putInfoMessage("Se ha a&ntilde;adido correctamente la localizaci$oacute;n");
		return "redirect:/admin/locations";
	}
	
	@RequestMapping(value = "/locations/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") PlaceAlert location) {
		model.addAttribute("location",location);
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/locations/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, @Valid PlaceAlert location, @PathVariable ("id") PlaceAlert before,BindingResult result) {
        if (result.hasErrors() & location.getId().equals(before.getId())) {
        	putErrorMessage("Hay un error en el formulario");
            return "locations";
        }
		serviceLocation.createLocation(location);
		putInfoMessage("Se ha a&ntilde;adido correctamente la localizaci&oacute;n");
		return "redirect:/admin/locations";
	}
	
	@RequestMapping(value = "/locations/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") PlaceAlert location) {
		serviceLocation.removeLocation(location);
		return "redirect:/admin/locations";
	}
	
	@RequestMapping("/feeds/get/{codeName}/update")
	public String updateNewsByFeed(Model model, @PathVariable ("codeName") Feed feed) {
		List<News> newsListAdd = serviceFeed.scrapFeed(feed);
		model.addAttribute(feed);
		model.addAttribute(newsListAdd);
		putInfoMessage("Sitio actualizado");
		return "oneFeed";
	}
	
	@RequestMapping(value="/feeds/create", method=RequestMethod.GET)
	public String formNewFeed(Model model) {
		model.addAttribute("feed",new FeedForm());
		return "feedForm";
	}
	
	@RequestMapping(value="/feeds/create", method=RequestMethod.POST)
	public String formNewFeed(Model model, @Valid FeedForm feed,BindingResult result) {
        if (result.hasErrors()) {
            return "feedForm";
        }
		Feed feedP = new Feed(feed);
		feedP = serviceFeed.createFeed(feedP);
		return "redirect:/admin/feeds/get/"+feedP.getCodeName()+"/edit";
	}
	
	@RequestMapping(value="/feeds/get/{codeName}/edit", method=RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable ("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm(feedP));
		return "feedForm";
	}
	
	@RequestMapping(value="/feeds/get/{codeName}/edit", method=RequestMethod.POST)
	public String saveFormEditFeed(Model model, @PathVariable ("codeName") Feed feedP, @ModelAttribute(value="feed") FeedForm feed, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        return "newsFeed";
	    }
		feedP.changeValues(feed);
		serviceFeed.updateFeed(feedP);
		model.addAttribute(new FeedForm(feedP));
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"addHtml"})
	public String addHtmlSelector(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getSelectorHtml().add(new PairValues());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"removeHtml"})
	public String removeHtmlSelector(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removeHtml")Integer index) {
		feed.getSelectorHtml().remove(index.intValue());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"addMeta"})
	public String addMetaSelector(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getSelectorMeta().add(new PairValues());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"removeMeta"})
	public String removeMetaSelector(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removeMeta")Integer index) {
		feed.getSelectorMeta().remove(index.intValue());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"addPage"})
	public String addPageNews(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getUrlPages().add(new String());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"removePage"})
	public String removePageNews(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removePage")Integer index) {
		feed.getUrlPages().remove(index.intValue());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/feeds/get/{codeName}/edit","/feeds/create"}, method=RequestMethod.POST, params={"testFeed"})
	public @ResponseBody News testFeed(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		News news = serviceFeed.testFeed(feed);
		//model.addAttribute(feed);
		return news;
	}

	@RequestMapping("/feeds/get/{codeName}/remove")
	public String removeFeed(@PathVariable ("codeName") Feed feed) {
		if (this.serviceFeed.removeFeed(feed)) {
			this.putInfoMessage("Se ha borrado correctamente el sitio " + feed.getName());
		} else {
			this.putErrorMessage("No se ha borrado el sitio " + feed.getName());
			return "oneFeed";
		}
		return "redirect:/feeds";
	}
}
