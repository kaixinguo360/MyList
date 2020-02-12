package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeMapper {

    void insert(Node node);

    void update(Node node, boolean isSimple);

    void delete(Long id);
    void deleteAll(List<Long> ids);

    Node select(Long id);
    List<Node> selectAll();
    List<Long> selectAllHangingIds();
}
