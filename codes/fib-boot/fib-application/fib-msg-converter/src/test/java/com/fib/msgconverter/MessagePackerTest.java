package com.fib.msgconverter;


import com.fib.msgconverter.message.MessagePacker;
import com.fib.msgconverter.message.MessageParser;
import com.fib.msgconverter.message.bean.MessageBean;
import com.fib.msgconverter.message.bean.impl.Ccms801Out;
import com.fib.msgconverter.message.bean.impl.GrpHdr;
import com.fib.msgconverter.message.metadata.Message;
import com.fib.msgconverter.message.metadata.handler.MessageMetadataManager;
import com.giantstone.common.config.ConfigManager;
import com.giantstone.common.util.CodeUtil;

import cn.hutool.json.JSONUtil;

public class MessagePackerTest {
	public static void main(String[] args) {
		String groupId = "npc_cnaps2_recv";
		String messageId = "Ccms801Out";
		ConfigManager.setRootPath(
				"E:\\\\git_source\\\\fib-feature-buildenv\\\\fib\\\\codes\\\\fib-boot\\\\fib-application\\\\fib-msg-converter\\\\src\\\\main\\\\resources\\\\");
		String path = "deploy\\cnaps";
		MessageMetadataManager.loadMetadataGroup(groupId, path);

		Ccms801Out t = new Ccms801Out();
		GrpHdr grpHdr = new GrpHdr();
		grpHdr.setMessageIdentification("1233445566");
		grpHdr.setCreationDateTime("2023-08-30");
		t.setGrpHdr(grpHdr);

		t.setCurrentSystemDate("20230830");
		t.setCurrentSystemStatus("01");
		MessagePacker packer = new MessagePacker();
		Message message = MessageMetadataManager.getMessage(groupId, messageId);
		packer.setMessage(message);
		packer.setMessageBean(t);

		byte[] byts = packer.pack();
		System.out.println(CodeUtil.Bytes2FormattedText(byts));

		MessageParser parser = new MessageParser();
		message = MessageMetadataManager.getMessage(groupId, messageId);
		parser.setMessage(message);
		parser.setMessageData(byts);
		MessageBean mb = parser.parse();
		System.out.println("mb=" + JSONUtil.toJsonPrettyStr(mb));
		
	}

}
