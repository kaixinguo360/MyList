package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartMapper {
    
    void insertChildren(Long parentId, List<Long> childIds);
    void deleteChildren(Long parentId, List<Long> childIds);
    void deleteAllChildren(Long parentId);
    List<Node> selectAllChildren(Long parentId);
    Integer countChildren(Long parentId);
    
    void deleteAllParent(Long userId, Long childIds);
    List<Node> selectAllParent(Long userId, Long childId);
    Integer countParent(Long userId, Long childId);

    void clean();
}
