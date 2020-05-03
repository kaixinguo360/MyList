package com.my.list.module.common;

import com.my.list.exception.DataException;
import com.my.list.module.common.mapper.ResourceMapper;
import com.my.list.module.common.mapper.ResourceTagMapper;
import com.my.list.module.common.service.ResourceService;
import com.my.list.system.mapper.Tag;
import com.my.list.system.mapper.User;
import com.my.list.system.service.TagService;
import com.my.list.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

public abstract class BaseResourceService<T extends Resource> implements ResourceService<T> {
    
    @Autowired
    private TagService tagService;
    private final ResourceMapper<T> mapper;
    private final ResourceTagMapper tagMapper;

    public BaseResourceService(ResourceMapper<T> mapper, ResourceTagMapper tagMapper) {
        this.mapper = mapper;
        this.tagMapper = tagMapper;
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

        if (resource.getTags() != null) {
            this.setTags(user, resource.getId(), resource.getTags());
        } else {
            resource.setTags(tagMapper.getTags(resource.getId()));
        }
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

        if (resource.getTags() != null) {
            this.setTags(user, resource.getId(), resource.getTags());
        } else {
            resource.setTags(tagMapper.getTags(resource.getId()));
        }
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
        List<String> andTags,
        List<String> orTags,
        List<String> notTags,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset
    ) {
        return mapper.search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset);
    }

    // ----- Tag ----- //

    public void addTag(@CurrentUser User user, Long resourceId, String tagName) {
        Tag tag = tagService.get(user, tagName);
        if (tag == null) {
            tag = Tag.builder()
                .user(user.getId())
                .name(tagName)
                .ctime(new Timestamp(System.currentTimeMillis()))
                .mtime(new Timestamp(System.currentTimeMillis()))
                .description("Auto created tag")
                .build();
            tagService.create(user, tag);
        }
        tagMapper.addTag(resourceId, tag.getId());
    }
    
    public void removeTag(@CurrentUser User user, Long resourceId, String tagName) {
        Tag tag = tagService.get(user, tagName);
        if (tag != null) {
            tagMapper.removeTag(resourceId, tag.getId());
        }
    }

    public void setTags(@CurrentUser User user, Long resourceId, List<String> tagNames) {
        List<String> oldTags = tagMapper.getTags(resourceId);
        for (String tag : oldTags) {
            this.removeTag(user, resourceId, tag);
        }
        for (String tag : tagNames) {
            this.addTag(user, resourceId, tag);
        }
    }
}
