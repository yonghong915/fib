package com.fib.autoconfigure.aop.aspect.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EcaAnnotation {

	/**
	 * 服务名-event:beanName.methondName:condition
	 * 
	 * @return
	 */
	String[] service() default {};
}
