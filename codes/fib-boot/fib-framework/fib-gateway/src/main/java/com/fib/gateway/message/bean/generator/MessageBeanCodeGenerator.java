package com.fib.gateway.message.bean.generator;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.message.bean.generator.FileUtil;
import com.fib.gateway.message.xml.message.bean.generator.StringUtil;

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
	public static String operBlank = "1";

	public void generate(Message var1, String var2) {
		if (null == var1) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageMeta" }));
		} else {
			String var3 = var1.getClassName().substring(0, var1.getClassName().lastIndexOf("."));
			String var4 = var1.getClassName().substring(var1.getClassName().lastIndexOf(".") + 1);
			StringBuffer var5 = new StringBuffer(10240);
			new HashMap();
			var5.append("package ");
			var5.append(var3.toLowerCase());
			var5.append(";");
			var5.append(NEW_LINE);
			var5.append(NEW_LINE);
			var5.append("import com.fib.gateway.message.bean.*;");
			var5.append(NEW_LINE);
			var5.append("import com.fib.gateway.message.xml.message.*;");
			var5.append(NEW_LINE);
			var5.append("import com.fib.gateway.message.*;");
			var5.append(NEW_LINE);

			var5.append("import java.math.BigDecimal;");
			var5.append(NEW_LINE);
			var5.append("import java.io.UnsupportedEncodingException;");
			var5.append(NEW_LINE);
			var5.append("import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;");
			var5.append(NEW_LINE);
			var5.append("import java.util.*;");
			var5.append(NEW_LINE);
			var5.append(NEW_LINE);
			var5.append("/**");
			var5.append(NEW_LINE);
			var5.append(" * ");
			var5.append(var1.getShortText());
			var5.append(NEW_LINE);
			var5.append(" */");
			var5.append(NEW_LINE);
			var5.append("public class ");
			var5.append(StringUtil.toUpperCaseFirstOne(var4));
			var5.append(" extends MessageBean{");
			var5.append(NEW_LINE);
			var5.append(NEW_LINE);
			Iterator var7 = var1.getFields().values().iterator();
			Iterator var8 = null;
			Field var9 = null;
			Field var10 = null;
			String var11 = null;
			String var12 = null;

			while (true) {
				do {
					do {
						if (!var7.hasNext()) {
							var5.append("\tpublic Object getAttribute(String name){");
							var5.append(NEW_LINE);
							var7 = var1.getFields().values().iterator();
							if (var7.hasNext()) {
								var9 = (Field) var7.next();
								var5.append("\t\tif(");
								var5.append("\"");
								var5.append(var9.getName());
								var5.append("\"");
								var5.append(".equals(name)){");
								var5.append(NEW_LINE);
								var5.append("\t\t\treturn  ");
								var5.append(this.getType(var9, var1));
								var5.append(";");
								var5.append(NEW_LINE);
								var5.append("\t\t}");
							}

							while (var7.hasNext()) {
								var9 = (Field) var7.next();
								var5.append("else if(");
								var5.append("\"");
								var5.append(var9.getName());
								var5.append("\"");
								var5.append(".equals(name)){");
								var5.append(NEW_LINE);
								var5.append("\t\t\treturn  ");
								var5.append(this.getType(var9, var1));
								var5.append(";");
								var5.append(NEW_LINE);
								var5.append("\t\t}");
							}

							var5.append(NEW_LINE);
							var5.append("\t\treturn null;");
							var5.append(NEW_LINE);
							var5.append("\t}");
							var5.append(NEW_LINE);
							var5.append(NEW_LINE);
							var5.append("\tpublic void setAttribute(String name,Object value){");
							var5.append(NEW_LINE);
							var7 = var1.getFields().values().iterator();
							boolean var18 = true;

							while (var7.hasNext()) {
								var9 = (Field) var7.next();
								if (var9.isEditable()) {
									if (var18) {
										var5.append("\t\tif(");
										var5.append("\"");
										var5.append(var9.getName());
										var5.append("\"");
										var5.append(".equals(name)){");
										var5.append(NEW_LINE);
										var5.append("\t\t\tthis.set");
										var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
										var5.append("(");
										var5.append(this.getSetAttType(var9, var1));
										var5.append(")");
										var5.append(";");
										var5.append(NEW_LINE);
										var5.append("\t\t}");
										var18 = false;
									} else {
										var5.append("else if(");
										var5.append("\"");
										var5.append(var9.getName());
										var5.append("\"");
										var5.append(".equals(name)){");
										var5.append(NEW_LINE);
										var5.append("\t\t\tthis.set");
										var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
										var5.append("(");
										var5.append(this.getSetAttType(var9, var1));
										var5.append(")");
										var5.append(";");
										var5.append(NEW_LINE);
										var5.append("\t\t}");
									}
								}
							}

							var5.append(NEW_LINE);
							var5.append("\t}");
							var5.append(NEW_LINE);
							var5.append(NEW_LINE);
							var5.append("\tpublic void cover(MessageBean bean){");
							var5.append(NEW_LINE);
							var5.append("\t\t");
							var5.append(StringUtil.toUpperCaseFirstOne(var4));
							var5.append(" newBean = ");
							var5.append("(");
							var5.append(StringUtil.toUpperCaseFirstOne(var4));
							var5.append(") bean;");
							var5.append(NEW_LINE);
							var7 = var1.getFields().values().iterator();

							while (true) {
								String var14;
								label1371: do {
									while (true) {
										while (true) {
											String var15;
											do {
												do {
													do {
														do {
															if (!var7.hasNext()) {
																var5.append(NEW_LINE);
																var5.append("\t}");
																var5.append(NEW_LINE);
																var5.append(NEW_LINE);
																var5.append("\tpublic void validate(){");
																var5.append(NEW_LINE);
																var7 = var1.getFields().values().iterator();

																while (true) {
																	HashMap var6;
																	do {
																		do {
																			do {
																				do {
																					do {
																						do {
																							do {
																								do {
																									if (!var7
																											.hasNext()) {
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\tpublic String toString(){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\treturn toString(false);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\tpublic String toString(boolean isWrap){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\treturn toString(isWrap,false);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\tpublic String toString(boolean isWrap,boolean isTable){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\tStringBuffer buf = new StringBuffer(10240);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\tStringBuffer tableBuf = new StringBuffer(2048);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\tString str = null;");
																										var5.append(
																												NEW_LINE);
																										var7 = var1
																												.getFields()
																												.values()
																												.iterator();

																										while (true) {
																											label989: do {
																												while (true) {
																													while (true) {
																														do {
																															if (!var7
																																	.hasNext()) {
																																var5.append(
																																		"\t\tif( 0 == buf.length()){");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\treturn null;");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t}else{");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\tif ( isTable ){");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t\tbuf.insert(0,\"<b>\");");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t}else{");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t\tbuf.insert(0,\"<b c=\\\"");
																																var5.append(
																																		var1.getClassName());
																																var5.append(
																																		"\\\">\");");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t}");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\tbuf.append(\"</b>\");");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\tif( !isWrap ){");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t\tbuf = new StringBuffer(buf.toString());");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t\tbuf.insert(0,\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\");");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t\treturn buf.toString();");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t}else{");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t\treturn buf.toString();");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\t}");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t}");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t}");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\tpublic boolean isNull(){");
																																var5.append(
																																		NEW_LINE);
																																var7 = var1
																																		.getFields()
																																		.values()
																																		.iterator();

																																while (true) {
																																	label940: do {
																																		while (true) {
																																			while (var7
																																					.hasNext()) {
																																				var9 = (Field) var7
																																						.next();
																																				if (2002 != var9
																																						.getFieldType()
																																						&& 2003 != var9
																																								.getFieldType()
																																						&& 2008 != var9
																																								.getFieldType()
																																						&& 2009 != var9
																																								.getFieldType()) {
																																					if (2004 != var9
																																							.getFieldType()
																																							&& 2011 != var9
																																									.getFieldType()) {
																																						continue label940;
																																					}

																																					if (var9.getReference() != null) {
																																						var11 = var9
																																								.getReference()
																																								.getClassName();
																																					} else if (!"dynamic"
																																							.equalsIgnoreCase(
																																									var9.getReferenceType())
																																							&& !"expression"
																																									.equalsIgnoreCase(
																																											var9.getReferenceType())) {
																																						var11 = var9
																																								.getCombineOrTableFieldClassName();
																																						if (null == var11) {
																																							var11 = var1
																																									.getClassName()
																																									+ StringUtil
																																											.toUpperCaseFirstOne(
																																													var9.getName());
																																						}
																																					} else {
																																						var11 = MessageBean.class
																																								.getName();
																																					}

																																					var5.append(
																																							"\t\t");
																																					var5.append(
																																							var11);
																																					var5.append(
																																							" ");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							"Field = null;");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\tfor( int i =0; i < ");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							".size(); i++) {");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\t");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							"Field = (");
																																					var5.append(
																																							var11);
																																					var5.append(
																																							") ");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							".get(i);");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\tif ( null != ");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							" && !");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							"Field.isNull() ){");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\t\treturn false;");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\t}");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t}");
																																					var5.append(
																																							NEW_LINE);
																																				} else {
																																					var5.append(
																																							"\t\tif( null != ");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							"){");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\tif (  !");
																																					var5.append(
																																							StringUtil
																																									.toLowerCaseFirstOne(
																																											var9.getName()));
																																					var5.append(
																																							".isNull() ){");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\t\treturn false;");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t\t}");
																																					var5.append(
																																							NEW_LINE);
																																					var5.append(
																																							"\t\t}");
																																					var5.append(
																																							NEW_LINE);
																																				}
																																			}

																																			var5.append(
																																					"\t\treturn true;");
																																			var5.append(
																																					NEW_LINE);
																																			var5.append(
																																					"\t}");
																																			var5.append(
																																					NEW_LINE);
																																			var5.append(
																																					"}");
																																			var5.append(
																																					NEW_LINE);
																																			var14 = var5
																																					.toString();
																																			var15 = this.outputDir
																																					+ var1.getClassName()
																																							.replaceAll(
																																									"\\.",
																																									"/")
																																					+ ".java";

																																			try {
																																				FileUtil.saveAsData(
																																						var15,
																																						var14.getBytes(
																																								var2),
																																						false);
																																			} catch (UnsupportedEncodingException var17) {
																																				var17.printStackTrace();
																																			}

																																			return;
																																		}
																																	} while (3000 != var9
																																			.getDataType()
																																			&& 3001 != var9
																																					.getDataType()
																																			&& 3006 != var9
																																					.getDataType()
																																			&& 3002 != var9
																																					.getDataType()
																																			&& 3010 != var9
																																					.getDataType());

																																	var5.append(
																																			"\t\tif( null != ");
																																	var5.append(
																																			StringUtil
																																					.toLowerCaseFirstOne(
																																							var9.getName()));
																																	if ("0" == operBlank) {
																																		var5.append(
																																				" && 0 !=");
																																		var5.append(
																																				StringUtil
																																						.toLowerCaseFirstOne(
																																								var9.getName()));
																																		if (3002 == var9
																																				.getDataType()) {
																																			var5.append(
																																					".length ");
																																		} else {
																																			var5.append(
																																					".length() ");
																																		}
																																	}

																																	var5.append(
																																			"){");
																																	var5.append(
																																			NEW_LINE);
																																	var5.append(
																																			"\t\t\treturn false;");
																																	var5.append(
																																			NEW_LINE);
																																	var5.append(
																																			"\t\t}");
																																	var5.append(
																																			NEW_LINE);
																																}
																															}

																															var9 = (Field) var7
																																	.next();
																														} while (!var9
																																.isEditable());

																														if (2002 != var9
																																.getFieldType()
																																&& 2003 != var9
																																		.getFieldType()
																																&& 2008 != var9
																																		.getFieldType()
																																&& 2009 != var9
																																		.getFieldType()) {
																															if (2004 != var9
																																	.getFieldType()
																																	&& 2011 != var9
																																			.getFieldType()) {
																																if (3000 == var9
																																		.getDataType()
																																		|| 3001 == var9
																																				.getDataType()
																																		|| 3006 == var9
																																				.getDataType()
																																		|| 3002 == var9
																																				.getDataType()
																																		|| 3010 == var9
																																				.getDataType()) {
																																	var5.append(
																																			"\t\tif( null != ");
																																	var5.append(
																																			StringUtil
																																					.toLowerCaseFirstOne(
																																							var9.getName()));
																																	var5.append(
																																			"){");
																																}

																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\tbuf.append(\"<a n=\\\"");
																																var5.append(
																																		StringUtil
																																				.toLowerCaseFirstOne(
																																						var9.getName()));
																																var5.append(
																																		"\\\" t=\\\"");
																																var5.append(
																																		Constant.getDataTypeText(
																																				var9.getDataType()));
																																var5.append(
																																		"\\\">\");");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\tbuf.append(");
																																if (3002 == var9
																																		.getDataType()) {
																																	var5.append(
																																			"new String(CodeUtil.BytetoHex(");
																																	var5.append(
																																			StringUtil
																																					.toLowerCaseFirstOne(
																																							var9.getName()));
																																	var5.append(
																																			"))");
																																} else {
																																	var5.append(
																																			StringUtil
																																					.toLowerCaseFirstOne(
																																							var9.getName()));
																																}

																																var5.append(
																																		");");
																																var5.append(
																																		NEW_LINE);
																																var5.append(
																																		"\t\t\tbuf.append(\"</a>\");");
																																var5.append(
																																		NEW_LINE);
																																continue label989;
																															}

																															if (var9.getReference() != null) {
																																var11 = var9
																																		.getReference()
																																		.getClassName();
																															} else if (!"dynamic"
																																	.equalsIgnoreCase(
																																			var9.getReferenceType())
																																	&& !"expression"
																																			.equalsIgnoreCase(
																																					var9.getReferenceType())) {
																																var11 = var9
																																		.getCombineOrTableFieldClassName();
																																if (null == var11) {
																																	var11 = var1
																																			.getClassName()
																																			+ StringUtil
																																					.toUpperCaseFirstOne(
																																							var9.getName());
																																}
																															} else {
																																var11 = MessageBean.class
																																		.getName();
																															}

																															var5.append(
																																	"\t\t");
																															var5.append(
																																	var11);
																															var5.append(
																																	" ");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	"Field = null;");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\tfor( int i =0; i < ");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	".size(); i++) {");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	"Field = (");
																															var5.append(
																																	var11);
																															var5.append(
																																	") ");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	".get(i);");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tstr = ");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	"Field.toString(true,true);");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tif( null != str){");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t\ttableBuf.append(str);");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t}");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t}");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\tif ( 0 != tableBuf.length()){");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tbuf.append(\"<a n=\\\"");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	"\\\" t=\\\"list\\\" c=\\\"java.util.ArrayList\\\" rc=\\\"");
																															var5.append(
																																	var11);
																															var5.append(
																																	"\\\">\");");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tbuf.append(tableBuf);");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tbuf.append(\"</a>\");");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\ttableBuf.delete(0,tableBuf.length());");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t}");
																															var5.append(
																																	NEW_LINE);
																														} else {
																															var5.append(
																																	"\t\tif( null != ");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	"){");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tstr = ");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	".toString(true);");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\tif( null != str){");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t\tbuf.append(\"<a n=\\\"");
																															var5.append(
																																	StringUtil
																																			.toLowerCaseFirstOne(
																																					var9.getName()));
																															var5.append(
																																	"\\\" t=\\\"bean\\\">\");");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t\tbuf.append(str);");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t\tbuf.append(\"");
																															var5.append(
																																	"</a>\");");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t\t}");
																															var5.append(
																																	NEW_LINE);
																															var5.append(
																																	"\t\t}");
																															var5.append(
																																	NEW_LINE);
																														}
																													}
																												}
																											} while (3000 != var9
																													.getDataType()
																													&& 3001 != var9
																															.getDataType()
																													&& 3006 != var9
																															.getDataType()
																													&& 3002 != var9
																															.getDataType()
																													&& 3010 != var9
																															.getDataType());

																											var5.append(
																													"\t\t}");
																											var5.append(
																													NEW_LINE);
																										}
																									}

																									var9 = (Field) var7
																											.next();
																								} while (2007 == var9
																										.getFieldType());
																							} while (2005 == var9
																									.getFieldType());
																						} while (2006 == var9
																								.getFieldType());
																					} while (!var9.isEditable());

																					if (var9.isRequired()
																							&& 3003 != var9
																									.getDataType()
																							&& 3007 != var9
																									.getDataType()
																							&& 3004 != var9
																									.getDataType()
																							&& 3005 != var9
																									.getDataType()
																							&& 3008 != var9
																									.getDataType()
																							&& 2000 != var9
																									.getFieldType()) {
																						var5.append("\t\t//");
																						var5.append(
																								var9.getShortText());
																						var5.append("闈炵┖妫�鏌�");
																						var5.append(NEW_LINE);
																						if (2002 != var9.getFieldType()
																								&& 2008 != var9
																										.getFieldType()
																								&& 2003 != var9
																										.getFieldType()
																								&& 2009 != var9
																										.getFieldType()) {
																							if (2004 != var9
																									.getFieldType()
																									&& 2011 != var9
																											.getFieldType()) {
																								var5.append(
																										"\t\tif ( null == ");
																								var5.append(StringUtil
																										.toLowerCaseFirstOne(
																												var9.getName()));
																							} else {
																								var5.append(
																										"\t\tif (null == ");
																								var5.append(StringUtil
																										.toLowerCaseFirstOne(
																												var9.getName()));
																								var5.append(" || ");
																								var5.append(" 0 == ");
																								var5.append(StringUtil
																										.toLowerCaseFirstOne(
																												var9.getName()));
																								var5.append(".size() ");
																							}
																						} else {
																							var5.append(
																									"\t\tif ( null == ");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(" || ");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(".isNull() ");
																						}

																						var5.append("){");
																						var5.append(NEW_LINE);
																						var5.append(
																								"\t\t\tthrow new RuntimeException(");
																						var5.append(NEW_LINE);
																						var5.append("\t\t\t\t");
																						var5.append(
																								"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.null\", new String[]{\"");
																						if (null != var9
																								.getShortText()) {
																							var5.append(var9
																									.getShortText());
																							var5.append("(");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(")");
																						} else {
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																						}

																						var5.append("\"}));");
																						var5.append(NEW_LINE);
																						var5.append("\t\t}");
																						var5.append(NEW_LINE);
																						var5.append(NEW_LINE);
																					}

																					if (2002 != var9.getFieldType()
																							&& 2003 != var9
																									.getFieldType()
																							&& 2008 != var9
																									.getFieldType()
																							&& 2009 != var9
																									.getFieldType()) {
																						if (2004 != var9.getFieldType()
																								&& 2011 != var9
																										.getFieldType()) {
																							String[] var20;
																							if (2000 == var9
																									.getFieldType()) {
																								var11 = Constant
																										.getJavaTypeByDataType(
																												var9.getDataType());
																								if ("String"
																										.equals(var11)
																										|| "byte[]"
																												.equals(var11)) {
																									if (var9.isRequired()) {
																										var5.append(
																												"\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"闈炵┖妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\tif ( null == ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.null\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\"}));");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									} else {
																										var5.append(
																												"\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"涓嶄负绌哄垯鎸変互涓嬭鍒欐牎楠�");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\tif( null != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("0" == operBlank) {
																											var5.append(
																													" && 0!= ");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("String"
																													.equals(var11)) {
																												var5.append(
																														".length()");
																											} else {
																												var5.append(
																														".length");
																											}
																										}

																										var5.append(
																												"){");
																										var5.append(
																												NEW_LINE);
																									}

																									if (var9.getLength() != -1) {
																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"鏁版嵁闀垮害妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										if ("String"
																												.equals(var11)
																												&& (null != var9
																														.getDataCharset()
																														|| null != var1
																																.getMsgCharset())) {
																											var5.append(
																													"\t\t\ttry{");
																											var5.append(
																													NEW_LINE);
																										}

																										var5.append(
																												"\t\t\tif ( ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("String"
																												.equals(var11)) {
																											if (null != var9
																													.getDataCharset()) {
																												var5.append(
																														".getBytes(\""
																																+ var9.getDataCharset()
																																+ "\").length ");
																											} else if (null != var1
																													.getMsgCharset()) {
																												var5.append(
																														".getBytes(\""
																																+ var1.getMsgCharset()
																																+ "\").length");
																											} else {
																												var5.append(
																														".getBytes().length ");
																											}
																										}

																										if ("byte[]"
																												.equals(var11)) {
																											var5.append(
																													".length ");
																										}

																										if (!var9
																												.isStrictDataLength()
																												&& 3002 != var9
																														.getDataType()) {
																											var5.append(
																													"> ");
																										} else {
																											var5.append(
																													"!= ");
																										}

																										var5.append(var9
																												.getLength());
																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"");
																										if (!var9
																												.isStrictDataLength()
																												&& 3002 != var9
																														.getDataType()) {
																											var5.append(
																													"Message.field.maxLength");
																										} else {
																											var5.append(
																													"Message.field.length");
																										}

																										var5.append(
																												"\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\", \"");
																										var5.append(var9
																												.getLength());
																										var5.append(
																												"\"");
																										var5.append(
																												"}));");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										if ("String"
																												.equals(var11)
																												&& (null != var9
																														.getDataCharset()
																														|| null != var1
																																.getMsgCharset())) {
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}catch (UnsupportedEncodingException e) {");
																											var5.append(
																													NEW_LINE);
																											if (null != var9
																													.getDataCharset()) {
																												var5.append(
																														"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
																												if (null != var9
																														.getShortText()) {
																													var5.append(
																															var9.getShortText());
																													var5.append(
																															"(");
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																													var5.append(
																															")");
																												} else {
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																												}

																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getDataCharset());
																												var5.append(
																														"\"}));");
																											} else {
																												var5.append(
																														"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
																												var5.append(
																														var1.getId());
																												var5.append(
																														"\", \"");
																												var5.append(
																														var1.getMsgCharset());
																												var5.append(
																														"\"}));");
																											}

																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}");
																										}

																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}

																									if (3010 == var9
																											.getDataType()) {
																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"杈撳叆绫诲瀷妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\tBigDecimal big");
																										var5.append(
																												StringUtil
																														.toUpperCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												" = null;");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\ttry{");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tbig");
																										var5.append(
																												StringUtil
																														.toUpperCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												" = new BigDecimal(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												");");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t} catch (Exception e) {");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustBigDecimal\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\"}) + e.getMessage(), e);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										var5.append(
																												NEW_LINE);
																										var20 = var9
																												.getPattern()
																												.split(",");
																										var5.append(
																												"\t\t\tif ( ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												".indexOf(\".\") == -1 && ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												".length() > ");
																										if (var20.length > 1) {
																											var5.append(
																													Integer.parseInt(
																															var20[0])
																															- Integer
																																	.parseInt(
																																			var20[1]));
																										} else {
																											var5.append(
																													Integer.parseInt(
																															var20[0]));
																										}

																										var5.append(
																												"){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.wrong\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\", \"");
																										var5.append(var9
																												.getPattern());
																										var5.append(
																												"\"}));");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}else{");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tif( -1 != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												".indexOf(\".\")){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\tif ( ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												".substring(0, ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												".indexOf(\".\")).length() > ");
																										if (var20.length > 1) {
																											var5.append(
																													Integer.parseInt(
																															var20[0])
																															- Integer
																																	.parseInt(
																																			var20[1]));
																										} else {
																											var5.append(
																													Integer.parseInt(
																															var20[0]));
																										}

																										var5.append(
																												"){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.wrong\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\", \"");
																										var5.append(var9
																												.getPattern());
																										var5.append(
																												"\"}));");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												" = big");
																										var5.append(
																												StringUtil
																														.toUpperCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												".divide(new BigDecimal(1), ");
																										if (var20.length > 1) {
																											var5.append(
																													var20[1]);
																										} else {
																											var5.append(
																													"0");
																										}

																										var5.append(
																												", BigDecimal.ROUND_HALF_UP).toString();");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}

																									if (3001 == var9
																											.getDataType()) {
																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"杈撳叆绫诲瀷妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\tif(!CodeUtil.isNumeric(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustNum\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\"}));");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}

																									if (3006 == var9
																											.getDataType()) {
																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"鏍煎紡妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\ttry{");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tjava.text.DateFormat dateformat = new java.text.SimpleDateFormat(\"");
																										var5.append(var9
																												.getPattern());
																										var5.append(
																												"\");");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tdateformat.setLenient(false);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tdateformat.parse(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												");");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t} catch (Exception e) {");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\", \"");
																										var5.append(var9
																												.getPattern());
																										var5.append(
																												"\"}) + e.getMessage(), e);");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}

																									if (3000 == var9
																											.getDataType()
																											&& null != var9
																													.getPattern()) {
																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"鏍煎紡妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\tjava.util.regex.Pattern ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"Pattern = java.util.regex.Pattern.compile(\"");
																										var5.append(var9
																												.getPattern());
																										var5.append(
																												"\");");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\tjava.util.regex.Matcher ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"Matcher = ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"Pattern.matcher(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												");");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\tif ( !");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"Matcher.matches() ){");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
																										if (null != var9
																												.getShortText()) {
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"(");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													")");
																										} else {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																										}

																										var5.append(
																												"\", \"");
																										var5.append(var9
																												.getPattern());
																										var5.append(
																												"\"}));");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										var5.append(
																												NEW_LINE);
																									}

																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}
																								}
																							} else if (2001 == var9
																									.getFieldType()) {
																								var11 = Constant
																										.getJavaTypeByDataType(
																												var9.getDataType());
																								int var19;
																								if (var9.getRefLengthField() != null) {
																									var10 = var9
																											.getRefLengthField();
																									if (3001 != var10
																											.getDataType()
																											&& 3004 != var10
																													.getDataType()
																											|| !"String"
																													.equals(var11)
																													&& !"byte[]"
																															.equalsIgnoreCase(
																																	var11)) {
																										if (("String"
																												.equals(var11)
																												|| "byte[]"
																														.equalsIgnoreCase(
																																var11))
																												&& (var9.getMaxLength() != -1
																														|| var9.getMinLength() != -1)) {
																											if (!var9
																													.isRequired()) {
																												var5.append(
																														"\t\tif ( null != ");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												if ("0" == operBlank) {
																													var5.append(
																															" && 0 != ");
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																													if ("String"
																															.equals(var11)) {
																														var5.append(
																																".length()");
																													} else {
																														var5.append(
																																".length");
																													}
																												}

																												var5.append(
																														" ) {");
																												var5.append(
																														NEW_LINE);
																											}

																											var5.append(
																													"\t\t\t//");
																											var5.append(
																													var9.getShortText());
																											var5.append(
																													"闀垮害妫�鏌�");
																											var5.append(
																													NEW_LINE);
																											if ("String"
																													.equals(var11)
																													&& (null != var9
																															.getDataCharset()
																															|| null != var1
																																	.getMsgCharset())) {
																												var5.append(
																														"\t\t\ttry{");
																												var5.append(
																														NEW_LINE);
																											}

																											var5.append(
																													"\t\t\tif ( ");
																											if (var9.getMaxLength() != -1) {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												if ("String"
																														.equals(var11)) {
																													if (null != var9
																															.getDataCharset()) {
																														var5.append(
																																".getBytes(\""
																																		+ var9.getDataCharset()
																																		+ "\").length > ");
																													} else if (null != var1
																															.getMsgCharset()) {
																														var5.append(
																																".getBytes(\""
																																		+ var1.getMsgCharset()
																																		+ "\").length > ");
																													} else {
																														var5.append(
																																".getBytes().length > ");
																													}
																												} else if ("byte[]"
																														.equalsIgnoreCase(
																																var11)) {
																													var5.append(
																															".length > ");
																												}

																												var5.append(
																														var9.getMaxLength());
																											}

																											if (var9.getMaxLength() != -1
																													&& var9.getMinLength() != -1) {
																												var5.append(
																														" || ");
																											}

																											if (var9.getMinLength() != -1) {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												if ("String"
																														.equals(var11)) {
																													if (null != var9
																															.getDataCharset()) {
																														var5.append(
																																".getBytes(\""
																																		+ var9.getDataCharset()
																																		+ "\").length < ");
																													} else if (null != var1
																															.getMsgCharset()) {
																														var5.append(
																																".getBytes(\""
																																		+ var1.getMsgCharset()
																																		+ "\").length < ");
																													} else {
																														var5.append(
																																".getBytes().length < ");
																													}
																												} else if ("byte[]"
																														.equalsIgnoreCase(
																																var11)) {
																													var5.append(
																															".length < ");
																												}

																												var5.append(
																														var9.getMinLength());
																											}

																											var5.append(
																													" ) {");
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t\tthrow new RuntimeException(");
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t\t\t");
																											if (var9.getMaxLength() != -1
																													&& var9.getMinLength() != -1) {
																												var5.append(
																														"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getMaxLength());
																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getMinLength());
																												var5.append(
																														"\"}));");
																											} else if (var9
																													.getMaxLength() != -1) {
																												var5.append(
																														"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getMaxLength());
																												var5.append(
																														"\"}));");
																											} else if (var9
																													.getMinLength() != -1) {
																												var5.append(
																														"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.minLength\", new String[]{\"");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getMinLength());
																												var5.append(
																														"\"}));");
																											}

																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}");
																											if ("String"
																													.equals(var11)
																													&& (null != var9
																															.getDataCharset()
																															|| null != var1
																																	.getMsgCharset())) {
																												var5.append(
																														NEW_LINE);
																												var5.append(
																														"\t\t\t}catch (UnsupportedEncodingException e) {");
																												var5.append(
																														NEW_LINE);
																												if (null != var9
																														.getDataCharset()) {
																													var5.append(
																															"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
																													if (null != var9
																															.getShortText()) {
																														var5.append(
																																var9.getShortText());
																														var5.append(
																																"(");
																														var5.append(
																																StringUtil
																																		.toLowerCaseFirstOne(
																																				var9.getName()));
																														var5.append(
																																")");
																													} else {
																														var5.append(
																																StringUtil
																																		.toLowerCaseFirstOne(
																																				var9.getName()));
																													}

																													var5.append(
																															"\", \"");
																													var5.append(
																															var9.getDataCharset());
																													var5.append(
																															"\"}));");
																												} else {
																													var5.append(
																															"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
																													var5.append(
																															var1.getId());
																													var5.append(
																															"\", \"");
																													var5.append(
																															var1.getMsgCharset());
																													var5.append(
																															"\"}));");
																												}

																												var5.append(
																														NEW_LINE);
																												var5.append(
																														"\t\t\t}");
																											}

																											var5.append(
																													NEW_LINE);
																											var5.append(
																													NEW_LINE);
																											if (!var9
																													.isRequired()) {
																												var5.append(
																														"\t\t}");
																												var5.append(
																														NEW_LINE);
																												var5.append(
																														NEW_LINE);
																											}
																										}
																									} else {
																										if (!var9
																												.isRequired()) {
																											var5.append(
																													"\t\tif ( null != ");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("0" == operBlank) {
																												var5.append(
																														" && 0 != ");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												if ("String"
																														.equals(var11)) {
																													var5.append(
																															".length()");
																												} else {
																													var5.append(
																															".length");
																												}
																											}

																											var5.append(
																													" ) {");
																											var5.append(
																													NEW_LINE);
																										}

																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"闀垮害妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										if ("String"
																												.equals(var11)
																												&& (null != var9
																														.getDataCharset()
																														|| null != var1
																																.getMsgCharset())) {
																											var5.append(
																													"\t\t\ttry{");
																											var5.append(
																													NEW_LINE);
																										}

																										var5.append(
																												"\t\t\tif ( ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("String"
																												.equals(var11)) {
																											if (null != var9
																													.getDataCharset()) {
																												var5.append(
																														".getBytes(\""
																																+ var9.getDataCharset()
																																+ "\").length > ");
																											} else if (null != var1
																													.getMsgCharset()) {
																												var5.append(
																														".getBytes(\""
																																+ var1.getMsgCharset()
																																+ "\").length > ");
																											} else {
																												var5.append(
																														".getBytes().length > ");
																											}
																										} else if ("byte[]"
																												.equalsIgnoreCase(
																														var11)) {
																											var5.append(
																													".length > ");
																										}

																										if (var9.getMaxLength() != -1) {
																											var5.append(
																													var9.getMaxLength());
																										} else if (3001 == var10
																												.getDataType()) {
																											for (var19 = 0; var19 < var10
																													.getLength(); ++var19) {
																												var5.append(
																														"9");
																											}
																										} else if (3004 == var10
																												.getDataType()) {
																											var5.append(
																													"255");
																										}

																										if (var9.getMinLength() != -1) {
																											var5.append(
																													" || ");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("String"
																													.equals(var11)) {
																												if (null != var9
																														.getDataCharset()) {
																													var5.append(
																															".getBytes(\""
																																	+ var9.getDataCharset()
																																	+ "\").length < ");
																												} else if (null != var1
																														.getMsgCharset()) {
																													var5.append(
																															".getBytes(\""
																																	+ var1.getMsgCharset()
																																	+ "\").length < ");
																												} else {
																													var5.append(
																															".getBytes().length < ");
																												}
																											} else if ("byte[]"
																													.equalsIgnoreCase(
																															var11)) {
																												var5.append(
																														".length < ");
																											}

																											var5.append(
																													var9.getMinLength());
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																										if (var9.getMinLength() != -1) {
																											var5.append(
																													"\t\t\t\tthrow new RuntimeException(");
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t\t\t");
																											var5.append(
																													"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
																											if (null != var9
																													.getShortText()) {
																												var5.append(
																														var9.getShortText());
																												var5.append(
																														"(");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														")");
																											} else {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																											}

																											var5.append(
																													"\", \"");
																											if (var9.getMaxLength() != -1) {
																												var5.append(
																														var9.getMaxLength());
																											} else if (3001 == var10
																													.getDataType()) {
																												for (var19 = 0; var19 < var10
																														.getLength(); ++var19) {
																													var5.append(
																															"9");
																												}
																											} else if (3004 == var10
																													.getDataType()) {
																												var5.append(
																														"255");
																											}

																											var5.append(
																													"\", \"");
																											var5.append(
																													var9.getMinLength());
																											var5.append(
																													"\"}));");
																										} else {
																											var5.append(
																													"\t\t\t\tthrow new RuntimeException(");
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t\t\t");
																											var5.append(
																													"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
																											if (null != var9
																													.getShortText()) {
																												var5.append(
																														var9.getShortText());
																												var5.append(
																														"(");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														")");
																											} else {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																											}

																											var5.append(
																													"\", \"");
																											if (var9.getMaxLength() != -1) {
																												var5.append(
																														var9.getMaxLength());
																											} else if (3001 == var10
																													.getDataType()) {
																												for (var19 = 0; var19 < var10
																														.getLength(); ++var19) {
																													var5.append(
																															"9");
																												}
																											} else if (3004 == var10
																													.getDataType()) {
																												var5.append(
																														"255");
																											}

																											var5.append(
																													"\"}));");
																										}

																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										if ("String"
																												.equals(var11)
																												&& (null != var9
																														.getDataCharset()
																														|| null != var1
																																.getMsgCharset())) {
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}catch (UnsupportedEncodingException e) {");
																											var5.append(
																													NEW_LINE);
																											if (null != var9
																													.getDataCharset()) {
																												var5.append(
																														"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
																												if (null != var9
																														.getShortText()) {
																													var5.append(
																															var9.getShortText());
																													var5.append(
																															"(");
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																													var5.append(
																															")");
																												} else {
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																												}

																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getDataCharset());
																												var5.append(
																														"\"}));");
																											} else {
																												var5.append(
																														"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
																												var5.append(
																														var1.getId());
																												var5.append(
																														"\", \"");
																												var5.append(
																														var1.getMsgCharset());
																												var5.append(
																														"\"}));");
																											}

																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}");
																										}

																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																										if (!var9
																												.isRequired()) {
																											var5.append(
																													"\t\t}");
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													NEW_LINE);
																										}
																									}
																								} else if (3001 != var9
																										.getLengthFieldDataType()
																										&& 3002 != var9
																												.getLengthFieldDataType()
																										|| !"String"
																												.equals(var11)
																												&& !"byte[]"
																														.equalsIgnoreCase(
																																var11)) {
																									if (("String"
																											.equals(var11)
																											|| "byte[]"
																													.equalsIgnoreCase(
																															var11))
																											&& (var9.getMaxLength() != -1
																													|| var9.getMinLength() != -1)) {
																										if (!var9
																												.isRequired()) {
																											var5.append(
																													"\t\tif ( null != ");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("0" == operBlank) {
																												var5.append(
																														" && 0 != ");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												if ("String"
																														.equals(var11)) {
																													var5.append(
																															".length()");
																												} else {
																													var5.append(
																															".length");
																												}
																											}

																											var5.append(
																													" ) {");
																											var5.append(
																													NEW_LINE);
																										}

																										var5.append(
																												"\t\t\t//");
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"闀垮害妫�鏌�");
																										var5.append(
																												NEW_LINE);
																										if ("String"
																												.equals(var11)
																												&& (null != var9
																														.getDataCharset()
																														|| null != var1
																																.getMsgCharset())) {
																											var5.append(
																													"\t\t\ttry{");
																											var5.append(
																													NEW_LINE);
																										}

																										var5.append(
																												"\t\t\tif ( ");
																										if (var9.getMaxLength() != -1) {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("String"
																													.equals(var11)) {
																												if (null != var9
																														.getDataCharset()) {
																													var5.append(
																															".getBytes(\""
																																	+ var9.getDataCharset()
																																	+ "\").length > ");
																												} else if (null != var1
																														.getMsgCharset()) {
																													var5.append(
																															".getBytes(\""
																																	+ var1.getMsgCharset()
																																	+ "\").length > ");
																												} else {
																													var5.append(
																															".getBytes().length > ");
																												}
																											} else if ("byte[]"
																													.equalsIgnoreCase(
																															var11)) {
																												var5.append(
																														".length > ");
																											}

																											var5.append(
																													var9.getMaxLength());
																										}

																										if (var9.getMaxLength() != -1
																												&& var9.getMinLength() != -1) {
																											var5.append(
																													" || ");
																										}

																										if (var9.getMinLength() != -1) {
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("String"
																													.equals(var11)) {
																												if (null != var9
																														.getDataCharset()) {
																													var5.append(
																															".getBytes(\""
																																	+ var9.getDataCharset()
																																	+ "\").length < ");
																												} else if (null != var1
																														.getMsgCharset()) {
																													var5.append(
																															".getBytes(\""
																																	+ var1.getMsgCharset()
																																	+ "\").length < ");
																												} else {
																													var5.append(
																															".getBytes().length < ");
																												}
																											} else if ("byte[]"
																													.equalsIgnoreCase(
																															var11)) {
																												var5.append(
																														".length < ");
																											}

																											var5.append(
																													var9.getMinLength());
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										if (var9.getMaxLength() != -1
																												&& var9.getMinLength() != -1) {
																											var5.append(
																													"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
																											if (null != var9
																													.getShortText()) {
																												var5.append(
																														var9.getShortText());
																												var5.append(
																														"(");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														")");
																											} else {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																											}

																											var5.append(
																													"\", \"");
																											var5.append(
																													var9.getMaxLength());
																											var5.append(
																													"\", \"");
																											var5.append(
																													var9.getMinLength());
																											var5.append(
																													"\"}));");
																										} else if (var9
																												.getMaxLength() != -1) {
																											var5.append(
																													"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
																											if (null != var9
																													.getShortText()) {
																												var5.append(
																														var9.getShortText());
																												var5.append(
																														"(");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														")");
																											} else {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																											}

																											var5.append(
																													"\", \"");
																											var5.append(
																													var9.getMaxLength());
																											var5.append(
																													"\"}));");
																										} else if (var9
																												.getMinLength() != -1) {
																											var5.append(
																													"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.minLength\", new String[]{\"");
																											if (null != var9
																													.getShortText()) {
																												var5.append(
																														var9.getShortText());
																												var5.append(
																														"(");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														")");
																											} else {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																											}

																											var5.append(
																													"\", \"");
																											var5.append(
																													var9.getMinLength());
																											var5.append(
																													"\"}));");
																										}

																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																										if ("String"
																												.equals(var11)
																												&& (null != var9
																														.getDataCharset()
																														|| null != var1
																																.getMsgCharset())) {
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}catch (UnsupportedEncodingException e) {");
																											var5.append(
																													NEW_LINE);
																											if (null != var9
																													.getDataCharset()) {
																												var5.append(
																														"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
																												if (null != var9
																														.getShortText()) {
																													var5.append(
																															var9.getShortText());
																													var5.append(
																															"(");
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																													var5.append(
																															")");
																												} else {
																													var5.append(
																															StringUtil
																																	.toLowerCaseFirstOne(
																																			var9.getName()));
																												}

																												var5.append(
																														"\", \"");
																												var5.append(
																														var9.getDataCharset());
																												var5.append(
																														"\"}));");
																											} else {
																												var5.append(
																														"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
																												var5.append(
																														var1.getId());
																												var5.append(
																														"\", \"");
																												var5.append(
																														var1.getMsgCharset());
																												var5.append(
																														"\"}));");
																											}

																											var5.append(
																													NEW_LINE);
																											var5.append(
																													"\t\t\t}");
																										}

																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																										if (!var9
																												.isRequired()) {
																											var5.append(
																													"\t\t}");
																											var5.append(
																													NEW_LINE);
																											var5.append(
																													NEW_LINE);
																										}
																									}
																								} else {
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\tif ( null != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("0" == operBlank) {
																											var5.append(
																													" && 0 != ");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											if ("String"
																													.equals(var11)) {
																												var5.append(
																														".length()");
																											} else {
																												var5.append(
																														".length");
																											}
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																									}

																									var5.append(
																											"\t\t\t//");
																									var5.append(var9
																											.getShortText());
																									var5.append(
																											"闀垮害妫�鏌�");
																									var5.append(
																											NEW_LINE);
																									if ("String".equals(
																											var11)
																											&& (null != var9
																													.getDataCharset()
																													|| null != var1
																															.getMsgCharset())) {
																										var5.append(
																												"\t\t\ttry{");
																										var5.append(
																												NEW_LINE);
																									}

																									var5.append(
																											"\t\t\tif ( ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									if ("String".equals(
																											var11)) {
																										if (null != var9
																												.getDataCharset()) {
																											var5.append(
																													".getBytes(\""
																															+ var9.getDataCharset()
																															+ "\").length > ");
																										} else if (null != var1
																												.getMsgCharset()) {
																											var5.append(
																													".getBytes(\""
																															+ var1.getMsgCharset()
																															+ "\").length > ");
																										} else {
																											var5.append(
																													".getBytes().length > ");
																										}
																									} else if ("byte[]"
																											.equalsIgnoreCase(
																													var11)) {
																										var5.append(
																												".length > ");
																									}

																									if (var9.getMaxLength() != -1) {
																										var5.append(var9
																												.getMaxLength());
																									} else if (3001 == var9
																											.getLengthFieldDataType()) {
																										for (var19 = 0; var19 < var9
																												.getLengthFieldLength(); ++var19) {
																											var5.append(
																													"9");
																										}
																									} else if (3004 == var9
																											.getLengthFieldDataType()) {
																										var5.append(
																												"255");
																									}

																									if (var9.getMinLength() != -1) {
																										var5.append(
																												" || ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("String"
																												.equals(var11)) {
																											if (null != var9
																													.getDataCharset()) {
																												var5.append(
																														".getBytes(\""
																																+ var9.getDataCharset()
																																+ "\").length < ");
																											} else if (null != var1
																													.getMsgCharset()) {
																												var5.append(
																														".getBytes(\""
																																+ var1.getMsgCharset()
																																+ "\").length < ");
																											} else {
																												var5.append(
																														".getBytes().length < ");
																											}
																										} else if ("byte[]"
																												.equalsIgnoreCase(
																														var11)) {
																											var5.append(
																													".length < ");
																										}

																										var5.append(var9
																												.getMinLength());
																									}

																									var5.append(" ) {");
																									var5.append(
																											NEW_LINE);
																									if (var9.getMinLength() != -1) {
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxAndMin\", new String[]{\"");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"\", \"");
																										if (var9.getMaxLength() != -1) {
																											var5.append(
																													var9.getMaxLength());
																										} else if (3001 == var9
																												.getLengthFieldDataType()) {
																											for (var19 = 0; var19 < var9
																													.getLengthFieldLength(); ++var19) {
																												var5.append(
																														"9");
																											}
																										} else if (3004 == var9
																												.getLengthFieldDataType()) {
																											var5.append(
																													"255");
																										}

																										var5.append(
																												"\", \"");
																										var5.append(var9
																												.getMinLength());
																										var5.append(
																												"\"}));");
																									} else {
																										var5.append(
																												"\t\t\t\tthrow new RuntimeException(");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t\t\t");
																										var5.append(
																												"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.maxLength\", new String[]{\"");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												"\", \"");
																										if (var9.getMaxLength() != -1) {
																											var5.append(
																													var9.getMaxLength());
																										} else if (3001 == var9
																												.getLengthFieldDataType()) {
																											for (var19 = 0; var19 < var9
																													.getLengthFieldLength(); ++var19) {
																												var5.append(
																														"9");
																											}
																										} else if (3004 == var9
																												.getLengthFieldDataType()) {
																											var5.append(
																													"255");
																										}

																										var5.append(
																												"\"}));");
																									}

																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t}");
																									if ("String".equals(
																											var11)
																											&& (null != var9
																													.getDataCharset()
																													|| null != var1
																															.getMsgCharset())) {
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}catch (UnsupportedEncodingException e) {");
																										var5.append(
																												NEW_LINE);
																										if (null != var9
																												.getDataCharset()) {
																											var5.append(
																													"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"field.encoding.unsupport\", new String[]{\"");
																											if (null != var9
																													.getShortText()) {
																												var5.append(
																														var9.getShortText());
																												var5.append(
																														"(");
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																												var5.append(
																														")");
																											} else {
																												var5.append(
																														StringUtil
																																.toLowerCaseFirstOne(
																																		var9.getName()));
																											}

																											var5.append(
																													"\", \"");
																											var5.append(
																													var9.getDataCharset());
																											var5.append(
																													"\"}));");
																										} else {
																											var5.append(
																													"\t\t\t\tthrow new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(\"message.encoding.unsupport\", new String[]{\"");
																											var5.append(
																													var1.getId());
																											var5.append(
																													"\", \"");
																											var5.append(
																													var1.getMsgCharset());
																											var5.append(
																													"\"}));");
																										}

																										var5.append(
																												NEW_LINE);
																										var5.append(
																												"\t\t\t}");
																									}

																									var5.append(
																											NEW_LINE);
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}
																								}

																								if (3001 == var9
																										.getDataType()) {
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\tif ( null != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("0" == operBlank) {
																											var5.append(
																													" && 0 !=");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													".length()");
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																									}

																									var5.append(
																											"\t\t//");
																									var5.append(var9
																											.getShortText());
																									var5.append(
																											"杈撳叆绫诲瀷妫�鏌�");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\tif(!CodeUtil.isNumeric(");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(")){");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tthrow new RuntimeException(");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustNum\", new String[]{\"");
																									if (null != var9
																											.getShortText()) {
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")");
																									} else {
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																									}

																									var5.append(
																											"\"}));");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t}");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}
																								}

																								if (3006 == var9
																										.getDataType()) {
																									var5.append(
																											"\t\t//");
																									var5.append(var9
																											.getShortText());
																									var5.append(
																											"鏍煎紡妫�鏌�");
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\tif ( null != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("0" == operBlank) {
																											var5.append(
																													" && 0 !=");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													".length()");
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																									}

																									var5.append(
																											"\t\ttry{");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\tjava.text.DateFormat dateformat = new java.text.SimpleDateFormat(\"");
																									var5.append(var9
																											.getPattern());
																									var5.append("\");");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\tdateformat.setLenient(false);");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\tdateformat.parse(");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(");");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t} catch (Exception e) {");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tthrow new RuntimeException(");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\t");
																									var5.append(
																											"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
																									if (null != var9
																											.getShortText()) {
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")");
																									} else {
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																									}

																									var5.append(
																											"\", \"");
																									var5.append(var9
																											.getPattern());
																									var5.append(
																											"\"}) + e.getMessage(), e);");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t}");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}
																								}

																								if (3000 == var9
																										.getDataType()
																										&& null != var9
																												.getPattern()) {
																									var5.append(
																											"\t\t\t//");
																									var5.append(var9
																											.getShortText());
																									var5.append(
																											"鏍煎紡妫�鏌�");
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\tif ( null != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("0" == operBlank) {
																											var5.append(
																													" && 0 !=");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													".length()");
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																									}

																									var5.append(
																											"\t\t\tjava.util.regex.Pattern ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											"Pattern = java.util.regex.Pattern.compile(\"");
																									var5.append(var9
																											.getPattern());
																									var5.append("\");");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\tjava.util.regex.Matcher ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											"Matcher = ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											"Pattern.matcher(");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(");");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\tif ( !");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											"Matcher.matches() ){");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tthrow new RuntimeException(");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\t");
																									var5.append(
																											"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
																									if (null != var9
																											.getShortText()) {
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")");
																									} else {
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																									}

																									var5.append(
																											"\", \"");
																									var5.append(var9
																											.getPattern());
																									var5.append(
																											"\"}));");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t}");
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}
																								}

																								if (3010 == var9
																										.getDataType()) {
																									var5.append(
																											"\t\t\t//");
																									var5.append(var9
																											.getShortText());
																									var5.append(
																											"杈撳叆绫诲瀷妫�鏌�");
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\tif ( null != ");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										if ("0" == operBlank) {
																											var5.append(
																													" && 0 !=");
																											var5.append(
																													StringUtil
																															.toLowerCaseFirstOne(
																																	var9.getName()));
																											var5.append(
																													".length()");
																										}

																										var5.append(
																												" ) {");
																										var5.append(
																												NEW_LINE);
																									}

																									var5.append(
																											"\t\t\tBigDecimal big");
																									var5.append(
																											StringUtil
																													.toUpperCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											" = null;");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\ttry{");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tbig");
																									var5.append(
																											StringUtil
																													.toUpperCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											" = new BigDecimal(");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(");");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t} catch (Exception e) {");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tthrow new RuntimeException(");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.parameter.type.mustBigDecimal\", new String[]{\"");
																									if (null != var9
																											.getShortText()) {
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")");
																									} else {
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																									}

																									var5.append(
																											"\"}) + e.getMessage(), e);");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t}");
																									var5.append(
																											NEW_LINE);
																									var20 = var9
																											.getPattern()
																											.split(",");
																									var5.append(
																											"\t\t\tif ( ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											".indexOf(\".\") == -1 && ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											".length() > ");
																									if (var20.length > 1) {
																										var5.append(
																												Integer.parseInt(
																														var20[0])
																														- Integer
																																.parseInt(
																																		var20[1]));
																									} else {
																										var5.append(
																												Integer.parseInt(
																														var20[0]));
																									}

																									var5.append("){");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tthrow new RuntimeException(");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\t");
																									var5.append(
																											"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
																									if (null != var9
																											.getShortText()) {
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")");
																									} else {
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																									}

																									var5.append(
																											"\", \"");
																									var5.append(var9
																											.getPattern());
																									var5.append(
																											"\"}));");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t}else{");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tif( -1 != ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											".indexOf(\".\")){");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\tif ( ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											".substring(0, ");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											".indexOf(\".\")).length() > ");
																									if (var20.length > 1) {
																										var5.append(
																												Integer.parseInt(
																														var20[0])
																														- Integer
																																.parseInt(
																																		var20[1]));
																									} else {
																										var5.append(
																												Integer.parseInt(
																														var20[0]));
																									}

																									var5.append("){");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\tthrow new RuntimeException(");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\t");
																									var5.append(
																											"MultiLanguageResourceBundle.getInstance().getString(\"Message.field.pattern.illegal\", new String[]{\"");
																									if (null != var9
																											.getShortText()) {
																										var5.append(var9
																												.getShortText());
																										var5.append(
																												"(");
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																										var5.append(
																												")");
																									} else {
																										var5.append(
																												StringUtil
																														.toLowerCaseFirstOne(
																																var9.getName()));
																									}

																									var5.append(
																											"\", \"");
																									var5.append(var9
																											.getPattern());
																									var5.append(
																											"\"}));");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t\t}");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t}");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t\t");
																									var5.append(
																											StringUtil
																													.toLowerCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											" = big");
																									var5.append(
																											StringUtil
																													.toUpperCaseFirstOne(
																															var9.getName()));
																									var5.append(
																											".divide(new BigDecimal(1), ");
																									if (var20.length > 1) {
																										var5.append(
																												var20[1]);
																									} else {
																										var5.append(
																												"0");
																									}

																									var5.append(
																											", BigDecimal.ROUND_HALF_UP).toString();");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											"\t\t\t}");
																									var5.append(
																											NEW_LINE);
																									var5.append(
																											NEW_LINE);
																									if (!var9
																											.isRequired()) {
																										var5.append(
																												"\t\t}");
																										var5.append(
																												NEW_LINE);
																										var5.append(
																												NEW_LINE);
																									}
																								}
																							}
																						} else {
																							if (var9.getReference() != null) {
																								var11 = var9
																										.getReference()
																										.getClassName();
																							} else if (!"dynamic"
																									.equalsIgnoreCase(
																											var9.getReferenceType())
																									&& !"expression"
																											.equalsIgnoreCase(
																													var9.getReferenceType())) {
																								var11 = var9
																										.getCombineOrTableFieldClassName();
																								if (null == var11) {
																									var11 = var1
																											.getClassName()
																											+ StringUtil
																													.toUpperCaseFirstOne(
																															var9.getName());
																								}
																							} else {
																								var11 = MessageBean.class
																										.getName();
																							}

																							if (!var9.isRequired()) {
																								var5.append(
																										"\t\tif ( 0 != ");
																								var5.append(StringUtil
																										.toLowerCaseFirstOne(
																												var9.getName()));
																								var5.append(
																										".size() ) { ");
																							}

																							var5.append("\t\t//");
																							var5.append(var9
																									.getShortText());
																							var5.append("姝ｇ‘鎬ф鏌�");
																							var5.append(NEW_LINE);
																							var5.append(
																									"\t\tfor( int i = 0; i < ");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(
																									".size(); i++){");
																							var5.append(NEW_LINE);
																							var5.append("\t\t\t((");
																							var5.append(var11);
																							var5.append(")");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(
																									".get(i)).validate();");
																							var5.append(NEW_LINE);
																							var5.append("\t\t}");
																							var5.append(NEW_LINE);
																							var5.append(NEW_LINE);
																							if (!var9.isRequired()) {
																								var5.append("\t\t}");
																								var5.append(NEW_LINE);
																								var5.append(NEW_LINE);
																							}
																						}
																					} else {
																						var5.append("\t\t//");
																						var5.append(
																								var9.getShortText());
																						var5.append("姝ｇ‘鎬ф鏌�");
																						var5.append(NEW_LINE);
																						if (!var9.isRequired()) {
																							var5.append("\t\t");
																							var5.append(
																									"if ( null != ");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(" && !");
																							var5.append(StringUtil
																									.toLowerCaseFirstOne(
																											var9.getName()));
																							var5.append(
																									".isNull() ){ ");
																							var5.append(NEW_LINE);
																						}

																						var5.append("\t\t\t");
																						var5.append(StringUtil
																								.toLowerCaseFirstOne(
																										var9.getName()));
																						var5.append(".validate();");
																						var5.append(NEW_LINE);
																						if (!var9.isRequired()) {
																							var5.append("\t\t}");
																						}

																						var5.append(NEW_LINE);
																						var5.append(NEW_LINE);
																					}
																				} while (2000 != var9.getFieldType()
																						&& 2001 != var9.getFieldType());

																				var6 = (HashMap) var9.getValueRange();
																			} while (var6 == null);
																		} while (0 == var6.size());
																	} while (null != var6.get("default-ref"));

																	var5.append("\t\t//");
																	var5.append(var9.getShortText());
																	var5.append("鎸囧畾鍊煎煙妫�鏌�");
																	var5.append(NEW_LINE);
																	if (!var9.isRequired()) {
																		if (!"String".equals(var11)
																				&& !"byte[]".equals(var11)) {
																			var5.append("\t\tif ( 0 != ");
																		} else {
																			var5.append("\t\tif ( null != ");
																		}

																		var5.append(StringUtil
																				.toLowerCaseFirstOne(var9.getName()));
																		if (("String".equals(var11)
																				|| "byte[]".equals(var11))
																				&& "0" == operBlank) {
																			var5.append(" && 0 !=");
																			var5.append(StringUtil.toLowerCaseFirstOne(
																					var9.getName()));
																			if ("String".equals(var11)) {
																				var5.append(".length()");
																			} else {
																				var5.append(".length");
																			}
																		}

																		var5.append(" ) {");
																		var5.append(NEW_LINE);
																	}

																	var5.append("\t\tif ( ");
																	var8 = var6.keySet().iterator();

																	while (true) {
																		while (var8.hasNext()) {
																			var12 = (String) var8.next();
																			if (!"String".equals(var11)
																					&& !"byte[]".equals(var11)) {
																				var5.append(var12);
																				var5.append(" != ");
																				var5.append(
																						StringUtil.toLowerCaseFirstOne(
																								var9.getName()));
																				var5.append(" && ");
																			} else {
																				var5.append("!\"");
																				var5.append(var12);
																				var5.append("\".equals( ");
																				var5.append(
																						StringUtil.toLowerCaseFirstOne(
																								var9.getName()));
																				var5.append(") && ");
																			}
																		}

																		var5.delete(var5.length() - 3, var5.length());
																		var5.append(") {");
																		var5.append(NEW_LINE);
																		StringBuffer var21 = new StringBuffer();
																		var8 = var6.keySet().iterator();

																		while (var8.hasNext()) {
																			var21.append(var8.next());
																			var21.append("/");
																		}

																		var21.delete(var21.length() - 1,
																				var21.length());
																		var5.append(
																				"\t\t\tthrow new RuntimeException(");
																		var5.append(NEW_LINE);
																		var5.append(
																				"\t\t\t\tMultiLanguageResourceBundle.getInstance().getString(\"Message.field.value.in\", new String[]{\")");
																		if (null != var9.getShortText()) {
																			var5.append(var9.getShortText());
																			var5.append("(");
																			var5.append(StringUtil.toLowerCaseFirstOne(
																					var9.getName()));
																			var5.append(")");
																		} else {
																			var5.append(StringUtil.toLowerCaseFirstOne(
																					var9.getName()));
																		}

																		var5.append("\", \"");
																		var5.append(var21.toString());
																		var5.append("\"}));");
																		var5.append(NEW_LINE);
																		var5.append("\t\t}");
																		var5.append(NEW_LINE);
																		var5.append(NEW_LINE);
																		if (!var9.isRequired()) {
																			var5.append("\t\t}");
																			var5.append(NEW_LINE);
																			var5.append(NEW_LINE);
																		}
																		break;
																	}
																}
															}

															var9 = (Field) var7.next();
															var14 = StringUtil.toLowerCaseFirstOne(var9.getName());
														} while (2005 == var9.getFieldType());
													} while (2006 == var9.getFieldType());
												} while (2007 == var9.getFieldType());
											} while (2010 == var9.getFieldType());

											if (2000 != var9.getFieldType() && 2001 != var9.getFieldType()) {
												if (2002 != var9.getFieldType() && 2003 != var9.getFieldType()
														&& 2008 != var9.getFieldType() && 2009 != var9.getFieldType()) {
													continue label1371;
												}

												var5.append(NEW_LINE);
												var5.append("\t\t");
												var5.append("if( null != " + var14 + "&&null != newBean.get"
														+ StringUtil.toUpperCaseFirstOne(var14) + "()){");
												var5.append(NEW_LINE);
												var5.append("\t\t\t");
												var5.append(var14 + ".cover(newBean.get"
														+ StringUtil.toUpperCaseFirstOne(var14) + "());");
												var5.append(NEW_LINE);
												var5.append("\t\t}");
											} else {
												var15 = Constant.getJavaTypeByDataType(var9.getDataType());
												if ("String".equals(var15)) {
													var5.append(NEW_LINE);
													var5.append("\t\t");
													var5.append("if( null != newBean.get"
															+ StringUtil.toUpperCaseFirstOne(var14)
															+ "() && 0 != newBean.get"
															+ StringUtil.toUpperCaseFirstOne(var14) + "().length()){");
													var5.append(NEW_LINE);
													var5.append("\t\t\t");
													var5.append(var14 + " = newBean.get"
															+ StringUtil.toUpperCaseFirstOne(var14) + "();");
													var5.append(NEW_LINE);
													var5.append("\t\t}");
												} else if (!"int".equals(var15) && !"byte".equals(var15)
														&& !"short".equals(var15) && !"long".equals(var15)) {
													if ("byte[]".equals(var15)) {
														var5.append(NEW_LINE);
														var5.append("\t\t");
														var5.append("if( null != newBean.get"
																+ StringUtil.toUpperCaseFirstOne(var14)
																+ "()&&0!=newBean.get"
																+ StringUtil.toUpperCaseFirstOne(var14)
																+ "().length){");
														var5.append(NEW_LINE);
														var5.append("\t\t\t");
														var5.append(var14 + " = newBean.get"
																+ StringUtil.toUpperCaseFirstOne(var14) + "();");
														var5.append(NEW_LINE);
														var5.append("\t\t}");
													}
												} else {
													var5.append(NEW_LINE);
													var5.append("\t\t");
													var5.append("if( 0 != newBean.get"
															+ StringUtil.toUpperCaseFirstOne(var14) + "()){");
													var5.append(NEW_LINE);
													var5.append("\t\t\t");
													var5.append(var14 + " = newBean.get"
															+ StringUtil.toUpperCaseFirstOne(var14) + "();");
													var5.append(NEW_LINE);
													var5.append("\t\t}");
												}
											}
										}
									}
								} while (2004 != var9.getFieldType() && 2011 != var9.getFieldType());

								var5.append(NEW_LINE);
								var5.append("\t\t");
								var5.append("if(null!=" + var14 + "&&null!=newBean.get"
										+ StringUtil.toUpperCaseFirstOne(var14) + "()){");
								var5.append(NEW_LINE);
								var5.append("\t\t\t");
								var5.append("int " + var14 + "Size=" + var14 + ".size();");
								var5.append(NEW_LINE);
								var5.append("\t\t\t");
								var5.append("if(" + var14 + "Size>newBean.get" + StringUtil.toUpperCaseFirstOne(var14)
										+ "().size()){");
								var5.append(NEW_LINE);
								var5.append("\t\t\t\t");
								var5.append(var14 + "Size=newBean.get" + StringUtil.toUpperCaseFirstOne(var14)
										+ "().size();");
								var5.append(NEW_LINE);
								var5.append("\t\t\t}");
								var5.append(NEW_LINE);
								var5.append("\t\t\t");
								var5.append("for( int i =0; i < " + var14 + "Size; i++) {");
								var5.append(NEW_LINE);
								var5.append("\t\t\t\t");
								if (!"dynamic".equals(var9.getReferenceType())
										&& !"expression".equals(var9.getReferenceType())) {
									var5.append(
											"get" + StringUtil.toUpperCaseFirstOne(var14) + "At(i).cover(newBean.get"
													+ StringUtil.toUpperCaseFirstOne(var14) + "At(i));");
								} else {
									var5.append("((com.giantstone.message.bean.MessageBean)" + var14
											+ ".get(i)).cover((com.giantstone.message.bean.MessageBean)newBean.get"
											+ StringUtil.toUpperCaseFirstOne(var14) + "().get(i));");
								}

								var5.append(NEW_LINE);
								var5.append("\t\t\t}");
								var5.append(NEW_LINE);
								var5.append("\t\t}");
							}
						}

						var9 = (Field) var7.next();
						if (2002 != var9.getFieldType() && 2003 != var9.getFieldType() && 2004 != var9.getFieldType()
								&& 2011 != var9.getFieldType() && 2008 != var9.getFieldType()
								&& 2009 != var9.getFieldType()) {
							var11 = Constant.getJavaTypeByDataType(var9.getDataType());
						} else if (var9.getReference() != null) {
							var11 = var9.getReference().getClassName();
						} else if (!"dynamic".equalsIgnoreCase(var9.getReferenceType())
								&& !"expression".equalsIgnoreCase(var9.getReferenceType())) {
							var11 = var9.getCombineOrTableFieldClassName();
							if (null == var11) {
								var11 = var1.getClassName() + StringUtil.toUpperCaseFirstOne(var9.getName());
							}

							Message var13 = new Message();
							var13.setId(var1.getId() + "-" + var9.getName());
							var13.setClassName(var11);
							var13.setShortText(var9.getShortText());
							var13.setFields(var9.getSubFields());
							if (null != var9.getDataCharset()) {
								var13.setMsgCharset(var9.getDataCharset());
							} else if (null != var1.getMsgCharset()) {
								var13.setMsgCharset(var1.getMsgCharset());
							}

							this.generate(var13, var2);
						} else {
							var11 = MessageBean.class.getName();
						}

						var5.append("\t//");
						var5.append(var9.getShortText());
						var5.append(NEW_LINE);
						var5.append("\tprivate ");
						if (2004 != var9.getFieldType() && 2011 != var9.getFieldType()) {
							var5.append(var11);
						} else {
							var5.append("List");
						}

						var5.append(" ");
						var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
						if (2004 != var9.getFieldType() && 2011 != var9.getFieldType()) {
							if ((2002 == var9.getFieldType() || 2003 == var9.getFieldType()
									|| 2008 == var9.getFieldType() || 2009 == var9.getFieldType())
									&& !"dynamic".equalsIgnoreCase(var9.getReferenceType())
									&& !"expression".equalsIgnoreCase(var9.getReferenceType())) {
								var5.append(" = new ");
								var5.append(var11);
								var5.append("()");
							} else if (var9.getValue() != null) {
								switch (var9.getDataType()) {
								case 3000:
								case 3001:
								case 3006:
								case 3010:
								case 3011:
									var5.append(" = \"");
									var5.append(var9.getValue());
									var5.append("\"");
									break;
								case 3002:
									var5.append(" = CodeUtil.HextoByte(\"");
									var5.append(var9.getValue());
									var5.append("\")");
									break;
								case 3003:
								case 3007:
									var5.append(" = ");
									var5.append(var9.getValue());
									break;
								case 3004:
								case 3005:
								case 3008:
								default:
									var5.append(" = (");
									var5.append(Constant.getJavaTypeByDataType(var9.getDataType()));
									var5.append(")");
									var5.append(var9.getValue());
									break;
								case 3009:
									var5.append(" = ");
									var5.append(var9.getValue());
									var5.append("l");
								}
							}
						} else {
							var5.append(" = new ArrayList(20)");
						}

						var5.append(";");
						var5.append(NEW_LINE);
						var5.append(NEW_LINE);
						var5.append("\tpublic ");
						if (2004 != var9.getFieldType() && 2011 != var9.getFieldType()) {
							var5.append(var11);
						} else {
							var5.append("List");
						}

						var5.append(" get");
						var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
						var5.append("(){");
						var5.append(NEW_LINE);
						var5.append("\t\treturn ");
						var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
						var5.append(";");
						var5.append(NEW_LINE);
						var5.append("\t}");
						var5.append(NEW_LINE);
						var5.append(NEW_LINE);
						if ((2004 == var9.getFieldType() || 2011 == var9.getFieldType())
								&& !"dynamic".equalsIgnoreCase(var9.getReferenceType())
								&& !"expression".equalsIgnoreCase(var9.getReferenceType())) {
							var5.append("\tpublic ");
							var5.append(var11);
							var5.append(" get");
							var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
							var5.append("At(int index){");
							var5.append(NEW_LINE);
							var5.append("\t\treturn (");
							var5.append(var11);
							var5.append(")");
							var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
							var5.append(".get(index);");
							var5.append(NEW_LINE);
							var5.append("\t}");
							var5.append(NEW_LINE);
							var5.append(NEW_LINE);
							var5.append("\tpublic int get");
							var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
							var5.append("RowNum(){");
							var5.append(NEW_LINE);
							var5.append("\t\treturn ");
							var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
							var5.append(".size();");
							var5.append(NEW_LINE);
							var5.append("\t}");
							var5.append(NEW_LINE);
							var5.append(NEW_LINE);
						}
					} while (!var9.isEditable());

					var5.append("\tpublic void set");
					var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
					var5.append("(");
					if (2004 != var9.getFieldType() && 2011 != var9.getFieldType()) {
						var5.append(var11);
					} else {
						var5.append("List");
					}

					var5.append(" ");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append("){");
					var5.append(NEW_LINE);
					var5.append("\t\tthis.");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(" = ");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(";");
					var5.append(NEW_LINE);
					var5.append("\t}");
					var5.append(NEW_LINE);
					var5.append(NEW_LINE);
				} while (2004 != var9.getFieldType() && 2011 != var9.getFieldType());

				if (!"dynamic".equalsIgnoreCase(var9.getReferenceType())
						&& !"expression".equalsIgnoreCase(var9.getReferenceType())) {
					var5.append("\tpublic ");
					var5.append(var11);
					var5.append(" create");
					var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
					var5.append("(){");
					var5.append(NEW_LINE);
					var5.append("\t\t");
					var5.append(var11);
					var5.append(" newRecord = new ");
					var5.append(var11);
					var5.append("();");
					var5.append(NEW_LINE);
					var5.append("\t\t");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(".add(newRecord);");
					var5.append(NEW_LINE);
					var5.append("\t\treturn newRecord;");
					var5.append(NEW_LINE);
					var5.append("\t}");
					var5.append(NEW_LINE);
					var5.append(NEW_LINE);
					var5.append("\tpublic void add");
					var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
					var5.append("(");
					var5.append(var11);
					var5.append(" newRecord){");
					var5.append(NEW_LINE);
					var5.append("\t\t");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(".add(newRecord);");
					var5.append(NEW_LINE);
					var5.append("\t}");
					var5.append(NEW_LINE);
					var5.append(NEW_LINE);
					var5.append("\tpublic void set");
					var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
					var5.append("At(int index, ");
					var5.append(var11);
					var5.append(" newRecord){");
					var5.append(NEW_LINE);
					var5.append("\t\t");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(".set(index, newRecord);");
					var5.append(NEW_LINE);
					var5.append("\t}");
					var5.append(NEW_LINE);
					var5.append(NEW_LINE);
					var5.append("\tpublic ");
					var5.append(var11);
					var5.append(" remove");
					var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
					var5.append("At(int index){");
					var5.append(NEW_LINE);
					var5.append("\t\treturn (");
					var5.append(var11);
					var5.append(")");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(".remove(index);");
					var5.append(NEW_LINE);
					var5.append("\t}");
					var5.append(NEW_LINE);
					var5.append(NEW_LINE);
					var5.append("\tpublic void clear");
					var5.append(StringUtil.toUpperCaseFirstOne(var9.getName()));
					var5.append("(){");
					var5.append(NEW_LINE);
					var5.append("\t\t");
					var5.append(StringUtil.toLowerCaseFirstOne(var9.getName()));
					var5.append(".clear();");
					var5.append(NEW_LINE);
					var5.append("\t}");
					var5.append(NEW_LINE);
					var5.append(NEW_LINE);
				}
			}
		}
	}

	public String getOutputDir() {
		return this.outputDir;
	}

	public void setOutputDir(String var1) {
		this.outputDir = var1;
		if (!this.outputDir.endsWith("/")) {
			this.outputDir = this.outputDir + "/";
		}

	}

	private String getType(Field var1, Message var2) {
		String var3 = null;
		if (0 != var1.getDataType()) {
			var3 = Constant.getJavaTypeByDataType(var1.getDataType());
			if ("int".equals(var3)) {
				return "Integer.valueOf(this.get" + StringUtil.toUpperCaseFirstOne(var1.getName()) + "())";
			} else if ("long".equals(var3)) {
				return "Long.valueOf(this.get" + StringUtil.toUpperCaseFirstOne(var1.getName()) + "())";
			} else if ("byte".equals(var3)) {
				return "Byte.valueOf(this.get" + StringUtil.toUpperCaseFirstOne(var1.getName()) + "())";
			} else {
				return "short".equals(var3)
						? "Short.valueOf(this.get" + StringUtil.toUpperCaseFirstOne(var1.getName()) + "())"
						: "this.get" + StringUtil.toUpperCaseFirstOne(var1.getName()) + "()";
			}
		} else {
			return "this.get" + StringUtil.toUpperCaseFirstOne(var1.getName()) + "()";
		}
	}

	private String getSetAttType(Field var1, Message var2) {
		String var3 = null;
		String var4 = null;
		if (2002 != var1.getFieldType() && 2003 != var1.getFieldType() && 2004 != var1.getFieldType()
				&& 2011 != var1.getFieldType() && 2008 != var1.getFieldType() && 2009 != var1.getFieldType()) {
			var3 = Constant.getJavaTypeByDataType(var1.getDataType());
			if ("int".equals(var3)) {
				var4 = "((Integer)value).intValue()";
			} else if ("long".equals(var3)) {
				var4 = "((Long)value).longValue()";
			} else if ("byte".equals(var3)) {
				var4 = "((Byte)value).byteValue()";
			} else if ("short".equals(var3)) {
				var4 = "((Short)value).shortValue()";
			} else {
				var4 = "(" + var3 + ")value";
			}
		} else {
			if (var1.getReference() != null) {
				var3 = var1.getReference().getClassName();
			} else if (!"dynamic".equalsIgnoreCase(var1.getReferenceType())
					&& !"expression".equalsIgnoreCase(var1.getReferenceType())) {
				var3 = var1.getCombineOrTableFieldClassName();
				if (null == var3) {
					var3 = var2.getClassName() + StringUtil.toUpperCaseFirstOne(var1.getName());
				}
			} else {
				var3 = MessageBean.class.getName();
			}

			var4 = "(" + var3 + ")value";
		}

		if (2004 == var1.getFieldType() || 2011 == var1.getFieldType()) {
			var3 = "List";
			var4 = "(" + var3 + ")value";
		}

		return var4;
	}

//	static {
//		try {
//			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
//				Properties var0 = ConfigManager.loadProperties("messagebean.properties");
//				if (null != var0.getProperty("oper_blank") && 0 != var0.getProperty("oper_blank").length()
//						&& "0".equalsIgnoreCase(var0.getProperty("oper_blank"))) {
//					operBlank = "0";
//				}
//			}
//		} catch (Exception var1) {
//			var1.printStackTrace();
//			ExceptionUtil.throwActualException(var1);
//		}
//
//	}
}
