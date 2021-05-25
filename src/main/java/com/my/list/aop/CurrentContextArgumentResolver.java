package com.my.list.aop;

import com.my.list.Constants;
import com.my.list.exception.UnauthorizedException;
import com.my.list.service.LinkService;
import com.my.list.service.NodeService;
import com.my.list.service.SearchService;
import com.my.list.service.UserContext;
import com.sun.istack.internal.NotNull;
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
                parameter.getParameterType().isAssignableFrom(SearchService.class) ||
                parameter.getParameterType().isAssignableFrom(LinkService.class)
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
            if (clazz.isAssignableFrom(UserContext.class))
                return userContext;
            if (clazz.isAssignableFrom(NodeService.class))
                return userContext.nodeService;
            if (clazz.isAssignableFrom(SearchService.class))
                return userContext.searchService;
            if (clazz.isAssignableFrom(LinkService.class))
                return userContext.linkService;
            else
                throw new RuntimeException("Unknown parameter type, ParameterType=" + clazz);
        } else {
            throw new UnauthorizedException("No valid token found, CURRENT_CONTEXT=null");
        }
    }
}
