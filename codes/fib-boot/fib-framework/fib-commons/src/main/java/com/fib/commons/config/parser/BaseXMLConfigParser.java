package com.fib.commons.config.parser;

import org.dom4j.Document;

import com.fib.commons.config.Configuration;

public interface BaseXMLConfigParser extends ConfigurationParser {

	public Configuration parse(Document document, String configName);
}
