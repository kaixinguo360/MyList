package com.my.list.exception;

import org.springframework.http.HttpStatus;

public class DataException extends SimpleException {
    public DataException(String msg) {
        super(msg, HttpStatus.BAD_REQUEST);
    }
}
