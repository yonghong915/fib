package com.fib.upp.util.configuration.jconfig.parser;

import org.dom4j.Document;

import com.fib.upp.util.configuration.jconfig.Configuration;

public interface ConfigurationParser {

	Configuration parse(Document doc, String configurationName);

}
