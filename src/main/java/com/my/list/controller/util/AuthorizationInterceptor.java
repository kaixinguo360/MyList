package com.my.list.controller.util;

import com.my.list.Constants;
import com.my.list.exception.UnauthorizedException;
import com.my.list.service.UserContext;
import com.my.list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> bean = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();

        // Get annotation
        Authorization annotation = method.getAnnotation(Authorization.class);
        if (annotation == null) {
            annotation = bean.getAnnotation(Authorization.class);
            if (annotation == null) {
                return true;
            }
        }
        
        // Get token & userContext
        String token = request.getHeader(Constants.AUTHORIZATION);
        if (token == null) {
            throw new UnauthorizedException("No valid token found.");
        }

        // Check permission
        if (annotation.value()) {
            userService.checkAdminToken(token);
        } else {
            UserContext userContext = userService.getUserContext(token);
            if (userContext != null) {
                request.setAttribute(Constants.CURRENT_TOKEN, token);
                request.setAttribute(Constants.CURRENT_CONTEXT, userContext);
            } else {
                throw new UnauthorizedException("Unauthorized token, token=" + token);
            }
        }
        
        return true;
    }
}
