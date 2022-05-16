package com.fib.autoconfigure.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.util.PrefixUtil;

@ConfigurationProperties(prefix = PrefixUtil.MINIO_PREFIX)
@Component
public class MinioProperties {
	private String url;
	private String urlProxy;
	private String name;
	private String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlProxy() {
		return urlProxy;
	}

	public void setUrlProxy(String urlProxy) {
		this.urlProxy = urlProxy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}