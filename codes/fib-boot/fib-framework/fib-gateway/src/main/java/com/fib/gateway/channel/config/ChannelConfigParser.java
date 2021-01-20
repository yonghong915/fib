package com.fib.gateway.channel.config;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.xml.sax.SAXException;

import com.fib.commons.exception.CommonException;
import com.fib.commons.xml.Dom4jUtils;
import com.fib.gateway.channel.config.processor.ErrorMappingConfig;
import com.fib.gateway.channel.config.processor.MessageHandlerConfig;
import com.fib.gateway.channel.config.processor.MessageTransformerConfig;
import com.fib.gateway.channel.config.processor.event.ActionConfig;
import com.fib.gateway.channel.config.processor.event.ProcessorRule;
import com.fib.gateway.channel.config.processor.event.ScheduleRule;
import com.fib.gateway.channel.config.recognizer.RecognizerConfig;
import com.fib.gateway.channel.config.route.Determination;
import com.fib.gateway.channel.config.route.RouteRule;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.util.SessionConstants;
import com.fib.gateway.message.xml.config.processor.Processor;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import cn.hutool.core.util.StrUtil;

public class ChannelConfigParser {
	private ChannelConfig config;

	private Document doc = null;

	public ChannelConfig parse(FileInputStream in) {
		config = new ChannelConfig();
		prepareXPath(in);

		parseAll();

		checkAll();
		return config;
	}

	private void checkAll() {
		// 1. event-handler
		checkEventHandler();

		// 1. connector
		checkConnector();

		// 2. message-type-recognizer
		checkMessageTypeRecognizer();

		// 3. return-code-recognizer
		checkReturnCodeRecognizer();

		// 4. recognizer-table
		// no check

		// 5. processor-table
		checkProcessorTable();

		// 6. route-table
		checkRouteTable();

	}

