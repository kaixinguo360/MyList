package com.my.list.modules.resource;

import com.my.list.exception.DataException;
import com.my.list.exception.NotImplementedException;
import com.my.list.modules.tag.Tag;
import com.my.list.modules.user.User;
import com.my.list.modules.tag.TagService;
import com.my.list.util.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceService {

    @Autowired private TagService tagService;
    @Autowired private ResourceMapper mapper;

    // ----- Single Crud ----- //
    
    /**
     * POST /{resource}
     */
    public void create(User user, Resource resource) {
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() != null) throw new DataException("Id of input resource has already set.");

        resource.setUser(user.getId());
        resource.setCtime(new Timestamp(System.currentTimeMillis()));
        resource.setMtime(new Timestamp(System.currentTimeMillis()));
        mapper.insert(user, resource);

        if (resource.getTags() != null) {
            this.setTags(user, resource.getId(), resource.getTags());
        } else {
            resource.setTags(mapper.getTags(resource.getId()));
        }
    }

    /**
     * GET /{resource}/{id}
     */
    public Resource get(User user, Long id) {
        if (id == null) throw new DataException("Input id is null");
        return mapper.select(user, id);
    }

    /**
     * PUT /{resource}
     */
    public void update(User user, Resource resource) {
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() == null) throw new DataException("Id of input resource is not set.");
        if (!resource.getUser().equals(user.getId())) throw new DataException("Wrong user.");

        resource.setMtime(new Timestamp(System.currentTimeMillis()));
        mapper.update(user, resource);

        if (resource.getTags() != null) {
            this.setTags(user, resource.getId(), resource.getTags());
        } else {
            resource.setTags(mapper.getTags(resource.getId()));
        }
    }

    /**
     * DELETE /{resource}/{id}
     */
    public void delete(User user, Long id) {
        if (id == null) throw new DataException("Input id is null");
        mapper.delete(user, id);
    }
    
    // ----- Tag ----- //

    /**
     * POST /{resource}/{id}/tag
     */
    public void addTags(User user, Long id, List<String> tags) {
        if (mapper.select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }

        if (tags.size() == 0) {
            return;
        }

        List<Long> tagIds = new ArrayList<>();
        for (String tag : tags) {
            Tag tagObject = tagService.get(user, tag);
            if (tagObject == null) {
                tagObject = Tag.builder()
                    .user(user.getId())
                    .name(tag)
                    .ctime(new Timestamp(System.currentTimeMillis()))
                    .mtime(new Timestamp(System.currentTimeMillis()))
                    .description("Auto created tag")
                    .build();
                tagService.create(user, tagObject);
            }
            tagIds.add(tagObject.getId());
        }

        mapper.addTags(id, tagIds);
    }

    /**
     * GET /{resource}/{id}/tag
     */
    public List<String> getTags(User user, Long id) {
        if (mapper.select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }
        return mapper.getTags(id);
    }

    /**
     * PUT /{resource}/{id}/tag
     */
    public void setTags(User user, Long id, List<String> tags) {
        this.clearTags(user, id);
        this.addTags(user, id, tags);
    }

    /**
     * DELETE /{resource}/{id}/tag
     */
    public void removeTags(@CurrentUser User user, Long id, List<String> tags) {
        if (mapper.select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }

        List<Long> tagIds = new ArrayList<>();
        for (String tag : tags) {
            Tag tagObject = tagService.get(user, tag);
            if (tagObject != null) {
                tagIds.add(tagObject.getId());
            }
        }

        mapper.removeTags(id, tagIds);
    }

    /**
     * DELETE /{resource}/{id}/tag/all
     */
    public void clearTags(@CurrentUser User user, Long id) {
        if (mapper.select(user, id) == null) {
            throw new DataException("No such resource, id=" + id);
        }
        mapper.clearTags(id);
    }
    
    // ----- Search ----- //
    
    public List<Resource> search(
        User user,
        List<String> andTags,
        List<String> orTags,
        List<String> notTags,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset,
        String orderBy,
        String orderDirection
    ) {
        switch (orderBy) {
            case "ctime":
            case "mtime":
                break;
            case "pageTitle":
                orderBy = "page_title";
                break;
            case "imageTitle":
                orderBy = "image_title";
                break;
            default:
                throw new NotImplementedException("Image_OrderBy[" + orderBy + "]");
        }
        return mapper.search(user, andTags, orTags, notTags, includeText, excludeText, limit, offset, orderBy, orderDirection);
    }
}
