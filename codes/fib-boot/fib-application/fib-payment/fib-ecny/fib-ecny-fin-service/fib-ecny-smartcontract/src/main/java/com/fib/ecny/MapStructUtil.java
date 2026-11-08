package com.fib.ecny;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.github.linpeilie.Converter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapStructUtil {
    private final static Converter CONVERTER = SpringUtil.getBean(Converter.class);

    /**
     * 将T类型对象，转换为desc类型的对象赋值并返回desc对象
     *
     * @param source
     * @param desc
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> V converter(T source, Class<V> desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        return CONVERTER.convert(source, desc);
    }

    /**
     * 将T类型对象，按照配置的映射字段规则，给desc类型的对象赋值并返回desc对象
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> V converter(T source, V desc) {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        if (ObjectUtil.isNull(desc)) {
            return null;
        }
        return CONVERTER.convert(source, desc);
    }

    /**
     * 将T类型的集合，转换为desc类型的集合并返回
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @param <T>
     * @param <V>
     * @return desc
     */
    public static <T, V> List<V> converter(List<T> sourceList, Class<V> desc) {
        if (ObjectUtil.isNull(sourceList)) {
            return null;
        }
        if (CollUtil.isEmpty(sourceList)) {
            return CollUtil.newArrayList();
        }
        if (sourceList.getFirst().getClass().equals(desc)) {
            return (List<V>) sourceList;
        }
        return CONVERTER.convert(sourceList, desc);
    }

    /**
     * 将Map转换为beanClass类型的集合并返回
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    public static <T> T converter(Map<String, Object> map, Class<T> beanClass) {
        if (MapUtil.isEmpty(map)) {
            return null;
        }
        if (ObjectUtil.isNull(beanClass)) {
            return null;
        }
        return CONVERTER.convert(map, beanClass);
    }
}
