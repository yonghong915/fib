package com.fib.autoconfigure.msgconverter.channel.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.fib.autoconfigure.msgconverter.channel.config.processor.ErrorMappingConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.MessageHandlerConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.MessageTransformerConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.Processor;
import com.fib.autoconfigure.msgconverter.channel.config.processor.event.ActionConfig;
import com.fib.autoconfigure.msgconverter.channel.config.recognizer.RecognizerConfig;
import com.fib.autoconfigure.msgconverter.channel.config.route.Determination;
import com.fib.autoconfigure.msgconverter.channel.config.route.RouteRule;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB.ChannelMode;
import com.fib.autoconfigure.msgconverter.message.metadata.ConstantMB.RouteRuleType;
import com.fib.autoconfigure.msgconverter.util.EnumConstants;
import com.fib.autoconfigure.msgconverter.util.EnumConstants.MessageObjectType;
import com.fib.commons.exception.CommonException;
import com.fib.commons.util.CommUtils;
import com.fib.commons.xml.Dom4jUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;

public class ChannelConfigParser {
	private ChannelConfig config;
	private Document doc;

	public ChannelConfig parse(String fileName) {
		InputStream in = FileUtil.getInputStream(fileName);
		return parse(in);
	}

	public ChannelConfig parse(InputStream in) {
		config = new ChannelConfig();
		this.doc = Dom4jUtils.createDocument(in);

		parseAll();

		// 3. 检查配置合法性、关联合法性等
		checkAll();
		return config;
	}

	private void parseAll() {
		String attrVal = null;
		String xpathExpression = null;
		// 1. <channel>
		// @type | @class
		xpathExpression = "/channel/@type";
		attrVal = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
		if (CharSequenceUtil.isNotBlank(attrVal)) {
			config.setType(attrVal);
		} else {
			xpathExpression = "/channel/@class";
			attrVal = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
			Assert.notBlank(attrVal, () -> new CommonException("channel/@class must not be null."));
			config.setClassName(attrVal);
		}

		// @mode
		xpathExpression = "/channel/@mode";
		attrVal = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
		Assert.notBlank(attrVal, () -> new CommonException("channel/@mode must not be null."));
		config.setMode(ConstantMB.ChannelMode.getChannelModeByName(CommUtils.getRealValue("uppcfg", attrVal)));

		// description
		xpathExpression = "/channel/description";
		attrVal = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
		config.setDescription(attrVal);

		// content-charset
		xpathExpression = "/channel/content-charset";
		attrVal = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
		config.setCharset(attrVal);

		// 2. <connector>
		parseConnector();

		// 3. <message-bean>
		parseMessageBean();

		// 3. <event-handler>
		parseEventHandler();

		// 5. <message-type-recognizer>
		parseMessageTypeRecognizer();

		// 6. <return-code-recognizer>
		parseReturnCodeRecognizer();

		// 7. <recognizer-table>
		parseRecognizerTable();

		// 8. <processor-table>
		parseProcessorTable();

		// 9. <route-table>
		parseRouteTable();

		// 10. <channel-symbol-table>
		parseChannelSymbolTable();

	}

	private void parseChannelSymbolTable() {
		if (ChannelMode.CLIENT == config.getMode()) {
			return;
		}

		String expression = "channel/channel-symbol-table/channel-symbol";
		List<Node> nodeList = Dom4jUtils.selectNodes(doc, expression);
		ChannelSymbol channelSymbol = null;

		for (Node node : nodeList) {
			channelSymbol = parseChannelSymbol(node);
			if (config.getChannelSymbolTable().containsKey(channelSymbol.getSymbol())) {
				throw new CommonException("reduplicate channel-symbol[" + channelSymbol.getSymbol());
			}
			config.getChannelSymbolTable().put(channelSymbol.getSymbol(), channelSymbol);
		}
	}

	private ChannelSymbol parseChannelSymbol(Node node) {
		String value = null;
		String expression = null;
		ChannelSymbol channelSymbol = new ChannelSymbol();

		// @symbol
		expression = "@symbol";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isEmptyIfStr(value), () -> new CommonException("channel/processor-table/processor/@id muse not be null"));
		channelSymbol.setSymbol(CommUtils.getRealValue("uppcfg", value));

