package com.fib.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

import com.fib.commons.exception.CommonException;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-06
 */
public class Dom4jUtils {
	private static final String DEFAULT_NAMESPACE = "http://www.fib.com/schema";
	private static final String DEFAULT_SAX_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
	private static final String DEFAULT_NAMESPACE_NAME = "default";
	private static final String EXPRESSION_WITH_NS = "/" + DEFAULT_NAMESPACE_NAME + ":";

	private Dom4jUtils() {
	}

	public static Document createDocument(InputStream is) {
		Document doc = null;
		SAXReader xmlReader = new SAXReader();
		try {
			xmlReader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = xmlReader.read(is);
		} catch (SAXException | DocumentException e1) {
			throw new CommonException("Failed to create xml document.");
		}
		Element rootElement = doc.getRootElement();
		String namespace = rootElement.getNamespaceURI();

		if (namespace != null) {
			Map<String, String> namespaces = new TreeMap<>();
			namespaces.put(DEFAULT_NAMESPACE_NAME, namespace);
			DocumentFactory.getInstance().setXPathNamespaceURIs(namespaces);
		}
		return doc;
	}

	public static List<Node> selectNodes(Document doc, String xpathExpression) {
		Element rootElement = doc.getRootElement();
		String namespace = rootElement.getNamespaceURI();
		if (namespace != null) {
			xpathExpression = xpathExpression.replace("/", EXPRESSION_WITH_NS);
		}
		return doc.selectNodes(xpathExpression);
	}

	public static Document getDocument(String filePath) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(filePath);
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static Document getDocument(InputStream is) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			reader.setFeature(DEFAULT_SAX_FEATURE, true);
			doc = reader.read(is);
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static Document getXPathDocument(InputStream is, String... namespaces)
			throws DocumentException, SAXException {
		Map<String, String> map = new HashMap<>();
		map.put("ns", (ArrayUtil.isEmpty(namespaces)) ? DEFAULT_NAMESPACE : namespaces[0]);
		DocumentFactory factory = new DocumentFactory();
		factory.setXPathNamespaceURIs(map);
		SAXReader reader = new SAXReader(factory);
		reader.setFeature(DEFAULT_SAX_FEATURE, true);
		return reader.read(is);
	}

	public static Document getXPathDocument(String xml, String... namespaces) throws DocumentException, SAXException {
		Map<String, String> map = new HashMap<>();
		map.put("ns", (ArrayUtil.isEmpty(namespaces)) ? DEFAULT_NAMESPACE : namespaces[0]);
		DocumentFactory factory = new DocumentFactory();
		factory.setXPathNamespaceURIs(map);
		SAXReader reader = new SAXReader(factory);
		reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		try {
			return reader.read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException | DocumentException e) {
			throw new CommonException("Failed to read xml.", e);
		}
	}

	public static XPath getXPath(Element rootEle, String xpath) {
		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		nameSpaceMap.put(DEFAULT_NAMESPACE_NAME, rootEle.getNamespaceURI());
		xpath = xpath.replaceAll("/", "/" + DEFAULT_NAMESPACE_NAME + ":");
		XPath xItemName = rootEle.createXPath(xpath);
		xItemName.setNamespaceURIs(nameSpaceMap);
		return xItemName;
	}

	public static XPath getXPath(String xpathExpression, String... namespaces) {
		Map<String, String> nameSpaceMap = new HashMap<>();

		String namespaceKey = "";
		String namespace = "";
		if (ArrayUtil.isEmpty(namespaces)) {
			namespaceKey = DEFAULT_NAMESPACE_NAME;
			namespace = DEFAULT_NAMESPACE;
		} else if (namespaces.length == 1) {
			namespaceKey = DEFAULT_NAMESPACE_NAME;
			namespace = namespaces[0];
		} else {
			namespace = namespaces[0];
			namespaceKey = namespaces[1];
		}
		nameSpaceMap.put(namespaceKey, namespace);
		DocumentFactory.getInstance().setXPathNamespaceURIs(nameSpaceMap);

		xpathExpression = xpathExpression.replaceAll("/", "/" + DEFAULT_NAMESPACE_NAME + ":");
		return DocumentFactory.getInstance().createXPath(xpathExpression);
	}

	public static String getXPathValue(Document doc, String xpath) {
		Node node = doc.selectSingleNode(xpath);
		if (Objects.isNull(node)) {
			return StrUtil.EMPTY;
		}
		return node.getText();
	}

	public static String getXPathValue(Node node, String xpath) {
		Node childNode = node.selectSingleNode(xpath);
		if (Objects.isNull(childNode)) {
			return StrUtil.EMPTY;
		}
		return childNode.getText();
	}

