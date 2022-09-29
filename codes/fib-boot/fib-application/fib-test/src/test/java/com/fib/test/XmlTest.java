package com.fib.test;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.xml.IXmlService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlTest {
	@Autowired
	private IXmlService dom4jService;

	@Test
	public void dod() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
		String xmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<Document xmlns=\"urn:cnaps:std:ccms:2010:tech:xsd:ccms.990.001.02\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
				+ "	<ComConf>\r\n" + "		<ConfInf>\r\n" + "			<OrigSndr>313684093748</OrigSndr>\r\n"
				+ "			<OrigSndDt>20191219</OrigSndDt>\r\n" + "			<MT>hvps.111.001.01</MT>\r\n"
				+ "			<MsgId>00000000000003329478</MsgId>\r\n" + "			<MsgRefId>20191219000024084562</MsgRefId>\r\n"
				+ "			<MsgPrcCd>PM1I0000</MsgPrcCd>\r\n" + "		</ConfInf>\r\n" + "	</ComConf>\r\n" + "</Document>";
		Document doc = dom4jService.getDocument(xmlText);
		System.out.println(doc.asXML());

		String val = dom4jService.getXPathValue(doc, "/default:Document/default:ComConf/default:ConfInf/default:OrigSndr");
		System.out.println("val=" + val);

		List<Node> nodes = dom4jService.selectNodes(doc, "/default:Document/default:ComConf/default:ConfInf/default:OrigSndr");
		for (Node node : nodes) {
			System.out.println(node.getText());
		}

		String schemaURI = "config/xsd/ccms.990.001.02.xsd";
		boolean validFlag = dom4jService.validate(schemaURI);
		System.out.println("validFlag=" + validFlag);
	}
}
