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
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

public class MessageHandler extends DefaultHandler {
	public class _AB {
		private List<Field> fields;

		private _AB() {
			this.fields = new ArrayList<>();
		}

		public Field A() {
			return this.fields.isEmpty() ? null : this.fields.remove(this.fields.size() - 1);
		}

		public void A(Field field) {
			this.fields.add(field);
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
		Iterator<Field> var2;
		if (1002 == this.message.getType() || 1003 == this.message.getType()) {
			var2 = this.message.getFields().values().iterator();
			Set<String> var3 = new HashSet<>(this.message.getFields().size());

			while (var2.hasNext()) {
				var1 = var2.next();
				if (null == var1.getTag()) {
					throw new CommonException("");
				}

				if (1002 == this.message.getType() && 2004 != var1.getFieldType() && 2005 != var1.getFieldType()
						&& 2000 != var1.getFieldType() && 2002 != var1.getFieldType() && 2008 != var1.getFieldType()) {
					throw new CommonException("");
				}

				if (var3.contains(var1.getTag())) {
					throw new CommonException("");
				}

				var3.add(var1.getTag());
			}
		}

		if (1001 == this.message.getType()) {
			var2 = this.message.getFields().values().iterator();

			while (var2.hasNext()) {
				var1 = var2.next();
				if (2001 == var1.getFieldType() || 2003 == var1.getFieldType() || 2009 == var1.getFieldType()) {
					throw new CommonException("");
				}
			}
		}

	}

	private void A(Map var1, Field var2) {
		if (3001 != var2.getDataType() && 3003 != var2.getDataType() && 3005 != var2.getDataType()
				&& 3004 != var2.getDataType() && 3007 != var2.getDataType() && 3008 != var2.getDataType()
				&& 3009 != var2.getDataType()) {
			throw new CommonException("");
		} else {
			Field var3 = (Field) var1.get(var2.getStartFieldName());
			if (null == var3) {
				throw new CommonException("");
			} else {
				var2.setStartField(var3);
				if (var2.getStartFieldName().equals(var2.getEndFieldName())) {
					var2.setEndField(var2.getStartField());
				} else {
					Field var4 = (Field) var1.get(var2.getEndFieldName());
					if (null == var4) {
						throw new CommonException("");
					}

					var2.setEndField(var4);
				}

			}
		}
	}

