package com.ucm.ilsa.veterinaria.web.controller;

import java.sql.Date;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.WebJarAssetLocator;

import com.google.common.collect.Lists;
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.GraphData;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
import com.ucm.ilsa.veterinaria.service.ConfiguracionService;
import com.ucm.ilsa.veterinaria.service.impl.AlertServiceImpl;
import com.ucm.ilsa.veterinaria.service.impl.RiskServiceImpl;
import com.ucm.ilsa.veterinaria.web.controller.impl.admin.ConfiguracionController;

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
	private ConfiguracionService configuracionService;
	
	@Autowired 
	private ConfiguracionService configuracion;

	public MainController() {
		this.assetLocator = new WebJarAssetLocator();
		this.menu = "Panel de Control";
	}

	@ModelAttribute("alertsUncheck")
	public Set<Alert> getAllAlertsUnchecked() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		Set<Alert> lista = service.getAlertDetectActivatedAfter(today);
		return lista;
	}
	
	@ModelAttribute("risksUncheck")
	public Set<Risk> getAllRisksUnchecked() {
		return serviceRisk.getAllAlertActive();
	}

	@ModelAttribute("alertsScoreToday")
	public List<Object[]> getAllAlertsToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgDay(today.toString(),5);
		return lista;
	}
	
	@ModelAttribute("alertsScoreWeek")
	public List<Object[]> getAllAlertsWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@ModelAttribute("risksScoreToday")
	public List<Object[]> getAllRisksToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgDay(today.toString(),5);
		return lista;
	}
	
	@ModelAttribute("risksScoreWeek")
	public List<Object[]> getAllRisksWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@ModelAttribute("risksActivateInLast")
	public Set<Risk> getAllRiskInLast() {
		Date now = new Date(System.currentTimeMillis());
		Date date = new Date(now.getYear(), now.getMonth(), now.getDate()-configuracion.getConfiguracion().getDayRisks());
		Set<Risk> lista = serviceRisk.getAlertDetectActivatedAfter(date);
		for (Risk risk : lista) {
			for (NewsDetect news : risk.getNewsDetect()) {
				news.setAlertDetect(null);
			}
		}
		return lista;
	}
	

	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
//		Set<Alert> listAlert = service.getAllAlert();
//		for (Alert alert : listAlert) {
//			for (NewsDetect news : alert.getNewsDetect()) {
//				if (!news.getFalPositive() && !news.getHistory()) {
//					for (Location loc : news.getLocationsNear()) {
//						listCountries.put(
//								CountryCode.getByCode(loc.getCountry().name())
//										.getAlpha3(), "rgba(255,87,34,1)");
//					}
//				}
//			}
//		}
//		listCountries.put("defaultFill", "rgba(200,200,200,1)");
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
	
	@ModelAttribute("week")
	public GraphData getGraphDataForWeek() {
		List<String> dias = Lists.newArrayList("Domingo","Lunes","Martes","Miercoles","Jueves","Viernes","Sabado");
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
			graph.alertas.add(stat.getNumAlerts());
			graph.noticias.add(stat.getNumNews());
			graph.labels.add(dias.get(stat.getDate().getDay()).concat(" ").concat(String.valueOf(stat.getDate().getDate())));
		}
		graph.labels.remove(graph.labels.size()-1);
		graph.labels.add("Hoy");
		return graph;
	}
	
	@ModelAttribute("month")
	public GraphData getGraphDataForMonth() {
		GraphData graph = new GraphData();
//		List<Object[]> stats = statisticsRepository.getStatsByWeek();
//		while(stats.size()<4) {
//			Object[] t = {((Integer)stats.get(0)[0])-1,0,0};
//			stats.add(0, t);
//		}
//		for (Object[] stat : stats) {
//			graph.alertas.add(stat[1]);
//			graph.noticias.add(stat[2]);
//			graph.labels.add("Semana " + (Integer)stat[0]);
//		}
		return graph;
	}
	
	@ModelAttribute("year")
	public GraphData getGraphDataForYear() {
		List<String> dias = Lists.newArrayList("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre");
		GraphData graph = new GraphData();
//		List<Object[]> stats = statisticsRepository.getStatsByMonth();
//		while(stats.size()<12) {
//			Integer month = ((Integer)stats.get(0)[0])-1;
//			month = month<1?12:month;
//			Object[] t = {month,0,0};
//			stats.add(0, t);
//		}
//		for (Object[] stat : stats) {
//			graph.alertas.add(stat[1]);
//			graph.noticias.add(stat[2]);
//			graph.labels.add(dias.get((Integer)stat[0]-1));
//		}
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
}
