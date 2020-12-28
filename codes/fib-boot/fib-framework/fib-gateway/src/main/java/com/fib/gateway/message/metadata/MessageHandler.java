package com.fib.gateway.message.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.util.EnumConstants;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.message.CodeUtil;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import cn.hutool.core.util.StrUtil;

public class MessageHandler extends DefaultHandler {
	public class _AB {
		private ArrayList A;

		private _AB() {
			this.A = new ArrayList();
		}

		public Object A() {
			return this.A.size() > 0 ? this.A.remove(this.A.size() - 1) : null;
		}

		public void A(Object var1) {
			this.A.add(var1);
		}

		// $FF: synthetic method
		_AB(int var1) {
			this();
		}

	}

	private Message message;
	private String beanFilePath;
	private String channelId;
	_AB I;
	Map G;
	ValueRange A;
	Map B;
	A H;
	Field J;
	String F;
	String E;

	MessageHandler() {
		this.message = new Message();
		this.beanFilePath = null;
		this.channelId = null;
		this.I = new MessageHandler._AB((1));
		this.G = new HashMap();
		this.A = null;
		this.B = new HashMap();
		this.H = null;
		this.J = null;
		this.F = null;
		this.E = null;
	}

	public Message parseFile(String channelId, File beanFile) {
		this.beanFilePath = beanFile.getAbsolutePath();

		try (FileInputStream fis = new FileInputStream(beanFile)) {
			return this.parseFile(channelId, fis);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}
	}

	public Message parseFile(String channelId, InputStream var2) {
		this.channelId = channelId;
		SAXParserFactory var3 = SAXParserFactory.newDefaultInstance();

		try {
			SAXParser var4 = var3.newSAXParser();
			var4.parse(var2, this);
		} catch (Exception e) {
			throw new CommonException("Failed to read file", e);
		}

		this.A();
		this.A(this.message.getFields());
		this.A(this.message.getClassName(), this.message.getFields());
		return this.message;
	}

	private void A() {
		Field var1;
		Iterator var2;
		if (1002 == this.message.getType() || 1003 == this.message.getType()) {
			var1 = null;
			var2 = this.message.getFields().values().iterator();
			HashSet var3 = new HashSet(this.message.getFields().size());

			while (var2.hasNext()) {
				var1 = (Field) var2.next();
				if (null == var1.getTag()) {
					throw new RuntimeException("");
				}

				if (1002 == this.message.getType() && 2004 != var1.getFieldType() && 2005 != var1.getFieldType()
						&& 2000 != var1.getFieldType() && 2002 != var1.getFieldType() && 2008 != var1.getFieldType()) {
					throw new RuntimeException("");
				}

				if (var3.contains(var1.getTag())) {
					throw new RuntimeException("");
				}

				var3.add(var1.getTag());
			}
		}

		if (1001 == this.message.getType()) {
			var1 = null;
			var2 = this.message.getFields().values().iterator();

			while (var2.hasNext()) {
				var1 = (Field) var2.next();
				if (2001 == var1.getFieldType() || 2003 == var1.getFieldType() || 2009 == var1.getFieldType()) {
					throw new RuntimeException("");
				}
			}
		}

	}

	private void A(Map var1, Field var2) {
		if (3001 != var2.getDataType() && 3003 != var2.getDataType() && 3005 != var2.getDataType()
				&& 3004 != var2.getDataType() && 3007 != var2.getDataType() && 3008 != var2.getDataType()
				&& 3009 != var2.getDataType()) {
			throw new RuntimeException("");
		} else {
			Field var3 = (Field) var1.get(var2.getStartFieldName());
			if (null == var3) {
				throw new RuntimeException("");
			} else {
				var2.setStartField(var3);
				if (var2.getStartFieldName().equals(var2.getEndFieldName())) {
					var2.setEndField(var2.getStartField());
				} else {
					Field var4 = (Field) var1.get(var2.getEndFieldName());
					if (null == var4) {
						throw new RuntimeException("");
					}

					var2.setEndField(var4);
				}

			}
		}
	}

	private void A(Map var1) {
		Field var2 = null;
		Iterator var3 = var1.values().iterator();
		int var4 = 0;

		do {
			do {
				do {
					do {
						do {
							if (!var3.hasNext()) {
								return;
							}

							var2 = (Field) var3.next();
							++var4;
							if (("dynamic".equalsIgnoreCase(var2.getReferenceType())
									|| "expression".equalsIgnoreCase(var2.getReferenceType()))
									&& 2002 != var2.getFieldType() && 2003 != var2.getFieldType()
									&& 2004 != var2.getFieldType()) {
								throw new RuntimeException("");
							}

							Field var5;
							if (2001 == var2.getFieldType() || 2003 == var2.getFieldType()
									|| 2009 == var2.getFieldType()
									|| 2011 == var2.getFieldType() && 0 != var2.getLengthFieldLength()) {
								if (var2.getRefLengthFieldName() != null) {
									var5 = (Field) var1.get(var2.getRefLengthFieldName());
									if (null == var5) {
										throw new RuntimeException("");
									}

									if (2012 != var5.getFieldType() && 2007 != var5.getFieldType()) {
										throw new RuntimeException("");
									}

									if (2012 == var5.getFieldType() && 3003 != var5.getDataType()) {
										throw new RuntimeException("");
									}

									var2.setRefLengthField(var5);
								} else {
									switch (var2.getLengthFieldDataType()) {
									case 3001:
										break;
									case 3002:
									case 3006:
									default:
										throw new RuntimeException("");
									case 3003:
									case 3007:
										if (var2.getLengthFieldLength() != 4) {
											throw new RuntimeException("");
										}
										break;
									case 3004:
										if (var2.getLengthFieldLength() != 1) {
											throw new RuntimeException("");
										}
										break;
									case 3005:
									case 3008:
										if (var2.getLengthFieldLength() != 2) {
											throw new RuntimeException("");
										}
										break;
									case 3009:
										if (var2.getLengthFieldLength() != 8) {
											throw new RuntimeException("");
										}
									}
								}
							}

							if (2007 == var2.getFieldType() || 2012 == var2.getFieldType()) {
								this.A(var1, var2);
							}

							if (2005 == var2.getFieldType()) {
								if (3001 != var2.getDataType() && 3003 != var2.getDataType()
										&& 3005 != var2.getDataType() && 3004 != var2.getDataType()
										&& 3007 != var2.getDataType() && 3008 != var2.getDataType()) {
									throw new RuntimeException("");
								}

								var5 = (Field) var1.get(var2.getTableName());
								if (null == var5) {
									throw new RuntimeException("");
								}

								if (var5.getFieldType() != 2004 && var5.getFieldType() != 2011) {
									throw new RuntimeException("");
								}

								var2.setTable(var5);
							}

							if (2004 == var2.getFieldType() && var2.getRowNumFieldName() != null) {
								var5 = (Field) var1.get(var2.getRowNumFieldName());
								if (null != var5) {
									if (var5.getFieldType() != 2005) {
										throw new RuntimeException("");
									}

									var2.setRowNumField(var5);
								}
							}

							if (1002 == this.message.getType() && null == var2.getSubFields()
									&& null == var2.getReference()) {
								throw new RuntimeException("");
							}

							if (2010 == var2.getFieldType()) {
								if (3002 != var2.getDataType()) {
									throw new RuntimeException("");
								}

								var5 = (Field) var1.get(var2.getStartFieldName());
								if (null != var5) {
									var2.setStartField(var5);
									if (var2.getStartFieldName().equals(var2.getEndFieldName())) {
										var2.setEndField(var2.getStartField());
									} else {
										Field var6 = (Field) var1.get(var2.getEndFieldName());
										if (null == var6) {
											throw new RuntimeException("");
										}

										var2.setEndField(var6);
									}
								}
							}
						} while (2011 != var2.getFieldType());
					} while (null != var2.getRefLengthFieldName());
				} while (null != var2.getTabSuffix());
			} while (null != var2.getSuffix() && var2.isLastRowSuffix());
		} while (var4 >= var1.size());

		throw new RuntimeException("");
	}

