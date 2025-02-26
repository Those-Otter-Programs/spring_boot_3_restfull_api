package com.thoseop.api.members_logs.entity.enums;

import java.util.stream.Stream;

public enum AuthStatus {

    SUCCESS("SUCCESS"), 
    FAILURE("FAILURE");

    private String status;

    AuthStatus(String status) {
	this.status = status;
    }

    public String getStatus() {
	return this.status;
    }

    // added the AuthStatus.of() method to make it easy to get a AuthStatus instance based on its string value.
    public static AuthStatus of(String status) {
	return Stream.of(AuthStatus.values())
		.filter(p -> p.getStatus() == status).findFirst()
		.orElseThrow(IllegalArgumentException::new);
    }
}
