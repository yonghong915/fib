package com.fib.ecny.base.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 交易状态
 */
@Getter
public enum TranStatusEnum {
    INITIALIZED(0, "初始化"),
    PROCESSING(1, "处理中"),
    SUCCESS(2, "成功"),
    FAILED(3, "失败"),
    REVERSED(4, "已冲正"),
    ADJUSTED(5, "已调账");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    TranStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TranStatusEnum ofCode(Integer code) {
        for (TranStatusEnum s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("非法交易状态:" + code);
    }
}
