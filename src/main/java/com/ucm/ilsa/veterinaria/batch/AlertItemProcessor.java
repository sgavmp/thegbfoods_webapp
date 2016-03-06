package com.ucm.ilsa.veterinaria.batch;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.CharsetEnum;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.builder.NewsBuilder;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.service.impl.FeedScrapingImpl;

import es.ucm.visavet.gbf.domains.filterconstructor.TokenMgrError;
import es.ucm.visavet.gbf.topics.manager.ITopicsManager;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

class TestTopicsManager implements ITopicsManager {
	  public boolean existsTopic(String topic) {
	    switch(topic) {
	        case "Enfermedad": return true;
	        case "Dolencia": return true;
	        case "Desesperación": return true;
	        default: return false;    
	    }  
	  }
	  public Set<String> getDependencies(String topic) {
	    Set dEnfermedad = new HashSet<String>();
	      dEnfermedad.add("Dolencia");
	    Set dDolencia = new HashSet<String>();
	      dDolencia.add("Desesperación");
	    Set dDesesperacion = new HashSet<String>();      
	    switch(topic) {
	        case "Enfermedad": return dEnfermedad;
	        case "Dolencia": return dDolencia;
	        case "Desesperación": return dDesesperacion;
	        default: return new HashSet<String>();    
	    }
	  } 
	  public void addDependency(String topic, String ofTopic) {
	     System.out.println(topic+"--->"+ofTopic);  
	   }       
	  public InputStream getDefinition(String topic) {return null;}  
	}

public class AlertItemProcessor implements ItemProcessor<Alert, Alert> {

	private final static Logger LOGGER = Logger
			.getLogger(AlertItemProcessor.class);

	private List<String> links = new ArrayList<String>();

	@Override
	public Alert process(Alert item)  {
		String temp = "";
//		for (String aux : item.getWords().split(",")) {
//			aux = aux.trim();
//			if ((aux.contains(" ") || aux.contains(".") || aux.contains("-")) && !aux.contains("\"")) {
//				aux = "'".concat(aux).concat("'");
//				temp += aux + " ";
//			}
//		}
//		if (temp!="") {
//			temp = temp.trim();
//			item.setWords(temp);
//		}
		TopicValidator validator = new TopicValidator(new TopicValidatorSemantics(item.getTitle(),new TestTopicsManager()), new ByteArrayInputStream(item.getWords().getBytes()));
		try {
			validator.topic();
		} catch (TokenMgrError | ParseException e) {
			LOGGER.error("Error en alerta " + item.getTitle());
		}
		return item;
	}

}
