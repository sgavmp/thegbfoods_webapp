package com.ucm.ilsa.veterinaria.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SegmentInfos;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.TermQueryBuilder;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SimpleCollector;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockFactory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertAbstract;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.FeedPlaceEnum;
import com.ucm.ilsa.veterinaria.domain.FeedTypeEnum;
import com.ucm.ilsa.veterinaria.domain.Location;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.topic.AllHitsCollector;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.repository.LocationRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;
import com.ucm.ilsa.veterinaria.repository.NewsRepository;
import com.ucm.ilsa.veterinaria.scheduler.AlertTaskContainer;
import com.ucm.ilsa.veterinaria.scheduler.SchedulerService;
import com.ucm.ilsa.veterinaria.service.ConfiguracionService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructor;
import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructorSemantics;

@Service
public class NewsIndexServiceImpl implements NewsIndexService, Runnable {

	private final static Logger LOGGER = Logger
			.getLogger(NewsIndexServiceImpl.class);

	private Directory allDirectory;
	private IndexWriter allIndex;
	private IndexReader readAllIndex;
	private boolean newsPending = false, taskScheduled = false;

	@Autowired
	private ConfiguracionService configuracion;

	@Autowired
	private AlertServiceImpl alertService;

	@Autowired
	private RiskServiceImpl riskService;

	@Autowired
	private FeedService feedService;

	@Autowired
	private TopicManager topicManager;

	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private NewsDetectRepository newsDetectRepository;

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Autowired
	private LocationRepository locationRepository;

	@PostConstruct
	public void initDirectory() {
		if (!"".equals(configuracion.getConfiguracion().getPathIndexNews())
				&& null != configuracion.getConfiguracion().getPathIndexNews()) {
			getDirectory();
			getWriter();
			LOGGER.info("Indexado de noticias activado.");
		} else {
			LOGGER.info("Indexado de noticias no activado. Activelo en Configuración.");
		}
	}

	private Directory getDirectory() {
		if (this.allDirectory == null) {
			try {
				this.allDirectory = FSDirectory.open(new File(configuracion
						.getConfiguracion().getPathIndexNews().concat("/all"))
						.toPath());
			} catch (IOException e) {
				LOGGER.error("Error al abrir el indice o el directorio -> "
						+ e.getMessage());
			}
		}
		return this.allDirectory;
	}

