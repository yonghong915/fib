package com.fib.gateway;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.ClasspathUtil;
import com.fib.gateway.channel.config.ChannelConfig;
import com.fib.gateway.message.bean.generator.MessageBeanCodeGenerator;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.MessageMetadataManager;

import cn.hutool.core.collection.CollUtil;

public class CommGateway {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

	public void generateSourceFile(String deployPath, ChannelConfig channelConfig) {
		logger.info("generateSourceFile->generate source file,deploy path:{}", deployPath);
		String relSrcRootPath = deployPath + "src" + File.separatorChar;
		String messageBeanPath = deployPath + "message-bean";
		String relClassRootPath = deployPath + "classes";
		String srcRootPath = "";
		String classRootPath = "";
		try {
			srcRootPath = new File(relSrcRootPath).getCanonicalPath() + File.separatorChar;
			classRootPath = new File(relClassRootPath).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			throw new CommonException("");
		}

		File messageBeanDir = new File(messageBeanPath);
		if (!messageBeanDir.exists()) {
			logger.warn("path {} not exists.", messageBeanPath);
			return;
		}
		// TODO
		String channelId = "BEPS_121_bean";
		MessageMetadataManager.loadMetadataGroup(channelId, messageBeanDir);

		MessageBeanCodeGenerator mbcg = new MessageBeanCodeGenerator();
		mbcg.setOutputDir(srcRootPath);
		Map<String, Message> allMsgInGroup = MessageMetadataManager.getAllMessage().get(channelId);
		Iterator<Message> iter = allMsgInGroup.values().iterator();
		Message msg = null;
		String srcFilePath = "";
		String classFilePath = "";
		File srcFile = null;
		File classFile = null;
		List<String> modifiedFiles = new ArrayList<>();
		while (iter.hasNext()) {
			msg = iter.next();
			srcFilePath = srcRootPath + msg.getClassName().replace('.', File.separatorChar) + ".java";
			classFilePath = classRootPath + msg.getClassName().replace('.', File.separatorChar) + ".class";
			srcFile = new File(srcFilePath);
			classFile = new File(classFilePath);
			long configLastModifyTime = 0;
			configLastModifyTime = new File(messageBeanDir + File.separator + msg.getId() + ".xml").lastModified();
			if (!srcFile.exists() || !classFile.exists() || srcFile.lastModified() < configLastModifyTime) {
				// 配置文件有改变，生成java代码，加入列表
				mbcg.generate(msg, DEFAULT_ENCODING);
				modifiedFiles.add(srcFilePath);
			}
		}
		if (CollUtil.isNotEmpty(modifiedFiles)) {
			ClasspathUtil.compileFiles(modifiedFiles, srcRootPath, classRootPath, "UTF-8");
		}
	}
}
