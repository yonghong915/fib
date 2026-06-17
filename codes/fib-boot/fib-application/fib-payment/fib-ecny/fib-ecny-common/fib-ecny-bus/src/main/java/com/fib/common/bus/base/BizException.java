package com.fib.common.bus.base;

import com.fib.common.bus.base.annotation.IEnumFunction;

public class BizException extends RuntimeException {
    private final IEnumFunction errorCode;

    public BizException(IEnumFunction _errorCode) {
        super(_errorCode.getDesc());
        this.errorCode = _errorCode;
    }

}
