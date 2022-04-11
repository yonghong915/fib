package com.fib.autoconfigure.aop.aspect;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.aop.EcaEvent;
import com.fib.autoconfigure.aop.aspect.annotation.EcaAnnotation;
import com.fib.commons.exception.BusinessException;
import com.fib.core.util.SpringContextUtils;

@Aspect
@Component
public class EcaAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(EcaAspect.class);

	@Pointcut("@annotation(com.fib.autoconfigure.aop.aspect.annotation.EcaAnnotation)")
	private void pointCut() {
		// nothing to do
	}

	// 前置通知
	@Before("pointCut()")
	public void doBefore(JoinPoint joinPoint) {
		LOGGER.info("doBefore：now is {}", System.currentTimeMillis());
		Object[] args = joinPoint.getArgs();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		EcaAnnotation ecaAnnotation = signature.getMethod().getAnnotation(EcaAnnotation.class);
		if (null == ecaAnnotation) {
			return;
		}

		String[] services = ecaAnnotation.service();
		for (String service : services) {
			String[] svrStrs = service.split(":", -1);

			String event = svrStrs[0];
			if (!EcaEvent.IN_VALIDATE.getValue().equals(event)) {
				continue;
			}

			String beanName = svrStrs[1];
			String methodName = svrStrs[2];

			Object obj = SpringContextUtils.getBean(beanName);
			try {
				Object rtnObj = obj.getClass().getMethod(methodName, Map.class).invoke(obj, args);
				LOGGER.info(String.valueOf(rtnObj));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				throw new BusinessException("aaaa", "bbbb");
			}
		}
		LOGGER.info("即将执行方法为: {}", args);
	}

	// 后置通知
	@After("pointCut()")
	public void doAfter(JoinPoint jp) {
		LOGGER.info("后置通知：最后且一定执行.....");
	}

	// 返回通知
	@AfterReturning(returning = "ret", pointcut = "pointCut()")
	public void doAfterReturning(Object ret) {
		// 处理完请求，返回内容
		LOGGER.info("返回通知：方法的返回值 : {}", ret);
	}

	// 异常通知
	@AfterThrowing(throwing = "ex", pointcut = "pointCut()")
	public void throwss(JoinPoint jp, Exception ex) {
		LOGGER.info("异常通知：方法异常时执行.....");
		LOGGER.info("产生异常的方法：{}", jp);
		LOGGER.info("异常种类：", ex);
	}
}