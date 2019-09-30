package com.suncompass.basic.codegen.service.impl;

import com.suncompass.basic.codegen.mapper.BaseMapper;
import com.suncompass.basic.codegen.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private BaseMapper<T> baseMapper;

    @Override
    public Map<String, Object> findAll(Integer page, Integer limit, T t) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", baseMapper.count(t));
        List<T> list = null;
        if (page != null) {
            list = baseMapper.selectAll((page - 1) * limit, limit, t);
        } else {
            list = baseMapper.selectAll(null, null, t);
        }
        map.put("records", list);
        return map;
    }

    @Override
    public T get(Serializable id) {
        return (T) baseMapper.get(id);
    }

    @Override
    public Integer insert(T t) {
        return baseMapper.insert(t);
    }

    @Override
    public Integer update(T t) {
        return baseMapper.update(t);
    }

    @Override
    public Integer removeId(Serializable id) {
        return baseMapper.delete(id);
    }
}
