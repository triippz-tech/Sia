package com.trievosoftware.application.exceptions;

public class PollExpiredException extends Exception {
    public PollExpiredException(String errorMessage) {
        super(errorMessage);
    }
    public PollExpiredException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
