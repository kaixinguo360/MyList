package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionMapper {
    void insert(String name, String value);
    void delete(String name);
    String select(String name);
    void update(String name, String value);
}
