package com.thoseop.event;

import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorizationEvents {
    //    @EventListener
    //    public void onSuccess(AuthorizationGrantedEvent<?> successEvent)
    //    {
    //        log.info("Authorization success for the user: {} due to: {}", 
    //                successEvent.getAuthentication().get().getName(),
    //                successEvent.getAuthorizationResult().toString());
    //    }

    @EventListener
    public void onFailure(AuthorizationDeniedEvent<?> deniedEvent) {
	log.error("Authorization failed for the user: {} due to: {}", deniedEvent.getAuthentication().get().getName(),
		deniedEvent.getAuthorizationResult().toString());
    }
}
