package com.ucm.ilsa.veterinaria.business.tratamiento.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.extractor.AlphaExtractor;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.business.event.alerta.GeoTagAndFilterAlertEvent;
import com.ucm.ilsa.veterinaria.business.event.config.EventBusFactoryBean;
import com.ucm.ilsa.veterinaria.business.event.tratamiento.FeedUpdateEvent;
import com.ucm.ilsa.veterinaria.business.tratamiento.IntfTratamiento;
import com.ucm.ilsa.veterinaria.domain.News;

@Component
public class GeoTagTratamiento implements IntfTratamiento<FeedUpdateEvent> {
	
	
	private final static Logger LOGGER = Logger.getLogger(GeoTagTratamiento.class);

	@Override
	@Subscribe public void responseToEvent(FeedUpdateEvent event) {
		LOGGER.info("Ejemplo de tratamiento para el sitio: " + event.getFeed().getName());
		GeoTagAndFilterAlertEvent evento = new GeoTagAndFilterAlertEvent();
		evento.setFeed(event.getFeed());
		Map<News, List<ResolvedLocation>> map = new HashMap<>();
		for (News news : event.getListNews()) {
//			try {
//			//List<ResolvedLocation> locations = parser.parse(news.getContent());
//			if (locations.size()>0) 
//				map.put(news, locations);
//			} catch (Exception ex) {
//				LOGGER.error(ex.getMessage());
//			}
			map.put(news, new ArrayList<ResolvedLocation>());
		}
		evento.setLocations(map);
		evento.setDate(new Date(System.currentTimeMillis()));
		EventBusFactoryBean.getInstance().post(evento);
	}
	
	public Map<News, List<ResolvedLocation>> getLocations(List<News> listNews) {
		Map<News, List<ResolvedLocation>> map = new HashMap<>();
		for (News news : listNews) {
//			try {
//			List<ResolvedLocation> locations = parser.parse(news.getContent());
//			if (locations.size()>0) 
//				map.put(news, locations);
//			} catch (Exception ex) {
//				LOGGER.error(ex.getMessage());
//			}
			map.put(news, new ArrayList<ResolvedLocation>());
		}
		return map;
	}

}
