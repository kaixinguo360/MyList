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

    void insertParents(Long childId, List<Long> parentIds); // TODO: Add userId check, Add order setting
    void deleteParents(Long userId, Long childId, List<Long> parentIds);
    void deleteAllParent(Long userId, Long childId);
    List<Node> selectAllParent(Long userId, Long childId);
    Integer countParent(Long userId, Long childId);

    void clean();
}
