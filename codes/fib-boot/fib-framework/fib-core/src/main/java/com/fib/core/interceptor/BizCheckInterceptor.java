package com.fib.core.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fib.core.annotation.BizCheck;
import com.fib.core.base.validate.BaseValidator;
import com.fib.core.util.SpringContextUtils;

/**
 * 业务校验拦截器
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-15 11:26:00
 */
@Component
public class BizCheckInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(BizCheckInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		BizCheck bizCheck = getBizCheck(handlerMethod);
		if (null == bizCheck) {
			return true;
		}

		/* 业务校验 */
		return checkBiz(bizCheck, request, response);
	}

	private boolean checkBiz(BizCheck bizCheck, HttpServletRequest request, HttpServletResponse response) {
		String serviceName = bizCheck.serviceName();
		if (null == serviceName || serviceName.isEmpty()) {// 不用校验
			return true;
		}

		List<String> checkServices = getCheckServices(serviceName);
		if (null == checkServices || checkServices.isEmpty()) {// 不用校验
			return true;
		}

		String chckSvcName = "";
		for (Iterator<String> iter = checkServices.iterator(); iter.hasNext(); chckSvcName = iter.next()) {
			BaseValidator validator = SpringContextUtils.getBean(chckSvcName);
			if (!validator.check(request)) {
				logger.error("服务名[{}]->服务[{}]校验不通过", serviceName, chckSvcName);
				return false;
			}
		}
		return true;
	}

	private List<String> getCheckServices(String serviceName) {
		Map<String, List<String>> services = new HashMap<>();
		return services.get(serviceName);
	}

	private BizCheck getBizCheck(HandlerMethod handlerMethod) {
		if (handlerMethod.getBeanType().isAnnotationPresent(BizCheck.class)) {
			handlerMethod.getBeanType().getAnnotation(BizCheck.class);
		} else if (handlerMethod.getMethod().isAnnotationPresent(BizCheck.class)) {
			return handlerMethod.getMethod().getAnnotation(BizCheck.class);
		}
		return null;
	}
}