	private void A(Map<String, Field> var1) {
		Field var2 = null;
		Iterator<Field> var3 = var1.values().iterator();
		int var4 = 0;

		do {
			do {
				do {
					do {
						do {
							if (!var3.hasNext()) {
								return;
							}

							var2 = var3.next();
							++var4;
							if (("dynamic".equalsIgnoreCase(var2.getReferenceType())
									|| "expression".equalsIgnoreCase(var2.getReferenceType()))
									&& 2002 != var2.getFieldType() && 2003 != var2.getFieldType()
									&& 2004 != var2.getFieldType()) {
								throw new CommonException("");
							}

							Field var5;
							if (2001 == var2.getFieldType() || 2003 == var2.getFieldType()
									|| 2009 == var2.getFieldType()
									|| 2011 == var2.getFieldType() && 0 != var2.getLengthFieldLength()) {
								if (var2.getRefLengthFieldName() != null) {
									var5 = (Field) var1.get(var2.getRefLengthFieldName());
									if (null == var5) {
										throw new CommonException("");
									}

									if (2012 != var5.getFieldType() && 2007 != var5.getFieldType()) {
										throw new CommonException("");
									}

									if (2012 == var5.getFieldType() && 3003 != var5.getDataType()) {
										throw new CommonException("");
									}

									var2.setRefLengthField(var5);
								} else {
									switch (var2.getLengthFieldDataType()) {
									case 3001:
										break;
									case 3002:
									case 3006:
									default:
										throw new CommonException("");
									case 3003:
									case 3007:
										if (var2.getLengthFieldLength() != 4) {
											throw new CommonException("");
										}
										break;
									case 3004:
										if (var2.getLengthFieldLength() != 1) {
											throw new CommonException("");
										}
										break;
									case 3005:
									case 3008:
										if (var2.getLengthFieldLength() != 2) {
											throw new CommonException("");
										}
										break;
									case 3009:
										if (var2.getLengthFieldLength() != 8) {
											throw new CommonException("");
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
									throw new CommonException("");
								}

								var5 = (Field) var1.get(var2.getTableName());
								if (null == var5) {
									throw new CommonException("");
								}

								if (var5.getFieldType() != 2004 && var5.getFieldType() != 2011) {
									throw new CommonException("");
								}

								var2.setTable(var5);
							}

							if (2004 == var2.getFieldType() && var2.getRowNumFieldName() != null) {
								var5 = var1.get(var2.getRowNumFieldName());
								if (null != var5) {
									if (var5.getFieldType() != 2005) {
										throw new CommonException("");
									}

									var2.setRowNumField(var5);
								}
							}

							if (1002 == this.message.getType() && null == var2.getSubFields()
									&& null == var2.getReference()) {
								throw new CommonException("");
							}

							if (2010 == var2.getFieldType()) {
								if (3002 != var2.getDataType()) {
									throw new CommonException("");
								}

								var5 = var1.get(var2.getStartFieldName());
								if (null != var5) {
									var2.setStartField(var5);
									if (var2.getStartFieldName().equals(var2.getEndFieldName())) {
										var2.setEndField(var2.getStartField());
									} else {
										Field var6 = var1.get(var2.getEndFieldName());
										if (null == var6) {
											throw new CommonException("");
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

		throw new CommonException("");
	}

	private void A(String var1, Map<String, Field> var2) {
		Field var3 = null;
		Iterator<Field> var4 = var2.values().iterator();

		while (true) {
			do {
				if (!var4.hasNext()) {
					return;
				}

				var3 = var4.next();
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
					MessageMetadataManager.getClassCache().put(var5.getClassName(), var5);
				}

				this.A(var5.getClassName(), var5.getFields());
			}
		}
	}

	@Override
	public void endElement(String var1, String var2, String var3) throws SAXException {
		if ("field".equals(var3)) {
			Field var4 = this.I.A();
			if (var4.getFieldType() == 2002 && var4.getReference() == null
					&& !"dynamic".equalsIgnoreCase(var4.getReferenceType())
					&& !"expression".equalsIgnoreCase(var4.getReferenceType())) {
				if (var4.getReference() == null) {
					this.A(var4.getSubFields());
				}

				if (1002 == this.message.getType() && var4.getFieldType() == 2002 && var4.getReference() == null) {
					throw new CommonException("");
				}
			}

			if ((2002 == var4.getFieldType() || 2003 == var4.getFieldType() || 2004 == var4.getFieldType()
					|| 2011 == var4.getFieldType()) && var4.getReference() == null) {
				this.A(var4.getSubFields());
			}

			if ((2000 == var4.getFieldType() || 2001 == var4.getFieldType()) && 3010 == var4.getDataType()) {
				if (null == var4.getPattern()) {
					throw new CommonException("");
				}

				String[] var5 = var4.getPattern().split(",");
				if (var5.length > 2) {
					throw new CommonException(this.beanFilePath + ": field[@name='");
				}

				try {
					Integer.parseInt(var5[0].trim());
				} catch (Exception var8) {
					throw new CommonException(this.beanFilePath + " :");
				}

				if (var5.length == 2) {
					try {
						Integer.parseInt(var5[1].trim());
					} catch (Exception var7) {
						throw new CommonException(this.beanFilePath + " :");
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
					this.J = this.I.A();
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
						throw new CommonException(this.beanFilePath + ": Unsupport Event Type[" + this.F + "]!");
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
		String var5;
		Message var6;
		if (EnumConstants.MESSAGE_BEAN_NAME.equals(qName)) {
			buildMessageBean(attrs);
		} else if (EnumConstants.FIELD_BEAN_NAME.equals(qName)) {
			buildFieldBean(attrs);
		} else if (EnumConstants.VALUE_RANGE_NAME.equals(qName)) {
			this.G.clear();
			this.A = new ValueRange();
			var5 = attrs.getValue("default-ref");
			if (null != var5) {
				var5 = var5.trim();
				if (0 == var5.trim().length()) {
					throw new CommonException(
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
				throw new CommonException(this.beanFilePath + ": field[@name='" + this.J.getName()
						+ "']/value-range/value/@value " + MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new CommonException(
						this.beanFilePath + ": field[@name='" + this.J.getName() + "']/value-range/value/@value "
								+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			if (this.G.containsKey(var5)) {
				throw new CommonException(this.beanFilePath + ": field[@name='" + this.J.getName()
						+ "']/value-range/value[@value='" + var5 + "'] " + MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.startElement.reduplicate"));
			}

			this.A.setValue(var5);
			this.A.setShortText(attrs.getValue("short-text"));
			var5 = attrs.getValue("reference");
			if (var5 != null) {
				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new CommonException(this.beanFilePath + ": fiedl[@name='" + this.J.getName()
							+ "']/value-range/value/@reference "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				this.A.setReferenceId(var5);
				var6 = MessageMetadataManager.getMessage(this.channelId, var5);
				if (null == var6) {
					throw new CommonException(this.beanFilePath + ": "
							+ MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.startElement.field.valueRange.reference.notExist",
									new String[] { var5 }));
				}

				this.A.setReference(var6);
				if (null == this.G.get("default-ref")) {
					throw new CommonException(this.beanFilePath + ": field[@name='" + this.J.getName()
							+ "']/@default-ref " + MultiLanguageResourceBundle.getInstance().getString("null"));
				}
			}

			this.G.put(this.A.getValue(), this.A);
		} else if (EnumConstants.EVENT.equals(qName)) {
			var5 = attrs.getValue("type");
			if (null == var5) {
				throw new CommonException(this.beanFilePath + ": event/@type "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new CommonException(this.beanFilePath + ": event/@type "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			this.F = var5;
		} else if (EnumConstants.VARIABLE.equals(qName)) {
			this.H = new A();
			var5 = attrs.getValue("name");
			if (null == var5) {
				throw new CommonException(this.beanFilePath + ": variable/@name "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new CommonException(this.beanFilePath + ": variable/@name "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			this.H.B(var5);
			var5 = attrs.getValue("data-type");
			if (null == var5) {
				throw new CommonException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@data-type "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new CommonException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@data-type "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			try {
				this.H.A(Constant.getDataTypeByText(var5));
			} catch (Exception var13) {
				throw new CommonException(
						this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@data-type "
								+ MultiLanguageResourceBundle.getInstance()
										.getString("MessageMetadataManager.startElement.wrong")
								+ var13.getMessage(),
						var13);
			}

			var5 = attrs.getValue("value");
			if (null == var5) {
				throw new CommonException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@value "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var5 = var5.trim();
			if (0 == var5.length()) {
				throw new CommonException(this.beanFilePath + ": variable[@name='" + this.H.D() + "']/@value "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			this.H.C(var5);
			this.B.put(this.H.D(), this.H);
		}

	}

	private void buildMessageBean(Attributes attrs) {
		String attrValue = attrs.getValue(EnumConstants.MessageAttr.ID.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": message-bean/@id ");
		this.message.setId(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.TYPE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setType(Constant.getMessageTypeByText(attrValue));
		}

		this.message.setShortText(attrs.getValue(EnumConstants.MessageAttr.SHORT_TEXT.code()));
		this.message.setGroupId(this.channelId);
		attrValue = attrs.getValue(EnumConstants.MessageAttr.CLASS.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": message-bean/@class ");

		if (!"com.fib.gateway.message.bean.CommonMessageBean".equals(attrValue)
				&& MessageMetadataManager.getClassCache().containsKey(attrValue)) {
			Message messageTmp = MessageMetadataManager.getClassCache().get(attrValue);
			if (this.channelId.equalsIgnoreCase(messageTmp.getGroupId())) {
				throw new CommonException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.startElement.messageClass.reduplicate"));
			}
		}

		this.message.setClassName(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.XPATH.code());
		this.message.setXpath(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.SCHEMA_VALID.code());
		if ("true".equals(attrValue)) {
			this.message.setSchemaValid(true);
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.SCHEMA_VALID_TYPE.code());

		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setSchemaValidType(Constant.getSchemaTypeByTxt(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.SCHEMA_VALID_PATH.code());
		this.message.setSchemaValidPath(attrValue);

		attrValue = attrs.getValue(EnumConstants.MessageAttr.MESSAGE_CHARSET.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setMsgCharset(attrValue);
		}

		if (EnumConstants.MsgType.TAG.getCode() == this.message.getType()
				|| EnumConstants.MsgType.SWIFT.getCode() == this.message.getType()) {
			attrValue = attrs.getValue(EnumConstants.MessageAttr.PREFIX.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": Tag/Swift message-bean/@prefix");

			//
			if (HexUtil.isHexNumber(attrValue)) {
				String prefixTmp = attrValue.substring(2);
				this.message.setPrefixString(prefixTmp);
				this.message.setPrefix(HexUtil.decodeHex(prefixTmp));
			} else {
				this.message.setPrefixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				this.message.setPrefix(attrValue.getBytes());
			}

			attrValue = attrs.getValue(EnumConstants.MessageAttr.SUFFIX.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": Tag/Swift message-bean/@suffix ");

			if (HexUtil.isHexNumber(attrValue)) {
				String suffixTmp = attrValue.substring(2);
				this.message.setSuffixString(suffixTmp);
				this.message.setSuffix(HexUtil.decodeHex(suffixTmp));
			} else {
				this.message.setSuffixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				this.message.setSuffix(attrValue.getBytes());
			}
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.EXTENDED_ATTRIBUTES.code());
		if (StrUtil.isNotBlank(attrValue)) {
			this.message.setExtendedAttributeText(attrValue);
			String[] extendedAttrs = attrValue.split(";");
			for (int i = 0, len = extendedAttrs.length; i < len; ++i) {
				this.message.setExtendAttribute(extendedAttrs[i].substring(0, extendedAttrs[i].indexOf(":")),
						extendedAttrs[i].substring(extendedAttrs[i].indexOf(":") + 1));
			}
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.NAME_SPACE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			String[] namespaceAttrs = attrValue.split("\\|");
			Map<String, String> tmpMap = new HashMap<>();
			for (int i = 0, len = namespaceAttrs.length; i < len; ++i) {
				int var50 = namespaceAttrs[i].indexOf("=");
				tmpMap.put(namespaceAttrs[i].substring(0, var50), namespaceAttrs[i].substring(var50 + 1));
			}
			this.message.setNameSpaces(tmpMap);
		}

		attrValue = attrs.getValue(EnumConstants.MessageAttr.REMOVE_BLANK_NODE.code());
		if (StrUtil.isNotBlank(attrValue) && "true".equals(attrValue)) {
			this.message.setRemoveBlankNode(true);
		}
	}

	private void buildFieldBean(Attributes attrs) {
		Field var40 = this.I.A();
		int var9;
		byte[] var8;
		String var38;
		if (var40 != null && var40.getFieldType() != 2002 && var40.getFieldType() != 2003
				&& var40.getFieldType() != 2004 && var40.getFieldType() != 2011) {
			throw new CommonException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance().getString(
					"MessageMetadataManager.startElement.fieldType.illegal", new String[] { var40.getName() }));
		}

		Field field = new Field();
		String attrValue = attrs.getValue(EnumConstants.FieldAttr.NAME.code());
		ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field/@name ");

		if (MessageMetadataManager.keyWordSet.contains(attrValue)) {
			throw new CommonException(
					this.beanFilePath + ": field/@name [" + attrValue + "] " + MultiLanguageResourceBundle.getInstance()
							.getString("MessageMetadataManager.startElement.field.isReserved"));
		}

		if (var40 != null) {
			if (var40.getSubFields().containsKey(attrValue)) {
				throw new CommonException(this.beanFilePath + ": "
						+ MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.startElement.field.subField.reduplicate",
								new String[] { var40.getName() })
						+ ":" + attrValue);
			}
		} else if (this.message.getFields().containsKey(attrValue)) {
			throw new CommonException(this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance()
					.getString("MessageMetadataManager.startElement.field.reduplicate") + ":" + attrValue);
		}

		field.setName(attrValue);

		attrValue = attrs.getValue(EnumConstants.FieldAttr.FIELD_TYPE.code());
		ExceptionUtil.requireNotEmpty(attrValue,
				this.beanFilePath + ": field[@name='" + field.getName() + "']/@field-type ");
		field.setFieldType(Constant.getFieldTypeByText(attrValue));

		attrValue = attrs.getValue("padding");
		if (StrUtil.isNotBlank(attrValue)) {
			byte[] paddingByte = attrValue.getBytes();
			if (1 == paddingByte.length) {
				field.setPadding(paddingByte[0]);
			} else {
				if (4 != paddingByte.length || !attrValue.startsWith("0x")) {
					throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName() + "']/@padding "
							+ MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.padding.format.wrong"));
				}
				paddingByte = HexUtil.decodeHex(attrValue.substring(2));
				field.setPadding(paddingByte[0]);
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.PADDING_DIRECTION.code());
		if (StrUtil.isNotBlank(attrValue)) {
			if ("right".equalsIgnoreCase(attrValue)) {
				field.setPaddingDirection(EnumConstants.PADDING_RIGHT);
			} else if ("left".equalsIgnoreCase(attrValue)) {
				field.setPaddingDirection(EnumConstants.PADDING_LEFT);
			} else {
				if (!"none".equalsIgnoreCase(attrValue)) {
					throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName()
							+ "']/@padding-direction " + MultiLanguageResourceBundle.getInstance()
									.getString("MessageMetadataManager.startElement.paddingDirection.format.wrong"));
				}
				field.setPaddingDirection(EnumConstants.PADDING_NONE);
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.REQUIRED.code());
		if (StrUtil.isBlank(attrValue)) {
			field.setRequired(MessageMetadataManager.isRequired);
		} else {
			field.setRequired("true".equalsIgnoreCase(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.EDITABLE.code());

		if (StrUtil.isNotBlank(attrValue)) {
			field.setEditable(!"false".equalsIgnoreCase(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.IS_REMOVE_UNWATCHABLE.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setEditable(!"false".equalsIgnoreCase(attrValue));
		}

		if (field.isEditable()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.XPATH.code());
			field.setXpath(attrValue);
		}

		field.setShortText(attrs.getValue(EnumConstants.FieldAttr.SHORT_TEXT.code()));

		attrValue = attrs.getValue(EnumConstants.FieldAttr.ISO8583_NO.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setIso8583_no(Integer.parseInt(attrValue));
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.PREFIX.code());
		if (StrUtil.isNotBlank(attrValue)) {
			if (HexUtil.isHexNumber(attrValue)) {
				String prefixTmp = attrValue.substring(2);
				field.setPrefixString(prefixTmp);
				field.setPrefix(HexUtil.decodeHex(prefixTmp));
			} else {
				field.setPrefixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				field.setPrefix(attrValue.getBytes());
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.FIR_ROW_PREFIX.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setFirRowPrefix("true".equalsIgnoreCase(attrValue));
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.SUFFIX.code());
		if (StrUtil.isNotBlank(attrValue)) {
			if (HexUtil.isHexNumber(attrValue)) {
				String prefixTmp = attrValue.substring(2);
				field.setSuffixString(prefixTmp);
				field.setSuffix(HexUtil.decodeHex(prefixTmp));
			} else {
				field.setSuffixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
				field.setSuffix(attrValue.getBytes());
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.LAST_ROW_SUFFIX.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setLastRowSuffix("true".equalsIgnoreCase(attrValue));
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.EXTENDED_ATTRIBUTES.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setExtendedAttributeText(attrValue);
			String[] extendedAttrs = attrValue.split(";");
			for (int i = 0, len = extendedAttrs.length; i < len; ++i) {
				field.setExtendAttribute(extendedAttrs[i].substring(0, extendedAttrs[i].indexOf(":")),
						extendedAttrs[i].substring(extendedAttrs[i].indexOf(":") + 1));
			}
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_CHARSET.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setDataCharset(attrValue);
		}

		attrValue = attrs.getValue(EnumConstants.FieldAttr.SHIELD.code());
		if (StrUtil.isNotBlank(attrValue)) {
			field.setShield("true".equals(attrValue));
		}

		if (EnumConstants.FieldType.VAR_TABLE.getCode() == field.getFieldType()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.ROW_CUT.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setRowCut(!"false".equalsIgnoreCase(attrValue));
			}

		}

		if (EnumConstants.FieldType.VAR_FIELD.getCode() == field.getFieldType()
				|| EnumConstants.FieldType.VAR_COMBINE_FIELD.getCode() == field.getFieldType()
				|| EnumConstants.FieldType.VAR_REFERENCE_FIELD.getCode() == field.getFieldType()
				|| EnumConstants.FieldType.VAR_TABLE.getCode() == field.getFieldType()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.REF_LENGTH_FIELD.code());

			// TODO if else 判断????
			if (StrUtil.isNotBlank(attrValue)) {
				field.setRefLengthFieldName(attrValue);
				attrValue = attrs.getValue(EnumConstants.FieldAttr.REF_LENGTH_FIELD_OFFSET.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setRefLengthFieldOffset(Integer.parseInt(attrValue));
				}
			} else if (EnumConstants.FieldType.VAR_TABLE.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_FIELD_DATA_TYPE.code());
				ExceptionUtil.requireNotEmpty(attrValue,
						this.beanFilePath + ": field[@name='" + field.getName() + "']/@length-field-data-type ");
				field.setLengthFieldDataType(Constant.getDataTypeByText(attrValue));

				attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_FIELD_DATA_ENCODING.code());
				if (StrUtil.isNotBlank(attrValue) && "bcd".equals(attrValue)) {
					field.setLengthFieldDataEncoding(EnumConstants.DTA_ENC_TYP_BCD);
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_FIELD_LENGTH.code());
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field[@name='");
				field.setLengthFieldLength(Integer.parseInt(attrValue));
			} else {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.TAB_PREFIX.code());
				if (StrUtil.isNotBlank(attrValue)) {
					if (HexUtil.isHexNumber(attrValue)) {
						String prefixTmp = attrValue.substring(2);
						field.setTabPrefixString(prefixTmp);
						field.setTabPrefix(HexUtil.decodeHex(prefixTmp));
					} else {
						field.setTabPrefixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
						field.setTabPrefix(attrValue.getBytes());
					}
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.TAB_SUFFIX.code());
				if (StrUtil.isNotBlank(attrValue)) {
					if (HexUtil.isHexNumber(attrValue)) {
						String suffixTmp = attrValue.substring(2);
						field.setTabSuffixString(suffixTmp);
						field.setTabSuffix(HexUtil.decodeHex(suffixTmp));
					} else {
						field.setTabSuffixString(new String(HexUtil.encodeHex(attrValue.getBytes())));
						field.setTabSuffix(attrValue.getBytes());
					}
				}
			}
			attrValue = attrs.getValue(EnumConstants.FieldAttr.MAX_LENGTH.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setMaxLength(Integer.parseInt(attrValue));
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.MIN_LENGTH.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setMinLength(Integer.parseInt(attrValue));
			}
		}

		if (EnumConstants.MsgType.TAG.getCode() == this.message.getType()
				|| EnumConstants.MsgType.SWIFT.getCode() == this.message.getType()) {
			attrValue = attrs.getValue(EnumConstants.FieldAttr.TAG.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setTag(attrValue);
			}
		}

		if (EnumConstants.FieldType.COMBINE_FIELD.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.VAR_COMBINE_FIELD.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.TABLE.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.VAR_TABLE.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.REFERENCE_FIELD.getCode() != field.getFieldType()
				&& EnumConstants.FieldType.VAR_REFERENCE_FIELD.getCode() != field.getFieldType()) {
			if (EnumConstants.FieldType.LENGTH_FIELD.getCode() == field.getFieldType()
					|| EnumConstants.FieldType.PBOC_LENGTH_FIELD.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.START_FIELD.code());
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field[@name='" + "'] ");
				field.setStartFieldName(attrValue);

				attrValue = attrs.getValue(EnumConstants.FieldAttr.END_FIELD.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setEndFieldName(attrValue.trim());
				} else {
					field.setEndFieldName(field.getStartFieldName());
				}
			}

			if (EnumConstants.FieldType.MAC_FIELD.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.START_FIELD.code());
				if (StrUtil.isNotBlank(attrValue)) {
					field.setStartFieldName(attrValue);

					attrValue = attrs.getValue(EnumConstants.FieldAttr.END_FIELD.code());
					if (StrUtil.isNotBlank(attrValue)) {
						field.setEndFieldName(attrValue.trim());
					} else {
						field.setEndFieldName(field.getStartFieldName());
					}
				}

				attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA.code());
				if (StrUtil.isNotBlank(attrValue)) {
					String[] dataArr = attrValue.split(";");
					Map<String, String> dataTmp = new HashMap<>();
					for (int i = 0, len = dataArr.length; i < len; ++i) {
						dataTmp.put(dataArr[i], dataArr[i]);
					}
					field.setMacFldDataCache(dataTmp);
				}
			}

			if (EnumConstants.FieldType.TABLE_ROW_NUM_FIELD.getCode() == field.getFieldType()) {
				attrValue = attrs.getValue(EnumConstants.FieldAttr.TABLE.code());
				ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field[@name='");
				field.setTableName(attrValue);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_TYPE.code());
			ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field[@name='");
			field.setDataType(EnumConstants.DataType.getDataTypeCode(attrValue));

			attrValue = attrs.getValue(EnumConstants.FieldAttr.DATA_ENCODING.code());
			if (StrUtil.isNotBlank(attrValue) && "bcd".equalsIgnoreCase(attrValue)) {
				field.setDataEncoding(EnumConstants.DTA_ENC_TYP_BCD);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.PATTERN.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setPattern(attrValue);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH_SCRIPT.code());
			if (StrUtil.isNotBlank(attrValue)) {
				field.setLengthScript(attrValue);
			}

			if (EnumConstants.FieldType.VAR_FIELD.getCode() != field.getFieldType()) {
				if (EnumConstants.DataType.STR.getCode() != field.getDataType()
						&& EnumConstants.DataType.NUM.getCode() != field.getDataType()
						&& EnumConstants.DataType.BIN.getCode() != field.getDataType()
						&& EnumConstants.DataType.DECIMAL.getCode() != field.getDataType()
						&& EnumConstants.DataType.DOUBLE.getCode() != field.getDataType()) {
					if (EnumConstants.DataType.DATETIME.getCode() == field.getDataType()) {
						ExceptionUtil.requireNotEmpty(field.getPattern(), this.beanFilePath + ": field[@name='");
						field.setLength(field.getPattern().length());
					} else {
						attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH.code());
						int attrValInt = Integer.parseInt(StrUtil.isBlank(attrValue) ? "0" : attrValue.trim());

						System.out.println(field);
						checkDataTypeLength(field.getDataType(), attrValInt);
						field.setLength(attrValInt);
					}
				} else {
					attrValue = attrs.getValue(EnumConstants.FieldAttr.LENGTH.code());
					ExceptionUtil.requireNotEmpty(attrValue, this.beanFilePath + ": field[@name='");
					field.setLength(Integer.parseInt(attrValue));
				}
			}

			attrValue = attrs.getValue("strict-data-length");
			if (StrUtil.isNotBlank(attrValue)) {
				field.setStrictDataLength("true".equalsIgnoreCase(attrValue));
			}

			if (EnumConstants.FieldType.VAR_FIELD.getCode() == field.getFieldType()) {
				field.setPaddingDirection(EnumConstants.PADDING_NONE);
			}

			attrValue = attrs.getValue(EnumConstants.FieldAttr.VALUE.code());
			if (StrUtil.isNotBlank(attrValue)) {
				if (EnumConstants.FieldType.FIXED_FIELD.getCode() != field.getFieldType()
						&& EnumConstants.FieldType.TABLE_ROW_NUM_FIELD.getCode() != field.getFieldType()) {
					throw new CommonException(this.beanFilePath + ": ");
				}

				if (3000 != field.getDataType() && 3001 != field.getDataType() && 3010 != field.getDataType()
						&& 3011 != field.getDataType()) {
					if (3003 != field.getDataType() && 3007 != field.getDataType()) {
						if (3005 != field.getDataType() && 3008 != field.getDataType()) {
							if (3004 == field.getDataType()) {
								try {
									if (0 != attrValue.length()) {
										var8 = attrValue.getBytes();
										if (1 != var8.length) {
											if (4 != var8.length || !attrValue.startsWith("0x")) {
												throw new CommonException(this.beanFilePath + ": field[@name='"
														+ field.getName() + "']/@padding "
														+ MultiLanguageResourceBundle.getInstance().getString(
																"MessageMetadataManager.startElement.padding.format.wrong"));
											}

											var8 = CodeUtil.HextoByte(attrValue.substring(2));
										}
									}
								} catch (Exception var33) {
									throw new CommonException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notByteNumber",
													new String[] { field.getName(), attrValue }),
											var33);
								}
							} else if (3006 == field.getDataType()) {
								SimpleDateFormat var47 = new SimpleDateFormat(field.getPattern());
								var47.setLenient(false);

								try {
									var47.parse(attrValue);
								} catch (Exception var15) {
									throw new CommonException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.datetime.illegalPattern",
													new String[] { field.getName(), attrValue, field.getPattern() }));
								}
							} else if (3009 == field.getDataType()) {
								try {
									Long.parseLong(attrValue);
								} catch (Exception var14) {
									throw new CommonException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notLongNumber",
													new String[] { field.getName(), attrValue }),
											var14);
								}
							} else {
								if (3002 != field.getDataType()) {
									throw new CommonException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.canNotHave.defaultValue.1",
													new String[] { field.getName(),
															Constant.getDataTypeText(field.getDataType()) }));
								}

								var8 = attrValue.trim().getBytes();
								if (var8.length % 2 != 0) {
									throw new CommonException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notHexString",
													new String[] { field.getName(),
															Constant.getFieldTypeText(field.getFieldType()) }));
								}

								if (var8.length / 2 != field.getLength()) {
									throw new CommonException(this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.hexString.illegalLength",
													new String[] { field.getName(),
															Constant.getFieldTypeText(field.getFieldType()) })
											+ " " + field.getLength() * 2);
								}

								for (var9 = 0; var9 < var8.length; ++var9) {
									if ((var8[var9] < 48 || var8[var9] > 57) && (var8[var9] < 97 || var8[var9] > 102)
											&& (var8[var9] < 65 || var8[var9] > 70)) {
										throw new CommonException(this.beanFilePath + ": "
												+ MultiLanguageResourceBundle.getInstance().getString(
														"MessageMetadataManager.startElement.field.defaultValue.notHexString",
														new String[] { field.getName(),
																Constant.getFieldTypeText(field.getFieldType()) }));
									}
								}
							}
						} else {
							try {
								Short.parseShort(attrValue);
							} catch (Exception var16) {
								throw new CommonException(
										this.beanFilePath + ": " + MultiLanguageResourceBundle.getInstance().getString(
												"MessageMetadataManager.startElement.field.defaultValue.notShortNumber",
												new String[] { field.getName(), attrValue }) + var16.getMessage(),
										var16);
							}
						}
					} else {
						try {
							Integer.parseInt(attrValue);
						} catch (Exception var17) {
							throw new CommonException(this.beanFilePath + ": "
									+ MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.defaultValue.notIntNumber",
											new String[] { field.getName(), attrValue }));
						}
					}
				} else {
					if (attrValue.length() > field.getLength()) {
						throw new CommonException(this.beanFilePath + ": "
								+ MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.defaultValue.tooLong",
										new String[] { field.getName(), attrValue }));
					}

					if (field.isStrictDataLength() && attrValue.length() < field.getLength()) {
						throw new CommonException(this.beanFilePath + ": "
								+ MultiLanguageResourceBundle.getInstance().getString(
										"MessageMetadataManager.startElement.field.defaultValue.tooShort",
										new String[] { field.getName(), attrValue, "" + field.getLength() }));
					}

					if (3001 == field.getDataType()) {
						try {
							Integer.parseInt(attrValue);
						} catch (Exception var19) {
							throw new CommonException(
									this.beanFilePath + ": "
											+ MultiLanguageResourceBundle.getInstance().getString(
													"MessageMetadataManager.startElement.field.defaultValue.notNumber",
													new String[] { field.getName(), attrValue })
											+ var19.getMessage(),
									var19);
						}
					} else if (3010 == field.getDataType()) {
						try {
							new BigDecimal(attrValue);
						} catch (Exception var18) {
							throw new CommonException(this.beanFilePath + ": "
									+ MultiLanguageResourceBundle.getInstance().getString(
											"MessageMetadataManager.startElement.field.defaultValue.notDecimalNumber",
											new String[] { field.getName(), attrValue }));
						}
					}
				}

				field.setValue(attrValue);
			}

			attrValue = attrs.getValue("ref-value-range");
			if (null != attrValue) {
				attrValue = attrValue.trim();
				if (0 == attrValue.length()) {
					throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName()
							+ "']/@ref-value-range " + MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				field.setRefValueRange(attrValue);
				File var48 = (File) MessageMetadataManager.getGroupPathCache().get(this.channelId);
				String var43 = var48.getAbsolutePath();
				if (!var43.endsWith(System.getProperty("file.separator"))) {
					var43 = var43 + System.getProperty("file.separator");
				}

				String var44 = var43 + "value-range" + System.getProperty("file.separator") + attrValue + ".xml";
				File var11 = new File(var44);
				MessageMetadataManager._B var12 = new MessageMetadataManager._B((1));
				this.G = var12.A(this.channelId, var11);
			}
		} else {
			attrValue = attrs.getValue("reference-type");
			if (null != attrValue) {
				attrValue = attrValue.trim();
				if (0 == attrValue.length()) {
					throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName()
							+ "']/@reference-type " + MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				field.setReferenceType(attrValue);
			}

			attrValue = attrs.getValue("reference");
			if (null == attrValue && (2008 == field.getFieldType() || 2009 == field.getFieldType())) {
				throw new CommonException(
						this.beanFilePath + ": field[@name='" + field.getName() + "'] " + MultiLanguageResourceBundle
								.getInstance().getString("MessageMetadataManager.startElement.reference.null"));
			}

			if (attrValue != null) {
				attrValue = attrValue.trim();
				if (0 == attrValue.length()) {
					throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName() + "']/@reference "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				if (!"dynamic".equalsIgnoreCase(field.getReferenceType())
						&& !"expression".equalsIgnoreCase(field.getReferenceType())) {
					Message var42 = MessageMetadataManager.getMessage(this.channelId, attrValue);
					field.setReference(var42);
				}

				field.setReferenceId(attrValue);
			}

			field.setSubFields(new TreeMap());
			if (2004 == field.getFieldType()) {
				attrValue = attrs.getValue("row-num-field");
				if (attrValue != null) {
					attrValue = attrValue.trim();
					if (0 == attrValue.length()) {
						throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName()
								+ "']/@row-num-field " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					field.setRowNumFieldName(attrValue);
				}

				attrValue = attrs.getValue("row-xpath");
				if (attrValue != null) {
					attrValue = attrValue.trim();
					if (0 == attrValue.length()) {
						throw new CommonException(this.beanFilePath + ": field[@name='" + field.getName()
								+ "']/@row-xpath " + MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					field.setRowXpath(attrValue);
				}
			}

			attrValue = attrs.getValue("data-encoding");
			if (null != attrValue) {
				attrValue = attrValue.trim();
				if ("bcd".equalsIgnoreCase(attrValue)) {
					field.setDataEncoding(4001);
				}
			}
		}

		attrValue = attrs.getValue("class");
		if (field.getFieldType() == 2010) {
			var38 = attrs.getValue("calc-type");
			if (null == var38) {
				throw new CommonException(this.beanFilePath + ": mac-field/@calc-type "
						+ MultiLanguageResourceBundle.getInstance().getString("null"));
			}

			var38 = var38.trim();
			if (0 == var38.length()) {
				throw new CommonException(this.beanFilePath + ": mac-field/@calc-type "
						+ MultiLanguageResourceBundle.getInstance().getString("blank"));
			}

			field.setCalcType(var38);
			if ("java".equalsIgnoreCase(field.getCalcType())) {
				if (null == attrValue) {
					throw new CommonException(this.beanFilePath + ": mac-field/@class "
							+ MultiLanguageResourceBundle.getInstance().getString("null"));
				}

				attrValue = attrValue.trim();
				if (0 == attrValue.length()) {
					throw new CommonException(this.beanFilePath + ": mac-field/@class "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				field.setCombineOrTableFieldClassName(attrValue);
			}
		} else if (null != attrValue) {
			if (field.getFieldType() != 2002 && field.getFieldType() != 2003 && field.getFieldType() != 2004
					&& field.getFieldType() != 2011 && field.getFieldType() != 2010 && field.getFieldType() != 2008
					&& field.getFieldType() != 2001) {
				throw new CommonException(this.beanFilePath + ": "
						+ MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.startElement.field.canNotHave.class",
								new String[] { field.getName() }));
			}

			attrValue = attrValue.trim();
			if (attrValue.length() > 0) {
				field.setCombineOrTableFieldClassName(attrValue);
			}
		}

		if (var40 != null) {
			var40.setSubField(field.getName(), field);
			this.I.A(var40);
		} else {
			this.message.setField(field.getName(), field);
		}

		this.I.A(field);
		this.J = field;
	}

	private void checkDataTypeLength(int dataType, int dataTypeLength) {
		if (EnumConstants.DataType.INT.getCode() == dataType || EnumConstants.DataType.NETINT.getCode() == dataType) {
			if (dataTypeLength != 4) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else if (EnumConstants.DataType.BYTE.getCode() == dataType) {
			if (dataTypeLength != 1) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else if (EnumConstants.DataType.SHORT.getCode() == dataType
				|| EnumConstants.DataType.NETSHORT.getCode() == dataType) {
			if (dataTypeLength != 2) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else if (EnumConstants.DataType.DATETIME.getCode() == dataType
				|| EnumConstants.DataType.LONG.getCode() == dataType) {
			if (dataTypeLength != 8) {
				throw new CommonException(this.beanFilePath + ": ");
			}
		} else {
			throw new CommonException(this.beanFilePath + ": ");
		}
	}
}
