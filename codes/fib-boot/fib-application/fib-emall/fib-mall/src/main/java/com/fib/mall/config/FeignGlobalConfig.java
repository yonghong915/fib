package com.fib.mall.config;

import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * 此类如果不是全局配置文件共享不要加Configuration注解
 */
public class FeignGlobalConfig {
	// 注入自定义编码器
	@Bean
	Encoder feignEncoder(ObjectMapper objectMapper) {
		return new FeignCustomEncoder(objectMapper);
	}

	// 注入自定义解码器
	@Bean
	Decoder feignDecoder(ObjectMapper objectMapper) {
		return new FeignCustomDecoder(objectMapper);
	}
}