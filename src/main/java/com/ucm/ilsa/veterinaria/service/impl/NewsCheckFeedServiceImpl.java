package com.ucm.ilsa.veterinaria.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.extractor.AlphaExtractor;
import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.PointLocation;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

@Service
public class NewsCheckFeedServiceImpl implements NewsCheckFeedService {

	@Autowired
	private NewsDetectRepository repositoryNewsDetect;

	@Autowired
	private AlertServiceImpl alertService;

	@Autowired
	private PlaceAlertServiceImpl placeAlertService;

	@Autowired
	private StatisticsRepository statisticsRepository;

	private ConfiguracionRepository configuracionRepository;

	@Autowired
	private FeedService service;

	private GeoParser parser;

	private final static Logger LOGGER = Logger
			.getLogger(NewsCheckFeedServiceImpl.class);

	@Autowired
	public NewsCheckFeedServiceImpl(
			ConfiguracionRepository configuracionRepository)
			throws ClavinException {
		this.configuracionRepository = configuracionRepository;
		if (!configuracionRepository.exists("conf")) {
			LOGGER.info("No existe una configuración inicial, se inciará una en la base de datos.");
			configuracionRepository.save(new Configuracion());
		}
		ClassLoader classLoader = getClass().getClassLoader();
		String path = classLoader.getResource("IndexDirectory").getFile();
		path = path.replaceAll("%20", " ");
		parser = GeoParserFactory.getDefault(path, new AlphaExtractor(), 50,
				15, false);
		LOGGER.info("Indice CLAVIN-MOD: ".concat(path));
		LOGGER.info("GeoParser ".concat(parser != null ? "Iniciado"
				: "No iniciado"));
	}

