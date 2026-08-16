package com.fib.ecny.base.enums;

import com.fib.ecny.base.IEnumFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements IEnumFunction {
    SUCCESS(1, "000000", "成功"),
    FAIL(2, "999999", "失败"),
    TIMEOUT(3, "888888", "超时");

    private final Integer intCode;
    private final String strCode;
    private final String desc;
    ErrorCode(int code, String msg, String desc) {
        this.intCode = code;
        this.strCode = msg;
        this.desc = desc;
    }
    public Integer getIntCode(){
        return 0;
    }

    public String getDesc(){
        return "";
    }

    public String getStrCode(){
        return "";
    }
}
