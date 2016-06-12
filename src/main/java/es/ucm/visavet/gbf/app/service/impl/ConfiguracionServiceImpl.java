package es.ucm.visavet.gbf.app.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import es.ucm.visavet.gbf.app.domain.Configuracion;
import es.ucm.visavet.gbf.app.repository.ConfiguracionRepository;
import es.ucm.visavet.gbf.app.scheduler.SchedulerService;
import es.ucm.visavet.gbf.app.service.ConfiguracionService;
import es.ucm.visavet.gbf.app.service.NewsIndexService;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

	@Autowired
	private ConfiguracionRepository repository;

	@Autowired
	private SchedulerService schedulerService;

	@Autowired
	private NewsIndexService newsIndexService;

	private Configuracion configuracion;

	@Bean(name = "configuracion", autowire = Autowire.BY_TYPE)
	@Override
	public Configuracion getConfiguracion() {
		if (configuracion != null) {
			return configuracion;
		} else if (repository.exists("conf")) {
			configuracion = repository.findOne("conf");
		} else {
			configuracion = new Configuracion();
			repository.save(configuracion);
		}
		return configuracion;
	}

	@Override
	public void setConfiguracion(Configuracion configuracion) throws IOException {
		repository.save(configuracion);
		if (configuracion.getRunService() != this.configuracion.getRunService()) {
			this.configuracion = configuracion;
			if (this.configuracion.getRunService()) {
				schedulerService.init();
				newsIndexService.initDirectory();
			} else {
				schedulerService.stopAllTask();
				newsIndexService.stopDirectory();
			}
		}
		if (configuracion.getRunClavin() != this.configuracion.getRunClavin()) {
			
		}
		this.configuracion = configuracion;
	}

}
