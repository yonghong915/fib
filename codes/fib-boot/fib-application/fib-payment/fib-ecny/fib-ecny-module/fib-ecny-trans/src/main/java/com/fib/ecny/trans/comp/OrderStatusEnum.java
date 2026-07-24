package com.fib.ecny.trans.comp;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    ENABLED(1, "启用"),
    DISABLED(0, "禁用");

    @EnumValue
    private final int code;

    @JsonValue
    private final String desc;
}
