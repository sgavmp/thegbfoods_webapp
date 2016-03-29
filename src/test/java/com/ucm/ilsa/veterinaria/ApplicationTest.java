package com.ucm.ilsa.veterinaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Lists;
import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.domain.Topic;
import com.ucm.ilsa.veterinaria.domain.topic.TopicManager;
import com.ucm.ilsa.veterinaria.repository.ConfiguracionRepository;
import com.ucm.ilsa.veterinaria.repository.NewsRepository;
import com.ucm.ilsa.veterinaria.repository.TopicRepository;
import com.ucm.ilsa.veterinaria.service.ConfiguracionService;
import com.ucm.ilsa.veterinaria.service.FeedService;
import com.ucm.ilsa.veterinaria.service.NewsIndexService;

import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructor;
import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructorSemantics;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TopicValidator;
import es.ucm.visavet.gbf.topics.validator.TopicValidatorSemantics;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration 
//@ActiveProfiles("test-db")
@ActiveProfiles("test")
public class ApplicationTest  {
	
	@Autowired
	TopicManager topicManager;
	
	@Autowired
	TopicRepository topicRepository;

	private Directory allDirectory;
	private IndexWriter allIndex;
	private IndexReader readAllIndex;

    @Before
    public void setUp() {
    	try {
			this.allDirectory = FSDirectory.open(new File("/gb/news/all")
					.toPath());

			this.allIndex = new IndexWriter(allDirectory,
					new IndexWriterConfig(getAnalyzer()));
			this.readAllIndex = DirectoryReader.open(allDirectory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

    @Test
    public void canFetchMickey() {
    	Query q = NumericRangeQuery.newLongRange(News.fieldSiteType, 0L, 100L, true, true);
    	IndexReader reader = null;
		try {
			reader = DirectoryReader.open(allDirectory);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	IndexSearcher searcher = new IndexSearcher(reader);
    	TopDocs docs = null;
		try {
			docs = searcher.search(q, Integer.MAX_VALUE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ScoreDoc[] docsA = docs.scoreDocs;
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			try {
				Document d = searcher.doc(docId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(i);
		}
//    	Topic topic1 = new Topic();
//    	topic1.setTitle("lenguaAzul");
//    	topic1.setWords("'lengua azul'");
//  	  	InputStream stream1 = new ByteArrayInputStream(topic1.getWords().getBytes());
//    	TopicValidator validator = new TopicValidator(new TopicValidatorSemantics("lenguaAzul",topicManager), stream1);
//    	try {
//			validator.topic();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	topic1.setReferences(validator.getReferences());
//    	topic1 = topicRepository.save(topic1);
//    	Topic topic2 = new Topic();
//    	topic2.setTitle("lenguaAzulAlerta");
//    	topic2.setWords("#lenguaAzul");
//    	InputStream stream2 = new ByteArrayInputStream(topic2.getWords().getBytes());
//    	validator = new TopicValidator(new TopicValidatorSemantics("lenguaAzulAlerta",topicManager), stream2);
//    	try {
//			validator.topic();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	topic2.setReferences(validator.getReferences());
//    	topic2 = topicRepository.save(topic2);
//    	topic2.getReferences().contains(topic1);
//    	topic1.setWords("'lengua azul' #lenguaAzulAlerta");
//  	  	stream1 = new ByteArrayInputStream(topic1.getWords().getBytes());
//    	validator = new TopicValidator(new TopicValidatorSemantics("lenguaAzul",topicManager), stream1);
//    	try {
//			validator.topic();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
    
}
