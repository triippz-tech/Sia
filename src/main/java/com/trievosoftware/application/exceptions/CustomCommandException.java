package com.trievosoftware.application.exceptions;

public class CustomCommandException {

    public static class NoCommandExistsException extends Exception
    {
        public NoCommandExistsException(NullPointerException message) {
            super(message.getMessage());
        }
        public NoCommandExistsException(String message) {
            super(message);
        }
    }

    public static class CommandExistsException extends Exception
    {
        public CommandExistsException(NullPointerException message) {
            super(message.getMessage());
        }
        public CommandExistsException(String message) {
            super(message);
        }
    }

    public static class CommandInvalidParamException extends Exception
    {
        public CommandInvalidParamException(NullPointerException message) {
            super(message.getMessage());
        }
        public CommandInvalidParamException(String message) {
            super(message);
        }
    }
    public static class NoPrefixSetException extends Exception
    {
        public NoPrefixSetException(NullPointerException message) {
            super(message.getMessage());
        }
        public NoPrefixSetException(String message) {
            super(message);
        }
    }
    public static class InvalidRolePermissionException extends Exception
    {
        public InvalidRolePermissionException(NullPointerException message) {
            super(message.getMessage());
        }
        public InvalidRolePermissionException(String message) {
            super(message);
        }
    }
    public static class NoRoleFoundException extends Exception
    {
        public NoRoleFoundException(NullPointerException message) {
            super(message.getMessage());
        }
        public NoRoleFoundException(String message) {
            super(message);
        }
    }
}
