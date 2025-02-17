package com.thoseop.api.members.entity.enums;

import java.util.stream.Stream;

public enum MemberStatus {

    ENABLE(true), 
    DISABLE(false);

    private boolean status;

    MemberStatus(boolean status) {
	this.status = status;
    }

    public boolean getStatus() {
	return this.status;
    }

    // added the MemberStatus.of() method to make it easy to get a MemberStatus instance based on its boolean value.
    public static MemberStatus of(boolean status) {
	return Stream.of(MemberStatus.values())
		.filter(p -> p.getStatus() == status).findFirst()
		.orElseThrow(IllegalArgumentException::new);
    }
}
