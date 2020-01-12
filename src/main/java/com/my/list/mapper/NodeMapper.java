package com.my.list.mapper;

import com.my.list.bean.Node;
import java.util.List;

public interface NodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Node record);

    Node selectByPrimaryKey(Long id);

    List<Node> selectAll();

    int updateByPrimaryKey(Node record);
}