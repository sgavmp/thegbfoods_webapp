package es.ucm.visavet.gbf.app.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import es.ucm.visavet.gbf.app.service.impl.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
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
        //Configuramos los permisos de cada ruta
        .and()
            .authorizeRequests()
            .antMatchers("/alerts/ajax/stats").permitAll()
            .antMatchers("/risks/ajax/stats").permitAll()
            .antMatchers("/ajax/**").permitAll()
            .antMatchers("/static/**","/webjars/**","/error").permitAll()//Recursos estaticos
            .anyRequest().authenticated()
            //.antMatchers("/admin*").hasRole("ADMIN")//Panel de administracion
            //.antMatchers("/**").permitAll()//El resto esta abierto
            //Configuramos los parametros del login
        .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
        //Configuramos los parametros del logout
        .and()
            .logout()
        	.deleteCookies("JSESSIONID")
        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        	.logoutSuccessUrl("/login")
        //Activamos csrf para controlar la procedencia de los formularios
        .and()
        	.csrf();


    }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userDetailsService())
			.passwordEncoder(passwordEncoder());
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

	@Bean
	protected UserDetailsService userDetailsService() {
		return new UserDetailServiceImpl();
	}

	
	
	
}