	private void A(String var1, Map var2) {
		Field var3 = null;
		Iterator var4 = var2.values().iterator();

		while (true) {
			do {
				if (!var4.hasNext()) {
					return;
				}

				var3 = (Field) var4.next();
			} while (2002 != var3.getFieldType() && 2003 != var3.getFieldType() && 2004 != var3.getFieldType()
					&& 2011 != var3.getFieldType());

			if (!"dynamic".equalsIgnoreCase(var3.getReferenceType())
					&& !"expression".equalsIgnoreCase(var3.getReferenceType()) && null == var3.getReference()) {
				Message var5 = new Message();
				var5.setId(this.message.getId() + "-" + var3.getName());
				var5.setShortText(var3.getShortText());
				var5.setFields(var3.getSubFields());
				if ("com.giantstone.message.bean.CommonMessageBean".equals(this.message.getClassName())) {
					var5.setClassName("com.giantstone.message.bean.CommonMessageBean");
				} else {
					String var6 = var3.getCombineOrTableFieldClassName();
					if (null == var6) {
						var6 = var1 + StrUtil.upperFirst(var3.getName());
					}

					var5.setClassName(var6);
					MessageMetadataManager.access$100().put(var5.getClassName(), var5);
				}

				this.A(var5.getClassName(), var5.getFields());
			}
		}
	}

