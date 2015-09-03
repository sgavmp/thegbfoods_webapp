package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.net.MalformedURLException;
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

import com.rometools.rome.io.impl.FeedParsers;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedForm;
import com.ucm.ilsa.veterinaria.domain.Language;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.domain.WebLevel;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.impl.PlaceAlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/feeds")
public class FeedAdminController extends BaseController {

	@Autowired
	private FeedService serviceFeed;	
	
	@Autowired
	private SchedulerService schedulerService;
	
	public FeedAdminController() {
		this.menu = "Sitios";
	}
	
	@ModelAttribute("enumFiabilidad")
	public WebLevel[] getValuesOfFiabilidad() {
		return WebLevel.values();
	}
	
	@ModelAttribute("enumLanguaje")
	public Language[] getValuesOfLanguaje() {
		return Language.values();
	}
	
	@RequestMapping("/get/{codeName}/update")
	public String updateNewsByFeed(Model model, @PathVariable ("codeName") Feed feed) {
		List<News> newsListAdd = serviceFeed.scrapFeed(feed);
		model.addAttribute(feed);
		model.addAttribute(newsListAdd);
		putInfoMessage("Actualizando fuente");
		return "redirect:/feeds/get/{codeName}";
	}
	
	@RequestMapping("/get/{codeName}/update/ajax")
	public @ResponseBody String updateNewsByFeedAjax(Model model, @PathVariable ("codeName") Feed feed) {
//		serviceFeed.scrapFeed(feed);
		schedulerService.startTask(feed);
		return "ok";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String formNewFeed(Model model) {
		model.addAttribute("feed",new FeedForm());
		model.addAttribute("nuevo",true);
		return "feedForm";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String formNewFeed(Model model, @Valid FeedForm feed,BindingResult result) {
        if (result.hasErrors()) {
            return "feedForm";
        }
		Feed feedP = new Feed(feed);
		feedP = serviceFeed.createFeed(feedP);
		if (feedP.getCode()!=null) {
			this.putInfoMessage("Sitio creado correctamente");
		} else {
			this.putErrorMessage("Ha ocurrido un error, vuelva a intentarlo más tarde.");
		}
		return "redirect:/admin/feeds/get/"+feedP.getCode()+"/edit";
	}
	
	@RequestMapping(value="/get/{codeName}/edit", method=RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable ("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm(feedP));
		return "feedForm";
	}
	
	@RequestMapping(value="/get/{codeName}/edit", method=RequestMethod.POST)
	public String saveFormEditFeed(Model model, @PathVariable ("codeName") Feed feedP, @ModelAttribute(value="feed") FeedForm feed, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
	        return "newsFeed";
	    }
		int version = feedP.getVersion();
		feedP.changeValues(feed);
		feedP = serviceFeed.updateFeed(feedP);
		model.addAttribute(new FeedForm(feedP));
		if (version < feedP.getVersion()) {
			this.putInfoMessage("Sitio actualizado correctamente");
		} else {
			this.putErrorMessage("Ha ocurrido un error, vuelva a intentarlo más tarde.");
		}
		return "feedForm";
	}
	
	@RequestMapping(value="/get/{codeName}/test", method=RequestMethod.GET)
	public String formTestFeed(Model model, @PathVariable ("codeName") Feed feedP) {
		if (feedP.isRSS()) {
			feedP.setUrlNews("");
			feedP.setRSS(false);
		}
		model.addAttribute("feed", new FeedForm(feedP));
		return "comprobarForm";
	}
	
	@RequestMapping(value="/get/{codeName}/test", method=RequestMethod.POST, params={"testFeed"})
	public @ResponseBody News testTestFeed(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.setIsRSS(false);
		News news = serviceFeed.testFeed(feed);
		//model.addAttribute(feed);
		return news;
	}
	
	@RequestMapping(value="/get/{codeName}/test", method=RequestMethod.POST, params={"addHtml"})
	public String addHtmlSelectorTest(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getSelectorHtml().add(new PairValues());
		//model.addAttribute(feed);
		return "comprobarForm";
	}
	
	@RequestMapping(value="/get/{codeName}/test", method=RequestMethod.POST, params={"removeHtml"})
	public String removeHtmlSelectorTest(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removeHtml")Integer index) {
		feed.getSelectorHtml().remove(index.intValue());
		//model.addAttribute(feed);
		return "comprobarForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"addHtml"})
	public String addHtmlSelector(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getSelectorHtml().add(new PairValues());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"removeHtml"})
	public String removeHtmlSelector(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removeHtml")Integer index) {
		feed.getSelectorHtml().remove(index.intValue());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"addMeta"})
	public String addMetaSelector(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getSelectorMeta().add(new PairValues());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"removeMeta"})
	public String removeMetaSelector(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removeMeta")Integer index) {
		feed.getSelectorMeta().remove(index.intValue());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"addPage"})
	public String addPageNews(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		feed.getUrlPages().add(new String());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"removePage"})
	public String removePageNews(Model model, @ModelAttribute(value="feed") FeedForm feed, @RequestParam("removePage")Integer index) {
		feed.getUrlPages().remove(index.intValue());
		//model.addAttribute(feed);
		return "feedForm";
	}
	
	@RequestMapping(value={"/get/{codeName}/edit","/create"}, method=RequestMethod.POST, params={"testFeed"})
	public @ResponseBody News testFeed(Model model, @ModelAttribute(value="feed") FeedForm feed) {
		News news = serviceFeed.testFeed(feed);
		if (news.getContent().length()>500)
			news.setContent(news.getContent().substring(0, 500).concat("..."));
		//model.addAttribute(feed);
		return news;
	}

	@RequestMapping("/get/{codeName}/remove")
	public String removeFeed(@PathVariable ("codeName") Feed feed) {
		if (this.serviceFeed.removeFeed(feed)) {
			this.putInfoMessage("Se ha borrado correctamente la fuente " + feed.getName());
		} else {
			this.putErrorMessage("No se ha borrado la fuente " + feed.getName());
			return "oneFeed";
		}
		return "redirect:/feeds";
	}

}
