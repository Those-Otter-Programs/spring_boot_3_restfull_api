package com.thoseop.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thoseop.exception.response.OtterAPIErrorResponse;

@RestControllerAdvice
public class OtterGlobalExceptionHandler {

    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(Exception ex) {
	return new OtterAPIErrorResponse(new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		"Error: %s".formatted(HttpStatus.UNAUTHORIZED.value()), 
		ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
	return new OtterAPIErrorResponse(new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		"Error: %s".formatted(HttpStatus.UNAUTHORIZED.value()), 
		ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAccountStatusException(AccountStatusException ex) {
	return new OtterAPIErrorResponse(new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		"Error: %s".formatted(HttpStatus.UNAUTHORIZED.value()), 
		ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public OtterAPIErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
	return new OtterAPIErrorResponse(new Date().toString(), 
		HttpStatus.FORBIDDEN.value(),
		"Error: %s".formatted(HttpStatus.FORBIDDEN.value()), 
		ex.getMessage(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OtterAPIErrorResponse handleOtherException(Exception ex) {
	return new OtterAPIErrorResponse(new Date().toString(), 
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		"Error: %s".formatted(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
		ex.getMessage(), ex.getMessage());
    }
}
