package com.my.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.controller.util.AuthorizationInterceptor;
import com.my.list.controller.util.CurrentContextArgumentResolver;
import com.my.list.controller.util.CurrentTokenArgumentResolver;
import com.my.list.controller.util.SimpleResponseReturnHandler;
import com.my.list.dto.SimpleType;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.ListType;
import com.my.list.type.TagType;
import com.my.list.type.image.ImageType;
import com.my.list.type.music.MusicType;
import com.my.list.type.text.TextType;
import com.my.list.type.video.VideoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@SpringBootApplication
public class MyListApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MyListApplication.class, args);
    }
    
    @Bean
    public TypeConfig typeConfig(
        ListType listType,
        TagType tagType,
        TextType textType,
        ImageType imageType,
        MusicType musicType,
        VideoType videoType
    ) {
        TypeConfig typeConfig = new TypeConfig();
        typeConfig.addType(SimpleType.nodeType("node"));
        typeConfig.addType(tagType);
        typeConfig.addType(listType);
        typeConfig.addType(textType);
        typeConfig.addType(imageType);
        typeConfig.addType(musicType);
        typeConfig.addType(videoType);
        return typeConfig;
    }
    
    @Bean
    public ObjectMapper objectMapper(TypeConfig typeConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(typeConfig.getTypes().stream()
            .filter(Type::isHasExtraData)
            .map(Type::getExtraDataClass)
            .collect(Collectors.toList())
        );
        return objectMapper;
    }
    
    @Autowired private AuthorizationInterceptor authorizationInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
            .addPathPatterns("/**");
    }

    @Autowired private CurrentContextArgumentResolver currentContextArgumentResolver;
    @Autowired private CurrentTokenArgumentResolver currentTokenArgumentResolver;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentContextArgumentResolver);
        argumentResolvers.add(currentTokenArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedMethods("*")
            .allowedOrigins("*");
    }
    
    @Autowired private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    @Autowired private SimpleResponseReturnHandler simpleResponseReturnHandler;
    @PostConstruct
    public void init() {
        List<HandlerMethodReturnValueHandler> newReturnValueHandlers = new ArrayList<>();
        newReturnValueHandlers.add(0, simpleResponseReturnHandler);

        List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        if (returnValueHandlers != null) newReturnValueHandlers.addAll(returnValueHandlers);

        requestMappingHandlerAdapter.setReturnValueHandlers(newReturnValueHandlers);
    }
}