	private void checkEventHandler() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String eventHandlerClazz = config.getEventHandlerClazz();
		if (null != eventHandlerClazz) {
			try {
				Class.forName(eventHandlerClazz);
			} catch (ClassNotFoundException e) {
				// throw new RuntimeException("Can't find event-handler["
				// + eventHandlerClazz + "]!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkEventHandler.eventHandler.canNotFind",
						new String[] { eventHandlerClazz }));
			}
		}
	}

	private void checkRouteTable() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		Iterator it = config.getRouteTable().values().iterator();
		RouteRule routeRule = null;
		while (it.hasNext()) {
			routeRule = (RouteRule) it.next();
			checkRouteRule(routeRule);
		}
	}

	private void checkRouteRule(RouteRule routeRule) {
		// 根据路由类型检查
		if (RouteRule.TYP_STATIC == routeRule.getType()) {
			// 静态路由。必须指定目的通道符号
			if (null == routeRule.getDestinationChannelSymbol()) {
				// throw new RuntimeException("RouteRule[" + routeRule.getId()
				// + "]'s dest-channel-symbol is null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkRouteRule.destChannelSymbol.null",
						new String[] { routeRule.getId() }));
			}
			if (!config.getChannelSymbolTable().containsKey(routeRule.getDestinationChannelSymbol())) {
				// throw new RuntimeException("RouteRule[" + routeRule.getId()
				// + "]'s dest-channel-symbol["
				// + routeRule.getDestinationChannelSymbol()
				// + "] doesn't exist!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkRouteRule.destChannelSymbol.notExist",
						new String[] { routeRule.getId(), routeRule.getDestinationChannelSymbol() }));
			}
		} else {
			// 动态路由。
			// 必须有表达式。
			if (null == routeRule.getExpression()) {
				// throw new RuntimeException("RouteRule[" + routeRule.getId()
				// + "]'s expression is null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkRouteRule.expression.null", new String[] { routeRule.getId() }));
			}
			// 必须有Determination
			if (null == routeRule.getDeterminationTable()) {
				// throw new RuntimeException("RouteRule[" + routeRule.getId()
				// + "]'s determination is null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkRouteRule.determination.null", new String[] { routeRule.getId() }));
			}
		}

	}

	private void checkProcessorTable() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		Iterator it = config.getProcessorTable().values().iterator();
		Processor processor = null;
		while (it.hasNext()) {
			processor = (Processor) it.next();
			checkProcessor(processor);
		}
	}

	private void checkProcessor(Processor processor) {
		// @route-rule
		if (Processor.TYP_LOCAL != processor.getType() && !processor.isLocalSource()
				&& !config.getRouteTable().containsKey(processor.getRouteRuleId())) {
			// throw new RuntimeException("processor[" + processor.getId()
			// + "]'s route-rule[" + processor.getRouteRuleId()
			// + "] doesn't exist!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"ChannelConfigParser.checkProcessor.routeRule.notExist",
					new String[] { processor.getId(), processor.getRouteRuleId() }));
		}

		// <error-mapping>
		if (null == processor.getErrorMappingConfig() && !processor.isSourceAsync() && !processor.isLocalSource()) {
			// throw new RuntimeException("processor[" + processor.getId()
			// + "]'s error-mapping" + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"ChannelConfigParser.checkProcessor.errorMapping.null", new String[] { processor.getId() }));
		}

		// 根据处理类型分别检查
		if (Processor.TYP_TRANSFORM == processor.getType()) {
			// 处理类型：转换。必须指定请求、应答报文的message-bean-id、mapping-id。
			// Request Message
			if (!processor.isLocalSource()) {
				if (!(Processor.MSG_OBJ_TYP_MAP == processor.getSourceChannelMessageObjectType())) {
					if (null == processor.getRequestMessageConfig().getSourceMessageId()) {
						// throw new RuntimeException(
						// "processor["
						// + processor.getId()
						// + "]("
						// + Processor.getTextByType(processor
						// .getType())
						// + ")'s request-message/@source-message-id is null!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"ChannelConfigParser.checkProcessor.requestMessage.sourceMessageId.null",
								new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
					}
				}

				if (Processor.MSG_OBJ_TYP_MB == processor.getDestChannelMessageObjectType()) {
					if (null == processor.getRequestMessageConfig().getDestinationMessageId()) {
						// throw new RuntimeException(
						// "processor["
						// + processor.getId()
						// + "]("
						// + Processor.getTextByType(processor
						// .getType())
						// + ")'s request-message/@dest-message-id is null!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"ChannelConfigParser.checkProcessor.requestMessage.destMessageId.null",
								new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
					}
				}

				if (null == processor.getRequestMessageConfig().getMappingId()) {
					// throw new RuntimeException("processor[" +
					// processor.getId()
					// + "]("
					// + Processor.getTextByType(processor.getType())
					// + ")'s request-message/@bean-mapping is null!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"ChannelConfigParser.checkProcessor.requestMessage.beanMapping.null",
							new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
				}

				if (!processor.isSourceAsync()) {
					// 处理器源通道需要同步处理时
					// 应答配置不能为空
					if (null == processor.getResponseMessageConfig()) {
						// throw new RuntimeException("processor["
						// + processor.getId() + "]("
						// + Processor.getTextByType(processor.getType())
						// + ")'s response-message is null!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"ChannelConfigParser.checkProcessor.responseMessage.null",
								new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
					}

					// 返回码识别器的错误码集合不为空，则processor必须有对业务错误报文的处理
					// 将返回码识别器移到client端，与processor不在同一channel下，因此此处不校验
					// if (null != config.getReturnCodeRecognizerConfig()
					// && null != config.getReturnCodeRecognizerConfig()
					// .getErrorCodeSet()
					// && null == processor.getErrorMessageConfig()) {
					// throw new RuntimeException(
					// MultiLanguageResourceBundle
					// .getInstance()
					// .getString(
					// "ChannelConfigParser.checkProcessor.errorMessage.null",
					// new String[] {
					// processor.getId(),
					// Processor
					// .getTextByType(processor
					// .getType()) }));
					// }

					if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
						if (null == processor.getResponseMessageConfig().getSourceMessageId()) {
							// throw new RuntimeException(
							// "processor["
							// + processor.getId()
							// + "]("
							// + Processor.getTextByType(processor
							// .getType())
							// +
							// ")'s response-message/@source-message-id is null!");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"ChannelConfigParser.checkProcessor.responseMessage.sourceMessageId.null",
									new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
						}

					}
					// 转换处理，应答报文配置必须有映射规则ID
					if (null == processor.getResponseMessageConfig().getMappingId()) {
						// throw new RuntimeException("processor["
						// + processor.getId() + "]("
						// + Processor.getTextByType(processor.getType())
						// + ")'s response-message/@bean-mapping is null!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"ChannelConfigParser.checkProcessor.responseMessage.beanMapping.null",
								new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
					}

					// 转换处理，如果配置了错误码，则错误应答报文配置必须有映射规则ID
					if (null != config.getReturnCodeRecognizerConfig()
							&& null != config.getReturnCodeRecognizerConfig().getErrorCodeSet()
							&& null == processor.getErrorMappingConfig().getMappingRuleId()) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"ChannelConfigParser.checkProcessor.errorMessage.beanMapping.null",
								new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
					}

				}
				if (!processor.isDestAsync()) {
					if (Processor.MSG_OBJ_TYP_MB == processor.getDestChannelMessageObjectType()) {
						if (null == processor.getResponseMessageConfig().getDestinationMessageId()) {
							// throw new RuntimeException(
							// "processor["
							// + processor.getId()
							// + "]("
							// + Processor
							// .getTextByType(processor
							// .getType())
							// +
							// ")'s response-message/@dest-message-id is null!");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"ChannelConfigParser.checkProcessor.responseMessage.destMessageId.null",
									new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
						}
					}
				}
			}
		} else if (Processor.TYP_LOCAL == processor.getType()) {
			// 处理类型：本地处理。必须指定源通道请求、应答报文的message-bean-id，及请求处理器。
			if (null == processor.getRequestMessageHandlerConfig()) {
				// throw new RuntimeException("processor[" + processor.getId()
				// + "](" + Processor.getTextByType(processor.getType())
				// + ")'s request-handler is null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkProcessor.requestHandler.null",
						new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
			}

			if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
				if (null == processor.getRequestMessageConfig().getSourceMessageId()) {
					// throw new RuntimeException("processor[" +
					// processor.getId()
					// + "]("
					// + Processor.getTextByType(processor.getType())
					// + ")'s request-message/@source-message-id is null!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"ChannelConfigParser.checkProcessor.requestMessage.sourceMessageId.null",
							new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
				}

				if (!processor.isSourceAsync()) {
					if (!(Processor.MSG_OBJ_TYP_MAP == processor.getDestChannelMessageObjectType())) {
						if (null == processor.getResponseMessageConfig().getSourceMessageId()) {
							// throw new RuntimeException(
							// "processor["
							// + processor.getId()
							// + "]("
							// + Processor.getTextByType(processor
							// .getType())
							// +
							// ")'s response-message/@source-message-id is null!");
							throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
									"ChannelConfigParser.checkProcessor.responseMessage.sourceMessageId.null",
									new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
						}
					}
				}
			}
		} else {
			// 处理类型：转发。
			// 此时源通道和目标通道的通讯方式必须相同
			if (processor.isDestAsync() ^ processor.isSourceAsync()) {
				// throw new RuntimeException(
				// "Processor["
				// + processor.getId()
				// + "]'s "
				// +
				// "source-async and dest-async must be the same while Processor's Type is "
				// + Processor
				// .getTextByType(Processor.TYP_TRANSMIT));
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkProcessor.sourceAsyncAndDestAsync.same",
						new String[] { processor.getId(), Processor.getTextByType(Processor.TYP_TRANSMIT) }));
			}
			// 当存在请求或应答处理器时，必须指定源通道请求、应答报文的message-bean-id
			if (processor.getRequestMessageHandlerConfig() != null) {
				if (null == processor.getRequestMessageConfig().getSourceMessageId()) {
					// throw new RuntimeException("processor[" +
					// processor.getId()
					// + "]("
					// + Processor.getTextByType(processor.getType())
					// + ")'s request-message/@source-message-id is null!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"ChannelConfigParser.checkProcessor.requestMessage.sourceMessageId.null",
							new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
				}
			}

			if (processor.getResponseMessageHandlerConfig() != null) {
				if (null == processor.getResponseMessageConfig().getSourceMessageId()) {
					// throw new RuntimeException(
					// "processor["
					// + processor.getId()
					// + "]("
					// + Processor.getTextByType(processor
					// .getType())
					// + ")'s response-message/@source-message-id is null!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"ChannelConfigParser.checkProcessor.responseMessage.sourceMessageId.null",
							new String[] { processor.getId(), Processor.getTextByType(processor.getType()) }));
				}
			}

			// 规定转发类型的处理器不能对报文进行特殊处理
			if (null != processor.getRequestMessageHandlerConfig()
					|| null != processor.getResponseMessageHandlerConfig()
					|| null != processor.getErrorMessageHandlerConfig()) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkProcessor.transmit.messageHandler.notNull",
						new String[] { processor.getId(), Processor.getTextByType(processor.getType()),
								Processor.getTextByType(Processor.TYP_TRANSMIT) }));
			}
			// 当路由规则为动态路由时，必须指定源通道请求报文的message-bean-id
			// if (!processor.isLocalSource()
			// && RouteRule.TYP_DYNAMIC == ((RouteRule) config
			// .getRouteTable().get(processor.getRouteRuleId()))
			// .getType()) {
			// if (null == processor.getRequestMessageConfig()
			// .getSourceMessageId()) {
			// throw new RuntimeException(
			// "processor["
			// + processor.getId()
			// + "]("
			// + Processor.getTextByType(processor
			// .getType())
			// +
			// ")'s route-rule's type is DYNAMIC, but its request-message/@source-message-id
			// is null!");
			// }
			// }
		}

		Map map = processor.getEventConfig();
		Iterator iterator = map.values().iterator();
		List list = null;
		ActionConfig actionConfig = null;
		while (iterator.hasNext()) {
			list = (List) iterator.next();
			for (int i = 0; i < list.size(); i++) {
				actionConfig = (ActionConfig) list.get(i);
				if (ActionConfig.TYP_JOB == actionConfig.getType()) {
					if (!config.getProcessorTable().containsKey(actionConfig.getProcessorRule().getProcessorId())) {
						// throw new RuntimeException("Job's processor["
						// + actionConfig.getProcessorRule()
						// .getProcessorId()
						// + "] is not exist!Channel Id["
						// + config.getMainConfig().getId() + "], "
						// + "Processor Id[" + processor.getId() + "]");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"ChannelConfigParser.checkProcessor.job.processor.notExist",
								new String[] { actionConfig.getProcessorRule().getProcessorId(),
										config.getMainConfig().getId(), processor.getId() }));
					}
				}
			}
		}

	}

	private void checkReturnCodeRecognizer() {
		// 返回码识别器可以不存在，此种情况下，不对应答报文做返回码识别，直接映射成源通道报文。
		// 某些第三方系统会定义一个统一的错误报文格式，此种情况下，对应答报文需做返回码识别，当为错误时，映射到错误报文。
		if (null == config.getReturnCodeRecognizerConfig()) {
			return;
		}

		ReturnCodeRecognizerConfig returnCodeRecognizerConfig = config.getReturnCodeRecognizerConfig();
		// 识别器是否存在？
		RecognizerConfig recognizerConfig = (RecognizerConfig) config.getRecognizerTable()
				.get(returnCodeRecognizerConfig.getRecognizerId());
		if (null == recognizerConfig) {
			// throw new RuntimeException(
			// "return-code-recognizer's recognizer-id="
			// + returnCodeRecognizerConfig.getRecognizerId()
			// + " doesn't exist!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"ChannelConfigParser.checkProcessor.returnCodeRecognizer.recognizerId.notExist",
					new String[] { returnCodeRecognizerConfig.getRecognizerId() }));
		}
		// 设置识别器
		returnCodeRecognizerConfig.setRecognizerConfig(recognizerConfig);

		// 检查成功返回码集合和失败返回码集合是否有重复
		if (returnCodeRecognizerConfig.getErrorCodeSet() != null) {
			Iterator it = returnCodeRecognizerConfig.getErrorCodeSet().iterator();
			String errorCode = null;
			while (it.hasNext()) {
				errorCode = (String) it.next();
				if (returnCodeRecognizerConfig.getSuccessCodeSet().contains(errorCode)) {
					// throw new RuntimeException(
					// "return-code-recognizer's error-code[" + errorCode
					// + " has already in success-code-set!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"ChannelConfigParser.checkProcessor.returnCodeRecognizer.errorCode.in.successCode",
							new String[] { errorCode }));
				}
			}
		}

	}

	private void checkMessageTypeRecognizer() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		MessageTypeRecognizerConfig messageTypeRecognizerConfig = config.getMessageTypeRecognizerConfig();
		if (null == messageTypeRecognizerConfig) {
			return;
		}
		// 识别器是否存在？
		RecognizerConfig recognizerConfig = (RecognizerConfig) config.getRecognizerTable()
				.get(messageTypeRecognizerConfig.getRecognizerId());
		if (null == recognizerConfig) {
			// throw new RuntimeException(
			// "message-type-recognizer's recognizer-id="
			// + messageTypeRecognizerConfig.getRecognizerId()
			// + " doesn't exist!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"ChannelConfigParser.checkProcessor.messageTypeRecognizer.recognizerId.notExist",
					new String[] { messageTypeRecognizerConfig.getRecognizerId() }));
		}
		// 设置识别器
		messageTypeRecognizerConfig.setRecognizerConfig(recognizerConfig);

		// 报文类型对应的处理器是否存在？
		String processorId = null;
		Map.Entry en = null;
		Iterator it = messageTypeRecognizerConfig.getMessageTypeProcessorMap().entrySet().iterator();
		while (it.hasNext()) {
			en = (Entry) it.next();
			processorId = (String) en.getValue();
			if (!config.getProcessorTable().containsKey(processorId)) {
				// throw new RuntimeException("message-type[" + en.getKey()
				// + "]'s processor[" + processorId + "] doesn't exist!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.checkProcessor.messageType.processor.notExist",
						new String[] { "" + en.getKey(), processorId }));
			}
		}

	}

	private void checkConnector() {
	}

	private void parseAll() {
		String value = null;
		String expression = null;

		// 1. <channel>
		// @type | @class
		expression = "ns:channel/@type";
		value = Dom4jUtils.getXPathValue(doc, expression);
		if (StrUtil.isNotBlank(value)) {
			value = value.trim();
			config.setType(value);
		} else {
			expression = "ns:channel/@class";
			value = Dom4jUtils.getXPathValue(doc, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			config.setClassName(value);
		}
		// @mode
		expression = "ns:channel/@mode";
		value = Dom4jUtils.getXPathValue(doc, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		config.setMode(ChannelConfig.getModeByText(value));

		// description
		expression = "ns:channel/ns:description";
		value = Dom4jUtils.getXPathValue(doc, expression);
		config.setDescription(value);

		// 2. <connector>
		parseConnector();

		// 3. <message-bean>
		parseMessageBean();

		// 3. <event-handler>
		parseEventHandler();

		// // 4. <default-processor>
		parseDefaultProcessor();

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
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String expression = "ns:channel/ns:channel-symbol-table/ns:channel-symbol";
		List<Node> nodeList = Dom4jUtils.getNodes(doc, expression);
		// if (0 == nodeList.getLength()) {
		// throw new RuntimeException(expression + "'s count is zero!");
		// }
		ChannelSymbol channelSymbol = null;
		for (int i = 0; i < nodeList.size(); i++) {
			channelSymbol = parseChannelSymbol(nodeList.get(i));
			if (config.getChannelSymbolTable().containsKey(channelSymbol.getSymbol())) {
				// throw new RuntimeException("channel-symbol[" + i
				// + "]/@symbol = " + channelSymbol.getSymbol()
				// + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { "channel-symbol[" + i + "]/@symbol = " + channelSymbol.getSymbol() }));
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
		value = Dom4jUtils.getXPathValue(node, expression);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException("null");
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException("blank");
			}
		}
		channelSymbol.setSymbol(value);

		// @channel-id
		expression = "@channel-id";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException("null");
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException("null");
			}
		}
		channelSymbol.setChannlId(value);

		// @name
		expression = "@name";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			}
		}
		channelSymbol.setName(value);

		return channelSymbol;
	}

	private void parseRouteTable() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String expression = "ns:channel/ns:route-table/ns:route-rule";
		List<Node> nodeList = Dom4jUtils.getNodes(doc, expression);
		// if (0 == nodeList.getLength()) {
		// throw new RuntimeException(expression + "'s count is zero!");
		// }
		RouteRule routeRule = null;
		for (int i = 0; i < nodeList.size(); i++) {
			routeRule = parseRouteRule(nodeList.get(i));
			if (config.getRouteTable().containsKey(routeRule.getId())) {
				// throw new RuntimeException("route-rule[" + i + "]/@id = "
				// + routeRule.getId() + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { "route-rule[" + i + "]/@id = " + routeRule.getId() }));
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
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		routeRule.setId(value);

		// @type
		expression = "@type";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		routeRule.setType(RouteRule.getTypeByText(value));

		// @dest-channel-symbol
		expression = "@dest-channel-symbol";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			routeRule.setDestinationChannelSymbol(value);
		}

		// <expression>
		expression = "ns:expression/text()";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			routeRule.setExpression(value);
		}

		// <determination>
		expression = "ns:determination";
		Node subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			routeRule.setDeterminationTable(parseDetermination(subNode));
		}

		//
		// <determination>/<default>
		expression = "ns:determination/ns:default";
		subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			routeRule.setDefaultDetermination(parseCase(subNode));
		}

		return routeRule;
	}

	private Map parseDetermination(Node node) {
		String value = null;
		String expression = null;

		// <case>
		expression = "ns:case";
		List<Node> nodeList = Dom4jUtils.getNodes(node, expression);
		if (0 == nodeList.size()) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + "'s count is zero!");
			throw new RuntimeException("count.zero");
		}

		Map determinationTable = new HashMap();
		Determination determination = null;
		for (int i = 0; i < nodeList.size(); i++) {
			determination = parseCase(nodeList.get(i));
			if (determinationTable.containsKey(determination.getResult())) {
				// throw new RuntimeException("determination/case[" + i
				// + "]/@result = " + determination.getResult()
				// + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { "determination/case[" + i + "]/@result = " + determination.getResult() }));
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
		value = Dom4jUtils.getXPathValue(node, expression);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException("null");
		} else {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			}
		}
		determination.setResult(value);

		// @dest-channel-symbol
		expression = "@dest-channel-symbol";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException("null");
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException("");
			}
		}
		determination.setDestinationChannelSymbol(value);

		// <processor-override>
		// expression = "processor-override";
		// Node subNode = (Node) xpath.evaluate(expression, node,
		// XPathConstants.NODE);
		// if (subNode != null) {
		// determination.setProcessorOverride(parseProcessor(subNode));
		// }
		expression = "ns:processor-override/text()";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				// value = getRealValue(value);
				if (!config.getProcessorTable().containsKey(value)) {
					// throw new RuntimeException("processor[" + value
					// + "] is NOT" + " exist in processor table");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("ChannelConfigParser.parseCase.processor.isNotInTable", new String[] { value }));
				} else {
					determination.setProcessorOverride(config.getProcessorTable().get(value));
				}
			}
		}

		return determination;
	}

	private void parseProcessorTable() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String expression = "ns:channel/ns:processor-table/ns:processor";
		List<Node> nodeList = Dom4jUtils.getNodes(doc, expression);
		if (nodeList.isEmpty()) {
			throw new CommonException("count.zero");
		}
		Processor processor = null;
		for (int i = 0; i < nodeList.size(); i++) {
			processor = parseProcessor(nodeList.get(i));
			if (config.getProcessorTable().containsKey(processor.getId())) {
				throw new CommonException("reduplicate processor @id = ");
			}
			config.getProcessorTable().put(processor.getId(), processor);
		}
	}

	private Processor parseProcessor(Node node) {
		String value = null;
		String expression = null;
		Processor processor = new Processor();

		// @id
		expression = "@id";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		processor.setId(value);

		// @type
		expression = "@type";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setType(Processor.getTypeByText(value));
		}

		// @route-rule
		expression = "@route-rule";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setRouteRuleId(value);
		}

		// @source-async
		expression = "@source-async";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setSourceAsync("true".equalsIgnoreCase(value));
		}

		// @dest-async
		expression = "@dest-async";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setDestAsync("true".equalsIgnoreCase(value));
		}

		// @timeout
		expression = "@timeout";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setTimeout(Integer.parseInt(value));
		}

		expression = "@source-channel-message-object-type";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setSourceChannelMessageObjectType(Processor.getMessageObjectTypeByText(value));
		}

		// @source-map-charset
		if (Processor.MSG_OBJ_TYP_MAP == processor.getSourceChannelMessageObjectType()) {
			expression = "@source-map-charset";
			value = Dom4jUtils.getXPathValue(node, expression);
			if (StrUtil.isNotBlank(value)) {
				processor.setSourceMapCharset(value);
			}
		}

		// @dest-channel-message-object-type
		expression = "@dest-channel-message-object-type";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setDestChannelMessageObjectType(Processor.getMessageObjectTypeByText(value));
		}

		// @dest-map-charset
		expression = "@dest-map-charset";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setDestMapCharset(value);
		}

		// @local-source
		expression = "@local-source";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			processor.setLocalSource("true".equals(value));
		}

		// @is-default
		expression = "@is-default";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			if ("true".equals(value)) {
				if (null != config.getDefaultProcessor()) {
					throw new CommonException("ChannelConfigParser.processor.defaultProcessor.isExist");
				}
				config.setDefaultProcessor(processor);
			}
		}

		// <request-message-transformer>
		expression = "ns:request-message-transformer";
		Node subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			processor.setRequestMessageConfig(parseMessageConfig(subNode));
		}

		// <request-message-handler>
		expression = "ns:request-message-handler";
		subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			processor.setRequestMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		// <response-message-transformer>
		expression = "ns:response-message-transformer";
		subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			processor.setResponseMessageConfig(parseMessageConfig(subNode));
		}

		// <response-handler>
		expression = "ns:response-message-handler";
		subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			processor.setResponseMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		// <error-message-transformer>
		expression = "ns:error-message-transformer";
		subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			processor.setErrorMessageConfig(parseMessageConfig(subNode));
		}

		// <error-message-handler>
		expression = "ns:error-message-handler";
		subNode = Dom4jUtils.getNode(node, expression);
		if (subNode != null) {
			processor.setErrorMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		// <error-mapping>
		expression = "ns:error-mapping";
		subNode = Dom4jUtils.getNode(node, expression);
		if (null != subNode) {
			processor.setErrorMappingConfig(parseErrorMappingConfig(processor, subNode));
		}

		// <error-handler>
		expression = "count(ns:event)";

		int count = Dom4jUtils.numberValueOf(node, expression);
		for (int i = 1; i < count + 1; i++) {
			expression = "ns:event[" + i + "]";
			subNode = Dom4jUtils.getNode(node, expression);
			parseEvent(subNode, processor);
		}
		return processor;
	}

	private void parseEvent(Node node, Processor processor) {
		// <action>
		String expression = "ns:action";
		Node subNode = Dom4jUtils.getNode(node, expression);

		ActionConfig actionConfig = null;
		if (null != subNode) {
			actionConfig = parseActionConfig(subNode);
		}

		// @type
		expression = "@type";
		String value = Dom4jUtils.getXPathValue(node, expression);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				if (processor.getEventConfig().containsKey(value)) {
					((List) processor.getEventConfig().get(SessionConstants.getStateByText(value))).add(actionConfig);
				} else {
					List list = new ArrayList();
					list.add(actionConfig);
					processor.getEventConfig().put(SessionConstants.getStateByText(value), list);
				}
			}
		}

		if (null == value || 0 == value.length()) {
			// event
			expression = "count(ns:event-set/ns:event)";
			Object obj = Dom4jUtils.getObject(node, expression);
			ExceptionUtil.requireNotEmpty(obj, "null");

			// @type
			int count = Integer.parseInt(value);
			if (0 == count) {
				throw new CommonException("ChannelConfigParser.parseEvent.event.count.zero");
			}
			for (int i = 1; i < count + 1; i++) {
				expression = "ns:event-set/ns:event[" + i + "]/@type";
				value = Dom4jUtils.getXPathValue(node, expression);
				ExceptionUtil.requireNotEmpty(value, "null");
				if (processor.getEventConfig().containsKey(value)) {
					((List) processor.getEventConfig().get(SessionConstants.getStateByText(value))).add(actionConfig);
				} else {
					List list = new ArrayList();
					list.add(actionConfig);
					processor.getEventConfig().put(SessionConstants.getStateByText(value), list);
				}
			}
		}
	}

	private ActionConfig parseActionConfig(Node node) {
		// @type
		String expression = "@type";
		String value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		ActionConfig actionConfig = new ActionConfig();
		actionConfig.setType(ActionConfig.getTypeByText(value));

		// @class
		if (ActionConfig.TYP_CLASS == actionConfig.getType()) {
			expression = "@class";
			value = Dom4jUtils.getXPathValue(node, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
		} else {
			// <schedule-rule>
			expression = "ns:job/ns:schedule-rule";
			Node subNode = Dom4jUtils.getNode(node, expression);
			if (null == subNode) {
				throw new CommonException("ChannelConfigParser.parseActionConfig.action.class.null");
			}
			actionConfig.setScheduleRule(parseScheduleRule(subNode));
			// <process-rule>
			expression = "ns:job/ns:process-rule";
			subNode = Dom4jUtils.getNode(node, expression);
			if (null == subNode) {
				throw new CommonException("ChannelConfigParser.parseActionConfig.action.class.null");
			}
			actionConfig.setProcessorRule(parseProcessorRule(subNode));
		}
		return actionConfig;
	}

	private ProcessorRule parseProcessorRule(Node node) {
		// @processor-id
		String expression = "@processor-id";
		String value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");

		ProcessorRule processorRule = new ProcessorRule();
		processorRule.setProcessorId(value);

		// @request-message-from
		expression = "@request-message-from";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		processorRule.setRequestMessageFrom(ProcessorRule.getRequestMessageFromByText(value));
		return processorRule;
	}

	private ScheduleRule parseScheduleRule(Node node) {
		// @loop
		String expression = "@loop";
		String value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");

		ScheduleRule scheduleRule = new ScheduleRule();
		int loop = Integer.parseInt(value);
		if (0 > loop) {
			throw new CommonException("count.zero");
		}
		scheduleRule.setLoop(loop);
		// @interval
		expression = "@interval";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");

		scheduleRule.setIntervalString(value);
		scheduleRule.setInterval(ScheduleRule.getTimesByText(value));
		// @end-flag
		expression = "@end-flag";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");

		scheduleRule.setEndFlag(SessionConstants.getStateByText(value));
		return scheduleRule;
	}

	private ErrorMappingConfig parseErrorMappingConfig(Processor processor, Node node) {
		String value = null;
		String expression = null;
		ErrorMappingConfig errorMappingConfig = new ErrorMappingConfig();

		// @source-message-id
		if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
			expression = "@source-message-id";
			value = Dom4jUtils.getXPathValue(node, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			errorMappingConfig.setSoureMessageId(value);
		}

		// @bean-mapping
		expression = "@bean-mapping";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		errorMappingConfig.setMappingRuleId(value);
		return errorMappingConfig;
	}

	private MessageHandlerConfig parseHandlerConfig(Node node) {
		String value = null;
		String expression = null;

		MessageHandlerConfig handlerConfig = new MessageHandlerConfig();
		// @class
		expression = "@class";
		value = Dom4jUtils.getXPathValue(node, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		handlerConfig.setClassName(value);

		// parameter
		handlerConfig.setParameters(parseParameters(node));
		return handlerConfig;
	}

	private MessageTransformerConfig parseMessageConfig(Node node) {
		String value = null;
		String expression = null;

		MessageTransformerConfig messageConfig = new MessageTransformerConfig();

		// @source-message-id
		expression = "@source-message-id";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			messageConfig.setSourceMessageId(value);
		}
		// @bean-mapping
		expression = "@bean-mapping";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			messageConfig.setMappingId(value);
		}
		// @dest-message-id
		expression = "@dest-message-id";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			messageConfig.setDestinationMessageId(value);
		}
		return messageConfig;
	}

	private void parseRecognizerTable() {
		String expression = "ns:channel/ns:recognizer-table/ns:recognizer";
		List<Node> nodeList = Dom4jUtils.getNodes(doc, expression);
		RecognizerConfig recognizerConfig = null;
		for (int i = 0; i < nodeList.size(); i++) {
			recognizerConfig = parseRecognizer(nodeList.get(i));
			if (null == recognizerConfig.getId()) {
				throw new CommonException("recognizer /@id");
			}
			if (config.getRecognizerTable().containsKey(recognizerConfig.getId())) {
				throw new CommonException("reduplicate recognizer /@id = ");
			}
			config.getRecognizerTable().put(recognizerConfig.getId(), recognizerConfig);
		}
	}

	private RecognizerConfig parseRecognizer(Node node) {
		String value = null;
		String expression = null;

		RecognizerConfig recognizerConfig = new RecognizerConfig();
		// @id
		expression = "@id";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			recognizerConfig.setId(value);
		}

		// @type | @class
		expression = "@type";
		value = Dom4jUtils.getXPathValue(node, expression);
		if (StrUtil.isNotBlank(value)) {
			recognizerConfig.setType(value);
		} else {
			expression = "@class";
			value = Dom4jUtils.getXPathValue(node, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			recognizerConfig.setClassName(value);
		}

		// <parameter>
		Map<String, String> parameters = parseParameters(node);
		recognizerConfig.setParameters(parameters);

		// <recognizer>
		expression = "ns:recognizer";
		List<Node> nodeList = Dom4jUtils.getNodes(node, expression);
		if (!nodeList.isEmpty()) {
			recognizerConfig.setComponentList(new ArrayList<>());
			RecognizerConfig subRecognizerConfig = null;
			for (int i = 0; i < nodeList.size(); i++) {
				subRecognizerConfig = parseRecognizer(nodeList.get(i));
				recognizerConfig.getComponentList().add(subRecognizerConfig);
			}
		}
		return recognizerConfig;
	}

	private Map<String, String> parseParameters(Node node) {
		String value = null;
		String expression = null;
		Map<String, String> parameters = null;

		expression = "count(ns:parameter)";
		int count = Dom4jUtils.numberValueOf(node, expression);
		if (0 == count) {
			return null;
		} else {
			parameters = new HashMap<>();
		}

		String name = null;
		for (int i = 1; i < count + 1; i++) {
			// name
			expression = "ns:parameter[" + i + "]/@name";
			value = Dom4jUtils.getXPathValue(node, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			name = value;

			// value
			expression = "ns:parameter[" + i + "]/text()";
			value = Dom4jUtils.getXPathValue(node, expression);
			parameters.put(name, value);
		}
		return parameters;
	}

	private void parseReturnCodeRecognizer() {
		String value = null;
		String expression = null;

		expression = "ns:channel/ns:return-code-recognizer";
		Object node = doc.selectSingleNode(expression);
		if (null == node) {
			return;
		}

		ReturnCodeRecognizerConfig returnCodeRecognizerConfig = new ReturnCodeRecognizerConfig();
		// @recognizer-id
		expression = "ns:channel/ns:return-code-recognizer/@recognizer-id";
		value = Dom4jUtils.getXPathValue(doc, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		returnCodeRecognizerConfig.setRecognizerId(value);

		// <success-code-set>
		int count = 0;
		expression = "count(ns:channel/ns:return-code-recognizer/ns:success-code-set/ns:code)";
		Object obj = Dom4jUtils.getObject(doc, expression);
		if (Objects.nonNull(obj)) {
			count = Integer.parseInt(String.valueOf(obj));
		}
		if (0 == count) {
			throw new CommonException("return-code-recognizer/success-code-set");
		}

		for (int i = 1; i < count + 1; i++) {
			// code
			expression = "ns:channel/ns:return-code-recognizer/ns:success-code-set/code[" + i + "]";
			value = Dom4jUtils.getXPathValue(doc, expression);
			ExceptionUtil.requireNotEmpty(value, "null");

			if (returnCodeRecognizerConfig.getSuccessCodeSet().contains(value)) {
				throw new CommonException("reduplicate");
			}
			returnCodeRecognizerConfig.getSuccessCodeSet().add(value);
		}

		// <error-code-set>
		count = 0;
		expression = "count(ns:channel/ns:return-code-recognizer/ns:error-code-set/ns:code)";
		obj = Dom4jUtils.getObject(doc, expression);
		if (Objects.nonNull(obj)) {
			count = Integer.parseInt(String.valueOf(obj));
		}
		for (int i = 1; i < count + 1; i++) {
			// code
			expression = "ns:channel/ns:return-code-recognizer/ns:error-code-set/code[" + i + "]";
			value = Dom4jUtils.getXPathValue(doc, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			if (returnCodeRecognizerConfig.getErrorCodeSet().contains(value)) {
				throw new CommonException("reduplicate");
			}
			returnCodeRecognizerConfig.getErrorCodeSet().add(value);
		}

		config.setReturnCodeRecognizerConfig(returnCodeRecognizerConfig);
	}

	private void parseMessageTypeRecognizer() {
		String value = null;
		String expression = null;
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		MessageTypeRecognizerConfig messageTypeRecognizerConfig = new MessageTypeRecognizerConfig();

		// @recognizer-id
		expression = "ns:channel/ns:message-type-recognizer/@recognizer-id";
		value = Dom4jUtils.getXPathValue(doc, expression);
		ExceptionUtil.requireNotEmpty(value, "");
		messageTypeRecognizerConfig.setRecognizerId(value);

		// <message>

		expression = "count(ns:channel/ns:message-type-recognizer/ns:message)";
		int count = Dom4jUtils.numberValueOf(doc, expression);

		String messageType = null;
		String processor = null;
		for (int i = 1; i < count + 1; i++) {
			// @message-type
			expression = "ns:channel/ns:message-type-recognizer/ns:message[" + i + "]/@message-type";
			value = Dom4jUtils.getXPathValue(doc, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			messageType = value;
			if (messageTypeRecognizerConfig.getMessageTypeProcessorMap().containsKey(messageType)) {
				throw new CommonException("reduplicate");
			}

			// @processor
			expression = "ns:channel/ns:message-type-recognizer/ns:message[" + i + "]/@processor";
			value = Dom4jUtils.getXPathValue(doc, expression);
			ExceptionUtil.requireNotEmpty(value, "null");
			processor = value;
			messageTypeRecognizerConfig.getMessageTypeProcessorMap().put(messageType, processor);
		}
		config.setMessageTypeRecognizerConfig(messageTypeRecognizerConfig);
	}

	private void parseDefaultProcessor() {
		// TODO Auto-generated method stub

	}

	private void parseEventHandler() {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}
		String expression = "ns:channel/ns:event-handler";
		String value = Dom4jUtils.getXPathValue(doc, expression);
		if (StrUtil.isNotBlank(value)) {
			config.setEventHandlerClazz(value);
		}
	}

	private void parseMessageBean() {
		String expression = "ns:channel/ns:message-bean/@group-id";
		String value = Dom4jUtils.getXPathValue(doc, expression);
		if (StrUtil.isNotBlank(value)) {
			config.setMessageBeanGroupId(value);
		}
	}

	private void parseConnector() {
		String value = null;
		String expression = null;
		ConnectorConfig connectorConfig = new ConnectorConfig();
		expression = "ns:channel/ns:connector/@config";
		value = Dom4jUtils.getXPathValue(doc, expression);
		ExceptionUtil.requireNotEmpty(value, "null");
		connectorConfig.setConfig(value);
		config.setConnectorConfig(connectorConfig);
	}

	private void prepareXPath(FileInputStream in) {
		try {
			doc = Dom4jUtils.getXPathDocument(in);
		} catch (DocumentException | SAXException e) {
			e.printStackTrace();
		}
	}
}
