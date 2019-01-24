package com.my.list.controller;

import org.springframework.http.HttpStatus;

public class RequestException extends Exception {

    private final HttpStatus status;

    public RequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
