package com.my.list.module.common.controller;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import com.my.list.util.CurrentUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

public interface SingleCrudController<T extends Resource> extends BaseController<T> {

    /**
     * POST /{resource}
     */
    @PostMapping("")
    @Transactional
    default T post(@CurrentUser User user, @RequestBody T resource) {
        getSingleCurdService().create(user, resource);
        return resource;
    }

    /**
     * GET /{resource}/{id}
     */
    @GetMapping("/{id}")
    default T get(@CurrentUser User user, @PathVariable Long id) {
        return getSingleCurdService().get(user, id);
    }

    /**
     * PUT /{resource}
     */
    @PutMapping("")
    @Transactional
    default T put(@CurrentUser User user, @RequestBody T resource) {
        getSingleCurdService().update(user, resource);
        return resource;
    }

    /**
     * DELETE /{resource}/{id}
     */
    @DeleteMapping("/{id}")
    @Transactional
    default void delete(@CurrentUser User user, @PathVariable Long id) {
        getSingleCurdService().delete(user, id);
    }

}
