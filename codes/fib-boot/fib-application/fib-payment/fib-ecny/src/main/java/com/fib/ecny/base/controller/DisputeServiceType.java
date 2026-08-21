package com.fib.ecny.base.controller;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisputeServiceType {
    String[] value() default {};
}
