package com.my.list.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {

    void insert(@Param("user") User user, @Param("resource") Tag resource);
    Tag select(@Param("user") User user, @Param("name") String name);
    void update(@Param("user") User user, @Param("resource") Tag resource);
    void delete(@Param("user") User user, @Param("name") String name);
    
    List<Tag> search(
        @Param("user") User user,
        @Param("includeText") List<String> includeText,
        @Param("excludeText") List<String> excludeText,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );
}
