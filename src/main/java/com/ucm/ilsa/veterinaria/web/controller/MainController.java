package com.ucm.ilsa.veterinaria.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.webjars.WebJarAssetLocator;

import com.google.common.collect.Lists;
import com.neovisionaries.i18n.CountryCode;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.Statistics;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.StatisticsRepository;
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
	private ConfiguracionRepository configuracionRepository;

	public MainController() {
		this.assetLocator = new WebJarAssetLocator();
		this.menu = "Resumen";
	}

	@ModelAttribute("alertsUncheck")
	public List<Alert> getAllAlertsUnchecked() {
		return service.getAllAlertActive();
	}
	
	@ModelAttribute("risksUncheck")
	public List<Risk> getAllRisksUnchecked() {
		return serviceRisk.getAllAlertActive();
	}

	@ModelAttribute("alertsActivateToday")
	public List<Alert> getAllAlertsToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Alert> lista = service.getAlertDetectActivatedAfter(today);
		return lista;
	}
	
	@ModelAttribute("risksActivateInLast")
	public List<Risk> getAllRiskInLast() {
		Date now = new Date(System.currentTimeMillis());
		Configuracion conf = configuracionRepository.findOne("conf");
		Date date = new Date(now.getYear(), now.getMonth(), now.getDate()-conf.getDayRisks());
		List<Risk> lista = serviceRisk.getAlertDetectActivatedAfter(date);
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
		List<Alert> listAlert = service.getAllAlert();
		for (Alert alert : listAlert) {
			for (NewsDetect news : alert.getNewsDetect()) {
				if (!news.getFalPositive() && !news.getHistory()) {
					for (Location loc : news.getLocationsNear()) {
						listCountries.put(
								CountryCode.getByCode(loc.getCountry().name())
										.getAlpha3(), "rgba(255,87,34,1)");
					}
				}
			}
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		return listCountries;
	}

	@ModelAttribute("stat")
	public Statistics getStatisticsToday() {
		Statistics statistics = statisticsRepository.findOne(new java.sql.Date(
				System.currentTimeMillis()));
		if (statistics == null) {
			statistics = new Statistics(new java.sql.Date(
					System.currentTimeMillis()), 0, 0);
			statisticsRepository.save(statistics);
		}
		return statistics;
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
