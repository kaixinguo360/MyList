package com.my.list.controller.util;

import com.my.list.service.AuthException;
import com.my.list.service.UserContext;
import com.my.list.service.data.ListService;
import com.my.list.service.data.NodeService;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentContextArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentContext.class)
            && (
            parameter.getParameterType().isAssignableFrom(UserContext.class) ||
                parameter.getParameterType().isAssignableFrom(NodeService.class) ||
                parameter.getParameterType().isAssignableFrom(ListService.class)
            );
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  NativeWebRequest request,
                                  WebDataBinderFactory factory) {
        Object object = request.getAttribute(Constants.CURRENT_CONTEXT, RequestAttributes.SCOPE_REQUEST);
        if (object != null) {
            UserContext userContext = (UserContext) object;
            Class<?> clazz = parameter.getParameterType();
            if (clazz.isAssignableFrom(NodeService.class))
                return userContext.nodeService;
            if (clazz.isAssignableFrom(ListService.class))
                return userContext.listService;
            if (clazz.isAssignableFrom(UserContext.class))
                return userContext;
            else
                throw new RuntimeException("Unknown parameter type, ParameterType=" + clazz);
        } else {
            throw new AuthException("No valid token found, CURRENT_CONTEXT=null");
        }
    }
}
