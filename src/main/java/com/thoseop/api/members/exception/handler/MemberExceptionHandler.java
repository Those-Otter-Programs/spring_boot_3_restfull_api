package com.thoseop.api.members.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thoseop.api.members.exception.MemberNotFoundException;
import com.thoseop.api.members.exception.PasswordNotChangedException;
import com.thoseop.exception.response.OtterAPIErrorResponse;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
//    @ResponseStatus(HttpStatus.NO_CONTENT) // does not work!
    @ResponseStatus(HttpStatus.OK)
    public OtterAPIErrorResponse handleAccessDeniedException(MemberNotFoundException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
//		HttpStatus.NO_CONTENT.value(),
		HttpStatus.OK.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		ex.getMessage());
    }

    @ExceptionHandler(PasswordNotChangedException.class)
//    @ResponseStatus(HttpStatus.NOT_MODIFIED) // does not work!
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public OtterAPIErrorResponse handleAccessDeniedException(PasswordNotChangedException ex) {
	return new OtterAPIErrorResponse(
		new Date().toString(), 
//		HttpStatus.NOT_MODIFIED.value(),
		HttpStatus.NOT_ACCEPTABLE.value(),
		ex.getClass().getName(), 
		ex.getMessage(), 
		ex.getMessage());
    }
}
