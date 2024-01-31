package com.fib.autoconfigure.crypto.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.util.PrefixUtil;

import cn.hutool.core.util.CharsetUtil;

@Configuration
@ConfigurationProperties(prefix = PrefixUtil.CRYPTO_PREFIX)
@ConditionalOnProperty(prefix = PrefixUtil.CRYPTO_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class CryptoProperties {
	/** 是否可用 */
	private boolean enable;

	/** 编码 */
	private String charset = CharsetUtil.UTF_8;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
}