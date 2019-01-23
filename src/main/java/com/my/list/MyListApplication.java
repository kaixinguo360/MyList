package com.my.list;

import com.my.list.aop.AuthorizationInterceptor;
import com.my.list.aop.CurrentUserArgumentResolver;
import com.my.list.json.spring.JsonReturnHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class MyListApplication implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    private final JsonReturnHandler jsonReturnHandler;

    @Autowired
    public MyListApplication(AuthorizationInterceptor authorizationInterceptor,
                             CurrentUserArgumentResolver currentUserArgumentResolver,
                             JsonReturnHandler jsonReturnHandler) {
        this.authorizationInterceptor = authorizationInterceptor;
        this.currentUserArgumentResolver = currentUserArgumentResolver;
        this.jsonReturnHandler = jsonReturnHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyListApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(jsonReturnHandler);
    }
}

