package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeMapper extends SimpleMapper<Node> {
    List<Node> selectAllByListId(Long list_id);
}
