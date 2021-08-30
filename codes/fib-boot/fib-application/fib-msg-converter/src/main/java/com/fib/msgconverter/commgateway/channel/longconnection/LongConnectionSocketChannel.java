package com.fib.msgconverter.commgateway.channel.longconnection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.ConnectorConfig;
import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.HeartbeatConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.LongConnectionSocketChannelConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.LongConnectionSocketChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.longconnection.config.MessageSymbol;
import com.fib.msgconverter.commgateway.channel.longconnection.config.database.LongConnectionSocketChannelConfigLoader;
import com.fib.msgconverter.commgateway.channel.longconnection.login.AbstractLogin;
import com.fib.msgconverter.commgateway.channel.longconnection.reader.AbstractReader;
import com.fib.msgconverter.commgateway.channel.longconnection.writer.AbstractWriter;
import com.fib.msgconverter.commgateway.module.impl.ExceptionMonitor;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 长连接通道
 * 
 * @author 刘恭亮
 * 
 */
public class LongConnectionSocketChannel extends Channel {
	public static final String EXCEPTIONIP = "IP";
	public static final String EXCEPTIONPORT = "PORT";
	/**
	 * 服务类型的连接所允许的最大连接数
	 */
	public static final int SERVER_CONN_BACKLOG = 1;
	/**
	 * 配置
	 */
	protected LongConnectionSocketChannelConfig config = null;

	/**
	 * 系统运行标志
	 */
	private boolean run = false;

	// /**
	// * 接收队列
	// */
	// private List receiveQueue = new LinkedList();

	/**
	 * 发送队列
	 */
	private List sendQueue = new LinkedList();

	/**
	 * 等待队列
	 */
	private Map waitQueue = new HashMap(1024);

	/**
	 * 接收线程
	 */
	private ReceiveHandler receiveHandler = null;

	/**
	 * 接收报文的连接
	 */
	private Connection receiveConnection = null;

	/**
	 * 接收器
	 */
	private AbstractReader reader = null;

	/**
	 * 发送线程
	 */
	private SendHandler sendHandler = null;

	/**
	 * 发送报文的连接
	 */
	private Connection sendConnection = null;

	/**
	 * 发送器
	 */
	private AbstractWriter writer = null;

	/**
	 * 连接监控线程
	 */
	private ConnectionMonitor monitor = null;

	/**
	 * 应答接收超时状态监控线程
	 */
	private WaitQueueMonitor waitQueueMonitor = null;

	/**
	 * 连接状态
	 */
	private ConnectionState state = null;

	/**
	 * 登陆程序
	 */
	private AbstractLogin login = null;

	/**
	 * 长连接通道类型，判断是做为接收通道还是发送通道还是双向通道
	 */
	private int type = ConnectorConfig.TYP_DOUBLE;

	/**
	 * 心跳包发送线程
	 */
	private HeartbeatSendHandler heartbeatSendHandler = null;

	/**
	 * 心跳包接送监控线程
	 */
	private HeartbeatReceiveMonitor heartbeatReceiveMonitor = null;

	/**
	 * 通道启动时间
	 */
	private long startTime;

	public void closeSource(Object source) {
		// do nothing
	}

	public void loadConfig(InputStream in) {
		if (CommGateway.isConfigDBSupport()) {
			LongConnectionSocketChannelConfigLoader loader = new LongConnectionSocketChannelConfigLoader();
			config = loader.loadConfig(channelConfig.getConnectorId());
		} else {
			LongConnectionSocketChannelConfigParser parser = new LongConnectionSocketChannelConfigParser();
			parser.setChannelConfig(channelConfig);
			parser.setMainConfig(mainConfig);
			config = parser.parse(in);
		}
	}

	public void sendRequestMessage(byte[] requestMessage, boolean async, int timeout) {
		if (!run || !state.isNormal() || !sendConnection.isConnect() || !receiveConnection.isConnect()) {
			// onRequestMessageSendException(connections, requestMessage,
			// new RuntimeException("Connection isn't available"));
			onRequestMessageSendException(connections, requestMessage, new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("LongConnectionSocketChannel.connect.invalidate")));
			return;
		}

		// 取索引号，登记
		String requestReferenceNumber = config.getRequestSerialNumberRecognizer().recognize(requestMessage);
		SendMessage sm = new SendMessage();
		sm.type = SendMessage.MSG_TYP_REQ;
		sm.message = requestMessage;
		sm.timeout = timeout;
		sm.startTime = System.currentTimeMillis();
		synchronized (waitQueue) {
			waitQueue.put(requestReferenceNumber, sm);
		}

