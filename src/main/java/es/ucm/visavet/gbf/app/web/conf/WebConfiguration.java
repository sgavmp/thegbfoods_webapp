package es.ucm.visavet.gbf.app.web.conf;

import net.sourceforge.html5val.Html5ValDialect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter  {
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
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
