package com.fib.upp.xml.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;

import com.fib.commons.xml.Dom4jUtils;

public class GatewayConfigParser {

	public GatewayConfig parse(String fileName) {
		return (GatewayConfig) CacheUtil.modualCache.get(fileName, k -> load(fileName));
	}

	protected Object load(String fileName) {
		GatewayConfig config = new GatewayConfig();
		Document document = Dom4jUtils.getDocument(fileName);
		Element rootEle = document.getRootElement();
		String id = rootEle.attribute("id").getText();
		String name = rootEle.attribute("name").getText();
		String monitorPort = rootEle.attribute("monitor-port").getText();
		System.out.println("id=" + id + "  name=" + name + "  monitorPort=" + monitorPort);
		config.setId(id);
		config.setName(name);
		config.setMonitorPort(Integer.valueOf(monitorPort));
		String xpath = "/gateway/channels/channel";

		Map<String, String> nameSpaceMap = new HashMap<String, String>();
		nameSpaceMap.put("default", rootEle.getNamespaceURI());
		xpath = xpath.replaceAll("/", "/default:");

		// System.out.println(path);
		XPath xItemName = rootEle.createXPath(xpath);
		xItemName.setNamespaceURIs(nameSpaceMap);
		String returnstr = "";
		List<Node> nodes = xItemName.selectNodes(rootEle);
		ChannelMainConfig channel = null;
		for (Node node : nodes) {
			channel = new ChannelMainConfig();
			Element element = (Element) node;
			System.out.println("returnstr=" + element.attributeValue("id") + "  " + element.attributeValue("name"));
			channel.setId(element.attributeValue("id"));
			channel.setName(element.attributeValue("name"));
			channel.setStartup(!"false".equals(element.attributeValue("startup")));
			channel.setDeploy(element.attributeValue("deploy"));
			config.setChannel(channel.getId(), channel);
		}

		xpath = "/gateway/event-handler";
		xpath = xpath.replaceAll("/", "/default:");
		XPath xItemName1 = rootEle.createXPath(xpath);
		xItemName1.setNamespaceURIs(nameSpaceMap);
		Element element = (Element) xItemName1.selectSingleNode(rootEle);
		if (element != null) {
			returnstr = element.attributeValue("max-handler-number");
			config.setEventHandlerNumber(Integer.valueOf(returnstr));
		}

		xpath = "/gateway/variable-file";
		xpath = xpath.replaceAll("/", "/default:");
		xItemName1 = rootEle.createXPath(xpath);
		xItemName1.setNamespaceURIs(nameSpaceMap);
		element = (Element) xItemName1.selectSingleNode(rootEle);
		if (element != null) {
			returnstr = element.attributeValue("name");
			config.setVariableFileName(returnstr);
		}
		System.out.println("config=" + config);
		return config;
	}
}
