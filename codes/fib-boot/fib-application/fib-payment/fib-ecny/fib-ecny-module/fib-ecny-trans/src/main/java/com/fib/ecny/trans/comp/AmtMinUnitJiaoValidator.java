package com.fib.ecny.trans.comp;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class AmtMinUnitJiaoValidator implements ConstraintValidator<AmtMinUnitJiao, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 为空不校验，如需非空搭配 @NotNull
        if (value == null) {
            return true;
        }

        BigDecimal amount;
        if (value instanceof BigDecimal) {
            amount = (BigDecimal) value;
        } else if (value instanceof Double) {
            amount = BigDecimal.valueOf((Double) value);
        } else if (value instanceof Float) {
            amount = BigDecimal.valueOf((Float) value);
        } else {
            // 不支持的类型直接不通过
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }


        // 核心规则：乘以10后必须是整数 → 最多1位小数
        BigDecimal multiplyTen = amount.multiply(BigDecimal.TEN);
        // 判断是否整数（小数部分等于0）
        return multiplyTen.stripTrailingZeros().scale() <= 0;
    }
}
