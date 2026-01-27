package com.fib.mall.config;

import java.lang.reflect.Type;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hutool.core.util.IdUtil;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.jackson.JacksonEncoder;

/**
 * 自定义 Feign 编码器：序列化前封装请求报文
 */
@Component
public class FeignCustomEncoder implements Encoder {

	// 复用Feign 默认的 Jackson 编码器
	private final JacksonEncoder delegate;

	public FeignCustomEncoder(ObjectMapper objectMapper) {
		this.delegate = new JacksonEncoder(objectMapper);
	}

	@Override
	public void encode(Object object, Type bodyType, RequestTemplate template) {
		// 1. 封装业务对象为公共请求体
		RequestWrapper<Object> wrapper = new RequestWrapper<>();
		wrapper.setRequestId(IdUtil.getSnowflakeNextIdStr());
		wrapper.setTimestamp(System.currentTimeMillis());
		wrapper.setData(object);

		// 2. 用默认编码器序列化封装后的对象
		delegate.encode(wrapper, RequestWrapper.class, template);
	}
}
