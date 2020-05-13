package com.my.list.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {

    void insert(@Param("user") User user, @Param("type") String type, @Param("group") Group group);
    Group select(@Param("user") User user, @Param("type") String type, @Param("name") String name);
    void update(@Param("user") User user, @Param("type") String type, @Param("group") Group group);
    void delete(@Param("user") User user, @Param("type") String type, @Param("name") String name);
    
    List<Group> selectAll(
        @Param("user") User user,
        @Param("type") String type,
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

}
