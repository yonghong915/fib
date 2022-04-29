package com.fib.msgconverter;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.upp.message.MessageParser;
import com.fib.upp.message.bean.impl.Ccms801Bean;
import com.fib.upp.message.bean.impl.Ccms801Out;
import com.fib.upp.message.bean.impl.GrpHdr;
import com.fib.upp.message.bean.impl.MsgHeader;
import com.fib.upp.message.metadata.handler.MessageMetadataManager;
import com.giantstone.message.bean.MessageBean;

import cn.hutool.core.io.FileUtil;

public class AppTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);

	public static void main(String[] args) {
		String messageBeanGroupId = "aabdddd";
		String messageId = "Ccms801Bean";
		File messageBeanDir = FileUtil.file("deploy/cnaps");
		MessageMetadataManager.loadMetadataGroup(messageBeanGroupId, messageBeanDir);

		String data = FileUtil.readString("data/cnaps/ccms.801.001.02.txt", StandardCharsets.UTF_8);
		byte[] message = data.getBytes();
		MessageParser parser = new MessageParser();
		parser.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
		parser.setMessageData(message);

		MessageBean mb = parser.parse();
		Ccms801Bean cb = (Ccms801Bean) mb;
		MsgHeader mh = cb.getMsgHeader();
		LOGGER.info("MsgHeader:[{}]", mh.toString());

		Ccms801Out co = cb.getCcms801Out();
		GrpHdr gh = co.getGrpHdr();
		LOGGER.info("GrpHdr:[{}]", gh.toString());

		LOGGER.info("Ccms801Out:[{}]", co.toString());
	}
}
