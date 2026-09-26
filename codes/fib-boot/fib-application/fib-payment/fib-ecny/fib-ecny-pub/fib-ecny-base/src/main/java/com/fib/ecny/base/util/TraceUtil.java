package com.fib.ecny.base.util;

import cn.hutool.core.util.IdUtil;
import org.apache.logging.log4j.ThreadContext;

public class TraceUtil {
    public static final String TRACE_ID_KEY = "traceId";

    /**
     * 生成并设置traceId
     */
    public static String setTraceId() {
        String traceId = generateTraceId();
        ThreadContext.put(TRACE_ID_KEY, traceId);
        return traceId;
    }

    /**
     * 使用外部传入traceId（比如网关透传过来）
     */
    public static void setTraceId(String traceId) {
        ThreadContext.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 获取当前traceId
     */
    public static String getTraceId() {
        return ThreadContext.get(TRACE_ID_KEY);
    }

    /**
     * 清除上下文，必须在请求/任务结束调用
     */
    public static void clear() {
        ThreadContext.remove(TRACE_ID_KEY);
    }

    /**
     * 生成traceId，可按金融规范改成雪花算法
     */
    private static String generateTraceId() {
        return IdUtil.getSnowflakeNextIdStr();
    }
}