package com.fib.ecny.base.enums;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    String message() default "字段码值不在合法范围内";

    Class<?>[] groups() default {};

    Class<? extends ICodeEnum> enumClass();

    boolean allowNull() default true;
}
