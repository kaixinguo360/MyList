package com.my.list.aop;

import com.my.list.Constants;
import com.my.list.controller.Authorization;
import com.my.list.data.Token;
import com.my.list.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private final TokenService tokenService;

    @Autowired
    public AuthorizationInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class bean = handlerMethod.getBeanType();
        Method method = handlerMethod.getMethod();
        if (bean.getAnnotation(Authorization.class) == null && method.getAnnotation(Authorization.class) == null){
            return true;
        }

        String token = request.getHeader(Constants.AUTHORIZATION);
        if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Error");
            return false;
        }
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);

        if (tokenService.checkToken(tokenEntity)) {
            request.setAttribute(Constants.CURRENT_TOKEN, tokenEntity);
            request.setAttribute(Constants.CURRENT_USER, tokenEntity.getUser());
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Error");
            return false;
        }
    }
}
