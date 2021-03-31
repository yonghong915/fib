package com.fib.gateway;

import java.io.File;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.fib.commons.xml.Dom4jUtils;
import com.fib.gateway.classloader.AppendableURLClassloader;
import com.fib.gateway.message.MessageParser;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Message;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.parser.AbstractMessageParser;
import com.fib.gateway.message.parser.DefaultMessageParser;

import bsh.BshClassManager;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;

public class Test {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		Test t = new Test();
		try {
			// t.testLoadClass();
			//t.testMessagePackAndParse();
			t.testBsh();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testMessagePackAndParse() throws Exception {
		String groupId = "BEPS_121_bean";
		String messageId = "BEPS_121_bean";
		String channelDeployPath = "E:\\git_source\\fib\\codes\\fib-boot\\fib-framework\\fib-gateway\\src\\main\\resources\\config\\upp\\deploy\\npc_cnaps2_recv\\";
		String messageBeanPath = channelDeployPath + "message-bean";
		File messageBeanDir = new File(messageBeanPath);

		// CommGateway cg = new CommGateway();
		// cg.generateSourceFile(channelDeployPath, null);

		MessageMetadataManager.loadMetadataGroup(groupId, messageBeanDir);

		Message message = MessageMetadataManager.getMessage(groupId, messageId);

		// 解报
		AbstractMessageParser parser = new DefaultMessageParser();
		parser.setMessage(message);
		String reqData = "{H:02102100099996  BEPS313684093748  BEPS20210323114139XMLbeps.121.001.01     BEPA6014CC2517958865600000000015614348923DXN       }^M\r\n"
				// +
				// "{S:MEQCIDTEMnXdcwMt/aKtqmZf3mkVNiQGgSI7juJ9xJrRtdfJAiB4xQk2XFafsjeIUe7Sx1n5SF8F\r\n"
				// + "CJtIIAzIMNgHIMXq0g==}^M\r\n" + "<?xml version=\"1.0\" encoding=\"UTF-8\"
				// standalone=\"no\" ?>\r\n"
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
				+ "    </CstmrCdtTrfInf>\r\n" + "  </CstmrCdtTrf></Document>";
		byte[] messageData = reqData.getBytes();
		// parser.setMessageData(messageData);
		// MessageBean parserBean = parser.parse();
		// logger.info(parserBean.toString());
		String path = "E:/git_source/fib/codes/fib-boot/fib-framework/fib-gateway/src/main/resources/config/upp/deploy/npc_cnaps2_recv/classes/";

		URL newurl = new URL("file", null, path);
		AppendableURLClassloader cl = new AppendableURLClassloader(new URL[] { newurl },
				Thread.currentThread().getContextClassLoader());
		Thread.currentThread().setContextClassLoader(cl);

		String messageBeanGroupId = "BEPS_121_bean";
		MessageParser parser1 = new MessageParser();
		parser1.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
		parser1.setMessageData(messageData);

		Object obj = parser1.parse();
		System.out.println(obj);
		if (obj instanceof MessageBean) {
			MessageBean bean = (MessageBean) obj;
			Object out121 = bean.getAttribute("BEPS_121_Out");
			System.out.println(out121);
			MessageBean a = (MessageBean) out121;
			Object bb = a.getAttribute("ccti");
			System.out.println(bb);

			List list = (List) bb;
			for (int i = 0; i < list.size(); i++) {
				Object cc = list.get(i);
				System.out.println(cc);
			}
		}

	}

	public void testLoadClass() throws MalformedURLException {
		String reqData = // "{H:02102100099996 BEPS313684093748 BEPS20210323114139XMLbeps.121.001.01
							// BEPA6014CC2517958865600000000015614348923DXN }^M\r\n"
				// +
				// "{S:MEQCIDTEMnXdcwMt/aKtqmZf3mkVNiQGgSI7juJ9xJrRtdfJAiB4xQk2XFafsjeIUe7Sx1n5SF8F\r\n"
				// + "CJtIIAzIMNgHIMXq0g==}^M\r\n" + "<?xml version=\"1.0\" encoding=\"UTF-8\"
				// standalone=\"no\" ?>\r\n"
				"<Document xmlns=\"urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
						+ "\r\n" + "  <CstmrCdtTrf>\r\n" + "    <PKGGrpHdr>\r\n"
						+ "      <MsgId>2021032375961815</MsgId>\r\n"
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
						+ "          </Othr>\r\n" + "        </Id>\r\n" + "      </DbtrAcct>\r\n"
						+ "      <DbtrAgt>\r\n" + "        <BrnchId>\r\n" + "          <Id>102653005077</Id>\r\n"
						+ "        </BrnchId>\r\n" + "      </DbtrAgt>\r\n" + "      <CdtrAgt>\r\n"
						+ "        <BrnchId>\r\n" + "          <Id>313684093748</Id>\r\n" + "        </BrnchId>\r\n"
						+ "      </CdtrAgt>\r\n" + "      <Cdtr>\r\n" + "        <Nm>左建英</Nm>\r\n" + "      </Cdtr>\r\n"
						+ "      <CdtrAcct>\r\n" + "        <Id>\r\n" + "          <Othr>\r\n"
						+ "            <Id>6231990000001208674</Id>\r\n" + "            <Issr>313684093748</Issr>\r\n"
						+ "          </Othr>\r\n" + "        </Id>\r\n" + "      </CdtrAcct>\r\n"
						+ "      <Amt Ccy=\"CNY\">100.00</Amt>\r\n" + "      <PmtTpInf>\r\n" + "        <CtgyPurp>\r\n"
						+ "          <Prtry>A100</Prtry>\r\n" + "        </CtgyPurp>\r\n" + "      </PmtTpInf>\r\n"
						+ "      <Purp>\r\n" + "        <Prtry>02102</Prtry>\r\n" + "      </Purp>\r\n"
						+ "      <AddtlInf>621032010221687457</AddtlInf>\r\n" + "    </CstmrCdtTrfInf>\r\n"
						+ "  </CstmrCdtTrf></Document>";
		String xpath = "/./ns:Document/ns:CstmrCdtTrf/ns:CstmrCdtTrfInf";
		Document doc;
		try {
			doc = Dom4jUtils.getXPathDocument(reqData, "urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01");
			List<Node> list = Dom4jUtils.getNodes(doc, xpath);
			System.out.println(list.size());
			for (Node node : list) {
				String xpathNode = "/./ns:Document/ns:CstmrCdtTrf/ns:CstmrCdtTrfInf/ns:Dbtr/ns:Nm/text()";
				String aa = Dom4jUtils.getXPathValue(node, xpathNode);
				System.out.println(aa);
			}
		} catch (DocumentException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testBsh() {
		BshClassManager var0 = new BshClassManager();
		var0.setClassLoader(Thread.currentThread().getContextClassLoader());
		Interpreter parentInterpreter = new Interpreter(new StringReader(""), System.out, System.err, false,
				new NameSpace(var0, "global"), (Interpreter) null, (String) null);

		BshClassManager var1 = new BshClassManager();
		var1.setClassLoader(Thread.currentThread().getContextClassLoader());
		Interpreter interpreter = new Interpreter(new StringReader(""), System.out, System.err, false,
				new NameSpace(parentInterpreter.getNameSpace(), var1, "global"), (Interpreter) null, (String) null);
		String script = " StringBuffer buf = new StringBuffer(\"aaaa\"); buf.append(\"待报解预算收入\") ;"
				+ "buf.append(\"null\");System.out.println(buf.toString()) ";
		try {
			Object obj = interpreter.eval(script);
			System.out.println(obj);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
