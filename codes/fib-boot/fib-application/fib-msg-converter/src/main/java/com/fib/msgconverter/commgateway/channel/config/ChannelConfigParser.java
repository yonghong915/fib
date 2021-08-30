package com.fib.msgconverter.commgateway.channel.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.config.processor.ErrorMappingConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageHandlerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageTransformerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ActionConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ProcessorRule;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ScheduleRule;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.config.route.Determination;
import com.fib.msgconverter.commgateway.channel.config.route.RouteRule;
import com.fib.msgconverter.commgateway.session.SessionConstants;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 通道配置文件解析器
 * 
 * @author fangyh
 * @version 1.0.0
 * @date 2021-08-30
 */
public class ChannelConfigParser {
	private ChannelConfig config;

	private XPath xpath = null;

	private Document doc = null;

	public ChannelConfig parse(InputStream in) {
		config = new ChannelConfig();

		// 1. 解析XML数据，准备XPATH
		prepareXPath(in);

		// 2. 提取配置数据
		try {
			parseAll();
		} catch (XPathExpressionException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		// 3. 检查配置合法性、关联合法性等
		checkAll();

		return config;
	}

	private void prepareXPath(InputStream in) {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			// domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			doc = builder.parse(new InputSource(in));
		} catch (ParserConfigurationException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (SAXException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (IOException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} catch (FactoryConfigurationError e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}
		XPathFactory factory = XPathFactory.newInstance();
		xpath = factory.newXPath();
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

		Iterator<RouteRule> it = config.getRouteTable().values().iterator();
		RouteRule routeRule = null;
		while (it.hasNext()) {
			routeRule = it.next();
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

		Iterator<Processor> it = config.getProcessorTable().values().iterator();
		Processor processor = null;
		while (it.hasNext()) {
			processor = it.next();
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

		Map<String, List<ActionConfig>> map = processor.getEventConfig();
		Iterator<List<ActionConfig>> iterator = map.values().iterator();
		List<ActionConfig> list = null;
		ActionConfig actionConfig = null;
		while (iterator.hasNext()) {
			list = iterator.next();
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
			Iterator<String> it = returnCodeRecognizerConfig.getErrorCodeSet().iterator();
			String errorCode = null;
			while (it.hasNext()) {
				errorCode = it.next();
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
		Map.Entry<String, String> en = null;
		Iterator<Map.Entry<String, String>> it = messageTypeRecognizerConfig.getMessageTypeProcessorMap().entrySet()
				.iterator();
		while (it.hasNext()) {
			en = it.next();
			processorId = en.getValue();
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

	private void parseAll() throws XPathExpressionException {
		String value = null;
		String expression = null;

		// 1. <channel>
		// @type | @class
		expression = "channel/@type";
		value = xpath.evaluate(expression, doc);
		if (value != null && value.trim().length() != 0) {
			value = value.trim();
			config.setType(getRealValue(value));
		} else {
			expression = "channel/@class";
			value = xpath.evaluate(expression, doc);
			if (null == value) {
				// throw new RuntimeException(expression + " is Null!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(expression + " is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
				}
			}
			config.setClassName(getRealValue(value));
		}
		// @mode
		expression = "channel/@mode";
		value = xpath.evaluate(expression, doc);
		if (null == value) {
			// throw new RuntimeException(expression + " is NULL!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(expression + " is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
			}
		}
		config.setMode(ChannelConfig.getModeByText(getRealValue(value)));

		// description
		expression = "channel/description/text()";
		value = xpath.evaluate(expression, doc);
		config.setDescription(value);

		// 2. <connector>
		parseConnector();

		// 3. <message-bean>
		parseMessageBean();

		// 3. <event-handler>
		parseEventHandler();

		// // 4. <default-processor>
		// parseDefaultProcessor();

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

	private void parseMessageBean() throws XPathExpressionException {
		String expression = "channel/message-bean/@group-id";
		String value = xpath.evaluate(expression, doc);

		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				config.setMessageBeanGroupId(getRealValue(value));
			}
		}
	}

	private void parseEventHandler() throws XPathExpressionException {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String value = null;
		String expression = null;

		expression = "channel/event-handler";
		Object node = xpath.evaluate(expression, doc, XPathConstants.NODE);
		if (null == node) {
			return;
		}

		// event-handler/text()
		expression = "channel/event-handler/text()";
		value = xpath.evaluate(expression, doc);
		if (null == value) {
			// throw new RuntimeException(expression + " is NULL!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(expression + " is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
			}
		}
		config.setEventHandlerClazz(getRealValue(value));
	}

	private void parseChannelSymbolTable() throws XPathExpressionException {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String expression = "channel/channel-symbol-table/channel-symbol";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		// if (0 == nodeList.getLength()) {
		// throw new RuntimeException(expression + "'s count is zero!");
		// }
		ChannelSymbol channelSymbol = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			channelSymbol = parseChannelSymbol(nodeList.item(i));
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

	private ChannelSymbol parseChannelSymbol(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;
		ChannelSymbol channelSymbol = new ChannelSymbol();

		// @symbol
		expression = "@symbol";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
		}
		channelSymbol.setSymbol(getRealValue(value));

		// @channel-id
		expression = "@channel-id";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
		}
		channelSymbol.setChannlId(getRealValue(value));

		// @name
		expression = "@name";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			}
		}
		channelSymbol.setName(getRealValue(value));

		return channelSymbol;
	}

	private void parseRouteTable() throws XPathExpressionException {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String expression = "channel/route-table/route-rule";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		// if (0 == nodeList.getLength()) {
		// throw new RuntimeException(expression + "'s count is zero!");
		// }
		RouteRule routeRule = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			routeRule = parseRouteRule(nodeList.item(i));
			if (config.getRouteTable().containsKey(routeRule.getId())) {
				// throw new RuntimeException("route-rule[" + i + "]/@id = "
				// + routeRule.getId() + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { "route-rule[" + i + "]/@id = " + routeRule.getId() }));
			}
			config.getRouteTable().put(routeRule.getId(), routeRule);
		}

	}

