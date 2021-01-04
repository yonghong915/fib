package com.fib.gateway.message.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.util.EnumConstants;
import com.fib.gateway.message.util.ExceptionUtil;
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
	private static Logger logger = LoggerFactory.getLogger(MessageMetadataManager.class);
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
	protected static final Set<String> keyWordSet = new HashSet<>();

	public MessageMetadataManager() {
		this.message = new Message();
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
		field.setFieldType(EnumConstants.FieldType.getFieldTypeCode(attrValue));

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

	public static Message getMessage(String groupId, String messageId) {
		ExceptionUtil.requireNotEmpty(groupId, "parameter.null ");
		ExceptionUtil.requireNotEmpty(messageId, "parameter.null ");

		Map<String, Message> groupCache = cache.get(groupId);
		ExceptionUtil.requireNotEmpty(groupCache, "getMessage.group.null ");

		Message message = groupCache.get(messageId);
		if (null != message) {
			return message;
		}

		File file = groupPathCache.get(groupId);
		File messageFile = new File(file.toString() + System.getProperty("file.separator") + messageId + ".xml");
		logger.info("parse MessageBean file is :{}", messageFile.getAbsolutePath());
		message = load(groupId, messageFile);
		ExceptionUtil.requireNotEmpty(message, "getMessage.file.isNotExist ");

		if (!messageId.equals(message.getId())) {
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
					"MessageMetadataManager.getMessage.messageId.notEqual.fileName",
					new String[] { messageId, message.getId() }));
		}

		if (!MessageBean.COMMON_MESSAGEBEAN_CLASS.equals(message.getClassName())
				&& cacheByClass.containsKey(message.getClassName())) {
			Message var6 = cacheByClass.get(message.getClassName());
			if (!message.equalTo(var6)) {
				throw new CommonException(MultiLanguageResourceBundle.getInstance().getString(
						"MessageMetadataManager.getMessage.messageClass.reduplicate",
						new String[] { messageId, groupId, message.getClassName() }));
			}
		}
		groupCache.put(messageId, message);
		cacheByClass.put(message.getClassName(), message);
		return message;
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

		File[] files = messageBeanDir.listFiles((File dir, String name) -> name.endsWith(".xml"));

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

			if (!MessageBean.COMMON_MESSAGEBEAN_CLASS.equals(message.getClassName())
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

	static Map<String, File> getGroupPathCache() {
		return groupPathCache;
	}

	static Map<String, Message> getClassCache() {
		return cacheByClass;
	}
}