	private IndexWriter getWriter() {
		if (this.allIndex == null) {
			try {
				this.allIndex = new IndexWriter(getDirectory(),
						new IndexWriterConfig(getAnalyzer()));
			} catch (IOException e) {
				LOGGER.error("Error al abrir el indice o el directorio -> "
						+ e.getMessage());
			}
		} else if (!this.allIndex.isOpen()) {
			try {
				this.allIndex = new IndexWriter(getDirectory(),
						new IndexWriterConfig(getAnalyzer()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error("Error al abrir el indice o el directorio -> "
						+ e.getMessage());
			}
		}
		return this.allIndex;
	}

	public static Analyzer getAnalyzer() {
		Map<String, Analyzer> aMap = new HashMap<String, Analyzer>();
		Analyzer sa = new StandardAnalyzer(CharArraySet.EMPTY_SET);
		aMap.put(News.fieldTitleNoCase, sa);
		aMap.put(News.fieldTitle, new WhitespaceAnalyzer());
		aMap.put(News.fieldBodyNoCase, sa);
		aMap.put(News.fieldBody, new WhitespaceAnalyzer());
		aMap.put(News.fieldUrl, new WhitespaceAnalyzer());
		aMap.put(News.fieldSite, new WhitespaceAnalyzer());
		return new PerFieldAnalyzerWrapper(sa, aMap);
	}

	private void addDocument(News news) throws Exception {
		if (news != null) {
			try {
				Document d = new Document();
				d.add(new StringField(News.fieldUrl, news.getUrl(),
						Field.Store.YES));
				d.add(new TextField(News.fieldTitle, news.getTitle(),
						Field.Store.YES));
				d.add(new TextField(News.fieldTitleNoCase, news.getTitle(),
						Field.Store.NO));
				d.add(new TextField(News.fieldBody, news.getContent(),
						Field.Store.YES));
				d.add(new TextField(News.fieldBody, news.getContent(),
						Field.Store.NO));
				d.add(new StringField(News.fieldDatePub, DateTools
						.dateToString(news.getPubDate(), Resolution.MINUTE),
						Field.Store.YES));
				d.add(new StringField(News.fieldSite,
						news.getSite().toString(), Field.Store.YES));
				d.add(new StringField(News.fieldDateCreate, DateTools
						.dateToString(new Date(), Resolution.MINUTE),
						Field.Store.YES));
				Feed feed = feedService.getFeedByCodeName(news.getSite());
				if (feed.getFeedType() == null) {
					feed.setFeedType(FeedTypeEnum.general);
				}
				if (feed.getFeedPlace() != null) {
					if (feed.getFeedPlace().isEmpty()) {
						feed.getFeedPlace().add(FeedPlaceEnum.general);
					}
				} else {
					feed.setFeedPlace(Lists.newArrayList(FeedPlaceEnum.general));
				}
				d.add(new StringField(News.fieldSiteType, feed.getFeedType()
						.getValue().toString(), Field.Store.YES));
				for (FeedPlaceEnum type : feed.getFeedPlace())
					d.add(new StringField(News.fieldSiteLoc, type.getValue()
							.toString(), Field.Store.YES));
				getWriter().updateDocument(
						new Term(News.fieldUrl, news.getUrl()), d);
				feedService.updateFeed(feed);
			} catch (NullPointerException ex) {
				LOGGER.error("Error al almacenar noticia.");
			}
		}
	}

	private List<NewsDetect> search(Query q, IndexSearcher searcher,
			AlertAbstract alert) throws Exception {
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document d = searcher.doc(docId);
			NewsDetect newsDocument = new NewsDetect();
			newsDocument.setAlertDetect(alert);
			newsDocument.setDatePub(DateTools.stringToDate(d.get("datePub")));
			newsDocument.setLink(d.get("id"));
			newsDocument.setScore(docsA[i].score);
			newsDocument.setScoreLucene(docsA[i].score);
			newsDocument.setSite(feedService.getFeedByCodeName(new Long(d
					.get("feed"))));
			if (alert==null || newsDocument.getSite()==null)
				LOGGER.error("Error");
			newsDocument.setTitle(d.get("title"));
			listNewsDetect.add(newsDocument);
		}
		return listNewsDetect;
	}

	@Override
	public void indexNews(News news) throws Exception {
		addDocument(news);
		getWriter().commit();
	}

	@Override
	public void indexNews(List<News> lNews) throws Exception {
		for (News news : lNews) {
			addDocument(news);
		}
		getWriter().commit();
	}

	@Override
	public void removeNews(News news) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeNews(List<News> news) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNews(News news) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNews(List<News> news) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<News> search(AlertAbstract alert) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopDirectory() throws IOException {
		getWriter().commit();
		getWriter().close();
		getDirectory().close();
		this.allDirectory = null;
		LOGGER.info("Se han cerrado los indices");
	}

	@Transactional
	@Override
	public boolean markNewNews(Feed feed) {
		this.newsPending = true;
		if (!taskScheduled) {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 300);
			scheduler.schedule(this, startTime);
			taskScheduled = true;
		}
		return true;
	}

	private Query createQuery(AlertAbstract alert, Long from, Long to) throws UnsupportedEncodingException {
		InputStreamReader alertDefinition = new InputStreamReader(new ByteArrayInputStream(alert.getWords()
				.getBytes()),"UTF-8");
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alertDefinition = new InputStreamReader(new ByteArrayInputStream(alert.getWords().getBytes()),"UTF-8");
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query q2 = TermRangeQuery.newStringRange("dateCreate",
				DateTools.timeToString(from, Resolution.MINUTE),
				DateTools.timeToString(to, Resolution.MINUTE), false, true);
		BooleanClause op1 = new BooleanClause(q, BooleanClause.Occur.SHOULD);
		BooleanClause op2 = new BooleanClause(q1, BooleanClause.Occur.SHOULD);
		BooleanClause op = new BooleanClause(q2, BooleanClause.Occur.MUST);
		BooleanQuery.Builder b = new BooleanQuery.Builder();
		b.add(op1);
		b.add(op2);
		Query qS = b.build();
		BooleanClause opS = new BooleanClause(qS, BooleanClause.Occur.MUST);
		BooleanClause opC = new BooleanClause(q2, BooleanClause.Occur.MUST);
		b = new BooleanQuery.Builder();
		b.add(opS);
		b.add(opC);
		Query query = b.build();
		return query;
	}

