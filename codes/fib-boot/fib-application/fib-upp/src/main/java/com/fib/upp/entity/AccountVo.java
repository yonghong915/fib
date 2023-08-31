package com.fib.upp.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fib.upp.MoneySerializer;

public class AccountVo {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long accoutId;

	@JsonSerialize(using = MoneySerializer.class)
	private Long balance;
}
