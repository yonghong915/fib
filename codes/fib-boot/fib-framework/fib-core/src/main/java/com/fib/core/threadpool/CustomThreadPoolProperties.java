package com.fib.core.threadpool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 通用线程池属性
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-06-04
 */
@Component
@PropertySource("classpath:config/common/custom_threadpool.properties")
@ConfigurationProperties(prefix = "custom.threadpool")
public class CustomThreadPoolProperties extends AbstractExecutorPool {

}