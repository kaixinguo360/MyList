package com.my.list.domain;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CrudMapper<T> {
    int deleteByPrimaryKey(@Param("id") Long id);
    int insert(T record);
    T selectByPrimaryKey(@Param("id") Long id);
    List<T> selectAll();
    int updateByPrimaryKey(T record);
}
