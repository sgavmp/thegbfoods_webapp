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
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.service.impl.FeedScrapingImpl;

import es.ucm.visavet.gbf.topics.manager.ITopicsManager;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

public class AlertItemProcessor implements ItemProcessor<Alert, Alert> {
	
	@Autowired
	private TopicManager topicManager;

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
//		TopicValidator validator = new TopicValidator(new TopicValidatorSemantics(item.getTitle(),topicManager), new ByteArrayInputStream(item.getWords().getBytes()));
//		try {
//			validator.topic();
//		} catch (TokenMgrError | ParseException e) {
//			LOGGER.error("Error en alerta " + item.getTitle());
//		}
		return item;
	}

}
