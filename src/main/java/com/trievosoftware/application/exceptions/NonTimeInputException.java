package com.trievosoftware.application.exceptions;

public class NonTimeInputException extends Exception {
    public NonTimeInputException(String errorMessage) {
        super(errorMessage);
    }
    public NonTimeInputException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
