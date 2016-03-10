package com.ucm.ilsa.veterinaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
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

import com.ucm.ilsa.veterinaria.domain.Configuracion;
import com.ucm.ilsa.veterinaria.domain.Feed;
import com.ucm.ilsa.veterinaria.domain.News;
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


    @Before
    public void setUp() {
    	
    }

    @Test
    public void canFetchMickey() {
    	String topic = "\"lengua azul\"  | triquinosis - ('fiebre porciona' & gripe  | frío - calor - ÑoñeríA & ambigüeDa  AMbiGÜEDÁD676 )      | Enfermedad | @Tperiodico | @Lespaña";
  	  	InputStream stream = new ByteArrayInputStream(topic.getBytes());
//    	TopicValidator validator = new TopicValidator(new TopicValidatorSemantics("Desesperación",topicManager), stream);
//    	try {
//			validator.topic();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	
    	QueryConstructor queryConstructor = new QueryConstructor(new QueryConstructorSemantics(topicManager,News.fieldBody,
                News.fieldBodyNoCase,News.fieldSiteLoc, News.fieldSiteType),stream);
    	Query q = null;
		try {
			q = queryConstructor.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(q);
    	
    }
    
}