	public void checkNews(List<News> listNews, Feed feed) {
		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ feed.getName());
		Integer newsNum = listNews.size();
		Integer alertDetectNum = 0;
		List<Location> lugares = placeAlertService.getAllLocations();
		List<Alert> alertas = alertService.getAllAlert();
		feed = service.setSateOfFeed(feed, UpdateStateEnum.DETECT_ALERTS);
		Configuracion configuracion = configuracionRepository.findOne("conf");
		Map<News, List<String>> mapNewsDetectPreliminar = new HashedMap();
		if (configuracion.getPalabrasAlerta().length() > 0
				&& configuracion.getUsarPalabrasAlerta()) {// Solo
															// se
															// filtra
			// por los
			// terminos de
			// alerta
			// general si
			// hay alguno
			List<String> terminos = Lists.newArrayList(configuracion
					.getPalabrasAlerta().split(","));
			for (News news : listNews) {
				if (news != null) {
					for (String word : terminos) {
						boolean caseSensitive = false;
						if (word.startsWith("\"") && word.endsWith("\"")) {
							word = word.replace("\"", "").trim();
							caseSensitive = true;
						} else {
							word = word.toLowerCase().trim();
						}
						String content = caseSensitive ? news.getContent()
								: news.getContent().toLowerCase();
						if (content.contains(word)
								|| content.startsWith(word + " ")
								|| content.endsWith(" " + word)) {
							if (mapNewsDetectPreliminar.containsKey(news)) {
								mapNewsDetectPreliminar.get(news).add(word);
							} else {
								List<String> wordsDetect = new ArrayList<String>();
								wordsDetect.add(word);
								mapNewsDetectPreliminar.put(news, wordsDetect);
							}
						}
					}
				}
			}
			listNews = Lists.newArrayList(mapNewsDetectPreliminar.keySet());
		}
		for (Alert alerta : alertas) {
			Map<News, List<String>> mapNewsDetect = new HashedMap();
			// Comprobamos todos los terminos y los almacenamos
			for (News news : listNews) {
				if (news != null) {
					List<String> terminos = Lists.newArrayList(alerta
							.getWords().split(","));
					terminos.add(alerta.getTitle());
					for (String word : terminos) {
						boolean caseSensitive = false;
						if (word.startsWith("\"") && word.endsWith("\"")) {
							word = word.replace("\"", "").trim();
							caseSensitive = true;
						} else {
							word = word.toLowerCase().trim();
						}
						String content = caseSensitive ? news.getContent()
								: news.getContent().toLowerCase();
						if (content.contains(" " + word + " ")
								|| content.startsWith(word + " ")
								|| content.endsWith(" " + word)) {
							if (mapNewsDetect.containsKey(news)) {
								mapNewsDetect.get(news).add(word);
							} else {
								List<String> wordsDetect = new ArrayList<String>();
								wordsDetect.add(word);
								mapNewsDetect.put(news, wordsDetect);
							}
						}
					}
					if (mapNewsDetectPreliminar.size() > 0) {
						if (mapNewsDetect.containsKey(news))
							mapNewsDetect.get(news).addAll(
									mapNewsDetectPreliminar.get(news));
					}
				}
			}
			// Si se han detectado palabras de la alerta en alguna noticia
			if (mapNewsDetect.size() > 0) {
				feed = service.setSateOfFeed(feed,
						UpdateStateEnum.DETECT_PLACES);
				for (News news : mapNewsDetect.keySet()) {
					if (repositoryNewsDetect.findFirstByAlertDetectAndLink(
							alerta, news.getUrl()) != null) {
						continue;
					}
					List<String> wordsDetect = mapNewsDetect.get(news);
					NewsDetect newsDetect = new NewsDetect();
					newsDetect.setDatePub(news.getPubDate());
					newsDetect.setAlertDetect(alerta);
					newsDetect.setLink(news.getUrl());
					newsDetect.setSite(feed);
					newsDetect.setTitle(news.getTitle());
					newsDetect.setWordsDetect(wordsDetect);
					String regExp = null;
					if (configuracion.getUsarPalabrasLugar()) {
						regExp = configuracion.getPaabrasLugar();
					}
					List<ResolvedLocation> locationsAp = null;
					try {
						locationsAp = parser.parse(news.getContent(), regExp);
					} catch (Exception e) {
						LOGGER.info("Se ha producido un error al obtener las localizaciones de la noticia");
						LOGGER.debug(e.getMessage());
					}
					// Si la alerta es de Nivel 2 o 3 se detecta los
					// proveedores por el pais
					if (alerta.getType().equals(AlertLevel.orange)
							|| alerta.getType().equals(AlertLevel.red)) {
						// Obtenemos los lugares que coincidan con el pais
						// de
						// las localizaciones encontradas
						newsDetect
								.setLocationsNear(obtenerLocalizacionesCercanasPais(
										locationsAp, lugares));
					} else { // Si la alerta es de Nivel 1 se detecta los
								// proveedores por cercania a los puntos
								// encontrados
						// Obtenemos los lugares que entren en el radio de
						// la localizacion
						newsDetect
								.setLocationsNear(obtenerLocalizacionesCercanas(
										locationsAp, lugares));
					}
					// Guardamos los lugares encontrados de la noticia
					List<PointLocation> puntos = new ArrayList<PointLocation>();
					for (ResolvedLocation loc : locationsAp) {
						PointLocation point = new PointLocation();
						point.setCountry(loc.getGeoname()
								.getPrimaryCountryCode());
						point.setLatitude(loc.getGeoname().getLatitude());
						point.setLongitude(loc.getGeoname().getLongitude());
						point.setName(loc.getMatchedName());
						puntos.add(point);
					}
					newsDetect.setLocations(puntos);
					if (repositoryNewsDetect.findFirstByAlertDetectAndLink(
							alerta, newsDetect.getLink()) == null) {
						repositoryNewsDetect.save(newsDetect);
						alertDetectNum++;
						alerta.getNewsDetect().add(newsDetect);
					}
				}
				alertService.update(alerta);
			}

		}
		Date today = new Date(System.currentTimeMillis());
		Statistics statistics = statisticsRepository.findOne(today);
		if (statistics != null) {
			statistics.increment(alertDetectNum, 0);
		} else {
			statistics = new Statistics(today, alertDetectNum, newsNum);
		}
		statisticsRepository.save(statistics);
	}

	public List<Alert> checkNews(News news, Feed feed) {
		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ feed.getName());
		List<Location> lugares = placeAlertService.getAllLocations();
		List<Alert> alertas = alertService.getAllAlert();
		Configuracion configuracion = configuracionRepository.findOne("conf");
		List<String> newsDetectPreliminar = new ArrayList<String>();
		List<Alert> alertasDetectadas = Lists.newArrayList();
		if (configuracion.getPalabrasAlerta().length() > 0
				&& configuracion.getUsarPalabrasAlerta()) {// Solo
															// se
															// filtra
			// por los
			// terminos de
			// alerta
			// general si
			// hay alguno
			List<String> terminos = Lists.newArrayList(configuracion
					.getPalabrasAlerta().split(","));
			if (news != null) {
				for (String word : terminos) {
					boolean caseSensitive = false;
					if (word.startsWith("\"") && word.endsWith("\"")) {
						word = word.replace("\"", "").trim();
						caseSensitive = true;
					} else {
						word = word.toLowerCase().trim();
					}
					String content = caseSensitive ? news.getContent() : news
							.getContent().toLowerCase();
					if (content.contains(word)
							|| content.startsWith(word + " ")
							|| content.endsWith(" " + word)) {
						newsDetectPreliminar.add(word);
					}
				}
			}
		}
		if (newsDetectPreliminar.size() > 0
				|| !configuracion.getUsarPalabrasAlerta()) {
			for (Alert alerta : alertas) {
				Alert alertDetectActive = new Alert();
				alertDetectActive.setNewsDetect(new ArrayList<NewsDetect>());
				List<String> wordsDetect = new ArrayList();
				// Comprobamos todos los terminos y los almacenamos
				if (news != null) {
					List<String> terminos = Lists.newArrayList(alerta
							.getWords().split(","));
					terminos.add(alerta.getTitle());
					String contentInLowerCase = news.getContent().toLowerCase();
					for (String word : terminos) {
						if (contentInLowerCase.contains(" "
								+ word.toLowerCase() + " ")) {
							wordsDetect.add(word);
						}
					}
				}
				if (newsDetectPreliminar.size() > 0) {
					wordsDetect.addAll(newsDetectPreliminar);
				}
				// Si se han detectado palabras de la alerta en la noticia
				if (!wordsDetect.isEmpty()) {
					NewsDetect newsDetect = new NewsDetect();
					newsDetect.setDatePub(news.getPubDate());
					newsDetect.setAlertDetect(alertDetectActive);
					newsDetect.setLink(news.getUrl());
					newsDetect.setSite(feed);
					newsDetect.setTitle(news.getTitle());
					newsDetect.setWordsDetect(wordsDetect);
					String regExp = null;
					if (configuracion.getUsarPalabrasLugar()) {
						regExp = configuracion.getPaabrasLugar();
					}
					List<ResolvedLocation> locationsAp = null;
					try {
						locationsAp = parser.parse(news.getContent(), regExp);
					} catch (Exception e) {
						LOGGER.info("Se ha producido un error al obtener las localizaciones de la noticia");
						LOGGER.debug(e.getMessage());
					}
					// Si la alerta es de Nivel 2 o 3 se detecta los
					// proveedores por el pais
					if (alerta.getType().equals(AlertLevel.orange)
							|| alerta.getType().equals(AlertLevel.red)) {
						// Obtenemos los lugares que coincidan con el pais
						// de
						// las localizaciones encontradas
						newsDetect
								.setLocationsNear(obtenerLocalizacionesCercanasPais(
										locationsAp, lugares));
					} else { // Si la alerta es de Nivel 1 se detecta los
								// proveedores por cercania a los puntos
								// encontrados
						// Obtenemos los lugares que entren en el radio de
						// la localizacion
						newsDetect
								.setLocationsNear(obtenerLocalizacionesCercanas(
										locationsAp, lugares));
					}
					// Guardamos los lugares encontrados de la noticia
					List<PointLocation> puntos = new ArrayList<PointLocation>();
					for (ResolvedLocation loc : locationsAp) {
						PointLocation point = new PointLocation();
						point.setCountry(loc.getGeoname()
								.getPrimaryCountryCode());
						point.setLatitude(loc.getGeoname().getLatitude());
						point.setLongitude(loc.getGeoname().getLongitude());
						point.setName(loc.getMatchedName());
						puntos.add(point);
					}
					newsDetect.setLocations(puntos);
					alertDetectActive.getNewsDetect().add(newsDetect);
				}
				if (!alertDetectActive.getNewsDetect().isEmpty())
					alertasDetectadas.add(alertDetectActive);
			}
		}
		return alertasDetectadas;
	}

	private List<Location> obtenerLocalizacionesCercanasPais(
			List<ResolvedLocation> locations, List<Location> lugares) {
		Map<CountryCode, Integer> countCountry = new HashedMap();
		Integer maxCount = 0;
		// Contamos las apariciones de cada pais
		for (ResolvedLocation loc : locations) {
			CountryCode code = loc.getGeoname().getPrimaryCountryCode();
			Integer num = countCountry.containsKey(code) ? countCountry
					.get(code) + 1 : 1;
			countCountry.put(code, num);
			if (maxCount < num) {
				maxCount = num;
			}
		}
		// Seleccionamos el pais con mas apraciones o los que mas en caso de
		// empate
		List<CountryCode> countries = new ArrayList<CountryCode>();
		for (CountryCode code : countCountry.keySet()) {
			if (countCountry.get(code) >= maxCount) {
				countries.add(code);
			}
		}
		Set<Location> lugaresCercanos = Sets.newHashSet();
		// Comprobamos la inclusion de cada proveedor en los paises
		for (CountryCode code : countries) {
			for (Location lugar : lugares) {
				if (lugar.getCountry().equals(code)) {
					lugaresCercanos.add(lugar);
				}
			}
		}
		return Lists.newArrayList(lugaresCercanos);
	}

	private List<Location> obtenerLocalizacionesCercanas(
			List<ResolvedLocation> locations, List<Location> lugares) {
		Configuracion configuracion = configuracionRepository.findOne("conf");
		List<Location> lugaresCercanos = new ArrayList<Location>();
		for (ResolvedLocation resolvedLocation : locations) {
			for (Location location : lugares) {
				if (location.isNearOf(resolvedLocation.getGeoname()
						.getLatitude(), resolvedLocation.getGeoname()
						.getLongitude(),configuracion.getRadiusNear())) {
					lugaresCercanos.add(location);
				}
			}
		}
		return Lists.newArrayList(lugaresCercanos);
	}

	public Map<News, List<ResolvedLocation>> getLocations(List<News> listNews, boolean isTest) {
		Map<News, List<ResolvedLocation>> map = new HashMap<>();
		Configuracion configuracion = configuracionRepository.findOne("conf");
		String regExp = null;
		if (configuracion.getUsarPalabrasLugar() || isTest) {
			regExp = configuracion.getPaabrasLugar();
		}
		for (News news : listNews) {
			try {
				List<ResolvedLocation> locations = parser.parse(
						news.getContent(), regExp);
				if (locations.size() > 0)
					map.put(news, locations);
				else
					map.put(news, new ArrayList<ResolvedLocation>());
			} catch (Exception ex) {
				LOGGER.info("Se ha producido un error al obtener las localizaciones de la noticia");
				LOGGER.debug(ex.getMessage());
			}
			
		}
		return map;
	}
}