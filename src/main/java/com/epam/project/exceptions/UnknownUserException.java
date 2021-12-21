package com.epam.project.exceptions;

public class UnknownUserException extends Exception {
    public UnknownUserException() {
        super("Unknown user");
    }
}
