package com.fib.upp.service.gateway.message.metadata;

import lombok.Data;

@Data
public class Variable {
	private String id;
	private String name;
	private int dataType;
	private String value;
}
