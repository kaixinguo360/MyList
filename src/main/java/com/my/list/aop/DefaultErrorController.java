package com.my.list.aop;

import com.my.list.controller.RequestException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class DefaultErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public Object handleError(HttpServletRequest request) throws RequestException {

        Object statusCode = request.getAttribute("javax.servlet.error.status_code");
        Object url = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

        if (statusCode instanceof Integer) {
            if (url instanceof String && ((String) url).startsWith("/api/")) {
                HttpStatus status = HttpStatus.resolve((int) statusCode);
                if (status == null) {
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
                throw new RequestException(status.getReasonPhrase(), status);
            } else {
                return "/";
            }
        } else {
            return "/";
        }
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
