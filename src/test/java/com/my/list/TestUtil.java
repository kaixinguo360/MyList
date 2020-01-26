package com.my.list;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

@Mapper
public interface TestUtil {
    @Options(statementType= StatementType.CALLABLE)
    @Select("call clean_all()")
    void clean_all();
}
