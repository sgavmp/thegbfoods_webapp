package es.ucm.visavet.gbf.app.service;

import java.io.IOException;

import es.ucm.visavet.gbf.app.domain.Configuracion;

public interface ConfiguracionService {
	public Configuracion getConfiguracion();
	public void setConfiguracion(Configuracion configuracion) throws IOException;
}
