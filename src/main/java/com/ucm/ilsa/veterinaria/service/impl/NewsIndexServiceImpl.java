package com.ucm.ilsa.veterinaria.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
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
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SimpleCollector;
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
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Risk;
import com.ucm.ilsa.veterinaria.domain.topic.AllHitsCollector;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
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

	private Directory ramDirectory, activeDirectory, allDirectory;
	private IndexWriter ramIndex, activeIndex, allIndex;
	private IndexReader readRamIndex, readActiveIndex, readAllIndex;
	private boolean newsPending = false;
	private Set<AlertAbstract> taskRunning = Sets.newHashSet(),
			taskPending = Sets.newHashSet();

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

	@PostConstruct
	public void initDirectory() {
		if (!"".equals(configuracion.getConfiguracion().getPathIndexNews())
				&& null != configuracion.getConfiguracion().getPathIndexNews()) {
			try {
				this.ramDirectory = new RAMDirectory();
				// this.activeDirectory = new MMapDirectory(new
				// File(configuracion.getConfiguracion().getPathIndexNews().concat("/daily")).toPath());
				this.allDirectory = FSDirectory.open(new File(configuracion
						.getConfiguracion().getPathIndexNews().concat("/all"))
						.toPath());
				this.ramIndex = new IndexWriter(ramDirectory,
						new IndexWriterConfig(getAnalyzer()));
				// this.activeIndex = new IndexWriter(activeDirectory, new
				// IndexWriterConfig(makeAnalyzer()));
				this.allIndex = new IndexWriter(allDirectory,
						new IndexWriterConfig(getAnalyzer()));
			} catch (IOException ex) {
				// TODO
			}
			LOGGER.info("Indexado de noticias activado.");
		} else {
			LOGGER.info("Indexado de noticias no activado. Activelo en Configuración.");
		}
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
				d.add(new StringField("id", news.getUrl(), Field.Store.YES));
				d.add(new TextField("title", news.getTitle(), Field.Store.YES));
				d.add(new TextField("titleN", news.getTitle(), Field.Store.NO));
				d.add(new TextField("body", news.getContent(), Field.Store.YES));
				d.add(new TextField("bodyN", news.getContent(), Field.Store.NO));
				d.add(new StringField("datePub", DateTools.dateToString(
						news.getPubDate(), Resolution.MINUTE), Field.Store.YES));
				d.add(new StringField("feed", news.getSite().toString(),
						Field.Store.YES));
				d.add(new StringField("dateCreate", DateTools.dateToString(
						new Date(), Resolution.MINUTE), Field.Store.YES));
				// ramIndex.addDocument(d);
				// activeIndex.addDocument(d);
				// activeIndex.commit();
				allIndex.addDocument(d);
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
			newsDocument.setSite(feedService.getFeedByCodeName(new Long(d
					.get("feed"))));
			newsDocument.setTitle(d.get("title"));
			listNewsDetect.add(newsDocument);
		}
		return listNewsDetect;
	}

	@Override
	public void indexNews(News news) throws Exception {
		addDocument(news);
	}

	@Override
	public void indexNews(List<News> lNews) throws Exception {
		for (News news : lNews) {
			addDocument(news);
		}
		allIndex.commit();
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
	public void emptyRamIndex() throws IOException {
		ramIndex.deleteAll();
		ramIndex.commit();
	}

	@Override
	public void emptyNewsIndexBefore(Integer days) {

	}

	@Override
	public List<News> search(AlertAbstract alert) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopDirectory() throws IOException {
		allIndex.commit();
		allIndex.close();
		allDirectory.close();
		ramIndex.close();
		ramDirectory.close();
		LOGGER.info("Se han cerrado los indices");
	}

	@Transactional
	@Override
	public boolean markNewNews(Feed feed) {
		this.newsPending = true;
		Set<AlertAbstract> tasks = Sets.newHashSet();
		if (feed.getForAlerts()) {
			tasks.addAll(alertService.getAllAlert());
		}
		if (feed.getForRisks()) {
			tasks.addAll(riskService.getAllAlert());
		}
		if (taskRunning.isEmpty()) {
			if (taskPending.isEmpty()) {
				taskRunning.addAll(tasks);
				Date startTime = new Date();
				startTime.setTime(startTime.getTime() + 1000 * 300);
				scheduler.schedule(this, startTime);
				return true;
			} else {
				taskPending.addAll(tasks);
				return false;
			}
		} else {
			taskPending.addAll(tasks);
			return false;
		}
	}

	private Query createQuery(AlertAbstract alert) {
		InputStream alertDefinition = new ByteArrayInputStream(alert.getWords()
				.getBytes());
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alertDefinition = new ByteArrayInputStream(alert.getWords().getBytes());
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BooleanClause op1 = new BooleanClause(q, BooleanClause.Occur.SHOULD);
		BooleanClause op2 = new BooleanClause(q1, BooleanClause.Occur.SHOULD);
		BooleanQuery.Builder b = new BooleanQuery.Builder();
		b.add(op1);
		b.add(op2);
		Query query = b.build();
		return query;
	}

	@Override
	public void run() {
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
			reader = DirectoryReader.open(ramDirectory);
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		if (taskRunning.isEmpty()) {
			LOGGER.error("Se añaden las tareas a la cola de tareas a ejecutar.");
			taskRunning.addAll(taskPending);
			taskPending.clear();
		}
		LOGGER.error("Hay " + taskRunning.size() + " alertas para analizar.");
		for (AlertAbstract alert : taskRunning) {
			Query q = createQuery(alert);
			try {
				List<NewsDetect> listNewsDetect = search(q, searcher, alert);

				LOGGER.info("Se han detectado: " + listNewsDetect.size()
						+ " posibles alertas de: " + alert.getTitle());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		taskRunning.clear();
		try {
			reader.close();
			emptyRamIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!taskPending.isEmpty()) {
			// Reprogramamos la tarea para dentro de 5 minutos
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 300);
			scheduler.schedule(this, startTime);
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas.");
	}

	private void updateIndex() throws Exception {
		LOGGER.info("Añadiendo nuevas noticias al indice.");
		int i = 0;
		for (News news : newsRepository.findAll()) {
			addDocument(news);
			i++;
			newsRepository.delete(news);
		}
		ramIndex.commit();
		allIndex.commit();
		LOGGER.info("Se han añadido " + i + " noticias al indice.");
	}

	@Override
	public void resetAllAlerts() throws IOException {
		LOGGER.info("Se inicia el proceso de busqueda de alertas en todo el indice.");
		boolean closed = allIndex == null ? false : allIndex.isOpen();
		if (!closed) {
			this.allDirectory = FSDirectory.open(new File(configuracion
					.getConfiguracion().getPathIndexNews().concat("/all"))
					.toPath());
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(allDirectory);
		} catch (IOException e1) {
			LOGGER.error("Error al abrir el indice para realizar la busqueda.");
		}
		LOGGER.info("Se borran todas las alertas detectadas.");
		newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		Set<Alert> alertas = alertService.getAllAlert();
		Set<Risk> riesgos = riskService.getAllAlert();
		LOGGER.info("Hay " + alertas.size() + " alertas para analizar.");
		LOGGER.info("Hay " + riesgos.size() + " riesgos para analizar.");
		for (Alert alert : alertas) {
			Query q = createQuery(alert);
			try {
				List<NewsDetect> listNewsDetect = search(q, searcher, alert);
				newsDetectRepository.save(listNewsDetect);
				if (listNewsDetect.size() > 0)
					LOGGER.info("Se han detectado: " + listNewsDetect.size()
							+ " posibles alertas de: " + alert.getTitle());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Risk alert : riesgos) {
			Query q = createQuery(alert);
			try {
				List<NewsDetect> listNewsDetect = search(q, searcher, alert);
				newsDetectRepository.save(listNewsDetect);
				if (listNewsDetect.size() > 0)
					LOGGER.info("Se han detectado: " + listNewsDetect.size()
							+ " posibles alertas de: " + alert.getTitle());
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
		if (!closed) {
			this.allDirectory.close();
		}
		LOGGER.info("Finaliza el proceso de busqueda de alertas en todo el indice.");

	}

	@Override
	public void resetAlert(AlertAbstract alert) {
		// TODO Auto-generated method stub

	}

}
