package es.ucm.visavet.gbf.app.web.controller.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.topic.TopicManager;
import es.ucm.visavet.gbf.app.service.impl.PlaceAlertServiceImpl;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

@Controller
@RequestMapping("/locations")
public class LocationsController extends BaseController {
	
	@Autowired
	private PlaceAlertServiceImpl serviceLocation;
	
	@Autowired
	private TopicManager topicManager;
	
	public LocationsController() {
		this.menu = "Proveedores";
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("location", new Location());
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, @Valid Location location,BindingResult result) {
        if (result.hasErrors()) {
            return "locations";
        }
		try {
			serviceLocation.createLocation(location);
		}  catch (IOException e) {
			model.addAttribute("error", "Se ha producido un error al detectar las localizaciones.");
			return "locations";
		}
		redirectAttributes.addFlashAttribute("info","Se ha a&ntilde;adido correctamente la nueva localizaci&oacute;n");
		return "redirect:/locations";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Location location) {
		model.addAttribute("location",location);
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, @Valid Location location, @PathVariable ("id") Location before,BindingResult result) throws UnsupportedEncodingException {
        if (result.hasErrors() & location.getId().equals(before.getId())) {
        	redirectAttributes.addFlashAttribute("error","Hay un error en el formulario");
            return "locations";
        }
        TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(location.getName(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(location.getQuery().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic() + " no existe.");
			return "locations";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "locations";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", "Se ha producido un error al validar el topic.");
			return "locations";
		}
		try {
			serviceLocation.updateLocation(before.bind(location));
		} catch (IOException e) {
			model.addAttribute("error", "Se ha producido un error al detectar las localizaciones.");
			return "locations";
		}
		redirectAttributes.addFlashAttribute("info","Se ha actualizado correctamente la localizaci&oacute;n");
		return "redirect:/locations";
	}
	
	@RequestMapping(value = "/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") Location location) {
		serviceLocation.removeLocation(location);
		return "redirect:/locations";
	}
	
}
