package com.ucm.ilsa.veterinaria.service.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bericotech.clavin.ClavinException;
import com.bericotech.clavin.GeoParser;
import com.bericotech.clavin.GeoParserFactory;
import com.bericotech.clavin.extractor.AlphaExtractor;
import com.bericotech.clavin.gazetteer.CountryCode;
import com.bericotech.clavin.resolver.ResolvedLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.AlertLevel;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Topic;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.PointLocation;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.ScrapStatistics;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.domain.UpdateStateEnum;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.TopicRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.ScrapStatisticsRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.ConfiguracionService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsCheckFeedService;

@Service
public class NewsCheckFeedServiceImpl implements NewsCheckFeedService {

	@Autowired
	private NewsDetectRepository repositoryNewsDetect;

	@Autowired
	private AlertServiceImpl alertService;

	@Autowired
	private RiskServiceImpl riskService;

	@Autowired
	private PlaceAlertServiceImpl placeAlertService;

	@Autowired
	private ScrapStatisticsRepository statisticsRepository;

	@Autowired
	private TopicRepository dictionaryRepository;

	@Autowired
	private ConfiguracionService configuracion;

	@Autowired
	private FeedService service;

	private GeoParser parser;

	private final static Logger LOGGER = Logger
			.getLogger(NewsCheckFeedServiceImpl.class);

	@Autowired
	public NewsCheckFeedServiceImpl(ConfiguracionService configuracion)
			throws ClavinException {
		if (configuracion.getConfiguracion().getPathIndexClavin() != null && configuracion.getConfiguracion().getRunClavin()) {
			if (!configuracion.getConfiguracion().getPathIndexClavin().isEmpty()) {
				String path = configuracion.getConfiguracion().getPathIndexClavin();
				parser = GeoParserFactory.getDefault(path,
						new AlphaExtractor(), 50, 15, false);
				LOGGER.info("Indice CLAVIN-MOD: ".concat(path));
				LOGGER.info("GeoParser ".concat(parser != null ? "Iniciado"
						: "No iniciado"));
			}
		}
	}

