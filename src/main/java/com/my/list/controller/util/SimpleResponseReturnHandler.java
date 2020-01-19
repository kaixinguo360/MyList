package com.my.list.controller.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleResponseReturnHandler implements HandlerMethodReturnValueHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), SimpleResponse.class) ||
            returnType.hasMethodAnnotation(SimpleResponse.class));
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  @NotNull MethodParameter returnType,
                                  @NotNull ModelAndViewContainer mavContainer,
                                  @NotNull NativeWebRequest webRequest) throws IOException {
        mavContainer.setRequestHandled(true);

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        response.getWriter().write(
            objectMapper.writeValueAsString(
                new SimpleResponseEntity(returnValue)
            )
        );
    }
}
