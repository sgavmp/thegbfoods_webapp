package com.ucm.ilsa.veterinaria.web.controller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.WebJarAssetLocator;

import com.google.common.collect.Lists;
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.GraphData;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.ConfiguracionService;
import com.ucm.ilsa.veterinaria.service.FeedScraping;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;
import com.ucm.ilsa.veterinaria.statistics.TrendLine;
import com.ucm.ilsa.veterinaria.statistics.impl.PolyTrendLine;
import com.ucm.ilsa.veterinaria.web.controller.impl.ConfiguracionController;

import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;

@Controller
public class MainController extends BaseController {

	private static long ONE_DAY = 86400000;

	private WebJarAssetLocator assetLocator;

	@Autowired
	private AlertServiceImpl service;

	@Autowired
	private RiskServiceImpl serviceRisk;

	@Autowired
	private StatisticsRepository statisticsRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ConfiguracionService configuracionService;

	@Autowired
	private ConfiguracionService configuracion;

	@Autowired
	private NewsIndexService newsIndexService;

	@Autowired
	private FeedService feedService;
	
	@Autowired
	private FeedScraping feedScraping;

	public MainController() {
		this.assetLocator = new WebJarAssetLocator();
		this.menu = "Panel de Control";
	}

	@ModelAttribute("alertsScoreToday")
	public List<Object[]> getAllAlertsToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgDay(
				today.toString(), 5);
		return lista;
	}

	@ModelAttribute("alertsScoreWeek")
	public List<Object[]> getAllAlertsWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate() - 7);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(
				today.toString(), past.toString(), 7);
		return lista;
	}

	@ModelAttribute("risksScoreToday")
	public List<Object[]> getAllRisksToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgDay(
				today.toString(), 5);
		return lista;
	}

	@ModelAttribute("risksScoreWeek")
	public List<Object[]> getAllRisksWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate() - 7);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(
				today.toString(), past.toString(), 7);
		return lista;
	}

	@ModelAttribute("locationsScoreWeek")
	public List<Object[]> getAllLocationsWeek() {
		return statisticsRepository.getStatsOfLocationLastWeek();
	}

	@ModelAttribute("risksActivateInLast")
	public Set<Risk> getAllRiskInLast() {
		Date now = new Date(System.currentTimeMillis());
		Date date = new Date(now.getYear(), now.getMonth(), now.getDate()
				- configuracion.getConfiguracion().getDayRisks());
		Set<Risk> lista = serviceRisk.getAlertDetectActivatedAfter(date);
		for (Risk risk : lista) {
			for (NewsDetect news : risk.getNewsDetect()) {
				news.setAlertDetect(null);
			}
		}
		return lista;
	}

	@ModelAttribute("scrapStat")
	public List<Statistics> getScrapStatLastWeek() {
		return statisticsRepository.getStatsLastWeek();
	}

	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		List<Location> listLocation = (List<Location>) locationRepository
				.findAll();
		for (Location loc : listLocation) {
			listCountries.put(CountryCode.getByCode(loc.getCountry().name())
					.getAlpha3(), "rgba(255,87,34,1)");
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		return listCountries;
	}

	@ModelAttribute("graph")
	public List<Statistics> getGraphStat() {
		List<Statistics> graph = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		for (int i = 0; i < 7; i++) {
			java.sql.Date current = new java.sql.Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			graph.add(statistics);
		}
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/graph/alerts")
	public List<Object> getGraphDataForAlerts() {
		return Lists.newArrayList();
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/week")
	public GraphData getGraphDataForWeek() {
		List<String> dias = Lists.newArrayList("Domingo", "Lunes", "Martes",
				"Miercoles", "Jueves", "Viernes", "Sabado");
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			java.sql.Date current = new java.sql.Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
			graph.alertas.add(stat.getNumNews());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add("Hoy");
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/week")
	public GraphData getAlertGraphDataForWeek() {
		List<String> dias = Lists.newArrayList("Domingo", "Lunes", "Martes",
				"Miercoles", "Jueves", "Viernes", "Sabado");
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			java.sql.Date current = new java.sql.Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
			graph.alertas.add(stat.getAlertas());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add("Hoy");
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/by/week")
	public GraphData getAlertGraphDataByWeek() {
		GraphData graph = new GraphData();
		List<Object[]> stats = statisticsRepository.getStatsByWeek();
		int i = 0;
		for (Object[] stat : stats) {
			graph.alertas.add(stat[3].toString());
			graph.noticias.add(stat[0].toString());
			graph.labels.add("SEMANA " + stat[5]);
			if (i >= 5)
				break;
			i++;
		}
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/trend")
	public GraphData getAlertGraphDataTrend() {
		TrendLine t = new PolyTrendLine(2);
		GraphData graph = new GraphData();
		List<Statistics> stats = (List<Statistics>) statisticsRepository
				.findAll();
		SimpleDateFormat parserSDF = new SimpleDateFormat("YYYY-MM-dd");
		double[] x = new double[stats.size()];
		double[] y = new double[stats.size()];
		Date month = new Date(System.currentTimeMillis() - (31 * ONE_DAY));
		int i = 0;
		for (Statistics stat : stats) {
			try {
				if (stat.getAlertas() > 200)
					continue;
				x[i] = new Double(stat.getFecha().getTime() / 86400000);
				y[i] = new Double(stat.getAlertas());
				i++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		t.setValues(y, x);
		for (int z = 365; z >= 0; z--) {
			Date date = new Date(System.currentTimeMillis() - (z * ONE_DAY));
			date.setDate(date.getDate() - z);
			double p = t.predict(new Double(date.getTime() / 86400000));
			graph.labels.add(date.toLocaleString());
			graph.alertas.add(p);
		}
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/risk/week")
	public GraphData getRiskGraphDataForWeek() {
		List<String> dias = Lists.newArrayList("Domingo", "Lunes", "Martes",
				"Miercoles", "Jueves", "Viernes", "Sabado");
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			java.sql.Date current = new java.sql.Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
			graph.alertas.add(stat.getRiesgos());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add("Hoy");
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/risk/by/week")
	public GraphData getRiskGraphDataByWeek() {
		GraphData graph = new GraphData();
		List<Object[]> stats = statisticsRepository.getStatsByWeek();
		int i = 0;
		for (Object[] stat : stats) {
			graph.alertas.add(stat[2].toString());
			graph.noticias.add(stat[0].toString());
			graph.labels.add("SEMANA " + stat[5]);
			if (i >= 5)
				break;
			i++;
		}
		return graph;
	}

	@ModelAttribute("month")
	public GraphData getGraphDataForMonth() {
		GraphData graph = new GraphData();
		// List<Object[]> stats = statisticsRepository.getStatsByWeek();
		// while(stats.size()<4) {
		// Object[] t = {((Integer)stats.get(0)[0])-1,0,0};
		// stats.add(0, t);
		// }
		// for (Object[] stat : stats) {
		// graph.alertas.add(stat[1]);
		// graph.noticias.add(stat[2]);
		// graph.labels.add("Semana " + (Integer)stat[0]);
		// }
		return graph;
	}

	@ModelAttribute("year")
	public GraphData getGraphDataForYear() {
		List<String> dias = Lists.newArrayList("Enero", "Febrero", "Marzo",
				"Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
				"Octubre", "Noviembre", "Diciembre");
		GraphData graph = new GraphData();
		// List<Object[]> stats = statisticsRepository.getStatsByMonth();
		// while(stats.size()<12) {
		// Integer month = ((Integer)stats.get(0)[0])-1;
		// month = month<1?12:month;
		// Object[] t = {month,0,0};
		// stats.add(0, t);
		// }
		// for (Object[] stat : stats) {
		// graph.alertas.add(stat[1]);
		// graph.noticias.add(stat[2]);
		// graph.labels.add(dias.get((Integer)stat[0]-1));
		// }
		return graph;
	}

	@RequestMapping(value = { "", "/" })
	public String checkAlert() {
		return "resume";
	}

	@ResponseBody
	@RequestMapping("/webjars/{webjar}/**")
	public ResponseEntity<Object> locateWebjarAsset(
			@PathVariable String webjar, HttpServletRequest request) {
		try {
			String mvcPrefix = "/webjars/" + webjar + "/"; // This prefix must
															// match the mapping
															// path!
			String mvcPath = (String) request
					.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String fullPath = assetLocator.getFullPath(webjar,
					mvcPath.substring(mvcPrefix.length()));
			return new ResponseEntity<Object>(new ClassPathResource(fullPath),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String searchQueryTestForm(HttpServletRequest request, String query,
			Model model) {
		model.addAttribute("query", "");
		return "query";
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public String searchQueryTest(HttpServletRequest request, String query,
			Model model) {
		model.addAttribute("query", query);
		try {
			model.addAttribute("result", newsIndexService.search(query));
			return "query";
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", "El topic " + e.getTopic()
					+ " no existe.");
			return "query";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", e.toString());
			return "query";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error",
					"Se ha producido un error al validar la query.");
			return "query";
		} catch (Exception e) {
			model.addAttribute("error", "Se ha producido un error al buscar.");
			return "query";
		}
	}
	
	@RequestMapping(value = "/chart", method = RequestMethod.GET)
	public String chart(Model model) {
		return "chart";
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String loadHistory(Model model) {
		model.addAttribute("feeds", feedService.getAllFeed());
		return "load";
	}

	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String loadHistory(@RequestParam("feed") Feed feed,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		if (feed==null) {
			redirectAttributes.addFlashAttribute("error",
					"Tienes que elegir el website en el que almacenar las noticias.");
		} else if (!file.isEmpty()) {
			try {
				List<News> listNews = feedScraping.scrapingHistoric(feed, file.getInputStream());
				redirectAttributes.addFlashAttribute("news",listNews);
				redirectAttributes.addFlashAttribute("info",
						"Se han almacenado correctamente las noticias del hsitorico");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute(
						"error",
						"Se ha producido un error al procesar el archivo. Si el error persiste avise al administrador.");
			}
		} else {
			redirectAttributes.addFlashAttribute("error","Tienes que subir un archivo.");
		}

		return "redirect:/load";
	}
}
