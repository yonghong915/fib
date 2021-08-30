package com.fib.msgconverter.commgateway.channel.longconnection.config;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.nio.config.FilterConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.ReaderConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.WriterConfig;
import com.fib.msgconverter.commgateway.config.ChannelMainConfig;
import com.fib.msgconverter.commgateway.config.base.DynamicObjectConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ExceptionUtil;

public class LongConnectionSocketChannelConfigParser extends DefaultHandler {
	private LongConnectionSocketChannelConfig config = null;
	private String fileName = "connector";
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

	/**
	 * 解析后检查
	 */
	private void configCheck() {
		// 1. ConnectionConfig
		if (config.getConnectionConfigs().size() <= 0) {
			// throw new RuntimeException(fileName
			// + ": <connection> number is zero!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.connection.zero", new String[] { fileName }));
		}
		if (config.getConnectionConfigs().size() > 2) {
			// throw new RuntimeException(fileName
			// + ": too many <connection>! :"
			// + config.getConnectionConfigs().size());
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.connection.tooMany",
					new String[] { fileName, "" + config.getConnectionConfigs().size() }));
		}
		if (config.getConnectionConfigs().size() == 1) {
			ConnectionConfig[] conns = new ConnectionConfig[1];
			config.getConnectionConfigs().values().toArray(conns);
			if (ConnectionConfig.COMM_DIRECTION_DOUBLE != conns[0].getDirection()) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": only one <connection>, its communicate direction must be \"double\"!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.connection.direction.double",
						new String[] { fileName }));
			}
		}
		if (config.getConnectionConfigs().size() == 2) {
			ConnectionConfig[] conns = new ConnectionConfig[2];
			config.getConnectionConfigs().values().toArray(conns);
			if (conns[0].getDirection() == conns[1].getDirection()) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": two <connection>! each <connection>'s communicate direction must be
				// opposite!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.connection.direction.opposite",
						new String[] { fileName }));
			}
			if (ConnectionConfig.COMM_DIRECTION_DOUBLE == conns[0].getDirection()
					|| ConnectionConfig.COMM_DIRECTION_DOUBLE == conns[1].getDirection()) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": two <connection>! their communicate direction can't be \"double\"!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.connection.direction.notDouble",
						new String[] { fileName }));
			}
			for (int i = 0; i < conns.length; i++) {
				if (ConnectionConfig.CONN_TYP_CLIENT == conns[i].getType()) {
					checkClientConn(conns[i]);
				} else {
					checkServerConn(conns[i]);
				}
			}
		}

		// 2. heartbeat
		HeartbeatConfig hearbeatConfig = null;
		Iterator<HeartbeatConfig> it = config.getHeartbeatConfigs().iterator();
		while (it.hasNext()) {
			hearbeatConfig = it.next();
			if (!config.getConnectionConfigs().containsKey(hearbeatConfig.getConnectionId())) {
				throw new RuntimeException(
						fileName + ": hearbeat's connection[" + hearbeatConfig.getConnectionId() + "] doesn't exist!");
			}
			if (ConnectionConfig.COMM_DIRECTION_DOUBLE == hearbeatConfig.getDirection()) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": hearbeat's communicate direction can't be \"double\"!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.heartbeat.connection.notDouble",
						new String[] { fileName }));
			}
			if (ConnectionConfig.COMM_DIRECTION_SEND == hearbeatConfig.getDirection()) {
				if (hearbeatConfig.getInterval() <= 0) {
					// throw new RuntimeException(
					// fileName
					// +
					// ": hearbeat's direction is \"send\", its @interval must be set!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannelConfigParser.configCheck.heartbeat.connection.direction.send",
							new String[] { fileName }));
				}
			}
			if (ConnectionConfig.COMM_DIRECTION_RECEIVE == hearbeatConfig.getDirection()
					&& hearbeatConfig.getResponseMessageSymbolId() != null) {
				if (hearbeatConfig.getResponseConnectionId() == null) {
					// 应答连接不填则默认为接收连接
					hearbeatConfig.setResponseConnectionId(hearbeatConfig.getConnectionId());
				} else {
					if (!config.getConnectionConfigs().containsKey(hearbeatConfig.getResponseConnectionId())) {
						// throw new RuntimeException(fileName
						// + ": hearbeat's response-connection["
						// + hearbeatConfig.getConnectionId()
						// + "] doesn't exist!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
								"LongConnectionSocketChannelConfigParser.configCheck.heartbeat.connection.responseConnection.notExist",
								new String[] { fileName, hearbeatConfig.getResponseConnectionId() }));
					}
				}

				if (hearbeatConfig.isResponseTurnBack()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("LongConnectionSocketChannel.responseTurnBack.onlyForSend"));
				}
			}

			if (null != hearbeatConfig.getMessageSymbolId()) {
				MessageSymbol messageSymbol = (MessageSymbol) config.getMessageSymbolTable()
						.get(hearbeatConfig.getMessageSymbolId());
				if (messageSymbol == null) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannelConfigParser.configCheck.heartbeat.messageSymbol.notExist",
							new String[] { fileName, hearbeatConfig.getMessageSymbolId() }));
				}

				hearbeatConfig.setMessageSymbol(messageSymbol);
			}

			if (null != hearbeatConfig.getResponseMessageSymbolId()) {
				MessageSymbol messageSymbol = (MessageSymbol) config.getMessageSymbolTable()
						.get(hearbeatConfig.getResponseMessageSymbolId());
				if (null == messageSymbol) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannelConfigParser.configCheck.heartbeat.messageSymbol.notExist",
							new String[] { fileName, hearbeatConfig.getResponseMessageSymbolId() }));
				}

				hearbeatConfig.setResponseMessageSymbol(messageSymbol);
			}
		}

		if (!(ChannelConfig.MODE_SERVER == channelConfig.getMode())) {
			Map<String, RecognizerConfig> recognizerTable = channelConfig.getRecognizerTable();
			// request-serial-number-recognizer
			String tmp = config.getRequestSerialNumberRecognizerId();
			if (null == tmp) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": RequestSerialNumberRecognizer is NULL!Please check
				// <request-serial-number-recognizer>");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.requestSerialNumberRecognizer.null",
						new String[] { fileName }));
			}
			if (!recognizerTable.containsKey(tmp)) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": Unkown request-serial-number-recognizer! Can't find this id["
				// + tmp + "] in Channel["
				// + mainConfig.getId()
				// + "]'s <recognizer-table>");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.requestSerialNumberRecognizer.unkownId",
						new String[] { fileName, tmp, mainConfig.getId() }));
			} else {
				config.setRequestSerialNumberRecognizer(createRecognizer(recognizerTable.get(tmp)));
			}
			// response-serial-number-recognizer
			tmp = config.getResponseSerialNumberRecognizerId();
			if (null == tmp) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": ResponseSerialNumberRecognizer is NULL!Please check
				// <response-serial-number-recognizer>");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.responseSerialNumberRecognizer.null",
						new String[] { fileName }));
			}
			if (!recognizerTable.containsKey(tmp)) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": Unkown response-serial-number-recognizer! Can't find this id["
				// + tmp + "] in Channel["
				// + mainConfig.getId()
				// + "]'s <recognizer-table>");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.responseSerialNumberRecognizer.unkownId",
						new String[] { fileName, tmp, mainConfig.getId() }));
			} else {
				config.setResponseSerialNumberRecognizer(createRecognizer((RecognizerConfig) recognizerTable.get(tmp)));
			}
			if (ChannelConfig.MODE_DOUBLE == channelConfig.getMode()) {
				// code-recognizer
				CodeRecognizerConfig codeRecognizerConfig = config.getCodeRecognizerConfig();
				if (null == codeRecognizerConfig) {
					// throw new RuntimeException(
					// fileName
					// +
					// ": CodeRecognizer is NULL!Please check <code-recognizer>");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannelConfigParser.configCheck.codeRecognizer.null",
							new String[] { fileName }));
				}
				if (0 == codeRecognizerConfig.getRequestCodeSet().size()
						&& 0 == codeRecognizerConfig.getResponseCodeSet().size()) {
					// throw new RuntimeException(
					// fileName
					// +
					// ": Both CodeRecognizer's request-code-set and response-code-set"
					// + " is empty!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannelConfigParser.configCheck.codeRecognizer.requestCodeSetAndResponseCodeSet.empty",
							new String[] { fileName }));
				}
				tmp = codeRecognizerConfig.getRecognizerId();
				if (!recognizerTable.containsKey(tmp)) {
					// throw new RuntimeException(
					// fileName
					// + ": Unkown code-recognizer! Can't find this id["
					// + tmp + "] in Channel["
					// + mainConfig.getId()
					// + "]'s <recognizer-table>");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannelConfigParser.configCheck.codeRecognier.unkownId",
							new String[] { fileName, tmp, mainConfig.getId() }));
				} else {
					config.setCodeRecognizer(createRecognizer((RecognizerConfig) recognizerTable.get(tmp)));
				}
			}
		}

		// 4. login
		if (null == config.getLoginConfig()) {
			// throw new RuntimeException(fileName
			// + ": LoginConfig is null! Please check <login>!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.loginConfig.null", new String[] { fileName }));
		}
		// 5. reader.filter-list
		List list = config.getReaderConfig().getFilterConfigList();
		for (int i = 0; i < list.size(); i++) {
			String className = ((FilterConfig) list.get(i)).getClassName();
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// throw new RuntimeException("Can't find class[" +
				// className
				// + "]!" + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.class.notFound",
						new String[] { className, e.getMessage() }), e);
			}
		}
		// 6. writer.filter-list
		list = config.getWriterConfig().getFilterConfigList();
		for (int i = 0; i < list.size(); i++) {
			String className = ((FilterConfig) list.get(i)).getClassName();
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// throw new RuntimeException("Can't find class[" +
				// className
				// + "]!" + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannelConfigParser.configCheck.class.notFound",
						new String[] { className, e.getMessage() }), e);
			}
		}
	}

	private void checkServerConn(ConnectionConfig connectionConfig) {
		// port
		if (connectionConfig.getPort() <= 0) {
			// throw new RuntimeException(fileName + ": RECEIVE Connection["
			// + connectionConfig.getId()
			// + "] must have 'port' attribute!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.receiveConnection.needPort",
					new String[] { fileName, connectionConfig.getId() }));
		}

	}

	private void checkClientConn(ConnectionConfig connectionConfig) {
		// server-address
		String ip = connectionConfig.getServerAddress();
		if (ip == null) {
			// throw new RuntimeException(fileName + ": SEND Connection["
			// + connectionConfig.getId()
			// + "] must have 'server-address' attribute!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.sendConnection.needServerAddress",
					new String[] { fileName, connectionConfig.getId() }));
		}
		try {
			InetAddress.getAllByName(ip);
		} catch (UnknownHostException e) {
			// throw new RuntimeException(fileName + ": SEND Connection["
			// + connectionConfig.getId() + "] 's 'server-address'["
			// + ip + "] is wrong! :" + e.getMessage(), e);
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.sendConnection.serverAddress.wrong",
					new String[] { fileName, connectionConfig.getId(), ip, e.getMessage() }), e);
		}

		// port
		if (connectionConfig.getPort() <= 0) {
			// throw new RuntimeException(fileName + ": SEND Connection["
			// + connectionConfig.getId()
			// + "] must have 'port' attribute!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
					"LongConnectionSocketChannelConfigParser.configCheck.sendConnection.needPort",
					new String[] { fileName, connectionConfig.getId() }));
		}

	}

	public LongConnectionSocketChannelConfig parse(InputStream in) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			parser.parse(in, this);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		}
		// 解析后检查
		configCheck();
		return config;
	}

	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		// System.out.println(name);
		if ("long-connection-socket-channel".equals(name)) {
			config = new LongConnectionSocketChannelConfig();
		} else if ("connection".equals(name)) {
			ConnectionConfig conn = new ConnectionConfig();

			// @id
			String attr = attributes.getValue("id");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": connection/@id is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "connection/@id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": connection/@id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "connection/@id" }));
				}
				attr = getRealValue(attr);
			}
			if (config.getConnectionConfigs().containsKey(attr)) {
				// throw new RuntimeException(fileName + ": connection/@id["
				// + attr + "] is reduplicate!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("reduplicate",
						new String[] { fileName + ": connection/@id" }));
			}
			conn.setId(attr);

			// @type
			attr = attributes.getValue("type");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": connection/@type is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "connection/@type" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": connection/@type is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "connection/@type" }));
				}
				attr = getRealValue(attr);
			}
			conn.setType(ConnectionConfig.getTypeByText(attr));

			// @direction
			attr = attributes.getValue("direction");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": connection/@direction is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "connection/@direction" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": connection/@direction is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "connection/@direction" }));
				}
				attr = getRealValue(attr);
			}
			conn.setDirection(ConnectionConfig.getDirectionByText(attr));

			// @server-address
			attr = attributes.getValue("server-address");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": connection/@server-address is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
							new String[] { fileName, "connection/@server-address" }));
				}
				conn.setServerAddressString(attr);
				attr = getRealValue(attr);
				conn.setServerAddress(attr);
			}

			// @port
			attr = attributes.getValue("port");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": connection/@port is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "connection/@port" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": connection/@port is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "connection/@port" }));
				}
				conn.setPortString(attr);
				attr = getRealValue(attr);
			}
			conn.setPort(Integer.parseInt(attr));

			// @local-port
			attr = attributes.getValue("local-port");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "connection/@local-port" }));
				}
				conn.setLocalPortString(attr);
				attr = getRealValue(attr);
				conn.setLocalPort(Integer.parseInt(attr));
			}

			// @local-server-address
			attr = attributes.getValue("local-server-address");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "connection/@local-server-address" }));
				}
				conn.setLocalServerAddressString(attr);
				attr = getRealValue(attr);
				conn.setLocalServerAddress(attr);
			}

			// add to config
			config.setConnectionConfig(conn.getId(), conn);

		} else if ("heartbeat-message".equals(name)) {
			HeartbeatConfig heartbeat = new HeartbeatConfig();

			// @connection-id
			String attr = attributes.getValue("connection-id");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": heartbeat-message/@connection-id is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "heartbeat-message/@connection-id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// fileName
					// + ": heartbeat-message/@connection-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@connection-id" }));
				}
				attr = getRealValue(attr);
			}
			heartbeat.setConnectionId(attr);

			// @direction
			attr = attributes.getValue("direction");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": heartbeat-message/@direction is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "heartbeat-message/@direction" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": heartbeat-message/@direction is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@direction" }));
				}
				attr = getRealValue(attr);
			}
			heartbeat.setDirection(ConnectionConfig.getDirectionByText(attr));

			// @interval
			attr = attributes.getValue("interval");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": heartbeat-message/@interval is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@interval" }));
				}
				attr = getRealValue(attr);
				heartbeat.setInterval(Long.parseLong(attr));
			}

			// @message-symbol
			attr = attributes.getValue("message-symbol");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": heartbeat-message/@message is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@message-symbol" }));
				}
				attr = getRealValue(attr);
				heartbeat.setMessageSymbolId(attr);
			}

			// hexStrCheck(attr);
			// heartbeat.setMessage(CodeUtil.HextoByte(attr));

			// @response-message-symbol
			attr = attributes.getValue("response-message-symbol");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// fileName
					// + ": heartbeat-message/@response-message is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@response-message-symbol" }));
				}
				attr = getRealValue(attr);
				heartbeat.setResponseMessageSymbolId(attr);
			}

			// @response-connection-id
			attr = attributes.getValue("response-connection-id");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// fileName
					// +
					// ": heartbeat-message/@response-connection-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@response-connection-id" }));
				}
				attr = getRealValue(attr);
				heartbeat.setResponseConnectionId(attr);
			}

			// response-turn-back
			attr = attributes.getValue("response-turn-back");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "heartbeat-message/@response-turn-back" }));
				}

				attr = getRealValue(attr);

				if ("true".equals(attr)) {
					heartbeat.setResponseTurnBack(true);
				}
			}

			// add to config
			config.getHeartbeatConfigs().add(heartbeat);

		} else if ("reader".equals(name)) {
			ReaderConfig readerConfig = new ReaderConfig();
			config.setReaderConfig(readerConfig);

			currentParamOwner = readerConfig;

			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": reader/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "reader/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": reader/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "reader/@class" }));
				}
				attr = getRealValue(attr);
			}
			readerConfig.setClassName(attr);
		} else if ("writer".equals(name)) {
			WriterConfig writerConfig = new WriterConfig();
			config.setWriterConfig(writerConfig);

			currentParamOwner = writerConfig;

			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": writer/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "writer/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": writer/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "writer/@class" }));
				}
				attr = getRealValue(attr);
			}
			writerConfig.setClassName(attr);
			// } else if ("message-helper".equals(name)) {
			// MessageHelperConfig messageHelperConfig = new
			// MessageHelperConfig();
			// config.setMessageHelperConfig(messageHelperConfig);
			//
			// currentParamOwner = messageHelperConfig;
			//
			// // @class
			// String attr = attributes.getValue("class");
			// if (null == attr) {
			// throw new RuntimeException(this.fileName
			// + ": message-helper/@class is Null!");
			// } else {
			// attr = attr.trim();
			// if (0 == attr.length()) {
			// throw new RuntimeException(this.fileName
			// + ": message-helper/@class is Blank!");
			// }
			// }
			// messageHelperConfig.setClassName(attr);
		} else if ("login".equals(name)) {
			LoginConfig loginConfig = new LoginConfig();
			config.setLoginConfig(loginConfig);

			currentParamOwner = loginConfig;

			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": login/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "login/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": login/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "login/@class" }));
				}
				attr = getRealValue(attr);
			}
			loginConfig.setClassName(attr);
		} else if ("parameter".equals(name)) {
			// @name
			String attr = attributes.getValue("name");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": parameter/@name is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "parameter/@name" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": parameter/@name is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "parameter/@name" }));
				}
				attr = getRealValue(attr);
			}
			currentParameter = attr;

			// currentParamOwner.setParameter(attr, currentParameter);
		} else if ("filter".equals(name)) {
			FilterConfig filterConfig = new FilterConfig();
			// @class
			String attr = attributes.getValue("class");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": filter/@class is Null!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "filter/@class" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": filter/@class is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "filter/@class" }));
				}
				attr = getRealValue(attr);
			}
			filterConfig.setClassName(attr);
			if (currentParamOwner instanceof ReaderConfig) {
				((ReaderConfig) currentParamOwner).getFilterConfigList().add(filterConfig);
			} else {
				((WriterConfig) currentParamOwner).getFilterConfigList().add(filterConfig);
			}
		} else if ("request-serial-number-recognizer".equals(name)) {
			String attr = attributes.getValue("recognizer-id");
			if (null == attr) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": request-serial-number-recognizer/@recognizer-id is NULL!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "request-serial-number-recognizer/@recognizer-id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// fileName
					// +
					// ": request-serial-number-recognizer/@recognizer-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "request-serial-number-recognizer/@recognizer-id" }));
				}
				attr = getRealValue(attr);
			}
			config.setRequestSerialNumberRecognizerId(attr);
		} else if ("response-serial-number-recognizer".equals(name)) {
			String attr = attributes.getValue("recognizer-id");
			if (null == attr) {
				// throw new RuntimeException(
				// fileName
				// +
				// ": response-serial-number-recognizer/@recognizer-id is NULL!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "response-serial-number-recognizer/@recognizer-id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(
					// fileName
					// +
					// ": response-serial-number-recognizer/@recognizer-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "response-serial-number-recognizer/@recognizer-id" }));
				}
				attr = getRealValue(attr);
			}
			config.setResponseSerialNumberRecognizerId(attr);
		} else if ("code-recognizer".equals(name)) {
			CodeRecognizerConfig codeRecognizerConfig = new CodeRecognizerConfig();
			config.setCodeRecognizerConfig(codeRecognizerConfig);

			// @recognizer-id
			String attr = attributes.getValue("recognizer-id");
			if (null == attr) {
				// throw new RuntimeException(fileName
				// + ": code-recognizer/@recognizer-id is NULL!");
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "code-recognizer/@recognizer-id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					// throw new RuntimeException(fileName
					// + ": code-recognizer/@recognizer-id is Blank!");
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "code-recognizer/@recognizer-id" }));
				}
				attr = getRealValue(attr);
			}
			codeRecognizerConfig.setRecognizerId(attr);
		} else if ("request-code-set".equals(name)) {
			currentCodeSet = config.getCodeRecognizerConfig().getRequestCodeSet();
		} else if ("response-code-set".equals(name)) {
			currentCodeSet = config.getCodeRecognizerConfig().getResponseCodeSet();
		} else if ("message-symbol".equals(name)) {
			MessageSymbol messageSymbol = new MessageSymbol();
			// @id
			String attr = attributes.getValue("id");
			if (null == attr) {
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.null",
						new String[] { fileName, "message-symbol-table/message-symbol/@id" }));
			} else {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "message-symbol-table/message-symbol/@id" }));
				}
				attr = getRealValue(attr);
			}
			messageSymbol.setId(attr);

			// @type
			attr = attributes.getValue("type");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "message-symbol-table/message-symbol/@type" }));
				}
				attr = getRealValue(attr);
				messageSymbol.setType(MessageSymbol.getTypeByText(attr));
			}

			// @data-type
			attr = attributes.getValue("data-type");
			if (null != attr) {
				attr = attr.trim();
				if (0 == attr.length()) {
					throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString("config.blank",
							new String[] { fileName, "message-symbol-table/message-symbol/@data-type" }));
				}
				attr = getRealValue(attr);
				messageSymbol.setDataType(MessageSymbol.getDataTypeByText(attr));
			}

			config.getMessageSymbolTable().put(messageSymbol.getId(), messageSymbol);
			currentParamOwner = messageSymbol;
		}
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		if ("reader".equals(name)) {
			currentParamOwner = null;
		} else if ("writer".equals(name)) {
			currentParamOwner = null;
		} else if ("message-helper".equals(name)) {
			currentParamOwner = null;
		} else if ("parameter".equals(name)) {
			// currentParameter.setValue(elementValue);
			String parameter = elementValue;
			if (parameter != null) {
				parameter = parameter.trim();
				if (0 == parameter.length()) {
					parameter = null;
				}
			}
			((DynamicObjectConfig) currentParamOwner).setParameter(currentParameter, parameter);
			currentParameter = null;
		} else if ("code".equals(name)) {
			String code = elementValue;
			if (null != code) {
				code = code.trim();
				if (0 < code.length()) {
					currentCodeSet.add(getRealValue(code));
				}
			}
		} else if ("message-symbol".equals(name)) {
			String value = elementValue;
			if (null != value) {
				value = value.trim();
				if (0 == value.length()) {
					value = null;
				} else {
					value = getRealValue(value);
				}
				((MessageSymbol) currentParamOwner).setValue(value);
			}
		}
		elementValue = null;

	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		// System.out.println("elementValue="+elementValue);
		if (null == elementValue) {
			elementValue = new String(ch, start, length);
		} else {
			elementValue += new String(ch, start, length);
		}
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

	private String elementValue = null;
	private Object currentParamOwner = null;
	private String currentParameter = null;
	private Set currentCodeSet = null;

	private AbstractMessageRecognizer createRecognizer(RecognizerConfig recognizerConfig) {
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
