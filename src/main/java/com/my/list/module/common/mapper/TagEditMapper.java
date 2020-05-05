package com.my.list.module.common.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagEditMapper {

    void addTag(
        @Param("resourceId") Long resourceId,
        @Param("tagId") Long tagId
    );

    void removeTag(
        @Param("resourceId") Long resourceId,
        @Param("tagId") Long tagId
    );
    
    List<String> getTags(
        @Param("resourceId") Long resourceId
    );
}
