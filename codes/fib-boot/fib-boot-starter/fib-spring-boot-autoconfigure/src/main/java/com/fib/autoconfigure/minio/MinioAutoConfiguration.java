package com.fib.autoconfigure.minio;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.minio.MinioClient;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(MinioClient.class)
@ComponentScan("com.fib.autoconfigure.minio")
public class MinioAutoConfiguration {

	@Bean
	public MinioClient minioClient(MinioProperties properties) {
		MinioClient.Builder builder = MinioClient.builder();
		builder.endpoint(properties.getUrl());
		if (StringUtils.hasLength(properties.getName()) && StringUtils.hasLength(properties.getPassword())) {
			builder.credentials(properties.getName(), properties.getPassword());
		}
		return builder.build();
	}
}