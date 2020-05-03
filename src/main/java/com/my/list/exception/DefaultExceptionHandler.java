package com.my.list.exception;

import com.my.list.util.SimpleResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {
        SimpleException.class,
    })
    public ResponseEntity<SimpleResponseEntity> exceptionHandler(Exception e) {
        e.printStackTrace();
        SimpleResponseEntity response = new SimpleResponseEntity(e);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
