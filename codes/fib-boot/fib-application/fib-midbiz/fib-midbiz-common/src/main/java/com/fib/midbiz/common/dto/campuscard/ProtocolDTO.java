package com.fib.midbiz.common.dto.campuscard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProtocolDTO {

	@NotBlank(message = "用户名不能为空")
	@Size(min = 2, max = 30, message = "用户名长度必须在 2-20 位之间")
	private String payerAcctNo;

	private String payerAcctNm;

	private String payeeAcctNo;

	private String payeeAcctNm;
}
