package com.fib.msgconverter.commgateway.channel.mq.config;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.CodeRecognizerConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.nio.filter.AbstractMessageFilter;
import com.fib.msgconverter.commgateway.config.ChannelMainConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

public class MQChannelConfigParser extends DefaultHandler {
	public static final int TYPE_SERVER = 1001;
	public static final int TYPE_CLIENT = 1002;
	public static final int TYPE_DOUBLE = 1003;
	public static final int QUEUE_TYPE_SEND = 2001;
	public static final int QUEUE_TYPE_RECEIVE = 2002;

	private MQChannelConfig config;
	private String fileName;

	/**
	 * 通道具体配置
	 */
	private ChannelConfig channelConfig;

	public void setChannelConfig(ChannelConfig channelConfig) {
		this.channelConfig = channelConfig;
	}

	/**
	 * 通道主配置：id、name、startup、deploy等
	 */
	private ChannelMainConfig mainConfig;

	public void setMainConfig(ChannelMainConfig mainConfig) {
		this.mainConfig = mainConfig;
	}

	public MQChannelConfig parse(InputStream in) {
		fileName = fileName;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(in, this);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
					ex.printStackTrace(System.err);
				}
			}
		}
		// 解析后检查
		configCheck();
		return config;
	}

	private void configCheck() {
		if (0 == config.getQuqueConfig().size()) {
			// throw new RuntimeException(this.fileName
			// + ": <queue> number is zero");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("count.zero",
							new String[] { fileName + ": <queue>" }));

		} else if (2 < config.getQuqueConfig().size()) {
			// throw new RuntimeException(fileName + ": <queue> number["
			// + config.getQuqueConfig().size()
			// + "] is too big, 2 at most");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"MQChannelConfigParser.configCheck.count.moreThan2",
									new String[] { fileName + ": <queue>" }));
		} else {
			Map recognizerTable = channelConfig.getRecognizerTable();
			Iterator iter = config.getQuqueConfig().values().iterator();
			String tmp = null;
			while (iter.hasNext()) {
				QueueConfig queueConfig = (QueueConfig) iter.next();
				tmp = queueConfig.getMessageKeyRecognizerId();
				if (null != tmp) {
					if (!recognizerTable.containsKey(tmp)) {
						throw new RuntimeException(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MQChannel.messageKeyRecognizer.unkownId",
												new String[] { fileName, tmp,
														mainConfig.getId() }));
					} else {
						queueConfig
								.setMessageKeyRecognizer(createRecognizer((RecognizerConfig) recognizerTable
										.get(tmp)));
					}

				}
			}

			if (TYPE_DOUBLE == config.getType()) {
				if (null == config.getCodeRecognizerConfig()) {
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"MQChannelConfigParser.configCheck.type.notDouble",
											new String[] { fileName }));
				}

				tmp = config.getCodeRecognizerConfig().getRecognizerId();
				if (!recognizerTable.containsKey(tmp)) {
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"LongConnectionSocketChannelConfigParser.configCheck.codeRecognier.unkownId",
											new String[] { fileName, tmp,
													mainConfig.getId() }));
				} else {
					config
							.setCodeRecognizer(createRecognizer((RecognizerConfig) recognizerTable
									.get(tmp)));
				}
			}
		}
	}

	private String elementValue = null;
	private String attr = null;
	private int queuePos = 0;
	private QueueConfig currKeyOwner;
	private Set currentCodeSet;

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (null == elementValue) {
			elementValue = new String(ch, start, length);
		} else {
			elementValue += new String(ch, start, length);
		}
	}

	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if ("mq-channel".equalsIgnoreCase(name)) {
			config = new MQChannelConfig();
			// @type
			attr = attributes.getValue("type");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": mq-channel/@type is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString("config.null",
								new String[] { fileName, "mq-channel/@type" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": mq-channel/@type is blank");
					throw new RuntimeException(
							MultiLanguageResourceBundle.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"mq-channel/@type" }));
				}
				attr = getRealValue(attr);
			}
			config.setType(getTypeByText(attr));
			// @ccsid
			attr = attributes.getValue("ccsid");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": mq-channel/@ccsid is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"mq-channel/@ccsid" }));
				}
				attr = getRealValue(attr);
				config.setCcsid(Integer.parseInt(attr));
			}
			// @timeout
			attr = attributes.getValue("timeout");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": mq-channel/@timeout is Blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"mq-channel/@timeout" }));
				}
				attr = getRealValue(attr);
				config.setTimeout(Integer.parseInt(attr));
			}
		} else if ("queue".equalsIgnoreCase(name)) {
			QueueConfig queueConfig = new QueueConfig();
			currKeyOwner = queueConfig;
			// @name
			attr = attributes.getValue("name");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": mq-channel/queue["
				// + queuePos + "]/@name is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"mq-channel/queue[" + queuePos
												+ "]/@name" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "]/@name is blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"mq-channel/queue[" + queuePos
													+ "]/@name" }));
				}
				attr = getRealValue(attr);

				if (null != config.getQuqueConfig().get(attr)) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "] is reduplicated");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"reduplicate",
									new String[] { fileName
											+ ": mq-channel/queue[" + queuePos
											+ "]" }));
				}
			}
			queueConfig.setName(attr);
			// @type
			attr = attributes.getValue("type");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": mq-channel/queue["
				// + queuePos + "]/@type is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"mq-channel/queue[" + queuePos
												+ "]/@type" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "]/@type is blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"mq-channel/queue[" + queuePos
													+ "]/@type" }));
				}
				attr = getRealValue(attr);
			}
			queueConfig.setType(getQueueTypeByText(attr));
			// @server-address
			attr = attributes.getValue("server-address");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": mq-channel/queue["
				// + queuePos + "]/@server-address is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"mq-channel/queue[" + queuePos
												+ "]/@server-address" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "]/@server-address is blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"mq-channel/queue[" + queuePos
													+ "]/@server-address" }));
				}
				queueConfig.setServerAddressString(attr);
				attr = getRealValue(attr);
			}
			queueConfig.setServerAddress(attr);
			// @port
			attr = attributes.getValue("port");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": mq-channel/queue["
				// + queuePos + "]/@port is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"mq-channel/queue[" + queuePos
												+ "]/@port" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "]/@port is blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"mq-channel/queue[" + queuePos
													+ "]/@port" }));
				}
				queueConfig.setPortString(attr);
				attr = getRealValue(attr);
			}
			queueConfig.setPort(Integer.parseInt(attr));
			// @queue-manager
			attr = attributes.getValue("queue-manager");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": mq-channel/queue["
				// + queuePos + "]/@queue-manager is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"mq-channel/queue[" + queuePos
												+ "]/@queue-manager" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "]/@queue-manager is blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"mq-channel/queue[" + queuePos
													+ "]/@queue-manager" }));
				}
				attr = getRealValue(attr);
			}
			queueConfig.setQueueManager(attr);
			// @channel
			attr = attributes.getValue("channel");
			if (null == attr) {
				// throw new RuntimeException(fileName +
				// ": mq-channel/queue["
				// + queuePos + "]/@channel is null");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] {
										fileName,
										"mq-channel/queue[" + queuePos
												+ "]/@channel" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName +
					// ": mq-channel/queue["
					// + queuePos + "]/@channel is blank");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] {
											fileName,
											"mq-channel/queue[" + queuePos
													+ "]/@channel" }));
				}
				attr = getRealValue(attr);
			}
			queueConfig.setChannel(attr);

			// @relation-message-id
			attr = attributes.getValue("relation-message-id");
			if (null != attr) {
				attr = attr.trim();
				if (0 != attr.length()) {
					if (attr.startsWith("0x")) {
						queueConfig.setRelationMessageId(CodeUtil
								.HextoByte(attr));
					} else {
						queueConfig.setRelationMessageId(attr.getBytes());
					}
				}
			}
			
			config.getQuqueConfig().put(queueConfig.getName(), queueConfig);
		} else if ("message-key-recognizer".equalsIgnoreCase(name)) {
			// MessageKeyConfig keyConfig = new MessageKeyConfig();
			// // @from-index
			// attr = attributes.getValue("from-index");
			// if (null == attr) {
			// // throw new RuntimeException(fileName
			// // + ": mq-channel/message-key" + "/@from-index is null");
			// throw new RuntimeException(
			// MultiLanguageResourceBundle
			// .getInstance()
			// .getString(
			// "config.null",
			// new String[] { fileName,
			// "mq-channel/message-key/@from-index" }));
			// } else {
			// attr = attr.trim();
			// if (0 == attr.length()) {
			// // throw new RuntimeException(fileName
			// // + ": mq-channel/message-key"
			// // + "/@from-index is blank");
			// throw new RuntimeException(
			// MultiLanguageResourceBundle
			// .getInstance()
			// .getString(
			// "config.blank",
			// new String[] { fileName,
			// "mq-channel/message-key/@from-index" }));
			// }
			// }
			// keyConfig.setFromIndex(Integer.parseInt(attr));
			// // @key-length
			// attr = attributes.getValue("key-length");
			// if (null == attr) {
			// // throw new RuntimeException(fileName
			// // + ": mq-channel/message-key" + "/@key-length is null");
			// throw new RuntimeException(
			// MultiLanguageResourceBundle
			// .getInstance()
			// .getString(
			// "config.null",
			// new String[] { fileName,
			// "mq-channel/message-key/@key-length" }));
			// } else {
			// attr = attr.trim();
			// if (0 == attr.length()) {
			// // throw new RuntimeException(fileName
			// // + ": mq-channel/message-key"
			// // + "/@key-length is blank");
			// throw new RuntimeException(
			// MultiLanguageResourceBundle
			// .getInstance()
			// .getString(
			// "config.blank",
			// new String[] { fileName,
			// "mq-channel/message-key/@key-length" }));
			// }
			// }
			// keyConfig.setKeyLength(Integer.parseInt(attr));

			// @recognizer-id
			attr = attributes.getValue("recognizer-id");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"config.blank",
											new String[] { fileName,
													"mq-channel/message-key/@recognizer-id" }));
				}
				attr = getRealValue(attr);
				currKeyOwner.setMessageKeyRecognizerId(attr);
			}
			// currKeyOwner.setKeyConfig(keyConfig);
			// //@key-encoding
			// attr = attributes.getValue("key-encoding");
			// keyConfig.setKeyEncoding(attr);
		} else if ("code-recognizer".equals(name)) {
			CodeRecognizerConfig codeRecognizerConfig = new CodeRecognizerConfig();
			config.setCodeRecognizerConfig(codeRecognizerConfig);

			// @recognizer-id
			String attr = attributes.getValue("recognizer-id");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": code-recognizer/@recognizer-id is NULL!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"config.null",
								new String[] { fileName,
										"code-recognizer/@recognizer-id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": code-recognizer/@recognizer-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"code-recognizer/@recognizer-id" }));
				}
				attr = getRealValue(attr);
			}
			codeRecognizerConfig.setRecognizerId(attr);
		} else if ("request-code-set".equals(name)) {
			currentCodeSet = config.getCodeRecognizerConfig()
					.getRequestCodeSet();
		} else if ("response-code-set".equals(name)) {
			currentCodeSet = config.getCodeRecognizerConfig()
					.getResponseCodeSet();
		} else if ("reader-filter".equals(name)) {
			String attr = attributes.getValue("class");
			if (null == attr) {
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance()
								.getString(
										"config.null",
										new String[] { fileName,
												"reader-filter/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": code-recognizer/@recognizer-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"reader-filter/@class" }));
				}
				attr = getRealValue(attr);
			}

