package com.my.list.module.common;

import com.my.list.module.common.controller.ResourceController;
import com.my.list.module.common.service.ResourceService;
import com.my.list.system.mapper.User;
import com.my.list.util.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
public abstract class BaseResourceController<T extends Resource> implements ResourceController<T> {

    private final ResourceService<T> service;

    public BaseResourceController(ResourceService<T> service) {
        this.service = service;
    }

    // ----- Single CRUD ----- //

    /**
     * POST /{resource}
     */
    @PostMapping("")
    @Transactional
    public Object post(@CurrentUser User user, @RequestBody T resource) {
        log.debug("post");
        service.create(user, resource);
        return resource;
    }

    /**
     * GET /{resource}/{id}
     */
    @GetMapping("/{id}")
    public Object get(@CurrentUser User user, @PathVariable Long id) {
        log.debug("get");
        return service.get(user, id);
    }

    /**
     * PUT /{resource}
     */
    @PutMapping("")
    @Transactional
    public Object put(@CurrentUser User user, @RequestBody T resource) {
        log.debug("put");
        service.update(user, resource);
        return resource;
    }

    /**
     * DELETE /{resource}/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@CurrentUser User user, @PathVariable Long id) {
        log.debug("delete");
        service.delete(user, id);
    }
    
    // ----- Search ----- //

    /**
     * GET /{resource}
     */
    @GetMapping("")
    public List<T> search(
        @CurrentUser User user,
        @RequestParam(required = false) List<String> andTags,
        @RequestParam(required = false) List<String> orTags,
        @RequestParam(required = false) List<String> notTags,
        @RequestParam(required = false) List<String> includeText,
        @RequestParam(required = false) List<String> excludeText,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        return service.search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset);
    }
}
