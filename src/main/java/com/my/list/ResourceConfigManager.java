package com.my.list;

import com.my.list.domain.Image;
import com.my.list.domain.ImageMapper;
import com.my.list.exception.SimpleException;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceConfigManager {

    private final Map<String, ResourceConfig> configs = new HashMap<>();

    public ResourceConfigManager(ImageMapper imageMapper) {
        registerMapper("image", Image.class, imageMapper);
    }

    /**
     * register a resource service
     */
    public void registerMapper(String type, Class<?> clazz, Object service) {
        this.configs.put(type, ResourceConfig.builder()
            .entityClass(clazz)
            .resourceMapper(service)
            .build());
    }

    /**
     * get entity class
     */
    public Class<?> getEntityClass(String type) {
        return configs.get(type).entityClass;
    }

    /**
     * get resource mapper
     */
    public <T> T getMapper(String type, Class<T> clazz) {
        if (!configs.containsKey(type) || configs.get(type).resourceMapper == null) {
            throw new SimpleException("Api Not Implemented", HttpStatus.NOT_IMPLEMENTED);
        }
        Object service = configs.get(type).resourceMapper;
        if (!clazz.isInstance(service)) {
            throw new SimpleException("Api Not Implemented", HttpStatus.NOT_IMPLEMENTED);
        }
        return clazz.cast(service);
    }

    @Builder
    public static class ResourceConfig {
        private final Class<?> entityClass;
        private final Object resourceMapper;
    }
}
