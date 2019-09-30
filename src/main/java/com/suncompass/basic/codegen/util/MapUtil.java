package com.suncompass.basic.codegen.util;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {

    public static Map<String, Object> obj2map(Object source, String[] propertyNames) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (source == null)
            return null;
        if (propertyNames == null || propertyNames.length == 0) {
            return null;
        }
        for (String propertyName : propertyNames) {
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(source.getClass(), propertyName);
            if (descriptor != null && descriptor.getReadMethod() != null) {
                try {
                    Method readMethod = descriptor.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source, null);
                    map.put(propertyName, value);
                } catch (Exception ex) {
                    throw new RuntimeException("Could not copy properties from source to target", ex);
                }
            }
        }
        return map;
    }

    public static List<Map<String, Object>> list2map(List list, String[] propertyNames) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Object obj : list) {
            Map<String, Object> map = MapUtil.obj2map(obj, propertyNames);
            listMap.add(map);
        }
        return listMap;
    }

    public static <T> T map2obj(Map<String, Object> map, Class<T> cls) {
        T obj = null;
        try {
            obj = cls.newInstance();
            for (Field field : cls.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object object = map.get(field.getName());
                    if (object != null && field.getType().isAssignableFrom(object.getClass())) {
                        field.set(obj, object);
                    }
                    field.setAccessible(flag);
                }
            }
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> List<T> maps2objs(List<Map<String, Object>> listMap, Class<T> cls) {
        List<T> listObj = new ArrayList<>();
        for (int i = 0; i < listMap.size(); i++) {
            T obj = map2obj(listMap.get(i), cls);
            listObj.add(obj);
        }
        return listObj;
    }

}
