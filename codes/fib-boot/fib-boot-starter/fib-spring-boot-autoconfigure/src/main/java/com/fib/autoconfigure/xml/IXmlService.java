package com.fib.autoconfigure.xml;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

public interface IXmlService {
	Document getDocument(InputStream in);

	Document getDocument(String xmlText);

	List<Node> selectNodes(Node node, String xpathExpression);

	String getXPathValue(Node node, String xpathExpression);

	boolean validate(String schemaURI);
}
