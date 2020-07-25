package com.my.list.module.common.controller;

import com.my.list.module.common.Resource;
import com.my.list.modules.user.User;
import com.my.list.util.CurrentUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

public interface TagEditController<T extends Resource> extends BaseController<T> {

    // ----- Single ----- //

    /**
     * POST /{resource}/{id}/tag
     */
    @PostMapping("/{id}/tag")
    @Transactional
    default void addTags(@CurrentUser User user, @PathVariable Long id, @RequestBody List<String> tags) {
        getTagEditService().addTags(user, id, tags);
    }

    /**
     * GET /{resource}/{id}/tag
     */
    @GetMapping("/{id}/tag")
    @Transactional
    default List<String> getTags(@CurrentUser User user, @PathVariable Long id) {
        return getTagEditService().getTags(user, id);
    }

    /**
     * PUT /{resource}/{id}/tag
     */
    @PutMapping("/{id}/tag")
    @Transactional
    default void setTags(@CurrentUser User user, @PathVariable Long id, @RequestBody List<String> tags) {
        getTagEditService().setTags(user, id, tags);
    }

    /**
     * DELETE /{resource}/{id}/tag
     */
    @DeleteMapping("/{id}/tag")
    @Transactional
    default void removeTags(@CurrentUser User user, @PathVariable Long id, @RequestBody List<String> tags) {
        getTagEditService().removeTags(user, id, tags);
    }

    /**
     * DELETE /{resource}/{id}/tag/all
     */
    @DeleteMapping("/{id}/tag/all")
    @Transactional
    default void clearTags(@CurrentUser User user, @PathVariable Long id) {
        getTagEditService().clearTags(user, id);
    }

    // ----- Batch ----- //

    /**
     * POST /{resource}/batch/tag
     */
    @PostMapping("/batch/tag")
    @Transactional
    default void batchAddTags(@CurrentUser User user, @RequestParam List<Long> ids, @RequestBody List<String> tags) {
        ids.forEach(id -> addTags(user, id, tags));
    }

    /**
     * GET /{resource}/batch/tag
     */
    @GetMapping("/batch/tag")
    @Transactional
    default List<List<String>> batchGetTags(@CurrentUser User user, @RequestParam List<Long> ids) {
        return ids.stream()
            .map(id -> getTags(user, id))
            .collect(Collectors.toList());
    }

    /**
     * PUT /{resource}/batch/tag
     */
    @PutMapping("/batch/tag")
    @Transactional
    default void batchSetTags(@CurrentUser User user, @RequestParam List<Long> ids, @RequestBody List<String> tags) {
        ids.forEach(id -> setTags(user, id, tags));
    }

    /**
     * DELETE /{resource}/batch/tag
     */
    @DeleteMapping("/batch/tag")
    @Transactional
    default void batchRemoveTags(@CurrentUser User user, @RequestParam List<Long> ids, @RequestBody List<String> tags) {
        ids.forEach(id -> removeTags(user, id, tags));
    }

    /**
     * DELETE /{resource}/batch/tag/all
     */
    @DeleteMapping("/batch/tag/all")
    @Transactional
    default void batchClearTags(@CurrentUser User user, @RequestParam List<Long> ids) {
        ids.forEach(id -> clearTags(user, id));
    }

}
