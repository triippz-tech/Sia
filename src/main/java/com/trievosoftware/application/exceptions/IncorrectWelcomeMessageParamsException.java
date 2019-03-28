package com.trievosoftware.application.exceptions;

public class IncorrectWelcomeMessageParamsException extends Exception {
    public IncorrectWelcomeMessageParamsException(String errorMessage) {
        super(errorMessage);
    }
    public IncorrectWelcomeMessageParamsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
