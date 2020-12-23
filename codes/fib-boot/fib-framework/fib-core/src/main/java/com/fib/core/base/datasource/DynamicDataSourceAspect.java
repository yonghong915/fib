package com.fib.core.base.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fib.core.util.ConstantUtil;

@Aspect
@Component
@Order(-1) // 保证在@Transactional之前执行
public class DynamicDataSourceAspect {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("@annotation(DS)")
	private void pointcut() {
		//
	}

	@Before("pointcut()")
	public void beforeSwitchDS(JoinPoint point) {
		// 获得当前访问的class
		Class<?> className = point.getTarget().getClass();
		// 获得访问的方法名
		String methodName = point.getSignature().getName();
		// 得到方法的参数的类型
		Class<?>[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
		String dsType = ConstantUtil.DSType.DS_TYPE_SYSDB;
		try {
			// 得到访问的方法对象
			Method method = className.getMethod(methodName, argClass);
			// 判断是否存在@DS注解
			if (method.isAnnotationPresent(DS.class)) {
				DS annotation = method.getAnnotation(DS.class);
				// 取出注解中的数据源名
				dsType = annotation.value();
			}
		} catch (Exception e) {
			logger.error("Failed to switch datasource", e);
		}
		// 切换数据源
		DataSourceContextHolder.setDataSourceType(dsType);
	}

	@After("pointcut()")
	public void afterSwitchDS(JoinPoint point) {
		DataSourceContextHolder.clearDataSource();
	}
}
