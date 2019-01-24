package com.my.list.controller;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class RequestException extends Exception {

    public final HttpStatus status;
    public final ErrorResponse response;

    public RequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.response = new ErrorResponse();
    }

    public class ErrorResponse {

        private final Date timestamp;
        private final int status;
        private final String message;

        private ErrorResponse() {
            this.timestamp = new Date();
            this.status = RequestException.this.status.value();
            this.message = RequestException.this.getMessage();
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
