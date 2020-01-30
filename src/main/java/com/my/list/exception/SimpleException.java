package com.my.list.exception;

import org.springframework.http.HttpStatus;

public class SimpleException extends RuntimeException {
    
    private final HttpStatus status;
    
    public SimpleException(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
