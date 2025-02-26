package com.thoseop.event;

import java.util.Date;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.thoseop.api.members_logs.entity.AuthenticationFailureLogEntity;
import com.thoseop.api.members_logs.entity.enums.AuthStatus;
import com.thoseop.api.members_logs.service.AuthenticationFailureLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthenticationEvent {

    private final AuthenticationFailureLogService authFailLogService;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent authSuccessEvent) {
	log.info("AuthenticationEvents - login successful for the user: {} on {}", 
		authSuccessEvent.getAuthentication().getName(),		
		new Date(authSuccessEvent.getTimestamp()).toString());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent authFailureEvent) {
	log.error("AuthenticationEvents - login failure for the user: {}. Exception: {} on {}", 
		authFailureEvent.getAuthentication().getName(),
		authFailureEvent.getException().getMessage(),
		new Date(authFailureEvent.getTimestamp()).toString());

	WebAuthenticationDetails authDetails = 
		(WebAuthenticationDetails) authFailureEvent.getAuthentication().getDetails();

	authFailLogService.saveAuthLog(new AuthenticationFailureLogEntity( 
		authFailureEvent.getAuthentication().getName(),
		AuthStatus.FAILURE,
		authDetails.getRemoteAddress(),
		authFailureEvent.getException().getMessage(),
		new Date(authFailureEvent.getTimestamp())));
    }
}
