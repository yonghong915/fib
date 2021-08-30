package com.fib.msgconverter.message.bean.generator;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.FileUtil;
import com.giantstone.common.util.StringUtil;
import com.giantstone.message.bean.MessageBean;
import com.giantstone.message.metadata.Constant;
import com.giantstone.message.metadata.Field;
import com.giantstone.message.metadata.Message;

public class MessageBeanCodeGenerator {

	private static final String NEW_LINE = System.getProperty("line.separator");
	private String outputDir;

	public MessageBeanCodeGenerator() {
	}

	public void generate(Message message, String s) {
		if (null == message)
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageMeta" }));
		String s1 = message.getClassName().substring(0, message.getClassName().lastIndexOf("."));
		String s2 = message.getClassName().substring(message.getClassName().lastIndexOf(".") + 1);
		StringBuffer stringbuffer = new StringBuffer(10240);
		HashMap hashmap = new HashMap();
		stringbuffer.append("package ");
		stringbuffer.append(s1.toLowerCase());
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

		stringbuffer.append("import com.giantstone.common.util.*;");
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
		stringbuffer.append(StringUtil.toUpperCaseFirstOne(s2));
		stringbuffer.append(" extends MessageBean{");

		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\tpublic void validate(){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t");
		stringbuffer.append("String name = CodeUtil.Bytes2FormattedText(this.getClass().getName().getBytes());");
		
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t");
		stringbuffer.append("System.out.println(\"className=\"+this.getClass().getName()+\"  name=\" + name);");
	
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("}");
		stringbuffer.append(NEW_LINE);
		String s7 = stringbuffer.toString();
		String s8 = (new StringBuilder()).append(outputDir).append(message.getClassName().replaceAll("\\.", "/"))
				.append(".java").toString();
		try {
			FileUtil.saveAsData(s8, s7.getBytes(s), false);
		} catch (UnsupportedEncodingException unsupportedencodingexception) {
			unsupportedencodingexception.printStackTrace();
		}
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String s) {
		outputDir = s;
//		if (outputDir.endsWith("/")) goto _L2; else goto _L1
//_L1:
//		new StringBuilder();
//		this;
//		JVM INSTR dup_x1 ;
//		outputDir;
//		append();
//		"/";
//		append();
//		toString();
//		outputDir;
//_L2:
	}

	private String getType(Field field, Message message) {
		Object obj = null;
		if (0 != field.getDataType()) {
			String s = Constant.getJavaTypeByDataType(field.getDataType());
			if ("int".equals(s))
				return (new StringBuilder()).append("Integer.valueOf(this.get")
						.append(StringUtil.toUpperCaseFirstOne(field.getName())).append("())").toString();
			if ("long".equals(s))
				return (new StringBuilder()).append("Long.valueOf(this.get")
						.append(StringUtil.toUpperCaseFirstOne(field.getName())).append("())").toString();
			if ("byte".equals(s))
				return (new StringBuilder()).append("Byte.valueOf(this.get")
						.append(StringUtil.toUpperCaseFirstOne(field.getName())).append("())").toString();
			if ("short".equals(s))
				return (new StringBuilder()).append("Short.valueOf(this.get")
						.append(StringUtil.toUpperCaseFirstOne(field.getName())).append("())").toString();
			else
				return (new StringBuilder()).append("this.get").append(StringUtil.toUpperCaseFirstOne(field.getName()))
						.append("()").toString();
		} else {
			return (new StringBuilder()).append("this.get").append(StringUtil.toUpperCaseFirstOne(field.getName()))
					.append("()").toString();
		}
	}

	private String getSetAttType(Field field, Message message) {
		Object obj = null;
		String s3 = null;
		if (2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType()
				|| 2011 == field.getFieldType() || 2008 == field.getFieldType() || 2009 == field.getFieldType()) {
			String s;
			if (field.getReference() != null)
				s = field.getReference().getClassName();
			else if ("dynamic".equalsIgnoreCase(field.getReferenceType())
					|| "expression".equalsIgnoreCase(field.getReferenceType())) {
				s = MessageBean.class.getName();
			} else {
				s = field.getCombineOrTableFieldClassName();
				if (null == s)
					s = (new StringBuilder()).append(message.getClassName())
							.append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
			}
			s3 = (new StringBuilder()).append("(").append(s).append(")value").toString();
		} else {
			String s1 = Constant.getJavaTypeByDataType(field.getDataType());
			if ("int".equals(s1))
				s3 = "((Integer)value).intValue()";
			else if ("long".equals(s1))
				s3 = "((Long)value).longValue()";
			else if ("byte".equals(s1))
				s3 = "((Byte)value).byteValue()";
			else if ("short".equals(s1))
				s3 = "((Short)value).shortValue()";
			else
				s3 = (new StringBuilder()).append("(").append(s1).append(")value").toString();
		}
		if (2004 == field.getFieldType() || 2011 == field.getFieldType()) {
			String s2 = "List";
			s3 = (new StringBuilder()).append("(").append(s2).append(")value").toString();
		}
		return s3;
	}
}
