package com.fib.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring工具类
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Component
public class SpringContextUtils implements ApplicationContextAware, DisposableBean {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		if (null == applicationContext) {
			applicationContext = arg0;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 通过名称获取Bean
	 * 
	 * @param <T>
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 通过指定类型获取Bean
	 * 
	 * @param <T>
	 * @param clz
	 * @return
	 */
	public static <T> T getBean(Class<T> clz) {
		return (T) applicationContext.getBean(clz);
	}

	@Override
	public void destroy() throws Exception {
		applicationContext = null;
	}
}