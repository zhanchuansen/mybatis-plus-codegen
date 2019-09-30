package com.suncompass.basic.codegen.service;
import java.io.Serializable;
import java.util.Map;

public interface BaseService<T>{

    // 查询所有(分页)
    Map<String, Object> findAll(Integer page, Integer limit, T t);

    T get(Serializable id);

    Integer insert(T t);

    Integer update(T t);

    Integer removeId(Serializable id);
}
