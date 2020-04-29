package com.my.list;

import com.my.list.util.AuthorizationInterceptor;
import com.my.list.util.CurrentUserArgumentResolver;
import com.my.list.util.SimpleResponseReturnHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MyListApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MyListApplication.class, args);
    }

    // Add CORS Policy
    @Override public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedMethods("*")
                .allowedOrigins("*");
    }

     // Add Interceptor - AuthorizationInterceptor
    @Autowired private AuthorizationInterceptor authorizationInterceptor;
    @Override public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**");
    }

    // Add Argument Resolver - CurrentUser
    @Autowired private CurrentUserArgumentResolver currentUserArgumentResolver;
    @Override public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserArgumentResolver);
    }

    // Add Return Handler - SimpleResponseReturnHandler
    @Autowired private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    @Autowired private SimpleResponseReturnHandler simpleResponseReturnHandler;
    @PostConstruct public void init() {
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>();
        newReturnValueHandlers.add(0, simpleResponseReturnHandler);

        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        if (returnValueHandlers != null) newReturnValueHandlers.addAll(returnValueHandlers);

        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }
}
