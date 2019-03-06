package com.trievosoftware.application.exceptions;

public class NoPremiumFoundException extends Exception {

    public NoPremiumFoundException(String errorMessage) {
        super(errorMessage);
    }
    public NoPremiumFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