		// 放入发送队列
		synchronized (sendQueue) {
			sendQueue.add(sm);
			// 通知发送线程
			sendQueue.notify();
		}

	}

	protected boolean isRequestMessage(byte[] message) {
		String code = config.getCodeRecognizer().recognize(message);
		Set codeSet = config.getCodeRecognizerConfig().getRequestCodeSet();

		boolean isRequest;
		if (null != codeSet) {
			if (codeSet.contains(code)) {
				isRequest = true;
			} else {
				isRequest = false;
			}
		} else {
			codeSet = config.getCodeRecognizerConfig().getResponseCodeSet();
			if (codeSet.contains(code)) {
				isRequest = false;
			} else {
				isRequest = true;
			}
		}

		return isRequest;
	}

	private class SendMessage {
		// 请求报文
		public static final int MSG_TYP_REQ = 0x31;
		// 应答报文
		public static final int MSG_TYP_RES = 0x32;
		// 探测报文（心跳包）
		public static final int MSG_TYP_TEST = 0x33;

		public int type;
		public byte[] message;
		public long timeout;
		public long startTime;
	}

	public void sendResponseMessage(byte[] responseMessage) {
		if (!run || !state.isNormal() || !sendConnection.isConnect() || !receiveConnection.isConnect()) {
			// onResponseMessageSendException(connections, responseMessage,
			// new RuntimeException("Connection isn't available"));
			onResponseMessageSendException(connections, responseMessage,
					new RuntimeException(MultiLanguageResourceBundle.getInstance()
							.getString("LongConnectionSocketChannel.connect.invalidate")));
			return;
		}

		// 放入发送队列
		synchronized (sendQueue) {
			SendMessage sm = new SendMessage();
			sm.type = SendMessage.MSG_TYP_RES;
			sm.message = responseMessage;
			sendQueue.add(sm);
			// 通知发送线程
			sendQueue.notify();
		}

	}

	public void shutdown() {
		// 1. internalShutdown
		internalShutdown();

		// 2. stop monitor
		if (monitor != null) {
			monitor.setRunFlag(false);
			try {
				monitor.interrupt();
				monitor.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			monitor = null;
		}

		if (null != heartbeatReceiveMonitor) {
			heartbeatReceiveMonitor.setRunFlag(false);
			try {
				heartbeatReceiveMonitor.interrupt();
				heartbeatReceiveMonitor.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			heartbeatReceiveMonitor = null;
		}

		if (null != heartbeatSendHandler) {
			heartbeatSendHandler.setRunFlag(false);
			try {
				heartbeatSendHandler.interrupt();
				heartbeatSendHandler.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			heartbeatSendHandler = null;
		}
	}

	/**
	 * 内部关闭：连接和Handler
	 */
	private void internalShutdown() {
		// 1. set run flag
		run = false;

		// 2. close all Connection
		if (connections != null) {
			Connection conn = null;
			Iterator it = connections.iterator();
			while (it.hasNext()) {
				conn = (Connection) it.next();
				conn.close();
			}
			connections = null;
		}

		// 3. stop handler
		if (null != sendHandler) {
			try {
				sendHandler.interrupt();
				sendHandler.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			sendHandler = null;
		}
		if (null != receiveHandler) {
			try {
				receiveHandler.interrupt();
				receiveHandler.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			receiveHandler = null;
		}

		if (null != heartbeatSendHandler) {
			try {
				heartbeatSendHandler.interrupt();
				heartbeatSendHandler.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			heartbeatSendHandler = null;
		}

		if (null != waitQueueMonitor) {
			try {
				waitQueueMonitor.interrupt();
				waitQueueMonitor.join(6000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			waitQueueMonitor = null;
		}
	}

	public void start() {
		type = super.channelConfig.getMode();
		// 1. reader & writer
		reader = (AbstractReader) ClassUtil.createClassInstance(config.getReaderConfig().getClassName());
		reader.setLogger(logger);
		reader.setParameters(config.getReaderConfig().getParameters());
		reader.setFilterList(config.getReaderConfig().createFilterList());

		writer = (AbstractWriter) ClassUtil.createClassInstance(config.getWriterConfig().getClassName());
		// 2. 设置接收通道心跳包
		List heartbeatConfigs = config.getHeartbeatConfigs();
		for (int i = 0; i < heartbeatConfigs.size(); i++) {
			HeartbeatConfig heartbeatConfig = (HeartbeatConfig) heartbeatConfigs.get(i);
			if (ConnectionConfig.COMM_DIRECTION_RECEIVE == heartbeatConfig.getDirection()) {
				reader.setHeartbeatConfig(heartbeatConfig);
				writer.setResponseHeartbeatConfig(heartbeatConfig);
				if (heartbeatConfig.getInterval() > 0) {
					heartbeatReceiveMonitor = new HeartbeatReceiveMonitor();
					heartbeatReceiveMonitor.interval = heartbeatConfig.getInterval();
				}
			}
		}
		writer.setLogger(logger);
		writer.setParameters(config.getWriterConfig().getParameters());
		writer.setFilterList(config.getWriterConfig().createFilterList());
		// 3. login
		login = (AbstractLogin) ClassUtil.createClassInstance(config.getLoginConfig().getClassName());
		login.setParameters(config.getLoginConfig().getParameters());
		login.setLogger(logger);

		// 4. 启动连接监视器
		state = new ConnectionState();
		monitor = new ConnectionMonitor();
		monitor.start();

		// 5. 内部启动：连接和Handler
		internalStart();

	}

	private Map contextMap = new HashMap();

	/**
	 * 内部启动：连接和Handler
	 */
	private void internalStart() {
		// 1. Connection & Stream
		connections = new Connections();
		Connection conn = null;
		ConnectionConfig conf = null;
		List heartbeatConfigs = config.getHeartbeatConfigs();
		Iterator it = config.getConnectionConfigs().values().iterator();
		while (it.hasNext()) {
			conf = (ConnectionConfig) it.next();
			if (ConnectionConfig.CONN_TYP_CLIENT == conf.getType()) {
				conn = new ClientConnection();
				((ClientConnection) conn).setConnId(getId() + "_" + conf.getId());
			} else {
				conn = new ServerConnection();
			}
			conn.setId(conf.getId());
			conn.setConf(conf);
			conn.connect();

			// register
			connections.setConnection(conn.getId(), conn);

			// Streams
			if (conf.getDirection() == ConnectionConfig.COMM_DIRECTION_RECEIVE) {
				// 双Socket接收方
				// receiveStream = conn.getIs();
				receiveConnection = conn;
			} else if (conf.getDirection() == ConnectionConfig.COMM_DIRECTION_SEND) {
				// 双Socket发送方
				// sendStream = conn.getOs();
				sendConnection = conn;
			} else {
				// 单Socket双向
				// receiveStream = conn.getIs();
				// sendStream = conn.getOs();
				receiveConnection = conn;
				sendConnection = conn;
			}

		}

		// 2. login
		try {
			if (!login.login(sendConnection, receiveConnection)) {
				// onFatalException(login, new
				// RuntimeException("login failed!"));
				onFatalException(login, new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"LongConnectionSocketChannel.internalStart.login.failed.1", new String[] { getId() })));
				return;
			} else {
				if (logger.isInfoEnabled()) {
					logger.info(MultiLanguageResourceBundle.getInstance()
							.getString("LongConnectionSocketChannel.internalStart.login.success"));
				}
			}
		} catch (Exception excp) {
			// onFatalException(login, new RuntimeException("login failed!",
			// excp));
			onFatalException(login,
					new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannel.internalStart.login.failed.2",
							new String[] { getId(), excp.getMessage() }), excp));
			return;
		}

		// 3. run
		run = true;
		startTime = System.currentTimeMillis();

		// 4. handlers
		receiveHandler = new ReceiveHandler();
		receiveHandler.start();
		sendHandler = new SendHandler();
		sendHandler.start();
		// 5. waitQueueMonitor
		if (ChannelConfig.MODE_SERVER != channelConfig.getMode()
				&& (null == waitQueueMonitor || !waitQueueMonitor.isAlive())) {
			// 发送或双向通道
			// 启动应答超时状态监控器
			waitQueueMonitor = new WaitQueueMonitor();
			waitQueueMonitor.start();
		}
		// 6. heartbeanSendHandler
		HeartbeatConfig heartbeatConfig = null;
		for (int i = 0; i < heartbeatConfigs.size(); i++) {
			heartbeatConfig = (HeartbeatConfig) heartbeatConfigs.get(i);
			if (ConnectionConfig.COMM_DIRECTION_SEND == heartbeatConfig.getDirection()
					&& sendConnection.getId().equals(heartbeatConfig.getConnectionId())) {
				break;
			}
			heartbeatConfig = null;
		}
		if (null != heartbeatConfig && (null == heartbeatSendHandler || !heartbeatSendHandler.isAlive())) {
			heartbeatSendHandler = new HeartbeatSendHandler();
			heartbeatSendHandler.interval = heartbeatConfig.getInterval();
			heartbeatSendHandler.messageSymbol = heartbeatConfig.getMessageSymbol();
			writer.setHeartbeatConfig(heartbeatConfig);
			heartbeatSendHandler.connectionId = sendConnection.getId();
			heartbeatSendHandler.start();
		}
		if (null != heartbeatReceiveMonitor && !heartbeatReceiveMonitor.isAlive()) {
			heartbeatReceiveMonitor.start();
		}
	}

	private Connections connections = null;

	private class Connections {
		private Map cache = new HashMap();

		public String toString() {
			String NEW_LINE = System.getProperty("line.separator");
			StringBuffer buf = new StringBuffer(1024);
			buf.append("******** Connections ******** ");
			buf.append(NEW_LINE);
			Iterator it = iterator();
			while (it.hasNext()) {
				buf.append(((Connection) it.next()).toString());
				buf.append(NEW_LINE);
			}
			return buf.toString();
		}

		public Connection getConnection(String id) {
			return (Connection) cache.get(id);
		}

		public void setConnection(String id, Connection conn) {
			cache.put(id, conn);
		}

		public Iterator iterator() {
			return cache.values().iterator();
		}

		public void clear() {
			cache.clear();
		}

		public void close() {
			Iterator it = iterator();
			while (it.hasNext()) {
				((Connection) it.next()).close();
			}
		}
	}

	private class ClientConnection extends Connection {
		private String connId;
		private String dumpIp;
		private int port;

		public void setConnId(String connId) {
			this.connId = connId;
		}

		public String toString() {
			// StringBuffer buf = new StringBuffer(1024);
			// buf.append("ClientConnection : id[");
			// buf.append(id);
			// buf.append("] connect[");
			// buf.append(connect);
			// buf.append("] direction[");
			// buf.append(ConnectionConfig.getDirectionText(conf.getDirection()))
			// ;
			// buf.append("] address[");
			// buf.append(conf.getServerAddress());
			// buf.append(":");
			// buf.append(conf.getPort());
			// buf.append("]");
			// return buf.toString();
			return MultiLanguageResourceBundle.getInstance().getString("ClientConnection.toString",
					new String[] { id, "" + connect, ConnectionConfig.getDirectionText(conf.getDirection()),
							conf.getServerAddress(), "" + conf.getPort() });
		}

		public void connect() {
			// 启动一个连接线程不停的尝试连接
			Connector c = new Connector();
			c.start();
		}

		private class Connector extends Thread {

			public void run() {
				// 重复连，直到连接上

				if (CommGateway.isExceptionMonitorSupport()) {

					contextMap.put(this, connId);
					contextMap.put(connId + EXCEPTIONIP, conf.getServerAddress());
					contextMap.put(connId + EXCEPTIONPORT, conf.getPort());
				}
				ClientConnection.this.connect = false;

				while (true) {
					try {
						if (null != conf.getLocalServerAddress()) {
							socket = new Socket(conf.getServerAddress(), conf.getPort(),
									InetAddress.getByName(conf.getLocalServerAddress()), conf.getLocalPort());
						} else {
							ClientConnection.this.socket = new Socket(ClientConnection.this.conf.getServerAddress(),
									ClientConnection.this.conf.getPort());
						}
						ClientConnection.this.is = ClientConnection.this.socket.getInputStream();
						ClientConnection.this.os = ClientConnection.this.socket.getOutputStream();
						break;
					} catch (IOException e) {
						// e.printStackTrace();
						close();
						if (CommGateway.isExceptionMonitorSupport()) {
							ExceptionMonitor.dump(connId, e);
						}
						// ExceptionUtil.throwActualException(e);
						try {
							Thread.currentThread().sleep(5000);
						} catch (InterruptedException e1) {
							// e1.printStackTrace();
						}
					}
				}

				ClientConnection.this.connect = true;
				connectedTime = System.currentTimeMillis();
				if (logger.isInfoEnabled()) {
					logger.info(MultiLanguageResourceBundle.getInstance()
							.getString("LongConnectionSocketChannel.clientConnection.connected", new String[] {
									socket.toString(),
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(connectedTime)) }));
				}
			}
		}

	}

	private class ServerConnection extends Connection {
		private ServerSocket server = null;
		private boolean needRestart = false;

		public String toString() {
			StringBuffer buf = new StringBuffer(1024);
			// buf.append("ServerConnection : id[");
			// buf.append(id);
			// buf.append("] connect[");
			// buf.append(connect);
			// buf.append("] direction[");
			// buf.append(ConnectionConfig.getDirectionText(conf.getDirection()))
			// ;
			// buf.append("] listenPort[");
			// buf.append(conf.getPort());
			// buf.append("]");
			buf.append(MultiLanguageResourceBundle.getInstance().getString("ServerConnection.toString", new String[] {
					id, "" + connect, ConnectionConfig.getDirectionText(conf.getDirection()), "" + conf.getPort() }));
			if (socket != null) {
				// buf.append(" remote[");
				// buf.append(socket);
				// buf.append("]");
				buf.append(MultiLanguageResourceBundle.getInstance().getString("ServerConnection.toString.socket",
						new String[] { "" + socket }));
			}
			return buf.toString();
		}

		public void connect() {
			needRestart = false;
			// 启动一个接收器，直到接收到一个连接
			Listener listener = new Listener();
			listener.start();
		}

		private class Listener extends Thread {
			public void run() {
				connect = false;
				try {
					server = new ServerSocket(ServerConnection.this.conf.getPort(),
							ServerConnection.this.conf.getBacklog());
				} catch (IOException e) {
					// e.printStackTrace();
					close();
					onFatalException(this, e);
					return;
				}

				while (!needRestart) {
					try {
						socket = server.accept();
						is = socket.getInputStream();
						os = socket.getOutputStream();
					} catch (IOException e) {
						// e.printStackTrace();
						ServerConnection.super.close();
						// onException(this, e);
						continue;
					}
					break;
				}

				connect = true;
				connectedTime = System.currentTimeMillis();
				if (logger.isInfoEnabled()) {
					logger.info(MultiLanguageResourceBundle.getInstance()
							.getString("LongConnectionSocketChannel.serverConnection.connected", new String[] {
									socket.toString(),
									new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date(connectedTime)) }));
				}
			}
		}

		public void close() {
			needRestart = true;
			super.close();
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}

	}

	// /**
	// * 长连接主线程
	// * @author 刘恭亮
	// *
	// */
	// private class LongConnectionMainThread extends Thread{

	/**
	 * 重置内部资源：连接和Handler
	 */
	private void internalRestart() {
		internalShutdown();
		internalStart();
	}

	/**
	 * 接收线程
	 * 
	 * @author liugl
	 * 
	 */
	private class ReceiveHandler extends Thread {
		/**
		 * 接收连接最后活动时间
		 */
		private long lastReceiveAlive = 0;
		// 等待通道重建标志，当发送通道异常时由SendHandler设置
		boolean waitChannelReset = false;

		public void setWaitChannelReset() {
			waitChannelReset = true;
		}

		public void run() {
			byte[] message;
			while (run) {
				try {
					message = null;
					if (null == reader || !receiveConnection.isConnect()) {
						try {
							Thread.currentThread().sleep(5000);
						} catch (InterruptedException e) {
							// e.printStackTrace();
						}
						continue;
					}

					try {
						message = reader.read(receiveConnection.getIs());
					} catch (Exception excp) {
						// excp.printStackTrace();
						logger.error(MultiLanguageResourceBundle.getInstance().getString(
								"LongConnectionSocketChannel.reader.read.failed", new String[] { excp.getMessage() }),
								excp);
						// 报告通道状态
						synchronized (state) {
							if (!waitChannelReset) {
								// 1. 告知发送线程通道即将重建
								sendHandler.setWaitChannelReset();

								// 2. 报告通道状态不正常
								state.setState(false);

								// 3. 通知通道监控线程处理
								state.notify();
							}
						}
						return;
					}

					if (null != message) {
						lastReceiveAlive = System.currentTimeMillis();
						// 1. 判断是否心跳包
						boolean isHeartbeat = reader.isHeartbeatMessage(message);
						if (isHeartbeat) {
							// 处理心跳包
							if (reader.getHeartbeatConfig().getResponseMessageSymbol() != null) {
								byte[] response = ((MessageSymbol) config.getMessageSymbolTable()
										.get(reader.getHeartbeatConfig().getResponseMessageSymbol().getId()))
												.determineMessage(message);
								// 有应答心跳包，则需确定发送通道
								if (reader.getHeartbeatConfig().getResponseConnectionId() != null) {
									// 取应答连接
									Connection responseConnection = connections
											.getConnection(reader.getHeartbeatConfig().getResponseConnectionId());
									if (ConnectionConfig.COMM_DIRECTION_RECEIVE == responseConnection.getConf()
											.getDirection()) {
										// 回应心跳包的连接为接收连接，则直接取其outputStream发送应答心跳包
										try {
											responseConnection.getOs().write(response);
										} catch (IOException e) {
											// e.printStackTrace();
											logger.error(MultiLanguageResourceBundle.getInstance().getString(
													"LongConnectionSocketChannel.reader.read.failed",
													new String[] { e.getMessage() }), e);
											// 报告通道状态
											synchronized (state) {
												if (!waitChannelReset) {
													// 1. 告知发送线程通道即将重建
													sendHandler.setWaitChannelReset();

													// 2. 报告通道状态不正常
													state.setState(false);

													// 3. 通知通道监控线程处理
													state.notify();
												}
											}
											return;
										}
										continue;
									}
								}

								// 无应答连接配置，或应答连接为发送连接或者是双向连接
								synchronized (sendQueue) {
									SendMessage sm = new SendMessage();
									sm.type = SendMessage.MSG_TYP_TEST;
									sm.message = response;
									sendQueue.add(sm);
									// 通知发送线程
									sendQueue.notify();
								}
							}

							continue;
						}

						// synchronized (receiveQueue) {
						// receiveQueue.add(message);
						//
						// // 通知一个消费线程
						// receiveQueue.notify();
						// }
						// 2. 识别是请求还是应答，分别处理
						if (ChannelConfig.MODE_CLIENT == type) {
							// 如果是应答报文到达，从报文中索引号，在等待队列中取得请求报文，产生应答到达事件
							String responseReferenceNumber = null;
							responseReferenceNumber = config.getResponseSerialNumberRecognizer().recognize(message);
							SendMessage sm = null;
							synchronized (waitQueue) {
								sm = (SendMessage) waitQueue.remove(responseReferenceNumber);
							}

							if (null == sm) {
								// logger
								// .error(
								// "\n****************** Unmatched Response Message ******************\n"
								// + "referenceNumber = ["
								// + responseReferenceNumber
								// + "]\n"
								// + "message =\n"
								// + new String(
								// CodeUtil
								// .Bytes2FormattedText(message))
								// +
								// "\n**************************************************************"
								// );
								logger.error(MultiLanguageResourceBundle.getInstance()
										.getString("ReceiveHandler.responseMessage.unmatched", new String[] {
												responseReferenceNumber, CodeUtil.Bytes2FormattedText(message) }));
								continue;
							}
							if (logger.isInfoEnabled()) {
								logger.info(MultiLanguageResourceBundle.getInstance().getString(
										"SocketClientChannel.handleKey.receiveResponse.success",
										new String[] { CodeUtil.Bytes2FormattedText(message) }));
							}
							onResponseMessageArrived(receiveConnection, sm.message, message);
						} else if (ChannelConfig.MODE_SERVER == type) {
							if (logger.isInfoEnabled()) {
								logger.info(MultiLanguageResourceBundle.getInstance().getString(
										"SocketServerChannel.handleKey.receiveRequest.success",
										new String[] { CodeUtil.Bytes2FormattedText(message) }));
							}
							onRequestMessageArrived(receiveConnection, message);
						} else {
							if (isRequestMessage(message)) {
								if (logger.isInfoEnabled()) {
									logger.info(MultiLanguageResourceBundle.getInstance().getString(
											"SocketServerChannel.handleKey.receiveRequest.success",
											new String[] { CodeUtil.Bytes2FormattedText(message) }));
								}
								// 如果是外系统请求到达，产生请求到达事件
								onRequestMessageArrived(receiveConnection, message);
							} else {
								// 如果是应答报文到达，从报文中索引号，在等待队列中取得请求报文，产生应答到达事件
								String responseReferenceNumber = config.getResponseSerialNumberRecognizer()
										.recognize(message);
								SendMessage sm = null;
								synchronized (waitQueue) {
									sm = (SendMessage) waitQueue.remove(responseReferenceNumber);
								}
								if (null == sm) {
									// logger
									// .error(
									// "\n****************** Unmatched Response Message ******************\n"
									// + "referenceNumber = ["
									// + responseReferenceNumber
									// + "]\n"
									// + "message =\n"
									// + new String(
									// CodeUtil
									// .Bytes2FormattedText(message))
									// +
									// "\n**************************************************************"
									// );
									logger.error(MultiLanguageResourceBundle.getInstance()
											.getString("ReceiveHandler.responseMessage.unmatched", new String[] {
													responseReferenceNumber, CodeUtil.Bytes2FormattedText(message) }));
									continue;
								}
								if (logger.isInfoEnabled()) {
									logger.info(MultiLanguageResourceBundle.getInstance().getString(
											"SocketClientChannel.handleKey.receiveResponse.success",
											new String[] { CodeUtil.Bytes2FormattedText(message) }));
								}
								onResponseMessageArrived(receiveConnection, sm.message, message);
							}
						}
					}
				} catch (Exception e) {
					logger.error(ExceptionUtil.getExceptionDetail(e));
				}
			}
		}

	}

	/**
	 * 发送线程
	 * 
	 * @author liugl
	 * 
	 */
	private class SendHandler extends Thread {
		// 等待通道重建标志，当接收通道异常时由ReceiveHandler设置
		boolean waitChannelReset = false;
		long lastSendTime = 0;

		public void setWaitChannelReset() {
			waitChannelReset = true;
		}

		public void run() {
			SendMessage sendMessage;
			while (run) {
				sendMessage = null;

				if (null == writer || !sendConnection.isConnect()) {
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
					continue;
				}

				synchronized (sendQueue) {
					if (0 == sendQueue.size()) {
						try {
							sendQueue.wait();
						} catch (InterruptedException e) {
							// e.printStackTrace();
						}
					}

					if (sendQueue.size() > 0) {
						sendMessage = (SendMessage) sendQueue.remove(0);
					} else {
						continue;
					}
				}
				if (logger.isInfoEnabled()) {
					if (SendMessage.MSG_TYP_REQ == sendMessage.type) {
						// 发送请求报文
						logger.info(MultiLanguageResourceBundle.getInstance().getString(
								"SocketClientChannel.handleKey.sendRequest.success",
								new String[] { CodeUtil.Bytes2FormattedText(sendMessage.message) }));

					} else if (SendMessage.MSG_TYP_RES == sendMessage.type) {
						// 产生应答报文发送成功事件
						logger.info(MultiLanguageResourceBundle.getInstance().getString(
								"SocketClientChannel.handleKey.sendRequest.success",
								new String[] { CodeUtil.Bytes2FormattedText(sendMessage.message) }));
					} else if (SendMessage.MSG_TYP_TEST == sendMessage.type) {
						// 心跳包
						logger.debug(MultiLanguageResourceBundle.getInstance().getString(
								"LongConnectionSocketChannel.sendHeartbeat.success",
								new String[] { CodeUtil.Bytes2FormattedText(sendMessage.message) }));
					}
				}

				try {
					writer.write(sendConnection.getOs(), sendMessage.message);
				} catch (Exception excp) {
					// excp.printStackTrace();
					logger.error(MultiLanguageResourceBundle.getInstance().getString(
							"LongConnectionSocketChannel.writer.write.failed", new String[] { excp.getMessage() }),
							excp);

					if (CommGateway.isExceptionMonitorSupport() && contextMap.containsKey(sendConnection)) {
						String transcationId = (String) contextMap.get(sendConnection);
						String dumpIp = (String) contextMap.get(transcationId + EXCEPTIONIP);
						int dumpPort = (Integer) contextMap.get(transcationId + EXCEPTIONPORT);
						if (SendMessage.MSG_TYP_REQ == sendMessage.type) {
							// 产生请求报文发送失败事件
							onRequestMessageSendException(sendConnection, sendMessage.message, excp, transcationId,
									dumpIp, dumpPort);
						} else if (SendMessage.MSG_TYP_RES == sendMessage.type) {
							// 产生应答报文发送失败事件
							onResponseMessageSendException(sendConnection, sendMessage.message, excp, transcationId,
									dumpIp, dumpPort);
						}

					} else {
						// 发送失败，产生失败事件
						if (SendMessage.MSG_TYP_REQ == sendMessage.type) {
							// 产生请求报文发送失败事件
							onRequestMessageSendException(sendConnection, sendMessage.message, excp);
						} else if (SendMessage.MSG_TYP_RES == sendMessage.type) {
							// 产生应答报文发送失败事件
							onResponseMessageSendException(sendConnection, sendMessage.message, excp);
						}
					}

					// 报告通道状态
					synchronized (state) {
						if (!waitChannelReset) {
							// 1. 告知接收线程通道即将重建
							receiveHandler.setWaitChannelReset();

							// 2. 报告通道状态不正常
							state.setState(false);

							// 3. 通知通道监控线程处理
							state.notify();
						}
					}

					return;

				}
				// lastSendTime = new Date().getTime();
				lastSendTime = System.currentTimeMillis();
				if (SendMessage.MSG_TYP_REQ == sendMessage.type) {
					// 产生请求报文发送成功事件
					onRequestMessageSent(sendConnection, sendMessage.message);
				} else if (SendMessage.MSG_TYP_RES == sendMessage.type) {
					// 产生应答报文发送成功事件
					onResponseMessageSent(sendConnection, sendMessage.message);
				} else if (SendMessage.MSG_TYP_TEST == sendMessage.type) {
					HeartbeatConfig heartbeatConfig = writer.getHeartbeatConfig();
					if (heartbeatConfig.isResponseTurnBack()) {
						byte[] responseMsg = heartbeatConfig.getResponseMessageSymbol().determineMessage();
						byte[] responseHeartbeat = new byte[responseMsg.length];
						int onceRead = 0;
						try {
							onceRead = sendConnection.getIs().read(responseHeartbeat);
							if (-1 == onceRead) {
								throw new RuntimeException(
										MultiLanguageResourceBundle.getInstance().getString("onceRead.-1"));
							}

							if (0 == onceRead) {
								throw new RuntimeException(
										MultiLanguageResourceBundle.getInstance().getString("onceRead.0"));
							}
						} catch (Exception e) {
							logger.error(MultiLanguageResourceBundle.getInstance().getString(
									"LongConnectionSocketChannel.receiveResponseHeartbeat.error",
									new String[] { e.getMessage() }));
							if (CommGateway.isExceptionMonitorSupport() && contextMap.containsKey(sendConnection)) {
								ExceptionMonitor.dump((String) contextMap.get(sendConnection), e);
							}
							// 报告通道状态
							synchronized (state) {
								if (!waitChannelReset) {
									// 1. 告知接收线程通道即将重建
									receiveHandler.setWaitChannelReset();

									// 2. 报告通道状态不正常
									state.setState(false);

									// 3. 通知通道监控线程处理
									state.notify();
								}
							}

							return;
						}

						logger.info(MultiLanguageResourceBundle.getInstance().getString(
								"LongConnectionSocketChannel.receiveResponseHeartbeat.success",
								new String[] { CodeUtil.Bytes2FormattedText(responseHeartbeat) }));
					}
				}
			}

		}
	}

	/**
	 * 连接监视器
	 * 
	 * @author liugl
	 * 
	 */
	private class ConnectionMonitor extends Thread {
		private boolean runFlag = true;

		public void setRunFlag(boolean runFlag) {
			this.runFlag = runFlag;
		}

		public void run() {
			while (runFlag) {
				synchronized (state) {
					while (state.isNormal()) {
						try {
							state.wait();
						} catch (InterruptedException e) {
							// e.printStackTrace();
							if (!runFlag) {
								return;
							}
						}
					}

					// 重建连接
					try {
						internalRestart();
					} catch (Exception e) {
						// e.printStackTrace();

						try {
							Thread.currentThread().sleep(5000);
						} catch (InterruptedException e1) {
							// e1.printStackTrace();
						}
						continue;
					}

					// 设置状态
					state.setState(true);

				}
			}

		}
	}

	/**
	 * 心跳包发送线程
	 * 
	 * @author Administrator
	 * 
	 */
	private class HeartbeatSendHandler extends Thread {
		public long interval;
		public String connectionId;
		public MessageSymbol messageSymbol;
		public boolean runFlag = true;

		public void setRunFlag(boolean isRun) {
			runFlag = isRun;
		}

		public void run() {
			Connection conn = connections.getConnection(connectionId);
			while (runFlag) {
				if (!conn.isConnect()) {
					try {
						Thread.currentThread().sleep(interval);
					} catch (Exception e) {
						// e.printStackTrace();
					}
					continue;
				}
				if (0 == sendHandler.lastSendTime) {
					try {
						Thread.currentThread().sleep(interval + startTime - conn.getConnectedTime());
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}
				long fromLastSend = System.currentTimeMillis() - sendHandler.lastSendTime;
				if (fromLastSend < interval) {
					try {
						Thread.currentThread().sleep(interval - fromLastSend);
					} catch (Exception e) {
						// e.printStackTrace();
					}
					continue;
				}
				// 发送心跳包
				synchronized (sendQueue) {
					SendMessage sm = new SendMessage();
					sm.type = SendMessage.MSG_TYP_TEST;
					sm.message = messageSymbol.determineMessage();
					sendQueue.add(sm);
					// 通知发送线程
					sendQueue.notify();
				}

				// 等待间隔时间
				try {
					Thread.currentThread().sleep(interval);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 连接状态
	 * 
	 * @author liugl
	 * 
	 */
	private class ConnectionState {
		boolean state = true;

		public boolean isNormal() {
			return state;
		}

		public void setState(boolean state) {
			this.state = state;
		}
	}

	/**
	 * 监控应答接收超时状态线程
	 * 
	 * @author baif
	 * 
	 */
	private class WaitQueueMonitor extends Thread {
		public void run() {
			while (run) {
				if (0 == waitQueue.size()) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				}

				if (0 == waitQueue.size()) {
					continue;
				}

				Set timeoutSet = new HashSet();
				synchronized (waitQueue) {
					Iterator iterator = waitQueue.keySet().iterator();
					Object key = null;
					SendMessage sm = null;
					while (iterator.hasNext()) {
						key = iterator.next();
						sm = (SendMessage) waitQueue.get(key);
						if (System.currentTimeMillis() - sm.startTime >= sm.timeout) {
							timeoutSet.add(key);
						}
					}

					iterator = timeoutSet.iterator();
					while (iterator.hasNext()) {
						key = iterator.next();
						sm = (SendMessage) waitQueue.remove(key);
						// 产生接收应答失败事件
						// onResponseMessageReceiveException(receiveConnection,
						// sm.message, new Exception(
						// "Receive Responese Message Timeout!"));
						onResponseMessageReceiveException(receiveConnection, sm.message, new RuntimeException(
								MultiLanguageResourceBundle.getInstance().getString("WaitQueueMonitor.timeout")));
					}
				}

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private class HeartbeatReceiveMonitor extends Thread {
		public long interval;
		public boolean runFlag = true;

		public void setRunFlag(boolean isRun) {
			runFlag = isRun;
		}

		public void run() {
			while (runFlag) {
				if (null == receiveConnection || !receiveConnection.isConnect() || null == receiveHandler
						|| receiveHandler.waitChannelReset) {
					try {
						sleep(interval);
					} catch (Exception e) {
						// e.printStackTrace();
					}
				} else {
					if (receiveHandler.lastReceiveAlive == 0) {
						try {
							sleep(interval);
						} catch (Exception e) {
							// e.printStackTrace();
						}
						receiveHandler.lastReceiveAlive = System.currentTimeMillis();
						continue;
					}
					long notAliveTime = System.currentTimeMillis() - receiveHandler.lastReceiveAlive;
					if (notAliveTime >= interval) {
						synchronized (state) {
							// 1. 告知接收线程通道即将重建
							receiveHandler.setWaitChannelReset();

							// 2. 报告通道状态不正常
							state.setState(false);

							// 3. 通知通道监控线程处理
							state.notify();
						}
					} else {
						try {
							sleep(interval - notAliveTime);
						} catch (Exception e) {
							// e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public LongConnectionSocketChannelConfig getConnectorConfig() {
		return config;
	}

	public void setConnectorConfig(LongConnectionSocketChannelConfig config) {
		this.config = config;
	}

}
