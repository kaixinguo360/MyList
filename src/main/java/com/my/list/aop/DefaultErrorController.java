package com.my.list.aop;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<SimpleResponseEntity> handleError(HttpServletRequest request) {
        SimpleResponseEntity response = new SimpleResponseEntity(request);
        return new ResponseEntity<>(response, response.status);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
