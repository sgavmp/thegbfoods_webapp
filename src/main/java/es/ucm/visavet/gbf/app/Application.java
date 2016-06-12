package es.ucm.visavet.gbf.app;

import java.util.concurrent.Executor;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@ComponentScan("es.ucm.visavet.gbf.app")
@EnableAsync
public class Application extends SpringBootServletInitializer implements AsyncConfigurer, CommandLineRunner {
	
	private final static Logger LOGGER = Logger
			.getLogger(Application.class);
	
	@Autowired
	Environment env;
	
	@Autowired
	Configuration configuration; 
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        BasicConfigurator.configure();
    }
    
    @Override
    public void run(String... args) throws Exception {
    	LOGGER.info("Deploy on " + env.getProperty("name"));
    }
	
    @Bean
	@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setThreadNamePrefix("ILSA-Async-");
        taskExecutor.initialize();
        return taskExecutor;
    }
    
    @Bean
    public TaskScheduler getSchedulerExecutor() {
    	ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    	taskScheduler.setPoolSize(1);
    	taskScheduler.setThreadNamePrefix("ILSA-Scheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }
	
	@Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }   
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
}
