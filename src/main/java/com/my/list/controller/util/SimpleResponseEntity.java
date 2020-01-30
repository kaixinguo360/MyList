package com.my.list.controller.util;

import com.my.list.exception.SimpleException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class SimpleResponseEntity {
    
    Object result;
    boolean success;
    String error;
    String message;
    HttpStatus status;

    SimpleResponseEntity() {}
    SimpleResponseEntity(Object result) {
        this.result = result;
        this.success = true;
        this.error = null;
        this.message = null;
        this.status = HttpStatus.OK;
    }
    SimpleResponseEntity(Exception e) {
        this.result = null;
        this.success = false;
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.status = (e instanceof SimpleException) ?
            ((SimpleException) e).getStatus() :
            HttpStatus.INTERNAL_SERVER_ERROR;
    }
    SimpleResponseEntity(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        status = HttpStatus.resolve(statusCode);
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        this.result = null;
        this.success = !status.isError();
        this.error = status.name();
        this.message = status.getReasonPhrase();
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

    public void setResult(Object result) {
        this.result = result;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public void setError(String error) {
        this.error = error;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setStatus(int status) {
        this.status = HttpStatus.valueOf(status);
    }
}
