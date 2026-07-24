package com.fib.ecny.trans.comp;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AmtMinUnitJiaoValidator.class)
public @interface AmtMinUnitJiao {
    String message() default "金额最小单位为角，最多保留1位小数";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
