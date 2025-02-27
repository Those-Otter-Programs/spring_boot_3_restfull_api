package com.thoseop.api.members_logs.exception;

public class AuthLogNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 4660534266178045364L;

    /**
     * Constructs a <code>MemberNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public AuthLogNotFoundException(String msg) {
	super(msg);
    }

    /**
     * Constructs a {@code MemberNotFoundException} with the specified message and root
     * cause.
     * @param msg the detail message.
     * @param cause root cause
     */
    public AuthLogNotFoundException(String msg, Throwable cause) {
	super(msg, cause);
    }
}
