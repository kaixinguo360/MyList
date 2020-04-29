package com.my.list.service.interfaces;

import com.my.list.domain.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SingleCrudService<T> {

    /**
     * POST /{resource}
     */
    T postResource(User user, @RequestBody T newResource);

    /**
     * GET /{resource}/{id}
     */
    T getResource(User user, @PathVariable Long resourceId);

    /**
     * GET /{resource}
     */
    List<T> getAllResources(User user);

    /**
     * PUT /{resource}
     */
    T putResource(User user, @RequestBody T updatedResource);

    /**
     * DELETE /{resource}/{id}
     */
    void deleteResource(User user, @PathVariable Long resourceId);
}