	private Query createQuery(Location location, Long from, Long to) throws UnsupportedEncodingException {
		InputStreamReader alertDefinition = new InputStreamReader(new ByteArrayInputStream(location
				.getQuery().getBytes()),"UTF-8");
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alertDefinition = new InputStreamReader(new ByteArrayInputStream(location.getQuery()
				.getBytes()),"UTF-8");
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query q2 = TermRangeQuery.newStringRange("dateCreate",
				DateTools.timeToString(from, Resolution.MINUTE),
				DateTools.timeToString(to, Resolution.MINUTE), false, true);
		BooleanClause op1 = new BooleanClause(q, BooleanClause.Occur.SHOULD);
		BooleanClause op2 = new BooleanClause(q1, BooleanClause.Occur.SHOULD);
		BooleanClause op = new BooleanClause(q2, BooleanClause.Occur.MUST);
		BooleanQuery.Builder b = new BooleanQuery.Builder();
		b.add(op1);
		b.add(op2);
		Query qS = b.build();
		BooleanClause opS = new BooleanClause(qS, BooleanClause.Occur.MUST);
		BooleanClause opC = new BooleanClause(q2, BooleanClause.Occur.MUST);
		b = new BooleanQuery.Builder();
		b.add(opS);
		b.add(opC);
		Query query = b.build();
		return query;
	}

