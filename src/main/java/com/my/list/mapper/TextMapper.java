package com.my.list.mapper;

import com.my.list.bean.Text;
import java.util.List;

public interface TextMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Text record);

    Text selectByPrimaryKey(Long id);

    List<Text> selectAll();

    int updateByPrimaryKey(Text record);
}