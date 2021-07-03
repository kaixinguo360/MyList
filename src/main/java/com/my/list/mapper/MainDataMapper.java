package com.my.list.mapper;

import com.my.list.entity.MainData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MainDataMapper {

    void insert(MainData mainData);

    void update(@Param("mainData") MainData mainData, @Param("isSimple") boolean isSimple);

    void delete(@Param("id") Long id);
    void deleteAll(@Param("ids") List<Long> ids);

    MainData select(@Param("id") Long id);
    List<MainData> selectAll();

    List<MainData> selectByIds(@Param("ids") List<Long> ids);
    List<Long> selectAllHangingIds();
}
