package com.trievosoftware.application.exceptions;

public class NoActiveWelcomeMessage extends Exception {
    public NoActiveWelcomeMessage(String errorMessage) {
        super(errorMessage);
    }
    public NoActiveWelcomeMessage(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
