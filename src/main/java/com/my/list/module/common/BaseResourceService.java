package com.my.list.module.common;

import com.my.list.exception.DataException;
import com.my.list.module.common.mapper.ResourceMapper;
import com.my.list.module.common.service.ResourceService;
import com.my.list.system.mapper.User;

import java.sql.Timestamp;
import java.util.List;

public abstract class BaseResourceService<T extends Resource> implements ResourceService<T> {

    private final ResourceMapper<T> mapper;

    public BaseResourceService(ResourceMapper<T> mapper) {
        this.mapper = mapper;
    }

    // ----- Single Crud ----- //

    /**
     * POST /{resource}
     */
    public void create(User user, T resource) {
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() != null) throw new DataException("Id of input resource has already set.");
        resource.setUser(user.getId());
        mapper.insert(user, resource);
    }

    /**
     * GET /{resource}/{id}
     */
    public T get(User user, Long id) {
        if (id == null) throw new DataException("Input id is null");
        return mapper.select(user, id);
    }

    /**
     * PUT /{resource}
     */
    public void update(User user, T resource) {
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() == null) throw new DataException("Id of input resource is not set.");
        if (!resource.getUser().equals(user.getId())) throw new DataException("Wrong user.");
        resource.setMtime(new Timestamp(System.currentTimeMillis()));
        mapper.update(user, resource);
    }

    /**
     * DELETE /{resource}/{id}
     */
    public void delete(User user, Long id) {
        if (id == null) throw new DataException("Input id is null");
        mapper.delete(user, id);
    }

    // ----- Search ----- //

    /**
     * GET /{resource}
     */
    public List<T> search(
        User user,
        List<Long> andTags,
        List<Long> orTags,
        List<Long> notTags,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset
    ) {
        return mapper.search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset);
    }
}
