package com.fib.commons.config.handler;

import com.fib.commons.config.Configuration;
import com.fib.commons.config.parser.ConfigurationParser;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-12-01
 */
public interface BaseXMLHandler extends ConfigurationHandler {
	/**
	 * 
	 * @param configName
	 * @param configurationparser
	 * @return
	 */
	public Configuration load(String configName, ConfigurationParser configurationparser);
}
