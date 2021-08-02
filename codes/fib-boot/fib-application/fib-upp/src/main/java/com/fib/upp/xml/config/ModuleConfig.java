package com.fib.upp.xml.config;

import java.util.Map;

import lombok.Data;

@Data
public class ModuleConfig {
	protected String className;
	protected Map<String, String> parameters;
	private String type;
}
