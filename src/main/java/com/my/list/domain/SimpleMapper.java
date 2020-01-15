package com.my.list.domain;

import java.util.List;

public interface SimpleMapper<T> {
    int deleteByPrimaryKey(Long id);
    int insert(T record);
    T selectByPrimaryKey(Long id);
    List<T> selectAll();
    int updateByPrimaryKey(T record);
}
