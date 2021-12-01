package com.fib.commons.config.parser;

import org.dom4j.Document;

import com.fib.commons.config.Configuration;

/**
 * 配置解析器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-12-01
 */
public interface ConfigurationParser {
	/**
	 * 文档解析
	 * 
	 * @param document
	 * @param configName
	 * @return
	 */
	public Configuration parse(Document document, String configName);
}