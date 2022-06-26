package com.raxn.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationEntryPoint.class);
	
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		//LOGGER.info("request="+request);
		LOGGER.error("Login error="+authException.getMessage());
		//LOGGER.error("Login error="+authException.getLocalizedMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
	}

}
