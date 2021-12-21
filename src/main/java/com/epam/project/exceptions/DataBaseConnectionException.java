package com.epam.project.exceptions;

public class DataBaseConnectionException extends Exception {
    public DataBaseConnectionException() {
        super("Unable to connect to database");
    }
}
