package com.my.list.module.image;

import com.my.list.module.common.mapper.SearchMapper;
import com.my.list.module.common.mapper.SingleCrudMapper;
import com.my.list.module.common.mapper.TagEditMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper extends SingleCrudMapper<Image>, SearchMapper<Image>, TagEditMapper {
}
