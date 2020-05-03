package com.my.list.service;

import com.my.list.ResourceConfigManager;
import com.my.list.domain.Resource;
import com.my.list.domain.User;
import com.my.list.domain.interfaces.SingleCurdMapper;
import com.my.list.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SingleCrudService {

    @Autowired
    private ResourceConfigManager manager;

    /**
     * POST /{resource}
     */
    public void create(String type, User user, Resource resource) {
        SingleCurdMapper<Resource> mapper = manager.getMapper(type, SingleCurdMapper.class);
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() != null) throw new DataException("Id of input resource has already set.");
        resource.setUser(user.getId());
        mapper.insert(user, resource);
    }

    /**
     * GET /{resource}/{id}
     */
    public Resource read(String type, User user, Long id) {
        SingleCurdMapper<Resource> mapper = manager.getMapper(type, SingleCurdMapper.class);
        if (id == null) throw new DataException("Input id is null");
        return mapper.select(user, id);
    }

    /**
     * GET /{resource}
     */
    public List<Resource> readAll(String type, User user, Integer limit, Integer offset) {
        SingleCurdMapper<Resource> mapper = manager.getMapper(type, SingleCurdMapper.class);
        return mapper.selectAll(user, limit, offset);
    }

    /**
     * PUT /{resource}
     */
    public void update(String type, User user, Resource resource) {
        SingleCurdMapper<Resource> mapper = manager.getMapper(type, SingleCurdMapper.class);
        if (resource == null) throw new DataException("Input resource is null");
        if (resource.getId() == null) throw new DataException("Id of input resource is not set.");
        if (!resource.getUser().equals(user.getId())) throw new DataException("Wrong user.");
        resource.setMtime(new Timestamp(System.currentTimeMillis()));
        mapper.update(user, resource);
    }

    /**
     * DELETE /{resource}/{id}
     */
    public void delete(String type, User user, Long id) {
        SingleCurdMapper<Resource> mapper = manager.getMapper(type, SingleCurdMapper.class);
        if (id == null) throw new DataException("Input id is null");
        mapper.delete(user, id);
    }
}