	@Override
	public void endElement(String var1, String var2, String var3) throws SAXException {
		if ("field".equals(var3)) {
			Field var4 = (Field) this.I.A();
			if (var4.getFieldType() == 2002 && var4.getReference() == null
					&& !"dynamic".equalsIgnoreCase(var4.getReferenceType())
					&& !"expression".equalsIgnoreCase(var4.getReferenceType())) {
				if (var4.getReference() == null) {
					this.A(var4.getSubFields());
				}

				if (1002 == this.message.getType() && var4.getFieldType() == 2002 && var4.getReference() == null) {
					throw new RuntimeException("");
				}
			}

			if ((2002 == var4.getFieldType() || 2003 == var4.getFieldType() || 2004 == var4.getFieldType()
					|| 2011 == var4.getFieldType()) && var4.getReference() == null) {
				this.A(var4.getSubFields());
			}

			if ((2000 == var4.getFieldType() || 2001 == var4.getFieldType()) && 3010 == var4.getDataType()) {
				if (null == var4.getPattern()) {
					throw new RuntimeException("");
				}

				String[] var5 = var4.getPattern().split(",");
				if (var5.length > 2) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='");
				}

				try {
					Integer.parseInt(var5[0].trim());
				} catch (Exception var8) {
					throw new RuntimeException(this.beanFilePath + " :");
				}

				if (var5.length == 2) {
					try {
						Integer.parseInt(var5[1].trim());
					} catch (Exception var7) {
						throw new RuntimeException(this.beanFilePath + " :");
					}
				}
			}

			if (0 != this.G.size()) {
				this.J.setValueRange(new HashMap());
				this.J.getValueRange().putAll(this.G);
				this.G.clear();
			}

			this.J = null;
		} else if ("value-range".equals(var3)) {
			this.J.setValueRange(new HashMap());
			this.J.getValueRange().putAll(this.G);
			this.G.clear();
		} else {
			String var9;
			if ("expression".equals(var3)) {
				var9 = this.E;
				if (null != var9) {
					var9 = var9.trim();
				}

				if (0 == var9.length()) {
					var9 = null;
				}

				if (this.J != null) {
					this.J.setExpression(var9);
				}

				this.E = null;
			} else if ("event".equals(var3)) {
				var9 = this.E;
				if (var9 != null) {
					var9 = var9.trim();
				}

				if (0 == var9.length()) {
					var9 = null;
				}

				boolean var10 = false;
				if (null == this.J) {
					this.J = (Field) this.I.A();
					var10 = true;
				}

				if ("row-post-pack".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPostRowPackEvent(var9);
					}
				} else if ("row-pre-pack".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPreRowPackEvent(var9);
					}
				} else if ("row-post-parse".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPostRowParseEvent(var9);
					}
				} else if ("row-pre-parse".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPreRowParseEvent(var9);
					}
				} else if ("post-pack".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPostPackEvent(var9);
					} else {
						this.message.setPostPackEvent(var9);
					}
				} else if ("pre-pack".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPrePackEvent(var9);
					} else {
						this.message.setPrePackEvent(var9);
					}
				} else if ("post-parse".equalsIgnoreCase(this.F)) {
					if (this.J != null) {
						this.J.setPostParseEvent(var9);
					} else {
						this.message.setPostParseEvent(var9);
					}
				} else {
					if (!"pre-parse".equalsIgnoreCase(this.F)) {
						throw new RuntimeException(this.beanFilePath + ": Unsupport Event Type[" + this.F + "]!");
					}

					if (this.J != null) {
						this.J.setPreParseEvent(var9);
					} else {
						this.message.setPreParseEvent(var9);
					}
				}

				if (var10 && null != this.J) {
					this.I.A(this.J);
				}

				this.E = null;
			} else if ("template".equals(var3)) {
				var9 = this.E;
				if (var9 != null) {
					var9 = var9.trim();
				}

				if (0 == var9.length()) {
					var9 = null;
				}

				this.message.setTemplate(var9);
				this.E = null;
			} else if ("message-bean".equals(var3) && this.B.size() != 0) {
				this.message.setVariable(new HashMap());
				this.message.getVariable().putAll(this.B);
				this.B.clear();
			}
		}

	}

	@Override
	public void characters(char[] var1, int var2, int var3) throws SAXException {
		if (null == this.E) {
			this.E = new String(var1, var2, var3);
		} else {
			this.E = this.E + new String(var1, var2, var3);
		}

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		String attrValue = "";
		String var5;
		Message var6;
		int var9;
		if (EnumConstants.MESSAGE_BEAN_NAME.equals(qName)) {
			attrValue = attrs.getValue(EnumConstants.MessageAttr.ID.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": message-bean/@id ");
			this.message.setId(attrValue);

			var5 = attrs.getValue("type");
			if (null != var5) {
				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new RuntimeException(this.beanFilePath + ": message-bean/@type ");
				}

				this.message.setType(Constant.getMessageTypeByText(var5));
			}

			this.message.setShortText(attrs.getValue("short-text"));
			this.message.setGroupId(this.channelId);
			var5 = attrs.getValue("class");
			if (null == var5) {
				throw new RuntimeException(this.beanFilePath + ": message-bean/@class ");
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new RuntimeException(this.beanFilePath + ": message-bean/@class ");
			}

			if (!"com.giantstone.message.bean.CommonMessageBean".equals(var5)
					&& MessageMetadataManager.access$100().containsKey(var5)) {
				var6 = (Message) MessageMetadataManager.access$100().get(var5);
				if (this.channelId.equalsIgnoreCase(var6.getGroupId())) {
					throw new RuntimeException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
							.getString("MessageMetadataManager.startElement.messageClass.reduplicate"));
				}
			}

			this.message.setClassName(var5);
			var5 = attrs.getValue("xpath");
			this.message.setXpath(var5);
			var5 = attrs.getValue("schema-valid");
			if ("true".equals(var5)) {
				this.message.setSchemaValid(true);
			}

			var5 = attrs.getValue("schema-valid-type");
			if (null != var5) {
				var5 = var5.trim();
				if (0 != var5.length()) {
					this.message.setSchemaValidType(Constant.getSchemaTypeByTxt(var5));
				}
			}

			var5 = attrs.getValue("schema-valid-path");
			this.message.setSchemaValidPath(var5);
			var5 = attrs.getValue("message-charset");
			if (null != var5 && 0 != var5.trim().length()) {
				this.message.setMsgCharset(var5);
			}

			if (1002 == this.message.getType() || 1003 == this.message.getType()) {
				var5 = attrs.getValue("prefix");
				if (null == var5) {
					throw new RuntimeException(this.beanFilePath + ": Tag/Swift message-bean/@prefix "
							+ MultiLanguageResourceBundle.getInstance().getString("null"));
				}

				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new RuntimeException(this.beanFilePath + ": Tag/Swift message-bean/@prefix "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				String var36;
				if (var5.length() > 2 && var5.startsWith("0x")) {
					var36 = var5.substring(2);
					if (!CodeUtil.isHexString(var36)) {
						throw new RuntimeException(
								this.beanFilePath + ": Tag/Swift message-bean/@prefix " + MultiLanguageResourceBundle
										.getInstance().getString("MessageMetadataManager.startElement.notHexString"));
					}

					this.message.setPrefixString(var36);
					this.message.setPrefix(CodeUtil.HextoByte(var36));
				} else {
					this.message.setPrefixString(new String(CodeUtil.BCDtoASC(var5.getBytes())));
					this.message.setPrefix(var5.getBytes());
				}

				var5 = attrs.getValue("suffix");
				if (null == var5) {
					throw new RuntimeException(this.beanFilePath + ": Tag/Swift message-bean/@suffix "
							+ MultiLanguageResourceBundle.getInstance().getString("null"));
				}

				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new RuntimeException(this.beanFilePath + ": Tag/Swift message-bean/@suffix "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				if (var5.length() > 2 && var5.startsWith("0x")) {
					var36 = var5.substring(2);
					if (!CodeUtil.isHexString(var36)) {
						throw new RuntimeException(
								this.beanFilePath + ": Tag/Swift message-bean/@suffix " + MultiLanguageResourceBundle
										.getInstance().getString("MessageMetadataManager.startElement.notHexString"));
					}

					this.message.setSuffixString(var36);
					this.message.setSuffix(CodeUtil.HextoByte(var36));
				} else {
					this.message.setSuffixString(new String(CodeUtil.BCDtoASC(var5.getBytes())));
					this.message.setSuffix(var5.getBytes());
				}
			}

			var5 = attrs.getValue("extended-attributes");
			String[] var37;
			int var50;
			if (null != var5) {
				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new RuntimeException(this.beanFilePath + ": message/@extended-attributes "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				try {
					this.message.setExtendedAttributeText(var5);
					var37 = var5.split(";");

					for (var50 = 0; var50 < var37.length; ++var50) {
						this.message.setExtendAttribute(var37[var50].substring(0, var37[var50].indexOf(":")),
								var37[var50].substring(var37[var50].indexOf(":") + 1));
					}
				} catch (Exception var32) {
					throw new RuntimeException(
							this.beanFilePath + ": message/@extended-attributes " + MultiLanguageResourceBundle
									.getInstance().getString("MessageMetadataManager.startElement.wrong"));
				}
			}

			var5 = attrs.getValue("name-space");
			if (null != var5) {
				var5 = var5.trim();
				if (0 != var5.length()) {
					try {
						var37 = var5.split("\\|");
						boolean var51 = false;
						HashMap var49 = new HashMap();

						for (var9 = 0; var9 < var37.length; ++var9) {
							var50 = var37[var9].indexOf("=");
							var49.put(var37[var9].substring(0, var50), var37[var9].substring(var50 + 1));
						}

						this.message.setNameSpaces(var49);
					} catch (Exception var31) {
						throw new RuntimeException(
								this.beanFilePath + ": message/@name-space" + MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong"));
					}
				}
			}

			var5 = attrs.getValue("remove_blank_node");
			if (null != var5) {
				var5 = var5.trim();
				if (0 != var5.length() && "true".equalsIgnoreCase(var5)) {
					this.message.setRemoveBlankNode(true);
				}
			}
		} else if (EnumConstants.FIELD_BEAN_NAME.equals(qName)) {
			Field var40 = (Field) this.I.A();
			if (var40 != null && var40.getFieldType() != 2002 && var40.getFieldType() != 2003
					&& var40.getFieldType() != 2004 && var40.getFieldType() != 2011) {
				throw new RuntimeException(this.beanFilePath + ": "
						+ MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.startElement.fieldType.illegal",
								new String[] { var40.getName() }));
			}

			Field var35 = new Field();
			String var7 = attrs.getValue("name");
			if (null == var7) {
				throw new RuntimeException(this.beanFilePath + ": field/@name "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var7 = var7.trim();
			if (0 == var7.length()) {
				throw new RuntimeException(this.beanFilePath + ": field/@name "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			if (MessageMetadataManager.keyWordSet.contains(var7)) {
				throw new RuntimeException(
						this.beanFilePath + ": field/@name [" + var7 + "] " + MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.startElement.field.isReserved"));
			}

			if (var40 != null) {
				if (var40.getSubFields().containsKey(var7)) {
					throw new RuntimeException(this.beanFilePath + ": "
							+ MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.startElement.field.subField.reduplicate",
									new String[] { var40.getName() })
							+ ":" + var7);
				}
			} else if (this.message.getFields().containsKey(var7)) {
				throw new RuntimeException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.startElement.field.reduplicate") + ":" + var7);
			}

			var35.setName(var7);
			var7 = attrs.getValue("field-type");
			if (null == var7) {
				throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "']/@field-type "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var7 = var7.trim();
			if (0 == var7.length()) {
				throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "']/@field-type "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			try {
				var35.setFieldType(Constant.getFieldTypeByText(var7));
			} catch (Exception var30) {
				throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "']/@field-type "
						+ MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.startElement.wrong"));
			}

			var7 = attrs.getValue("padding");
			byte[] var8;
			if (null != var7) {
				var7 = var7.trim();
				if (0 != var7.length()) {
					var8 = var7.getBytes();
					if (1 == var8.length) {
						var35.setPadding(var8[0]);
					} else {
						if (4 != var8.length || !var7.startsWith("0x")) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
									+ "']/@padding " + MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.padding.format.wrong"));
						}

						var8 = CodeUtil.HextoByte(var7.substring(2));
						var35.setPadding(var8[0]);
					}
				}
			}

			var7 = attrs.getValue("padding-direction");
			if (null != var7) {
				var7 = var7.trim();
				if (0 != var7.length()) {
					if ("right".equalsIgnoreCase(var7)) {
						var35.setPaddingDirection(5002);
					} else if ("left".equalsIgnoreCase(var7)) {
						var35.setPaddingDirection(5001);
					} else {
						if (!"none".equalsIgnoreCase(var7)) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
									+ "']/@padding-direction " + MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.paddingDirection.format.wrong"));
						}

						var35.setPaddingDirection(5000);
					}
				}
			}

			var7 = attrs.getValue("required");
			if (null != var7) {
				var7 = var7.trim();
				if ("true".equalsIgnoreCase(var7)) {
					var35.setRequired(true);
				} else {
					var35.setRequired(false);
				}
			} else {
				var35.setRequired(MessageMetadataManager.isRequired);
			}

			var7 = attrs.getValue("editable");
			if (null != var7) {
				var7 = var7.trim();
				if ("false".equalsIgnoreCase(var7)) {
					var35.setEditable(false);
				}
			}

			var7 = attrs.getValue("isRemoveUnwatchable");
			if (null != var7) {
				var7 = var7.trim();
				if ("false".equalsIgnoreCase(var7)) {
					var35.setRemoveUnwatchable(false);
				}
			}

			if (var35.isEditable()) {
				var7 = attrs.getValue("xpath");
				var35.setXpath(var7);
			}

			var35.setShortText(attrs.getValue("short-text"));
			var7 = attrs.getValue("iso8583-no");
			if (null != var7) {
				var7 = var7.trim();
				if (0 == var7.length()) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
							+ "']/@iso8583-no " + MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				try {
					var35.setIso8583_no(Integer.parseInt(var7));
				} catch (Exception var29) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
							+ "']/@iso8583-no " + MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.wrong"));
				}
			}

			var7 = attrs.getValue("prefix");
			String var38;
			if (null != var7 && var7.length() > 0) {
				if (var7.length() > 2 && var7.startsWith("0x")) {
					var38 = var7.substring(2);
					if (!CodeUtil.isHexString(var38)) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@prefix " + MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.notHexString"));
					}

					var35.setPrefixString(var38);
					var35.setPrefix(CodeUtil.HextoByte(var38));
				} else {
					var35.setPrefixString(new String(CodeUtil.BCDtoASC(var7.getBytes())));
					var35.setPrefix(var7.getBytes());
				}

				var7 = attrs.getValue("fir-row-prefix");
				if (null != var7) {
					var7 = var7.trim();
					if (0 != var7.length() && "true".equalsIgnoreCase(var7)) {
						var35.setFirRowPrefix(true);
					}
				}
			}

			var7 = attrs.getValue("suffix");
			if (null != var7 && var7.length() > 0) {
				if (var7.length() > 2 && var7.startsWith("0x")) {
					var38 = var7.substring(2);
					if (!CodeUtil.isHexString(var38)) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@suffix " + MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.notHexString"));
					}

					var35.setSuffixString(var38);
					var35.setSuffix(CodeUtil.HextoByte(var38));
				} else {
					var35.setSuffix(var7.getBytes());
					var35.setSuffixString(new String(CodeUtil.BCDtoASC(var7.getBytes())));
				}

				var7 = attrs.getValue("last-row-suffix");
				if (null != var7) {
					var7 = var7.trim();
					if (0 != var7.length() && "true".equalsIgnoreCase(var7)) {
						var35.setLastRowSuffix(true);
					}
				}
			}

			var7 = attrs.getValue("extended-attributes");
			String[] var39;
			if (null != var7) {
				var7 = var7.trim();
				if (0 == var7.length()) {
					throw new RuntimeException(
							this.beanFilePath + ": field[@name='" + var35.getName() + "']/@extended-attributes "
									+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				try {
					var35.setExtendedAttributeText(var7);
					var39 = var7.split(";");

					for (var9 = 0; var9 < var39.length; ++var9) {
						var35.setExtendAttribute(var39[var9].substring(0, var39[var9].indexOf(":")),
								var39[var9].substring(var39[var9].indexOf(":") + 1));
					}
				} catch (Exception var34) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
							+ "']/@extended-attributes " + MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.wrong"));
				}
			}

			var7 = attrs.getValue("data-charset");
			if (null != var7) {
				var7 = var7.trim();
				if (0 != var7.length()) {
					var35.setDataCharset(var7);
				}
			}

			var7 = attrs.getValue("shield");
			if (null != var7) {
				var7 = var7.trim();
				if ("true".equals(var7)) {
					var35.setShield(true);
				}
			}

			if (2011 == var35.getFieldType()) {
				var7 = attrs.getValue("row-cut");
				if (null != var7) {
					var7 = var7.trim();
					if ("false".equalsIgnoreCase(var7)) {
						var35.setRowCut(false);
					}
				}
			}

			if (2001 == var35.getFieldType() || 2003 == var35.getFieldType() || 2009 == var35.getFieldType()
					|| 2011 == var35.getFieldType()) {
				var7 = attrs.getValue("ref-length-field");
				if (null != var7) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getName() + "']/@ref-length-field "
										+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					var35.setRefLengthFieldName(var7);
					var7 = attrs.getValue("ref-length-field-offset");
					if (null != var7) {
						try {
							var35.setRefLengthFieldOffset(Integer.parseInt(var7));
						} catch (Exception var28) {
							throw new RuntimeException(
									this.beanFilePath + ": field[@name='" + var35.getName()
											+ "']/@ref-length-field-offset "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.wrong")
											+ var28.getMessage(),
									var28);
						}
					}
				} else if (2011 != var35.getFieldType()) {
					var7 = attrs.getValue("length-field-data-type");
					if (null == var7) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getName() + "']/@length-field-data-type "
										+ MultiLanguageResourceBundle.getInstance().getString("null"));
					}

					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getName() + "']/@length-field-data-type "
										+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					try {
						var35.setLengthFieldDataType(Constant.getDataTypeByText(var7));
					} catch (Exception var27) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@length-field-data-type " + MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong"));
					}

					var7 = attrs.getValue("length-field-data-encoding");
					if (null != var7) {
						var7 = var7.trim();
						if ("bcd".equalsIgnoreCase(var7)) {
							var35.setLengthFieldDataEncoding(4001);
						}
					}

					var7 = attrs.getValue("length-field-length");
					if (null == var7) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getName() + "']/@length-field-length "
										+ MultiLanguageResourceBundle.getInstance().getString("null"));
					}

					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getCalcType() + "']/@length-field-length "
										+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					try {
						var35.setLengthFieldLength(Integer.parseInt(var7));
					} catch (Exception var26) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@length-field-length " + MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong"));
					}
				} else {
					var7 = attrs.getValue("tab-prefix");
					if (null != var7) {
						var7 = var7.trim();
						if (0 != var7.length()) {
							if (var7.length() > 2 && var7.startsWith("0x")) {
								var38 = var7.substring(2);
								if (!CodeUtil.isHexString(var38)) {
									throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
											+ "']/@tab-suffix " + MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"));
								}

								var35.setTabPrefixString(var7);
								var35.setTabPrefix(CodeUtil.HextoByte(var38));
							} else {
								var35.setTabPrefixString(new String(CodeUtil.BCDtoASC(var7.getBytes())));
								var35.setTabPrefix(var7.getBytes());
							}
						}
					}

					var7 = attrs.getValue("tab-suffix");
					if (null != var7) {
						var7 = var7.trim();
						if (0 != var7.length()) {
							if (var7.length() > 2 && var7.startsWith("0x")) {
								var38 = var7.substring(2);
								if (!CodeUtil.isHexString(var38)) {
									throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
											+ "']/@tab-suffix " + MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.notHexString"));
								}

								var35.setTabSuffixString(var7);
								var35.setTabSuffix(CodeUtil.HextoByte(var38));
							} else {
								var35.setTabSuffixString(new String(CodeUtil.BCDtoASC(var7.getBytes())));
								var35.setTabSuffix(var7.getBytes());
							}
						}
					}
				}

				var7 = attrs.getValue("max-length");
				if (null != var7) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@max-length " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					try {
						var35.setMaxLength(Integer.parseInt(var7));
					} catch (Exception var25) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@max-length " + MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong"));
					}
				}

				var7 = attrs.getValue("min-length");
				if (null != var7) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@min-length " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					try {
						var35.setMinLength(Integer.parseInt(var7));
					} catch (Exception var24) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getName() + "']/@min-length "
										+ MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.wrong")
										+ var24.getMessage(),
								var24);
					}
				}
			}

			if (1002 == this.message.getType() || 1003 == this.message.getType()) {
				var7 = attrs.getValue("tag");
				if (null != var7) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "']/@tag "
								+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					var35.setTag(var7);
				}
			}

			if (2002 != var35.getFieldType() && 2003 != var35.getFieldType() && 2004 != var35.getFieldType()
					&& 2011 != var35.getFieldType() && 2008 != var35.getFieldType() && 2009 != var35.getFieldType()) {
				if (2007 == var35.getFieldType() || 2012 == var35.getFieldType()) {
					var7 = attrs.getValue("start-field");
					if (null == var7) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + "'] "
								+ MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.lengthField.startField.null"));
					}

					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "'] "
								+ MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.lengthField.startField.blank"));
					}

					var35.setStartFieldName(var7);
					var7 = attrs.getValue("end-field");
					if (null != var7 && 0 != var7.trim().length()) {
						var35.setEndFieldName(var7.trim());
					} else {
						var35.setEndFieldName(var35.getStartFieldName());
					}
				}

				if (2010 == var35.getFieldType()) {
					var7 = attrs.getValue("start-field");
					if (null != var7) {
						var7 = var7.trim();
						if (0 == var7.length()) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "'] "
									+ MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.lengthField.startField.blank"));
						}

						var35.setStartFieldName(var7);
						var7 = attrs.getValue("end-field");
						if (null != var7 && 0 != var7.trim().length()) {
							var35.setEndFieldName(var7.trim());
						} else {
							var35.setEndFieldName(var35.getStartFieldName());
						}
					}

					var7 = attrs.getValue("data");
					if (null != var7) {
						var7 = var7.trim();
						if (0 != var7.length()) {
							var39 = var7.split(";");
							HashMap var41 = new HashMap();

							for (int var10 = 0; var10 < var39.length; ++var10) {
								var41.put(var39[var10], var39[var10]);
							}

							var35.setMacFldDataCache(var41);
						}
					}
				}

				if (2005 == var35.getFieldType()) {
					var7 = attrs.getValue("table");
					if (null == var7) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@table " + MultiLanguageResourceBundle.getInstance().getString("null"));
					}

					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@table " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					var35.setTableName(var7);
				}

				var7 = attrs.getValue("data-type");
				if (null == var7) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
							+ "']/@data-type " + MultiLanguageResourceBundle.getInstance().getString("null"));
				}

				var7 = var7.trim();
				if (0 == var7.length()) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
							+ "']/@data-type " + MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				try {
					var35.setDataType(Constant.getDataTypeByText(var7));
				} catch (Exception var23) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
							+ "']/@data-type "
							+ MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.wrong")
							+ var23.getMessage(), var23);
				}

				var7 = attrs.getValue("data-encoding");
				if (null != var7) {
					var7 = var7.trim();
					if ("bcd".equalsIgnoreCase(var7)) {
						var35.setDataEncoding(4001);
					}
				}

				var7 = attrs.getValue("pattern");
				if (null != var7) {
					var7 = var7.trim();
					if (0 != var7.length()) {
						var35.setPattern(var7);
					}
				}

				var7 = attrs.getValue("length-script");
				if (null != var7) {
					var7 = var7.trim();
					if (0 != var7.length()) {
						var35.setLengthScript(var7);
					}
				}

				if (2001 != var35.getFieldType()) {
					if (3000 != var35.getDataType() && 3001 != var35.getDataType() && 3002 != var35.getDataType()
							&& 3010 != var35.getDataType() && 3011 != var35.getDataType()) {
						if (3006 == var35.getDataType()) {
							if (null == var35.getPattern()) {
								throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
										+ "'] " + MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.datatime.pattern.null"));
							}

							try {
								new SimpleDateFormat(var35.getPattern());
							} catch (Exception var21) {
								throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
										+ "'] " + MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.datatime.pattern.wrong"));
							}

							var35.setLength(var35.getPattern().length());
						} else {
							var7 = attrs.getValue("length");
							if (var7 != null) {
								var7 = var7.trim();
							}

							boolean var45 = false;

							int var46;
							try {
								var46 = Integer.parseInt(var7);
							} catch (Exception var20) {
								throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
										+ "']/@length[ " + var7 + "] " + MultiLanguageResourceBundle.getInstance()
												.getString("MessageMetadataManager.startElement.format.wrong"));
							}

							switch (var35.getDataType()) {
							case 3003:
							case 3007:
								if (var46 != 4) {
									throw new RuntimeException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.length.notEqual4",
													new String[] { var35.getName() }));
								}
								break;
							case 3004:
								if (var46 != 1) {
									throw new RuntimeException(
											this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.length.notEqual1"));
								}
								break;
							case 3005:
							case 3008:
								if (var46 != 2) {
									throw new RuntimeException(
											this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.length.notEqual2"));
								}
								break;
							case 3006:
							default:
								throw new RuntimeException(this.beanFilePath + ": "
										+ MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.unsupportDataType",
												new String[] { var35.getName() })
										+ ":" + var35.getDataType());
							case 3009:
								if (var46 != 8) {
									throw new RuntimeException(
											this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
													.getString("MessageMetadataManager.startElement.length.notEqual8"));
								}
							}

							var35.setLength(var46);
						}
					} else {
						var7 = attrs.getValue("length");
						if (null == var7) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
									+ "']/@length " + MultiLanguageResourceBundle.getInstance().getString("null"));
						}

						var7 = var7.trim();
						if (0 == var7.length()) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
									+ "']/@length " + MultiLanguageResourceBundle.getInstance().getString("blank"));
						}

						try {
							var35.setLength(Integer.parseInt(var7));
						} catch (Exception var22) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
									+ "']/@length[" + var7 + "] " + MultiLanguageResourceBundle.getInstance()
											.getString("MessageMetadataManager.startElement.format.wrong"));
						}
					}
				}

				var7 = attrs.getValue("strict-data-length");
				if (null != var7) {
					var7 = var7.trim();
					if ("true".equalsIgnoreCase(var7)) {
						var35.setStrictDataLength(true);
					}
				}

				if (2001 == var35.getFieldType()) {
					var35.setPaddingDirection(5000);
				}

				var7 = attrs.getValue("value");
				if (null != var7) {
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": "
								+ MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.defaultValue.null",
										new String[] { var35.getName(), var7 }));
					}

					if (2000 != var35.getFieldType() && 2005 != var35.getFieldType()) {
						throw new RuntimeException(this.beanFilePath + ": "
								+ MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.canNotHave.defaultValue.2",
										new String[] { var35.getName(),
												Constant.getFieldTypeText(var35.getFieldType()) }));
					}

					if (3000 != var35.getDataType() && 3001 != var35.getDataType() && 3010 != var35.getDataType()
							&& 3011 != var35.getDataType()) {
						if (3003 != var35.getDataType() && 3007 != var35.getDataType()) {
							if (3005 != var35.getDataType() && 3008 != var35.getDataType()) {
								if (3004 == var35.getDataType()) {
									try {
										if (0 != var7.length()) {
											var8 = var7.getBytes();
											if (1 != var8.length) {
												if (4 != var8.length || !var7.startsWith("0x")) {
													throw new RuntimeException(this.beanFilePath + ": field[@name='"
															+ var35.getName() + "']/@padding "
															+ MultiLanguageResourceBundle.getInstance().getString(
																	"MessageMetadataManager.startElement.padding.format.wrong"));
												}

												var8 = CodeUtil.HextoByte(var7.substring(2));
											}
										}
									} catch (Exception var33) {
										throw new RuntimeException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notByteNumber",
														new String[] { var35.getName(), var7 }),
												var33);
									}
								} else if (3006 == var35.getDataType()) {
									SimpleDateFormat var47 = new SimpleDateFormat(var35.getPattern());
									var47.setLenient(false);

									try {
										var47.parse(var7);
									} catch (Exception var15) {
										throw new RuntimeException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.datetime.illegalPattern",
														new String[] { var35.getName(), var7, var35.getPattern() }));
									}
								} else if (3009 == var35.getDataType()) {
									try {
										Long.parseLong(var7);
									} catch (Exception var14) {
										throw new RuntimeException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notLongNumber",
														new String[] { var35.getName(), var7 }),
												var14);
									}
								} else {
									if (3002 != var35.getDataType()) {
										throw new RuntimeException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.canNotHave.defaultValue.1",
														new String[] { var35.getName(),
																Constant.getDataTypeText(var35.getDataType()) }));
									}

									var8 = var7.trim().getBytes();
									if (var8.length % 2 != 0) {
										throw new RuntimeException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notHexString",
														new String[] { var35.getName(),
																Constant.getFieldTypeText(var35.getFieldType()) }));
									}

									if (var8.length / 2 != var35.getLength()) {
										throw new RuntimeException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.hexString.illegalLength",
														new String[] { var35.getName(),
																Constant.getFieldTypeText(var35.getFieldType()) })
												+ " " + var35.getLength() * 2);
									}

									for (var9 = 0; var9 < var8.length; ++var9) {
										if ((var8[var9] < 48 || var8[var9] > 57)
												&& (var8[var9] < 97 || var8[var9] > 102)
												&& (var8[var9] < 65 || var8[var9] > 70)) {
											throw new RuntimeException(this.beanFilePath + ": "
													+ MultiLanguageResourceBundle.getInstance().getString(
															"MessageMetadataManager.startElement.field.defaultValue.notHexString",
															new String[] { var35.getName(),
																	Constant.getFieldTypeText(var35.getFieldType()) }));
										}
									}
								}
							} else {
								try {
									Short.parseShort(var7);
								} catch (Exception var16) {
									throw new RuntimeException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notShortNumber",
													new String[] { var35.getName(), var7 })
											+ var16.getMessage(), var16);
								}
							}
						} else {
							try {
								Integer.parseInt(var7);
							} catch (Exception var17) {
								throw new RuntimeException(this.beanFilePath + ": "
										+ MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notIntNumber",
												new String[] { var35.getName(), var7 }));
							}
						}
					} else {
						if (var7.length() > var35.getLength()) {
							throw new RuntimeException(this.beanFilePath + ": "
									+ MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.defaultValue.tooLong",
											new String[] { var35.getName(), var7 }));
						}

						if (var35.isStrictDataLength() && var7.length() < var35.getLength()) {
							throw new RuntimeException(this.beanFilePath + ": "
									+ MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.defaultValue.tooShort",
											new String[] { var35.getName(), var7, "" + var35.getLength() }));
						}

						if (3001 == var35.getDataType()) {
							try {
								Integer.parseInt(var7);
							} catch (Exception var19) {
								throw new RuntimeException(this.beanFilePath + ": "
										+ MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notNumber",
												new String[] { var35.getName(), var7 })
										+ var19.getMessage(), var19);
							}
						} else if (3010 == var35.getDataType()) {
							try {
								new BigDecimal(var7);
							} catch (Exception var18) {
								throw new RuntimeException(this.beanFilePath + ": "
										+ MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notDecimalNumber",
												new String[] { var35.getName(), var7 }));
							}
						}
					}

					var35.setValue(var7);
				}

				var7 = attrs.getValue("ref-value-range");
				if (null != var7) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(
								this.beanFilePath + ": field[@name='" + var35.getName() + "']/@ref-value-range "
										+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					var35.setRefValueRange(var7);
					File var48 = (File) MessageMetadataManager.access$300().get(this.channelId);
					String var43 = var48.getAbsolutePath();
					if (!var43.endsWith(System.getProperty("file.separator"))) {
						var43 = var43 + System.getProperty("file.separator");
					}

					String var44 = var43 + "value-range" + System.getProperty("file.separator") + var7 + ".xml";
					File var11 = new File(var44);
					MessageMetadataManager._B var12 = new MessageMetadataManager._B((1));
					this.G = var12.A(this.channelId, var11);
				}
			} else {
				var7 = attrs.getValue("reference-type");
				if (null != var7) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@reference-type " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					var35.setReferenceType(var7);
				}

				var7 = attrs.getValue("reference");
				if (null == var7 && (2008 == var35.getFieldType() || 2009 == var35.getFieldType())) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName() + "'] "
							+ MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.reference.null"));
				}

				if (var7 != null) {
					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
								+ "']/@reference " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					if (!"dynamic".equalsIgnoreCase(var35.getReferenceType())
							&& !"expression".equalsIgnoreCase(var35.getReferenceType())) {
						Message var42 = MessageMetadataManager.getMessage(this.channelId, var7);
						var35.setReference(var42);
					}

					var35.setReferenceId(var7);
				}

				var35.setSubFields(new TreeMap());
				if (2004 == var35.getFieldType()) {
					var7 = attrs.getValue("row-num-field");
					if (var7 != null) {
						var7 = var7.trim();
						if (0 == var7.length()) {
							throw new RuntimeException(
									this.beanFilePath + ": field[@name='" + var35.getName() + "']/@row-num-field "
											+ MultiLanguageResourceBundle.getInstance().getString("blank"));
						}

						var35.setRowNumFieldName(var7);
					}

					var7 = attrs.getValue("row-xpath");
					if (var7 != null) {
						var7 = var7.trim();
						if (0 == var7.length()) {
							throw new RuntimeException(this.beanFilePath + ": field[@name='" + var35.getName()
									+ "']/@row-xpath " + MultiLanguageResourceBundle.getInstance().getString("blank"));
						}

						var35.setRowXpath(var7);
					}
				}

				var7 = attrs.getValue("data-encoding");
				if (null != var7) {
					var7 = var7.trim();
					if ("bcd".equalsIgnoreCase(var7)) {
						var35.setDataEncoding(4001);
					}
				}
			}

			var7 = attrs.getValue("class");
			if (var35.getFieldType() == 2010) {
				var38 = attrs.getValue("calc-type");
				if (null == var38) {
					throw new RuntimeException(this.beanFilePath + ": mac-field/@calc-type "
							+ MultiLanguageResourceBundle.getInstance().getString("null"));
				}

				var38 = var38.trim();
				if (0 == var38.length()) {
					throw new RuntimeException(this.beanFilePath + ": mac-field/@calc-type "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				var35.setCalcType(var38);
				if ("java".equalsIgnoreCase(var35.getCalcType())) {
					if (null == var7) {
						throw new RuntimeException(this.beanFilePath + ": mac-field/@class "
								+ MultiLanguageResourceBundle.getInstance().getString("null"));
					}

					var7 = var7.trim();
					if (0 == var7.length()) {
						throw new RuntimeException(this.beanFilePath + ": mac-field/@class "
								+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					var35.setCombineOrTableFieldClassName(var7);
				}
			} else if (null != var7) {
				if (var35.getFieldType() != 2002 && var35.getFieldType() != 2003 && var35.getFieldType() != 2004
						&& var35.getFieldType() != 2011 && var35.getFieldType() != 2010 && var35.getFieldType() != 2008
						&& var35.getFieldType() != 2001) {
					throw new RuntimeException(this.beanFilePath + ": "
							+ MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.startElement.field.canNotHave.class",
									new String[] { var35.getName() }));
				}

				var7 = var7.trim();
				if (var7.length() > 0) {
					var35.setCombineOrTableFieldClassName(var7);
				}
			}

			if (var40 != null) {
				var40.setSubField(var35.getName(), var35);
				this.I.A(var40);
			} else {
				this.message.setField(var35.getName(), var35);
			}

			this.I.A(var35);
			this.J = var35;
		} else if (EnumConstants.VALUE_RANGE_NAME.equals(qName)) {
			this.G.clear();
			this.A = new ValueRange();
			var5 = attrs.getValue("default-ref");
			if (null != var5) {
				var5 = var5.trim();
				if (0 == var5.trim().length()) {
					throw new RuntimeException(
							this.beanFilePath + ": field[@name='" + this.J.getName() + "']/value-range/value-ref "
									+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				this.A.setValue("default-ref");
				this.A.setReferenceId(var5);
				var6 = MessageMetadataManager.getMessage(this.channelId, var5);
				this.A.setReference(var6);
				this.G.put("default-ref", this.A);
			}
		} else if (EnumConstants.VALUE_NAME.equals(qName)) {
			this.A = new ValueRange();
			var5 = attrs.getValue("value");
			if (null == var5) {
				throw new RuntimeException(this.beanFilePath + ": field[@name='" + this.J.getName()
						+ "']/value-range/value/@value " + MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new RuntimeException(
						this.beanFilePath + ": field[@name='" + this.J.getName() + "']/value-range/value/@value "
								+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			if (this.G.containsKey(var5)) {
				throw new RuntimeException(this.beanFilePath + ": field[@name='" + this.J.getName()
						+ "']/value-range/value[@value='" + var5 + "'] " + MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.startElement.reduplicate"));
			}

			this.A.setValue(var5);
			this.A.setShortText(attrs.getValue("short-text"));
			var5 = attrs.getValue("reference");
			if (var5 != null) {
				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new RuntimeException(this.beanFilePath + ": fiedl[@name='" + this.J.getName()
							+ "']/value-range/value/@reference "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				this.A.setReferenceId(var5);
				var6 = MessageMetadataManager.getMessage(this.channelId, var5);
				if (null == var6) {
					throw new RuntimeException(this.beanFilePath + ": "
							+ MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.startElement.field.valueRange.reference.notExist",
									new String[] { var5 }));
				}

				this.A.setReference(var6);
				if (null == this.G.get("default-ref")) {
					throw new RuntimeException(this.beanFilePath + ": field[@name='" + this.J.getName()
							+ "']/@default-ref " + MultiLanguageResourceBundle.getInstance().getString("null"));
				}
			}

			this.G.put(this.A.getValue(), this.A);
		} else if (EnumConstants.EVENT.equals(qName)) {
			var5 = attrs.getValue("type");
			if (null == var5) {
				throw new RuntimeException(this.beanFilePath + ": event/@type "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new RuntimeException(this.beanFilePath + ": event/@type "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			this.F = var5;
		} else if (EnumConstants.VARIABLE.equals(qName)) {
			this.H = new A();
			var5 = attrs.getValue("name");
			if (null == var5) {
				throw new RuntimeException(this.beanFilePath + ": variable/@name "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new RuntimeException(this.beanFilePath + ": variable/@name "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			this.H.B(var5);
			var5 = attrs.getValue("data-type");
			if (null == var5) {
				throw new RuntimeException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@data-type "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new RuntimeException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@data-type "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			try {
				this.H.A(Constant.getDataTypeByText(var5));
			} catch (Exception var13) {
				throw new RuntimeException(
						this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@data-type "
								+ MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong")
								+ var13.getMessage(),
						var13);
			}

			var5 = attrs.getValue("value");
			if (null == var5) {
				throw new RuntimeException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@value "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new RuntimeException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@value "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			this.H.C(var5);
			this.B.put(this.H.D(), this.H);
		}

	}
}
