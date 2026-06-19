package com.fib.ecny.trans.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.fib.common.bus.base.BizContext;
import com.fib.common.bus.base.IBusiService;
import com.fib.common.bus.base.RespEntity;

public class BizBaseController {
    protected boolean insertMessage(BizContext context) {
        return true;
    }

    protected void checkField(BizContext context) {
        ValidatorUtils.validate(context.getRequest());
    }

    protected RespEntity execute(BizContext context, RespEntity response, Class<?> bizService) {
        IBusiService biz = (IBusiService) SpringUtil.getBean(bizService);
        if (null == biz) {

        }
        context.put("flag", true);
        return biz.execute(context);
    }
}
