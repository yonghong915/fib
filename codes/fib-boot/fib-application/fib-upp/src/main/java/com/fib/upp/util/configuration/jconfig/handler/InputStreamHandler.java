package com.fib.upp.util.configuration.jconfig.handler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.Document;

import com.fib.commons.xml.Dom4jUtils;
import com.fib.upp.util.configuration.jconfig.Configuration;
import com.fib.upp.util.configuration.jconfig.parser.ConfigurationParser;
import com.fib.upp.util.configuration.jconfig.parser.ConfigurationParserFactory;

import cn.hutool.core.io.FileUtil;

public class InputStreamHandler extends AbstractHandler {
	private boolean validate;
	private String filename;

	public InputStreamHandler() {
		validate = false;
	}

	public InputStreamHandler(String filename) {
		validate = false;
		this.filename = filename;
	}

	public void setFileName(String fileName) {
		filename = fileName;
	}

	public void setValidation(boolean validate) {
		this.validate = validate;
	}

	public Configuration load(String configurationName) {
		ConfigurationParser parser = ConfigurationParserFactory.getParser(configurationName);
		return load(configurationName, parser);
	}

	@Override
	public Configuration load(String configurationName, ConfigurationParser parser) {
		try (InputStream is = new BufferedInputStream(new FileInputStream(FileUtil.file(filename)))) {
			Document doc = Dom4jUtils.getDocument(is);
			Configuration config = parser.parse(doc, configurationName);
			return config;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void store(Configuration configuration) {
		// TODO Auto-generated method stub

	}
}
