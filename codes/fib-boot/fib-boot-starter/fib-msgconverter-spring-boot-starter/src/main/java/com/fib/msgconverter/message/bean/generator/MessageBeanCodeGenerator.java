package com.fib.msgconverter.message.bean.generator;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.msgconverter.message.metadata.Message;

import cn.hutool.core.text.CharSequenceUtil;

public class MessageBeanCodeGenerator {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageBeanCodeGenerator.class);
	private static final String NEW_LINE = System.getProperty("line.separator");
	private String outputDir;

	public void setOutputDir(String srcRootPath) {
		outputDir = srcRootPath;
		if (!outputDir.endsWith("/")) {
			outputDir += "/";
		}
	}

	public void generate(Message message, String encoding) {
		String pkgName = message.getClassName().substring(0, message.getClassName().lastIndexOf("."));
		String className = message.getClassName().substring(message.getClassName().lastIndexOf(".") + 1);
		StringBuilder stringbuffer = new StringBuilder(10240);
		HashMap hashmap = new HashMap();
		stringbuffer.append("package ");
		stringbuffer.append(pkgName.toLowerCase());
		stringbuffer.append(";");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import com.fib.msgconverter.message.bean.*;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import java.math.BigDecimal;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import java.io.UnsupportedEncodingException;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import java.util.*;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("/**");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(" * ");
		stringbuffer.append(message.getShortText());
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(" */");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("public class ");
		stringbuffer.append(CharSequenceUtil.upperFirst(className));
		stringbuffer.append(" extends MessageBean {");

		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);

		stringbuffer.append("\tpublic void validate() {");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t");
		stringbuffer.append("}");
		//TODO
		stringbuffer.append("}");

		LOGGER.info("source={}", stringbuffer);
	}

}
