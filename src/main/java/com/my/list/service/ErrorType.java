package com.my.list.service;

public enum ErrorType {

    UNKNOWN_ERROR(500),
    BAD_REQUEST(500),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    ALREADY_EXIST(500);

    int code;

    ErrorType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
