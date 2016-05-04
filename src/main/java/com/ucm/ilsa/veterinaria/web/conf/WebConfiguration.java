package com.ucm.ilsa.veterinaria.web.conf;

import javax.annotation.PostConstruct;

import net.sourceforge.html5val.Html5ValDialect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter  {
	
	@Value("${gbfood.resources.statics}")
	private String staticsLocation = "file:/tomcatfolder/app/gbfood/resources/static/";
	@Value("${gbfood.resources.templates}")
	private String templatesLocation = "/tomcatfolder/app/gbfood/resources/templates/";
	
	@Autowired
    private SpringTemplateEngine templateEngine;

    @PostConstruct
    public void extension() {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(templatesLocation);
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(true);
        templateEngine.addTemplateResolver(resolver);
    }
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations(staticsLocation);
    }

	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
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
