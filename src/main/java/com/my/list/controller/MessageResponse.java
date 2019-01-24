package com.my.list.controller;

import java.util.Date;

class MessageResponse {
    private Date timestamp;
    private int status;
    private String message;

    MessageResponse(String message) {
        this.timestamp = new Date();
        this.status = 200;
        this.message = message;
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

    @Override
    public String toString() {
        return "MessageResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
