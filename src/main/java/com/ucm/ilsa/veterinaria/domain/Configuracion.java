package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Configuracion {
	@Id
	private String id = "conf";
	@Lob
	private String palabrasAlerta = "";
	@Lob
	private String paabrasLugar = "(^|\\s|\\,|\\.|\\-)(\\s*)(at|in|on|of|from|and|the|en|de)*(\\s)(?-i)([A-Z]\\w+)";
	private boolean usarPalabrasAlerta = false;
	private boolean usarPalabrasLugar = false;
	private Double radiusNear = (double) 10;
	private Integer dayRisks = 10;
	
	public Configuracion() {
		super();
	}
	public String getPalabrasAlerta() {
		return palabrasAlerta;
	}
	public void setPalabrasAlerta(String palabrasAlerta) {
		this.palabrasAlerta = palabrasAlerta;
	}
	public String getPaabrasLugar() {
		return paabrasLugar;
	}
	public void setPaabrasLugar(String paabrasLugar) {
		this.paabrasLugar = paabrasLugar;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean getUsarPalabrasAlerta() {
		return usarPalabrasAlerta;
	}
	public void setUsarPalabrasAlerta(boolean usarPalabrasAlerta) {
		this.usarPalabrasAlerta = usarPalabrasAlerta;
	}
	public boolean getUsarPalabrasLugar() {
		return usarPalabrasLugar;
	}
	public void setUsarPalabrasLugar(boolean usarPalabrasLugar) {
		this.usarPalabrasLugar = usarPalabrasLugar;
	}
	public Double getRadiusNear() {
		return radiusNear;
	}
	public void setRadiusNear(Double radiusNear) {
		this.radiusNear = radiusNear;
	}
	public Integer getDayRisks() {
		return dayRisks;
	}
	public void setDayRisks(Integer dayRisks) {
		this.dayRisks = dayRisks;
	}
	
	
	
}
