package com.trievosoftware.application.exceptions;

public class NoWelcomeMessageFound extends Exception {
    public NoWelcomeMessageFound(String errorMessage) {
        super(errorMessage);
    }
    public NoWelcomeMessageFound(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
