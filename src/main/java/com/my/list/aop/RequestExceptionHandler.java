package com.my.list.aop;

import com.my.list.controller.RequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class RequestExceptionHandler {

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity requestExceptionHandler(RequestException e) {
        return new ResponseEntity<>(e.response, e.status);
    }

}
