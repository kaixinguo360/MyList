package com.my.list.util;

import com.my.list.exception.SimpleException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {
        SimpleException.class,
    })
    public ResponseEntity<SimpleResponseEntity> exceptionHandler(Exception e) {
        SimpleResponseEntity response = new SimpleResponseEntity(e);
        return new ResponseEntity<>(response, response.status);
    }
}
