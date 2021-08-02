package com.fib.commons.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.Test;

import com.fib.upp.xml.Message;
import com.fib.upp.xml.ModuleCacheManager;
import com.fib.upp.xml.XmlHelper;


public class TestXml {
	@Test
	public void testDom4j() {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.02\">\r\n "
				+ "  <FIToFICstmrCdtTrf>\r\n" + "    <GrpHdr>\r\n"
				+ "      <MsgId key=\"报文标识号\" signer=\"true\">123241223</MsgId>\r\n"
				+ "      <CreDtTm key=\"报文发送时间\" signer=\"true\">20120323</CreDtTm>\r\n"
				+ "      <BtchBookg key=\"固定填写false\">false</BtchBookg>\r\n"
				+ "      <NbOfTxs key=\"固定填写1\" signer=\"true\">1</NbOfTxs>\r\n"
				+ "      <CtrlSum key=\"金额\" signer=\"true\">2323.67</CtrlSum>\r\n"
				+ "      <IntrBkSttlmDt key=\"工作日期\"></IntrBkSttlmDt>\r\n" + "      <SttlmInf>\r\n"
				+ "        <SttlmMtd key=\"固定填写CLRG\">CLRG</SttlmMtd>\r\n" + "      </SttlmInf>\r\n" + "    </GrpHdr>\r\n"
				+ "    <CdtTrfTxInf>\r\n" + "      <PmtId>\r\n"
				+ "        <EndToEndId key=\"端到端标识号\" signer=\"true\"></EndToEndId>\r\n"
				+ "        <TxId key=\"支付业务序号\" signer=\"true\"></TxId>\r\n" + "      </PmtId>\r\n"
				+ "      <PmtTpInf>\r\n" + "        <InstrPrty key=\"固定填写NORM\"></InstrPrty>\r\n"
				+ "        <CtgyPurp>\r\n" + "          <Prtry key=\"业务类型编码集\" signer=\"true\"></Prtry>\r\n"
				+ "        </CtgyPurp>\r\n" + "      </PmtTpInf>\r\n"
				+ "      <IntrBkSttlmAmt Ccy=\"CNY\" key=\"金额\" signer=\"true\"></IntrBkSttlmAmt>\r\n"
				+ "      <AccptncDtTm key=\"业务受理时间\" signer=\"true\"></AccptncDtTm>\r\n"
				+ "      <ChrgBr key=\"固定填写DEBT\"></ChrgBr>\r\n" + "      <Dbtr>\r\n"
				+ "        <Nm key=\"付款人名称\" signer=\"true\"></Nm>\r\n" + "        <CtctDtls>\r\n"
				+ "          <EmailAdr key=\"固定填写SIGNERXXX\"></EmailAdr>\r\n" + "        </CtctDtls>\r\n"
				+ "      </Dbtr>\r\n" + "      <DbtrAcct>\r\n" + "        <Id>\r\n" + "          <Othr>\r\n"
				+ "            <Id key=\"付款人账号\" signer=\"true\"></Id>\r\n"
				+ "            <Issr key=\"付款人开户行名称\" signer=\"true\"></Issr>\r\n" + "          </Othr>\r\n"
				+ "        </Id>\r\n" + "        <Tp>\r\n"
				+ "          <Prtry key=\"付款人账户类型\" signer=\"true\"></Prtry>\r\n" + "        </Tp>\r\n"
				+ "      </DbtrAcct>\r\n" + "      <DbtrAgt>\r\n" + "        <FinInstnId>\r\n"
				+ "          <ClrSysMmbId>\r\n" + "            <MmbId key=\"付款清算行行号\" signer=\"true\"></MmbId>\r\n"
				+ "          </ClrSysMmbId>\r\n" + "        </FinInstnId>\r\n" + "        <BrnchId>  \r\n"
				+ "          <Id key=\"付款人开户行所属网银系统行号\" signer=\"true\"></Id>\r\n" + "          <PstlAdr>\r\n"
				+ "            <CtrySubDvsn key=\"付款人开户行所属城市代码\"></CtrySubDvsn>\r\n" + "          </PstlAdr>\r\n"
				+ "        </BrnchId>\r\n" + "      </DbtrAgt>\r\n" + "      <CdtrAgt>\r\n" + "        <FinInstnId>\r\n"
				+ "          <ClrSysMmbId>\r\n" + "            <MmbId key=\"收款清算行行号\" signer=\"true\" ></MmbId>\r\n"
				+ "          </ClrSysMmbId>\r\n" + "        </FinInstnId>\r\n" + "        <BrnchId>\r\n"
				+ "          <Id key=\"收款人开户行所属网银系统行号\" signer=\"true\"></Id>\r\n" + "        </BrnchId>\r\n"
				+ "      </CdtrAgt>\r\n" + "      <Cdtr>\r\n" + "        <Nm key=\"收款人名称\" signer=\"true\"></Nm>\r\n"
				+ "      </Cdtr>\r\n" + "      <CdtrAcct>\r\n" + "        <Id>\r\n" + "          <Othr>\r\n"
				+ "            <Id key=\"收款人账号\" signer=\"true\"></Id>\r\n" + "          </Othr>\r\n"
				+ "        </Id>\r\n" + "      </CdtrAcct>\r\n" + "      <Purp>\r\n"
				+ "        <Prtry key=\"业务种类编码集\" signer=\"true\"></Prtry>\r\n" + "      </Purp>\r\n"
				+ "      <RmtInf>\r\n" + "      	<Ustrd key=\"附言\">附言1</Ustrd>\r\n" + "      </RmtInf>\r\n"
				+ "    </CdtTrfTxInf>\r\n" + "  </FIToFICstmrCdtTrf>\r\n" + "</Document>\r\n" + "";

		Document document;
		String filePath = "E:\\git_source\\develop\\fib\\codes\\fib-boot\\fib-framework\\fib-commons\\src\\main\\resources\\config\\xml\\ibps.101.001.01.xml";
		try {
			Document templateEle = (Document) ModuleCacheManager.getInstance().getModule(filePath);
			document = DocumentHelper.parseText(str);
			String xpath = "/Document/FIToFICstmrCdtTrf/GrpHdr/MsgId";
			String ss = XmlHelper.INSTANCE.getSingleNodeText(xpath, document.getRootElement());
			System.out.println("ss="+ss);
		
			Message msg = new Message();
			XmlHelper.INSTANCE.unpack(msg, document.getRootElement(), templateEle.getRootElement());
			System.out.println(msg);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
