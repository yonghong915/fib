package com.fib.gateway;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.gateway.message.MessageParser;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.parser.AbstractMessageParser;
import com.fib.gateway.message.parser.DefaultMessageParser;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class)
public class MessageBeanTest {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testMessagePackAndParse() throws Exception {
		String groupId = "BEPS_121_bean";
		String messageId = "BEPS_121_bean";
		String channelDeployPath = "E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\npc_cnaps2_recv\\";
		String messageBeanPath = channelDeployPath + "message-bean";
		File messageBeanDir = new File(messageBeanPath);
		
		//CommGateway cg = new CommGateway();
		//cg.generateSourceFile(channelDeployPath, null);
		
		
		MessageMetadataManager.loadMetadataGroup(groupId, messageBeanDir);

		Message message = MessageMetadataManager.getMessage(groupId, messageId);

		// 解报
		AbstractMessageParser parser = new DefaultMessageParser();
		parser.setMessage(message);
		String reqData = "{H:02102100099996  BEPS313684093748  BEPS20210323114139XMLbeps.121.001.01     BEPA6014CC2517958865600000000015614348923DXN       }^M\r\n" + 
				"{S:MEQCIDTEMnXdcwMt/aKtqmZf3mkVNiQGgSI7juJ9xJrRtdfJAiB4xQk2XFafsjeIUe7Sx1n5SF8F\r\n" + 
				"CJtIIAzIMNgHIMXq0g==}^M\r\n" + 
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n" + 
				"<Document xmlns=\"urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
				"\r\n" + 
				"  <CstmrCdtTrf>\r\n" + 
				"    <PKGGrpHdr>\r\n" + 
				"      <MsgId>2021032375961815</MsgId>\r\n" + 
				"      <CreDtTm>2021-03-23T11:41:39</CreDtTm>\r\n" + 
				"      <InstgPty>\r\n" + 
				"        <InstgDrctPty>102100099996</InstgDrctPty>\r\n" + 
				"      </InstgPty>\r\n" + 
				"      <InstdPty>\r\n" + 
				"        <InstdDrctPty>313684093748</InstdDrctPty>\r\n" + 
				"      </InstdPty>\r\n" + 
				"      <NbOfTxs>1</NbOfTxs>\r\n" + 
				"      <CtrlSum Ccy=\"CNY\">100.00</CtrlSum>\r\n" + 
				"      <SysCd>BEPS</SysCd>\r\n" + 
				"    </PKGGrpHdr>\r\n" + 
				"    <NPCPrcInf>\r\n" + 
				"      <PrcSts>PR03</PrcSts>\r\n" + 
				"      <PrcCd>NEZI0000</PrcCd>\r\n" + 
				"      <NetgDt>2021-03-23</NetgDt>\r\n" + 
				"      <NetgRnd>03</NetgRnd>\r\n" + 
				"      <RcvTm>2021-03-23T11:41:39</RcvTm>\r\n" + 
				"      <TrnsmtTm>2021-03-23T11:41:39</TrnsmtTm>\r\n" + 
				"    </NPCPrcInf>\r\n" + 
				"    <CstmrCdtTrfInf>\r\n" + 
				"      <TxId>2021032332670371</TxId>\r\n" + 
				"      <Dbtr>\r\n" + 
				"        <Nm>待报解预算收入-待报解预算收入（代理支库专用）</Nm>\r\n" + 
				"        <PstlAdr>\r\n" + 
				"          <AdrLine>null</AdrLine>\r\n" + 
				"        </PstlAdr>\r\n" + 
				"      </Dbtr>\r\n" + 
				"      <DbtrAcct>\r\n" + 
				"        <Id>\r\n" + 
				"          <Othr>\r\n" + 
				"            <Id>3100028011200311022</Id>\r\n" + 
				"            <Issr>102653005001</Issr>\r\n" + 
				"          </Othr>\r\n" + 
				"        </Id>\r\n" + 
				"      </DbtrAcct>\r\n" + 
				"      <DbtrAgt>\r\n" + 
				"        <BrnchId>\r\n" + 
				"          <Id>102653005077</Id>\r\n" + 
				"        </BrnchId>\r\n" + 
				"      </DbtrAgt>\r\n" + 
				"      <CdtrAgt>\r\n" + 
				"        <BrnchId>\r\n" + 
				"          <Id>313684093748</Id>\r\n" + 
				"        </BrnchId>\r\n" + 
				"      </CdtrAgt>\r\n" + 
				"      <Cdtr>\r\n" + 
				"        <Nm>左建英</Nm>\r\n" + 
				"      </Cdtr>\r\n" + 
				"      <CdtrAcct>\r\n" + 
				"        <Id>\r\n" + 
				"          <Othr>\r\n" + 
				"            <Id>6231990000001208674</Id>\r\n" + 
				"            <Issr>313684093748</Issr>\r\n" + 
				"          </Othr>\r\n" + 
				"        </Id>\r\n" + 
				"      </CdtrAcct>\r\n" + 
				"      <Amt Ccy=\"CNY\">100.00</Amt>\r\n" + 
				"      <PmtTpInf>\r\n" + 
				"        <CtgyPurp>\r\n" + 
				"          <Prtry>A100</Prtry>\r\n" + 
				"        </CtgyPurp>\r\n" + 
				"      </PmtTpInf>\r\n" + 
				"      <Purp>\r\n" + 
				"        <Prtry>02102</Prtry>\r\n" + 
				"      </Purp>\r\n" + 
				"      <AddtlInf>621032010221687457</AddtlInf>\r\n" + 
				"    </CstmrCdtTrfInf>\r\n" + 
				"  </CstmrCdtTrf>";
		byte[] messageData = reqData.getBytes();
		parser.setMessageData(messageData);
		MessageBean parserBean = parser.parse();
		logger.info(parserBean.toString());
		assertNotNull(parserBean);
	}
}
