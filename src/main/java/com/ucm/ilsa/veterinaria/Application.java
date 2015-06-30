package com.ucm.ilsa.veterinaria;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

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

import com.ucm.ilsa.veterinaria.domain.Alert;
import com.ucm.ilsa.veterinaria.domain.AlertDetect;
import com.ucm.ilsa.veterinaria.domain.NewsDetect;
import com.ucm.ilsa.veterinaria.repository.AlertDetectRepository;
import com.ucm.ilsa.veterinaria.repository.NewsDetectRepository;

@SpringBootApplication
@ComponentScan("com.ucm.ilsa.veterinaria")
@EnableAsync
public class Application extends SpringBootServletInitializer implements AsyncConfigurer, CommandLineRunner {
	
	@Autowired
	Environment env;
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
    	System.out.println("Deploy on " + env.getProperty("name"));
    }
	
    @Bean
	@Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setThreadNamePrefix("ILSA-Async-");
        taskExecutor.initialize();
        return taskExecutor;
    }
    
    @Bean
    public TaskScheduler getSchedulerExecutor() {
    	ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    	taskScheduler.setPoolSize(4);
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
