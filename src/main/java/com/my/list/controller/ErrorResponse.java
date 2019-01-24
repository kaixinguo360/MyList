package com.my.list.controller;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorResponse {

    private final Date timestamp;
    private final int status;
    private final String message;

    public ErrorResponse(String message, HttpStatus status) {
        this.timestamp = new Date();
        this.status = status.value();
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
