package com.fib.msgconverter.message.metadata;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.BusinessException;
import com.fib.commons.exception.CommonException;
import com.fib.msgconverter.message.metadata.handler.MessageHandler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;

public final class MessageMetadataManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageMetadataManager.class);
	private static Map<String, Map<String, Message>> cache = new HashMap<>(128);
	private static final Map<String, File> groupPathCache = new HashMap<>(128);
	private static final Map<String, Message> cacheByClass = new HashMap<>(1024);
	private static final Set<String> keyWordSet = new HashSet<>();

	private MessageMetadataManager() {
	}

	public static Set<String> keyWordSet() {
		return keyWordSet;
	}

	static {
		keyWordSet.add("parent");
		keyWordSet.add("Parent");
		keyWordSet.add("metadata");
		keyWordSet.add("Metadata");
	}

	public static Map<String, Message> cacheClass() {
		return cacheByClass;
	}

	public static void putCacheClass(String s, Message message) {
		cacheByClass.put(s, message);
	}

	public static void loadMetadataGroup(String groupId, String path) {
		Assert.notBlank(groupId, () -> new CommonException("parameter.null.groupId"));
		Assert.notBlank(path, () -> new CommonException("parameter.null.path"));

		File file = FileUtil.file(path);
		loadMetadataGroup(groupId, file);
	}

	public static void loadMetadataGroup(String groupId, File file) {
		Assert.notBlank(groupId, () -> new IllegalArgumentException("parameter.null.groupId"));
		Assert.notNull(file, () -> new IllegalArgumentException("parameter.null.dir"));
		Assert.isTrue(file.isDirectory(), () -> new CommonException("MessageMetadataManager.loadMetadataGroup.dir.isNotDirectory"));
		Assert.isTrue(file.canRead(), () -> new CommonException("MessageMetadataManager.loadMetadataGroup.dir.canNotRead"));

		File[] afile = file.listFiles((subFile, fileName) -> fileName.endsWith(".xml"));
		Map<String, Message> obj = cache.computeIfAbsent(groupId, key -> {
			groupPathCache.put(groupId, file);
			return new HashMap<>(1024);
		});

		for (int i = 0, len = afile.length; i < len; i++) {
			String s2 = afile[i].getName();
			String s1 = s2.substring(0, s2.lastIndexOf("."));
			if (null != obj.get(s1)) {
				continue;
			}

			Message message = load(groupId, afile[i]);
			Assert.isTrue(s1.equals(message.getId()), () -> new CommonException("MessageMetadataManager.getMessage.messageId.notEqual.fileName"));
			LOGGER.info("message={}", message);
			obj.put(s1, message);
		}
	}

	private static Message load(String groupId, File file) {
		MessageHandler handler = new MessageHandler();
		handler.setGroupId(groupId);
		return handler.parse(file);
	}

	public static Message getMessage(String groupId, String messageId) {
		Assert.notBlank(groupId, () -> new BusinessException("aaaaa", "parameter.null.groupId"));
		Assert.notBlank(messageId, () -> new BusinessException("aaaaa", "parameter.null.messageId"));
		Map<String, Message> map = cache.get(groupId);
		Assert.notNull(map, () -> new BusinessException("aaaaa", "MessageMetadataManager.getMessage.group.null " + groupId));
		Message message = map.get(messageId);
		if (null == message) {
			File file = groupPathCache.get(groupId);
			File file1 = new File((new StringBuilder()).append(file.toString()).append(System.getProperty("file.separator")).append(messageId)
					.append(".xml").toString());
			message = load(groupId, file1);
			Assert.notNull(message, () -> new CommonException("MessageMetadataManager.getMessage.file.isNotExist " + messageId));

			Assert.isTrue(messageId.equals(message.getId()),
					() -> new CommonException("MessageMetadataManager.getMessage.messageId.notEqual.fileName"));

			if (cacheByClass.containsKey(message.getClassName())) {
				Message message1 = cacheByClass.get(message.getClassName());

				// Assert.isTrue(message.equalTo(message1), () -> new
				// CommonException("MessageMetadataManager.getMessage.messageClass.reduplicate"));
			}
			map.put(messageId, message);
			cacheByClass.put(message.getClassName(), message);
		}
		return message;
	}

	public static Map<String, Map<String, Message>> getAllMessage() {
		return cache;
	}
}