		// @channel-id
		expression = "@channel-id";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isEmptyIfStr(value), () -> new CommonException("channel/processor-table/processor/@id muse not be null"));
		channelSymbol.setChannlId(CommUtils.getRealValue("uppcfg", value));

		// @name
		expression = "@name";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			channelSymbol.setName(CommUtils.getRealValue("uppcfg", value));
		}

		return channelSymbol;
	}

	private void parseRouteTable() {
		if (ChannelMode.CLIENT == config.getMode()) {
			return;
		}
		String expression = "channel/route-table/route-rule";
		List<Node> nodeList = Dom4jUtils.selectNodes(doc, expression);

		RouteRule routeRule = null;
		for (Node node : nodeList) {
			routeRule = parseRouteRule(node);
			if (config.getRouteTable().containsKey(routeRule.getId())) {
				throw new CommonException("reduplicate route-rule[" + routeRule.getId());
			}
			config.getRouteTable().put(routeRule.getId(), routeRule);
		}
	}

	private RouteRule parseRouteRule(Node node) {
		String value = null;
		String expression = null;
		RouteRule routeRule = new RouteRule();

		// @id
		expression = "@id";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isEmptyIfStr(value), () -> new CommonException("channel/processor-table/processor/@id muse not be null"));
		routeRule.setId(CommUtils.getRealValue("uppcfg", value));

		// @type
		expression = "@type";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isEmptyIfStr(value), () -> new CommonException("channel/processor-table/processor/@id muse not be null"));
		routeRule.setType(RouteRuleType.getRouteRuleTypeByName(CommUtils.getRealValue("uppcfg", value)));

		// @dest-channel-symbol
		expression = "@dest-channel-symbol";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			routeRule.setDestinationChannelSymbol(CommUtils.getRealValue("uppcfg", value));
		}

		// @dest-channel-symbol
		expression = "expression";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			routeRule.setExpression(CommUtils.getRealValue("uppcfg", value));
		}

		// <determination>
		expression = "determination";
		Node subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			routeRule.setDeterminationTable(parseDetermination(subNode));
		}

		// <determination>/<default>
		expression = "determination/default";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			routeRule.setDefaultDetermination(parseCase(subNode));
		}
		return routeRule;
	}

	private Map<String, Determination> parseDetermination(Node node) {
		String expression = null;

		// <case>
		expression = "case";
		List<Node> nodeLst = Dom4jUtils.selectNodes(expression, node);
		Assert.isFalse(CollUtil.isEmpty(nodeLst), () -> new CommonException("count.zero"));

		Map<String, Determination> determinationTable = new HashMap<>();
		Determination determination = null;
		for (Node subNode : nodeLst) {
			determination = parseCase(subNode);
			if (determinationTable.containsKey(determination.getResult())) {
				throw new CommonException("reduplicate");
			}
			determinationTable.put(determination.getResult(), determination);
		}
		return determinationTable;
	}

	private Determination parseCase(Node node) {
		String value = null;
		String expression = null;
		Determination determination = new Determination();
		// @result
		expression = "@result";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isBlankIfStr(value), () -> new CommonException("null"));
		determination.setResult(CommUtils.getRealValue("uppcfg", value));

		// @dest-channel-symbol
		expression = "@dest-channel-symbol";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isBlankIfStr(value), () -> new CommonException("null"));
		determination.setDestinationChannelSymbol(CommUtils.getRealValue("uppcfg", value));

		expression = "processor-override";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (StrUtil.isEmptyIfStr(value)) {
			value = CommUtils.getRealValue("uppcfg", value);
			Assert.isFalse(!config.getProcessorTable().containsKey(value),
					() -> new CommonException("ChannelConfigParser.parseCase.processor.isNotInTable"));

			determination.setProcessorOverride(config.getProcessorTable().get(value));
		}
		return determination;
	}

	private void parseProcessorTable() {
		if (ConstantMB.ChannelMode.CLIENT == config.getMode()) {
			return;
		}
		String expression = "/channel/processor-table/processor";
		List<Node> nodeLst = Dom4jUtils.selectNodes(doc, expression);
		Assert.isFalse(CollUtil.isEmpty(nodeLst), () -> new CommonException("channel/processor-table/processor muse not be null"));

		Processor processor = null;
		for (Node node : nodeLst) {
			processor = parseProcessor(node);
			if (config.getProcessorTable().containsKey(processor.getId())) {
				throw new CommonException("reduplicate");
			}
			config.getProcessorTable().put(processor.getId(), processor);
		}
	}

	private Processor parseProcessor(Node node) {
		Processor processor = new Processor();
		String expression = "@id";
		String value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isEmptyIfStr(value), () -> new CommonException("channel/processor-table/processor/@id muse not be null"));
		processor.setId(CommUtils.getRealValue("uppcfg", value));

		expression = "@type";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setType(EnumConstants.ProcessorType.getProcessorTypeByName(CommUtils.getRealValue("uppcfg", value)));
		}

		expression = "@route-rule";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setRouteRuleId(CommUtils.getRealValue("uppcfg", value));
		}

		expression = "@source-async";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setSourceAsync("true".equals(value));
		}

		expression = "@dest-async";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setDestAsync("true".equals(value));
		}

		expression = "@timeout";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setTimeout(Integer.parseInt(CommUtils.getRealValue("uppcfg", value)));
		}

		expression = "@source-channel-message-object-type";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setSourceChannelMessageObjectType(MessageObjectType.getMessageObjectTypeByCode(CommUtils.getRealValue("uppcfg", value)));
		}

		if (MessageObjectType.MAP == processor.getSourceChannelMessageObjectType()) {
			expression = "@source-map-charset";
			value = Dom4jUtils.getSingleNodeText(expression, node);

			if (CharSequenceUtil.isNotBlank(value)) {
				processor.setSourceMapCharset(CommUtils.getRealValue("uppcfg", value));
			}
		}

		expression = "@dest-channel-message-object-type";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setDestChannelMessageObjectType(MessageObjectType.getMessageObjectTypeByCode(CommUtils.getRealValue("uppcfg", value)));
		}

		expression = "@dest-map-charset";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setDestMapCharset(CommUtils.getRealValue("uppcfg", value));
		}

		expression = "@local-source";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			processor.setLocalSource("true".equals(value));
		}

		expression = "@is-default";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			if ("true".equals(value)) {
				if (null != config.getDefaultProcessor()) {
					throw new CommonException("ChannelConfigParser.processor.defaultProcessor.isExist");
				}
				config.setDefaultProcessor(processor);
			}
		}

		expression = "request-message-transformer";
		Node subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setRequestMessageConfig(parseMessageConfig(subNode));
		}

		expression = "request-message-handler";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setRequestMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		expression = "response-message-transformer";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setResponseMessageConfig(parseMessageConfig(subNode));
		}

		expression = "error-message-transformer";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setErrorMessageConfig(parseMessageConfig(subNode));
		}

		expression = "error-message-handler";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setErrorMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		expression = "error-mapping";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setErrorMappingConfig(parseErrorMappingConfig(processor, subNode));
		}

		expression = "response-message-handler";
		subNode = Dom4jUtils.getSingleNode(expression, node);
		if (subNode != null) {
			processor.setResponseMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		expression = "event";
		List<Node> subNodes = Dom4jUtils.selectNodes(expression, node);
		for (Node subN : subNodes) {
			parseEvent(subN, processor);
		}

		return processor;
	}

	private MessageTransformerConfig parseMessageConfig(Node node) {
		String value = null;
		String expression = null;
		MessageTransformerConfig messageConfig = new MessageTransformerConfig();

		// @source-message-id
		expression = "@source-message-id";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			messageConfig.setSourceMessageId(CommUtils.getRealValue("uppcfg", value));
		}

		// @bean-mapping
		expression = "@bean-mapping";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			messageConfig.setMappingId(CommUtils.getRealValue("uppcfg", value));
		}

		// @dest-message-id
		expression = "@dest-message-id";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (CharSequenceUtil.isNotBlank(value)) {
			messageConfig.setDestinationMessageId(CommUtils.getRealValue("uppcfg", value));
		}

		return messageConfig;
	}

	private ErrorMappingConfig parseErrorMappingConfig(Processor processor, Node node) {
		String value = null;
		String expression = null;
		ErrorMappingConfig errorMappingConfig = new ErrorMappingConfig();
		// @source-message-id
		if (MessageObjectType.MESSAGE_BEAN == processor.getSourceChannelMessageObjectType()) {
			expression = "@source-message-id";
			value = Dom4jUtils.getSingleNodeText(expression, node);
			Assert.isFalse(StrUtil.isBlankIfStr(value), () -> new CommonException("null"));
			errorMappingConfig.setSoureMessageId(CommUtils.getRealValue("uppcfg", value));
		}

		// @bean-mapping
		expression = "@bean-mapping";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isBlankIfStr(value), () -> new CommonException("null"));
		errorMappingConfig.setMappingRuleId(CommUtils.getRealValue("uppcfg", value));
		return errorMappingConfig;
	}

	private MessageHandlerConfig parseHandlerConfig(Node node) {
		String value = null;
		String expression = null;
		MessageHandlerConfig handlerConfig = new MessageHandlerConfig();
		// @class
		expression = "@class";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		Assert.isFalse(StrUtil.isBlankIfStr(value), () -> new CommonException("null"));
		handlerConfig.setClassName(CommUtils.getRealValue("uppcfg", value));
		// parameter
		handlerConfig.setParameters(parseParameters(node));
		return handlerConfig;
	}

	private void parseEvent(Node node, Processor processor) {
		String value = null;
		String expression = null;

		ActionConfig actionConfig = null;
		// <action>
		expression = "action";
		Node subNode = Dom4jUtils.getSingleNode(expression, node);
		if (null != subNode) {
			// actionConfig = parseActionConfig(subNode);
		}

		// @type
		expression = "@type";
		value = Dom4jUtils.getSingleNodeText(expression, node);

	}

	private void parseRecognizerTable() {
		String expression = "/channel/recognizer-table/recognizer";
		List<Node> nodeLst = Dom4jUtils.selectNodes(doc, expression);

		RecognizerConfig recognizerConfig = null;
		for (Node node : nodeLst) {
			recognizerConfig = parseRecognizer(node);
			if (recognizerConfig == null || config.getRecognizerTable().containsKey(recognizerConfig.getId())) {
				throw new CommonException("reduplicate");
			}
			config.getRecognizerTable().put(recognizerConfig.getId(), recognizerConfig);
		}
	}

	private RecognizerConfig parseRecognizer(Node node) {
		String value = null;
		String expression = null;
		RecognizerConfig recognizerConfig = new RecognizerConfig();
		expression = "@id";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		// Assert.notBlank(value, () -> new CommonException("/channel/connector/@config
		// must not be null."));
		recognizerConfig.setId(value);

		expression = "@type";
		value = Dom4jUtils.getSingleNodeText(expression, node);
		if (StrUtil.isEmptyIfStr(value)) {
			expression = "@class";
			value = Dom4jUtils.getSingleNodeText(expression, node);
			Assert.notBlank(value, () -> new CommonException("/channel/connector/@config must not be null."));
			recognizerConfig.setClassName(value);
		} else {
			recognizerConfig.setType(value);
		}

		// <parameter>
		Map<String, String> parameters = parseParameters(node);
		recognizerConfig.setParameters(parameters);

		// <recognizer>
		expression = "recognizer";
		List<Node> nodeLst = Dom4jUtils.getNodes(node, expression);

		RecognizerConfig subRecognizerConfig = null;
		recognizerConfig.setComponentList(new ArrayList<>());
		for (Node subNode : nodeLst) {
			subRecognizerConfig = parseRecognizer(subNode);
			recognizerConfig.getComponentList().add(subRecognizerConfig);
		}
		return recognizerConfig;
	}

	private Map<String, String> parseParameters(Node node) {
		String expression = "parameter";
		Map<String, String> parameters = new HashMap<>();
		List<Node> nodeLst = Dom4jUtils.getNodes(node, expression);
		for (Node subNode : nodeLst) {
			String text = subNode.getText();
			expression = "@name";
			Element el = (Element) subNode;
			String name = el.attributeValue("name");// Dom4jUtils.getSingleNodeText(expression, node);
			String value = el.getText();
			parameters.put(name, value);
		}
		return parameters;
	}

	private void parseReturnCodeRecognizer() {
		// TODO Auto-generated method stub

	}

	private void parseMessageTypeRecognizer() {
		String value = null;
		String expression = null;

		if (ConstantMB.ChannelMode.CLIENT == config.getMode()) {
			return;
		}

		MessageTypeRecognizerConfig messageTypeRecognizerConfig = new MessageTypeRecognizerConfig();
		// @recognizer-id
		expression = "/channel/message-type-recognizer/@recognizer-id";
		value = Dom4jUtils.getSingleNodeText(expression, doc);
		Assert.notBlank(value, () -> new CommonException("/channel/connector/@config must not be null."));
		messageTypeRecognizerConfig.setRecognizerId(CommUtils.getRealValue("uppcfg", value));

		expression = "/channel/message-type-recognizer/message";
		List<Node> nodeLst = Dom4jUtils.selectNodes(doc, expression);
		String messageType = null;
		String processor = null;
		for (Node node : nodeLst) {
			expression = "@message-type";
			value = Dom4jUtils.getSingleNodeText(expression, node);
			Assert.notBlank(value, () -> new CommonException("/channel/connector/@config must not be null."));
			messageType = value;
			if (messageTypeRecognizerConfig.getMessageTypeProcessorMap().containsKey(messageType)) {
				throw new CommonException("reduplicate");
			}

			expression = "@processor";
			value = Dom4jUtils.getSingleNodeText(expression, node);
			Assert.notBlank(value, () -> new CommonException("/channel/connector/@config must not be null."));
			processor = value;
			messageTypeRecognizerConfig.getMessageTypeProcessorMap().put(messageType, processor);
		}
		config.setMessageTypeRecognizerConfig(messageTypeRecognizerConfig);
	}

	private void parseEventHandler() {
		// TODO Auto-generated method stub

	}

	private void parseMessageBean() {
		// TODO Auto-generated method stub

	}

	private void parseConnector() {
		String value = null;
		String xpathExpression = null;
		ConnectorConfig connectorConfig = new ConnectorConfig();
		xpathExpression = "/channel/connector/@config";
		value = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
		Assert.notBlank(value, () -> new CommonException("/channel/connector/@config must not be null."));
		connectorConfig.setConfig(value);

		xpathExpression = "/channel/connector/@type";
		value = Dom4jUtils.getSingleNodeText(xpathExpression, doc);
		if (CharSequenceUtil.isNotBlank(value)) {
			connectorConfig.setType(ConstantMB.ConnectorMode.getConnectorModeByName(value));
		}
		config.setConnectorConfig(connectorConfig);
	}

	private void checkAll() {
		// 1. event-handler
		// checkEventHandler();

		// 1. connector
		// checkConnector();

		// 2. message-type-recognizer
		checkMessageTypeRecognizer();

		// 3. return-code-recognizer
		// checkReturnCodeRecognizer();

		// 4. recognizer-table
		// no check

		// 5. processor-table
		checkProcessorTable();

		// 6. route-table
		checkRouteTable();
	}

	private void checkRouteTable() {
		// TODO Auto-generated method stub

	}

	private void checkProcessorTable() {
		// TODO Auto-generated method stub

	}

	private void checkMessageTypeRecognizer() {
		if (ConstantMB.ChannelMode.CLIENT == config.getMode()) {
			return;
		}

		MessageTypeRecognizerConfig messageTypeRecognizerConfig = config.getMessageTypeRecognizerConfig();
		if (null == messageTypeRecognizerConfig) {
			return;
		}
		// 识别器是否存在？
		RecognizerConfig recognizerConfig = config.getRecognizerTable().get(messageTypeRecognizerConfig.getRecognizerId());
		if (null == recognizerConfig) {
			throw new CommonException("message-type-recognizer's recognizer-id=" + messageTypeRecognizerConfig.getRecognizerId() + " doesn't exist!");
		}
		// 设置识别器
		messageTypeRecognizerConfig.setRecognizerConfig(recognizerConfig);

		// 报文类型对应的处理器是否存在？
		String processorId = null;
		Map.Entry<String, String> en = null;
		Iterator<Entry<String, String>> it = messageTypeRecognizerConfig.getMessageTypeProcessorMap().entrySet().iterator();
		while (it.hasNext()) {
			en = it.next();
			processorId = en.getValue();
			if (!config.getProcessorTable().containsKey(processorId)) {
				throw new CommonException("message-type[" + en.getKey() + "]'s processor[" + processorId + "] doesn't exist!");
			}
		}
	}
}