package com.my.list.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends SimpleException {
    public UnauthorizedException(String msg) {
        super(msg, HttpStatus.UNAUTHORIZED);
    }
}
