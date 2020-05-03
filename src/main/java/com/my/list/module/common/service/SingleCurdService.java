package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;

public interface SingleCurdService<T extends Resource> {

    /**
     * POST /{resource}
     */
    void create(User user, T resource);

    /**
     * GET /{resource}/{id}
     */
    T get(User user, Long id);

    /**
     * PUT /{resource}
     */
    void update(User user, T resource);

    /**
     * DELETE /{resource}/{id}
     */
    void delete(User user, Long id);

}