//			config.getReaderFilterList()
//					.add(
//							(AbstractMessageFilter) ClassUtil
//									.createClassInstance(attr));
			
			config.setReaderFilter((AbstractMessageFilter) ClassUtil
					.createClassInstance(attr));
		} else if ("writer-filter".equals(name)) {
			String attr = attributes.getValue("class");
			if (null == attr) {
				throw new RuntimeException(
						MultiLanguageResourceBundle.getInstance()
								.getString(
										"config.null",
										new String[] { fileName,
												"writer-filter/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": code-recognizer/@recognizer-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle
							.getInstance().getString(
									"config.blank",
									new String[] { fileName,
											"writer-filter/@class" }));
				}
				attr = getRealValue(attr);
			}

//			config.getWriterFilterList()
//					.add(
//							(AbstractMessageFilter) ClassUtil
//									.createClassInstance(attr));
			
			config.setWriterFilter((AbstractMessageFilter) ClassUtil
					.createClassInstance(attr));
		}
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("queue".equalsIgnoreCase(name)) {
			queuePos++;
		} else if ("code".equals(name)) {
			String code = elementValue;
			if (null != code) {
				code = code.trim();
				if (0 < code.length()) {
					currentCodeSet.add(getRealValue(getRealValue(code)));
				}
			}
		}

		elementValue = null;
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
					String realValue = CommGateway.getVariableConfig()
							.getProperty(value);
					if (null == realValue || 0 == realValue.trim().length()) {
						throw new RuntimeException(MultiLanguageResourceBundle
								.getInstance().getString(
										"CommGateway.variable.notFound",
										new String[] { value }));

					} else {
						return realValue;
					}

				}
			}
		}
	}

	private int getQueueTypeByText(String text) {
		if ("send".equalsIgnoreCase(text)) {
			return QUEUE_TYPE_SEND;
		} else if ("receive".equalsIgnoreCase(text)) {
			return QUEUE_TYPE_RECEIVE;
		} else {
			// throw new RuntimeException(fileName
			// + ": mq-channel/queue/@type must be 'send' or 'receive'");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MQChannelConfigParser.queueType",
							new String[] { fileName
									+ ": mq-channel/queue/@type" }));
		}
	}

	private int getTypeByText(String text) {
		if ("server".equalsIgnoreCase(text)) {
			return TYPE_SERVER;
		} else if ("client".equalsIgnoreCase(text)) {
			return TYPE_CLIENT;
		} else if ("double".equalsIgnoreCase(text)) {
			return TYPE_DOUBLE;
		} else {
			// throw new RuntimeException(fileName
			// + ": mq-channel/@type must be 'server' or 'client'");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MQChannelConfigParser.channelType",
							new String[] { fileName + ": mq-channel/@type" }));
		}
	}

	private AbstractMessageRecognizer createRecognizer(
			RecognizerConfig recognizerConfig) {
		AbstractMessageRecognizer recognizer = null;
		try {
			Class clazz = Class.forName(recognizerConfig.getClassName());
			recognizer = (AbstractMessageRecognizer) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			ExceptionUtil.throwActualException(e);
		} catch (InstantiationException e) {
			ExceptionUtil.throwActualException(e);
		} catch (IllegalAccessException e) {
			ExceptionUtil.throwActualException(e);
		}
		recognizer.setParameters(recognizerConfig.getParameters());

		if (recognizer instanceof AbstractCompositeMessageRecognizer) {
			AbstractCompositeMessageRecognizer compositeRecognizer = (AbstractCompositeMessageRecognizer) recognizer;
			RecognizerConfig componentConfig = null;
			Iterator it = recognizerConfig.getComponentList().iterator();
			while (it.hasNext()) {
				componentConfig = (RecognizerConfig) it.next();
				compositeRecognizer.add(createRecognizer(componentConfig));
			}
		}

		return recognizer;
	}

}
