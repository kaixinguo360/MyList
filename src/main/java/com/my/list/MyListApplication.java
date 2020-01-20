package com.my.list;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.controller.util.AuthorizationInterceptor;
import com.my.list.controller.util.CurrentContextArgumentResolver;
import com.my.list.controller.util.CurrentTokenArgumentResolver;
import com.my.list.controller.util.SimpleResponseReturnHandler;
import com.my.list.dto.Type;
import com.my.list.dto.TypeConfig;
import com.my.list.type.image.Image;
import com.my.list.type.image.ImageMapper;
import com.my.list.type.music.Music;
import com.my.list.type.music.MusicMapper;
import com.my.list.type.text.Text;
import com.my.list.type.text.TextMapper;
import com.my.list.type.video.Video;
import com.my.list.type.video.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
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
        TextMapper textMapper,
        ImageMapper imageMapper,
        MusicMapper musicMapper,
        VideoMapper videoMapper
    ) {
        TypeConfig typeConfig = new TypeConfig();
        typeConfig.addType(new Type("node"));
        typeConfig.addType(new Type("list", true));
        typeConfig.addType(new Type("tag", true));
        typeConfig.addType(new Type(Text.TYPE_NAME, Text.class, textMapper));
        typeConfig.addType(new Type(Image.TYPE_NAME, Image.class, imageMapper));
        typeConfig.addType(new Type(Music.TYPE_NAME, Music.class, musicMapper));
        typeConfig.addType(new Type(Video.TYPE_NAME, Video.class, videoMapper));
        return typeConfig;
    }
    
    @Bean
    public ObjectMapper objectMapper(TypeConfig typeConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(typeConfig.getTypes().stream()
            .filter(type ->type.hasExtraData)
            .map(type -> type.extraDataClass)
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
