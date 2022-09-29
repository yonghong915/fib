package com.fib.upp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.msgconverter.message.bean.generator.MessageBeanCodeGenerator;
import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.message.metadata.MessageMetadataManager;
import com.fib.msgconverter.message.metadata.handler.MessageHandler;

import cn.hutool.core.io.FileUtil;

public class TestMessageMetadataManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
//	@Test
//	public void test() {
////		String groupId = "npc_cnaps2_recv";
////		String messageBeanDir = "outpro/deploy/npc_cnaps2_recv";
////		MessageMetadataManager.loadMetadataGroup(groupId, messageBeanDir);
////
////		MessageMetadataManager.getAllMessage();
//         
//	}

	public static void main(String[] args) {
		// MessageHandler handler = new MessageHandler();
		File file = FileUtil.file("outpro\\deploy\\npc_cnaps2_recv\\message-bean");
		// handler.setGroupId("npc_cnaps2_recv");
		// Message message = handler.parse(file);
		String groupId = "npc_cnaps2_recv";
		List<String> modifiedFiles = new ArrayList<String>();
		MessageMetadataManager.loadMetadataGroup(groupId, file);
		// LOGGER.info("message={}", message);
		String channelDeployPath = "outpro/deploy/npc_cnaps2_recv/";
		String messageBeanPath = channelDeployPath + "message-bean";
		File messageBeanDir = FileUtil.file(messageBeanPath);
		Map<String, Message> allMsgInGroup = MessageMetadataManager.getAllMessage().get(groupId);
		Iterator<Message> iter = allMsgInGroup.values().iterator();
		File srcFile = null;
		File classFile = null;
		String srcFilePath = null;
		String classFilePath = null;
		Message msg = null;
		String classRootPath = "";
		String srcRootPath = null;
		try {
			srcRootPath = new File(channelDeployPath + "src" + File.separatorChar).getCanonicalPath() + File.separator;
			classRootPath = new File(channelDeployPath + "classes" + File.separatorChar).getCanonicalPath() + File.separatorChar;
		} catch (IOException e) {
			e.printStackTrace();
		}

		MessageBeanCodeGenerator mbcg = new MessageBeanCodeGenerator();
		mbcg.setOutputDir(srcRootPath);
		long configLastModifyTime = 0;
		String encoding = "UTF-8";
		while (iter.hasNext()) {
			msg = iter.next();
			LOGGER.info("msg={}", msg);
			srcFilePath = srcRootPath + msg.getClassName().replace('.', File.separatorChar) + ".java";
			classFilePath = classRootPath + msg.getClassName().replace('.', File.separatorChar) + ".class";
			srcFile = new File(srcFilePath);
			classFile = new File(classFilePath);
			configLastModifyTime = new File(messageBeanDir + File.separator + msg.getId() + ".xml").lastModified();
			if (!srcFile.exists() || !classFile.exists() || srcFile.lastModified() < configLastModifyTime) {
				// 配置文件有改变，生成java代码，加入列表
				mbcg.generate(msg, encoding);
				modifiedFiles.add(srcFilePath);
			}
		}
	}
}
