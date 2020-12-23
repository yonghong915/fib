package com.fib.core.base.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fib.core.util.ConstantUtil;

/**
 * 数据源切换注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface DS {
	String value() default ConstantUtil.DSType.DS_TYPE_SYSDB;
}