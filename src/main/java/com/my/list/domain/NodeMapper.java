package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeMapper extends SimpleMapper<Node> {
    int updateByPrimaryKeyWithUserId(Long userId, Node node);
    int deleteByPrimaryKeyWithUserId(Long userId, Long id);
    int insertWithUserId(Long userId, Node node);
    Node selectByPrimaryKeyWithUserId(Long userId, Long id);
    List<Node> selectAllByListIdWithUserId(Long userId, Long listId);
    List<Node> selectAllByListId(Long listId);
}
