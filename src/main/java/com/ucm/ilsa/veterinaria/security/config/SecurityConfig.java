package com.ucm.ilsa.veterinaria.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        //Activamos la funcion de recordar al usuario almacenando la sesion en la DB
        .rememberMe()
        .tokenValiditySeconds(1209600)
        	.key("6772b7939386362af7ed96915")
        	.rememberMeServices(persistentTokenBasedRememberMeServices())
    	//Configuramos los parametros del login
        .and()
            .formLogin()
            .permitAll()
        //Configuramos los parametros del logout
        .and()
            .logout()
        	.deleteCookies("JSESSIONID")
        	.logoutSuccessUrl("/")
            .permitAll()
        //Configuramos los permisos de cada ruta
        .and()
            .authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .anyRequest().authenticated()
        //Activamos csrf para controlar la procedencia de los formularios
        .and()
        	.csrf();


    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.jdbcAuthentication()
			.passwordEncoder(passwordEncoder())
			.dataSource(dataSource);
			//.withUser("user").password(passwordEncoder().encode("password")).roles("USER");
	}
	
	@Bean
	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices(){
		PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices = 
				new PersistentTokenBasedRememberMeServices("6772b7939386362af7ed96915", 
						userDetailsService(), jdbcTokenRepositoryImpl());
		persistentTokenBasedRememberMeServices.setAlwaysRemember(false);
		return persistentTokenBasedRememberMeServices;
	}
	
	@Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl() {
    	JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
    	jdbcTokenRepositoryImpl.setDataSource(dataSource);
    	return jdbcTokenRepositoryImpl;
    }

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
