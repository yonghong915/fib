package com.fib.gateway.message.packer;

import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.ValueRange;
import com.fib.gateway.message.util.ByteBuffer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ConfigManager;
import com.fib.gateway.message.util.EnumConstants;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.util.StringUtil;
import com.fib.gateway.message.util.XmlTools;
import com.fib.gateway.message.xml.message.Constant;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.message.xml.script.BeanShellEngine;

import lombok.EqualsAndHashCode;

/**
 * XML消息组包器
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@EqualsAndHashCode(callSuper = false)
public class XmlMessagePacker extends AbstractMessagePacker {
	private BeanShellEngine beanShellEngine = null;

	@Override
	public byte[] pack() {
		if (null == this.message) {
			throw new CommonException("message null");
		}
		if (null == this.messageBean) {
			throw new CommonException("messageBean null");
		}
		if (variableCache.isEmpty()) {
			this.loadVariable();
		}
		if (null != this.getParentBean()) {
			this.messageBean.setParent(this.getParentBean());
		}

		this.messageBean.setMetadata(this.message);
		Iterator<Field> fieldIter = this.message.getFields().values().iterator();

		Field field = null;
		while (fieldIter.hasNext()) {
			field = fieldIter.next();
			if (EnumConstants.FieldType.TABLE_ROW_NUM_FIELD.getCode() == field.getFieldType()) {
				this.buildTableRowField(field);
			}
		}

		byte[] tmplateByte = this.message.getTemplate().getBytes();
		if (null != this.message.getPrePackEvent()) {
			this.buildVariableCache("pre-pack", this.message.getPostPackEvent(), (ByteBuffer) null);
		}

		byte[] msgByte = this.getMessageBeanByte(this.messageBean, tmplateByte);
		if (this.message.getPostPackEvent() != null) {
			ByteBuffer var5 = new ByteBuffer();
			var5.append(msgByte);
			this.buildVariableCache("post-pack", this.message.getPostPackEvent(), var5);
			msgByte = var5.toBytes();
		}

		if (null != this.message.getMsgCharset()) {
			try {
				msgByte = (new String(msgByte)).getBytes(this.message.getMsgCharset());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new CommonException("message.encoding.unsupport");
			}
		}

		if (this.message.isRemoveBlankNode()) {
			msgByte = XmlTools.removeBlankNode(msgByte);
		}

		if (this.message.isSchemaValid()) {
			try {
				InputSource inputSource = new InputSource(new StringReader(new String(msgByte)));
				this.validateXml(inputSource);
			} catch (Exception e) {
				e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
		}
		return msgByte;
	}

	private synchronized void validateXml(InputSource inputSource) {
		try {
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(Constant.getSchemaType(this.message.getSchemaValidType()));
			File file = new File(ConfigManager.getFullPathFileName(this.message.getSchemaValidPath()));
			Schema schema = schemaFactory.newSchema(file);
			Validator validator = schema.newValidator();
			SAXSource saxSource = new SAXSource(inputSource);
			validator.validate(saxSource);

		} catch (Exception var7) {
			ExceptionUtil.throwActualException(var7);
		}
	}

	public byte[] getMessageBeanByte(MessageBean messagebean, byte[] abyte0) {
		ByteBuffer bytebuffer = new ByteBuffer(abyte0.length);

		int i1 = 0;
		int j1 = 0;

		int i2 = 0;
		do {
			if (i2 >= abyte0.length)
				break;
			byte byte0 = abyte0[i2];
			if (byte0 == 36 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 123) {
				i2++;
				int i = ++i2;
				int k = -1;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 125) {
						k = i2++;
						break;
					}
					i2++;
				} while (true);
				if (i2 <= abyte0.length && -1 != k) {
					String s = new String(abyte0, i, k - i);
					if (null == s || 0 == s.length())
						throw new CommonException(MultiLanguageResourceBundle.getInstance()
								.getString("XmlMessagePacker.variableName.null"));
					Object obj2 = variableCache.get(s);
					if (null == obj2)
						obj2 = getAttribute(messagebean, s);
					if (obj2 != null)
						bytebuffer.append(data2String(messagebean, obj2, s).getBytes());
				} else {
					throw new CommonException(
							MultiLanguageResourceBundle.getInstance().getString("XmlMessagePacker.variable.wrong"));
				}
			} else if (byte0 == 60 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 37) {
				i2++;
				int k1 = ++i2;
				int l1 = -1;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 62 && abyte0[i2 - 1] == 37) {
						l1 = i2 - 1;
						i2++;
						break;
					}
					i2++;
				} while (true);
				if (i2 <= abyte0.length && -1 != l1) {
					String s2 = new String(abyte0, k1, l1 - k1);
					if (null == s2 || 0 == s2.length())
						throw new CommonException(MultiLanguageResourceBundle.getInstance()
								.getString("XmlMessagePacker.merge.script.null"));
					Object obj6 = evalEngine(messagebean, s2);
					if (obj6 != null) {
						if (obj6 instanceof String)
							bytebuffer.append(((String) obj6).getBytes());
						else if (obj6 instanceof byte[])
							bytebuffer.append((byte[]) obj6);
						else
							throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
									"XmlMessagePacker.merge.script.exec.resultType.illegal",
									new String[] { s2, obj6.getClass().getName() }));
					}
				} else {
					throw new CommonException(MultiLanguageResourceBundle.getInstance()
							.getString("XmlMessagePacker.merge.script.form.wrong"));
				}
			} else if (byte0 == 60 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 35) {
				i1++;
				i2 += 2;
				int j = i2++;
				int l = -1;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 35) {
						l = i2++;
						break;
					}
					i2++;
				} while (true);
				String s1 = new String(abyte0, j, l - j);
				String[] as = s1.split(" ");
				s1 = as[1];
				j = i2;
				do {
					if (i2 >= abyte0.length)
						break;
					byte0 = abyte0[i2];
					if (byte0 == 35 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 62) {
						l = i2;
						i2 += 2;
						j1++;
					} else if (byte0 == 60 && i2 < abyte0.length - 1 && abyte0[i2 + 1] == 35) {
						i2 += 2;
						i1++;
					}
					if (i1 <= j1)
						break;
					i2++;
				} while (true);
				String s3 = new String(abyte0, j, l - j);
				ArrayList<?> arraylist = (ArrayList<?>) getAttribute(messagebean, s1);
				if (null == arraylist)
					arraylist = new ArrayList<>();
				Field field = this.message.getField(s1);
				int j2 = 0;
				while (j2 < arraylist.size()) {
					if (null != field.getPreRowPackEvent())
						buildVariableCache(field, "row-pre-pack", field.getPreRowPackEvent(), arraylist.size(), j2);
					Message message = field.getReference();
					if (null == message) {
						message = new Message();
						message.setId((new StringBuilder()).append(this.message.getId()).append("-")
								.append(field.getName()).toString());
						message.setShortText(field.getShortText());
						message.setFields(field.getSubFields());
						message.setType(1001);
						message.setTemplate(s3);
					}
					if (2011 == field.getFieldType()) {
						bytebuffer.append(getMessageByte(message, (MessageBean) arraylist.get(j2)));
					} else {
						AbstractMessagePacker abstractmessagepacker = MessagePackerFactory.getMessagePacker(message);
						abstractmessagepacker.setVariableCache(variableCache);
						abstractmessagepacker.setMessage(message);
						abstractmessagepacker.setMessageBean((MessageBean) arraylist.get(j2));
						abstractmessagepacker.setParentBean(messagebean);
						bytebuffer.append(abstractmessagepacker.pack());
					}
					if (null != field.getPostRowPackEvent())
						buildVariableCache(field, "row-post-pack", field.getPostRowPackEvent(), arraylist.size(), j2);
					j2++;
				}
			} else {
				bytebuffer.append(byte0);
				i2++;
			}
		} while (true);
		return bytebuffer.toBytes();
	}

	private String data2String(MessageBean messageBean, Object dataType, String fieldName) {
		Field field;
		Field var4;
		String[] var6;
		String[] var7;
		int var8;
		if (dataType instanceof Integer) {
			return String.valueOf(dataType);
		} else if (dataType instanceof Long) {
			return String.valueOf(dataType);
		} else if (dataType instanceof Short) {
			return String.valueOf(dataType);
		} else if (dataType instanceof Byte) {
			return String.valueOf(dataType);
		} else if (dataType instanceof String) {
			field = this.message.getField(fieldName);
			String extendAttr = field.getExtendAttribute("replace_all");
			if (null != field && null != extendAttr) {
				byte[] var5 = ((String) dataType).getBytes();
				var6 = extendAttr.split("\\|");
				for (var8 = 0; var8 < var6.length; ++var8) {
					var7 = var6[var8].split("=");
					if (2 != var7.length) {
						throw new CommonException("field[@name=" + field.getName() + "]'s extended-attributes[@key="
								+ "replace_all" + "] pattern error. pattern : hex=hex;hex=hex!");
					}

					var5 = CodeUtil.replaceAll(var5, CodeUtil.HextoByte(var7[0].trim()),
							CodeUtil.HextoByte(var7[1].trim()), 0, var5.length);
				}
				dataType = new String(var5);
			}
			return StringUtil.formatXmlValue((String) dataType);
		} else {
			ByteBuffer var13;
			if (dataType instanceof MessageBean) {
				var4 = this.message.getField(fieldName);
				Message var14;
				if ("dynamic".equalsIgnoreCase(var4.getReferenceType())) {
					var14 = this.getRefMessage(var4, this.message, messageBean);
				} else {
					var14 = var4.getReference();
					if (null == var14) {
						var14 = new Message();
						var14.setId(this.message.getId() + "-" + var4.getName());
						var14.setShortText(var4.getShortText());
						var14.setFields(var4.getSubFields());
					}
				}

				AbstractMessagePacker var16 = MessagePackerFactory.getMessagePacker(var14);
				var16.setVariableCache(this.variableCache);
				var16.setMessage(var14);
				var16.setMessageBean((MessageBean) dataType);
				var16.setParentBean(messageBean);
				String var18 = var4.getDataCharset();
				if (null == var18) {
					var18 = this.message.getMsgCharset();
				}

				if (null == var18) {
					var18 = this.getDefaultCharset();
				}

				if (null == var18) {
					return 1001 != var14.getType() ? StringUtil.formatXmlValue(new String(var16.pack()))
							: new String(var16.pack());
				} else {
					try {
						return 1001 != var14.getType() ? StringUtil.formatXmlValue(new String(var16.pack(), var18))
								: new String(var16.pack(), var18);
					} catch (Exception var11) {
						var11.printStackTrace();
						ExceptionUtil.throwActualException(var11);
						return null;
					}
				}
			} else if (dataType instanceof List) {
				var4 = this.message.getField(fieldName);
				var13 = new ByteBuffer(512);
				Message var15;
				if ("dynamic".equalsIgnoreCase(var4.getReferenceType())) {
					var15 = this.getRefMessage(var4, this.message, messageBean);
				} else {
					var15 = var4.getReference();
					if (null == var15) {
						var15 = new Message();
						var15.setId(this.message.getId() + "-" + var4.getName());
						var15.setShortText(var4.getShortText());
						var15.setFields(var4.getSubFields());
					}
				}

				for (var8 = 0; var8 < ((List<?>) dataType).size(); ++var8) {
					if (null != var4.getPreRowPackEvent()) {
						this.buildVariableCache(var4, "row-pre-pack", var4.getPreRowPackEvent(),
								((List<?>) dataType).size(), var8);
					}

					if (null != var4.getRowXpath()) {
						var13.append("<".getBytes());
						var13.append(var4.getRowXpath().getBytes());
						var13.append(">".getBytes());
					}

					AbstractMessagePacker var17 = MessagePackerFactory.getMessagePacker(var15);
					var17.setVariableCache(this.variableCache);
					var17.setMessage(var15);
					var17.setMessageBean((MessageBean) ((List<?>) dataType).get(var8));
					var17.setParentBean(messageBean);
					String var9 = var4.getDataCharset();
					if (null == var9) {
						var9 = this.message.getMsgCharset();
					}

					if (null == var9) {
						var9 = this.getDefaultCharset();
					}

					if (null == var9) {
						if (1001 != var15.getType()) {
							var13.append(StringUtil.formatXmlValue(new String(var17.pack())).getBytes());
						} else {
							var13.append(var17.pack());
						}
					} else {
						try {
							if (1001 != var15.getType()) {
								var13.append(StringUtil.formatXmlValue(new String(var17.pack(), var9)).getBytes());
							} else {
								var13.append(var17.pack());
							}
						} catch (Exception var12) {
							var12.printStackTrace();
							ExceptionUtil.throwActualException(var12);
						}
					}

					if (null != var4.getRowXpath()) {
						var13.append("</".getBytes());
						var13.append(var4.getRowXpath().getBytes());
						var13.append(">".getBytes());
					}

					if (null != var4.getPostRowPackEvent()) {
						this.buildVariableCache(var4, "row-post-pack", var4.getPostRowPackEvent(),
								((List<?>) dataType).size(), var8);
					}
				}

				return var13.toString();
			} else {
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
						"XmlMessagePacker.data2String.variable.valueType.illegal",
						new String[] { fieldName, dataType.getClass().getName() }));
			}
		}
	}

	protected byte[] getMessageByte(Message message, MessageBean messageBean) {
		int var3 = 0;
		byte[] var4 = message.getTemplate().getBytes();
		ByteBuffer var6 = new ByteBuffer(var4.length);
		ByteBuffer var7 = new ByteBuffer(var4.length);
		String var10 = null;
		Object var11 = null;

		while (true) {
			while (true) {
				label46: while (var3 < var4.length) {
					byte var12 = var4[var3];
					var7.clear();
					if (var12 == 60) {
						while (var3 < var4.length) {
							var12 = var4[var3];
							if (var12 == 36 && var3 < var4.length - 1 && var4[var3 + 1] == 123) {
								++var3;
								++var3;
								int var13 = var3;

								int var14;
								for (var14 = -1; var3 < var4.length; ++var3) {
									var12 = var4[var3];
									if (var12 == 125) {
										var14 = var3++;
										break;
									}
								}

								if (var3 <= var4.length && -1 != var14) {
									var10 = new String(var4, var13, var14 - var13);
									if (null != var10 && 0 != var10.length()) {
										var11 = this.variableCache.get(var10);
										if (null == var11) {
											var11 = this.getAttribute(messageBean, var10);
										}

										if (var11 == null) {
											while (true) {
												if (var3 >= var4.length) {
													continue label46;
												}

												++var3;
												if (var4[var3] == 62) {
													continue label46;
												}
											}
										}

										if (!(var11 instanceof String)) {
											throw new CommonException(
													"XmlMessagePacker.packerVarTable.varTable.subField.mustBeSame");
										}

										var7.append(this.data2String(messageBean, var11, var10).getBytes());

										while (var3 < var4.length) {
											var12 = var4[var3];
											var7.append(var12);
											++var3;
											if (var12 == 62) {
												break;
											}
										}

										var6.append(var7.toBytes());
										break;
									}

									throw new CommonException("XmlMessagePacker.variableName.null");
								}

								throw new CommonException("XmlMessagePacker.variable.wrong");
							}

							var7.append(var12);
							++var3;
						}
					} else {
						++var3;
					}
				}

				var6.append(var7.toBytes());
				return var6.toBytes();
			}
		}
	}

	protected void buildTableRowField(Field field) {
		Object var3 = this.messageBean.getAttribute(field.getTable().getName());
		if (null == var3) {
			var3 = new ArrayList<>();
			this.messageBean.setAttribute(field.getTable().getName(), var3);
		}

		Object var4 = null;
		switch (field.getDataType()) {
		case 3001:
			var4 = Integer.toString(((List<?>) var3).size());
			break;

		case 3003:
		case 3007:
			var4 = ((List<?>) var3).size();
			break;
		case 3004:
			var4 = (byte) ((List<?>) var3).size();
			break;
		case 3005:
		case 3008:
			var4 = (short) ((List<?>) var3).size();
			break;
		case 3002:
		case 3006:
		default:
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
					"tableRowField.dataType.unsupport", new String[] { field.getName(), "" + field.getDataType() }));
		}

		if (field.isEditable()) {
			switch (field.getDataType()) {
			case 3003:
			case 3007:
				this.messageBean.setAttribute(field.getName(), var4);
				break;
			case 3004:
				this.messageBean.setAttribute(field.getName(), var4);
				break;
			case 3005:
			case 3008:
				this.messageBean.setAttribute(field.getName(), var4);
				break;

			case 3009:
				this.messageBean.setAttribute(field.getName(), var4);
				break;
			case 3006:
			default:
				this.messageBean.setAttribute(field.getName(), var4);
				break;
			}
		}

	}

	private Object getAttribute(MessageBean bean, String name) {
		return bean.getAttribute(name);
	}

	protected void buildVariableCache(Field field, String var2, String var3, int rowNum, int currRow) {
		BeanShellEngine engine = new BeanShellEngine();
		engine.set("messagePacker", this);
		engine.set("message", this.message);
		engine.set("messageBean", this.messageBean);
		engine.set("messageBuf", this.buf);
		engine.set("field", field);
		engine.set("rowNum", rowNum);
		engine.set("currRow", currRow);
		Iterator<String> var7;
		String var8;
		if (0 != this.variableCache.size()) {
			var7 = this.variableCache.keySet().iterator();

			while (var7.hasNext()) {
				var8 = var7.next();
				engine.set(var8, this.variableCache.get(var8));
			}
		}
		engine.eval(var3);
		if (0 != this.variableCache.size()) {
			var7 = this.variableCache.keySet().iterator();
			while (var7.hasNext()) {
				var8 = var7.next();
				this.variableCache.put(var8, engine.get(var8));
			}
		}
	}

	protected Message getRefMessage(Field field, Message message, MessageBean messageBean) {
		String[] refIds = field.getReferenceId().split("\\.");
		Message var5 = message;
		if (refIds.length > 1) {
			MessageBean var6 = messageBean;
			for (int var7 = 0; var7 < refIds.length - 1; ++var7) {
				var6 = var6.getParent();
			}
			var5 = var6.getMetadata();
		}
		Field var9 = var5.getField(refIds[refIds.length - 1]);
		Object var10 = messageBean.getAttribute(field.getReferenceId());
		ValueRange var8 = (ValueRange) var9.getValueRange().get(var10);
		if (null == var8) {
			var8 = (ValueRange) var9.getValueRange().get("default-ref");
		}
		return var8.getReference();
	}

	protected Object evalEngine(MessageBean messageBean, String var2) {
		BeanShellEngine engine = this.getBeanShellEngine();
		engine.set("bean", messageBean);
		return engine.eval(var2);
	}

	private BeanShellEngine getBeanShellEngine() {
		if (null == this.beanShellEngine) {
			this.beanShellEngine = new BeanShellEngine();
		}
		return this.beanShellEngine;
	}

	protected void buildVariableCache(String var1, String var2, ByteBuffer byteBuffer) {
		BeanShellEngine engine = new BeanShellEngine();
		engine.set("messagePacker", this);
		engine.set("message", this.message);
		engine.set("messageBean", this.messageBean);
		engine.set("messageBuf", byteBuffer);
		Iterator<String> var5;
		String var6;
		if (0 != this.variableCache.size()) {
			var5 = this.variableCache.keySet().iterator();
			while (var5.hasNext()) {
				var6 = var5.next();
				engine.set(var6, this.variableCache.get(var6));
			}
		}

		engine.eval(var2);
		if (0 != this.variableCache.size()) {
			var5 = this.variableCache.keySet().iterator();

			while (var5.hasNext()) {
				var6 = var5.next();
				this.variableCache.put(var6, engine.get(var6));
			}
		}

	}
}
