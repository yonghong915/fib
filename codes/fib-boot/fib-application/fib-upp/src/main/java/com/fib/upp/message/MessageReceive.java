package com.fib.upp.message;

import java.io.UnsupportedEncodingException;

public class MessageReceive {
	public static void execute() {
		String receiveMsg = "{H:02103100000026  BEPS313684093748  BEPS20210121090735XMLbeps.121.001.01     BEPE5FB7A35B272764562021012120203150    3DXN       }\r\n"
				+ "{S:MEUCIQCNYuIMGkwHA47f1Yq1+OBPdmKJ3HfxewU6pW97XK2ongIgX8zTNv2Bj3iHmAocJufT04ki3oSSbx//kGzapTlbBN8=}\r\n"
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\r\n"
				+ "<Document xmlns=\"urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
				+ "\r\n" + "  <CstmrCdtTrf>\r\n" + "    <PKGGrpHdr>\r\n" + "      <MsgId>2021012120203150</MsgId>\r\n"
				+ "      <CreDtTm>2021-01-21T09:07:35</CreDtTm>\r\n" + "      <InstgPty>\r\n"
				+ "        <InstgDrctPty>103100000026</InstgDrctPty>\r\n" + "      </InstgPty>\r\n"
				+ "      <InstdPty>\r\n" + "        <InstdDrctPty>313684093748</InstdDrctPty>\r\n"
				+ "      </InstdPty>\r\n" + "      <NbOfTxs>1</NbOfTxs>\r\n"
				+ "      <CtrlSum Ccy=\"CNY\">120000.00</CtrlSum>\r\n" + "      <SysCd>BEPS</SysCd>\r\n"
				+ "      <Rmk>0</Rmk>\r\n" + "    </PKGGrpHdr>\r\n" + "    <NPCPrcInf>\r\n"
				+ "      <PrcSts>PR03</PrcSts>\r\n" + "      <PrcCd>NEZI0000</PrcCd>\r\n"
				+ "      <NetgDt>2021-01-21</NetgDt>\r\n" + "      <NetgRnd>01</NetgRnd>\r\n"
				+ "      <RcvTm>2021-01-21T09:07:35</RcvTm>\r\n" + "      <TrnsmtTm>2021-01-21T09:07:35</TrnsmtTm>\r\n"
				+ "    </NPCPrcInf>\r\n" + "    <CstmrCdtTrfInf>\r\n" + "      <TxId>2021012116842718</TxId>\r\n"
				+ "      <Dbtr>\r\n" + "        <Nm>阿根吾呷</Nm>\r\n" + "      </Dbtr>\r\n" + "      <DbtrAcct>\r\n"
				+ "        <Id>\r\n" + "          <Othr>\r\n" + "            <Id>6228450968027273774</Id>\r\n"
				+ "            <Issr>103684964610</Issr>\r\n" + "          </Othr>\r\n" + "        </Id>\r\n"
				+ "      </DbtrAcct>\r\n" + "      <DbtrAgt>\r\n" + "        <BrnchId>\r\n"
				+ "          <Id>103684964610</Id>\r\n" + "        </BrnchId>\r\n" + "      </DbtrAgt>\r\n"
				+ "      <CdtrAgt>\r\n" + "        <BrnchId>\r\n" + "          <Id>313684001107</Id>\r\n"
				+ "        </BrnchId>\r\n" + "      </CdtrAgt>\r\n" + "      <Cdtr>\r\n"
				+ "        <Nm>西昌勇玖木业有限责任公司</Nm>\r\n" + "      </Cdtr>\r\n" + "      <CdtrAcct>\r\n"
				+ "        <Id>\r\n" + "          <Othr>\r\n" + "            <Id>01100302000003686</Id>\r\n"
				+ "            <Issr>313684001107</Issr>\r\n" + "          </Othr>\r\n" + "        </Id>\r\n"
				+ "      </CdtrAcct>\r\n" + "      <Amt Ccy=\"CNY\">120000.00</Amt>\r\n" + "      <PmtTpInf>\r\n"
				+ "        <CtgyPurp>\r\n" + "          <Prtry>A100</Prtry>\r\n" + "        </CtgyPurp>\r\n"
				+ "      </PmtTpInf>\r\n" + "      <Purp>\r\n" + "        <Prtry>02103</Prtry>\r\n"
				+ "      </Purp>\r\n" + "    </CstmrCdtTrfInf>\r\n" + "  </CstmrCdtTrf>\r\n" + "\r\n" + "</Document>";

		byte[] messageData = null;
		try {
			messageData = receiveMsg.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] des = new byte[3];
		System.arraycopy(messageData, 0, des, 0, des.length);
		String BeginFlag = new String(des);
		System.out.println("BeginFlag=" + BeginFlag);

		des = new byte[2];
		System.arraycopy(messageData, 3, des, 0, des.length);
		String VersionID = new String(des);
		System.out.println("VersionID=" + VersionID);

		des = new byte[14];
		System.arraycopy(messageData, 5, des, 0, des.length);
		String OrigSender = new String(des);
		System.out.println("OrigSender=" + OrigSender);

		des = new byte[4];
		System.arraycopy(messageData, 19, des, 0, des.length);
		String OrigSenderSID = new String(des);
		System.out.println("OrigSenderSID=" + OrigSenderSID);

	}

	public static void main(String[] args) {
		execute();
	}
}
