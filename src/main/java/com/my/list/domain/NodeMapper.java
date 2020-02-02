package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeMapper {
    int delete(Long id);
    int insert(Node node);
    Node select(Long id);
    List<Node> selectAll();
    int update(Node node, boolean isSimple);
}
