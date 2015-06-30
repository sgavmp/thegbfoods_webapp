package com.ucm.ilsa.veterinaria.web.controller.impl.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.service.impl.PlaceAlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Controller
@RequestMapping("/admin/locations")
public class LocationsController extends BaseController {
	
	
	@Autowired
	private PlaceAlertServiceImpl serviceLocation;
	
	public LocationsController() {
		this.menu = "locations";
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("location", new Location());
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createLocation(Model model, @Valid Location location,BindingResult result) {
        if (result.hasErrors()) {
            return "locations";
        }
		serviceLocation.createLocation(location);
		putInfoMessage("Se ha a&ntilde;adido correctamente la nueva localizaci&oacute;n");
		return "redirect:/admin/locations";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method=RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable ("id") Location location) {
		model.addAttribute("location",location);
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/get/{id}/edit", method=RequestMethod.POST)
	public String updateLocation(Model model, @Valid Location location, @PathVariable ("id") Location before,BindingResult result) {
        if (result.hasErrors() & location.getId().equals(before.getId())) {
        	putErrorMessage("Hay un error en el formulario");
            return "locations";
        }
		serviceLocation.createLocation(location);
		putInfoMessage("Se ha actualizado correctamente la localizaci&oacute;n");
		return "redirect:/admin/locations";
	}
	
	@RequestMapping(value = "/get/{id}/remove", method=RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable ("id") Location location) {
		serviceLocation.removeLocation(location);
		return "redirect:/admin/locations";
	}
	
}
