package com.fib.ecny.trans.controller;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

//common-core
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ValidatorUtils {
    private static final Validator validator = SpringUtil.getBean(Validator.class);

    /**
     * 手动校验参数
     *
     * @param object 入参
     * @param groups 校验分组
     * @param <T>
     */
    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> first = violations.iterator().next();
            log.error("校验失败-对象:{},字段及错误:{},分组:{}", object.getClass().getSimpleName(),
                    violations.stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.joining(",")),
                    groups.length > 0 ? groups[0].getSimpleName() : "默认分组");
            throw new ValidationException(first.getMessage());
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
