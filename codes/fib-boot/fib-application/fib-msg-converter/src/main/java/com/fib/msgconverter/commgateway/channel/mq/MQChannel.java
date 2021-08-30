package com.fib.msgconverter.commgateway.channel.mq;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.mq.config.MQChannelConfig;
import com.fib.msgconverter.commgateway.channel.mq.config.MQChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.mq.config.QueueConfig;
import com.fib.msgconverter.commgateway.channel.mq.config.database.MQChannelConfigLoader;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

public class MQChannel extends Channel {
	// utf-8
	private static final int CCSID = 1209;
	// MQ通道配置
	private MQChannelConfig config;
	// 发送队列
	private QueueConfig sendQueueConfig;
	// 接收队列
	private QueueConfig receiveQueueConfig;
	// 请求队列管理器-发送
	private MQQueueManager sendManager;
	// 请求队列-发送
	private MQQueue sendQueue;
	// 请求队列管理器-应答
	private MQQueueManager receiveManager;
	// 请求队列管理器-应答
	private MQQueue receiveQueue;
	// 发送线程
	private MQsendThread sender;
	// 应答线程
	private MQreceiveThread receiver;
	// 监控线程
	private MQmonitorThread monitor;

	// 发送队列
	private LinkedList sendList = new LinkedList();
	// 存储所有等待应答的requestMessage key=messageId的16进制字符串 value=ClientRequestInfo
	private Map waitMap = new HashMap(64);
	// 启动标志
	private boolean run = false;

	public void closeSource(Object source) {

	}

