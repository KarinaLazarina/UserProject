package com.test.userproject.exception;

public class UserNotFoundException extends RuntimeException {
    private final static String USER_NOT_FOUND_MESSAGE = "User with provided id was not found. Id: %s";

    public UserNotFoundException(String id) {
        super(String.format(USER_NOT_FOUND_MESSAGE, id));
    }
}
