package com.fib.midbiz.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocConfig {
	/**
	 * 自定义接口文档基本信息
	 */
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				// 文档标题、版本、描述
				.info(new Info().title("校园卡系统接口文档").version("1.0.0")
						.description("基于 Spring Boot 3.x + SpringDoc OpenAPI 3 构建的接口文档")
						// 联系人信息（可选）
						.contact(new Contact().name("开发团队").email("dev@example.com")));
	}
}