	@Override
	public void run() {
		taskScheduled = false;
		LOGGER.info("Se inicia el proceso de busqueda de alertas.");
		if (this.newsPending) {
			try {
				updateIndex();
			} catch (Exception e) {
				LOGGER.error("Error al actualizar el indice.");
			}
			this.newsPending = false;
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		// Proceso de detección de localizaciones
		Iterable<Location> locations = locationRepository.findAll();
		for (Location loc : locations) {
			Long from = loc.getUltimaRecuperacion() != null ? loc
					.getUltimaRecuperacion().getTime() : 0;
			Long to = new Date().getTime();
			Query q;
			try {
				q = createQuery(loc, from, to);
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error("Error al construir la query de loc:" + loc.getName());
				continue;
			}
			try {
				List<String> listNewsDetect = searchLocation(q, searcher, loc);
				loc = locationRepository.findOne(loc.getId());
				loc.setUltimaRecuperacion(new Timestamp(to));
				loc.getNews().addAll(listNewsDetect);
				locationRepository.save(loc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Proceso de detección de alertas
		Set<AlertAbstract> tasks = Sets.newHashSet();
		tasks.addAll(alertService.getAllAlert());
		tasks.addAll(riskService.getAllAlert());
		LOGGER.error("Hay " + tasks.size() + " alertas para analizar.");
		for (AlertAbstract alert : tasks) {
			Long from = alert.getUltimaRecuperacion() != null ? alert
					.getUltimaRecuperacion().getTime() : 0;
			Long to = new Date().getTime();
			Query q;
			try {
				q = createQuery(alert, from, to);
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error("Error al construir la query de alerta:" + alert.getTitle());
				continue;
			}
			try {
				List<NewsDetect> listNewsDetect = search(q, searcher, alert);
				if (!listNewsDetect.isEmpty()) {
					LOGGER.info("Se han detectado: " + listNewsDetect.size()
							+ " posibles alertas de: " + alert.getTitle());
					newsDetectRepository.save(listNewsDetect);
				}
				alert.setUltimaRecuperacion(new Timestamp(to));
				if (alert instanceof Alert) {
					alert = alertService.getOneById(alert.getId());
					alert.setUltimaRecuperacion(new Timestamp(to));
					alertService.update((Alert) alert);
				} else {
					alert = riskService.getOneById(alert.getId());
					alert.setUltimaRecuperacion(new Timestamp(to));
					riskService.update((Risk) alert);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas.");
	}

	private List<String> searchLocation(Query q, IndexSearcher searcher,
			Location loc) throws Exception {
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		List<String> listNewsDetect = Lists.newArrayList();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document d = searcher.doc(docId);
			NewsDetect newsDocument = new NewsDetect();
			newsDocument.setTitle(d.get("title"));
			listNewsDetect.add(d.get("id"));
		}
		return listNewsDetect;
	}

	private void updateIndex() throws Exception {
		LOGGER.info("Añadiendo nuevas noticias al indice.");
		int i = 0;
		for (News news : newsRepository.findAll()) {
			addDocument(news);
			i++;
			newsRepository.delete(news);
		}
		getWriter().commit();
		LOGGER.info("Se han añadido " + i + " noticias al indice.");
	}

	@Override
	@Transactional
	public void resetAllAlerts() throws IOException {
		LOGGER.info("Se inicia el proceso de busqueda de alertas en todo el indice.");
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		// newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		Set<Alert> alertas = alertService.getAllAlert();
		Set<Risk> riesgos = riskService.getAllAlert();
		LOGGER.info("Hay " + alertas.size() + " alertas para analizar.");
		LOGGER.info("Hay " + riesgos.size() + " riesgos para analizar.");
		for (Alert alert : alertas) {
			resetAlertInter(alert, searcher);
		}
		for (Risk alert : riesgos) {
			resetAlertInter(alert, searcher);
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas en todo el indice.");

	}

	private void resetAlertInter(AlertAbstract alert, IndexSearcher searcher) throws UnsupportedEncodingException {
		Long to = new Date().getTime();
		Query q = createQuery(alert, 0L, to);
		try {
			List<NewsDetect> listNewsDetect = search(q, searcher, alert);
			LOGGER.info("Se borran todas las alertas detectadas anteriores.");
			List<NewsDetect> newsToRemove = Lists.newArrayList(alert
					.getNewsDetect());
			newsDetectRepository.save(listNewsDetect);
			alert.getNewsDetect().clear();
			alert.getNewsDetect().addAll(listNewsDetect);
			if (alert instanceof Alert) {
				alert = alertService.getOneById(alert.getId());
				alert.setUltimaRecuperacion(new Timestamp(to));
				alertService.update((Alert) alert);
			} else {
				alert = riskService.getOneById(alert.getId());
				alert.setUltimaRecuperacion(new Timestamp(to));
				riskService.update((Risk) alert);
			}
			for (NewsDetect news : newsToRemove) {
				newsDetectRepository.delete(news.getId());
			}
			if (listNewsDetect.size() > 0)
				LOGGER.info("Se han detectado: " + listNewsDetect.size()
						+ " posibles alertas de: " + alert.getTitle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void resetAlert(AlertAbstract alert) throws IOException {
		LOGGER.info("Se inicia el proceso de busqueda de alertas en todo el indice.");
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		resetAlertInter(alert, searcher);
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas en todo el indice.");
	}

	private void resetLocationInter(Location loc, IndexSearcher searcher) throws UnsupportedEncodingException {
		Long to = new Date().getTime();
		Query q = createQuery(loc, 0L, to);
		try {
			List<String> listNewsDetect = searchLocation(q, searcher, loc);
			loc = locationRepository.findOne(loc.getId());
			loc.setUltimaRecuperacion(new Timestamp(to));
			if (loc.getNews() == null)
				loc.setNews(listNewsDetect);
			else
				loc.getNews().addAll(listNewsDetect);
			locationRepository.save(loc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void resetAllLocation() throws IOException {
		LOGGER.info("Se inicia el proceso de busqueda de localizaciones en todo el indice.");
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		// newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		// Proceso de detección de localizaciones
		Iterable<Location> locations = locationRepository.findAll();
		for (Location loc : locations) {
			resetLocationInter(loc, searcher);
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}
		LOGGER.info("Finaliza el proceso de busqueda de localizaciones en todo el indice.");

	}

	@Override
	public void resetLocation(Location loc) throws IOException {
		LOGGER.info("Se inicia el proceso de busqueda de alertas en todo el indice.");
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		resetLocationInter(loc, searcher);
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas en todo el indice.");
	}

	@Override
	public void updateIndex(Feed feed) throws Exception {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Query q = new TermQuery(new Term(News.fieldSite, feed.getId()
				.toString()));
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document doc = searcher.doc(docId);
			doc.removeFields(News.fieldSiteType);
			doc.removeFields(News.fieldSiteLoc);
			doc.add(new StringField(News.fieldSiteType, feed.getFeedType()
					.getValue().toString(), Field.Store.YES));
			for (FeedPlaceEnum type : feed.getFeedPlace())
				doc.add(new StringField(News.fieldSiteLoc, type.getValue()
						.toString(), Field.Store.YES));
			String url = doc.get("id").toString();
			try {
				getWriter().updateDocument(new Term("id", url), doc);
			} catch (Exception e) {
				LOGGER.error("Error al actualizar el documento con id " + docId
						+ " y url " + url);
			}
		}
		reader.close();
		getWriter().commit();
	}

	@Override
	public List<NewsDetect> search(String query) throws Exception {
		LOGGER.info("Se inicia el proceso de busqueda de alertas de prueba.");
		Alert alert = new Alert();
		alert.setWords(query);
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Long to = new Date().getTime();
		Query q = createQuery(alert, 0L, to);
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		try {
			listNewsDetect = search(q, searcher, alert);
			LOGGER.info("Se han detectado: " + listNewsDetect.size()
					+ " posibles alertas de prueba");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas de prueba.");
		if (listNewsDetect.size() > 21)
			return listNewsDetect.subList(0, 20);
		else
			return listNewsDetect;
	}

	@Override
	public void removeFeedFromIndex(Feed feed) throws Exception {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Query q = new TermQuery(new Term(News.fieldSite, feed.getId()
				.toString()));
		try {
			getWriter().deleteDocuments(q);
		} catch (Exception e) {
			LOGGER.error("Error al eliminar las noticias del websites: " + feed.getId());
		}
		reader.close();
		getWriter().commit();

	}

}
