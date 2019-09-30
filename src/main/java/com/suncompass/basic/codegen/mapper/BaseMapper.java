package com.suncompass.basic.codegen.mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<T> {

    List<T> selectAll(@Param("currentRecord") Integer currentRecord, @Param("limit") Integer limit, @Param("t") T t);

    Long count(@Param("t") T t);

    T get(Serializable id);

    int insert(T t);

    int update(@Param("t") T t);

    int delete(Serializable id);
}
