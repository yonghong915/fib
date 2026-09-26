package com.fib.ecny.base.enums;

import lombok.Getter;

/**
 * 冲正标志
 */
@Getter
public enum ReversalFlagEnum {
    NOT_REVERSED("N", "未冲正/未调账"),
    PROCESSING("P", "处理中"),
    REVERSED("Y", "已冲正"),
    ADJUSTED("R", "已调账");

    private final String code;
    private final String desc;

    ReversalFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
