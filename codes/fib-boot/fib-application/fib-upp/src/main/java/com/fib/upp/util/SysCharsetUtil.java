package com.fib.upp.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public enum SysCharsetUtil {
	/***/
	CNAPS("CNAPS", StandardCharsets.UTF_8),

	/***/
	IBPS("IBPS", StandardCharsets.UTF_8),

	/***/
	CCBC("CCBC", Charset.forName("GBK")),

	/***/
	EPCC("EPCC", StandardCharsets.UTF_8);

	private String sysCode;
	private Charset charset;

	SysCharsetUtil(String sysCode, Charset charset) {
		this.sysCode = sysCode;
		this.charset = charset;
	}

	public String getSysCode() {
		return sysCode;
	}

	public Charset getCharset() {
		return charset;
	}
}
