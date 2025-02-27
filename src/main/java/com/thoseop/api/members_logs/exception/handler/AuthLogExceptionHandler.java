package com.thoseop.api.members_logs.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thoseop.api.members.exception.MemberNotFoundException;
import com.thoseop.api.members_logs.exception.AuthLogNotFoundException;
import com.thoseop.exception.response.OtterAPIErrorResponse;

@RestControllerAdvice
public class AuthLogExceptionHandler {

    @ExceptionHandler(AuthLogNotFoundException.class)
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
}
