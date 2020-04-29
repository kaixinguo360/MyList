package com.my.list;

import com.my.list.domain.Image;
import com.my.list.exception.SimpleException;
import com.my.list.service.ImageService;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceServiceManager {

    private final Map<String, ServiceConfig> configs = new HashMap<>();

    public ResourceServiceManager(ImageService imageService) {
        registerService("image", Image.class, imageService);
    }

    /**
     * register a resource service
     */
    public void registerService(String type, Class<?> clazz, Object service) {
        this.configs.put(type, ServiceConfig.builder()
                .entityClass(clazz)
                .resourceService(service)
                .build());
    }

    /**
     * get entity class
     */
    public Class<?> getEntityClass(String type) {
        return configs.get(type).entityClass;
    }

    /**
     * get resource service
     */
    public <T> T getService(String type, Class<T> clazz) {
        if (!configs.containsKey(type) || configs.get(type).resourceService == null) {
            throw new SimpleException("Api Not Implemented", HttpStatus.NOT_IMPLEMENTED);
        }
        Object service = configs.get(type).resourceService;
        if (!clazz.isInstance(service)) {
            throw new SimpleException("Api Not Implemented", HttpStatus.NOT_IMPLEMENTED);
        }
        return clazz.cast(service);
    }

    @Builder
    public static class ServiceConfig {
        private final Class<?> entityClass;
        private final Object resourceService;
    }
}
