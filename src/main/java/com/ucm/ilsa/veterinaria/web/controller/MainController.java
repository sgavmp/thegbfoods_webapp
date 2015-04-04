package com.ucm.ilsa.veterinaria.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.webjars.WebJarAssetLocator;

import com.ucm.ilsa.veterinaria.domain.Alert;

@Controller
public class MainController {
	
	private WebJarAssetLocator assetLocator;
	
	public MainController() {
		this.assetLocator = new WebJarAssetLocator();
	}
	
	@RequestMapping(value = {"","/"})
	public String checkAlert() {
		return "redirect:/alerts";
	}
	
	@ResponseBody
	@RequestMapping("/webjars/{webjar}/**")
	public ResponseEntity locateWebjarAsset(@PathVariable String webjar, HttpServletRequest request) {
	    try {
	    	String mvcPrefix = "/webjars/" + webjar + "/"; // This prefix must match the mapping path!
	    	String mvcPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String fullPath = assetLocator.getFullPath(webjar, mvcPath.substring(mvcPrefix.length()));
	        return new ResponseEntity(new ClassPathResource(fullPath), HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
}
