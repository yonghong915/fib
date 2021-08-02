package com.fib.upp.service.gateway.message.bean.generator;

import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.dubbo.common.compiler.Compiler;
import org.apache.dubbo.common.compiler.support.JdkCompiler;
import org.apache.dubbo.common.extension.ExtensionLoader;

import com.fib.upp.service.gateway.constant.EnumConstants;
import com.fib.upp.service.gateway.message.metadata.Field;
import com.fib.upp.service.gateway.message.metadata.MessageMetaData;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-07-02
 */
public class MessageBeanCodeGenerator {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private String outputDir;

	public void setOutputDir(String srcRootPath) {
		this.outputDir = srcRootPath;
		if (!this.outputDir.endsWith("/")) {
			this.outputDir += "/";
		}
	}

	public void generate(MessageMetaData message, String defaultEncoding) {
		Assert.notNull(message);
		String classPath = message.getClassName();
		if (StrUtil.isEmpty(classPath)) {
			return;
		}

		String className = message.getClassName().substring(message.getClassName().lastIndexOf(".") + 1);
		StringBuilder stringbuffer = new StringBuilder(10240);

		buildHeader(message, stringbuffer);

		stringbuffer.append("public class ");
		stringbuffer.append(StrUtil.upperFirst(className));
		stringbuffer.append(" extends MessageBean").append(StrUtil.DELIM_START);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);

		buildBody(message, stringbuffer);

		stringbuffer.append(StrUtil.DELIM_END);
		System.out.println(stringbuffer.toString());

		String s8 = stringbuffer.toString();
		String s10 = (new StringBuilder()).append(outputDir).append(message.getClassName().replaceAll("\\.", "/"))
				.append(".java").toString();
		FileUtil.writeString(s8, s10, defaultEncoding);

		ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
		Compiler compiler = new JdkCompiler();
		compiler.compile(s8, Thread.currentThread().getContextClassLoader());
	}

	private void buildBody(MessageMetaData message, StringBuilder stringbuffer) {

		Iterator<Field> iterator = message.getFields().values().iterator();
		while (iterator.hasNext()) {
			Field field = iterator.next();

			stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
					.append("/**");
			stringbuffer.append(field.getShortText());
			stringbuffer.append("*/");
			stringbuffer.append(NEW_LINE);

			stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
					.append("private ");
			stringbuffer.append(getJavaTypeByDataType(field.getDataType())).append(StrUtil.C_SPACE)
					.append(StrUtil.lowerFirst(field.getName()));
			stringbuffer.append(";");
			stringbuffer.append(NEW_LINE);

			stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
					.append("public ");
			stringbuffer.append(getJavaTypeByDataType(field.getDataType()));
			stringbuffer.append(StrUtil.C_SPACE);
			stringbuffer.append("get");
			stringbuffer.append(StrUtil.upperFirst(field.getName()));
			stringbuffer.append("()").append(StrUtil.DELIM_START);
			stringbuffer.append(NEW_LINE);
			stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
					.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
					.append("return ");
			stringbuffer.append(StrUtil.lowerFirst(field.getName()));
			stringbuffer.append(";");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
					.append(StrUtil.DELIM_END);
			stringbuffer.append(NEW_LINE);
			stringbuffer.append(NEW_LINE);

			if (field.isEditable()) {
				stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
						.append(StrUtil.C_SPACE).append("public void set");
				stringbuffer.append(StrUtil.upperFirst(field.getName()));
				stringbuffer.append("(");

				stringbuffer.append(getJavaTypeByDataType(field.getDataType()));
				stringbuffer.append(StrUtil.C_SPACE);
				stringbuffer.append(StrUtil.lowerFirst(field.getName()));

				stringbuffer.append(")").append(StrUtil.DELIM_START);
				stringbuffer.append(NEW_LINE);
				stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
						.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
						.append(StrUtil.C_SPACE).append("this.");
				stringbuffer.append(StrUtil.lowerFirst(field.getName()));
				stringbuffer.append(" = ");
				stringbuffer.append(StrUtil.lowerFirst(field.getName()));
				stringbuffer.append(";");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append(StrUtil.C_SPACE).append(StrUtil.C_SPACE).append(StrUtil.C_SPACE)
						.append(StrUtil.C_SPACE).append(StrUtil.DELIM_END);
				stringbuffer.append(NEW_LINE);

			}
		}
	}

	private void buildHeader(MessageMetaData message, StringBuilder stringbuffer) {
		String pkgPath = message.getClassName().substring(0, message.getClassName().lastIndexOf("."));
		stringbuffer.append("package ");
		stringbuffer.append(pkgPath.toLowerCase());
		stringbuffer.append(";");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import com.fib.upp.service.gateway.message.bean.MessageBean;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import java.math.BigDecimal;");
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

	}

	public static void main(String[] args) {
		MessageMetaData message = new MessageMetaData();
		message.setClassName("com.fib.upp.FibMessageBean");
		message.setShortText("Fib MessageBean设计");

		Field idField = new Field();
		idField.setName("id");
		idField.setShortText("id field");
		idField.setDataType(EnumConstants.DataType.INT.getCode());
		idField.setFieldType(EnumConstants.FieldType.FIXED_FIELD.getCode());
		idField.setEditable(false);
		message.setField("id", idField);

		Field nameField = new Field();
		nameField.setName("name");
		nameField.setShortText("name field");
		nameField.setDataType(EnumConstants.DataType.STR.getCode());
		nameField.setFieldType(EnumConstants.FieldType.FIXED_FIELD.getCode());
		nameField.setEditable(true);
		message.setField("name", nameField);

		MessageBeanCodeGenerator gen = new MessageBeanCodeGenerator();
		gen.generate(message, "UTF-8");
	}

	public static String getJavaTypeByDataType(int var0) {
		switch (var0) {
		case 3000:
		case 3001:
		case 3006:
		case 3010:
		case 3011:
			return "String";
		case 3002:
			return "byte[]";
		case 3003:
		case 3007:
			return "Integer";
		case 3004:
			return "byte";
		case 3005:
		case 3008:
			return "Short";
		case 3009:
			return "Long";
		default:

		}
		return null;
	}
}