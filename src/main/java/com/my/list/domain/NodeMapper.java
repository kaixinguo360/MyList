package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Node record);

    Node selectByPrimaryKey(Long id);

    List<Node> selectAll();

    int updateByPrimaryKey(Node record);
}
