package com.my.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.ResourceServiceManager;
import com.my.list.domain.User;
import com.my.list.exception.SimpleException;
import com.my.list.service.interfaces.SingleCrudService;
import com.my.list.util.Authorization;
import com.my.list.util.CurrentUser;
import com.my.list.util.SimpleController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.my.list.Constants.API_ROOT;

@Authorization
@SimpleController
@RequestMapping(API_ROOT)
public class SingleCrudController {

    @Autowired
    private ResourceServiceManager manager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ----- Single CRUD ----- //

    /**
     * POST /{type}
     */
    @PostMapping("/{type}")
    public Object postResource(
            @PathVariable String type,
            @CurrentUser User user,
            @RequestBody Map<String, Object> body
    ) {
        SingleCrudService<Object> service = manager.getService(type, SingleCrudService.class);
        Object resource = toEntity(type, body);
        return service.postResource(user, resource);
    }

    /**
     * GET /{type}/{id}
     */
    @GetMapping("/{type}/{id}")
    public Object getResource(
            @PathVariable String type,
            @CurrentUser User user,
            @PathVariable Long id
    ) {
        SingleCrudService<Object> service = manager.getService(type, SingleCrudService.class);
        return service.getResource(user, id);
    }

    /**
     * GET /{type}
     */
    @GetMapping("/{type}")
    public List<Object> getAllResources(
            @PathVariable String type,
            @CurrentUser User user
    ) {
        SingleCrudService<Object> service = manager.getService(type, SingleCrudService.class);
        return service.getAllResources(user);
    }

    /**
     * PUT /{type}
     */
    @PutMapping("/{type}")
    public Object putResource(
            @PathVariable String type,
            @CurrentUser User user,
            @RequestBody Map<String, Object> body
    ) {
        SingleCrudService<Object> service = manager.getService(type, SingleCrudService.class);
        return service.putResource(user, body);
    }

    /**
     * DELETE /{type}/{id}
     */
    @DeleteMapping("/{type}/{id}")
    public void deleteResource(
            @PathVariable String type,
            @CurrentUser User user,
            @PathVariable Long id
    ) {
        SingleCrudService<Object> service = manager.getService(type, SingleCrudService.class);
        service.deleteResource(user, id);
    }

    // ----- private ----- //

    private Object toEntity(String resourceType, Map<String, Object> newResource) {
        try {
            Class<?> clazz = manager.getEntityClass(resourceType);
            return objectMapper.convertValue(newResource, clazz);
        } catch (IllegalArgumentException e) {
            throw new SimpleException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
