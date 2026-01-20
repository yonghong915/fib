package com.fib.msgconverter.message.metadata.handler;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fib.commons.exception.BusinessException;
import com.fib.commons.exception.CommonException;
import com.fib.msgconverter.message.metadata.Message;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;
import cn.hutool.core.lang.Assert;

public class MessageMetadataManager {

	protected static final Set<String> keyWordSet;
	public static final boolean IS_REQUIRED = false;
	private static Map<String, Map<String, Message>> cache = new HashMap<>(128);
	static final Map<String, Message> cacheByClass = new HashMap<>(1024);
	static final Map<String, File> groupPathCache = new HashMap<>(128);

	private MessageMetadataManager() {
	}

	public static Map<String, Message> getClassCache() {
		return cacheByClass;
	}

	public static Map<String, File> getGroupPathCache() {
		return groupPathCache;
	}

	public static File getGroupPath(String s) {
		return groupPathCache.get(s);
	}

	public static void clear() {
		cache.clear();
		cacheByClass.clear();
		groupPathCache.clear();
	}

	public static Message getMessageByClass(String messageBeanClassName) {
		Assert.notBlank(messageBeanClassName, () -> new BusinessException("aaaaa", "parameter.null.messageBeanClassName"));
		return cacheByClass.get(messageBeanClassName);
	}

	public static Map<String, Map<String, Message>> getAllMessage() {
		return cache;
	}

	public static boolean isMessageExist(String groupId, String messageId) {
		Assert.notBlank(groupId, () -> new BusinessException("aaaaa", "parameter.null.groupId"));
		Assert.notBlank(messageId, () -> new BusinessException("aaaaa", "parameter.null.messageId"));

		Map<String, Message> map = cache.get(groupId);
		Assert.notNull(map, () -> new BusinessException("aaaaa", "MessageMetadataManager.getMessage.group.null " + groupId));

		Message message = map.get(messageId);
		return null != message;
	}

	public static void reload(String groupId, String messageId, String s2) {
		Assert.notBlank(groupId, () -> new BusinessException("aaaaa", "parameter.null.groupId"));
		Assert.notBlank(messageId, () -> new BusinessException("aaaaa", "parameter.null.messageId"));

		Map<String, Message> map = cache.get(groupId);
		Assert.notNull(map, () -> new BusinessException("aaaaa", "MessageMetadataManager.getMessage.group.null " + groupId));

		File file = groupPathCache.get(groupId);
		File file1 = new File((new StringBuilder()).append(file.toString()).append(System.getProperty("file.separator")).append(messageId)
				.append(".xml").toString());
		Message message = null;
		try {
			message = cacheByClass.remove(s2);
			Message message1 = load(groupId, file1);
			Assert.notNull(message1, () -> new CommonException("MessageMetadataManager.getMessage.file.isNotExist " + messageId));

			Assert.isTrue(messageId.equals(message1.getId()),
					() -> new CommonException("MessageMetadataManager.getMessage.messageId.notEqual.fileName"));
			map.put(messageId, message1);
			cacheByClass.put(message1.getClassName(), message1);
		} catch (Exception exception) {
			if (null != s2)
				cacheByClass.put(s2, message);
			ExceptionUtil.throwActualException(exception);
		}
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

				Assert.isTrue(message.equalTo(message1), () -> new CommonException("MessageMetadataManager.getMessage.messageClass.reduplicate"));
			}
			map.put(messageId, message);
			cacheByClass.put(message.getClassName(), message);
		}
		return message;
	}

	public static void loadMetadataGroup(String groupId, String path) {
		Assert.notBlank(path, () -> new CommonException("parameter.null.path"));

		File file = new File(ConfigManager.getFullPathFileName(path));
		loadMetadataGroup(groupId, file);
	}

	public static void loadMetadataGroup(String groupId, File file) {
		Assert.notBlank(groupId, () -> new IllegalArgumentException(
				MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "groupId" })));
		Assert.notNull(file,
				() -> new IllegalArgumentException(MultiLanguageResourceBundle.getInstance().getString("parameter.null", new String[] { "dir" })));

		Assert.isTrue(file.isDirectory(), () -> new CommonException("MessageMetadataManager.loadMetadataGroup.dir.isNotDirectory"));
		Assert.isTrue(file.canRead(), () -> new CommonException("MessageMetadataManager.loadMetadataGroup.dir.canNotRead"));

		File[] afile = file.listFiles((_, fileName) -> fileName.endsWith(".xml"));
       
		Map<String, Message> obj = cache.computeIfAbsent(groupId, _ -> {
			groupPathCache.put(groupId, file);
			return new HashMap<>(1024);
		});

		for (int i = 0; i < afile.length; i++) {
			String s2 = afile[i].getName();
			String s1 = s2.substring(0, s2.lastIndexOf("."));
			if (null != obj.get(s1))
				continue;
			Message message = load(groupId, afile[i]);

			Assert.isTrue(s1.equals(message.getId()), () -> new CommonException("MessageMetadataManager.getMessage.messageId.notEqual.fileName"));

			if (cacheByClass.containsKey(message.getClassName())) {
				Message message1 = cacheByClass.get(message.getClassName());

				Assert.isTrue(message.equalTo(message1),
						() -> new CommonException("MessageMetadataManager.loadMetadataGroup.messageClass.reduplicate"));
			}
			obj.put(s1, message);
			cacheByClass.put(message.getClassName(), message);
		}
	}

	public static void loadMetadataGroupByDB(String s) {
		//
	}

	private static Message load(String s, File file) {
		MessageHandler mh = new MessageHandler();
		return mh.parseMessage(s, file);
	}

	static {
		keyWordSet = new HashSet<>();
		keyWordSet.add("parent");
		keyWordSet.add("Parent");
		keyWordSet.add("metadata");
		keyWordSet.add("Metadata");
//		try {
//			if (FileUtil.isExist(ConfigManager.getFullPathFileName("messagebean.properties"))) {
//				Properties properties = ConfigManager.loadProperties("messagebean.properties");
//				if (null != properties.getProperty("required") && 0 != properties.getProperty("required").length()
//						&& "1".equalsIgnoreCase(properties.getProperty("required")))
//					isRequired = true;
//			}
//		} catch (Exception exception) {
//			exception.printStackTrace();
//			ExceptionUtil.throwActualException(exception);
//		}
	}
}
