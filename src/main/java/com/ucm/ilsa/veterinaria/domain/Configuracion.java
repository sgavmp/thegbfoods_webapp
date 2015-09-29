package com.ucm.ilsa.veterinaria.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Configuracion {
	@Id
	private String id = "conf";
	@Lob
	private String palabrasAlerta;
	@Lob
	private String paabrasLugar;
	private boolean usarPalabrasAlerta = false;
	private boolean usarPalabrasLugar = false;
	
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
	
}
