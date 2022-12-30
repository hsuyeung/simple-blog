package com.hsuyeung.blog.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型类转换相关工具类
 *
 * @author hsuyeung
 * @date 2022/05/16
 */
public final class ConvertUtil {

    /**
     * 将对象转为指定泛型的对象
     *
     * @param source 数据源
     * @param clazz  要转换为的类
     * @param <T>    类的类型泛型
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, Class<T> clazz) {
        T t;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("类型转换失败", e);
        }
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 将对象集合转为指定泛型的对象集合
     *
     * @param sourceList 数据源
     * @param clazz      要转换为的类
     * @param <T>        类的类型泛型
     * @return 转换后的对象
     */
    public static <T> List<T> convertList(List<?> sourceList, Class<T> clazz) {
        List<T> resultList = new ArrayList<>(sourceList.size());
        sourceList.forEach(source -> resultList.add(convert(source, clazz)));
        return resultList;
    }

    /**
     * 将 Map 的 value 转为指定的泛型
     *
     * @param sourceMap 数据源
     * @param valueType 值的类型
     * @param <K>       键类型
     * @param <V>       值类型
     * @return 转换后的 Map
     */
    public static <K, V> Map<K, V> convertMap(Map<K, ?> sourceMap, Class<V> valueType) {
        Map<K, V> resultMap = new HashMap<>(sourceMap.size());
        sourceMap.forEach((k, v) -> resultMap.put(k, convert(v, valueType)));
        return resultMap;
    }

    /**
     * 将 String 转换为其他基本数据类型
     *
     * @param source    源字符串
     * @param valueType 基本数据类型
     * @param <T>       基本数据类型
     * @return 基本数据类型值
     */
    public static <T> T convertType(String source, Class<T> valueType) {
        try {
            Constructor<T> constructor = valueType.getConstructor(String.class);
            return constructor.newInstance(source);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalArgumentException("类型转换失败", e);
        }
    }

    private ConvertUtil() {

    }
}
