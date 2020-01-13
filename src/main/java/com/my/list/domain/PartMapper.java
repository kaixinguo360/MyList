package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Part record);

    Part selectByPrimaryKey(Long id);

    List<Part> selectAll();

    int updateByPrimaryKey(Part record);
}
