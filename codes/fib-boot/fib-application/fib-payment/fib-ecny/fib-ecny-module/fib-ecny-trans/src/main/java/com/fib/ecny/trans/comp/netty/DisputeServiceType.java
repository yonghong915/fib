package com.fib.ecny.trans.comp.netty;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DisputeServiceType {
    String[] values() default {};
}
