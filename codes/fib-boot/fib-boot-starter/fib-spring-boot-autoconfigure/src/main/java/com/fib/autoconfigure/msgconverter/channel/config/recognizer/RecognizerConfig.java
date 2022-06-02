package com.fib.autoconfigure.msgconverter.channel.config.recognizer;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fib.autoconfigure.msgconverter.channel.message.recognizer.MessageRecognizer;
import com.fib.autoconfigure.msgconverter.gateway.config.base.TypedDynamicObjectConfig;

/**
 * 识别器配置
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-23 16:43:46
 */
public class RecognizerConfig extends TypedDynamicObjectConfig {
	private String id;

	/**
	 * 内嵌识别器组件配置的列表
	 */
	private List<RecognizerConfig> componentList = null;

	/**
	 * 默认值
	 */
	private String defaultValue = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<RecognizerConfig> getComponentList() {
		return componentList;
	}

	public void setComponentList(List<RecognizerConfig> componentList) {
		this.componentList = componentList;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	protected void setObjInterface() {
		this.objInterface = MessageRecognizer.class;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
