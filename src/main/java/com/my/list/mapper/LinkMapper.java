package com.my.list.mapper;

import com.my.list.entity.MainData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LinkMapper {
    
    void insertChildren(@Param("parentId") Long parentId, @Param("childIds") List<Long> childIds);
    void deleteChildren(@Param("parentId") Long parentId, @Param("childIds") List<Long> childIds);
    void deleteAllChildren(@Param("parentId") Long parentId);
    List<MainData> selectAllChildren(@Param("parentIds") List<Long> parentIds);
    Integer countChildren(@Param("parentId") Long parentId);

    void insertParents(@Param("childId") Long childId, @Param("parentIds") List<Long> parentIds); // TODO: Add userId check, Add order setting
    void deleteParents(@Param("userId") Long userId, @Param("childId") Long childId, @Param("parentIds") List<Long> parentIds);
    void deleteAllParent(@Param("userId") Long userId, @Param("childId") Long childId);
    List<MainData> selectAllParent(@Param("userId") Long userId, @Param("childIds") List<Long> childIds);
    Integer countParent(@Param("userId") Long userId, @Param("childId") Long childId);
}
