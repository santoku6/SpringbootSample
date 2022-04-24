package com.raxn.exception;

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
public class InvalidUserAuthenticationEntryPoint implements AuthenticationEntryPoint {
	Logger LOGGER = LoggerFactory.getLogger(InvalidUserAuthenticationEntryPoint.class);
	
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		LOGGER.error("Login error="+authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
	}

}
