package com.thoseop.api.members.entity.enums;

import java.util.stream.Stream;

public enum MemberLockedStatus {

    UNLOCKED(true), 
    LOCKED(false);

    private boolean status;

    MemberLockedStatus(boolean status) {
	this.status = status;
    }

    public boolean getStatus() {
	return this.status;
    }

    // added the MemberStatus.of() method to make it easy to get a MemberStatus instance based on its boolean value.
    public static MemberLockedStatus of(boolean status) {
	return Stream.of(MemberLockedStatus.values())
		.filter(p -> p.getStatus() == status).findFirst()
		.orElseThrow(IllegalArgumentException::new);
    }
}
