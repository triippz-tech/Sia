package com.trievosoftware.application.exceptions;

public class NoPollsFoundException extends Exception {
    public NoPollsFoundException(NullPointerException message) {
        super(message.getMessage());
    }

    public NoPollsFoundException(String message) {
        super(message);
    }
}
