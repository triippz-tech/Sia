package com.trievosoftware.application.exceptions;

public class StringNotIntegerException extends Exception {
    public StringNotIntegerException(NullPointerException message) {
        super(message.getMessage());
    }

    public StringNotIntegerException(String message) {
        super(message);
    }
}
