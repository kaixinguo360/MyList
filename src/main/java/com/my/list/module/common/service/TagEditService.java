package com.my.list.module.common.service;

import com.my.list.exception.DataException;
import com.my.list.module.common.Resource;
import com.my.list.system.mapper.Tag;
import com.my.list.system.mapper.User;
import com.my.list.util.CurrentUser;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface TagEditService<T extends Resource> extends BaseService<T> {

    /**
     * POST /{resource}/{id}/tag
     */
    default void addTags(User user, Long id, List<String> tags) {
        if (getSingleCrudMapper().select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }
        
        if (tags.size() == 0) {
            return;
        }
        
        List<Long> tagIds = new ArrayList<>();
        for (String tag : tags) {
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
            tagIds.add(tagObject.getId());
        }
        
        getTagEditMapper().addTags(id, tagIds);
    }

    /**
     * GET /{resource}/{id}/tag
     */
    default List<String> getTags(User user, Long id) {
        if (getSingleCrudMapper().select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }
        return getTagEditMapper().getTags(id);
    }

    /**
     * PUT /{resource}/{id}/tag
     */
    default void setTags(User user, Long id, List<String> tags) {
        this.clearTags(user, id);
        this.addTags(user, id, tags);
    }

    /**
     * DELETE /{resource}/{id}/tag
     */
    default void removeTags(@CurrentUser User user, Long id, List<String> tags) {
        if (getSingleCrudMapper().select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }
        
        List<Long> tagIds = new ArrayList<>();
        for (String tag : tags) {
            Tag tagObject = getTagService().get(user, tag);
            if (tagObject != null) {
                tagIds.add(tagObject.getId());
            }
        }
        
        getTagEditMapper().removeTags(id, tagIds);
    }

    /**
     * DELETE /{resource}/{id}/tag/all
     */
    default void clearTags(@CurrentUser User user, Long id) {
        if (getSingleCrudMapper().select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }
        getTagEditMapper().clearTags(id);
    }

}
