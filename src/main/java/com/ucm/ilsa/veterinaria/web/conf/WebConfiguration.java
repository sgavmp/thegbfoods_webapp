package com.ucm.ilsa.veterinaria.web.conf;

import net.sourceforge.html5val.Html5ValDialect;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter  {
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

	@Bean
    public SpringSecurityDialect thymeleafSecurityDialect() {
        return new SpringSecurityDialect();
    }
	
	@Bean
	public Html5ValDialect thymeleafValidationHtml5Dialect() {
		return new Html5ValDialect();
	}
}
