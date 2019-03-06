package com.trievosoftware.application.exceptions;

public class NoBanFoundExcetion extends Exception {
    public NoBanFoundExcetion(String errorMessage) {
        super(errorMessage);
    }
    public NoBanFoundExcetion(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
