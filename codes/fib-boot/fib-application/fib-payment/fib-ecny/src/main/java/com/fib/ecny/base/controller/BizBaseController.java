package com.fib.ecny.base.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.fib.ecny.base.BizContext;
import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.RespEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class BizBaseController {
    private static final String FLAG = "serviceFlag";
    protected boolean insertMessage(BizContext context) {
        // 插入信息日志信息表
        return true;
    }

    protected void checkField(BizContext context) {
        //校验请求参数，实体类注解校验
        ValidatorUtils.validate(context.getRequest());
    }

    protected RespEntity execute(BizContext context, RespEntity response, Class<?> bizService) {
        Object bizObj = SpringUtil.getBean(bizService);
        if (Objects.isNull(bizObj) || !(bizObj instanceof IBizService)) {
            log.error("请求业务服务为空或不是IBizService的子类");
            return RespEntity.fail();
        }
        IBizService biz = (IBizService) bizObj;
        context.put(FLAG,Boolean.TRUE);
        context.put("flag", true);
        return biz.execute(context);
    }
}
