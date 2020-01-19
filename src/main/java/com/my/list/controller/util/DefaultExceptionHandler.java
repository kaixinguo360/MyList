package com.my.list.controller.util;

import com.my.list.service.AuthException;
import com.my.list.service.DataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {
        DataException.class,
        AuthException.class,
    })
    public ResponseEntity<SimpleResponseEntity> exceptionHandler(Exception e) {
        SimpleResponseEntity response = new SimpleResponseEntity(e);
        return new ResponseEntity<>(response, response.status);
    }
}
