package com.my.list.domain;

import com.my.list.domain.interfaces.BatchCurdMapper;
import com.my.list.domain.interfaces.SingleCurdMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageMapper extends SingleCurdMapper<Image>, BatchCurdMapper<Image> {
}
