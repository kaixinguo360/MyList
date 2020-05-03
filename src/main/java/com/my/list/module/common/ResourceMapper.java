package com.my.list.module.common;

import com.my.list.module.common.mapper.SingleCrudMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceMapper<T extends Resource> extends SingleCrudMapper<T> {
}
