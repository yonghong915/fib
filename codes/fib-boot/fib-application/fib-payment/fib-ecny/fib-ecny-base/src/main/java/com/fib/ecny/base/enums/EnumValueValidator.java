package com.fib.ecny.base.enums;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<EnumValue, String> {
    private Set<String> validCodeSet;
    private boolean allowNull;

    @Override
    public void initialize(EnumValue annotation) {
        ICodeEnum[] constants = annotation.enumClass().getEnumConstants();
        validCodeSet = Arrays.stream(constants).map(ICodeEnum::getCode).collect(Collectors.toSet());
        allowNull = annotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return (Objects.isNull(value) ? allowNull : validCodeSet.contains(value));
    }
}
