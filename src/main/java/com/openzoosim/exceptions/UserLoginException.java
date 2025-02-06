package com.openzoosim.exceptions;

public class UserLoginException extends Exception {
    public UserLoginException() {
        super("An error occurred when attempting to login the user!");
    }

    public UserLoginException(String email) {
        super("An error occurred when attempting to login the user! [" + email + "]");
    }
}
