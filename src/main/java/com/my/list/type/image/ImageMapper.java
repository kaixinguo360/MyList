package com.my.list.type.image;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Image record);

    Image selectByPrimaryKey(Long id);

    List<Image> selectAll();

    int updateByPrimaryKey(Image record);
}
