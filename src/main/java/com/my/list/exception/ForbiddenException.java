package com.my.list.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends SimpleException {
    public ForbiddenException(String msg) {
        super(msg, HttpStatus.FORBIDDEN);
    }
}
