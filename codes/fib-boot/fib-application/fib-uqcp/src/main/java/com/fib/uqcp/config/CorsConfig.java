package com.fib.uqcp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域
 * 
 * @author fangyh
 * @version 1.0
 * @date 2023-11-07 15:54:16
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowCredentials(true).allowedOriginPatterns("*").allowedMethods(new String[] { "GET", "POST", "PUT", "DELETE" })
				.allowedHeaders("*").exposedHeaders("*");
	}
}
