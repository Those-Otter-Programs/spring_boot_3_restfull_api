package com.thoseop.api.members.entity.enums;

import java.util.stream.Stream;

public enum MemberExpiredStatus {

    NON_EXPIRED(true), 
    EXPIRED(false);

    private boolean status;

    MemberExpiredStatus(boolean status) {
	this.status = status;
    }

    public boolean getStatus() {
	return this.status;
    }

    // added the MemberStatus.of() method to make it easy to get a MemberStatus instance based on its boolean value.
    public static MemberExpiredStatus of(boolean status) {
	return Stream.of(MemberExpiredStatus.values())
		.filter(p -> p.getStatus() == status).findFirst()
		.orElseThrow(IllegalArgumentException::new);
    }
}
