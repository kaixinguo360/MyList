package com.my.list.module.common;

import com.my.list.system.mapper.User;
import com.my.list.util.CurrentUser;
import lombok.extern.slf4j.Slf4j;
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
        return service.read(user, id);
    }

    /**
     * GET /{resource}
     */
    @GetMapping("")
    public List<T> getAll(
        @CurrentUser User user,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset
    ) {
        log.debug("getAll");
        return service.readAll(user, limit, offset);
    }

    /**
     * PUT /{resource}
     */
    @PutMapping("")
    public Object put(@CurrentUser User user, @RequestBody T resource) {
        log.debug("put");
        service.update(user, resource);
        return resource;
    }

    /**
     * DELETE /{resource}/{id}
     */
    @DeleteMapping("/{id}")
    public void delete(@CurrentUser User user, @PathVariable Long id) {
        log.debug("delete");
        service.delete(user, id);
    }

}
