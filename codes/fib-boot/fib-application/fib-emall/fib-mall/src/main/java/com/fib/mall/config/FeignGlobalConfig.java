package com.fib.mall.config;

import org.springframework.context.annotation.Bean;

import feign.codec.Decoder;
import feign.codec.Encoder;

/**
 * 此类如果不是全局配置文件共享不要加Configuration注解
 */
public class FeignGlobalConfig {

	private FeignCustomEncoder feignCustomEncoder;

	private FeignCustomDecoder feignCustomDecoder;

	public FeignGlobalConfig(FeignCustomEncoder feignCustomEncoder, FeignCustomDecoder feignCustomDecoder) {
		this.feignCustomEncoder = feignCustomEncoder;
		this.feignCustomDecoder = feignCustomDecoder;
	}

	// 注入自定义编码器
	@Bean
	Encoder feignEncoder() {
		return feignCustomEncoder;
	}

	// 注入自定义解码器
	@Bean
	Decoder feignDecoder() {
		return feignCustomDecoder;
	}
}