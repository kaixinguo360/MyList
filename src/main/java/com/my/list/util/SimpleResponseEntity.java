package com.my.list.util;

import com.my.list.exception.SimpleException;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Data
public class SimpleResponseEntity {

    private Object result;
    private boolean success;
    private String error;
    private String message;
    private HttpStatus status;

    public SimpleResponseEntity() {}
    public SimpleResponseEntity(Object result) {
        this.result = result;
        this.success = true;
        this.error = null;
        this.message = "OK";
        this.status = HttpStatus.OK;
    }
    public SimpleResponseEntity(Exception e) {
        this.result = null;
        this.success = false;
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.status = (e instanceof SimpleException) ?
            ((SimpleException) e).getStatus() :
            HttpStatus.INTERNAL_SERVER_ERROR;
    }
    public SimpleResponseEntity(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        status = HttpStatus.resolve(statusCode);
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        this.result = null;
        this.success = !status.isError();
        this.error = status.name();
        this.message = status.getReasonPhrase();
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
    public int getStatus() {
        return status.value();
    }
    public void setStatus(int status) {
        this.status = HttpStatus.valueOf(status);
    }
}
