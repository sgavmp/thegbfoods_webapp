package com.ucm.ilsa.veterinaria.business.alerta.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.extractor.AlphaExtractor;
import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.business.alerta.IntfAlerta;
import com.ucm.ilsa.veterinaria.business.event.alerta.GeoTagAndFilterAlertEvent;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.repository.AlertDetectRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.service.impl.PlaceAlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.BaseController;

@Component
public class GeoTagAndFilterAlert implements
		IntfAlerta<GeoTagAndFilterAlertEvent> {

	@Autowired
	private AlertDetectRepository repository;
	
	@Autowired
	private NewsDetectRepository repositoryNewsDetect;

	@Autowired
	private AlertServiceImpl alertService;

	@Autowired
	private PlaceAlertServiceImpl placeAlertService;
	
	private GeoParser parser;

	private final static Logger LOGGER = Logger
			.getLogger(GeoTagAndFilterAlert.class);
	
	public GeoTagAndFilterAlert() throws ClavinException {
		ClassLoader classLoader = getClass().getClassLoader();
		String path = classLoader.getResource("IndexDirectory").getFile();
		path = path.replaceAll("%20", " ");
		parser = GeoParserFactory.getDefault(path, new AlphaExtractor(), 50, 15, false);
		LOGGER.info("Indice CLAVIN-MOD: ".concat(path));
		LOGGER.info("GeoParser ".concat(parser!=null?"Iniciado":"No iniciado"));
	}

	@Override
	@Subscribe
	public void responseToEvent(GeoTagAndFilterAlertEvent event) {
		List<Location> lugares = placeAlertService.getAllLocations();
		List<Alert> alertas = alertService.getAllAlert();

		for (Alert alerta : alertas) {
			AlertDetect alertDetectActive = repository.findByCheckIsFalseAndAlertOrderByCreateDateDesc(alerta);
			Map<News,List<String>> mapNewsDetect = new HashedMap();
			//Comprobamos todos los terminos y los almacenamos
			for (News news : event.getLocations().keySet()) {
				for (String word : alerta.getWords().split(",")) {
					if (news.getContent().toLowerCase().contains(word.toLowerCase())) {
						if (mapNewsDetect.containsKey(news)){
							mapNewsDetect.get(news).add(word);
						} else {
							List<String> wordsDetect = new ArrayList<String>();
							wordsDetect.add(word);
							mapNewsDetect.put(news, wordsDetect);
						}
					}
				}
			}
			//Si se han detectado palabras de la alerta en alguna noticia
			if (mapNewsDetect.size()>0) {
				if (alertDetectActive==null) {
					alertDetectActive = new AlertDetect();
					alertDetectActive.setAlert(alerta);
					alertDetectActive.setCheck(false);
					repository.save(alertDetectActive);
					if (alertDetectActive.getNewsDetect()==null)
						alertDetectActive.setNewsDetect(new ArrayList<NewsDetect>());
				}
				for (News news : mapNewsDetect.keySet()) {
					List<String> wordsDetect = mapNewsDetect.get(news);
					NewsDetect newsDetect = new NewsDetect();
					newsDetect.setDatePub(news.getPubDate());
					newsDetect.setAlert_detect(alertDetectActive);
					newsDetect.setLink(news.getUrl());
					newsDetect.setSite(event.getFeed());
					newsDetect.setTitle(news.getTitle());
					newsDetect.setWordsDetect(wordsDetect);
					List<ResolvedLocation> locationsAp = null;
					try {
						locationsAp = parser.parse(news.getContent());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newsDetect.setLocationsNear(obtenerLocalizacionesCercanas(locationsAp,lugares));//Obtenemos los lugares que coincidan con el pais de las localizaciones encontradas
					repositoryNewsDetect.save(newsDetect);
					alertDetectActive.getNewsDetect().add(newsDetect);
				}
				repository.save(alertDetectActive);
			}
			
		}

		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ event.getFeed().getName());
		Integer num = 0;
		// BaseController.putInfoMessage(num + " nuevas alertas detectadas");
		// LOGGER.info("Finalizada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
		// + event.getFeed().getName());
		// LOGGER.info("Se han detectado ".concat(num.toString()).concat(
		// " nuevas alertas de la fuente ".concat(event.getFeed()
		// .getName())));
	}
	
	public List<AlertDetect> detectAlert(Feed feed, Map<News,List<ResolvedLocation>> locations) {
		List<Location> lugares = placeAlertService.getAllLocations();
		List<Alert> alertas = alertService.getAllAlert();
		List<AlertDetect> listDetect = new ArrayList<AlertDetect>();
		for (Alert alerta : alertas) {
			AlertDetect alertDetectActive = null;
			Map<News,List<String>> mapNewsDetect = new HashedMap();
			//Comprobamos todos los terminos y los almacenamos
			for (News news : locations.keySet()) {
				for (String word : alerta.getWords().split(",")) {
					if (news.getContent().toLowerCase().contains(word.toLowerCase())) {
						if (mapNewsDetect.containsKey(news)){
							mapNewsDetect.get(news).add(word);
						} else {
							List<String> wordsDetect = new ArrayList<String>();
							wordsDetect.add(word);
							mapNewsDetect.put(news, wordsDetect);
						}
					}
				}
			}
			//Si se han detectado palabras de la alerta en alguna noticia
			if (mapNewsDetect.size()>0) {
				if (alertDetectActive==null) {
					alertDetectActive = new AlertDetect();
					alertDetectActive.setAlert(alerta);
					alertDetectActive.setCheck(false);
					if (alertDetectActive.getNewsDetect()==null)
						alertDetectActive.setNewsDetect(new ArrayList<NewsDetect>());
				}
				for (News news : mapNewsDetect.keySet()) {
					List<String> wordsDetect = mapNewsDetect.get(news);
					NewsDetect newsDetect = new NewsDetect();
					newsDetect.setDatePub(news.getPubDate());
					newsDetect.setAlert_detect(alertDetectActive);
					newsDetect.setLink(news.getUrl());
					newsDetect.setSite(feed);
					newsDetect.setTitle(news.getTitle());
					newsDetect.setWordsDetect(wordsDetect);
					List<ResolvedLocation> locationsAp = null;
					try {
						locationsAp = parser.parse(news.getContent());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					newsDetect.setLocationsNear(obtenerLocalizacionesCercanas(locationsAp,lugares));//Obtenemos los lugares que coincidan con el pais de las localizaciones encontradas
					alertDetectActive.getNewsDetect().add(newsDetect);
				}
				listDetect.add(alertDetectActive);
			}
		}

		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ feed.getName());
		return listDetect;
	}

	private List<Location> obtenerLocalizacionesCercanas(List<ResolvedLocation> locations,List<Location> lugares) {
		Map<CountryCode,Integer> countCountry = new HashedMap();
		CountryCode maxCountry;
		Integer maxCount = 0;
		//Contamos las apariciones de cada pais
		for (ResolvedLocation loc : locations) {
			CountryCode code = loc.getGeoname().getPrimaryCountryCode();
			Integer num = countCountry.getOrDefault(code, 0)+1;
			countCountry.put(code, num);
			if (maxCount<num) {
				maxCount = num;
				maxCountry = code;
			}
		}
		//Seleccionamos el pais con mas apraciones o los que mas en caso de empate
		List<CountryCode> countries = new ArrayList<CountryCode>();
		for (CountryCode code : countCountry.keySet()) {
			if (countCountry.get(code)>=maxCount) {
				countries.add(code);
			}
		}
		Set<Location> lugaresCercanos = Sets.newHashSet();
		//Comprobamos la inclusion de cada proveedor en los paises
		for (CountryCode code : countries) {
			for (Location lugar : lugares) {
				if (lugar.getCountry().equals(code)) {
					lugaresCercanos.add(lugar);
				}
			}
		}
		return Lists.newArrayList(lugaresCercanos);
	}

}
