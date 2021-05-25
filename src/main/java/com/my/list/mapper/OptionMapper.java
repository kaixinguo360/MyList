package com.my.list.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OptionMapper {
    void insert(@Param("name") String name, @Param("value") String value);
    void delete(@Param("name") String name);
    String select(@Param("name") String name);
    void update(@Param("name") String name, @Param("value") String value);
}
