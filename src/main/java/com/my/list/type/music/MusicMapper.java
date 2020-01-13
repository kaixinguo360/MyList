package com.my.list.type.music;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Music record);

    Music selectByPrimaryKey(Long id);

    List<Music> selectAll();

    int updateByPrimaryKey(Music record);
}
