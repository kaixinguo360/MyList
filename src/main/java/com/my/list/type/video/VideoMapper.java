package com.my.list.type.video;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VideoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Video record);

    Video selectByPrimaryKey(Long id);

    List<Video> selectAll();

    int updateByPrimaryKey(Video record);
}
