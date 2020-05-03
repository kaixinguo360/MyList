package com.my.list.module.common.controller;

import com.my.list.system.mapper.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SingleCrudController<T> {

    /**
     * POST /{resource}
     */
    Object post(User user, @RequestBody T resource);

    /**
     * GET /{resource}/{id}
     */
    Object get(User user, @PathVariable Long id);

    /**
     * GET /{resource}
     */
    List<T> getAll(
        User user,
        @RequestParam(required = false, defaultValue = "100") Integer limit,
        @RequestParam(required = false, defaultValue = "0") Integer offset
    );

    /**
     * PUT /{resource}
     */
    Object put(User user, @RequestBody T resource);

    /**
     * DELETE /{resource}/{id}
     */
    void delete(User user, @PathVariable Long id);
    
}
