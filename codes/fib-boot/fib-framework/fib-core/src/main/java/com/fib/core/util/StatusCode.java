package com.fib.core.util;

import com.fib.core.web.RestStatus;
import com.google.common.collect.ImmutableMap;

/**
 * Response status Enum Class 状态码
 *
 * @author fangyh
 * @version 1.0.0
 * @date 2020-12-14
 */
public enum StatusCode implements RestStatus {
	/**
	 * 操作成功
	 */
	SUCCESS("000000", "SUCCESS"),

	/* 参数错误:100001-199999 */
	/* 用户错误:200001-299999 */
	/* 业务错误:300001-399999 */
	/* 权限错误:400001-499999 */

	/**
	 * 操作失败
	 */
	FAIL("E999999", "FAIL"),

	/**
	 * 加密失败
	 */
	GENKEY_FAIL("E990000", "failed to gen key"),

	/**
	 * 加密失败
	 */
	ENCRYPT_FAIL("E990001", "failed to encrypt"),

	/**
	 * 解密失败
	 */
	DECRYPT_FAIL("E990002", "failed to decrypt"),

	/***/
	KEY_LENGTH_INSUFFICIENT("E990003", "key length insufficient"),

	/***/
	PACK_XML_EXCEPTION("E990004", "组装XML异常"),

	/***/
	UNPACK_XML_EXCEPTION("E990005", "解包XML异常"),

	/**
	 * 加载文件异常
	 */
	ESB_REQ_EXCEPTION("E990006", "ESB异常"),

	/**
	 * 空指针异常
	 */
	NULLPOINTER("E000001", "发生空指针异常"),

	/**
	 * 请求参数类型不匹配
	 */
	ILLEGAL_ARGUMENT("E000002", "请求参数类型不匹配"),

	/**
	 * 数据库访问异常
	 */
	DB_EXCEPTION("E000003", "数据库访问异常"),

	/**
	 * 加载文件异常
	 */
	LOAD_FILE_EXCEPTION("E000004", "加载文件异常"),

	/**
	 * 参数校验异常
	 */
	PARAMS_CHECK_EXCEPTION("E000005", "参数校验异常"),

	/**
	 * 参数[{}]为空
	 */
	PARAMS_CHECK_NULL("E000006", "参数为空"),

	/**
	 * 参数{}重复
	 */
	PARAMS_DUPLICATE("E000007", "参数{}重复"),

	/***/
	INVALID_TOKEN("2001", "访问令牌不合法"),

	/***/
	ACCESS_DENIED("2003", "没有权限访问该资源"),

	/***/
	USERNAME_OR_PASSWORD_ERROR("1002", "用户名或密码错误"),

	/***/
	UNSUPPORTED_GRANT_TYPE("1003", "不支持的认证模式"),

	/** 其他异常 */
	RTN_NULL("E000011", "{}"),

	/** 其他异常 */
	OTHER_EXCEPTION("E000009", "其他异常"),
	
	/** 账户状态不符 */
	ACCT_STAT_INCOMP("E000019", "账户状态不符"),
	
	/** 账户余额不足 */
	ACCT_BAL_INSUFFICIENT ("E000029", "账户余额不足"),
	;

	private final String code;

	private final String message;

	StatusCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}

	private static final ImmutableMap<String, StatusCode> CACHE;

	static {
		final ImmutableMap.Builder<String, StatusCode> builder = ImmutableMap.builder();
		for (StatusCode statusCode : values()) {
			builder.put(statusCode.code(), statusCode);
		}
		CACHE = builder.build();
	}

	public static StatusCode valueOfCode(String code) {
		final StatusCode status = CACHE.get(code);
		if (status == null) {
			throw new IllegalArgumentException("No matching constant for [" + code + "]");
		}
		return status;
	}
}
