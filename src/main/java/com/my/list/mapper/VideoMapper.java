package com.my.list.mapper;

import com.my.list.bean.Video;
import java.util.List;

public interface VideoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Video record);

    Video selectByPrimaryKey(Long id);

    List<Video> selectAll();

    int updateByPrimaryKey(Video record);
}