package com.fib.ecny.common.exception;

import com.fib.ecny.base.IEnumFunction;

public class BizException extends RuntimeException {
    private final IEnumFunction errorCode;

    private String message;

    public BizException(IEnumFunction _errorCode) {
        super(_errorCode.getDesc());
        this.errorCode = _errorCode;
    }

    public BizException(IEnumFunction _errorCode, String message) {
        super(message);
        this.errorCode = _errorCode;
        this.message = message;
    }
}
