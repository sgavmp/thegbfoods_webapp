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
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.PairValues;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	
	private static String PREFIX = "/admin/";
	@Autowired
	private FeedService serviceFeed;
	
	
	public AdminController() {
		this.menu = "admin";
	}
	
	@RequestMapping("**")
	public String menuWeb(){
		return PREFIX.concat("main");
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
