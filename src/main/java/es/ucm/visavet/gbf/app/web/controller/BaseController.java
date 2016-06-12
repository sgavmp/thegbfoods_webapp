package es.ucm.visavet.gbf.app.web.controller;

import java.util.Date;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.ucm.visavet.gbf.app.domain.UserDetailsImpl;
import es.ucm.visavet.gbf.app.util.MD5Util;

@SuppressWarnings("deprecation")
public abstract class BaseController {
	
	//Indica al template el menu que tiene que activar
	protected String menu="";
	
	
	@ModelAttribute("hoy")
	public Date getHoy() {
		return new Date();
	}
	
	@ModelAttribute("semana")
	public Date getSemana() {
		Date ayer = new Date(new Date().getTime() - 7 * 24 * 3600 * 1000l  );
		return ayer;
	}
	
	@ModelAttribute("mes")
	public Date getMes() {
		Date ayer = new Date(new Date().getTime() - 31 * 24 * 3600 * 1000l  );
		return ayer;
	}
	
	@ModelAttribute("ayer")
	public Date getAyer() {
		Date ayer = new Date(new Date().getTime() - 24 * 3600 * 1000l );
		return ayer;
	}
	
	@ModelAttribute("avatar")
	public String getAvatarHash(@AuthenticationPrincipal UserDetailsImpl activeUser) {
		if (activeUser!= null)
			return "http://www.gravatar.com/avatar/" + MD5Util.md5Hex(activeUser.getEmail());
		else
			return "";
	}
	
	@ModelAttribute("menu")
	public String getMenuActive() {
		return menu;
	}

}
