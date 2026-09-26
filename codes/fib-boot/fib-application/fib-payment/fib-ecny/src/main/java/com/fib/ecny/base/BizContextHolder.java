package com.fib.ecny.base;

import com.alibaba.ttl.TransmittableThreadLocal;

public class BizContextHolder {

    // TTL：支持线程池任务提交时快照父线程上下文
    private static final TransmittableThreadLocal<BizContext> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 设置上下文
     */
    public static void set(BizContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 获取上下文
     */
    public static BizContext get() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 安全获取，非null
     */
    public static BizContext getOrNew() {
        BizContext context = CONTEXT_HOLDER.get();
        if (context == null) {
            context = new BizContext();
            CONTEXT_HOLDER.set(context);
        }
        return context;
    }

    /**
     * 清空上下文，必须调用，防止内存泄漏、上下文污染
     * web请求在filter/interceptor执行完毕调用clear()
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

//    public static String getTraceId() {
//        BizContext ctx = get();
//        return ctx == null ? null : ctx.getTraceId();
//    }
//
//    public static void setTraceId(String traceId) {
//        getOrNew().setTraceId(traceId);
//    }
}
