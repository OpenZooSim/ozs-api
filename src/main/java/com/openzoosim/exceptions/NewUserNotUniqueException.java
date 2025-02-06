package com.openzoosim.exceptions;

public class NewUserNotUniqueException extends Exception {
    public NewUserNotUniqueException() {
        super("The new user could not be created because emails must be unique!");
    }
}
