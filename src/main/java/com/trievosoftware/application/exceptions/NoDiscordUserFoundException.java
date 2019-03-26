package com.trievosoftware.application.exceptions;

public class NoDiscordUserFoundException extends Exception {
    public NoDiscordUserFoundException(NullPointerException message) {
        super(message.getMessage());
    }

    public NoDiscordUserFoundException(String message) {
        super(message);
    }
}
