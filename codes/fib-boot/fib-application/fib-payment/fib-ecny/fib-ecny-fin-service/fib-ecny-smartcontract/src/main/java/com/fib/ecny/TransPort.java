package com.fib.ecny;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TransPort {
    TEST("","");

    private final String code;
    private final String desc;

    public TransPort fromByCode(String code){
        return Arrays.stream(values()).filter(item->item.code.equals(code)).findFirst().orElse(TEST);
    }

}
