package es.ucm.visavet.gbf.app;

import org.springframework.stereotype.Component;

@Component
public class Configuration {
	
	public String enviroment;

	public String getEnviroment() {
		return enviroment;
	}

	public void setEnviroment(String enviroment) {
		this.enviroment = enviroment;
	}
	
	
}
