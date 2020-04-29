package com.my.list.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleResponseReturnHandler implements HandlerMethodReturnValueHandler {

    private final ObjectMapper objectMapper;

    public SimpleResponseReturnHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), SimpleResponse.class) ||
            returnType.hasMethodAnnotation(SimpleResponse.class));
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  @NonNull MethodParameter returnType,
                                  @NonNull ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest) throws IOException {
        mavContainer.setRequestHandled(true);

        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        
        response.getWriter().write(
            objectMapper.writeValueAsString(
                new SimpleResponseEntity(returnValue)
            )
        );
    }
}
