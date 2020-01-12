package com.my.list.mapper;

import com.my.list.bean.Image;
import java.util.List;

public interface ImageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Image record);

    Image selectByPrimaryKey(Long id);

    List<Image> selectAll();

    int updateByPrimaryKey(Image record);
}