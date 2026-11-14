package com.fib.ecny.base.enums;

import lombok.Getter;

@Getter
public enum ChannelEnum implements ICodeEnum {
    COUTER("110", "柜面"),
    UWAP("uwap","uwap"),
    DCEP("dcep","dcep");
    private final String code;
    private final String desc;

    ChannelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
