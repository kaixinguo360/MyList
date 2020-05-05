package com.my.list.module.common.service;

import com.my.list.exception.DataException;
import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;

import java.sql.Timestamp;

public interface SingleCurdService<T extends Resource> extends BaseService<T> {

    /**
     * POST /{resource}
     */
    default void create(User user, T resource) {
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() != null) throw new DataException("Id of input resource has already set.");

        resource.setUser(user.getId());
        resource.setCtime(new Timestamp(System.currentTimeMillis()));
        resource.setMtime(new Timestamp(System.currentTimeMillis()));
        getSingleCrudMapper().insert(user, resource);

        if (this instanceof TagEditService) {
            if (resource.getTags() != null) {
                ((TagEditService<T>) this).setTags(user, resource.getId(), resource.getTags());
            } else {
                resource.setTags(getTagEditMapper().getTags(resource.getId()));
            }
        }
    }

    /**
     * GET /{resource}/{id}
     */
    default T get(User user, Long id) {
        if (id == null) throw new DataException("Input id is null");
        return getSingleCrudMapper().select(user, id);
    }

    /**
     * PUT /{resource}
     */
    default void update(User user, T resource) {
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() == null) throw new DataException("Id of input resource is not set.");
        if (!resource.getUser().equals(user.getId())) throw new DataException("Wrong user.");

        resource.setMtime(new Timestamp(System.currentTimeMillis()));
        getSingleCrudMapper().update(user, resource);

        if (this instanceof TagEditService) {
            if (resource.getTags() != null) {
                ((TagEditService<T>) this).setTags(user, resource.getId(), resource.getTags());
            } else {
                resource.setTags(getTagEditMapper().getTags(resource.getId()));
            }
        }
    }

    /**
     * DELETE /{resource}/{id}
     */
    default void delete(User user, Long id) {
        if (id == null) throw new DataException("Input id is null");
        getSingleCrudMapper().delete(user, id);
    }

}
