package com.thoseop.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationEvents {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent authSuccessEvent) {
	log.info("Login successful for the user: {}", authSuccessEvent.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent authFailureEvent) {
	log.error("Login failure for the user: {}", authFailureEvent.getAuthentication().getName(),
		authFailureEvent.getException().getMessage());
    }
}
