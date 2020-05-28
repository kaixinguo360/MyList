package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartMapper {
    
    void insertChildren(@Param("parentId") Long parentId, @Param("childIds") List<Long> childIds);
    void deleteChildren(@Param("parentId") Long parentId, @Param("childIds") List<Long> childIds);
    void deleteAllChildren(@Param("parentId") Long parentId);
    List<Node> selectAllChildren(@Param("parentId") Long parentId);
    Integer countChildren(@Param("parentId") Long parentId);

    void insertParents(@Param("childId") Long childId, @Param("parentIds") List<Long> parentIds); // TODO: Add userId check, Add order setting
    void deleteParents(@Param("userId") Long userId, @Param("parentIds") Long childId, List<Long> parentIds);
    void deleteAllParent(@Param("userId") Long userId, @Param("childId") Long childId);
    List<Node> selectAllParent(@Param("userId") Long userId, @Param("childId") Long childId);
    Integer countParent(@Param("userId") Long userId, @Param("childId") Long childId);
}
