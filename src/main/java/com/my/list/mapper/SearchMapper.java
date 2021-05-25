package com.my.list.mapper;

import com.my.list.entity.MainData;
import com.my.list.entity.filter.Filter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<MainData> getAll(@Param("userId") Long userId, @Param("filter") Filter filter);
}
