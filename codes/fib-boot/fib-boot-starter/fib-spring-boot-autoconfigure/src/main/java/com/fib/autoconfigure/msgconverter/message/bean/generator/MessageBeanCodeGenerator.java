package com.fib.autoconfigure.msgconverter.message.bean.generator;

import java.io.File;
import java.util.Iterator;

import com.fib.autoconfigure.msgconverter.message.bean.MessageBean;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB;
import com.fib.autoconfigure.msgconverter.message.metadata.Field;
import com.fib.autoconfigure.msgconverter.message.metadata.Message;
import com.fib.commons.exception.CommonException;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;

public class MessageBeanCodeGenerator {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private String outputDir;
	private static String operBlank = "1";

	public void setOutputDir(String srcRootPath) {
		this.outputDir = srcRootPath;
		if (!this.outputDir.endsWith("/")) {
			this.outputDir += "/";
		}
	}

	public void generate(Message message, String charset) {
		Assert.notNull(message, () -> new CommonException("null."));
		String packageName = message.getClassName().substring(0, message.getClassName().lastIndexOf("."));
		String className = message.getClassName().substring(message.getClassName().lastIndexOf(".") + 1);
		StringBuilder localStringBuffer = new StringBuilder(10240);
		localStringBuffer.append("package ");
		localStringBuffer.append(packageName.toLowerCase());
		localStringBuffer.append(";");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("import ").append(MessageBean.class.getPackageName()).append(".").append(MessageBean.class.getSimpleName())
				.append(";");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("import java.math.BigDecimal;");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("import java.io.UnsupportedEncodingException;");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("import java.util.*;");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("/**");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append(" * ");
		localStringBuffer.append(message.getShortText());
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append(" */");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("public class ");
		localStringBuffer.append(CharSequenceUtil.upperFirst(className));
		localStringBuffer.append(" extends MessageBean{");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append(NEW_LINE);

//		Iterator<Field> localIterator1 = message.getFields().values().iterator();
//		Field localField1 = null;
//		String str3 = null;
//		String str4 = null;
//		while (localIterator1.hasNext()) {
//			localField1 = localIterator1.next();
//			if ((2002 == localField1.getFieldType()) || (2003 == localField1.getFieldType()) || (2004 == localField1.getFieldType())
//					|| (2011 == localField1.getFieldType()) || (2008 == localField1.getFieldType()) || (2009 == localField1.getFieldType())) {
//				if (localField1.getReference() != null) {
//					str3 = localField1.getReference().getClassName();
//				} else if (("dynamic".equalsIgnoreCase(localField1.getReferenceType()))
//						|| ("expression".equalsIgnoreCase(localField1.getReferenceType()))) {
//					str3 = MessageBean.class.getName();
//				} else {
//					str3 = localField1.getCombineOrTableFieldClassName();
//					if (null == str3) {
//						str3 = message.getClassName() + CharSequenceUtil.upperFirst(localField1.getName());
//					}
//					Message localMessage = new Message();
//					localMessage.setId(message.getId() + "-" + localField1.getName());
//					localMessage.setClassName(str3);
//					localMessage.setShortText(localField1.getShortText());
//					localMessage.setFields(localField1.getSubFields());
//					generate(localMessage, charset);
//				}
//			} else {
//				str3 = ConstantMB.getJavaTypeByDataType(localField1.getDataType());
//			}
//
//			localStringBuffer.append("\t//");
//			localStringBuffer.append(localField1.getShortText());
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append("\tprivate ");
//
//			if ((2004 == localField1.getFieldType()) || (2011 == localField1.getFieldType())) {
//				localStringBuffer.append("List");
//			} else {
//				localStringBuffer.append(str3);
//			}
//
//			localStringBuffer.append(" ");
//			localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//
//			if ((2004 == localField1.getFieldType()) || (2011 == localField1.getFieldType())) {
//				localStringBuffer.append(" = new ArrayList(20)");
//			} else if (((2002 == localField1.getFieldType()) || (2003 == localField1.getFieldType()) || (2008 == localField1.getFieldType())
//					|| (2009 == localField1.getFieldType())) && (!"dynamic".equalsIgnoreCase(localField1.getReferenceType()))
//					&& (!"expression".equalsIgnoreCase(localField1.getReferenceType()))) {
//				localStringBuffer.append(" = new ");
//				localStringBuffer.append(str3);
//				localStringBuffer.append("()");
//			} else if (localField1.getValue() != null) {
//				switch (localField1.getDataType()) {
//				case 3000:
//				case 3001:
//				case 3006:
//				case 3010:
//				case 3011:
//					localStringBuffer.append(" = \"");
//					localStringBuffer.append(localField1.getValue());
//					localStringBuffer.append("\"");
//					break;
//				case 3002:
//					localStringBuffer.append(" = CodeUtil.HextoByte(\"");
//					localStringBuffer.append(localField1.getValue());
//					localStringBuffer.append("\")");
//					break;
//				case 3003:
//				case 3007:
//					localStringBuffer.append(" = ");
//					localStringBuffer.append(localField1.getValue());
//					break;
//				case 3009:
//					localStringBuffer.append(" = ");
//					localStringBuffer.append(localField1.getValue());
//					localStringBuffer.append("l");
//					break;
//				case 3004:
//				case 3005:
//				case 3008:
//				default:
//					localStringBuffer.append(" = (");
//					localStringBuffer.append(ConstantMB.getJavaTypeByDataType(localField1.getDataType()));
//					localStringBuffer.append(")");
//					localStringBuffer.append(localField1.getValue());
//				}
//			}
//
//			localStringBuffer.append(";");
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append("\tpublic ");
//
//			if ((2004 == localField1.getFieldType()) || (2011 == localField1.getFieldType())) {
//				localStringBuffer.append("List");
//			} else {
//				localStringBuffer.append(str3);
//			}
//
//			localStringBuffer.append(" get");
//			localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//			localStringBuffer.append("(){");
//			localStringBuffer.append(NEW_LINE);
//
//			if ((localField1.isShield())
//					&& ((3000 == localField1.getDataType()) || (3001 == localField1.getDataType()) || (3002 == localField1.getDataType())
//							|| (3004 == localField1.getDataType()) || (3006 == localField1.getDataType()) || (3010 == localField1.getDataType()))
//					&& ((2000 == localField1.getFieldType()) || (2001 == localField1.getFieldType()) || (2005 == localField1.getFieldType())
//							|| (2006 == localField1.getFieldType()) || (2007 == localField1.getFieldType())
//							|| (2010 == localField1.getFieldType()))) {
//				localStringBuffer.append("\t\tif( null != ");
//				localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//				localStringBuffer.append("&&  isShield() ) {");
//				localStringBuffer.append(NEW_LINE);
//				if (3004 == localField1.getDataType()) {
//					localStringBuffer.append("return 0x2A;");
//					localStringBuffer.append(NEW_LINE);
//				} else {
//					localStringBuffer.append("\t\t\t");
//					localStringBuffer.append("int valueLen = ");
//					if (3002 == localField1.getDataType()) {
//						localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//						localStringBuffer.append(".length;");
//					} else {
//						localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//						localStringBuffer.append(".length();");
//					}
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\t");
//					localStringBuffer.append("StringBuffer buf = new StringBuffer(valueLen);");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\t");
//					localStringBuffer.append("for( int i =0; i < valueLen; i++){ ");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\t\t");
//					localStringBuffer.append("buf.append(\"*\");");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\t");
//					localStringBuffer.append("}");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\t");
//					if (3002 == localField1.getDataType())
//						localStringBuffer.append("return buf.toString().getBytes();");
//					else
//						localStringBuffer.append("return buf.toString();");
//					localStringBuffer.append(NEW_LINE);
//				}
//				localStringBuffer.append("\t\t} else { ");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t\t");
//			}
//			localStringBuffer.append("\t\treturn ");
//			localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//			localStringBuffer.append(";");
//			localStringBuffer.append(NEW_LINE);
//			if ((localField1.isShield())
//					&& ((3000 == localField1.getDataType()) || (3001 == localField1.getDataType()) || (3002 == localField1.getDataType())
//							|| (3004 == localField1.getDataType()) || (3006 == localField1.getDataType()) || (3010 == localField1.getDataType()))
//					&& ((2000 == localField1.getFieldType()) || (2001 == localField1.getFieldType()) || (2005 == localField1.getFieldType())
//							|| (2006 == localField1.getFieldType()) || (2007 == localField1.getFieldType())
//							|| (2010 == localField1.getFieldType()))) {
//				localStringBuffer.append("\t\t}");
//				localStringBuffer.append(NEW_LINE);
//			}
//			localStringBuffer.append("\t}");
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append(NEW_LINE);
//			if (((2004 == localField1.getFieldType()) || (2011 == localField1.getFieldType()))
//					&& (!"dynamic".equalsIgnoreCase(localField1.getReferenceType()))
//					&& (!"expression".equalsIgnoreCase(localField1.getReferenceType()))) {
//				localStringBuffer.append("\tpublic ");
//				localStringBuffer.append(str3);
//				localStringBuffer.append(" get");
//				localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//				localStringBuffer.append("At(int index){");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t\treturn (");
//				localStringBuffer.append(str3);
//				localStringBuffer.append(")");
//				localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//				localStringBuffer.append(".get(index);");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t}");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\tpublic int get");
//				localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//				localStringBuffer.append("RowNum(){");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t\treturn ");
//				localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//				localStringBuffer.append(".size();");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t}");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append(NEW_LINE);
//			}
//
//			if (localField1.isEditable()) {
//				localStringBuffer.append("\tpublic void set");
//				localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//				localStringBuffer.append("(");
//				if ((2004 == localField1.getFieldType()) || (2011 == localField1.getFieldType()))
//					localStringBuffer.append("List");
//				else
//					localStringBuffer.append(str3);
//				localStringBuffer.append(" ");
//				localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//				localStringBuffer.append("){");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t\tthis.");
//				localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//				localStringBuffer.append(" = ");
//				localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//				localStringBuffer.append(";");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append("\t}");
//				localStringBuffer.append(NEW_LINE);
//				localStringBuffer.append(NEW_LINE);
//				if (((2004 == localField1.getFieldType()) || (2011 == localField1.getFieldType()))
//						&& (!"dynamic".equalsIgnoreCase(localField1.getReferenceType()))
//						&& (!"expression".equalsIgnoreCase(localField1.getReferenceType()))) {
//					localStringBuffer.append("\tpublic ");
//					localStringBuffer.append(str3);
//					localStringBuffer.append(" create");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("(){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t");
//					localStringBuffer.append(str3);
//					localStringBuffer.append(" newRecord = new ");
//					localStringBuffer.append(str3);
//					localStringBuffer.append("();");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t");
//					localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//					localStringBuffer.append(".add(newRecord);");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\treturn newRecord;");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t}");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\tpublic void add");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("(");
//					localStringBuffer.append(str3);
//					localStringBuffer.append(" newRecord){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t");
//					localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//					localStringBuffer.append(".add(newRecord);");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t}");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\tpublic void set");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("At(int index, ");
//					localStringBuffer.append(str3);
//					localStringBuffer.append(" newRecord){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t");
//					localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//					localStringBuffer.append(".set(index, newRecord);");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t}");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\tpublic ");
//					localStringBuffer.append(str3);
//					localStringBuffer.append(" remove");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("At(int index){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\treturn (");
//					localStringBuffer.append(str3);
//					localStringBuffer.append(")");
//					localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//					localStringBuffer.append(".remove(index);");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t}");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\tpublic void clear");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("(){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t");
//					localStringBuffer.append(CharSequenceUtil.lowerFirst(localField1.getName()));
//					localStringBuffer.append(".clear();");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t}");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append(NEW_LINE);
//				}
//			}
//		}
//
//		localStringBuffer.append("\tpublic Object getAttribute(String name){");
//		localStringBuffer.append(NEW_LINE);
//		localIterator1 = message.getFields().values().iterator();
//
//		if (localIterator1.hasNext()) {
//			localField1 = (Field) localIterator1.next();
//			localStringBuffer.append("\t\tif(");
//			localStringBuffer.append("\"");
//			localStringBuffer.append(localField1.getName());
//			localStringBuffer.append("\"");
//			localStringBuffer.append(".equals(name)){");
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append("\t\t\treturn  ");
//			localStringBuffer.append(getType(localField1, message));
//			localStringBuffer.append(";");
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append("\t\t}");
//		}
//
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t\treturn null;");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t}");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append(NEW_LINE);
//
//		localStringBuffer.append("\tpublic void setAttribute(String name,Object value){");
//		localStringBuffer.append(NEW_LINE);
//		localIterator1 = message.getFields().values().iterator();
//		int i = 1;
//		while (localIterator1.hasNext()) {
//			localField1 = (Field) localIterator1.next();
//			if (localField1.isEditable())
//				if (i != 0) {
//					localStringBuffer.append("\t\tif(");
//					localStringBuffer.append("\"");
//					localStringBuffer.append(localField1.getName());
//					localStringBuffer.append("\"");
//					localStringBuffer.append(".equals(name)){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\tthis.set");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("(");
//					localStringBuffer.append(getSetAttType(localField1, message));
//					localStringBuffer.append(")");
//					localStringBuffer.append(";");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t}");
//					i = 0;
//				} else {
//					localStringBuffer.append("else if(");
//					localStringBuffer.append("\"");
//					localStringBuffer.append(localField1.getName());
//					localStringBuffer.append("\"");
//					localStringBuffer.append(".equals(name)){");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t\tthis.set");
//					localStringBuffer.append(CharSequenceUtil.upperFirst(localField1.getName()));
//					localStringBuffer.append("(");
//					localStringBuffer.append(getSetAttType(localField1, message));
//					localStringBuffer.append(")");
//					localStringBuffer.append(";");
//					localStringBuffer.append(NEW_LINE);
//					localStringBuffer.append("\t\t}");
//				}
//		}
//
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t}");
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("\tpublic void validate(){");
		localStringBuffer.append(NEW_LINE);
