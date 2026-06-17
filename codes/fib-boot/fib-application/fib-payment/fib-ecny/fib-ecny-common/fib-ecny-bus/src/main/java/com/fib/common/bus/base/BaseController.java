package com.fib.common.bus.base;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.lang.NonNull;

public class BaseController {
    public RespEntity executor(@NonNull Class<?> clazz,@NonNull UwapBaseRequest uwapBaseRequest) {
        BizContext bizContext = new BizContext();
        MsgHeader msgHeader = uwapBaseRequest.getMsgHeader();
        Object obj = SpringUtil.getBean(clazz);
        if (obj instanceof IService iService) {
            return iService.arrow(bizContext);
        }
        return null;
    }
}
