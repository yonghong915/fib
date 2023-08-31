package com.fib.commons.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestHeader {
	private String systemCode;
	private String sign;
	private Long timeStamp;
	private String nonce;

	public RequestHeader(RequestHeaderBuilder builder) {
		this.systemCode = builder.systemCode;
		this.timeStamp = builder.timeStamp;
		this.nonce = builder.nonce;
	}

	public static RequestHeaderBuilder builder() {
		return new RequestHeaderBuilder();
	}

	public String getSystemCode() {
		return systemCode;
	}

	public String getSign() {
		return sign;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public String getNonce() {
		return nonce;
	}

	public static class RequestHeaderBuilder {
		private String systemCode;
		private String sign;
		private Long timeStamp;
		private String nonce;

		public RequestHeaderBuilder systemCode(String systemCode) {
			this.systemCode = systemCode;
			return this;
		}

		public RequestHeaderBuilder timeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
			return this;
		}

		public RequestHeaderBuilder nonce(String nonce) {
			this.nonce = nonce;
			return this;
		}

		public RequestHeader build() {
			return new RequestHeader(this);
		}
	}
}
