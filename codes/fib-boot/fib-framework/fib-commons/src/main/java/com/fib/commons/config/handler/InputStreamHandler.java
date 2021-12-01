package com.fib.commons.config.handler;

import java.io.InputStream;

import org.dom4j.Document;

import com.fib.commons.config.Configuration;
import com.fib.commons.config.parser.ConfigurationParser;
import com.fib.commons.config.parser.ConfigurationParserFactory;
import com.fib.commons.exception.CommonException;
import com.fib.commons.xml.Dom4jUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;

public class InputStreamHandler implements BaseXMLHandler {
	private boolean validate;
	private String fileName;
	private String configName;

	public InputStreamHandler(String fileName, String configName) {
		validate = false;
		this.fileName = fileName;
		this.configName = configName;
	}

	public void setValidation(boolean validate) {
		this.validate = validate;
	}

	@Override
	public Configuration load() {
		if (CharSequenceUtil.isEmpty(configName)) {
			throw new CommonException("There is not exists configName.");
		}
		ConfigurationParser parser = ConfigurationParserFactory.getParser(configName);
		return load(configName, parser);
	}

	@Override
	public Configuration load(String configName, ConfigurationParser parser) {
		if (CharSequenceUtil.isEmpty(fileName)) {
			throw new CommonException("There is not exists fileName.");
		}
		InputStream is = FileUtil.getInputStream(fileName);

		if (null == is) {
			throw new CommonException("The file could not be found in the classpath");
		}

		Document doc = Dom4jUtils.createDocument(is);

		// 校验
		if (validate && !checkValidate()) {
			throw new CommonException("Failed to validate file.");
		}
		return parser.parse(doc, configName);
	}
}
