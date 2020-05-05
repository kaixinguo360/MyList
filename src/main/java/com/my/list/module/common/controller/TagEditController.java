package com.my.list.module.common.controller;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface TagEditController<T extends Resource> extends BaseController<T> {

    /**
     * POST /{resource}/{id}/tag
     */
    @PostMapping("/tag")
    @Transactional
    default void addTag(User user, @RequestParam Long id, @RequestBody List<String> tags) {
        for (String tag : tags) {
            getTagEditService().addTag(user, id, tag);
        }
    }

    /**
     * DELETE /{resource}/{id}/tag
     */
    @DeleteMapping("/tag")
    @Transactional
    default void removeTag(User user, @RequestParam Long id, @RequestBody List<String> tags) {
        for (String tag : tags) {
            getTagEditService().removeTag(user, id, tag);
        }
    }

    /**
     * PUT /{resource}/{id}/tag
     */
    @PutMapping("/tag")
    @Transactional
    default void setTags(User user, @RequestParam Long id, @RequestBody List<String> tags) {
        getTagEditService().setTags(user, id, tags);
    }

}
