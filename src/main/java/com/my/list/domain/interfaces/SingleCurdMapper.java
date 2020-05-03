package com.my.list.domain.interfaces;

import com.my.list.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SingleCurdMapper<T> {

    void insert(@Param("user") User user, @Param("resource") T resource);
    T select(@Param("user") User user, @Param("id") Long id);
    List<T> selectAll(@Param("user") User user, @Param("limit") Integer limit, @Param("offset") Integer offset);
    void update(@Param("user") User user, @Param("resource") T resource);
    void delete(@Param("user") User user, @Param("id") Long id);

}
