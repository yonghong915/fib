//package com.fib.test;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentFactory;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.fib.autoconfigure.xml.IXmlService;
//import com.fib.autoconfigure.xml.dom4j.Dom4jService;
//
//import cn.hutool.core.io.FileUtil;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class XmlTest {
//	@Autowired
//	private IXmlService dom4jService;
//
//	@Test
//	public void dod() {
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
//		String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
//				+ "<Document xmlns=\"urn:cnaps:std:ccms:2010:tech:xsd:ccms.990.001.02\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
//				+ "	<ComConf>\r\n" + "		<ConfInf>\r\n" + "			<OrigSndr>313684093748</OrigSndr>\r\n"
//				+ "			<OrigSndDt>20191219</OrigSndDt>\r\n" + "			<MT>hvps.111.001.01</MT>\r\n"
//				+ "			<MsgId>00000000000003329478</MsgId>\r\n" + "			<MsgRefId>20191219000024084562</MsgRefId>\r\n"
//				+ "			<MsgPrcCd>PM1I0000</MsgPrcCd>\r\n" + "		</ConfInf>\r\n" + "	</ComConf>\r\n" + "</Document>";
//		Document doc = dom4jService.getDocument(xmlText);
//		System.out.println(doc.asXML());
//
//		String val = dom4jService.getXPathValue(doc, "/default:Document/default:ComConf/default:ConfInf/default:OrigSndr");
//		System.out.println("val=" + val);
//
//		List<Node> nodes = dom4jService.selectNodes(doc, "/default:Document/default:ComConf/default:ConfInf/default:OrigSndr");
//		for (Node node : nodes) {
//			System.out.println(node.getText());
//		}
//
//		String schemaURI = "config/schema/ccms.990.001.02.xsd";
//		boolean validFlag = dom4jService.validate(schemaURI);
//		System.out.println("validFlag=" + validFlag);
//	}
//
//	private static void setXPathNamespaceURIs(String namespaceURI) {
//		Map<String, String> namespaces = new TreeMap<>();
//		namespaces.put("default", namespaceURI);
//		DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
//	}
//
//	public static Document createDocument(String fileName) {
//		Document doc = DocumentHelper.createDocument();
//		doc.setXMLEncoding("UTF-8");
//		File file = new File("test.xml");
//
//		try {
//			Document templateDoc = new SAXReader().read(FileUtil.file("config/xml/template/ccms.990.001.02.xml"));
//			System.out.println("templateDoce=" + templateDoc.asXML());
//			setXPathNamespaceURIs(templateDoc.getRootElement().getNamespaceURI());
//			Node node = templateDoc.selectSingleNode("default:Document/default:ComConf/default:ConfInf/default:OrigSndr");
//			Map<String, String> dataMap = new HashMap<>();
//			dataMap.put("OrigSender", "3234564556567");
//
//			String text = node.getText();
//			System.out.println("text=" + text);
//
//			int startIndex = text.indexOf("${");
//			if (-1 == startIndex) {
//
//			}
//
//			startIndex += 2;
//			int endIndex = text.indexOf("}", startIndex);
//			if (-1 == endIndex) {
//
//			}
//
//			text = text.substring(startIndex, endIndex);
//			System.out.println("text=" + text);
//			String v = dataMap.get(text);
//			node.setText(v);
//			System.out.println("templateDoce=" + templateDoc.asXML());
////			Element rootEle = doc.addElement(templateDoc.getRootElement().getName(), templateDoc.getRootElement().getNamespace().getURI());
////			System.out.println("templateDoc=" + templateDoc.asXML());
//		} catch (DocumentException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
////
////		OutputFormat format = OutputFormat.createPrettyPrint();
////		XMLWriter writer;
////		try {
////			writer = new XMLWriter(new FileOutputStream(file), format);
////			writer.write(doc);
////			writer.close();
////		} catch (IOException e) {
////			throw new CommonException("Failed to create document by dom4j.", e);
////		}
//		return doc;
//	}
//
//	public static void main(String[] args) {
//		createDocument("");
//	}
//}
