package com.thoseop.api.members.exception;

public class PasswordNotChangedException extends RuntimeException {

    private static final long serialVersionUID = 4660534266178045364L;

    /**
     * Constructs a <code>MemberNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public PasswordNotChangedException(String msg) {
	super(msg);
    }

    /**
     * Constructs a {@code MemberNotFoundException} with the specified message and root
     * cause.
     * @param msg the detail message.
     * @param cause root cause
     */
    public PasswordNotChangedException(String msg, Throwable cause) {
	super(msg, cause);
    }

}
