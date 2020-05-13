package com.my.list.system.service;

import com.my.list.exception.DataException;
import com.my.list.system.mapper.Group;
import com.my.list.system.mapper.GroupMapper;
import com.my.list.system.mapper.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroupService {
    
    @Autowired private GroupMapper mapper;

    // ----- Single Crud ----- //

    public void create(User user, String type, Group group) {
        if (type == null) throw new DataException("Input type is null");
        if (group == null) throw new DataException("Input group is null");
        if (group.getId() != null) throw new DataException("Id of input group has already set.");
        if (mapper.select(user, type, group.getName()) != null) throw new DataException("Name of input group has already used.");
        group.setUser(user.getId());
        group.setType(type);
        mapper.insert(user, type, group);
    }

    public Group get(User user, String type, String name) {
        if (type == null) throw new DataException("Input type is null");
        if (name == null) throw new DataException("Input name is null");
        return mapper.select(user, type, name);
    }

    public void update(User user, String type, Group group) {
        if (type == null) throw new DataException("Input type is null");
        if (group == null) throw new DataException("Input group is null");
        if (group.getId() == null) throw new DataException("Id of input group is not set.");
        if (!group.getUser().equals(user.getId())) throw new DataException("Wrong user.");

        Group otherGroup = mapper.select(user, type, group.getName());
        if (otherGroup != null && !otherGroup.getId().equals(group.getId())) throw new DataException("Name of input group has already used.");
        
        group.setMtime(new Timestamp(System.currentTimeMillis()));
        mapper.update(user, type, group);
    }

    public void delete(User user, String type, String name) {
        if (type == null) throw new DataException("Input type is null");
        if (name == null) throw new DataException("Input id is null");
        mapper.delete(user, type, name);
    }

    // ----- getAll ----- //
    public List<Group> getAll(
        User user,
        String type,
        Integer limit,
        Integer offset
    ) {
        return mapper.selectAll(user, type, limit, offset);
    }
    
    // ----- getContent ----- //
    public Map<String, List<String>> getValue(User user, String type, String name) {
        Map<String, List<String>> values = new HashMap<>();
        values.put("andTags", new ArrayList<>());
        values.put("orTags", new ArrayList<>());
        values.put("notTags", new ArrayList<>());
        
        Group group = mapper.select(user, type, name);
        if (group != null && !StringUtils.isEmpty(group.getValue())) {
            for (String tag : group.getValue().split(",")) {
                switch (tag.charAt(0)) {
                    case '+':
                        values.get("orTags").add(tag); break;
                    case '-':
                        values.get("notTags").add(tag); break;
                    default:
                        values.get("andTags").add(tag); break;
                }
            }
        }
        
        return values;
    }

}
