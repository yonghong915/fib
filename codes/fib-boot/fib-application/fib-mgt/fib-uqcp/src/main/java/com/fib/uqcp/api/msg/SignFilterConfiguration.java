//package com.fib.uqcp.api.msg;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.fib.uqcp.autoconfigure.crypto.filter.UriFormatFilter;
//
//import jakarta.servlet.Filter;
//
//@Configuration
//public class SignFilterConfiguration {
//	@Value("${sign.maxTime:10}")
//	private String signMaxTime;
//	// filter中的初始化参数
//	private Map<String, String> initParametersMap = new HashMap<>();
//
//	@Bean
//	public FilterRegistrationBean contextFilterRegistrationBean() {
//		initParametersMap.put("signMaxTime", signMaxTime);
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setFilter(signFilter());
//		registration.setInitParameters(initParametersMap);
//		registration.addUrlPatterns("/sign/*");
//		registration.setName("SignFilter");
//		// 设置过滤器被调用的顺序
//		registration.setOrder(1);
//		return registration;
//	}
//
//	@Bean
//	public Filter signFilter() {
//		return new SignFilter();
//	}
//
//	@Bean
//	FilterRegistrationBean<UriFormatFilter> contextFilterRegistrationBean1() {
//		FilterRegistrationBean<UriFormatFilter> registration = new FilterRegistrationBean<>();
//		registration.setFilter(new UriFormatFilter());
//		registration.setInitParameters(initParametersMap);
//		registration.addUrlPatterns(("/*"));
//		registration.setName("SignFilter1");
//		// 设置过滤器被调用的顺序
//		registration.setOrder(1);
//		return registration;
//	}
//}