	public void loadConfig(InputStream in) {
		if (CommGateway.isConfigDBSupport()) {
			MQChannelConfigLoader loader = new MQChannelConfigLoader();
			config = loader.loadConfig(channelConfig.getConnectorId());
		} else {
			MQChannelConfigParser parser = new MQChannelConfigParser();
			parser.setChannelConfig(channelConfig);
			parser.setMainConfig(mainConfig);
			config = parser.parse(in);
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

	public void sendRequestMessage(byte[] requestMessage, boolean async,
			int timeout) {
		Message request = new Message();
		request.message = requestMessage;
		request.isAsync = async;
		if (-1 == config.getTimeout()) {
			request.timeout = timeout;
		} else {
			request.timeout = config.getTimeout();
		}
		synchronized (sendList) {
			sendList.add(request);
			sendList.notify();
		}
	}

	public void sendResponseMessage(byte[] responseMessage) {
		// throw new RuntimeException("MQClientChannel [" + getId()
		// + "] can NOT Send Response Message!");
		Session session = SessionManager.getSession(responseMessage);
		if( null == session){
			// 超时关闭
			return;
		}
		Message response = new Message();
		response.message = responseMessage;
		response.type = Message.TYPE_RES;
		response.messageId = (byte[]) session.getSource();

		synchronized (sendList) {
			sendList.add(response);
			sendList.notify();
		}
	}

	public void shutdown() {
		run = false;
		try {
			if (null != sender) {
				try {
					sender.interrupt();
					sender.join(5000);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				sender = null;
			}
			if (null != receiver) {
				try {
					receiver.interrupt();
					receiver.join(5000);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				receiver = null;
			}
			if (null != monitor) {
				try {
					monitor.interrupt();
					monitor.join(5000);
				} catch (Exception e) {
					// e.printStackTrace();
				}
				monitor = null;
			}

			if (null != sendQueue) {
				try {
					sendQueue.close();
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("Shut Down Send Queue ERROR", e);
					logger
							.error(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"MQChannel.shutDown.sendQueue.error"),
									e);
				}
				sendQueue = null;
			}
			if (null != sendManager) {
				try {
					sendManager.commit();
					sendManager.close();
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("Shut Down Send Manager ERROR", e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("MQChannel.shutDown.sendManager.error"),
							e);
				}
				sendManager = null;
			}
			if (null != receiveQueue) {
				try {
					receiveQueue.close();
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("Shut Down Receive Queue ERROR", e);
					logger
							.error(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"MQChannel.shutDown.receiveQueue.error"),
									e);
				}
				receiveQueue = null;
			}
			if (null != receiveManager) {
				try {
					receiveManager.commit();
					receiveManager.close();
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("Shut Down Receive Manager ERROR", e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString(
									"MQChannel.shutDown.receiveManager.error"),
							e);
				}
				receiveManager = null;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// logger.error("shut down mq channel error", e);
			logger.error(MultiLanguageResourceBundle.getInstance().getString(
					"MQChannel.shutDown.channel.error"), e);
		}
	}

	public static final String EXCEPTIONSEND = "SEND";
	public static final String EXCEPTIONRECV = "RECV";

	private String channelId;
	private String dumpIp;
	private int dumpPort;

	public void start() {
		if (null == config) {
			// throw new RuntimeException(
			// "Config is NULL,Please Load Config First");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString("MQChannel.start.config.null"));
		}
		channelId = getId();
		Hashtable map = new Hashtable();
		map.put(CMQC.CCSID_PROPERTY, config.getCcsid());
		map.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES);
		// 建立发送队列
		sendQueueConfig = getQueueConfigByType(MQChannelConfigParser.QUEUE_TYPE_SEND);
		if (null != sendQueueConfig) {
			if (CommGateway.isExceptionMonitorSupport()) {
				dumpIp = sendQueueConfig.getServerAddress();
				dumpPort = sendQueueConfig.getPort();
			}
			map
					.put(CMQC.HOST_NAME_PROPERTY, sendQueueConfig
							.getServerAddress());
			map.put(CMQC.PORT_PROPERTY, sendQueueConfig.getPort());
			map.put(CMQC.CHANNEL_PROPERTY, sendQueueConfig.getChannel());

			try {
				sendManager = new MQQueueManager(sendQueueConfig
						.getQueueManager(), map);
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.fatal("Connect to Send Queue Manager ERROR!", e);
				logger
						.fatal(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MQChannel.start.sendQueueManager.connect.error"),
								e);
				if (CommGateway.isExceptionMonitorSupport()) {
					onFatalException(sendManager, e, channelId + EXCEPTIONSEND);
				} else {
					onFatalException(sendManager, e);
				}
				shutdown();
				return;
			}
			try {
				sendQueue = sendManager.accessQueue(sendQueueConfig.getName(),
						CMQC.MQOO_OUTPUT);
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.fatal("Connect to Send Queue ERROR!", e);
				logger.fatal(MultiLanguageResourceBundle.getInstance()
						.getString("MQChannel.start.sendQueue.connect.error"),
						e);

				if (CommGateway.isExceptionMonitorSupport()) {
					onFatalException(sendQueue, e, channelId + EXCEPTIONSEND);
				} else {
					onFatalException(sendQueue, e);
				}
				shutdown();
				return;
			}
		}

		// 建立应答队列
		receiveQueueConfig = getQueueConfigByType(MQChannelConfigParser.QUEUE_TYPE_RECEIVE);
		if (null != receiveQueueConfig) {

			Hashtable resMap = new Hashtable();
			resMap.put(CMQC.CCSID_PROPERTY, config.getCcsid());
			resMap.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES);
			resMap.put(CMQC.HOST_NAME_PROPERTY, receiveQueueConfig
					.getServerAddress());
			resMap.put(CMQC.PORT_PROPERTY, receiveQueueConfig.getPort());
			resMap.put(CMQC.CHANNEL_PROPERTY, receiveQueueConfig.getChannel());

			try {
				receiveManager = new MQQueueManager(receiveQueueConfig
						.getQueueManager(), resMap);
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.fatal("Connect to Receive Queue Manager ERROR!", e);
				logger
						.fatal(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MQChannel.start.receiveQueueManager.connect.error"),
								e);

				if (CommGateway.isExceptionMonitorSupport()) {
					onFatalException(receiveManager, e, channelId
							+ EXCEPTIONRECV);
				} else {
					onFatalException(receiveManager, e);
				}
				shutdown();
				return;
			}
			try {
				receiveQueue = receiveManager.accessQueue(receiveQueueConfig
						.getName(), CMQC.MQOO_INPUT_AS_Q_DEF
						| CMQC.MQOO_INQUIRE);
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.fatal("Connect to Receive Queue ERROR!", e);
				logger.fatal(
						MultiLanguageResourceBundle.getInstance().getString(
								"MQChannel.start.receiveQueue.connect.error"),
						e);
				if (CommGateway.isExceptionMonitorSupport()) {
					onFatalException(receiveQueue, e, channelId + EXCEPTIONRECV);
				} else {
					onFatalException(receiveQueue, e);
				}
				shutdown();
				return;
			}
		}

		if (null == receiveQueueConfig && null == sendQueueConfig) {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MQChannel.start.QueueConfig.notFount"));
		}

		sendList = new LinkedList();
		waitMap = new HashMap(32);

		run = true;

		if (null != sendQueueConfig) {
			// 启动发送线程
			sender = new MQsendThread();
			sender.start();
		}

		if (null != receiveQueueConfig) {
			// 启动应答线程
			receiver = new MQreceiveThread();
			receiver.start();
		}

		if (MQChannelConfigParser.TYPE_SERVER != config.getType()
				&& (null != receiveQueueConfig || null != sendQueueConfig)) {
			// 启动监控线程
			monitor = new MQmonitorThread();
			monitor.start();
		}
	}

	private QueueConfig getQueueConfigByType(int type) {
		if (type != MQChannelConfigParser.QUEUE_TYPE_RECEIVE
				&& type != MQChannelConfigParser.QUEUE_TYPE_SEND) {
			// throw new RuntimeException(
			// "unkown queue type, must be send or receive");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"MQChannel.getQueueConfigByType.quueType.unkown"));
		}
		Map queueConfigs = config.getQuqueConfig();
		Iterator iterator = queueConfigs.keySet().iterator();
		QueueConfig queueConfig = null;
		while (iterator.hasNext()) {
			queueConfig = (QueueConfig) queueConfigs.get(iterator.next());
			if (queueConfig.getType() == type) {
				break;
			}
			queueConfig = null;
		}
		return queueConfig;
	}

	/**
	 * 发送线程
	 * 
	 * @author Administrator
	 * 
	 */
	private class MQsendThread extends Thread {
		public void run() {
			while (run) {
				Message message = null;
				synchronized (sendList) {
					if (0 == sendList.size()) {
						try {
							sendList.wait();
						} catch (InterruptedException e) {
							// e.printStackTrace();
						}
					}
					if (0 < sendList.size()) {
						message = (Message) sendList.remove();
					} else {
						continue;
					}
				}
				MQMessage mqMessage = new MQMessage();

				// 发送前过滤
				byte[] requestMsg = message.message;
				// for (int i = 0; i < config.getWriterFilterList().size(); i++) {
				// requestMsg = config.getWriterFilterList().get(i).doFilter(
				// requestMsg);
				// }
				if (null != config.getWriteFilter()) {
					requestMsg = config.getWriteFilter().doFilter(requestMsg);
				}

				try {
					mqMessage.write(requestMsg);
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("MQ Message Write ERROR!", e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("MQChannel.MQMessage.write.error"), e);
					if (Message.TYPE_REQ == message.type) {
						if (CommGateway.isExceptionMonitorSupport()) {
							onRequestMessageSendException(sendQueue,
									message.message, e, channelId
											+ EXCEPTIONSEND, dumpIp, dumpPort);
						} else {
							onRequestMessageSendException(sendQueue,
									message.message, e);
						}
					} else {
						if (CommGateway.isExceptionMonitorSupport()) {
							onResponseMessageSendException(sendQueue,
									message.message, e, channelId
											+ EXCEPTIONSEND, dumpIp, dumpPort);
						} else {
							onResponseMessageSendException(sendQueue,
									message.message, e);
						}
					}
					continue;
				}
				try {
					MQPutMessageOptions pmo = new MQPutMessageOptions();
					if (Message.TYPE_REQ == message.type
							|| null == message.messageId) {
						pmo.options = pmo.options + CMQC.MQPMO_NEW_MSG_ID;
						// 请求
						if (null != sendQueueConfig.getRelationMessageId()) {
							mqMessage.correlationId = sendQueueConfig
									.getRelationMessageId();
						}
					} else {
						mqMessage.messageId = message.messageId;
						// 响应
						if (null != sendQueueConfig.getRelationMessageId()) {
							mqMessage.correlationId = sendQueueConfig
									.getRelationMessageId();
						} else {
							mqMessage.correlationId = message.messageId;
						}
					}
					pmo.options = pmo.options + CMQC.MQPMO_SYNCPOINT;
					sendQueue.put(mqMessage, pmo);
					sendManager.commit();
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("Put Message into Queue ERROR!", e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("MQChannel.putMessage.error"), e);
					if (Message.TYPE_REQ == message.type) {
						// synchronized (waitMap) {
						// waitMap.remove(new String(CodeUtil
						// .BytetoHex(messageId)));
						// }
						if (CommGateway.isExceptionMonitorSupport()) {
							onRequestMessageSendException(sendQueue,
									message.message, e, channelId
											+ EXCEPTIONSEND, dumpIp, dumpPort);
						} else {
							onRequestMessageSendException(sendQueue,
									message.message, e);
						}

					} else {
						if (CommGateway.isExceptionMonitorSupport()) {
							onResponseMessageSendException(sendQueue,
									message.message, e, channelId
											+ EXCEPTIONSEND, dumpIp, dumpPort);
						} else {
							onResponseMessageSendException(sendQueue,
									message.message, e);
						}

					}
					continue;
				}

				byte[] messageId = null;
				if (Message.TYPE_REQ == message.type && !message.isAsync) {
					AbstractMessageRecognizer keyRecognizer = getQueueConfigByType(
							MQChannelConfigParser.QUEUE_TYPE_SEND)
							.getMessageKeyRecognizer();

					if (null == keyRecognizer) {
						// messageId不在报文中
						messageId = mqMessage.messageId;
					} else {
						// messageId是报文中特定的一段
						messageId = keyRecognizer.recognize(message.message)
								.getBytes();
					}
					message.start = new Date().getTime();
					synchronized (waitMap) {
						waitMap.put(new String(CodeUtil.BytetoHex(messageId)),
								message);
					}
					if (null != logger && logger.isInfoEnabled()) {
						byte[] msg4Log = null;
						logger
								.info(MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MQClientChannel.MQsendThread.sendRequest.success",
												new String[] {
														CodeUtil
																.Bytes2FormattedText(messageId),
														CodeUtil
																.Bytes2FormattedText(message.message) }));
					}
				}

				if (Message.TYPE_REQ == message.type) {
					onRequestMessageSent(sendQueue, message.message);
				} else {
					if (null != logger && logger.isInfoEnabled()) {
						logger
								.info(MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MQServerChannel.MQsendThread.sendResponse",
												new String[] { CodeUtil
														.Bytes2FormattedText(message.message) }));
					}
					onResponseMessageSent(sendQueue, message.message);
				}
				// 产生请求消息发送完毕事件
				// logger.info("Send Request Message, message id : \n"
				// + CodeUtil.Bytes2FormattedText(messageId) + "\n"
				// + "request message : \n"
				// + CodeUtil.Bytes2FormattedText(request.requestMessage));

			}
		}
	}

	/**
	 * 接收线程
	 * 
	 * @author Administrator
	 * 
	 */
	private class MQreceiveThread extends Thread {
		public void run() {
			while (run) {
				long queueLength = 0;
				try {
					queueLength = receiveQueue.getCurrentDepth();
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("Get Receive Queue's Current Depth ERROR!",
					// e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("MQChannel.getQueueDepth.error"), e);
					if (queueLength <= 0) {
						if (CommGateway.isExceptionMonitorSupport()) {
							onException(receiveQueue, e, channelId
									+ EXCEPTIONRECV);
						} else {
							onException(receiveQueue, e);
						}
						try {
							Thread.sleep(5000);
						} catch (Exception e2) {
							// e2.printStackTrace();
						}
						continue;
					}
				}
				if (0 < queueLength) {
					MQMessage message = new MQMessage();
					try {
						MQGetMessageOptions gmo = new MQGetMessageOptions();
						gmo.options = gmo.options + CMQC.MQGMO_SYNCPOINT;
						gmo.options = gmo.options + CMQC.MQGMO_NO_WAIT;
						if (null != receiveQueueConfig.getRelationMessageId()) {
							gmo.matchOptions = CMQC.MQMO_MATCH_CORREL_ID;
							message.correlationId = receiveQueueConfig
									.getRelationMessageId();
						}
						gmo.waitInterval = (config.getTimeout() <= 0 ? 10000
								: config.getTimeout());
						synchronized (receiveQueue) {
							receiveQueue.get(message, gmo);
						}

						receiveManager.commit();
					} catch (MQException e) {
						// e.printStackTrace();
						if (2033 == e.reasonCode) {
							continue;
						}
						logger.error(MultiLanguageResourceBundle.getInstance()
								.getString("MQChannel.getMessage.error"), e);

						if (CommGateway.isExceptionMonitorSupport()) {
							onException(receiveQueue, e, channelId
									+ EXCEPTIONRECV);
						} else {
							onException(receiveQueue, e);
						}
						// }
					}
					byte[] receiveMessage = null;
					try {
						receiveMessage = new byte[message.getMessageLength()];
						message.readFully(receiveMessage);
					} catch (Exception e) {
						// e.printStackTrace();
						// logger.error("Read Message ERROR!", e);
						logger.error(MultiLanguageResourceBundle.getInstance()
								.getString("MQChannel.readMessage.error"), e);
						if (CommGateway.isExceptionMonitorSupport()) {
							onException(receiveQueue, e, channelId
									+ EXCEPTIONRECV);
						} else {
							onException(receiveQueue, e);
						}
						continue;
					}

					// 接收后过滤
					// for (int i = 0; i < config.getReaderFilterList().size(); i++) {
					// receiveMessage = config.getReaderFilterList().get(i)
					// .doFilter(receiveMessage);
					// }
					if (null != config.getReadFilter()) {
						receiveMessage = config.getReadFilter().doFilter(
								receiveMessage);
					}

					if (MQChannelConfigParser.TYPE_CLIENT == config.getType()) {
						byte[] messageId = null;
						AbstractMessageRecognizer keyRecognizer = getQueueConfigByType(
								MQChannelConfigParser.QUEUE_TYPE_RECEIVE)
								.getMessageKeyRecognizer();

						if (null == keyRecognizer) {
							// messageId不在报文中
							messageId = message.messageId;
						} else {
							// messageId是报文中特定的一段
							messageId = keyRecognizer.recognize(receiveMessage)
									.getBytes();
						}
						Message requestInfo = null;
						synchronized (waitMap) {
							requestInfo = (Message) waitMap.remove(new String(
									CodeUtil.BytetoHex(messageId)));
						}
						if (null == requestInfo) {
							// 不存在该请求报文信息
							// logger
							// .error("NO Request Message Found for Receive Message, receive message id : \n"
							// + CodeUtil
							// .Bytes2FormattedText(messageId));
							messageId = message.correlationId;
							requestInfo = (Message) waitMap.get(new String(
									CodeUtil.BytetoHex(messageId)));
							if (null == requestInfo) {
								logger
										.error(MultiLanguageResourceBundle
												.getInstance()
												.getString(
														"MQClientChannel.MQreceiveThread.receiveMessage.requestMessage.notFound",
														new String[] { CodeUtil
																.Bytes2FormattedText(messageId) }));
								continue;
							}
						}

						if (System.currentTimeMillis() - requestInfo.start >= requestInfo.timeout) {
							// 等待超时
							// logger
							// .error("Wait for Response timeout, request message : \n"
							// + CodeUtil
							// .Bytes2FormattedText(requestInfo.requestMessage));
							if (null != receiveMessage) {
								logger
										.error(MultiLanguageResourceBundle
												.getInstance()
												.getString(
														"MQClientChannel.MQreceiveThread.waitResponse.timeout",
														new String[] {
																CodeUtil
																		.Bytes2FormattedText(requestInfo.message),
																CodeUtil
																		.Bytes2FormattedText(receiveMessage) }));
							}
							onResponseMessageReceiveException(
									receiveQueue,
									requestInfo.message,
									new RuntimeException(
											MultiLanguageResourceBundle
													.getInstance()
													.getString(
															"MQClientChannel.MQreceiveThread.waitResponse.timeout",
															new String[] { CodeUtil
																	.Bytes2FormattedText(requestInfo.message) })));

						} else {
							// logger
							// .info("Receive Response Message, message id : \n"
							// + CodeUtil
							// .Bytes2FormattedText(messageId)
							// + "\n"
							// + "response message : \n"
							// + CodeUtil
							// .Bytes2FormattedText(responseMessage)
							// + "\n"
							// + "request message : \n"
							// + CodeUtil
							// .Bytes2FormattedText(requestInfo.requestMessage));
							synchronized (waitMap) {
								waitMap.remove(new String(CodeUtil
										.BytetoHex(messageId)));
							}
							if (null != logger && logger.isInfoEnabled()) {
								logger
										.info(MultiLanguageResourceBundle
												.getInstance()
												.getString(
														"MQClientChannel.MQreceiveThread.receiveResponse.success",
														new String[] {
																CodeUtil
																		.Bytes2FormattedText(messageId),
																CodeUtil
																		.Bytes2FormattedText(receiveMessage),
																CodeUtil
																		.Bytes2FormattedText(requestInfo.message) }));
							}
							onResponseMessageArrived(receiveQueue,
									requestInfo.message, receiveMessage);
						}
					} else if (MQChannelConfigParser.TYPE_SERVER == config
							.getType()) {
						logger
								.info(MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"MQServerChannel.MQreceiveThread.receiveRequest",
												new String[] { CodeUtil
														.Bytes2FormattedText(receiveMessage) }));

						onRequestMessageArrived(message.messageId,
								receiveMessage);
					} else {
						if (isRequestMessage(receiveMessage)) {
							logger
									.info(MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"MQServerChannel.MQreceiveThread.receiveRequest",
													new String[] { CodeUtil
															.Bytes2FormattedText(receiveMessage) }));
							onRequestMessageArrived(message.messageId,
									receiveMessage);
						} else {
							byte[] messageId = null;
							AbstractMessageRecognizer keyRecognizer = getQueueConfigByType(
									MQChannelConfigParser.QUEUE_TYPE_RECEIVE)
									.getMessageKeyRecognizer();

							if (null == keyRecognizer) {
								// messageId不在报文中
								messageId = message.messageId;
							} else {
								// messageId是报文中特定的一段
								messageId = keyRecognizer.recognize(
										receiveMessage).getBytes();
							}
							Message requestInfo = null;
							synchronized (waitMap) {
								requestInfo = (Message) waitMap
										.remove(new String(CodeUtil
												.BytetoHex(messageId)));
							}
							if (null == requestInfo) {
								// 不存在该请求报文信息
								// logger
								// .error("NO Request Message Found for Receive Message, receive message id : \n"
								// + CodeUtil
								// .Bytes2FormattedText(messageId));
								messageId = message.correlationId;
								synchronized (waitMap) {
									requestInfo = (Message) waitMap
											.remove(new String(CodeUtil
													.BytetoHex(messageId)));
								}
								if (null == requestInfo) {
									logger
											.error(MultiLanguageResourceBundle
													.getInstance()
													.getString(
															"MQClientChannel.MQreceiveThread.receiveMessage.requestMessage.notFound",
															new String[] { CodeUtil
																	.Bytes2FormattedText(messageId) }));
									continue;
								}
							}

							if (System.currentTimeMillis() - requestInfo.start >= requestInfo.timeout) {
								// 等待超时
								// logger
								// .error("Wait for Response timeout, request message : \n"
								// + CodeUtil
								// .Bytes2FormattedText(requestInfo.requestMessage));
								logger
										.error(MultiLanguageResourceBundle
												.getInstance()
												.getString(
														"MQClientChannel.MQreceiveThread.waitResponse.timeout",
														new String[] { CodeUtil
																.Bytes2FormattedText(requestInfo.message) }));
								onResponseMessageReceiveException(
										receiveQueue,
										requestInfo.message,
										new RuntimeException(
												MultiLanguageResourceBundle
														.getInstance()
														.getString(
																"MQClientChannel.MQreceiveThread.waitResponse.timeout",
																new String[] { CodeUtil
																		.Bytes2FormattedText(requestInfo.message) })));

							} else {
								// logger
								// .info("Receive Response Message, message id : \n"
								// + CodeUtil
								// .Bytes2FormattedText(messageId)
								// + "\n"
								// + "response message : \n"
								// + CodeUtil
								// .Bytes2FormattedText(responseMessage)
								// + "\n"
								// + "request message : \n"
								// + CodeUtil
								// .Bytes2FormattedText(requestInfo.requestMessage));

								logger
										.info(MultiLanguageResourceBundle
												.getInstance()
												.getString(
														"MQClientChannel.MQreceiveThread.receiveResponse.success",
														new String[] {
																CodeUtil
																		.Bytes2FormattedText(messageId),
																CodeUtil
																		.Bytes2FormattedText(receiveMessage),
																CodeUtil
																		.Bytes2FormattedText(requestInfo.message) }));
								onResponseMessageArrived(receiveQueue,
										requestInfo.message, receiveMessage);
							}
						}
					}
				} else {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
				}
			}
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

	/**
	 * 监控进程
	 * 
	 * @author Administrator
	 * 
	 */
	private class MQmonitorThread extends Thread {
		public void run() {
			while (run) {
				long currTime = System.currentTimeMillis();
				LinkedList timeout = new LinkedList();

				synchronized (waitMap) {
					Iterator iterator = waitMap.keySet().iterator();
					while (iterator.hasNext()) {
						Object key = iterator.next();
						Message requestInfo = (Message) waitMap.get(key);
						if (currTime - requestInfo.start >= requestInfo.timeout) {
							// 等待超时
							// logger
							// .error("Wait for Response timeout, request message id: \n"
							// + CodeUtil
							// .Bytes2FormattedText(CodeUtil
							// .HextoByte((String) key))
							// + "\n"
							// + "request message: \n"
							// + CodeUtil
							// .Bytes2FormattedText(requestInfo.requestMessage));
							logger
									.error(MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"MQClientChannel.MQreceiveThread.waitResponse.timeout",
													new String[] { CodeUtil
															.Bytes2FormattedText(requestInfo.message) }));

							timeout.add(key);
						}

						for (int i = 0; i < timeout.size(); i++) {
							requestInfo = (Message) waitMap.remove(timeout
									.get(i));
							onResponseMessageReceiveException(
									receiveQueue,
									requestInfo.message,
									new RuntimeException(
											MultiLanguageResourceBundle
													.getInstance()
													.getString(
															"MQClientChannel.MQreceiveThread.waitResponse.timeout",
															new String[] { CodeUtil
																	.Bytes2FormattedText(requestInfo.message) })));
						}
					}
				}

				try {
					this.sleep(10000);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private class Message {
		public static final int TYPE_REQ = 0x90;
		public static final int TYPE_RES = 0x91;
		public byte[] message;
		public byte[] messageId;
		public long start;
		public long timeout;
		public int type = 0x90;
		public boolean isAsync = false;
	}

	public MQChannelConfig getConnectorConfig() {
		return config;
	}

	public void setConnectorConfig(MQChannelConfig config) {
		this.config = config;
	}

}
