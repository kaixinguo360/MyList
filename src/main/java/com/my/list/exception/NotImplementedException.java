package com.my.list.exception;

import org.springframework.http.HttpStatus;

public class NotImplementedException extends SimpleException {

    public NotImplementedException(String apiName) {
        super("Api `" + apiName + "` is not implemented.", HttpStatus.NOT_IMPLEMENTED);
    }

}
