package com.fib.upp.xml.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GatewayConfig {
	private String id;
	private String name;
	private Integer monitorPort;
	private Integer eventHandlerNumber = 5;
	private List<ModuleConfig> modules = new ArrayList<>();
	private String variableFileName;

	private Map<String, ChannelMainConfig> channels = new HashMap<>();

	public void setChannel(String id, ChannelMainConfig channelConfig) {
		channels.put(id, channelConfig);
	}
}
