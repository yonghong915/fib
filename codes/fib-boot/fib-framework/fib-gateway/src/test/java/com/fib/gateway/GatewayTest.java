package com.fib.gateway;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.gateway.message.MessagePacker;
import com.fib.gateway.message.MessageParser;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.packer.AbstractMessagePacker;
import com.fib.gateway.message.parser.AbstractMessageParser;
import com.fib.upp.cnaps2.messagebean.recv.req.TransferMessageBean;

import cn.hutool.core.util.RandomUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
public class GatewayTest {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testMessagePackAndParse() throws Exception {
		String groupId = "TransferMessageBean";
		String messageId = "TransferMessageBean";
		String channelDeployPath = "E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\upp_cnaps2_recv\\";
		String messageBeanPath = channelDeployPath + "message-bean";
		File messageBeanDir = new File(messageBeanPath);
		MessageMetadataManager.loadMetadataGroup(groupId, messageBeanDir);

		TransferMessageBean bean = new TransferMessageBean();

		bean.setAccountNo("Acct" + RandomUtil.getSecureRandom().nextInt(32));
		bean.setAccountDate(LocalDateTime.now().toString());
		bean.setAccountBalance(String.valueOf(RandomUtil.getSecureRandom().nextDouble()));
		bean.setBalanceDirection("2345678");
		bean.setMsgNo("No" + RandomUtil.getSecureRandom().nextInt(64));

//		Message message = new Message();
//		message.setType(EnumConstants.MsgType.XML.getCode());
//		String template = "<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n" + 
//				"<MSG>\r\n" + 
//				"	<HEAD>\r\n" + 
//				"		<MSGNO>${msgNo}</MSGNO>\r\n" + 
//				"	</HEAD>\r\n" + 
//				"	<CFX>\r\n" + 
//				"		<ZH>${accountNo}</ZH>\r\n" + 
//				"		<ACSDATE>${accountDate}</ACSDATE>\r\n" + 
//				"		<ZHYE>${accountBalance}</ZHYE>\r\n" + 
//				"		<YEFX>${balanceDirection}</YEFX>\r\n" + 
//				"	</CFX>\r\n" + 
//				"</MSG>";
//		message.setTemplate(template);

		// 组报
		AbstractMessagePacker packer = new MessagePacker();
		Message message = MessageMetadataManager.getMessage(groupId, messageId);
		packer.setMessage(message);
		packer.setMessageBean(bean);
		byte[] m = packer.pack();
		logger.info(new String(m));
		// System.out.println(CodeUtil.Bytes2FormattedText(m));

		// 解报
		AbstractMessageParser parser = new MessageParser();
		parser.setMessage(message);
		String reqData = "<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n" + "<MSG>\r\n" + "	<HEAD>\r\n"
				+ "		<MSGNO>012345</MSGNO>\r\n" + "	</HEAD>\r\n" + "	<CFX>\r\n" + "		<ZH>012345678</ZH>\r\n"
				+ "		<ACSDATE>2012-12-30 12:30:00</ACSDATE>\r\n" + "		<ZHYE>666.66</ZHYE>\r\n"
				+ "		<YEFX>66666</YEFX>\r\n" + "	</CFX>\r\n" + "</MSG>";
		byte[] messageData = reqData.getBytes();
		parser.setMessageData(messageData);
		MessageBean parserBean = parser.parse();
		logger.info(parserBean.toString());
		assertNotNull(parserBean);
	}
}
