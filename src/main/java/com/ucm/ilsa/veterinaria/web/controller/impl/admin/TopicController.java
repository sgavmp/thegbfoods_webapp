package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Topic;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.repository.TopicRepository;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

@Controller
@RequestMapping("/admin/topic")
public class TopicController extends BaseController {

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private TopicManager topicManager;

	public TopicController() {
		this.menu = "Topics";
	}

	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("allTopic", topicRepository.findAll());
		model.addAttribute("term", new Topic());
		return "topic";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createLocation(Model model,
			RedirectAttributes redirectAttributes, Topic wordFilter,
			BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			model.addAttribute("error", "Hay un error en el formulario");
			return "topic";
		}
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "topic";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "topic";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "topic";
		}
		topicRepository.save(wordFilter);
		redirectAttributes.addFlashAttribute("info",
				"Se ha a&ntilde;adido correctamente el topic");
		return "redirect:/admin/topic";
	}

	@RequestMapping(value = "/get/{id}/edit", method = RequestMethod.GET)
	public String getFormUpdateLocation(Model model,
			@PathVariable("id") Topic word) {
		model.addAttribute("allTopic", topicRepository.findAll());
		model.addAttribute("term", word);
		return "topic";
	}

	@RequestMapping(value = "/get/{id}/edit", method = RequestMethod.POST)
	public String updateLocation(Model model,
			RedirectAttributes redirectAttributes, Topic wordFilter,
			@PathVariable("id") Topic before, BindingResult result) {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			model.addAttribute("error", "Hay un error en el formulario");
			return "topic";
		}
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new ByteArrayInputStream(wordFilter.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "topic";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "topic";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "topic";
		}
		topicRepository.save(wordFilter);
		redirectAttributes.addFlashAttribute("info",
				"Se ha actualizado correctamente el topic.");
		return "redirect:/admin/topic";
	}

	@RequestMapping(value = "/get/{id}/remove", method = RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable("id") Topic word) {
		topicRepository.delete(word);
		return "redirect:/admin/topic";
	}

}
