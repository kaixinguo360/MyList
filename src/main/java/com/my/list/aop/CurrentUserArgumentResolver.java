package com.my.list.aop;

import com.my.list.Constants;
import com.my.list.controller.CurrentUser;
import com.my.list.data.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(User.class) &&
                methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer container,
                                  NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {
        Object currentUser = request.getAttribute(Constants.CURRENT_USER, RequestAttributes.SCOPE_REQUEST);
        if (currentUser != null) {
            return currentUser;
        } else {
            throw new Exception("CURRENT_USER is null");
        }
    }
}
