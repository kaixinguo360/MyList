package com.my.list.json.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.list.json.JSON;
import com.my.list.json.JacksonJsonFilter;
import org.springframework.util.StringUtils;

public class CustomerJsonSerializer {


    ObjectMapper mapper = new ObjectMapper();
    JacksonJsonFilter jacksonFilter = new JacksonJsonFilter();

    /**
     * @param clazz target type
     * @param include include fields
     * @param filter filter fields
     */
    public void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null || clazz == void.class) return;
        if (!StringUtils.isEmpty(include)) {
            jacksonFilter.include(clazz, include.split(","));
        }
        if (!StringUtils.isEmpty(filter)) {
            jacksonFilter.filter(clazz, filter.split(","));
        }
        mapper.addMixIn(clazz, jacksonFilter.getClass());
    }

    public String toJson(Object object) throws JsonProcessingException {
        mapper.setFilterProvider(jacksonFilter);
        return mapper.writeValueAsString(object);
    }

    public void filter(JSON json) {
        this.filter(json.type(), json.include(), json.exclude());
    }
}

