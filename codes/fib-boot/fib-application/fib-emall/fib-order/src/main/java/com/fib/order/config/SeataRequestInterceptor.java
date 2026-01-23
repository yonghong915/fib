package com.fib.order.config;

import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.commons.lang.StringUtils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;

@Configuration
public class SeataRequestInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate requestTemplate) {
		String xid = RootContext.getXID();
		if (StringUtils.isNotBlank(xid)) {
			// 构建请求头
			requestTemplate.header("TX_XID", xid);
		}
	}
}