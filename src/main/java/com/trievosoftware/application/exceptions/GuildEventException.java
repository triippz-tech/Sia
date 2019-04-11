package com.trievosoftware.application.exceptions;

public class GuildEventException extends Exception
{
    public static class NoGuildEventFound extends Throwable
    {
        public NoGuildEventFound(String errorMessage) {
            super(errorMessage);
        }
        public NoGuildEventFound(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
    }

    public static class IncorrectGuildEventParamsException extends Throwable
    {
        public IncorrectGuildEventParamsException(String errorMessage) {
            super(errorMessage);
        }
        public IncorrectGuildEventParamsException(String errorMessage, Throwable err) {
            super(errorMessage, err);
        }
    }
}
