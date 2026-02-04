package com.fib.pay.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Autowired
	private SeataHandlerInterceptor seataHandlerInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		// 注册HandlerInterceptor，拦截所有请求
		registry.addInterceptor(seataHandlerInterceptor).addPathPatterns(new String[] { "/**" });
	}
}