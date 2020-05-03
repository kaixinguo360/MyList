package com.my.list.domain.interfaces;

import com.my.list.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BatchCurdMapper<T> {

    void batchInsert(User user, List<T> resources);
    List<T> batchSelect(User user, List<Long> ids);
    void batchUpdate(User user, List<T> resources);
    void batchDelete(User user, List<Long> ids);
    
}
