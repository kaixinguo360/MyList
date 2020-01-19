package com.my.list.controller.util;

import com.my.list.service.AuthException;
import com.my.list.service.DataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(value = {
        DataException.class,
        AuthException.class,
    })
    public ResponseEntity<SimpleResponse> exceptionHandler(Exception e) {
        SimpleResponse response = new SimpleResponse(e);
        return new ResponseEntity<>(response, response.status);
    }
}
