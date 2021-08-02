package com.fib.upp.service.gateway.message.metadata;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.upp.service.gateway.constant.EnumConstants;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

public class MessageMetadataManager {
	private static Logger logger = LoggerFactory.getLogger(MessageMetadataManager.class);

	private MessageMetaData message;

	private List<Field> fieldList = new ArrayList<>();

	public static Map<String, MessageMetaData> loadMetadataGroup(String channelId, File messageBeanDir) {
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
		Map<String, MessageMetaData> messageMap = new HashMap<>();
		List<File> files = FileUtil.loopFiles(messageBeanDir,
				(File name) -> name.isFile() && name.getName().toLowerCase().endsWith(".xml"));

		String prefixFileName = null;
		String fileName = null;
		MessageMetaData messageMetaData = null;
		for (int i = 0, len = files.size(); i < len; ++i) {
			File file = files.get(i);
			fileName = file.getAbsolutePath();
			prefixFileName = fileName.substring(0, fileName.lastIndexOf("."));
			messageMetaData = load(channelId, file);
			messageMap.put(prefixFileName, messageMetaData);
		}
		return messageMap;
	}

	private static MessageMetaData load(String channelId, File beanfile) {
		MessageHandler messageHandler = new MessageHandler();
		return messageHandler.parseFile(channelId, beanfile);
	}

	public static void main(String[] args) {
		String messageBeanDir = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-application\\fib-upp\\src\\main\\resources\\config";
		List<File> files = FileUtil.loopFiles(messageBeanDir,
				(File name) -> name.isFile() && name.getName().toLowerCase().endsWith(".xml"));
		System.out.println(files.size());
		for (File file : files) {
			System.out.println(file.getAbsolutePath());
		}
	}
}
