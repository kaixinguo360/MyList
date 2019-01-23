package com.my.list.json.spring;

import com.my.list.json.JSON;
import com.my.list.json.JSONS;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;

@Component
public class JsonReturnHandler implements HandlerMethodReturnValueHandler{

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(JSON.class) != null ||
                returnType.getMethodAnnotation(JSONS.class) != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        // Set this handler as the final handler
        mavContainer.setRequestHandled(true);

        // Get annotations and Call jsonSerializer.filter() method
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Annotation[] annotations = returnType.getMethodAnnotations();
        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
        Arrays.asList(annotations).forEach(annotation -> { // Parse Annotations
            if (annotation instanceof JSON) {
                JSON json = (JSON) annotation;
                jsonSerializer.filter(json);
            } else if (annotation instanceof JSONS) {
                JSONS jsons = (JSONS) annotation;
                Arrays.asList(jsons.value()).forEach(jsonSerializer::filter);
            }
        });

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = jsonSerializer.toJson(returnValue);
        response.getWriter().write(json);
    }
}