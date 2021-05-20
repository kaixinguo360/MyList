package com.my.list.controller.util;

import com.my.list.Constants;
import com.my.list.exception.UnauthorizedException;
import com.sun.istack.internal.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(String.class) &&
            parameter.hasParameterAnnotation(CurrentToken.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  NativeWebRequest request,
                                  WebDataBinderFactory factory) {
        Object token = request.getAttribute(Constants.CURRENT_TOKEN, RequestAttributes.SCOPE_REQUEST);
        if (token != null) {
            return token;
        } else {
            throw new UnauthorizedException("No valid token found, CURRENT_TOKEN=null");
        }
    }
}
