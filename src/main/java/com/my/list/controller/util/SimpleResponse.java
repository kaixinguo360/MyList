package com.my.list.controller.util;

import com.my.list.service.AuthException;
import com.my.list.service.DataException;
import org.springframework.http.HttpStatus;

public class SimpleResponse {
    
    Object result;
    boolean success;
    String error;
    String message;
    HttpStatus status;

    public SimpleResponse() {
        this(null, null);
    }
    public SimpleResponse(Object result) {
        this(result, null);
    }
    public SimpleResponse(Object result, String message) {
        this.result = result;
        this.success = true;
        this.error = null;
        this.message = message;
        this.status = HttpStatus.OK;
    }
    public SimpleResponse(Exception e) {
        this.result = null;
        this.success = false;
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
        if (e instanceof DataException) this.status = HttpStatus.BAD_REQUEST;
        if (e instanceof AuthException) this.status = HttpStatus.UNAUTHORIZED;
        if (this.status == null) this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public Object getResult() {
        return result;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getError() {
        return error;
    }
    public String getMessage() {
        return message;
    }
    public int getStatus() {
        return status.value();
    }
}
