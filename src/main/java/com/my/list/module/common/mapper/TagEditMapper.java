package com.my.list.module.common.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagEditMapper {

    void addTags(
        @Param("id") Long id,
        @Param("tagIds") List<Long> tagIds
    );

    void removeTags(
        @Param("id") Long id,
        @Param("tagIds") List<Long> tagIds
    );

    List<String> getTags(
        @Param("id") Long id
    );

    void clearTags(
        @Param("id") Long id
    );
}