	private RouteRule parseRouteRule(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;
		RouteRule routeRule = new RouteRule();

		// @id
		expression = "@id";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
		}
		routeRule.setId(getRealValue(value));

		// @type
		expression = "@type";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(expression + " is Null!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(expression + " is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
			}
		}
		routeRule.setType(RouteRule.getTypeByText(getRealValue(value)));

		// @dest-channel-symbol
		expression = "@dest-channel-symbol";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			}
		}
		routeRule.setDestinationChannelSymbol(getRealValue(value));

		// <expression>
		expression = "expression/text()";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			}
		}
		routeRule.setExpression(getRealValue(value));

		// <determination>
		expression = "determination";
		Node subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			routeRule.setDeterminationTable(parseDetermination(subNode));
		}

		//
		// <determination>/<default>
		expression = "determination/default";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			routeRule.setDefaultDetermination(parseCase(subNode));
		}

		return routeRule;
	}

	private Map<String, Determination> parseDetermination(Node node) throws XPathExpressionException {
		String expression = null;

		// <case>
		expression = "case";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
		if (0 == nodeList.getLength()) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + "'s count is zero!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("count.zero",
					new String[] { node.getNodeName() + "/" + expression }));
		}

		Map<String, Determination> determinationTable = new HashMap<>();
		Determination determination = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			determination = parseCase(nodeList.item(i));
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

	private Determination parseCase(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;
		Determination determination = new Determination();

		// @result
		expression = "@result";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			}
		}
		determination.setResult(getRealValue(value));

		// @dest-channel-symbol
		expression = "@dest-channel-symbol";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
		}
		determination.setDestinationChannelSymbol(getRealValue(value));

		// <processor-override>
		// expression = "processor-override";
		// Node subNode = (Node) xpath.evaluate(expression, node,
		// XPathConstants.NODE);
		// if (subNode != null) {
		// determination.setProcessorOverride(parseProcessor(subNode));
		// }
		expression = "processor-override/text()";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				value = getRealValue(value);
				if (!config.getProcessorTable().containsKey(value)) {
					// throw new RuntimeException("processor[" + value
					// + "] is NOT" + " exist in processor table");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("ChannelConfigParser.parseCase.processor.isNotInTable", new String[] { value }));
				} else {
					determination.setProcessorOverride((Processor) config.getProcessorTable().get(value));
				}
			}
		}

		return determination;
	}

	private void parseProcessorTable() throws XPathExpressionException {
		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		String expression = "channel/processor-table/processor";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		if (0 == nodeList.getLength()) {
			// throw new RuntimeException(expression + "'s count is zero!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("count.zero", new String[] { expression }));
		}
		Processor processor = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			processor = parseProcessor(nodeList.item(i));
			if (config.getProcessorTable().containsKey(processor.getId())) {
				// throw new RuntimeException("processor[" + i + "]/@id = "
				// + processor.getId() + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { "processor[" + i + "]/@id = " + processor.getId() }));
			}
			config.getProcessorTable().put(processor.getId(), processor);
		}

	}

	private Processor parseProcessor(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;
		Processor processor = new Processor();

		// @id
		expression = "@id";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
			if (80 < value.length()) {
				// throw new RuntimeException("process-id[@value=" + value
				// + "]'s length must be less than 20!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("ChannelConfigParser.checkProcessorId.length", new String[] { value }));
			}
		}
		processor.setId(value);

		// @type
		expression = "@type";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				processor.setType(Processor.getTypeByText(getRealValue(value)));
			}
		}

		// @route-rule
		expression = "@route-rule";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				processor.setRouteRuleId(getRealValue(value));
			}
		}

		// @source-async
		expression = "@source-async";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if ("true".equalsIgnoreCase(value)) {
				processor.setSourceAsync(true);
			}
		}

		// @dest-async
		expression = "@dest-async";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if ("true".equalsIgnoreCase(value)) {
				processor.setDestAsync(true);
			}
		}

		// @timeout
		expression = "@timeout";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				processor.setTimeout(Integer.parseInt(getRealValue(value)));
			}
		}

		// @error-handler
		// expression = "@error-handler";
		// value = xpath.evaluate(expression, node);
		// if (null != value) {
		// value = value.trim();
		// if (0 < value.length()) {
		// processor.setErrorHandlerId(value);
		// }
		// }
		// @source-channel-message-object-type
		expression = "@source-channel-message-object-type";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				processor.setSourceChannelMessageObjectType(Processor.getMessageObjectTypeByText(getRealValue(value)));
			}
		}

		// @source-map-charset
		if (Processor.MSG_OBJ_TYP_MAP == processor.getSourceChannelMessageObjectType()) {
			expression = "@source-map-charset";
			value = xpath.evaluate(expression, node);
			if (null != value) {
				value = value.trim();
				if (0 < value.length()) {
					processor.setSourceMapCharset(getRealValue(value));
				}
			}
		}

		// @dest-channel-message-object-type
		expression = "@dest-channel-message-object-type";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				processor.setDestChannelMessageObjectType(Processor.getMessageObjectTypeByText(getRealValue(value)));
			}
		}

		// @dest-map-charset
		expression = "@dest-map-charset";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				processor.setDestMapCharset(getRealValue(value));
			}
		}

		// @local-source
		expression = "@local-source";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				if ("true".equals(value)) {
					processor.setLocalSource(true);
				}
			}
		}

		// @is-default
		expression = "@is-default";
		value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if ("true".equals(value)) {
				if (null != config.getDefaultProcessor()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("ChannelConfigParser.processor.defaultProcessor.isExist"));
				}
				config.setDefaultProcessor(processor);
			}
		}

		// <request-message-transformer>
		expression = "request-message-transformer";
		Node subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			processor.setRequestMessageConfig(parseMessageConfig(subNode));
		}

		// <request-message-handler>
		expression = "request-message-handler";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			processor.setRequestMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		// <response-message-transformer>
		expression = "response-message-transformer";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			processor.setResponseMessageConfig(parseMessageConfig(subNode));
		}

		// <response-handler>
		expression = "response-message-handler";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			processor.setResponseMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		// <error-message-transformer>
		expression = "error-message-transformer";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			processor.setErrorMessageConfig(parseMessageConfig(subNode));
		}

		// <error-message-handler>
		expression = "error-message-handler";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (subNode != null) {
			processor.setErrorMessageHandlerConfig(parseHandlerConfig(subNode));
		}

		// <error-mapping>
		expression = "error-mapping";
		subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
		if (null != subNode) {
			processor.setErrorMappingConfig(parseErrorMappingConfig(processor, subNode));
		}

		// // <notifier>
		// expression = "notifier";
		// subNode = (Node) xpath.evaluate(expression, node,
		// XPathConstants.NODE);
		// if (null != subNode) {
		// processor.setNotifierConfig(parseNotifierConfig(subNode));
		// }
		// <error-handler>
		expression = "count(event)";
		value = xpath.evaluate(expression, node);

		if (null == value || 0 == value.trim().length()) {
			return processor;
		}

		int count = Integer.parseInt(value.trim());
		for (int i = 1; i < count + 1; i++) {
			expression = "event[" + i + "]";
			subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);

			parseEvent(subNode, processor);
		}

		return processor;
	}

	private void parseEvent(Node node, Processor processor) throws XPathExpressionException {
		// <action>
		String expression = "action";
		Node subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);

		ActionConfig actionConfig = null;
		if (null != subNode) {
			actionConfig = parseActionConfig(subNode);
		}

		// @type
		expression = "@type";
		String value = xpath.evaluate(expression, node);
		if (null != value) {
			value = value.trim();
			if (0 < value.length()) {
				if (processor.getEventConfig().containsKey(value)) {
					(processor.getEventConfig().get(SessionConstants.getStateByText(value))).add(actionConfig);
				} else {
					List<ActionConfig> list = new ArrayList<>();
					list.add(actionConfig);
					processor.getEventConfig().put(SessionConstants.getStateByText(value), list);
				}
			}
		}

		if (null == value || 0 == value.length()) {
			// event
			expression = "count(event-set/event)";
			value = xpath.evaluate(expression, node);

			if (null == value) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is NULL!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
						new String[] { node.getNodeName() + "/" + expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(node.getNodeName() + "/"
					// + expression + " is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
							new String[] { node.getNodeName() + "/" + expression }));
				}
				value = getRealValue(value);
			}

			// @type
			int count = Integer.parseInt(value);
			if (0 == count) {
				// throw new RuntimeException(
				// "count(<event-set>/<event>) should more than zero while"
				// + "<event>/@type is NULL or Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("ChannelConfigParser.parseEvent.event.count.zero"));
			}
			for (int i = 1; i < count + 1; i++) {
				expression = "event-set/event[" + i + "]/@type";
				value = xpath.evaluate(expression, node);
				if (null == value) {
					// throw new RuntimeException(node.getNodeName() + "/"
					// + expression + " is NULL!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
							new String[] { node.getNodeName() + "/" + expression }));
				} else {
					value = value.trim();
					if (0 == value.length()) {
						// throw new RuntimeException(node.getNodeName() + "/"
						// + expression + " is Blank!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
								new String[] { node.getNodeName() + "/" + expression }));
					}
					value = getRealValue(value);
				}

				if (processor.getEventConfig().containsKey(value)) {
					(processor.getEventConfig().get(SessionConstants.getStateByText(value))).add(actionConfig);
				} else {
					List<ActionConfig> list = new ArrayList<>();
					list.add(actionConfig);
					processor.getEventConfig().put(SessionConstants.getStateByText(value), list);
				}
			}
		}
	}

	private ActionConfig parseActionConfig(Node node) throws XPathExpressionException {
		// @type
		String expression = "@type";
		String value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		ActionConfig actionConfig = new ActionConfig();
		actionConfig.setType(ActionConfig.getTypeByText(value));

		// @class
		if (ActionConfig.TYP_CLASS == actionConfig.getType()) {
			expression = "@class";
			value = xpath.evaluate(expression, node);
			if (null == value) {
				throw new RuntimeException(node.getNodeName() + "/" + expression
						+ " must be not NULL while <action>/@type is \"" + ActionConfig.TYP_CLASS_TXT + "\"");
			}
		} else {
			// <schedule-rule>
			expression = "job/schedule-rule";
			Node subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
			if (null == subNode) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression
				// + " must be not NULL while <action>/@type is\""
				// + ActionConfig.TYP_JOB_TXT + "\"");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.parseActionConfig.action.class.null",
						new String[] { node.getNodeName() + "/" + expression, ActionConfig.TYP_CLASS_TXT }));
			}
			actionConfig.setScheduleRule(parseScheduleRule(subNode));
			// <process-rule>
			expression = "job/process-rule";
			subNode = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
			if (null == subNode) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression
				// + " must be not NULL while <action>/@type is\""
				// + ActionConfig.TYP_JOB_TXT + "\"");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"ChannelConfigParser.parseActionConfig.action.class.null",
						new String[] { node.getNodeName() + "/" + expression, ActionConfig.TYP_JOB_TXT }));
			}
			actionConfig.setProcessorRule(parseProcessorRule(subNode));
		}
		return actionConfig;
	}

	private ScheduleRule parseScheduleRule(Node node) throws XPathExpressionException {
		// @loop
		String expression = "@loop";
		String value = xpath.evaluate(expression, node);

		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		ScheduleRule scheduleRule = new ScheduleRule();
		int loop = Integer.parseInt(value);
		if (0 > loop) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " must more than zero!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("count.zero",
					new String[] { node.getNodeName() + "/" + expression }));
		}
		scheduleRule.setLoop(loop);
		// @interval
		expression = "@interval";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		scheduleRule.setIntervalString(value);
		scheduleRule.setInterval(ScheduleRule.getTimesByText(value));
		// @end-flag
		expression = "@end-flag";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		scheduleRule.setEndFlag(SessionConstants.getStateByText(value));

		return scheduleRule;
	}

	private ProcessorRule parseProcessorRule(Node node) throws XPathExpressionException {
		// @processor-id
		String expression = "@processor-id";
		String value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
			if (80 < value.length()) {
				// throw new RuntimeException("process-id[@value=" + value
				// + "]'s length must be less than 20!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
						.getString("ChannelConfigParser.checkProcessorId.length", new String[] { value }));
			}
		}

		ProcessorRule processorRule = new ProcessorRule();
		processorRule.setProcessorId(value);

		// @request-message-from
		expression = "@request-message-from";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		processorRule.setRequestMessageFrom(ProcessorRule.getRequestMessageFromByText(value));

		return processorRule;
	}

	private ErrorMappingConfig parseErrorMappingConfig(Processor processor, Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;

		ErrorMappingConfig errorMappingConfig = new ErrorMappingConfig();

		// @source-message-id
		if (Processor.MSG_OBJ_TYP_MB == processor.getSourceChannelMessageObjectType()) {
			expression = "@source-message-id";
			value = xpath.evaluate(expression, node);
			if (null == value) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
						new String[] { node.getNodeName() + "/" + expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(node.getNodeName() + "/"
					// + expression + " is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
							new String[] { node.getNodeName() + "/" + expression }));
				}
				value = getRealValue(value);
			}
			errorMappingConfig.setSoureMessageId(value);
		}

		// @bean-mapping
		expression = "@bean-mapping";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		errorMappingConfig.setMappingRuleId(value);

		return errorMappingConfig;
	}

	private MessageHandlerConfig parseHandlerConfig(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;

		MessageHandlerConfig handlerConfig = new MessageHandlerConfig();

		// @class
		expression = "@class";
		value = xpath.evaluate(expression, node);
		if (null == value) {
			// throw new RuntimeException(node.getNodeName() + "/" + expression
			// + " is Null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { node.getNodeName() + "/" + expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Blank!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
						new String[] { node.getNodeName() + "/" + expression }));
			}
			value = getRealValue(value);
		}
		handlerConfig.setClassName(value);

		// parameter
		handlerConfig.setParameters(parseParameters(node));

		return handlerConfig;
	}

	private MessageTransformerConfig parseMessageConfig(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;

		MessageTransformerConfig messageConfig = new MessageTransformerConfig();

		// @source-message-id
		expression = "@source-message-id";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			} else {
				value = getRealValue(value);
			}
		}
		messageConfig.setSourceMessageId(value);

		// @bean-mapping
		expression = "@bean-mapping";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			} else {
				value = getRealValue(value);
			}
		}
		messageConfig.setMappingId(value);

		// @dest-message-id
		expression = "@dest-message-id";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			} else {
				value = getRealValue(value);
			}
		}
		messageConfig.setDestinationMessageId(value);

		return messageConfig;
	}

	private void parseRecognizerTable() throws XPathExpressionException {
		String expression = "channel/recognizer-table/recognizer";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
		// if (0 == nodeList.getLength()) {
		// throw new RuntimeException(expression + "'s count is zero!");
		// }
		RecognizerConfig recognizerConfig = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			recognizerConfig = parseRecognizer(nodeList.item(i));
			if (null == recognizerConfig.getId()) {
				// throw new RuntimeException("recognizer[" + i +
				// "]/@id is null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
						new String[] { "recognizer[" + i + "]/@id" }));
			}
			if (config.getRecognizerTable().containsKey(recognizerConfig.getId())) {
				// throw new RuntimeException("recognizer[" + i + "]/@id = "
				// + recognizerConfig.getId() + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { "recognizer[" + i + "]/@id = " + recognizerConfig.getId() }));
			}
			config.getRecognizerTable().put(recognizerConfig.getId(), recognizerConfig);
		}

	}

	private RecognizerConfig parseRecognizer(Node node) throws XPathExpressionException {
		String value = null;
		String expression = null;

		RecognizerConfig recognizerConfig = new RecognizerConfig();
		// @id
		expression = "@id";
		value = xpath.evaluate(expression, node);
		if (value != null) {
			value = value.trim();
			if (0 == value.length()) {
				value = null;
			} else {
				value = getRealValue(value);
			}
		}
		recognizerConfig.setId(value);

		// @type | @class
		expression = "@type";
		value = xpath.evaluate(expression, node);
		if (value != null && value.trim().length() != 0) {
			value = value.trim();
			recognizerConfig.setType(value);
		} else {
			expression = "@class";
			value = xpath.evaluate(expression, node);
			if (null == value) {
				// throw new RuntimeException(node.getNodeName() + "/"
				// + expression + " is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
						new String[] { node.getNodeName() + "/" + expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(node.getNodeName() + "/"
					// + expression + " is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
							new String[] { node.getNodeName() + "/" + expression }));
				}
				value = getRealValue(value);
			}
			recognizerConfig.setClassName(value);
		}

		// <parameter>
		Map<String, String> parameters = parseParameters(node);
		recognizerConfig.setParameters(parameters);

		// <recognizer>
		expression = "recognizer";
		NodeList nodeList = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
		if (nodeList.getLength() > 0) {
			recognizerConfig.setComponentList(new ArrayList<>());
			RecognizerConfig subRecognizerConfig = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				subRecognizerConfig = parseRecognizer(nodeList.item(i));
				recognizerConfig.getComponentList().add(subRecognizerConfig);
			}
		}
		return recognizerConfig;
	}

	private Map<String, String> parseParameters(Node parentNode) throws XPathExpressionException {
		String value = null;
		String expression = null;
		Map<String, String> parameters = null;

		int count = 0;
		expression = "count(parameter)";
		value = xpath.evaluate(expression, parentNode);
		if (value != null) {
			count = Integer.parseInt(value);
		}
		if (0 == count) {
			return null;
		} else {
			parameters = new HashMap<>();
		}

		String name = null;
		for (int i = 1; i < count + 1; i++) {
			// name
			expression = "parameter[" + i + "]/@name";
			value = xpath.evaluate(expression, parentNode);
			if (null == value) {
				// throw new RuntimeException(parentNode.getNodeName() + "/"
				// + expression + " is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
						new String[] { parentNode.getNodeName() + "/" + expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(parentNode.getNodeName() + "/"
					// + expression + " is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("blank",
							new String[] { parentNode.getNodeName() + "/" + expression }));
				}
				value = getRealValue(value);
			}
			name = value;

			// value
			expression = "parameter[" + i + "]/text()";
			value = xpath.evaluate(expression, parentNode);

			parameters.put(name, value);
		}

		return parameters;
	}

	private void parseReturnCodeRecognizer() throws XPathExpressionException {
		String value = null;
		String expression = null;

		expression = "channel/return-code-recognizer";
		Object node = xpath.evaluate(expression, doc, XPathConstants.NODE);
		if (null == node) {
			return;
		}

		ReturnCodeRecognizerConfig returnCodeRecognizerConfig = new ReturnCodeRecognizerConfig();
		// @recognizer-id
		expression = "channel/return-code-recognizer/@recognizer-id";
		value = xpath.evaluate(expression, doc);
		if (null == value) {
			// throw new RuntimeException(expression + " is Null!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(expression + " is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
			}
			value = getRealValue(value);
		}
		returnCodeRecognizerConfig.setRecognizerId(value);

		// <success-code-set>
		int count = 0;
		expression = "count(channel/return-code-recognizer/success-code-set/code)";
		value = xpath.evaluate(expression, doc);
		if (value != null) {
			count = Integer.parseInt(value);
		}
		if (0 == count) {
			// throw new RuntimeException(
			// "return-code-recognizer/success-code-set is null!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("null",
					new String[] { "return-code-recognizer/success-code-set" }));
		}
		for (int i = 1; i < count + 1; i++) {
			// code
			expression = "channel/return-code-recognizer/success-code-set/code[" + i + "]/text()";
			value = xpath.evaluate(expression, doc);
			if (null == value) {
				// throw new RuntimeException(expression + " is Null!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(expression + " is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
				}
				value = getRealValue(value);
			}
			if (returnCodeRecognizerConfig.getSuccessCodeSet().contains(value)) {
				// throw new RuntimeException(expression + " = " + value
				// + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { expression + " = " + value }));
			}

			returnCodeRecognizerConfig.getSuccessCodeSet().add(value);
		}

		// <error-code-set>
		count = 0;
		expression = "count(channel/return-code-recognizer/error-code-set/code)";
		value = xpath.evaluate(expression, doc);
		if (value != null) {
			count = Integer.parseInt(value);
			returnCodeRecognizerConfig.setErrorCodeSet(new HashSet<>());
		}
		for (int i = 1; i < count + 1; i++) {
			// code
			expression = "channel/return-code-recognizer/error-code-set/code[" + i + "]/text()";
			value = xpath.evaluate(expression, doc);
			if (null == value) {
				// throw new RuntimeException(expression + " is Null!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(expression + " is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
				}
				value = getRealValue(value);
			}
			if (returnCodeRecognizerConfig.getErrorCodeSet().contains(value)) {
				// throw new RuntimeException(expression + " = " + value
				// + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { expression + " = " + value }));
			}

			returnCodeRecognizerConfig.getErrorCodeSet().add(value);
		}
		config.setReturnCodeRecognizerConfig(returnCodeRecognizerConfig);
	}

	private void parseMessageTypeRecognizer() throws XPathExpressionException {
		String value = null;
		String expression = null;

		if (ChannelConfig.MODE_CLIENT == config.getMode()) {
			return;
		}

		MessageTypeRecognizerConfig messageTypeRecognizerConfig = new MessageTypeRecognizerConfig();

		// @recognizer-id
		expression = "channel/message-type-recognizer/@recognizer-id";
		value = xpath.evaluate(expression, doc);
		if (null == value) {
			// throw new RuntimeException(expression + " is Null!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(expression + " is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
			}
			value = getRealValue(value);
		}
		messageTypeRecognizerConfig.setRecognizerId(value);

		// <message>
		int count = 0;
		expression = "count(channel/message-type-recognizer/message)";
		value = xpath.evaluate(expression, doc);
		if (value != null) {
			count = Integer.parseInt(value);
		}
		String messageType = null;
		String processor = null;
		for (int i = 1; i < count + 1; i++) {
			// @message-type
			expression = "channel/message-type-recognizer/message[" + i + "]/@message-type";
			value = xpath.evaluate(expression, doc);
			if (null == value) {
				// throw new RuntimeException(expression + " is Null!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(expression + " is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
				}
				value = getRealValue(value);
			}
			messageType = value;
			if (messageTypeRecognizerConfig.getMessageTypeProcessorMap().containsKey(messageType)) {
				// throw new RuntimeException(expression + " = " + messageType
				// + " is Reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { expression + " = " + messageType }));
			}

			// @processor
			expression = "channel/message-type-recognizer/message[" + i + "]/@processor";
			value = xpath.evaluate(expression, doc);
			if (null == value) {
				// throw new RuntimeException(expression + " is Null!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
			} else {
				value = value.trim();
				if (0 == value.length()) {
					// throw new RuntimeException(expression + " is Blank!");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
				}
				value = getRealValue(value);
			}
			processor = value;

			messageTypeRecognizerConfig.getMessageTypeProcessorMap().put(messageType, processor);
		}

		config.setMessageTypeRecognizerConfig(messageTypeRecognizerConfig);

	}

	private void parseConnector() throws XPathExpressionException {
		String value = null;
		String expression = null;

		ConnectorConfig connectorConfig = new ConnectorConfig();

		// // @type
		// expression = "channel/connector/@type";
		// value = xpath.evaluate(expression, doc);
		// if (null == value) {
		// throw new RuntimeException(expression + " is NULL!");
		// } else {
		// value = value.trim();
		// if (0 == value.length()) {
		// throw new RuntimeException(expression + " is Blank!");
		// }
		// }
		// connectorConfig.setType(ConnectorConfig.getChannelTypeByText(value));

		// @config
		expression = "channel/connector/@config";
		value = xpath.evaluate(expression, doc);
		if (null == value) {
			// throw new RuntimeException(expression + " is Null!");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("null", new String[] { expression }));
		} else {
			value = value.trim();
			if (0 == value.length()) {
				// throw new RuntimeException(expression + " is Blank!");
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance().getString("blank", new String[] { expression }));
			}
			value = getRealValue(value);
		}
		connectorConfig.setConfig(value);

		config.setConnectorConfig(connectorConfig);

	}

	private String getRealValue(String value) {
		if (null == value || null == CommGateway.getVariableConfig()) {
			return value;
		} else {
			int startIndex = value.indexOf("${");

			if (-1 == startIndex) {
				return value;
			} else {
				startIndex += 2;
				int endIndex = value.indexOf("}", startIndex);
				if (-1 == endIndex) {
					return value;
				} else {
					value = value.substring(startIndex, endIndex);
					String realValue = CommGateway.getVariableConfig().getProperty(value);
					if (null == realValue || 0 == realValue.trim().length()) {
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("CommGateway.variable.notFound", new String[] { value }));

					} else {
						return realValue;
					}

				}
			}
		}
	}
}