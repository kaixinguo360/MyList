package com.my.list.module.common.controller;

import com.my.list.system.mapper.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
     * PUT /{resource}
     */
    Object put(User user, @RequestBody T resource);

    /**
     * DELETE /{resource}/{id}
     */
    void delete(User user, @PathVariable Long id);
    
}
