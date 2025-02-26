package com.thoseop.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thoseop.exception.response.OtterAPIErrorResponse;

@RestControllerAdvice
public class OtterGlobalExceptionHandler {

    @ExceptionHandler({ UsernameNotFoundException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(UsernameNotFoundException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		"/token");
    }

    @ExceptionHandler({ AccountExpiredException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(AccountExpiredException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		"/token");
    }

    @ExceptionHandler({ LockedException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(LockedException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		"/token");
    }

    @ExceptionHandler({ CredentialsExpiredException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(CredentialsExpiredException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		"/token");
    }

    @ExceptionHandler({ DisabledException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(DisabledException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		"/token");
    }

    @ExceptionHandler({ BadCredentialsException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAuthenticationException(BadCredentialsException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getCause().toString(), 
		"/token");
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public OtterAPIErrorResponse handleAccountStatusException(AccountStatusException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.UNAUTHORIZED.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public OtterAPIErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.FORBIDDEN.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public OtterAPIErrorResponse handleOtherException(Exception ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
		HttpStatus.INTERNAL_SERVER_ERROR.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		ex.getMessage());
    }
}