	public static Node getNode(Document doc, String xpath) {
		return doc.selectSingleNode(xpath);
	}

	public static Node getNode(Node node, String xpath) {
		return node.selectSingleNode(xpath);
	}

	public static List<Node> getNodes(Document doc, String xpath) {
		return doc.selectNodes(xpath);
	}

	public static List<Node> getNodes(Node node, String xpath) {
		return node.selectNodes(xpath);
	}

	public static Object getObject(Document doc, String xpath) {
		return doc.selectObject(xpath);
	}

	public static Object getObject(Node node, String xpath) {
		return node.selectObject(xpath);
	}

	public static int numberValueOf(Document doc, String xpath) {
		Number number = doc.numberValueOf(xpath);
		try {
			return number.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public static int numberValueOf(Node node, String xpath) {
		Number number = node.numberValueOf(xpath);
		try {
			return number.intValue();
		} catch (Exception e) {
			return 0;
		}
	}

	public void check() {
//		// 创建默认的XML错误处理器
//		XMLErrorHandler errorHandler = new XMLErrorHandler();
//		// 获取基于 SAX 的解析器的实例
//		SAXParserFactory factory = SAXParserFactory.newInstance();
//		// 解析器在解析时验证 XML 内容。
//		factory.setValidating(true);
//		// 指定由此代码生成的解析器将提供对 XML 名称空间的支持。
//		factory.setNamespaceAware(true);
//		// 使用当前配置的工厂参数创建 SAXParser 的一个新实例。
//		try {
//			SAXParser parser = factory.newSAXParser();
//			// 创建一个读取工具
//			SAXReader xmlReader = new SAXReader();
//			// 获取要校验xml文档实例
//			Document xmlDocument = (Document) xmlReader.read(new File(xmlFileName));
//			// 设置 XMLReader 的基础实现中的特定属性。核心功能和属性列表可以在
//			// [url]http://sax.sourceforge.net/?selected=get-set[/url] 中找到。
//			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
//					"http://www.w3.org/2001/XMLSchema");
//			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", "file:" + xsdFileName);
//			// 创建一个SAXValidator校验工具，并设置校验工具的属性
//			SAXValidator validator = new SAXValidator(parser.getXMLReader());
//			// 设置校验工具的错误处理器，当发生错误时，可以从处理器对象中得到错误信息。
//			validator.setErrorHandler(errorHandler);
//			// 校验
//			validator.validate(xmlDocument);
//
////            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint()); 
//			// 如果错误信息不为空，说明校验失败，打印错误信息
//
//			if (errorHandler.getErrors().hasContent()) {
//				Element e = errorHandler.getErrors();
//				List<Node> list = e.content();
//				int count = 0;
//				String result = "";
//				for (Node node : list) {
//					String error = node.getText().split(":")[1];
//					if (error.contains("元素") && error.contains("无效")) {
//						count++;
//						result += error + " ";
//					}
//				}
//				System.err.println("共有" + count + "处错误");
//				System.err.println(result);
//			} else {
//				System.err.println("Good! XML文件通过XSD文件校验成功！");
//			}
//		} catch (ParserConfigurationException | SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public static void main(String[] args) {

		String msg = "<Document xmlns=\"urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n"
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
				+ "          <AdrLine>abcdegg</AdrLine>\r\n" + "        </PstlAdr>\r\n" + "      </Dbtr>\r\n"
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

		String xpath = "ns:Document/ns:CstmrCdtTrf/ns:CstmrCdtTrfInf/ns:Dbtr/ns:PstlAdr/ns:AdrLine/text()";
		try {
			Document doc = getXPathDocument(msg, "urn:cnaps:std:beps:2010:tech:xsd:beps.121.001.01");
			String rt = getXPathValue(doc, xpath);
			System.out.println(rt);

			Map<String, String> nameSpaceMap = new HashMap<String, String>();
			Document document = DocumentHelper.parseText(msg);
			nameSpaceMap.put(DEFAULT_NAMESPACE_NAME, document.getRootElement().getNamespaceURI());
			String path = "/Document/CstmrCdtTrf/CstmrCdtTrfInf/Dbtr/PstlAdr/AdrLine";
			path = path.replaceAll("/", "/" + DEFAULT_NAMESPACE_NAME + ":");
			System.out.println(path);
			XPath xItemName = document.getRootElement().createXPath(path);
			xItemName.setNamespaceURIs(nameSpaceMap);
			String returnstr = "";
			Node element = xItemName.selectSingleNode(document.getRootElement());
			if (element != null) {
				returnstr = element.getText();
			}

			System.out.println("returnstr=" + returnstr);

		} catch (DocumentException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
