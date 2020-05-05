package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BatchCrudService<T extends Resource> {

    /**
     * Batch Create Resources
     * POST /{resource}/batch
     */
    List<T> batchCreate(User user, @RequestBody List<T> resources);

    /**
     * Batch Read Resources
     * GET /{resource}/batch/{ids}
     */
    List<T> batchRead(User user, List<Long> ids);

    /**
     * Batch Update Resources
     * PUT /{resource}/batch
     */
    List<T> batchUpdate(User user, @RequestBody List<T> resources);

    /**
     * Batch Delete Resources
     * DELETE /{resource}/batch
     */
    void batchDelete(User user, @RequestBody List<Long> ids);
}
