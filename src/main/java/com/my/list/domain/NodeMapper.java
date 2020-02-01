package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NodeMapper extends CrudMapper<Node> {
    int updateByPrimaryKeyWithUserId(Long userId, Node node);
    int deleteByPrimaryKeyWithUserId(Long userId, Long id);
    int insertWithUserId(Long userId, Node node);
    Node selectByPrimaryKeyWithUserId(Long userId, Long id);
}
