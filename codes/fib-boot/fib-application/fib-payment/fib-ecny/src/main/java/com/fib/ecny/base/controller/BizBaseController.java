package com.fib.ecny.base.controller;

import com.fib.ecny.base.BizContext;
import com.fib.ecny.base.IBizService;
import com.fib.ecny.base.RespEntity;
import com.fib.ecny.common.util.ValidatorUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public abstract class BizBaseController {

    @Resource
    private BeanInstanceCacheHolder beanInstanceCacheHolder;
    private static final String FLAG = "serviceFlag";

    protected boolean insertMessage(BizContext context) {
        // 插入信息日志信息表
        return true;
    }

    protected void checkField(BizContext context) {
        //校验请求参数，实体类注解校验
        ValidatorUtils.validate(context.getRequest());
    }


    protected RespEntity execute(BizContext context, RespEntity response, Class<?> bizServiceClazz, IBizService bizService) {
        context.put(FLAG, Boolean.TRUE);
        context.put("flag", true);
        try {
            //IBizService ss = (IBizService) beanInstanceCacheHolder.getBean(bizServiceClazz);
            return Optional.ofNullable(bizService).orElse((IBizService) beanInstanceCacheHolder.getBean(bizServiceClazz)).execute(context);
            // return bizService.execute(context);
        } catch (Exception e) {
            log.error("请求业务服务为空或不是IBizService的子类", e);
            return RespEntity.fail();
        }
    }
}
