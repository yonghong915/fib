package com.fib.uias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-16
 */
@SpringBootApplication
//@EnableCaching
@EnableRetry
@EnableHystrix
@EnableHystrixDashboard
@ComponentScan({ "com.fib.core", "com.fib.uias" })
public class UiasApplication {
	public static void main(String[] args) {
		SpringApplication.run(UiasApplication.class, args);
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

//	@Bean(name = "hystrixRegistrationBean")
//	ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean() {
//		ServletRegistrationBean<HystrixMetricsStreamServlet> registration = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet(),
//				"/actuator/hystrix.stream");
//		registration.setName("HystrixMetricsStreamServlet");
//		registration.setLoadOnStartup(1);
//		return registration;
//	}

}
