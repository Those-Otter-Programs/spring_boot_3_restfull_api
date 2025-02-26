package com.thoseop.api.members_logs.http;

import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

import com.thoseop.api.members_logs.http.response.AuthenticationFailureLogResponse;

public interface AuthenticationFailureLogController {

    public ResponseEntity<PagedModel<EntityModel<AuthenticationFailureLogResponse>>> getMemberAuthenticationFailures(
	    String username, Integer page, Integer size, String sort); 
	
}
