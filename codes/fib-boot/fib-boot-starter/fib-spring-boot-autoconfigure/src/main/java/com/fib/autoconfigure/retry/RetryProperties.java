package com.fib.autoconfigure.retry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.fib.autoconfigure.util.PrefixUtil;

@Configuration
@ConfigurationProperties(prefix = PrefixUtil.RETRY_PREFIX)
@ConditionalOnProperty(prefix = PrefixUtil.RETRY_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class RetryProperties {
	/** 是否可用 */
	private boolean enable;
	/** 重试次数 */
	private int retryCount;

	/** 超时时间 */
	private int timeout;

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}