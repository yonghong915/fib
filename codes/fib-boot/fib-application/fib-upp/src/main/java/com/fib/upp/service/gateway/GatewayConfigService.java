package com.fib.upp.service.gateway;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fib.commons.xml.Dom4jUtils;
import com.fib.upp.util.configuration.jconfig.Configuration;
import com.fib.upp.xml.config.ChannelMainConfig;
import com.fib.upp.xml.config.GatewayConfig;
import com.fib.upp.xml.config.ModuleConfig;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

@Service
@CacheConfig(cacheNames = "caffeineCacheManager")
public class GatewayConfigService implements IConfig {
	
	@Autowired
	IConfig fibConfiguration;


	@Cacheable(key = "#fileName")
	@Override
	public Object load(String fileName, Object... params) {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		GatewayConfig config = new GatewayConfig();
		Document document = Dom4jUtils.getDocument(in);
		Element rootEle = document.getRootElement();
		String id = rootEle.attribute("id").getText();
		String name = rootEle.attribute("name").getText();
		String monitorPort = rootEle.attribute("monitor-port").getText();
		
		String filePath = "config" + FileUtil.FILE_SEPARATOR + "syspara";
		Object retObj1 = fibConfiguration.load(filePath);
		Configuration fibconfig = (Configuration) retObj1;
		
		String key = "";
		if (StrUtil.isWrap(monitorPort, "${", "}")) {
			key = StrUtil.sub(monitorPort, 2, monitorPort.length() - 1);
		}
		monitorPort = fibconfig.getVariable("gatewayConfig", key);
		
		config.setId(id);
		config.setName(name);
		config.setMonitorPort(Integer.valueOf(monitorPort));

		// modules
		String xpath = "/gateway/modules/module";
		XPath xItemName = Dom4jUtils.getXPath(rootEle, xpath);
		List<Node> nodes = xItemName.selectNodes(rootEle);
		ModuleConfig module = new ModuleConfig();
		String attrValue = "";
		for (Node node : nodes) {
			module = new ModuleConfig();
			Element element = (Element) node;
			attrValue = element.attributeValue("type");
			module.setType(attrValue);

			attrValue = element.attributeValue("class");
			module.setClassName(attrValue);
			config.getModules().add(module);
		}

		// channel
		xpath = "/gateway/channels/channel";
		xItemName = Dom4jUtils.getXPath(rootEle, xpath);

		nodes = xItemName.selectNodes(rootEle);
		ChannelMainConfig channel = null;
		for (Node node : nodes) {
			channel = new ChannelMainConfig();
			Element element = (Element) node;
			channel.setId(element.attributeValue("id"));
			channel.setName(element.attributeValue("name"));
			channel.setStartup(!"false".equals(element.attributeValue("startup")));
			channel.setDeploy(element.attributeValue("deploy"));
			config.setChannel(channel.getId(), channel);
		}

		// event-handler
		xpath = "/gateway/event-handler";
		xItemName = Dom4jUtils.getXPath(rootEle, xpath);
		Element element = (Element) xItemName.selectSingleNode(rootEle);
		if (element != null) {
			attrValue = element.attributeValue("max-handler-number");
			config.setEventHandlerNumber(Integer.valueOf(attrValue));
		}

		// variable-file
		xpath = "/gateway/variable-file";
		xItemName = Dom4jUtils.getXPath(rootEle, xpath);
		element = (Element) xItemName.selectSingleNode(rootEle);
		if (element != null) {
			attrValue = element.attributeValue("name");
			config.setVariableFileName(attrValue);
		}
		return config;
	}
	
    public static void main(String[] args) {
		String orgiSource = "${cnaps2_monitor-port}";
		
	}
}