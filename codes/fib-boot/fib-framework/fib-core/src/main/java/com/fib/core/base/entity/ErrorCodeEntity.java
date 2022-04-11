package com.fib.core.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("SYS_ERROR_CODE")
public class ErrorCodeEntity extends BaseEntity {

	private static final long serialVersionUID = 5565692809127703716L;
	/***/
	private String language;
	/***/
	private String systemCode;
	/***/
	private String errorCode;
	/***/
	private String errorDesc;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
}
