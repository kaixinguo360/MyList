package com.my.list.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.ResourceConfigManager;
import com.my.list.domain.Resource;
import com.my.list.domain.User;
import com.my.list.exception.SimpleException;
import com.my.list.service.SingleCrudService;
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

    @Autowired private ResourceConfigManager manager;
    @Autowired private SingleCrudService service;
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
        Resource resource = toEntity(type, body);
        service.create(type, user, resource);
        return resource;
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
        return service.read(type, user, id);
    }

    /**
     * GET /{type}
     */
    @GetMapping("/{type}")
    public List<Resource> getAllResources(
        @PathVariable String type,
        @CurrentUser User user,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        return service.readAll(type, user, limit, offset);
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
        Resource resource = toEntity(type, body);
        service.update(type, user, resource);
        return resource;
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
        service.delete(type, user, id);
    }

    // ----- private ----- //

    private Resource toEntity(String resourceType, Map<String, Object> newResource) {
        try {
            Class<?> clazz = manager.getEntityClass(resourceType);
            return (Resource) objectMapper.convertValue(newResource, clazz);
        } catch (IllegalArgumentException e) {
            throw new SimpleException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