//
//		localIterator1 = message.getFields().values().iterator();
//		while (localIterator1.hasNext()) {
//			localField1 = localIterator1.next();
//			buildValidate(localField1, localStringBuffer);
//		}
		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("\t}");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\tpublic String toString(){");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t\treturn toString(false);");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t}");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\tpublic String toString(boolean isWrap){");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t\treturn toString(isWrap,false);");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t}");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\tpublic String toString(boolean isWrap,boolean isTable){");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t\tStringBuffer buf = new StringBuffer(10240);");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t\tStringBuffer tableBuf = new StringBuffer(2048);");
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("\t\tString str = null;");
//		localStringBuffer.append(NEW_LINE);
//		localIterator1 = message.getFields().values().iterator();
//		while (localIterator1.hasNext()) {
//			localField1 = localIterator1.next();
//			localStringBuffer.append(NEW_LINE);
//			localStringBuffer.append("\t}");
//			localStringBuffer.append(NEW_LINE);
//		}
//
//		localStringBuffer.append(NEW_LINE);
//		localStringBuffer.append("}");

		localStringBuffer.append(NEW_LINE);
		localStringBuffer.append("}");
		localStringBuffer.append(NEW_LINE);
		System.out.println(localStringBuffer);

		String fileName = this.outputDir + message.getClassName().replaceAll("\\.", "/") + ".java";
		System.out.println("fileName=" + fileName);
		File d = FileUtil.writeString(localStringBuffer.toString(), fileName, charset);
		boolean f = d.exists();
		System.out.println(f);
	}

	private void buildValidate(Field localField1, StringBuilder localStringBuffer) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		MessageBeanCodeGenerator test = new MessageBeanCodeGenerator();
		Message message = new Message();
		message.setShortText("测试MessageBean");
		message.setClassName("com.fib.message.bean.TestMessageBean");

		Field field = new Field();
		field.setName("name");
		field.setFieldType(ConstantMB.FieldType.FIXED_FIELD.getCode());
		field.setDataType(ConstantMB.DataType.STR.getCode());
		field.setShortText("名字");

		message.setField(field.getName(), field);

		test.setOutputDir("outsrc");
		test.generate(message, "UTF-8");

	}

	private String getType(Field paramField, Message paramMessage) {
		String str = null;
		if (0 != paramField.getDataType()) {
			str = ConstantMB.getJavaTypeByDataType(paramField.getDataType());
			if ("int".equals(str))
				return "Integer.valueOf(this.get" + CharSequenceUtil.upperFirst(paramField.getName()) + "())";
			if ("long".equals(str))
				return "Long.valueOf(this.get" + CharSequenceUtil.upperFirst(paramField.getName()) + "())";
			if ("byte".equals(str))
				return "Byte.valueOf(this.get" + CharSequenceUtil.upperFirst(paramField.getName()) + "())";
			if ("short".equals(str))
				return "Short.valueOf(this.get" + CharSequenceUtil.upperFirst(paramField.getName()) + "())";
			return "this.get" + CharSequenceUtil.upperFirst(paramField.getName()) + "()";
		}
		return "this.get" + CharSequenceUtil.upperFirst(paramField.getName()) + "()";
	}

	private String getSetAttType(Field paramField, Message paramMessage) {
		String str1 = null;
		String str2 = null;
		if ((2002 == paramField.getFieldType()) || (2003 == paramField.getFieldType()) || (2004 == paramField.getFieldType())
				|| (2011 == paramField.getFieldType()) || (2008 == paramField.getFieldType()) || (2009 == paramField.getFieldType())) {
			if (paramField.getReference() != null) {
				str1 = paramField.getReference().getClassName();
			} else if (("dynamic".equalsIgnoreCase(paramField.getReferenceType()))
					|| ("expression".equalsIgnoreCase(paramField.getReferenceType()))) {
				str1 = MessageBean.class.getName();
			} else {
				str1 = paramField.getCombineOrTableFieldClassName();
				if (null == str1)
					str1 = paramMessage.getClassName() + CharSequenceUtil.upperFirst(paramField.getName());
			}
			str2 = "(" + str1 + ")value";
		} else {
			str1 = ConstantMB.getJavaTypeByDataType(paramField.getDataType());
			if ("int".equals(str1))
				str2 = "((Integer)value).intValue()";
			else if ("long".equals(str1))
				str2 = "((Long)value).longValue()";
			else if ("byte".equals(str1))
				str2 = "((Byte)value).byteValue()";
			else if ("short".equals(str1))
				str2 = "((Short)value).shortValue()";
			else
				str2 = "(" + str1 + ")value";
		}
		if ((2004 == paramField.getFieldType()) || (2011 == paramField.getFieldType())) {
			str1 = "List";
			str2 = "(" + str1 + ")value";
		}
		return str2;
	}
}
