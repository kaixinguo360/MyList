package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NodeMapper {

    void insert(Node node);

    void update(@Param("node") Node node, @Param("isSimple") boolean isSimple);

    void delete(@Param("id") Long id);
    void deleteAll(@Param("ids") List<Long> ids);

    Node select(@Param("id") Long id);
    List<Node> selectAll();
    List<Long> selectAllHangingIds();
}
