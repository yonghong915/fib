package com.fib.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fib.core.interceptor.ApiIdempotentInterceptor;
import com.fib.core.interceptor.LogInterceptor;
import com.fib.core.interceptor.SignInterceptor;
import com.fib.core.interceptor.WhiteListInterceptor;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {
	@Autowired
	private LogInterceptor logInterceptor;

	@Autowired
	private SignInterceptor signInterceptor;

	@Autowired
	private WhiteListInterceptor whiteListInterceptor;

	@Autowired
	private ApiIdempotentInterceptor apiIdempotentInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 访问速率限制 apiLimitRateInterceptor RateLimit guava

		// 公共参数检查 commonInterceptor

		// 日志拦截
		registry.addInterceptor(logInterceptor);

		// 参数签名认证
		registry.addInterceptor(signInterceptor);

		registry.addInterceptor(whiteListInterceptor);

		registry.addInterceptor(apiIdempotentInterceptor);
	}
}
