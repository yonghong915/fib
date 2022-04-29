//package com.fib.upp;
//
//import java.io.ByteArrayInputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//
//import org.dom4j.Document;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import com.fib.commons.xml.Dom4jUtils;
//import com.fib.upp.message.metadata.Field;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.HexUtil;
//
//public class Dom4jCnapsTest {
//	@Test
//	@Ignore("test")
//	public void test() {
//		String fileName = "data/cnaps/ccms.801.001.02.txt";
//		String xmlStr = FileUtil.readString(fileName, StandardCharsets.UTF_8);
//		Map<String, Object> msgHeader = MapUtil.newHashMap();
//		MsgHeader mh = new MsgHeader();
//
//		Field beginFlag = new Field();
//		beginFlag.setName("beginFlag");
//		beginFlag.setLength(3);
//		mh.add(beginFlag);
//
//		Field versionID = new Field();
//		versionID.setName("versionID");
//		versionID.setLength(2);
//		mh.add(versionID);
//
//		Field origSender = new Field();
//		origSender.setName("origSender");
//		origSender.setLength(14);
//		mh.add(origSender);
//
//		Field origSenderSID = new Field();
//		origSenderSID.setName("origSenderSID");
//		origSenderSID.setLength(4);
//		mh.add(origSenderSID);
//
//		Field origReceiver = new Field();
//		origReceiver.setName("origReceiver");
//		origReceiver.setLength(14);
//		mh.add(origReceiver);
//
//		Field origReceiverSID = new Field();
//		origReceiverSID.setName("origReceiverSID");
//		origReceiverSID.setLength(4);
//		mh.add(origReceiverSID);
//
//		Field origSendDate = new Field();
//		origSendDate.setName("origSendDate");
//		origSendDate.setLength(8);
//		mh.add(origSendDate);
//
//		Field origSendTime = new Field();
//		origSendTime.setName("origSendTime");
//		origSendTime.setLength(6);
//		mh.add(origSendTime);
//
//		Field structType = new Field();
//		structType.setName("structType");
//		structType.setLength(3);
//		mh.add(structType);
//
//		Field mesgType = new Field();
//		mesgType.setName("mesgType");
//		mesgType.setLength(20);
//		mh.add(mesgType);
//
//		Field mesgID = new Field();
//		mesgID.setName("mesgID");
//		mesgID.setLength(20);
//		mh.add(mesgID);
//
//		Field mesgRefID = new Field();
//		mesgRefID.setName("mesgRefID");
//		mesgRefID.setLength(20);
//		mh.add(mesgRefID);
//
//		Field mesgPriority = new Field();
//		mesgPriority.setName("mesgPriority");
//		mesgPriority.setLength(1);
//		mh.add(mesgPriority);
//
//		Field mesgDirection = new Field();
//		mesgDirection.setName("mesgDirection");
//		mesgDirection.setLength(1);
//		mh.add(mesgDirection);
//
//		Field reserve = new Field();
//		reserve.setName("reserve");
//		reserve.setLength(9);
//		mh.add(reserve);
//
//		Field endFlag = new Field();
//		endFlag.setName("endFlag");
//		endFlag.setLength(3);
//		mh.add(endFlag);
//
//		List<Field> fieldList = mh.getFields();
//		int startIdx = 0;
//		int endIdx = 0;
//		int fieldLen = fieldList.size();
//		Field field = null;
//		int nextIdx = 0;
//		for (int i = 0; i < fieldLen; i++) {
//			field = fieldList.get(i);
//			startIdx = endIdx;
//			endIdx = endIdx + field.getLength();
//			msgHeader.put(field.getName(), xmlStr.substring(startIdx, endIdx));
//
//			String aa = xmlStr.substring(startIdx, endIdx);
//			String hex = HexUtil.encodeHexStr(aa);
//			if ("7d0d0a".equals(hex)) {
//				nextIdx = endIdx;
//			}
//		}
//		System.out.println("MsgHeader=" + msgHeader);
//		System.out.println(xmlStr.substring(nextIdx));
//
//		String xml = xmlStr.substring(nextIdx);
//		parseMsgBody(xml);
//
//	}
//
//	private void parseMsgBody(String xml) {
//		Document doc = Dom4jUtils.createDocument(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
//
//		MsgBody mb = new MsgBody();
//
//		List<Field> fieldList = mb.getFields();
//		Field origSystemDate = new Field();
//		origSystemDate.setName("origSystemDate");
//		origSystemDate.setLength(1);
//		origSystemDate.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:OrgnlSysDt");
//		mb.add(origSystemDate);
//
//		Field origSystemStatus = new Field();
//		origSystemStatus.setName("origSystemStatus");
//		origSystemStatus.setLength(1);
//		origSystemStatus.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:OrgnlSysSts");
//		mb.add(origSystemStatus);
//
//		Field currentSystemDate = new Field();
//		currentSystemDate.setName("currentSystemDate");
//		currentSystemDate.setLength(1);
//		currentSystemDate.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:CurSysDt");
//		mb.add(currentSystemDate);
//
//		Field currentSystemStatus = new Field();
//		currentSystemStatus.setName("currentSystemStatus");
//		currentSystemStatus.setLength(1);
//		currentSystemStatus.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:CurSysSts");
//		mb.add(currentSystemStatus);
//
//		Field holidayFlag = new Field();
//		holidayFlag.setName("holidayFlag");
//		holidayFlag.setLength(1);
//		holidayFlag.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:HldayFlg");
//		mb.add(holidayFlag);
//
//		Field nextSystemDate = new Field();
//		nextSystemDate.setName("nextSystemDate");
//		nextSystemDate.setLength(1);
//		nextSystemDate.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:NxtSysDt");
//		mb.add(nextSystemDate);
//
//		Field bankChangeNumber = new Field();
//		bankChangeNumber.setName("bankChangeNumber");
//		bankChangeNumber.setLength(1);
//		bankChangeNumber.setXpath("default:Document/default:SysStsNtfctn/default:SysStsInf/default:BkChngNb");
//		mb.add(bankChangeNumber);
//
//		int fieldLen = fieldList.size();
//		Field field = null;
//		String xpath = null;
//		Map<String, Object> msgBody = MapUtil.newHashMap();
//		for (int i = 0; i < fieldLen; i++) {
//			field = fieldList.get(i);
//			xpath = field.getXpath();
//			String value = Dom4jUtils.getXPathValue(doc, xpath);
//			msgBody.put(field.getName(), value);
//		}
//		System.out.println("msgBody=" + msgBody);
//	}
//}