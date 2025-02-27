package com.thoseop.api.members.entity.enums;

import java.util.stream.Stream;

public enum MemberEnabledStatus {

    ENABLED(true), 
    DISABLED(false);

    private boolean status;

    MemberEnabledStatus(boolean status) {
	this.status = status;
    }

    public boolean getStatus() {
	return this.status;
    }

    // added the MemberStatus.of() method to make it easy to get a MemberStatus instance based on its boolean value.
    public static MemberEnabledStatus of(boolean status) {
	return Stream.of(MemberEnabledStatus.values())
		.filter(p -> p.getStatus() == status).findFirst()
		.orElseThrow(IllegalArgumentException::new);
    }
}
