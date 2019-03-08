package com.trievosoftware.application.exceptions;

public class NoActionsExceptions extends Exception{
    public NoActionsExceptions(String errorMessage) {
        super(errorMessage);
    }
    public NoActionsExceptions(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
