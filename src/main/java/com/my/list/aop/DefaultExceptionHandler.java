package com.my.list.aop;

import com.my.list.controller.ErrorResponse;
import com.my.list.controller.RequestException;
import com.my.list.service.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class DefaultExceptionHandler {

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity requestExceptionHandler(RequestException e) {
        HttpStatus status = e.getStatus();
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), status), status);
    }

    @ExceptionHandler(value = DataException.class)
    public ResponseEntity dataExceptionHandler(DataException e) {
        HttpStatus status = HttpStatus.resolve(e.getErrorType().getCode());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), status), status);
    }
}
