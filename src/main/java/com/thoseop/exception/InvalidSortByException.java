package com.thoseop.exception;

public class InvalidSortByException extends RuntimeException {

    private static final long serialVersionUID = 4660534266178045364L;

    /**
     * Constructs a <code>MemberNotFoundException</code> with the specified message.
     * @param msg the detail message.
     */
    public InvalidSortByException(String msg) {
	super(msg);
    }

    /**
     * Constructs a {@code MemberNotFoundException} with the specified message and root
     * cause.
     * @param msg the detail message.
     * @param cause root cause
     */
    public InvalidSortByException(String msg, Throwable cause) {
	super(msg, cause);
    }
}
