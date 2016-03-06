package com.ucm.ilsa.veterinaria.service;

import java.io.IOException;

import com.ucm.ilsa.veterinaria.domain.Configuracion;

public interface ConfiguracionService {
	public Configuracion getConfiguracion();
	public void setConfiguracion(Configuracion configuracion) throws IOException;
}
