package com.my.list.system.service;

import com.my.list.exception.DataException;
import com.my.list.system.mapper.Tag;
import com.my.list.system.mapper.TagMapper;
import com.my.list.system.mapper.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TagService {
    
    @Autowired private TagMapper mapper;

    // ----- Single Crud ----- //

    /**
     * POST /tag
     */
    public void create(User user, Tag tag) {
        if (tag == null) throw new DataException("Input tag is null");
        if (tag.getId() != null) throw new DataException("Id of input tag has already set.");
        if (mapper.select(user, tag.getName()) != null) throw new DataException("Name of input tag has already used.");
        tag.setUser(user.getId());
        mapper.insert(user, tag);
    }

    /**
     * GET /tag/{name}
     */
    public Tag get(User user, String name) {
        if (name == null) throw new DataException("Input name is null");
        return mapper.select(user, name);
    }

    /**
     * PUT /tag
     */
    public void update(User user, Tag tag) {
        if (tag == null) throw new DataException("Input tag is null");
        if (tag.getId() == null) throw new DataException("Id of input tag is not set.");
        if (!tag.getUser().equals(user.getId())) throw new DataException("Wrong user.");

        Tag otherTag = mapper.select(user, tag.getName());
        if (otherTag != null && !otherTag.getId().equals(tag.getId())) throw new DataException("Name of input tag has already used.");
        
        tag.setMtime(new Timestamp(System.currentTimeMillis()));
        mapper.update(user, tag);
    }

    /**
     * DELETE /tag/{name}
     */
    public void delete(User user, String name) {
        if (name == null) throw new DataException("Input id is null");
        mapper.delete(user, name);
    }

    // ----- Search ----- //

    /**
     * GET /{resource}
     */
    public List<Tag> search(
        User user,
        List<String> includeText,
        List<String> excludeText,
        Integer limit,
        Integer offset
    ) {
        return mapper.search(user, includeText, excludeText, limit, offset);
    }

}
