package com.fib.gateway.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 网关配置
 * 
 * @author 刘恭亮
 * 
 */
public class GatewayConfig {
	/**
	 * 网关id
	 */
	private String id;

	/**
	 * 网关名
	 */
	private String name;

	/**
	 * 监控端口号
	 */
	private int monitorPort;

	/**
	 * 监控端口号字符串(从配置文件中解析出的原始端口号)
	 */
	private String monitorPortString;

	/**
	 * 日志对象名
	 */
	private String loggerName;

	/**
	 * 事件处理线程数
	 */
	private int eventHandlerNumber = 5;

	/**
	 * 加载的模块
	 */
	private List<ModuleConfig> modules = new ArrayList<>();

	/**
	 * 安装的渠道
	 */
	private Map<String,ChannelMainConfig> channels = new HashMap<>();

	/**
	 * 变量文件名
	 */
	private String variableFileName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @param loggerName the loggerName to set
	 */
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	/**
	 * @return the eventHandlerNumber
	 */
	public int getEventHandlerNumber() {
		return eventHandlerNumber;
	}

	/**
	 * @param eventHandlerNumber the eventHandlerNumber to set
	 */
	public void setEventHandlerNumber(int eventHandlerNumber) {
		this.eventHandlerNumber = eventHandlerNumber;
	}

	/**
	 * @return the channels
	 */
	public Map<String,ChannelMainConfig> getChannels() {
		return channels;
	}

	public ChannelMainConfig getChannel(String id) {
		return (ChannelMainConfig) channels.get(id);
	}

	public void setChannel(String id, ChannelMainConfig channelConfig) {
		channels.put(id, channelConfig);
	}

	/**
	 * @param channels the channels to set
	 */
	public void setChannels(Map channels) {
		this.channels = channels;
	}

	/**
	 * @return the monitorPort
	 */
	public int getMonitorPort() {
		return monitorPort;
	}

	/**
	 * @param monitorPort the monitorPort to set
	 */
	public void setMonitorPort(int monitorPort) {
		this.monitorPort = monitorPort;
	}

	/**
	 * @return the modules
	 */
	public List<ModuleConfig> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<ModuleConfig> modules) {
		this.modules = modules;
	}

	public String getMonitorPortString() {
		return monitorPortString;
	}

	public void setMonitorPortString(String monitorPortString) {
		this.monitorPortString = monitorPortString;
	}

	public String getVariableFileName() {
		return variableFileName;
	}

	public void setVariableFileName(String variableFileName) {
		this.variableFileName = variableFileName;
	}

	/**
	 * add by wuhui 20090327 用于通过类型获得ModuleConfig
	 * 
	 * @param type
	 * @return
	 */
	public ModuleConfig getModulesByType(String type) {
		ModuleConfig config = null;
		for (int i = 0; i < modules.size(); i++) {
			config = (ModuleConfig) modules.get(i);
			if (type.equals(config.getType())) {
				return config;
			}
		}
		return null;
	}

}
