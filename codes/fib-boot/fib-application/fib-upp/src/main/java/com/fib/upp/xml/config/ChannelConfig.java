package com.fib.upp.xml.config;

import lombok.Data;

@Data
public class ChannelConfig {
	private ChannelMainConfig mainConfig;
	private int mode;
	private String description;
}
