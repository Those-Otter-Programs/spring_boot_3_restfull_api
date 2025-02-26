package com.thoseop.exception.handler;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OtterAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
	    AccessDeniedException accessDeniedException) throws IOException, ServletException {
	response.setHeader("Otter-Spring_Boot_3_RESTfull_API", "Custom Handling HTTP 403");
	response.setStatus(HttpStatus.FORBIDDEN.value());
	response.setContentType("application/json;charset=UTF-8");

	OtterAPIErrorResponse errorResponse = new OtterAPIErrorResponse(new Date().toString(), response.getStatus(),
		"Error: %s".formatted(response.getStatus()), accessDeniedException.getMessage(),
		request.getServletPath());
	ObjectMapper objMapper = new ObjectMapper();
	response.getWriter().write(objMapper.writeValueAsString(errorResponse));
    }
}