package com.fib.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fib.core.util.ConstantUtil;

import cn.hutool.core.util.IdUtil;

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
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 调用结束后删除
		MDC.remove(ConstantUtil.TRACE_ID);
	}
}