	@Transactional
	public void checkNews(List<News> listNews, Feed feed) {
		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ feed.getName());
		Integer newsNum = listNews.size();
		Integer alertDetectNum = 0;
		Set<Location> lugares = placeAlertService.getAllLocations();
		Set<Alert> alertas = alertService.getAllAlert();
		Set<Risk> riesgos = riskService.getAllAlert();
		Set<AlertAbstract> detectar = Sets.newHashSet();
		detectar.addAll(alertas);
		detectar.addAll(riesgos);
		feed = service.setSateOfFeed(feed, UpdateStateEnum.DETECT_ALERTS);
		for (News news : listNews) { // Iteramos sobre cada noticia
			if (news == null)
				continue;
			if (news.getContent() == null) {// Comprobación de contenido de la
											// noticia
				LOGGER.error("No se ha conseguido el cuerpo de la noticia con URL: "
						+ news.getUrl() + " y del sitio: " + feed.getName());
				continue;
			}

			Map<AlertAbstract, NewsDetect> alertWithNews = checkIfNewsIsAlert(
					news, detectar, feed);
			for (AlertAbstract alerta : alertWithNews.keySet()) {
				NewsDetect newsDetect = alertWithNews.get(alerta);
				if (repositoryNewsDetect.findFirstByAlertDetectAndLink(alerta,
						newsDetect.getLink()) == null) {
					repositoryNewsDetect.save(newsDetect);
					alertDetectNum++;
					try {
						Hibernate.initialize(alerta.getNewsDetect());
						if (alerta instanceof Alert) {
							alerta = alertService.getOneById(alerta.getId());
							alerta.getNewsDetect().add(newsDetect);
							alertService.update((Alert) alerta);
						} else {
							alerta = riskService.getOneById(alerta.getId());
							alerta.getNewsDetect().add(newsDetect);
							riskService.update((Risk) alerta);
						}
					} catch (StaleObjectStateException ex) {
						LOGGER.error("Se ha producido un error al añadir una noticia del sitio "
								+ feed.getName()
								+ " a la alerta "
								+ alerta.getTitle());
						if (alerta instanceof Alert) {
							alerta = alertService.getOneById(alerta.getId());
							alerta.getNewsDetect().add(newsDetect);
							alertService.update((Alert) alerta);
						} else {
							alerta = riskService.getOneById(alerta.getId());
							alerta.getNewsDetect().add(newsDetect);
							riskService.update((Risk) alerta);
						}
					}
				}
			}
		}
		Date today = new Date(System.currentTimeMillis());
		ScrapStatistics statistics = statisticsRepository.findOne(today);
		if (statistics != null) {
			statistics.setTotal(statistics.getTotal()+newsNum);
		} else {
			statistics = new ScrapStatistics(today, newsNum);
		}
		statisticsRepository.save(statistics);
	}

	private Map<AlertAbstract, NewsDetect> checkIfNewsIsAlert(News news,
			Set<AlertAbstract> alertas, Feed feed) {
		Map<AlertAbstract, NewsDetect> newsDetectByAlert = Maps.newHashMap();
		NewsDetect newsDetect = null;
		Set<Location> lugares = placeAlertService.getAllLocations();
		Set<String> newsDetectPreliminar = Sets.newHashSet();
		Set<Topic> dictionaries = Sets.newHashSet(dictionaryRepository
				.findAll());
		// Solo se filtra por los terminos de alerta general si hay
		// alguno
		for (AlertAbstract alerta : alertas) {// Iteramos sobre cada alerta
												// o riesgo si el
			if (alerta instanceof Alert && !feed.getForAlerts()) {
				continue;
			} else if (alerta instanceof Risk && !feed.getForRisks()) {
				continue;
			}
			// Se comprueba los terminos negativos
			if (alerta.getWordsNegative() != null) {
				Set<String> terminosNegativos = Sets.newHashSet(alerta
						.getWordsNegative().split(","));
				boolean skip = false; // Variable para saltar la alerta en caso
										// de coincidencia de algun termino
										// negativo
				for (String word : terminosNegativos) {
					boolean caseSensitive = false;
					if (word.startsWith("\"") && word.endsWith("\"")) {
						word = word.replace("\"", "").trim();
						caseSensitive = true;
					} else {
						word = word.toLowerCase().trim();
					}
					String content = caseSensitive ? news.getContent() : news
							.getContent().toLowerCase();
					if (content.contains(" " + word + " ")
							|| content.startsWith(word + " ")
							|| content.endsWith(" " + word)) {
						skip = true;
						break;
					}
				}
				// Si ha habido coincidencia esta activa y salta
				if (skip) {
					continue;
				}
			}
			// Se comprueba los terminos positivos
			// sitio esta activo las alertas
			Set<String> wordsDetect = Sets.newHashSet();
			// Comprobamos todos los terminos y los almacenamos
			Set<String> terminos = Sets
					.newHashSet(alerta.getWords().split(","));
			terminos.add(alerta.getTitle());// El titulo de la alerta
											// también se busca
			if (alerta.getTitleEn() != null) {
				terminos.add(alerta.getTitleEn());// El titulo en ingles de la
													// alerta también se busca
			}
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
				if (content.contains(" " + word + " ")
						|| content.startsWith(word + " ")
						|| content.endsWith(" " + word)) {
					wordsDetect.add(word);
				}
			}
			if (newsDetectPreliminar.size() > 0) {
				wordsDetect.addAll(newsDetectPreliminar);
			}
			// Si se han detectado palabras de la alerta en la noticia
			if (wordsDetect.size() > 0) {
				newsDetect = new NewsDetect();
				newsDetect.setDatePub(news.getPubDate());
				newsDetect.setAlertDetect(alerta);
				newsDetect.setLink(news.getUrl());
				newsDetect.setSite(feed);
				newsDetect.setTitle(news.getTitle());
				//newsDetect.setWordsDetect(wordsDetect);
				if (parser == null) {
					if (configuracion.getConfiguracion().getPathIndexClavin() != null) {
						if (!configuracion.getConfiguracion().getPathIndexClavin().isEmpty()) {
							String path = configuracion.getConfiguracion().getPathIndexClavin();
							try {
								parser = GeoParserFactory.getDefault(path,
										new AlphaExtractor(), 50, 15, false);
							} catch (ClavinException e) {
								LOGGER.error("Error iniciando Clavin");
							}
							LOGGER.info("Indice CLAVIN-MOD: ".concat(path));
							LOGGER.info("GeoParser "
									.concat(parser != null ? "Iniciado"
											: "No iniciado"));
						}
					}
				}
				if (parser != null) {
					if (alerta instanceof Alert) {
						feed = service.setSateOfFeed(feed,
								UpdateStateEnum.DETECT_PLACES);
						Alert alert = (Alert) alerta;
						String regExp = null;
						Set<ResolvedLocation> locationsAp = null;
						try {
							locationsAp = Sets.newHashSet(parser.parse(
									news.getContent(), regExp));
						} catch (Exception e) {
							LOGGER.error("Se ha producido un error al obtener las localizaciones de la noticia");
							LOGGER.debug(e.getMessage());
						}
						// Si la alerta es de Nivel 2 o 3 se detecta los
						// proveedores por el pais
//						if (alert.getType().equals(AlertLevel.orange)
//								|| alert.getType().equals(AlertLevel.red)) {
//							// Obtenemos los lugares que coincidan con el pais
//							// de
//							// las localizaciones encontradas
//							newsDetect
//									.setLocationsNear(obtenerLocalizacionesCercanasPais(
//											locationsAp, lugares));
//						} else { // Si la alerta es de Nivel 1 se detecta los
//									// proveedores por cercania a los puntos
//									// encontrados
//							// Obtenemos los lugares que entren en el radio de
//							// la localizacion
//							newsDetect
//									.setLocationsNear(obtenerLocalizacionesCercanas(
//											locationsAp, lugares));
//						}
						// Guardamos los lugares encontrados de la noticia
						Set<PointLocation> puntos = Sets.newHashSet();
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
					}
				} else {
					LOGGER.error("Error iniciando Clavin");
				}
				boolean mark = false; // Variable para marcar una noticia como
										// importante
				for (Topic dict : dictionaries) {
					Set<String> terminosDict = Sets.newHashSet(dict.getWords()
							.split(","));
					String terminosDetect = "";
					for (String word : terminosDict) {
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
							terminosDetect = terminosDetect.concat(word)
									.concat(",");
							mark = true;
						}
					}
				}
				newsDetect.setMark(mark);
				newsDetectByAlert.put(alerta, newsDetect);
			}
		}
		return newsDetectByAlert;
	}

	public Set<AlertAbstract> checkNews(News news, Feed feed) {
		LOGGER.info("Iniciada comprobacion de alerta de cercania y palabras de filtro para el sitio: "
				+ feed.getName());
		Set<AlertAbstract> alertDetect = Sets.newHashSet();
		Set<Location> lugares = placeAlertService.getAllLocations();
		Set<Alert> alertas = alertService.getAllAlert();
		Set<Risk> riesgos = riskService.getAllAlert();
		Set<AlertAbstract> detectar = Sets.newHashSet();
		detectar.addAll(alertas);
		detectar.addAll(riesgos);
		Map<AlertAbstract, NewsDetect> alertWithNews = checkIfNewsIsAlert(news,
				detectar, feed);
		for (AlertAbstract alerta : alertWithNews.keySet()) {
			NewsDetect newsDetect = alertWithNews.get(alerta);
			alerta.getNewsDetect().clear();
			alerta.getNewsDetect().add(newsDetect);
			alertDetect.add(alerta);
		}
		return alertDetect;
	}

	private Set<Location> obtenerLocalizacionesCercanasPais(
			Set<ResolvedLocation> locations, Set<Location> lugares) {
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
		return lugaresCercanos;
	}

	private Set<Location> obtenerLocalizacionesCercanas(
			Set<ResolvedLocation> locations, Set<Location> lugares) {
		Set<Location> lugaresCercanos = Sets.newHashSet();
		for (ResolvedLocation resolvedLocation : locations) {
			for (Location location : lugares) {
				if (location.isNearOf(resolvedLocation.getGeoname()
						.getLatitude(), resolvedLocation.getGeoname()
						.getLongitude(), configuracion.getConfiguracion().getRadiusNear())) {
					lugaresCercanos.add(location);
				}
			}
		}
		return lugaresCercanos;
	}

	public Map<News, List<ResolvedLocation>> getLocations(List<News> listNews,
			boolean isTest) {
		Map<News, List<ResolvedLocation>> map = new HashMap<>();
		String regExp = null;
		if (parser == null) {
			if (configuracion.getConfiguracion().getPathIndexClavin() != null) {
				if (!configuracion.getConfiguracion().getPathIndexClavin().isEmpty()) {
					String path = configuracion.getConfiguracion().getPathIndexClavin();
					try {
						parser = GeoParserFactory.getDefault(path,
								new AlphaExtractor(), 50, 15, false);
					} catch (ClavinException e) {
						LOGGER.error("Error iniciando Clavin");
					}
					LOGGER.info("Indice CLAVIN-MOD: ".concat(path));
					LOGGER.info("GeoParser "
							.concat(parser != null ? "Iniciado"
									: "No iniciado"));
				}
			}
		}
		if (parser!=null) {
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
		} else {
			LOGGER.error("Error iniciando Clavin");
		}
		return map;
	}
}
