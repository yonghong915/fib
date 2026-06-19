package com.fib.ecny.trans.controller;

import jakarta.validation.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorUtils {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T obj, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj, groups);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(",")));
        }

//        if (!violations.isEmpty()) {
//            // 处理校验失败的情况，例如返回错误信息等。
//            throw new ValidationException("校验失败");
//            return;
//            //return ResponseEntity.badRequest().body("校验失败: " + violations); // 需要更精细的处理方式，比如提取具体错误信息。
//        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
