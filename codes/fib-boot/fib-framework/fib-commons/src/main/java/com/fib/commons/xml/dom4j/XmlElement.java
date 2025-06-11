package com.fib.commons.xml.dom4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface XmlElement {
String name() default "";

int order() default Integer.MAX_VALUE; // 默认顺序为最大值（排在最后）;

boolean isCData() default false; // 是否使用CDATA
}
