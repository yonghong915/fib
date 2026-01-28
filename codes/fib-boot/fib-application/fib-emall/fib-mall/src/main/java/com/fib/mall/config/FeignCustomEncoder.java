package com.fib.mall.config;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
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

	@Autowired
	private NacosConfig nacosConfig;

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

		nacosConfig.getAppId();
		String appId = "app1001";
		String aesKey = "1234567890123456";
		String signKey = "signKey1234567890";
		SecurityUtils securityUtils = new SecurityUtils();
		ObjectMapper objectMapper = new ObjectMapper();
		String businessContent = null;

		EncryptRequest encryptRequest = new EncryptRequest();
		try {
			businessContent = objectMapper.writeValueAsString(wrapper);
			String cipherText = securityUtils.aesEncrypt(businessContent, aesKey);

			// 5. 生成安全参数
			long timestamp = System.currentTimeMillis();
			String nonce = securityUtils.generateNonce();

			// 6. 生成签名
			String signContent = appId + timestamp + nonce + cipherText;
			String sign = securityUtils.hmacSign(signContent, signKey);

			// 7. 封装加密请求

			encryptRequest.setAppId(appId);
			encryptRequest.setTimestamp(timestamp);
			encryptRequest.setNonce(nonce);
			encryptRequest.setSign(sign);
			encryptRequest.setCipherText(cipherText);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 2. 用默认编码器序列化封装后的对象
//		delegate.encode(wrapper, RequestWrapper.class, template);
		delegate.encode(encryptRequest, EncryptRequest.class, template);
	}
}
