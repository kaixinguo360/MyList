package com.my.list.domain;

import java.util.List;

public interface CrudMapper<T> {
    int deleteByPrimaryKey(Long id);
    int insert(T record);
    T selectByPrimaryKey(Long id);
    List<T> selectAll();
    int updateByPrimaryKey(T record);
}
