package com.trievosoftware.application.exceptions;

public class UserHasNoVoteException extends Exception {
    public UserHasNoVoteException(NullPointerException message) {
        super(message.getMessage());
    }
    public UserHasNoVoteException(String message) {
        super(message);
    }
}
