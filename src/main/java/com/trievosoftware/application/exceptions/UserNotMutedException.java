package com.trievosoftware.application.exceptions;

public class UserNotMutedException extends Exception {
    public UserNotMutedException(String errorMessage) {
        super(errorMessage);
    }
    public UserNotMutedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
