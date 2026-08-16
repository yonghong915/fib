package com.fib.ecny.base.service;

import com.fib.ecny.base.IEnumFunction;
import lombok.Getter;

@Getter
public enum EcnyErrorCode implements IEnumFunction {
    TIMEOUT("888888", "E100888888", "交易超时"),
    FAIL("9999999", "E100999999", "失败"),
    SUCCESS("0000000", "E100000000", "成功"),

    E1000002("0000002", "E100000001", "参数为空"),

    UWAP_R_0000("0000002", "E100000001", "参数为空"),

    UWAP_R_9999("PGO000", "999", "参数为空"),
    UWAP_R_0001("PGO000", "999", "参数为空"),

    DB_EXP("0000001", "E100000001", "数据库异常");
    private final String intCode;
    private final String strCode;
    private final String message;

    EcnyErrorCode(String intCode, String strCode, String message) {
        this.intCode = intCode;
        this.strCode = strCode;
        this.message = message;
    }

    @Override
    public String getDesc() {
        return message;
    }
}
