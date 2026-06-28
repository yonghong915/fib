package com.fib.common.bus.base;

public interface IBizService {
    default RespEntity execute(BizContext context) {
        return this.arrange(context);
    }

    /**
     * 应用层上下文编排实现接口
     *
     * @param context 上下文容器
     * @return 处理结果
     */
    RespEntity arrange(BizContext context);

    /**
     * 应用层上下文编排实现接口
     *
     * @param context 上下文容器
     * @return 处理结果
     */
    default RespEntity corpArrange(BizContext context) {
        return null;
    }
}
