package com.thoseop.exception;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Component
public class OtterBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    public OtterBasicAuthenticationEntryPoint(
	    @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
	this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {
	response.setHeader("Otter-Spring_Boot_3_RESTfull_API", "Custom Handling HTTP 401");
	response.setStatus(HttpStatus.UNAUTHORIZED.value());
	response.setContentType("application/json;charset=UTF-8");

	OtterAPIErrorResponse errorResponse = new OtterAPIErrorResponse(new Date().toString(), response.getStatus(),
		"Error: %s".formatted(response.getStatus()), authException.getMessage(), request.getServletPath());
	ObjectMapper objMapper = new ObjectMapper();
	response.getWriter().write(objMapper.writeValueAsString(errorResponse));

	this.resolver.resolveException(request, response, null, authException);
    }
}
