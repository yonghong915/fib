package com.fib.commons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class InternationalConfig {
	@Value(value = "${spring.messages.basename:message}")
	private String basename;

	@Bean(name = "messageSource")
	public ResourceBundleMessageSource getMessageResource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		String[] basenames = basename.split(",");
		for (String name : basenames) {
			messageSource.addBasenames(name);
		}
		return messageSource;
	}
}