package com.fib.gateway.message.bean.generator;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.util.FileUtil;
import com.fib.gateway.message.util.StringUtil;
import com.fib.gateway.message.xml.message.Constant;

import cn.hutool.core.util.StrUtil;

/**
 * MessageBean代码生成器
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public class MessageBeanCodeGenerator {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private String outputDir;
	private static String operBlank = "1";

	public MessageBeanCodeGenerator() {
		//
	}

	public void buildHeader(Message message, StringBuilder stringbuffer) {
		// pkgPath
		String pkgPath = message.getClassName().substring(0, message.getClassName().lastIndexOf("."));
		stringbuffer.append("package ");
		stringbuffer.append(pkgPath.toLowerCase());
		stringbuffer.append(";");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import com.fib.gateway.message.bean.*;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import com.fib.gateway.message.xml.message.*;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import com.fib.gateway.message.*;");
		stringbuffer.append(NEW_LINE);

		stringbuffer.append("import com.fib.gateway.message.util.*;");
		stringbuffer.append(NEW_LINE);

		stringbuffer.append("import java.math.BigDecimal;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import java.io.UnsupportedEncodingException;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;");
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

	private void buildAttrGetSet(Message message, StringBuilder stringbuffer) {
		Iterator<?> iterator = message.getFields().values().iterator();
		String s3 = null;
		do {
			if (!iterator.hasNext())
				break;
			Field field = (Field) iterator.next();
			if (2002 == field.getFieldType() || 2003 == field.getFieldType() || 2004 == field.getFieldType()
					|| 2011 == field.getFieldType() || 2008 == field.getFieldType() || 2009 == field.getFieldType()) {
				if (field.getReference() != null)
					s3 = field.getReference().getClassName();
				else if ("dynamic".equalsIgnoreCase(field.getReferenceType())
						|| "expression".equalsIgnoreCase(field.getReferenceType())) {
					s3 = MessageBean.class.getName();
				} else {
					s3 = field.getCombineOrTableFieldClassName();
					if (null == s3)
						s3 = (new StringBuilder()).append(message.getClassName())
								.append(StringUtil.toUpperCaseFirstOne(field.getName())).toString();
					Message message1 = new Message();
					message1.setId((new StringBuilder()).append(message.getId()).append("-").append(field.getName())
							.toString());
					message1.setClassName(s3);
					message1.setShortText(field.getShortText());
					message1.setFields(field.getSubFields());
					generate(message1, "UTF-8");
				}
			} else {
				s3 = Constant.getJavaTypeByDataType(field.getDataType());
			}
			stringbuffer.append("\t//");
			stringbuffer.append(field.getShortText());
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\tprivate ");
			if (2004 == field.getFieldType() || 2011 == field.getFieldType())
				stringbuffer.append("List");
			else
				stringbuffer.append(s3);
			stringbuffer.append(" ");
			stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
			if (2004 == field.getFieldType() || 2011 == field.getFieldType())
				stringbuffer.append(" = new ArrayList(20)");
			else if ((2002 == field.getFieldType() || 2003 == field.getFieldType() || 2008 == field.getFieldType()
					|| 2009 == field.getFieldType()) && !"dynamic".equalsIgnoreCase(field.getReferenceType())
					&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
				stringbuffer.append(" = new ");
				stringbuffer.append(s3);
				stringbuffer.append("()");
			} else if (field.getValue() != null)
				switch (field.getDataType()) {
				case 3000:
				case 3001:
				case 3006:
				case 3010:
				case 3011:
					stringbuffer.append(" = \"");
					stringbuffer.append(field.getValue());
					stringbuffer.append("\"");
					break;

				case 3002:
					stringbuffer.append(" = CodeUtil.HextoByte(\"");
					stringbuffer.append(field.getValue());
					stringbuffer.append("\")");
					break;

				case 3003:
				case 3007:
					stringbuffer.append(" = ");
					stringbuffer.append(field.getValue());
					break;

				case 3009:
					stringbuffer.append(" = ");
					stringbuffer.append(field.getValue());
					stringbuffer.append("l");
					break;

				case 3004:
				case 3005:
				case 3008:
				default:
					stringbuffer.append(" = (");
					stringbuffer.append(Constant.getJavaTypeByDataType(field.getDataType()));
					stringbuffer.append(")");
					stringbuffer.append(field.getValue());
					break;
				}
			stringbuffer.append(";");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\tpublic ");
			if (2004 == field.getFieldType() || 2011 == field.getFieldType())
				stringbuffer.append("List");
			else
				stringbuffer.append(s3);
			stringbuffer.append(" get");
			stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
			stringbuffer.append("(){");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\t\treturn ");
			stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
			stringbuffer.append(";");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\t}");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append(NEW_LINE);
			if ((2004 == field.getFieldType() || 2011 == field.getFieldType())
					&& !"dynamic".equalsIgnoreCase(field.getReferenceType())
					&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
				stringbuffer.append("\tpublic ");
				stringbuffer.append(s3);
				stringbuffer.append(" get");
				stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
				stringbuffer.append("At(int index){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\treturn (");
				stringbuffer.append(s3);
				stringbuffer.append(")");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
				stringbuffer.append(".get(index);");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t}");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\tpublic int get");
				stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
				stringbuffer.append("RowNum(){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\treturn ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
				stringbuffer.append(".size();");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t}");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append(NEW_LINE);
			}
			if (field.isEditable()) {
				stringbuffer.append("\tpublic void set");
				stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
				stringbuffer.append("(");
				if (2004 == field.getFieldType() || 2011 == field.getFieldType())
					stringbuffer.append("List");
				else
					stringbuffer.append(s3);
				stringbuffer.append(" ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
				stringbuffer.append("){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\tthis.");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
				stringbuffer.append(" = ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
				stringbuffer.append(";");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t}");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append(NEW_LINE);
				if ((2004 == field.getFieldType() || 2011 == field.getFieldType())
						&& !"dynamic".equalsIgnoreCase(field.getReferenceType())
						&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
					stringbuffer.append("\tpublic ");
					stringbuffer.append(s3);
					stringbuffer.append(" create");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
					stringbuffer.append("(){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer.append(s3);
					stringbuffer.append(" newRecord = new ");
					stringbuffer.append(s3);
					stringbuffer.append("();");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
					stringbuffer.append(".add(newRecord);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\treturn newRecord;");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\tpublic void add");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
					stringbuffer.append("(");
					stringbuffer.append(s3);
					stringbuffer.append(" newRecord){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
					stringbuffer.append(".add(newRecord);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\tpublic void set");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
					stringbuffer.append("At(int index, ");
					stringbuffer.append(s3);
					stringbuffer.append(" newRecord){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
					stringbuffer.append(".set(index, newRecord);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\tpublic ");
					stringbuffer.append(s3);
					stringbuffer.append(" remove");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
					stringbuffer.append("At(int index){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\treturn (");
					stringbuffer.append(s3);
					stringbuffer.append(")");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
					stringbuffer.append(".remove(index);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\tpublic void clear");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field.getName()));
					stringbuffer.append("(){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field.getName()));
					stringbuffer.append(".clear();");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
				}
			}
		} while (true);
	}

	private void buildGetAttribute(Message message, StringBuilder stringbuffer) {
		stringbuffer.append("\tpublic Object getAttribute(String name){");
		stringbuffer.append(NEW_LINE);
		Iterator<?> iterator = message.getFields().values().iterator();
		if (iterator.hasNext()) {
			Field field1 = (Field) iterator.next();
			stringbuffer.append("\t\tif(");
			stringbuffer.append("\"");
			stringbuffer.append(field1.getName());
			stringbuffer.append("\"");
			stringbuffer.append(".equals(name)){");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\t\t\treturn  ");
			stringbuffer.append(getType(field1, message));
			stringbuffer.append(";");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\t\t}");
		}
		for (; iterator.hasNext(); stringbuffer.append("\t\t}")) {
			Field field2 = (Field) iterator.next();
			stringbuffer.append("else if(");
			stringbuffer.append("\"");
			stringbuffer.append(field2.getName());
			stringbuffer.append("\"");
			stringbuffer.append(".equals(name)){");
			stringbuffer.append(NEW_LINE);
			stringbuffer.append("\t\t\treturn  ");
			stringbuffer.append(getType(field2, message));
			stringbuffer.append(";");
			stringbuffer.append(NEW_LINE);
		}

		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\treturn null;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
	}

	private void buildSetAttribute(Message message, StringBuilder stringbuffer) {
		stringbuffer.append("\tpublic void setAttribute(String name,Object value){");
		stringbuffer.append(NEW_LINE);
		Iterator<?> iterator = message.getFields().values().iterator();
		boolean flag = true;
		do {
			if (!iterator.hasNext())
				break;
			Field field3 = (Field) iterator.next();
			if (field3.isEditable())
				if (flag) {
					stringbuffer.append("\t\tif(");
					stringbuffer.append("\"");
					stringbuffer.append(field3.getName());
					stringbuffer.append("\"");
					stringbuffer.append(".equals(name)){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tthis.set");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field3.getName()));
					stringbuffer.append("(");
					stringbuffer.append(getSetAttType(field3, message));
					stringbuffer.append(")");
					stringbuffer.append(";");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
					flag = false;
				} else {
					stringbuffer.append("else if(");
					stringbuffer.append("\"");
					stringbuffer.append(field3.getName());
					stringbuffer.append("\"");
					stringbuffer.append(".equals(name)){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tthis.set");
					stringbuffer.append(StringUtil.toUpperCaseFirstOne(field3.getName()));
					stringbuffer.append("(");
					stringbuffer.append(getSetAttType(field3, message));
					stringbuffer.append(")");
					stringbuffer.append(";");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
				}
		} while (true);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
	}

	private void buildCover(Message message, StringBuilder stringbuffer) {
		String className = message.getClassName().substring(message.getClassName().lastIndexOf(".") + 1);
		stringbuffer.append("\tpublic void cover(MessageBean bean){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t");
		stringbuffer.append(StringUtil.toUpperCaseFirstOne(className));
		stringbuffer.append(" newBean = ");
		stringbuffer.append("(");
		stringbuffer.append(StringUtil.toUpperCaseFirstOne(className));
		stringbuffer.append(") bean;");
		stringbuffer.append(NEW_LINE);
		Iterator<?> iterator = message.getFields().values().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Field field4 = (Field) iterator.next();
			String s7 = StringUtil.toLowerCaseFirstOne(field4.getName());
			if (2005 != field4.getFieldType() && 2006 != field4.getFieldType() && 2007 != field4.getFieldType()
					&& 2010 != field4.getFieldType())
				if (2000 == field4.getFieldType() || 2001 == field4.getFieldType()) {
					String s9 = Constant.getJavaTypeByDataType(field4.getDataType());
					if ("String".equals(s9)) {
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t");
						stringbuffer.append((new StringBuilder()).append("if( null != newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("() && 0 != newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("().length()){").toString());
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t");
						stringbuffer.append((new StringBuilder()).append(s7).append(" = newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("();").toString());
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t}");
					} else if ("int".equals(s9) || "byte".equals(s9) || "short".equals(s9) || "long".equals(s9)) {
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t");
						stringbuffer.append((new StringBuilder()).append("if( 0 != newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("()){").toString());
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t");
						stringbuffer.append((new StringBuilder()).append(s7).append(" = newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("();").toString());
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t}");
					} else if ("byte[]".equals(s9)) {
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t");
						stringbuffer.append((new StringBuilder()).append("if( null != newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("()&&0!=newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("().length){").toString());
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t");
						stringbuffer.append((new StringBuilder()).append(s7).append(" = newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("();").toString());
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t}");
					}
				} else if (2002 == field4.getFieldType() || 2003 == field4.getFieldType()
						|| 2008 == field4.getFieldType() || 2009 == field4.getFieldType()) {
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer.append(
							(new StringBuilder()).append("if( null != ").append(s7).append("&&null != newBean.get")
									.append(StringUtil.toUpperCaseFirstOne(s7)).append("()){").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t");
					stringbuffer.append((new StringBuilder()).append(s7).append(".cover(newBean.get")
							.append(StringUtil.toUpperCaseFirstOne(s7)).append("());").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
				} else if (2004 == field4.getFieldType() || 2011 == field4.getFieldType()) {
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t");
					stringbuffer
							.append((new StringBuilder()).append("if(null!=").append(s7).append("&&null!=newBean.get")
									.append(StringUtil.toUpperCaseFirstOne(s7)).append("()){").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t");
					stringbuffer.append((new StringBuilder()).append("int ").append(s7).append("Size=").append(s7)
							.append(".size();").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t");
					stringbuffer.append((new StringBuilder()).append("if(").append(s7).append("Size>newBean.get")
							.append(StringUtil.toUpperCaseFirstOne(s7)).append("().size()){").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\t");
					stringbuffer.append((new StringBuilder()).append(s7).append("Size=newBean.get")
							.append(StringUtil.toUpperCaseFirstOne(s7)).append("().size();").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t");
					stringbuffer.append((new StringBuilder()).append("for( int i =0; i < ").append(s7)
							.append("Size; i++) {").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\t");
					if ("dynamic".equals(field4.getReferenceType()) || "expression".equals(field4.getReferenceType()))
						stringbuffer.append(
								(new StringBuilder()).append("((com.giantstone.message.bean.MessageBean)").append(s7)
										.append(".get(i)).cover((com.giantstone.message.bean.MessageBean)newBean.get")
										.append(StringUtil.toUpperCaseFirstOne(s7)).append("().get(i));").toString());
					else
						stringbuffer.append((new StringBuilder()).append("get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("At(i).cover(newBean.get")
								.append(StringUtil.toUpperCaseFirstOne(s7)).append("At(i));").toString());
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
				}
		} while (true);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
	}

	private void buildValidate(Message message, StringBuilder stringbuffer) {
		String s3 = null;
		stringbuffer.append("\tpublic void validate(){");
		stringbuffer.append(NEW_LINE);
		Iterator<?> iterator = message.getFields().values().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Field field5 = (Field) iterator.next();
			if (2007 != field5.getFieldType() && 2005 != field5.getFieldType() && 2006 != field5.getFieldType()
					&& field5.isEditable()) {
				if (field5.isRequired() && 3003 != field5.getDataType() && 3007 != field5.getDataType()
						&& 3004 != field5.getDataType() && 3005 != field5.getDataType() && 3008 != field5.getDataType()
						&& 2000 != field5.getFieldType()) {
					stringbuffer.append("\t\t//");
					stringbuffer.append(field5.getShortText());
					stringbuffer.append("非空检查");
					stringbuffer.append(NEW_LINE);
					if (2002 == field5.getFieldType() || 2008 == field5.getFieldType() || 2003 == field5.getFieldType()
							|| 2009 == field5.getFieldType()) {
						stringbuffer.append("\t\tif ( null == ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(" || ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".isNull() ");
					} else if (2004 == field5.getFieldType() || 2011 == field5.getFieldType()) {
						stringbuffer.append("\t\tif ( 0 == ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".size() ");
					} else {
						stringbuffer.append("\t\tif ( null == ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
					}
					stringbuffer.append("){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tthrow new RuntimeException(");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\t");
					stringbuffer.append(
							"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.null\", new String[]{\"");
					if (null != field5.getShortText()) {
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("(");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(")");
					} else {
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
					}
					stringbuffer.append("\"}));");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
				}
				if (2002 == field5.getFieldType() || 2003 == field5.getFieldType() || 2008 == field5.getFieldType()
						|| 2009 == field5.getFieldType()) {
					stringbuffer.append("\t\t//");
					stringbuffer.append(field5.getShortText());
					stringbuffer.append("正确性检查");
					stringbuffer.append(NEW_LINE);
					if (!field5.isRequired()) {
						stringbuffer.append("\t\t");
						stringbuffer.append("if ( null != ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(" && !");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".isNull() ){ ");
						stringbuffer.append(NEW_LINE);
					}
					stringbuffer.append("\t\t\t");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
					stringbuffer.append(".validate();");
					stringbuffer.append(NEW_LINE);
					if (!field5.isRequired())
						stringbuffer.append("\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
				} else if (2004 == field5.getFieldType() || 2011 == field5.getFieldType()) {
					if (field5.getReference() != null)
						s3 = field5.getReference().getClassName();
					else if ("dynamic".equalsIgnoreCase(field5.getReferenceType())
							|| "expression".equalsIgnoreCase(field5.getReferenceType())) {
						s3 = MessageBean.class.getName();
					} else {
						s3 = field5.getCombineOrTableFieldClassName();
						if (null == s3)
							s3 = (new StringBuilder()).append(message.getClassName())
									.append(StringUtil.toUpperCaseFirstOne(field5.getName())).toString();
					}
					if (!field5.isRequired()) {
						stringbuffer.append("\t\tif ( 0 != ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".size() ) { ");
					}
					stringbuffer.append("\t\t//");
					stringbuffer.append(field5.getShortText());
					stringbuffer.append("正确性检查");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\tfor( int i = 0; i < ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
					stringbuffer.append(".size(); i++){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t((");
					stringbuffer.append(s3);
					stringbuffer.append(")");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
					stringbuffer.append(".get(i)).validate();");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append(NEW_LINE);
					if (!field5.isRequired()) {
						stringbuffer.append("\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
					}
				} else if (2000 == field5.getFieldType()) {
					s3 = Constant.getJavaTypeByDataType(field5.getDataType());
					if ("String".equals(s3) || "byte[]".equals(s3)) {
						if (field5.isRequired()) {
							stringbuffer.append("\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("非空检查");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\tif ( null == ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("){");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.null\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\"}));");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						} else {
							stringbuffer.append("\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("不为空则按以下规则校验");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\tif( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0!= ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3))
									stringbuffer.append(".length()");
								else
									stringbuffer.append(".length");
							}
							stringbuffer.append("){");
							stringbuffer.append(NEW_LINE);
						}
						if (field5.getLength() != -1) {
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("数据长度检查");
							stringbuffer.append(NEW_LINE);
							if ("String".equals(s3)
									&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
								stringbuffer.append("\t\t\ttry{");
								stringbuffer.append(NEW_LINE);
							}
							stringbuffer.append("\t\t\tif ( ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("String".equals(s3))
								if (null != field5.getDataCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(field5.getDataCharset()).append("\").length ").toString());
								else if (null != message.getMsgCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(message.getMsgCharset()).append("\").length").toString());
								else
									stringbuffer.append(".getBytes().length ");
							if ("byte[]".equals(s3))
								stringbuffer.append(".length ");
							if (field5.isStrictDataLength() || 3002 == field5.getDataType())
								stringbuffer.append("!= ");
							else
								stringbuffer.append("> ");
							stringbuffer.append(field5.getLength());
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t");
							stringbuffer.append("MultiLanguageResourceBundle.getInstance().getString(\"");
							if (field5.isStrictDataLength() || 3002 == field5.getDataType())
								stringbuffer.append("Message.field.length");
							else
								stringbuffer.append("Message.field.maxLength");
							stringbuffer.append("\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getLength());
							stringbuffer.append("\"");
							stringbuffer.append("}));");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							if ("String".equals(s3)
									&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t}catch (UnsupportedEncodingException e) {");
								stringbuffer.append(NEW_LINE);
								if (null != field5.getDataCharset()) {
									stringbuffer.append(
											"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
									if (null != field5.getShortText()) {
										stringbuffer.append(field5.getShortText());
										stringbuffer.append("(");
										stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
										stringbuffer.append(")");
									} else {
										stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									}
									stringbuffer.append("\", \"");
									stringbuffer.append(field5.getDataCharset());
									stringbuffer.append("\"}));");
								} else {
									stringbuffer.append(
											"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
									stringbuffer.append(message.getId());
									stringbuffer.append("\", \"");
									stringbuffer.append(message.getMsgCharset());
									stringbuffer.append("\"}));");
								}
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t}");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
						if (3010 == field5.getDataType()) {
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("输入类型检查");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\tBigDecimal big");
							stringbuffer.append(StringUtil.toUpperCaseFirstOne(field5.getName()));
							stringbuffer.append(" = null;");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\ttry{");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tbig");
							stringbuffer.append(StringUtil.toUpperCaseFirstOne(field5.getName()));
							stringbuffer.append(" = new BigDecimal(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(");");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t} catch (Exception e) {");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(
									"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustBigDecimal\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\"}) + e.getMessage(), e);");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							stringbuffer.append(NEW_LINE);
							String as[] = field5.getPattern().split(",");
							stringbuffer.append("\t\t\tif ( ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(".indexOf(\".\") == -1 && ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(".length() > ");
							if (as.length > 1)
								stringbuffer.append(Integer.parseInt(as[0]) - Integer.parseInt(as[1]));
							else
								stringbuffer.append(Integer.parseInt(as[0]));
							stringbuffer.append("){");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.wrong\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getPattern());
							stringbuffer.append("\"}));");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}else{");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tif( -1 != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(".indexOf(\".\")){");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\tif ( ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(".substring(0, ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(".indexOf(\".\")).length() > ");
							if (as.length > 1)
								stringbuffer.append(Integer.parseInt(as[0]) - Integer.parseInt(as[1]));
							else
								stringbuffer.append(Integer.parseInt(as[0]));
							stringbuffer.append("){");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.wrong\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getPattern());
							stringbuffer.append("\"}));");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(" = big");
							stringbuffer.append(StringUtil.toUpperCaseFirstOne(field5.getName()));
							stringbuffer.append(".divide(new BigDecimal(1), ");
							if (as.length > 1)
								stringbuffer.append(as[1]);
							else
								stringbuffer.append("0");
							stringbuffer.append(", BigDecimal.ROUND_HALF_UP).toString();");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
						if (3001 == field5.getDataType()) {
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("输入类型检查");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\tif(!CodeUtil.isNumeric(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")){");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(
									"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustNum\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\"}));");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
						if (3006 == field5.getDataType()) {
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("格式检查");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\ttry{");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(
									"\t\t\t\tjava.text.DateFormat dateformat = new java.text.SimpleDateFormat(\"");
							stringbuffer.append(field5.getPattern());
							stringbuffer.append("\");");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tdateformat.setLenient(false);");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tdateformat.parse(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(");");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t} catch (Exception e) {");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getPattern());
							stringbuffer.append("\"}) + e.getMessage(), e);");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
						if (3000 == field5.getDataType() && null != field5.getPattern()) {
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("格式检查");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\tjava.util.regex.Pattern ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("Pattern = java.util.regex.Pattern.compile(\"");
							stringbuffer.append(field5.getPattern());
							stringbuffer.append("\");");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\tjava.util.regex.Matcher ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("Matcher = ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("Pattern.matcher(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(");");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\tif ( !");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("Matcher.matches() ){");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getPattern());
							stringbuffer.append("\"}));");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							stringbuffer.append(NEW_LINE);
						}
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
				} else if (2001 == field5.getFieldType()) {
					s3 = Constant.getJavaTypeByDataType(field5.getDataType());
					if (field5.getRefLengthField() != null) {
						Field field8 = field5.getRefLengthField();
						if ((3001 == field8.getDataType() || 3004 == field8.getDataType())
								&& ("String".equals(s3) || "byte[]".equalsIgnoreCase(s3))) {
							if (!field5.isRequired()) {
								stringbuffer.append("\t\tif ( null != ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("0" == operBlank) {
									stringbuffer.append(" && 0 != ");
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									if ("String".equals(s3))
										stringbuffer.append(".length()");
									else
										stringbuffer.append(".length");
								}
								stringbuffer.append(" ) {");
								stringbuffer.append(NEW_LINE);
							}
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("长度检查");
							stringbuffer.append(NEW_LINE);
							if ("String".equals(s3)
									&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
								stringbuffer.append("\t\t\ttry{");
								stringbuffer.append(NEW_LINE);
							}
							stringbuffer.append("\t\t\tif ( ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("String".equals(s3)) {
								if (null != field5.getDataCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(field5.getDataCharset()).append("\").length > ").toString());
								else if (null != message.getMsgCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(message.getMsgCharset()).append("\").length > ").toString());
								else
									stringbuffer.append(".getBytes().length > ");
							} else if ("byte[]".equalsIgnoreCase(s3))
								stringbuffer.append(".length > ");
							if (field5.getMaxLength() != -1)
								stringbuffer.append(field5.getMaxLength());
							else if (3001 == field8.getDataType()) {
								for (int i = 0; i < field8.getLength(); i++)
									stringbuffer.append("9");

							} else if (3004 == field8.getDataType())
								stringbuffer.append("255");
							if (field5.getMinLength() != -1) {
								stringbuffer.append(" || ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3)) {
									if (null != field5.getDataCharset())
										stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
												.append(field5.getDataCharset()).append("\").length < ").toString());
									else if (null != message.getMsgCharset())
										stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
												.append(message.getMsgCharset()).append("\").length < ").toString());
									else
										stringbuffer.append(".getBytes().length < ");
								} else if ("byte[]".equalsIgnoreCase(s3))
									stringbuffer.append(".length < ");
								stringbuffer.append(field5.getMinLength());
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
							if (field5.getMinLength() != -1) {
								stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t\t\t");
								stringbuffer.append(
										"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
								if (null != field5.getShortText()) {
									stringbuffer.append(field5.getShortText());
									stringbuffer.append("(");
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									stringbuffer.append(")");
								} else {
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								}
								stringbuffer.append("\", \"");
								if (field5.getMaxLength() != -1)
									stringbuffer.append(field5.getMaxLength());
								else if (3001 == field8.getDataType()) {
									for (int j = 0; j < field8.getLength(); j++)
										stringbuffer.append("9");

								} else if (3004 == field8.getDataType())
									stringbuffer.append("255");
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getMinLength());
								stringbuffer.append("\"}));");
							} else {
								stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t\t\t");
								stringbuffer.append(
										"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
								if (null != field5.getShortText()) {
									stringbuffer.append(field5.getShortText());
									stringbuffer.append("(");
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									stringbuffer.append(")");
								} else {
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								}
								stringbuffer.append("\", \"");
								if (field5.getMaxLength() != -1)
									stringbuffer.append(field5.getMaxLength());
								else if (3001 == field8.getDataType()) {
									for (int k = 0; k < field8.getLength(); k++)
										stringbuffer.append("9");

								} else if (3004 == field8.getDataType())
									stringbuffer.append("255");
								stringbuffer.append("\"}));");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							if ("String".equals(s3)
									&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t}catch (UnsupportedEncodingException e) {");
								stringbuffer.append(NEW_LINE);
								if (null != field5.getDataCharset()) {
									stringbuffer.append(
											"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
									if (null != field5.getShortText()) {
										stringbuffer.append(field5.getShortText());
										stringbuffer.append("(");
										stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
										stringbuffer.append(")");
									} else {
										stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									}
									stringbuffer.append("\", \"");
									stringbuffer.append(field5.getDataCharset());
									stringbuffer.append("\"}));");
								} else {
									stringbuffer.append(
											"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
									stringbuffer.append(message.getId());
									stringbuffer.append("\", \"");
									stringbuffer.append(message.getMsgCharset());
									stringbuffer.append("\"}));");
								}
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t}");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
							if (!field5.isRequired()) {
								stringbuffer.append("\t\t}");
								stringbuffer.append(NEW_LINE);
								stringbuffer.append(NEW_LINE);
							}
						} else if (("String".equals(s3) || "byte[]".equalsIgnoreCase(s3))
								&& (field5.getMaxLength() != -1 || field5.getMinLength() != -1)) {
							if (!field5.isRequired()) {
								stringbuffer.append("\t\tif ( null != ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("0" == operBlank) {
									stringbuffer.append(" && 0 != ");
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									if ("String".equals(s3))
										stringbuffer.append(".length()");
									else
										stringbuffer.append(".length");
								}
								stringbuffer.append(" ) {");
								stringbuffer.append(NEW_LINE);
							}
							stringbuffer.append("\t\t\t//");
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("长度检查");
							stringbuffer.append(NEW_LINE);
							if ("String".equals(s3)
									&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
								stringbuffer.append("\t\t\ttry{");
								stringbuffer.append(NEW_LINE);
							}
							stringbuffer.append("\t\t\tif ( ");
							if (field5.getMaxLength() != -1) {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3)) {
									if (null != field5.getDataCharset())
										stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
												.append(field5.getDataCharset()).append("\").length > ").toString());
									else if (null != message.getMsgCharset())
										stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
												.append(message.getMsgCharset()).append("\").length > ").toString());
									else
										stringbuffer.append(".getBytes().length > ");
								} else if ("byte[]".equalsIgnoreCase(s3))
									stringbuffer.append(".length > ");
								stringbuffer.append(field5.getMaxLength());
							}
							if (field5.getMaxLength() != -1 && field5.getMinLength() != -1)
								stringbuffer.append(" || ");
							if (field5.getMinLength() != -1) {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3)) {
									if (null != field5.getDataCharset())
										stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
												.append(field5.getDataCharset()).append("\").length < ").toString());
									else if (null != message.getMsgCharset())
										stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
												.append(message.getMsgCharset()).append("\").length < ").toString());
									else
										stringbuffer.append(".getBytes().length < ");
								} else if ("byte[]".equalsIgnoreCase(s3))
									stringbuffer.append(".length < ");
								stringbuffer.append(field5.getMinLength());
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							if (field5.getMaxLength() != -1 && field5.getMinLength() != -1) {
								stringbuffer.append(
										"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getMaxLength());
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getMinLength());
								stringbuffer.append("\"}));");
							} else if (field5.getMaxLength() != -1) {
								stringbuffer.append(
										"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getMaxLength());
								stringbuffer.append("\"}));");
							} else if (field5.getMinLength() != -1) {
								stringbuffer.append(
										"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.minLength\", new String[]{\"");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getMinLength());
								stringbuffer.append("\"}));");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
							if ("String".equals(s3)
									&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t}catch (UnsupportedEncodingException e) {");
								stringbuffer.append(NEW_LINE);
								if (null != field5.getDataCharset()) {
									stringbuffer.append(
											"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
									if (null != field5.getShortText()) {
										stringbuffer.append(field5.getShortText());
										stringbuffer.append("(");
										stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
										stringbuffer.append(")");
									} else {
										stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									}
									stringbuffer.append("\", \"");
									stringbuffer.append(field5.getDataCharset());
									stringbuffer.append("\"}));");
								} else {
									stringbuffer.append(
											"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
									stringbuffer.append(message.getId());
									stringbuffer.append("\", \"");
									stringbuffer.append(message.getMsgCharset());
									stringbuffer.append("\"}));");
								}
								stringbuffer.append(NEW_LINE);
								stringbuffer.append("\t\t\t}");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
							if (!field5.isRequired()) {
								stringbuffer.append("\t\t}");
								stringbuffer.append(NEW_LINE);
								stringbuffer.append(NEW_LINE);
							}
						}
					} else if ((3001 == field5.getLengthFieldDataType() || 3002 == field5.getLengthFieldDataType())
							&& ("String".equals(s3) || "byte[]".equalsIgnoreCase(s3))) {
						if (!field5.isRequired()) {
							stringbuffer.append("\t\tif ( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0 != ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3))
									stringbuffer.append(".length()");
								else
									stringbuffer.append(".length");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("长度检查");
						stringbuffer.append(NEW_LINE);
						if ("String".equals(s3)
								&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
							stringbuffer.append("\t\t\ttry{");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t\tif ( ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						if ("String".equals(s3)) {
							if (null != field5.getDataCharset())
								stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
										.append(field5.getDataCharset()).append("\").length > ").toString());
							else if (null != message.getMsgCharset())
								stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
										.append(message.getMsgCharset()).append("\").length > ").toString());
							else
								stringbuffer.append(".getBytes().length > ");
						} else if ("byte[]".equalsIgnoreCase(s3))
							stringbuffer.append(".length > ");
						if (field5.getMaxLength() != -1)
							stringbuffer.append(field5.getMaxLength());
						else if (3001 == field5.getLengthFieldDataType()) {
							for (int l = 0; l < field5.getLengthFieldLength(); l++)
								stringbuffer.append("9");

						} else if (3004 == field5.getLengthFieldDataType())
							stringbuffer.append("255");
						if (field5.getMinLength() != -1) {
							stringbuffer.append(" || ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("String".equals(s3)) {
								if (null != field5.getDataCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(field5.getDataCharset()).append("\").length < ").toString());
								else if (null != message.getMsgCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(message.getMsgCharset()).append("\").length < ").toString());
								else
									stringbuffer.append(".getBytes().length < ");
							} else if ("byte[]".equalsIgnoreCase(s3))
								stringbuffer.append(".length < ");
							stringbuffer.append(field5.getMinLength());
						}
						stringbuffer.append(" ) {");
						stringbuffer.append(NEW_LINE);
						if (field5.getMinLength() != -1) {
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("\", \"");
							if (field5.getMaxLength() != -1)
								stringbuffer.append(field5.getMaxLength());
							else if (3001 == field5.getLengthFieldDataType()) {
								for (int i1 = 0; i1 < field5.getLengthFieldLength(); i1++)
									stringbuffer.append("9");

							} else if (3004 == field5.getLengthFieldDataType())
								stringbuffer.append("255");
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getMinLength());
							stringbuffer.append("\"}));");
						} else {
							stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t\t\t");
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append("\", \"");
							if (field5.getMaxLength() != -1)
								stringbuffer.append(field5.getMaxLength());
							else if (3001 == field5.getLengthFieldDataType()) {
								for (int j1 = 0; j1 < field5.getLengthFieldLength(); j1++)
									stringbuffer.append("9");

							} else if (3004 == field5.getLengthFieldDataType())
								stringbuffer.append("255");
							stringbuffer.append("\"}));");
						}
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t}");
						if ("String".equals(s3)
								&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}catch (UnsupportedEncodingException e) {");
							stringbuffer.append(NEW_LINE);
							if (null != field5.getDataCharset()) {
								stringbuffer.append(
										"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
								if (null != field5.getShortText()) {
									stringbuffer.append(field5.getShortText());
									stringbuffer.append("(");
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									stringbuffer.append(")");
								} else {
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								}
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getDataCharset());
								stringbuffer.append("\"}));");
							} else {
								stringbuffer.append(
										"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
								stringbuffer.append(message.getId());
								stringbuffer.append("\", \"");
								stringbuffer.append(message.getMsgCharset());
								stringbuffer.append("\"}));");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
						}
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					} else if (("String".equals(s3) || "byte[]".equalsIgnoreCase(s3))
							&& (field5.getMaxLength() != -1 || field5.getMinLength() != -1)) {
						if (!field5.isRequired()) {
							stringbuffer.append("\t\tif ( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0 != ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3))
									stringbuffer.append(".length()");
								else
									stringbuffer.append(".length");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("长度检查");
						stringbuffer.append(NEW_LINE);
						if ("String".equals(s3)
								&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
							stringbuffer.append("\t\t\ttry{");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t\tif ( ");
						if (field5.getMaxLength() != -1) {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("String".equals(s3)) {
								if (null != field5.getDataCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(field5.getDataCharset()).append("\").length > ").toString());
								else if (null != message.getMsgCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(message.getMsgCharset()).append("\").length > ").toString());
								else
									stringbuffer.append(".getBytes().length > ");
							} else if ("byte[]".equalsIgnoreCase(s3))
								stringbuffer.append(".length > ");
							stringbuffer.append(field5.getMaxLength());
						}
						if (field5.getMaxLength() != -1 && field5.getMinLength() != -1)
							stringbuffer.append(" || ");
						if (field5.getMinLength() != -1) {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("String".equals(s3)) {
								if (null != field5.getDataCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(field5.getDataCharset()).append("\").length < ").toString());
								else if (null != message.getMsgCharset())
									stringbuffer.append((new StringBuilder()).append(".getBytes(\"")
											.append(message.getMsgCharset()).append("\").length < ").toString());
								else
									stringbuffer.append(".getBytes().length < ");
							} else if ("byte[]".equalsIgnoreCase(s3))
								stringbuffer.append(".length < ");
							stringbuffer.append(field5.getMinLength());
						}
						stringbuffer.append(" ) {");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\t");
						if (field5.getMaxLength() != -1 && field5.getMinLength() != -1) {
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getMaxLength());
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getMinLength());
							stringbuffer.append("\"}));");
						} else if (field5.getMaxLength() != -1) {
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getMaxLength());
							stringbuffer.append("\"}));");
						} else if (field5.getMinLength() != -1) {
							stringbuffer.append(
									"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.minLength\", new String[]{\"");
							if (null != field5.getShortText()) {
								stringbuffer.append(field5.getShortText());
								stringbuffer.append("(");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(")");
							} else {
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							}
							stringbuffer.append("\", \"");
							stringbuffer.append(field5.getMinLength());
							stringbuffer.append("\"}));");
						}
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t}");
						if ("String".equals(s3)
								&& (null != field5.getDataCharset() || null != message.getMsgCharset())) {
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}catch (UnsupportedEncodingException e) {");
							stringbuffer.append(NEW_LINE);
							if (null != field5.getDataCharset()) {
								stringbuffer.append(
										"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
								if (null != field5.getShortText()) {
									stringbuffer.append(field5.getShortText());
									stringbuffer.append("(");
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
									stringbuffer.append(")");
								} else {
									stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								}
								stringbuffer.append("\", \"");
								stringbuffer.append(field5.getDataCharset());
								stringbuffer.append("\"}));");
							} else {
								stringbuffer.append(
										"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
								stringbuffer.append(message.getId());
								stringbuffer.append("\", \"");
								stringbuffer.append(message.getMsgCharset());
								stringbuffer.append("\"}));");
							}
							stringbuffer.append(NEW_LINE);
							stringbuffer.append("\t\t\t}");
						}
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
					if (3001 == field5.getDataType()) {
						if (!field5.isRequired()) {
							stringbuffer.append("\t\tif ( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0 !=");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(".length()");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("输入类型检查");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\tif(!CodeUtil.isNumeric(");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(")){");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(
								"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustNum\", new String[]{\"");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\"}));");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
					if (3006 == field5.getDataType()) {
						stringbuffer.append("\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("格式检查");
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\tif ( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0 !=");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(".length()");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\ttry{");
						stringbuffer.append(NEW_LINE);
						stringbuffer
								.append("\t\t\tjava.text.DateFormat dateformat = new java.text.SimpleDateFormat(\"");
						stringbuffer.append(field5.getPattern());
						stringbuffer.append("\");");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\tdateformat.setLenient(false);");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\tdateformat.parse(");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(");");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t} catch (Exception e) {");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\t");
						stringbuffer.append(
								"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\", \"");
						stringbuffer.append(field5.getPattern());
						stringbuffer.append("\"}) + e.getMessage(), e);");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
					if (3000 == field5.getDataType() && null != field5.getPattern()) {
						stringbuffer.append("\t\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("格式检查");
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\tif ( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0 !=");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(".length()");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t\tjava.util.regex.Pattern ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append("Pattern = java.util.regex.Pattern.compile(\"");
						stringbuffer.append(field5.getPattern());
						stringbuffer.append("\");");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\tjava.util.regex.Matcher ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append("Matcher = ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append("Pattern.matcher(");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(");");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\tif ( !");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append("Matcher.matches() ){");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\t");
						stringbuffer.append(
								"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\", \"");
						stringbuffer.append(field5.getPattern());
						stringbuffer.append("\"}));");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t}");
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
					if (3010 == field5.getDataType()) {
						stringbuffer.append("\t\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("输入类型检查");
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\tif ( null != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if ("0" == operBlank) {
								stringbuffer.append(" && 0 !=");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(".length()");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\t\tBigDecimal big");
						stringbuffer.append(StringUtil.toUpperCaseFirstOne(field5.getName()));
						stringbuffer.append(" = null;");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\ttry{");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tbig");
						stringbuffer.append(StringUtil.toUpperCaseFirstOne(field5.getName()));
						stringbuffer.append(" = new BigDecimal(");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(");");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t} catch (Exception e) {");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(
								"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustBigDecimal\", new String[]{\"");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\"}) + e.getMessage(), e);");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t}");
						stringbuffer.append(NEW_LINE);
						String as1[] = field5.getPattern().split(",");
						stringbuffer.append("\t\t\tif ( ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".indexOf(\".\") == -1 && ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".length() > ");
						if (as1.length > 1)
							stringbuffer.append(Integer.parseInt(as1[0]) - Integer.parseInt(as1[1]));
						else
							stringbuffer.append(Integer.parseInt(as1[0]));
						stringbuffer.append("){");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\t");
						stringbuffer.append(
								"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\", \"");
						stringbuffer.append(field5.getPattern());
						stringbuffer.append("\"}));");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t}else{");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tif( -1 != ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".indexOf(\".\")){");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\tif ( ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".substring(0, ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(".indexOf(\".\")).length() > ");
						if (as1.length > 1)
							stringbuffer.append(Integer.parseInt(as1[0]) - Integer.parseInt(as1[1]));
						else
							stringbuffer.append(Integer.parseInt(as1[0]));
						stringbuffer.append("){");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\t");
						stringbuffer.append(
								"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\", \"");
						stringbuffer.append(field5.getPattern());
						stringbuffer.append("\"}));");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t\t");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						stringbuffer.append(" = big");
						stringbuffer.append(StringUtil.toUpperCaseFirstOne(field5.getName()));
						stringbuffer.append(".divide(new BigDecimal(1), ");
						if (as1.length > 1)
							stringbuffer.append(as1[1]);
						else
							stringbuffer.append("0");
						stringbuffer.append(", BigDecimal.ROUND_HALF_UP).toString();");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
				}
				if (2000 == field5.getFieldType() || 2001 == field5.getFieldType()) {
					HashMap<?, ?> hashmap1 = (HashMap<?, ?>) field5.getValueRange();
					if (hashmap1 != null && 0 != hashmap1.size() && null == hashmap1.get("default-ref")) {
						stringbuffer.append("\t\t//");
						stringbuffer.append(field5.getShortText());
						stringbuffer.append("指定值域检查");
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							if ("String".equals(s3) || "byte[]".equals(s3))
								stringbuffer.append("\t\tif ( null != ");
							else
								stringbuffer.append("\t\tif ( 0 != ");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							if (("String".equals(s3) || "byte[]".equals(s3)) && "0" == operBlank) {
								stringbuffer.append(" && 0 !=");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								if ("String".equals(s3))
									stringbuffer.append(".length()");
								else
									stringbuffer.append(".length");
							}
							stringbuffer.append(" ) {");
							stringbuffer.append(NEW_LINE);
						}
						stringbuffer.append("\t\tif ( ");
						for (Iterator<?> iterator1 = hashmap1.keySet().iterator(); iterator1.hasNext();) {
							String s6 = (String) iterator1.next();
							if ("String".equals(s3) || "byte[]".equals(s3)) {
								stringbuffer.append("!\"");
								stringbuffer.append(s6);
								stringbuffer.append("\".equals( ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(") && ");
							} else {
								stringbuffer.append(s6);
								stringbuffer.append(" != ");
								stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
								stringbuffer.append(" && ");
							}
						}

						stringbuffer.delete(stringbuffer.length() - 3, stringbuffer.length());
						stringbuffer.append(") {");
						stringbuffer.append(NEW_LINE);
						StringBuffer stringbuffer1 = new StringBuffer();
						for (Iterator<?> iterator2 = hashmap1.keySet().iterator(); iterator2.hasNext(); stringbuffer1
								.append("/"))
							stringbuffer1.append(iterator2.next());

						stringbuffer1.delete(stringbuffer1.length() - 1, stringbuffer1.length());
						stringbuffer.append("\t\t\tthrow new RuntimeException(");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(
								"\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.field.value.in\", new String[]{\")");
						if (null != field5.getShortText()) {
							stringbuffer.append(field5.getShortText());
							stringbuffer.append("(");
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
							stringbuffer.append(")");
						} else {
							stringbuffer.append(StringUtil.toLowerCaseFirstOne(field5.getName()));
						}
						stringbuffer.append("\", \"");
						stringbuffer.append(stringbuffer1.toString());
						stringbuffer.append("\"}));");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append("\t\t}");
						stringbuffer.append(NEW_LINE);
						stringbuffer.append(NEW_LINE);
						if (!field5.isRequired()) {
							stringbuffer.append("\t\t}");
							stringbuffer.append(NEW_LINE);
							stringbuffer.append(NEW_LINE);
						}
					}
				}
			}
		} while (true);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
	}

	private void buildToString(Message message, StringBuilder stringbuffer) {
		stringbuffer.append("\tpublic String toString(){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\treturn toString(false);");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\tpublic String toString(boolean isWrap){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\treturn toString(isWrap,false);");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\tpublic String toString(boolean isWrap,boolean isTable){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\tStringBuffer buf = new StringBuffer(10240);");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\tStringBuffer tableBuf = new StringBuffer(2048);");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\tString str = null;");
		stringbuffer.append(NEW_LINE);
		Iterator<?> iterator = message.getFields().values().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Field field6 = (Field) iterator.next();
			if (field6.isEditable())
				if (2002 == field6.getFieldType() || 2003 == field6.getFieldType() || 2008 == field6.getFieldType()
						|| 2009 == field6.getFieldType()) {
					stringbuffer.append("\t\tif( null != ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tstr = ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append(".toString(true);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tif( null != str){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\tbuf.append(\"<a n=\\\"");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("\\\" t=\\\"bean\\\">\");");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\tbuf.append(str);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\tbuf.append(\"");
					stringbuffer.append("</a>\");");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
					stringbuffer.append(NEW_LINE);
				} else if (2004 == field6.getFieldType() || 2011 == field6.getFieldType()) {
					String s4;
					if (field6.getReference() != null)
						s4 = field6.getReference().getClassName();
					else if ("dynamic".equalsIgnoreCase(field6.getReferenceType())
							|| "expression".equalsIgnoreCase(field6.getReferenceType())) {
						s4 = MessageBean.class.getName();
					} else {
						s4 = field6.getCombineOrTableFieldClassName();
						if (null == s4)
							s4 = (new StringBuilder()).append(message.getClassName())
									.append(StringUtil.toUpperCaseFirstOne(field6.getName())).toString();
					}
					stringbuffer.append("\t\t");
					stringbuffer.append(s4);
					stringbuffer.append(" ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("Field = null;");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\tfor( int i =0; i < ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append(".size(); i++) {");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("Field = (");
					stringbuffer.append(s4);
					stringbuffer.append(") ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append(".get(i);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tstr = ");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("Field.toString(true,true);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tif( null != str){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t\ttableBuf.append(str);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\tif ( 0 != tableBuf.length()){");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tbuf.append(\"<a n=\\\"");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("\\\" t=\\\"list\\\" c=\\\"java.util.ArrayList\\\" rc=\\\"");
					stringbuffer.append(s4);
					stringbuffer.append("\\\">\");");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tbuf.append(tableBuf);");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tbuf.append(\"</a>\");");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\ttableBuf.delete(0,tableBuf.length());");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t}");
					stringbuffer.append(NEW_LINE);
				} else {
					if (3000 == field6.getDataType() || 3001 == field6.getDataType() || 3006 == field6.getDataType()
							|| 3002 == field6.getDataType() || 3010 == field6.getDataType()) {
						stringbuffer.append("\t\tif( null != ");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
						stringbuffer.append("){");
					}
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tbuf.append(\"<a n=\\\"");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					stringbuffer.append("\\\" t=\\\"");
					stringbuffer.append(Constant.getDataTypeText(field6.getDataType()));
					stringbuffer.append("\\\">\");");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tbuf.append(");
					if (3002 == field6.getDataType()) {
						stringbuffer.append("new String(CodeUtil.BytetoHex(");
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
						stringbuffer.append("))");
					} else {
						stringbuffer.append(StringUtil.toLowerCaseFirstOne(field6.getName()));
					}
					stringbuffer.append(");");
					stringbuffer.append(NEW_LINE);
					stringbuffer.append("\t\t\tbuf.append(\"</a>\");");
					stringbuffer.append(NEW_LINE);
					if (3000 == field6.getDataType() || 3001 == field6.getDataType() || 3006 == field6.getDataType()
							|| 3002 == field6.getDataType() || 3010 == field6.getDataType()) {
						stringbuffer.append("\t\t}");
						stringbuffer.append(NEW_LINE);
					}
				}
		} while (true);
		stringbuffer.append("\t\tif( 0 == buf.length()){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\treturn null;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t}else{");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\tif ( isTable ){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t\tbuf.insert(0,\"<b>\");");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t}else{");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t\tbuf.insert(0,\"<b c=\\\"");
		stringbuffer.append(message.getClassName());
		stringbuffer.append("\\\">\");");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\tbuf.append(\"</b>\");");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\tif( !isWrap ){");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t\tbuf = new StringBuffer(buf.toString());");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t\tbuf.insert(0,\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\");");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t\treturn buf.toString();");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t}else{");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t\treturn buf.toString();");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
	}

	private void buildIsNull(Message message, StringBuilder stringbuffer) {
		stringbuffer.append("\tpublic boolean isNull(){");
		stringbuffer.append(NEW_LINE);
		Iterator<?> iterator = message.getFields().values().iterator();
		do {
			if (!iterator.hasNext())
				break;
			Field field7 = (Field) iterator.next();
			if (2002 == field7.getFieldType() || 2003 == field7.getFieldType() || 2008 == field7.getFieldType()
					|| 2009 == field7.getFieldType()) {
				stringbuffer.append("\t\tif( null != ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append("){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\tif (  !");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append(".isNull() ){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\t\treturn false;");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\t}");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t}");
				stringbuffer.append(NEW_LINE);
			} else if (2004 == field7.getFieldType() || 2011 == field7.getFieldType()) {
				String s5;
				if (field7.getReference() != null)
					s5 = field7.getReference().getClassName();
				else if ("dynamic".equalsIgnoreCase(field7.getReferenceType())
						|| "expression".equalsIgnoreCase(field7.getReferenceType())) {
					s5 = MessageBean.class.getName();
				} else {
					s5 = field7.getCombineOrTableFieldClassName();
					if (null == s5)
						s5 = (new StringBuilder()).append(message.getClassName())
								.append(StringUtil.toUpperCaseFirstOne(field7.getName())).toString();
				}
				stringbuffer.append("\t\t");
				stringbuffer.append(s5);
				stringbuffer.append(" ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append("Field = null;");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\tfor( int i =0; i < ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append(".size(); i++) {");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\t");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append("Field = (");
				stringbuffer.append(s5);
				stringbuffer.append(") ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append(".get(i);");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\tif ( null != ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append(" && !");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				stringbuffer.append("Field.isNull() ){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\t\treturn false;");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\t}");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t}");
				stringbuffer.append(NEW_LINE);
			} else if (3000 == field7.getDataType() || 3001 == field7.getDataType() || 3006 == field7.getDataType()
					|| 3002 == field7.getDataType() || 3010 == field7.getDataType()) {
				stringbuffer.append("\t\tif( null != ");
				stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
				if ("0" == operBlank) {
					stringbuffer.append(" && 0 !=");
					stringbuffer.append(StringUtil.toLowerCaseFirstOne(field7.getName()));
					if (3002 == field7.getDataType())
						stringbuffer.append(".length ");
					else
						stringbuffer.append(".length() ");
				}
				stringbuffer.append("){");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t\treturn false;");
				stringbuffer.append(NEW_LINE);
				stringbuffer.append("\t\t}");
				stringbuffer.append(NEW_LINE);
			}
		} while (true);
		stringbuffer.append("\t\treturn true;");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("\t}");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append("}");
		stringbuffer.append(NEW_LINE);
	}

	public void generate(Message message, String s) {
		if (Objects.isNull(message)) {
			return;
		}
		String classPath = message.getClassName();
		if (StrUtil.isEmpty(classPath)) {
			return;
		}
		String className = message.getClassName().substring(message.getClassName().lastIndexOf(".") + 1);
		StringBuilder stringbuffer = new StringBuilder(10240);

		buildHeader(message, stringbuffer);

		stringbuffer.append("public class ");
		stringbuffer.append(StringUtil.toUpperCaseFirstOne(className));
		stringbuffer.append(" extends MessageBean{");
		stringbuffer.append(NEW_LINE);
		stringbuffer.append(NEW_LINE);

		buildAttrGetSet(message, stringbuffer);

		buildGetAttribute(message, stringbuffer);

		buildSetAttribute(message, stringbuffer);

		buildCover(message, stringbuffer);

		buildValidate(message, stringbuffer);

		buildToString(message, stringbuffer);

		buildIsNull(message, stringbuffer);

		String s8 = stringbuffer.toString();
		String s10 = (new StringBuilder()).append(outputDir).append(message.getClassName().replaceAll("\\.", "/"))
				.append(".java").toString();
		try {
			FileUtil.saveAsData(s10, s8.getBytes(s), false);
		} catch (UnsupportedEncodingException unsupportedencodingexception) {
			unsupportedencodingexception.printStackTrace();
		}
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String s) {
		this.outputDir = s;
		if (!this.outputDir.endsWith("/")) {
			this.outputDir = this.outputDir + "/";
		}
	}

	private String getType(Field field, Message message) {
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

//	static 
//	{
//		try
//		{
//			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties")))
//			{
//				Properties properties = ConfigManager.loadProperties("messagebean.properties");
//				if (null != properties.getProperty("oper_blank") && 0 != properties.getProperty("oper_blank").length() && "0".equalsIgnoreCase(properties.getProperty("oper_blank")))
//					operBlank = "0";
//			}
//		}
//		catch (Exception exception)
//		{
//			exception.printStackTrace();
//			ExceptionUtil.throwActualException(exception);
//		}
//	}
}
