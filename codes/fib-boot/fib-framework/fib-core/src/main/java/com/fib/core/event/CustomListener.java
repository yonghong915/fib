package com.fib.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.fib.core.common.service.IAsyncService;
import com.fib.core.util.SpringContextUtils;

/**
 * 通用监听器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Configuration
public class CustomListener {
	private Logger logger = LoggerFactory.getLogger(CustomListener.class);

	@Async(value = "customAsyncExcecutor")
	@EventListener
	public void onApplicationEvent(CustomEvent event) {
		logger.info("Listener Event [{}],currentThread = [{}]", event, Thread.currentThread().getName());
		Object source = event.getSource();
		String serviceName = event.getServiceName();
		Object obj = SpringContextUtils.getBean(serviceName);
		if (obj instanceof IAsyncService) {
			IAsyncService service = (IAsyncService) obj;
			service.execute(source);
		} else {
			logger.warn("[{}] is not subclass of IAsyncService.", serviceName);
		}
	}
}