//package com.fib.interceptor;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import com.fib.core.threadpool.ThreadMdcUtil;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class RequestInterceptor implements HandlerInterceptor {
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//		ThreadMdcUtil.setTraceIdIfAbsent();
//		return true;
//	}
//
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//		ThreadMdcUtil.remove();
//	}
//}
