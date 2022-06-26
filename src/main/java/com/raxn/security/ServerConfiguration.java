package com.raxn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServerConfiguration extends WebSecurityConfigurerAdapter {
	//private static final Logger LOGGER = LoggerFactory.getLogger(ServerConfiguration.class);

	@Autowired
	CustomUserDetailsService userDetailsService;
	
	@Autowired
	private UserAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private SecurityFilter securityFilter;
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encodePassword());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		.authorizeRequests()
			.antMatchers("/login").permitAll()
           .antMatchers("/userinfo/**","/user","/user/**").hasAuthority("USER") // can pass multiple roles
           .antMatchers("/admin","/admin/**").hasAuthority("ADMIN")
           .anyRequest().permitAll()
           .and()
   		.exceptionHandling()
   		.authenticationEntryPoint(authenticationEntryPoint)
   		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
   		.and()
		.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
           
	}
	
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();

	}
}
