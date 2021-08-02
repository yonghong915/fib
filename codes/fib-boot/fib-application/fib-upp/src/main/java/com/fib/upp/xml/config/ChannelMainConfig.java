package com.fib.upp.xml.config;

import lombok.Data;

@Data
public class ChannelMainConfig {
	private String id;
	private String name;
	private boolean startup;
	private String deploy;
}
