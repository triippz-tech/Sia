package com.trievosoftware.application.exceptions;

public class InvalidTimeUnitException extends Exception {
    public InvalidTimeUnitException(String errorMessage) {
        super(errorMessage);
    }
    public InvalidTimeUnitException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
