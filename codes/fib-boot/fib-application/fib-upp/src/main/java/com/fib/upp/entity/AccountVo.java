package com.fib.upp.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fib.upp.MoneySerializer;

import java.io.Serializable;

public class AccountVo implements Serializable {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long accountId;

	@JsonSerialize(using = MoneySerializer.class)
	private Long balance;
}
