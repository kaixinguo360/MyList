package com.my.list.module.common.service;

import com.my.list.module.common.Resource;
import com.my.list.system.mapper.Tag;
import com.my.list.system.mapper.User;
import com.my.list.util.CurrentUser;

import java.sql.Timestamp;
import java.util.List;

public interface TagEditService<T extends Resource> extends BaseService<T> {

    /**
     * POST /{resource}/{id}?tags=...
     */
    default void addTag(@CurrentUser User user, Long id, String tag) {
        Tag tagObject = getTagService().get(user, tag);
        if (tagObject == null) {
            tagObject = Tag.builder()
                .user(user.getId())
                .name(tag)
                .ctime(new Timestamp(System.currentTimeMillis()))
                .mtime(new Timestamp(System.currentTimeMillis()))
                .description("Auto created tag")
                .build();
            getTagService().create(user, tagObject);
        }
        getTagEditMapper().addTag(id, tagObject.getId());
    }

    /**
     * DELETE /{resource}/{id}?tags=...
     */
    default void removeTag(@CurrentUser User user, Long id, String tag) {
        Tag tagObject = getTagService().get(user, tag);
        if (tagObject != null) {
            getTagEditMapper().removeTag(id, tagObject.getId());
        }
    }

    /**
     * PUT /{resource}/{id}?tags=...
     */
    default void setTags(@CurrentUser User user, Long resourceId, List<String> tags) {
        List<String> oldTags = getTagEditMapper().getTags(resourceId);
        for (String tag : oldTags) {
            this.removeTag(user, resourceId, tag);
        }
        for (String tag : tags) {
            this.addTag(user, resourceId, tag);
        }
    }

}
