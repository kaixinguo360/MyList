package com.my.list.domain;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

    void insert(Image image);

    void update(Image image, boolean isSimple);

    void delete(Long id);
    void deleteAll(List<Long> ids);

    Image select(Long id);
    List<Image> selectAll();
    List<Long> selectAllHangingIds();
}
