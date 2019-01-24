package com.my.list.service;

public class DataException extends Exception {

    private final ErrorType errorType;

    public DataException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
