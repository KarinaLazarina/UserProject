package com.test.userproject.exception;

public class EmailDuplicationException extends RuntimeException {
    private final static String EMAIL_DUPLICATION_MESSAGE = "User with provided email already exists. Email: %s";

    public EmailDuplicationException(String email) {
        super(String.format(EMAIL_DUPLICATION_MESSAGE, email));
    }
}
