package com.fib.gateway.message.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.util.EnumConstants;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import cn.hutool.core.util.StrUtil;

/**
 * 消息元数据管理器
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
public class MessageMetadataManager {
	/***/
	private Message message;

	/***/
	private List<Field> fieldList = new ArrayList<>();

	/***/
	private static Map<String, Map<String, Message>> cache = new HashMap<>(128);

	/***/
	private static Map<String, File> groupPathCache = new HashMap<>(128);

	/***/
	private static Map<String, Message> cacheByClass = new HashMap<>(1024);

	/***/
	public static final Set keyWordSet = new HashSet();

	/***/
	public static boolean isRequired = false;

	public MessageMetadataManager() {
		this.message = new Message();
	}

	public static void main(String[] args) {
		System.out.println(EnumConstants.MessageAttr.ID.code());
	}

	public void startElement(String eleName, Element ele) {
		if (EnumConstants.MESSAGE_BEAN_NAME.equals(eleName)) {
			buildMessageBean(ele);
		} else if (EnumConstants.FIELD_BEAN_NAME.equals(eleName)) {
			buildField(ele);
		}
	}

	private void buildMessageBean(Element ele) {
		String attrValue = ele.attribute(EnumConstants.MessageAttr.ID.code()).getValue();
		message.setId(attrValue);

		attrValue = ele.attribute(EnumConstants.MessageAttr.TYPE.code()).getValue();
		message.setType(EnumConstants.MsgType.getMessageTypeCode(attrValue));

		attrValue = ele.attribute(EnumConstants.MessageAttr.SHORT_TEXT.code()).getValue();
		message.setShortText(attrValue);

		attrValue = ele.attribute(EnumConstants.MessageAttr.CLASS.code()).getValue();
		message.setClassName(attrValue);

		attrValue = ele.attribute(EnumConstants.MessageAttr.XPATH.code()).getValue();
		message.setXpath(attrValue);
	}

	private void buildField(Element ele) {
		Field field = new Field();
		String attrValue = ele.attribute(EnumConstants.FieldAttr.NAME.code()).getValue();
		field.setName(attrValue);

		attrValue = ele.attribute(EnumConstants.FieldAttr.FIELD_TYPE.code()).getValue();
		field.setFieldType(EnumConstants.FiledType.getFieldTypeCode(attrValue));

		attrValue = ele.attribute(EnumConstants.FieldAttr.REQUIRED.code()).getValue();
		field.setRequired("true".equalsIgnoreCase(attrValue));

		attrValue = ele.attribute(EnumConstants.FieldAttr.XPATH.code()).getValue();
		field.setXpath(attrValue);

		attrValue = ele.attribute(EnumConstants.FieldAttr.SHORT_TEXT.code()).getValue();
		field.setShortText(attrValue);

		attrValue = ele.attribute(EnumConstants.FieldAttr.MAX_LENGTH.code()).getValue();
		field.setMaxLength(Integer.parseInt(attrValue));

		attrValue = ele.attribute(EnumConstants.FieldAttr.MIN_LENGTH.code()).getValue();
		field.setMinLength(Integer.parseInt(attrValue));

		attrValue = ele.attribute(EnumConstants.FieldAttr.DATA_TYPE.code()).getValue();
		field.setDataType(EnumConstants.DataType.getDataTypeCode(attrValue));

		attrValue = ele.attribute(EnumConstants.FieldAttr.VALUE.code()).getValue();
		field.setValue(attrValue);

		attrValue = ele.attribute(EnumConstants.FieldAttr.REFERENCE.code()).getValue();
		field.setReferenceId(attrValue);
		fieldList.add(field);
	}

	public static Message getMessage(String var0, String var1) {
		if (null == var0) {
			throw new IllegalArgumentException(
					MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "groupId" }));
		} else if (null == var1) {
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null",
					new String[] { "messageId" }));
		} else {
			Map var2 = (Map) cache.get(var0);
			if (null == var2) {
				throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
						.getString("MessageMetadataManager.getMessage.group.null", new String[] { var0 }));
			} else {
				Message var3 = (Message) var2.get(var1);
				if (null == var3) {
					File var4 = (File) groupPathCache.get(var0);
					File var5 = new File(var4.toString() + System.getProperty("file.separator") + var1 + ".xml");
					System.out.println("parseMB file is :" + var5.getAbsolutePath());
					var3 = load(var0, var5);
					if (null == var3) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("MessageMetadataManager.getMessage.file.isNotExist", new String[] { var1 }));
					}

					if (!var1.equals(var3.getId())) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"MessageMetadataManager.getMessage.messageId.notEqual.fileName",
								new String[] { var1, var3.getId() }));
					}

					if (!"com.giantstone.message.bean.CommonMessageBean".equals(var3.getClassName())
							&& cacheByClass.containsKey(var3.getClassName())) {
						Message var6 = (Message) cacheByClass.get(var3.getClassName());
						if (!var3.equalTo(var6)) {
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"MessageMetadataManager.getMessage.messageClass.reduplicate",
									new String[] { var1, var0, var3.getClassName() }));
						}
					}

					var2.put(var1, var3);
					cacheByClass.put(var3.getClassName(), var3);
				}

				return var3;
			}
		}
	}

	/**
	 * 加载元数据
	 * 
	 * @param channelId
	 * @param messageBeanDir
	 */
	public static void loadMetadataGroup(String channelId, File messageBeanDir) {
		if (StrUtil.isEmpty(channelId)) {
			throw new CommonException("channelId can not be empty.");
		}
		if (Objects.isNull(messageBeanDir)) {
			throw new CommonException("messageBeanDir can not be empty.");
		}
		if (!messageBeanDir.isDirectory()) {
			throw new CommonException("messageBeanDir is not directory.");
		}
		if (!messageBeanDir.canRead()) {
			throw new CommonException("messageBeanDir can not be read.");
		}

		File[] files = messageBeanDir.listFiles((File dir, String name) -> {
			return name.endsWith(".xml");
		});

		Map<String, Message> channelCache = cache.computeIfAbsent(channelId, k -> {
			groupPathCache.put(channelId, messageBeanDir);
			return new HashMap<>(1024);
		});

		Message message = null;
		String prefixFileName = null;
		String fileName = null;

		for (int i = 0, len = files.length; i < len; ++i) {
			fileName = files[i].getName();
			prefixFileName = fileName.substring(0, fileName.lastIndexOf("."));
			if (Objects.nonNull(channelCache.get(prefixFileName))) {
				continue;
			}

			message = load(channelId, files[i]);
			if (!prefixFileName.equals(message.getId())) {
				throw new CommonException("messageId is not equal fileName");
			}

			if (!"com.fib.gateway.message.bean.CommonMessageBean".equals(message.getClassName())
					&& cacheByClass.containsKey(message.getClassName())) {
				Message cacheMessage = cacheByClass.get(message.getClassName());
				if (!message.equalTo(cacheMessage)) {
					throw new CommonException("messageClass is reduplicate");
				}
			}
			channelCache.put(prefixFileName, message);
			cacheByClass.put(message.getClassName(), message);
		}
	}

	public static Map<String, Map<String, Message>> getAllMessage() {
		return cache;
	}

	/**
	 * 
	 * @param channelId
	 * @param beanfile
	 * @return
	 */
	private static Message load(String channelId, File beanfile) {
		MessageHandler messageHandler = new MessageHandler();
		return messageHandler.parseFile(channelId, beanfile);
	}

	static class _B extends DefaultHandler {
		private Map C;
		private String E;
		private String A;
		private ValueRange B;
		String D;

		private _B() {
			this.C = new HashMap();
			this.D = null;
		}

		public Map A(String var1, File var2) {
			this.A = var1;
			this.E = var2.getAbsolutePath();
			SAXParserFactory var3 = SAXParserFactory.newDefaultInstance();
			FileInputStream var5 = null;

			try {
				var5 = new FileInputStream(var2);
				SAXParser var4 = var3.newSAXParser();
				var4.parse(var5, this);
			} catch (Exception var15) {
				var15.printStackTrace(System.err);
				// ExceptionUtil.throwActualException(var15);
			} finally {
				if (var5 != null) {
					try {
						var5.close();
					} catch (Exception var14) {
						var14.printStackTrace(System.err);
					}
				}

			}

			return this.C;
		}

		public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
			String var5;
			if ("value-range".equals(var3)) {
				this.C.clear();
				var5 = var4.getValue("default-ref");
				if (null != var5) {
					var5 = var5.trim();
					if (0 == var5.trim().length()) {
						throw new RuntimeException(this.E + ": value-range/value-ref "
								+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					this.C.put("default-ref", var5);
				}
			} else if ("value".equals(var3)) {
				this.B = new ValueRange();
				var5 = var4.getValue("value");
				if (null == var5) {
					throw new RuntimeException(this.E + ": value-range/value/@value "
							+ MultiLanguageResourceBundle.getInstance().getString("null"));
				}

				var5 = var5.trim();
				if (0 == var5.length()) {
					throw new RuntimeException(this.E + ": value-range/value/@value "
							+ MultiLanguageResourceBundle.getInstance().getString("blank"));
				}

				if (this.C.containsKey(var5)) {
					throw new RuntimeException(
							this.E + ": value-range/value[@value='" + var5 + "'] " + MultiLanguageResourceBundle
									.getInstance().getString("MessageMetadataManager.startElement.reduplicate"));
				}

				this.B.setValue(var5);
				this.B.setShortText(var4.getValue("short-text"));
				var5 = var4.getValue("reference");
				if (var5 != null) {
					var5 = var5.trim();
					if (0 == var5.length()) {
						throw new RuntimeException(this.E + ": value-range/value/@reference "
								+ MultiLanguageResourceBundle.getInstance().getString("blank"));
					}

					this.B.setReferenceId(var5);
					Message var6 = MessageMetadataManager.getMessage(this.A, var5);
					this.B.setReference(var6);
					if (null == this.C.get("default-ref")) {
						throw new RuntimeException(this.E + ": value-range[@value='" + this.B.getValue()
								+ "']/@default-ref " + MultiLanguageResourceBundle.getInstance().getString("null"));
					}
				}

				this.C.put(this.B.getValue(), this.B);
			}

		}

		public void endElement(String var1, String var2, String var3) throws SAXException {
		}

		public void characters(char[] var1, int var2, int var3) throws SAXException {
			if (null == this.D) {
				this.D = new String(var1, var2, var3);
			} else {
				this.D = this.D + new String(var1, var2, var3);
			}

		}

		// $FF: synthetic method
		_B(int var1) {
			this();
		}
	}

	static Map access$300() {
		return groupPathCache;
	}

	static Map access$100() {
		return cacheByClass;
	}
}
