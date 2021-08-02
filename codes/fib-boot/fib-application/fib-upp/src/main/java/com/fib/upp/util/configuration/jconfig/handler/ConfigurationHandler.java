package com.fib.upp.util.configuration.jconfig.handler;

import com.fib.upp.util.configuration.jconfig.Configuration;
import com.fib.upp.util.configuration.jconfig.parser.ConfigurationParser;

public interface ConfigurationHandler {
	public abstract Configuration load(String s);

	public abstract Configuration load(String s, ConfigurationParser configurationparser);

	public abstract void store(Configuration configuration);
}
