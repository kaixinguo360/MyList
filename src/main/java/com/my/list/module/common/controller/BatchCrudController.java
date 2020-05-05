package com.my.list.module.common.controller;

import com.my.list.exception.NotImplementedException;
import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import com.my.list.util.CurrentUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BatchCrudController<T extends Resource> extends SingleCrudController<T> {

    // ----- Batch CRUD ----- //

    /**
     * POST /{resource}/batch
     */
    @PostMapping("/batch")
    @Transactional
    default List<T> batchPost(@CurrentUser User user, @RequestBody List<T> resources) {
        try {
            return getBatchCrudService().batchCreate(user, resources);
        } catch (NotImplementedException e) {
            return resources.stream()
                .map(resource -> post(user, resource))
                .collect(Collectors.toList());
        }
    }

    /**
     * GET /{resource}/batch?ids=...
     */
    @GetMapping("/batch")
    default List<T> batchGet(@CurrentUser User user, @RequestParam List<Long> ids) {
        try {
            return getBatchCrudService().batchRead(user, ids);
        } catch (NotImplementedException e) {
            return ids.stream()
                .map(id -> get(user, id))
                .collect(Collectors.toList());
        }
    }

    /**
     * PUT /{resource}/batch
     */
    @PutMapping("/batch")
    @Transactional
    default List<T> batchPut(@CurrentUser User user, @RequestBody List<T> resources) {
        try {
            return getBatchCrudService().batchUpdate(user, resources);
        } catch (NotImplementedException e) {
            return resources.stream()
                .map(resource -> put(user, resource))
                .collect(Collectors.toList());
        }
    }

    /**
     * DELETE /{resource}/batch
     */
    @DeleteMapping("/batch")
    @Transactional
    default void batchDelete(@CurrentUser User user, @RequestParam List<Long> ids) {
        try {
            getBatchCrudService().batchDelete(user, ids);
        } catch (NotImplementedException e) {
            ids.forEach(id -> delete(user, id));
        }
    }

}
