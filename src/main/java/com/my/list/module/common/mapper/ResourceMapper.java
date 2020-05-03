package com.my.list.module.common.mapper;

import com.my.list.module.common.Resource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceMapper<T extends Resource> extends SingleCrudMapper<T>, SearchMapper<T> {
}
