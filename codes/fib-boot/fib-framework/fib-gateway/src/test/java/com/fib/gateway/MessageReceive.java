package com.fib.gateway;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fib.gateway.classloader.AppendableURLClassloader;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Field;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.parser.DefaultMessageParser;

public class MessageReceive {
	public static void execute() {
		String receiveMsg = "{H:02102100099996  BEPS313684093748  BEPS20210323114139XMLbeps.121.001.01     BEPA6014CC2517958865600000000015614348923DXN       }^M\r\n"
				+ "{S:MEQCIDTEMnXdcwMt/aKtqmZf3mkVNiQGgSI7juJ9xJrRtdfJAiB4xQk2XFafsjeIUe7Sx1n5SF8F\r\n"
				+ "CJtIIAzIMNgHIMXq0g==}^M\r\n" + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n"
				+ "<Document xmlns=\"urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
				+ "\r\n" + "  <CstmrCdtTrf>\r\n" + "    <PKGGrpHdr>\r\n" + "      <MsgId>2021032375961815</MsgId>\r\n"
				+ "      <CreDtTm>2021-03-23T11:41:39</CreDtTm>\r\n" + "      <InstgPty>\r\n"
				+ "        <InstgDrctPty>102100099996</InstgDrctPty>\r\n" + "      </InstgPty>\r\n"
				+ "      <InstdPty>\r\n" + "        <InstdDrctPty>313684093748</InstdDrctPty>\r\n"
				+ "      </InstdPty>\r\n" + "      <NbOfTxs>1</NbOfTxs>\r\n"
				+ "      <CtrlSum Ccy=\"CNY\">100.00</CtrlSum>\r\n" + "      <SysCd>BEPS</SysCd>\r\n"
				+ "    </PKGGrpHdr>\r\n" + "    <NPCPrcInf>\r\n" + "      <PrcSts>PR03</PrcSts>\r\n"
				+ "      <PrcCd>NEZI0000</PrcCd>\r\n" + "      <NetgDt>2021-03-23</NetgDt>\r\n"
				+ "      <NetgRnd>03</NetgRnd>\r\n" + "      <RcvTm>2021-03-23T11:41:39</RcvTm>\r\n"
				+ "      <TrnsmtTm>2021-03-23T11:41:39</TrnsmtTm>\r\n" + "    </NPCPrcInf>\r\n"
				+ "    <CstmrCdtTrfInf>\r\n" + "      <TxId>2021032332670371</TxId>\r\n" + "      <Dbtr>\r\n"
				+ "        <Nm>待报解预算收入-待报解预算收入（代理支库专用）</Nm>\r\n" + "        <PstlAdr>\r\n"
				+ "          <AdrLine>null</AdrLine>\r\n" + "        </PstlAdr>\r\n" + "      </Dbtr>\r\n"
				+ "      <DbtrAcct>\r\n" + "        <Id>\r\n" + "          <Othr>\r\n"
				+ "            <Id>3100028011200311022</Id>\r\n" + "            <Issr>102653005001</Issr>\r\n"
				+ "          </Othr>\r\n" + "        </Id>\r\n" + "      </DbtrAcct>\r\n" + "      <DbtrAgt>\r\n"
				+ "        <BrnchId>\r\n" + "          <Id>102653005077</Id>\r\n" + "        </BrnchId>\r\n"
				+ "      </DbtrAgt>\r\n" + "      <CdtrAgt>\r\n" + "        <BrnchId>\r\n"
				+ "          <Id>313684093748</Id>\r\n" + "        </BrnchId>\r\n" + "      </CdtrAgt>\r\n"
				+ "      <Cdtr>\r\n" + "        <Nm>左建英</Nm>\r\n" + "      </Cdtr>\r\n" + "      <CdtrAcct>\r\n"
				+ "        <Id>\r\n" + "          <Othr>\r\n" + "            <Id>6231990000001208674</Id>\r\n"
				+ "            <Issr>313684093748</Issr>\r\n" + "          </Othr>\r\n" + "        </Id>\r\n"
				+ "      </CdtrAcct>\r\n" + "      <Amt Ccy=\"CNY\">100.00</Amt>\r\n" + "      <PmtTpInf>\r\n"
				+ "        <CtgyPurp>\r\n" + "          <Prtry>A100</Prtry>\r\n" + "        </CtgyPurp>\r\n"
				+ "      </PmtTpInf>\r\n" + "      <Purp>\r\n" + "        <Prtry>02102</Prtry>\r\n"
				+ "      </Purp>\r\n" + "      <AddtlInf>621032010221687457</AddtlInf>\r\n"
				+ "    </CstmrCdtTrfInf>\r\n" + "  </CstmrCdtTrf>";

		try {

			String sr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n"
					+ "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.02\">\r\n" + "\r\n"
					+ "  <FIToFICstmrCdtTrf>\r\n" + "    <GrpHdr>\r\n" + "      <MsgId>2021012100823355</MsgId>\r\n"
					+ "      <CreDtTm>2021-01-21T09:09:34</CreDtTm>\r\n" + "      <NbOfTxs>1</NbOfTxs>\r\n"
					+ "      <SttlmInf>\r\n" + "        <SttlmMtd>CLRG</SttlmMtd>\r\n" + "      </SttlmInf>\r\n"
					+ "    </GrpHdr>\r\n" + "    <CdtTrfTxInf>\r\n" + "      <PmtId>\r\n"
					+ "        <EndToEndId>2021012100823355</EndToEndId>\r\n"
					+ "        <TxId>2021012100823355</TxId>\r\n" + "      </PmtId>\r\n" + "      <PmtTpInf>\r\n"
					+ "        <CtgyPurp>\r\n" + "          <Prtry>A100</Prtry>\r\n" + "        </CtgyPurp>\r\n"
					+ "      </PmtTpInf>\r\n" + "      <IntrBkSttlmAmt Ccy=\"CNY\">200000.00</IntrBkSttlmAmt>\r\n"
					+ "      <SttlmPrty>NORM</SttlmPrty>\r\n" + "      <ChrgBr>DEBT</ChrgBr>\r\n"
					+ "      <InstgAgt>\r\n" + "        <FinInstnId>\r\n" + "          <ClrSysMmbId>\r\n"
					+ "            <MmbId>313163000004</MmbId>\r\n" + "          </ClrSysMmbId>\r\n"
					+ "        </FinInstnId>\r\n" + "        <BrnchId>\r\n" + "          <Id>313163000107</Id>\r\n"
					+ "        </BrnchId>\r\n" + "      </InstgAgt>\r\n" + "      <InstdAgt>\r\n"
					+ "        <FinInstnId>\r\n" + "          <ClrSysMmbId>\r\n"
					+ "            <MmbId>313684093748</MmbId>\r\n" + "          </ClrSysMmbId>\r\n"
					+ "        </FinInstnId>\r\n" + "        <BrnchId>\r\n" + "          <Id>313684004019</Id>\r\n"
					+ "        </BrnchId>\r\n" + "      </InstdAgt>\r\n" + "      <IntrmyAgt1>\r\n"
					+ "        <FinInstnId>\r\n" + "          <ClrSysMmbId>\r\n"
					+ "            <MmbId>313684004019</MmbId>\r\n" + "          </ClrSysMmbId>\r\n"
					+ "        </FinInstnId>\r\n" + "      </IntrmyAgt1>\r\n" + "      <Dbtr>\r\n"
					+ "        <Nm>温州群策科技有限公司</Nm>\r\n" + "        <PstlAdr>\r\n"
					+ "          <AdrLine>阳泉商行</AdrLine>\r\n" + "        </PstlAdr>\r\n" + "      </Dbtr>\r\n"
					+ "      <DbtrAcct>\r\n" + "        <Id>\r\n" + "          <Othr>\r\n"
					+ "            <Id>501000000162645</Id>\r\n" + "          </Othr>\r\n" + "        </Id>\r\n"
					+ "      </DbtrAcct>\r\n" + "      <DbtrAgt>\r\n" + "        <FinInstnId>\r\n"
					+ "          <ClrSysMmbId>\r\n" + "            <MmbId>313163000107</MmbId>\r\n"
					+ "          </ClrSysMmbId>\r\n" + "        </FinInstnId>\r\n" + "      </DbtrAgt>\r\n"
					+ "      <CdtrAgt>\r\n" + "        <FinInstnId>\r\n" + "          <ClrSysMmbId>\r\n"
					+ "            <MmbId>313684004019</MmbId>\r\n" + "          </ClrSysMmbId>\r\n"
					+ "        </FinInstnId>\r\n" + "      </CdtrAgt>\r\n" + "      <Cdtr>\r\n"
					+ "        <Nm>西昌市汇通客运有限公司</Nm>\r\n" + "        <PstlAdr>\r\n"
					+ "          <AdrLine>受托支付</AdrLine>\r\n" + "        </PstlAdr>\r\n" + "      </Cdtr>\r\n"
					+ "      <CdtrAcct>\r\n" + "        <Id>\r\n" + "          <Othr>\r\n"
					+ "            <Id>04010306000005972</Id>\r\n" + "          </Othr>\r\n" + "        </Id>\r\n"
					+ "      </CdtrAcct>\r\n" + "      <Purp>\r\n" + "        <Prtry>02102</Prtry>\r\n"
					+ "      </Purp>\r\n" + "      <RmtInf>\r\n" + "        <Ustrd>/H01/受托支付</Ustrd>\r\n"
					+ "        <Ustrd>/H03/受托支付</Ustrd>\r\n" + "        <Ustrd>/C00/2021-01-21</Ustrd>\r\n"
					+ "      </RmtInf>\r\n" + "    </CdtTrfTxInf>\r\n" + "  </FIToFICstmrCdtTrf>\r\n" + "\r\n"
					+ "</Document>";
			final byte[] messageData = receiveMsg.getBytes("UTF-8");

//			try {
//				Document document = Dom4jUtils.getXPathDocument(new String(messageData),
//						"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.02");
//				String xpath = "ns:Document/ns:FIToFICstmrCdtTrf/ns:GrpHdr/ns:SttlmInf/ns:SttlmMtd";
//				String rst = Dom4jUtils.getXPathValue(document, xpath);
//				System.out.println("rst=" + rst);
//			} catch (DocumentException | SAXException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			String groupId = "BEPS_121_bean";
			String messageId = "BEPS_121_bean";
			String channelDeployPath = "E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\npc_cnaps2_recv\\";
			String messageBeanPath = channelDeployPath + "message-bean";
			File messageBeanDir = new File(messageBeanPath);
			MessageMetadataManager.loadMetadataGroup(groupId, messageBeanDir);

			Message message = MessageMetadataManager.getMessage(groupId, messageId);
			System.out.println("message=" + message);
//			
			AppendableURLClassloader cl = new AppendableURLClassloader(Thread.currentThread().getContextClassLoader());
			File file = new File("E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\npc_cnaps2_recv\\src\\com\\fib\\upp\\cnaps2\\messagebean\\recv\\req\\BEPS_121_bean.class");
			URL[] urls = new URL[10];
			try {
				urls[0] = file.toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cl.append(urls);
			Thread.currentThread().setContextClassLoader(cl);
			CommGateway cg = new CommGateway();
			cg.setId("cnaps2");
			cg.setDeployPath(
					"E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\");
			cg.start();

			DefaultMessageParser parser = new DefaultMessageParser();
			parser.setMessage(message);
			parser.setMessageData(messageData);
			MessageBean parserBean = parser.parse();
			System.out.println("bean=" + parserBean.toString());

			System.out.println("");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<Field> getFiled() {
		List<Field> fieldLst = new ArrayList<Field>();
		Field BeginFlag = new Field();
		BeginFlag.setName("BeginFlag");
		BeginFlag.setLength(3);
		BeginFlag.setDataType(2000);
		fieldLst.add(BeginFlag);

		Field VersionID = new Field();
		VersionID.setName("VersionID");
		VersionID.setLength(2);
		VersionID.setDataType(2000);
		fieldLst.add(VersionID);

		Field OrigSender = new Field();
		OrigSender.setName("OrigSender");
		OrigSender.setLength(14);
		OrigSender.setDataType(2000);
		fieldLst.add(OrigSender);

		Field OrigSenderSID = new Field();
		OrigSenderSID.setName("OrigSenderSID");
		OrigSenderSID.setLength(4);
		OrigSenderSID.setDataType(2000);
		fieldLst.add(OrigSenderSID);

		Field OrigReceiver = new Field();
		OrigReceiver.setName("OrigReceiver");
		OrigReceiver.setLength(14);
		OrigReceiver.setDataType(2000);
		fieldLst.add(OrigReceiver);

		Field OrigReceiverSID = new Field();
		OrigReceiverSID.setName("OrigReceiverSID");
		OrigReceiverSID.setLength(4);
		OrigReceiverSID.setDataType(2000);
		fieldLst.add(OrigReceiverSID);

		Field OrigSendDate = new Field();
		OrigSendDate.setName("OrigSendDate");
		OrigSendDate.setLength(8);
		OrigSendDate.setDataType(2000);
		fieldLst.add(OrigSendDate);

		Field OrigSendTime = new Field();
		OrigSendTime.setName("OrigSendTime");
		OrigSendTime.setLength(6);
		OrigSendTime.setDataType(2000);
		fieldLst.add(OrigSendTime);

		Field StructType = new Field();
		StructType.setName("StructType");
		StructType.setLength(3);
		StructType.setDataType(2000);
		fieldLst.add(StructType);

		Field MesgType = new Field();
		MesgType.setName("MesgType");
		MesgType.setLength(20);
		MesgType.setDataType(2000);
		fieldLst.add(MesgType);

		Field MesgID = new Field();
		MesgID.setName("MesgID");
		MesgID.setLength(20);
		MesgID.setDataType(2000);
		fieldLst.add(MesgID);

		Field MesgRefID = new Field();
		MesgRefID.setName("MesgRefID");
		MesgRefID.setLength(20);
		MesgRefID.setDataType(2000);
		fieldLst.add(MesgRefID);

		Field MesgPriority = new Field();
		MesgPriority.setName("MesgPriority");
		MesgPriority.setLength(1);
		MesgPriority.setDataType(2000);
		fieldLst.add(MesgPriority);

		Field MesgDirection = new Field();
		MesgDirection.setName("MesgDirection");
		MesgDirection.setLength(1);
		MesgDirection.setDataType(2000);
		fieldLst.add(MesgDirection);

		Field Reserve = new Field();
		Reserve.setName("Reserve");
		Reserve.setLength(9);
		Reserve.setDataType(2000);
		fieldLst.add(Reserve);

		Field EndFlag = new Field();
		EndFlag.setName("MesgRefID");
		EndFlag.setLength(1);
		EndFlag.setDataType(2000);
		fieldLst.add(EndFlag);

		return fieldLst;
	}

	public static void main(String[] args) {
		execute();
	}
}
