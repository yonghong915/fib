package com.fib.ecny;

import cn.hutool.extra.spring.SpringUtil;
import io.github.linpeilie.Converter;

public class MapStructUtil {
    Converter CONVERTER = SpringUtil.getBean(Converter.class);
    public <V> V converter(T t, V v){
        return CONVERTER.convert((Object) t,v);
    }
}
