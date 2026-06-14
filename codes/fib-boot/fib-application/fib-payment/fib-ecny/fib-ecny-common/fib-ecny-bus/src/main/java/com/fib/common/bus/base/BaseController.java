package com.fib.common.bus.base;

import org.springframework.lang.NonNull;

public abstract class BaseController {
    protected RespEntity execute(@NonNull Class clazz, @NonNull BizContext bizContext) {
        clazz.getSimpleName();
        IService iService = null;
        return iService.arrow(bizContext);
    }
}
