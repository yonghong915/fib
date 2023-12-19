package com.fib.core.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fib.core.util.ConstantUtil;

import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * MDC（Mapped Diagnostic Context，映射调试上下文）拦截器,实现全链路调用日志跟踪
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-09-24
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果有上层调用就用上层的ID
		String traceId = request.getHeader(ConstantUtil.TRACE_ID);
		if (traceId == null) {
			traceId = IdUtil.simpleUUID();
		}

		MDC.put(ConstantUtil.TRACE_ID, traceId);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 调用结束后删除
		MDC.remove(ConstantUtil.TRACE_ID);
	}
}