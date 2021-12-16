package com.fib.upp.converter.xml.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.config.parser.ConfigurationManager;
import com.fib.commons.exception.CommonException;
import com.fib.commons.util.CommUtils;
import com.fib.commons.util.I18nUtils;
import com.fib.commons.xml.Dom4jUtils;
import com.fib.upp.converter.xml.config.base.DynamicObjectConfig;
import com.fib.upp.util.Constant;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-12-02
 */
public class GatewayConfigParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfigParser.class);
	Document document = null;
	private String fileName;
	private GatewayConfig config = null;
	private ChannelMainConfig channel = null;
	private DynamicObjectConfig currParaOwner;

	public GatewayConfig parse(String fileName) {
		LOGGER.info("fileName={}", fileName);
		if (CharSequenceUtil.isEmpty(fileName)) {
			throw new CommonException("fileName must be not empty.");
		}
		this.fileName = fileName;
		document = Dom4jUtils.createDocument(fileName);

		/**/
		buildGateway();

		/**/
		buildModule();

		/**/
		buildChannel();

		/**/
		buildEventHandler();

		/**/
		buildVariableFile();

		/**/
		configCheck();
		return config;
	}

	private void configCheck() {
		String gatewayId = fileName.substring(fileName.indexOf("gateway_") + "gateway_".length(),
				fileName.indexOf(".xml"));
		if (!gatewayId.equals(config.getId())) {
			throw new CommonException(I18nUtils.getMessage("CommGateway.loadConfig.configFileName.notEqual.id",
					config.getId(), fileName));
		}

		if (CharSequenceUtil.isNotEmpty(config.getVariableFileName())) {
			ConfigurationManager.getInstance().getConfiguration(config.getVariableFileName());
		}

		config.setMonitorPort(Integer.parseInt(CommUtils.getRealValue("uppcfg", config.getMonitorPortString())));
	}

	private void buildVariableFile() {
		Element node = (Element) document.selectSingleNode("gateway/variable-file");
		String attrValue = node.attributeValue("name");
		if (CharSequenceUtil.isNotEmpty(attrValue)) {
			config.setVariableFileName(attrValue);
		}
	}

	private void buildEventHandler() {
		Element node = (Element) document.selectSingleNode("gateway/event-handler");
		String attrValue = node.attributeValue("max-handler-number");
		CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
				"gateway/event-handler/@max-handler-number");
		config.setEventHandlerNumber(Integer.parseInt(attrValue));
	}

	private void buildChannel() {
		List<Node> nodes = document.selectNodes("gateway/channels/channel");
		Iterator<Node> iter = nodes.iterator();
		String attrValue = "";
		while (iter.hasNext()) {
			channel = new ChannelMainConfig();
			Element element = (Element) iter.next();
			attrValue = element.attributeValue("id");
			CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
					"gateway/channels/channel/@id");
			if (config.getChannels().containsKey(attrValue)) {
				throw new CommonException(
						I18nUtils.getMessage("reduplicate", fileName, "gateway/channels/channel/@id"));
			}
			channel.setId(attrValue);

			attrValue = element.attributeValue("name");
			CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
					"gateway/channels/channel/@name");
			channel.setName(attrValue);

			attrValue = element.attributeValue("startup");
			CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
					"gateway/channels/channel/@startup");
			channel.setStartup(!"false".equalsIgnoreCase(attrValue));

			attrValue = element.attributeValue("deploy");
			CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
					"gateway/channels/channel/@deploy");
			channel.setDeploy(attrValue);

			config.setChannel(channel.getId(), channel);
		}
	}

	private void buildModule() {
		List<Node> nodes = document.selectNodes("gateway/modules/module");
		Element ele = null;
		String attrValue = "";
		ModuleConfig module = new ModuleConfig();
		module.setParameters(new HashMap<>());
		currParaOwner = module;
		config.getModules().add(module);
		for (Node node : nodes) {
			ele = (Element) node;
			attrValue = ele.attributeValue("type");
			if (CharSequenceUtil.isNotEmpty(attrValue)) {
				module.setType(attrValue.trim());
			} else {
				attrValue = ele.attributeValue("class");
				CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
						"gateway/modules/module/@class");
				module.setClassName(attrValue);
			}

			List<Node> subNodes = document.selectNodes("gateway/modules/module/parameter");
			for (Node subNode : subNodes) {
				ele = (Element) subNode;
				attrValue = ele.attributeValue("name");
				CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
						"gateway/modules/module/parameter/@name");
				String paramVal = ele.getTextTrim();
				CommUtils.notEmpty(paramVal, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName,
						"gateway/modules/module/parameter/text()");
				currParaOwner.getParameters().put(attrValue, paramVal);
			}
		}
	}

	private void buildGateway() {
		config = new GatewayConfig();
		Node node = document.selectSingleNode("gateway");
		Element rooeEle = (Element) node;

		/* /gateway/@id */
		String attrValue = rooeEle.attributeValue("id");
		CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName, "gateway/@id");
		config.setId(attrValue);

		/* /gateway/@name */
		attrValue = rooeEle.attributeValue("name");
		CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName, "gateway/@name");
		config.setName(attrValue);

		/* /gateway/@monitor-port */
		attrValue = rooeEle.attributeValue("monitor-port");
		CommUtils.notEmpty(attrValue, Constant.MessageSourceKey.CONFIG_EMPTY_KEY, fileName, "gateway/@monitor-port");
		config.setMonitorPortString(attrValue);
	}
}