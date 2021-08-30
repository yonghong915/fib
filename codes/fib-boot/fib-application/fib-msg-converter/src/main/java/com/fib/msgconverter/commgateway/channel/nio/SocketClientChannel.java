/**
 * 北京长信通信息技术有限公司
 * 2008-8-30 上午09:20:27
 */
package com.fib.msgconverter.commgateway.channel.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.nio.config.SocketChannelConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.SocketClientChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.nio.config.database.SocketChannelConfigLoader;
import com.fib.msgconverter.commgateway.channel.nio.reader.AbstractNioReader;
import com.fib.msgconverter.commgateway.channel.nio.writer.AbstractNioWriter;
import com.fib.msgconverter.commgateway.module.impl.ExceptionMonitor;
import com.fib.msgconverter.commgateway.util.SensitiveInfoFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;

/**
 * Socket客户端通道
 * 
 * @author 刘恭亮
 * 
 */
public class SocketClientChannel extends Channel {
	public static final int READ_TIME_OUT = 30000;

	protected SocketChannelConfig config;

	protected NioClientThread executor;

	private HashMap waitMap = new HashMap();

	private WaitMapMonitorHandler monitor = null;

	private boolean run = true;

	public void loadConfig(InputStream in) {
		if (CommGateway.isConfigDBSupport()) {
			SocketChannelConfigLoader loader = new SocketChannelConfigLoader();
			config = loader.loadConfig(channelConfig.getConnectorId());
		} else {
			SocketClientChannelConfigParser parser = new SocketClientChannelConfigParser();
			config = parser.parse(in);
		}
	}

	public void sendRequestMessage(byte[] requestMessage, boolean isAsync,
			int timeout) {
		executor.send(requestMessage, isAsync, timeout);
	}

	public void sendResponseMessage(byte[] responseMessage) {
		// throw new RuntimeException("SocketClientChannel [" +
		// mainConfig.getId()
		// + "] can't send response message!");
		throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
				.getString(
						"SocketClientChannel.sendResponseMessage.canNotSend",
						new String[] { mainConfig.getId() }));

	}

	public void closeSource(Object source) {
		// throw new RuntimeException("SocketClientChannel [" + getId()
		// + "] doesn't need closeSource operation!");
		// 什么也不作
		SocketChannel channel = (SocketChannel) source;
		try {
			channel.close();
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error(e);
		}
	}

	public void shutdown() {
		run = false;
		executor.close();
		try {
			monitor.interrupt();
			monitor.join(500);
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	public void start() {
		run = true;
		executor = new NioClientThread();
		executor.setServerAddress(config.getConnectionConfig()
				.getServerAddress());
		executor.setPort(config.getConnectionConfig().getPort());
		executor.setCommBufferSize(config.getConnectionConfig()
				.getCommBufferSize());
		executor.setChannelId(getId());
		// reader
		AbstractNioReader reader = (AbstractNioReader) ClassUtil
				.createClassInstance(config.getReaderConfig().getClassName());
		reader.setLogger(logger);
		reader.setParameters(config.getReaderConfig().getParameters());
		reader.setFilterList(config.getReaderConfig().createFilterList());
		executor.setReaderSeed(reader);
		// writer
		AbstractNioWriter writer = (AbstractNioWriter) ClassUtil
				.createClassInstance(config.getWriterConfig().getClassName());
		writer.setLogger(logger);
		writer.setParameters(config.getWriterConfig().getParameters());
		writer.setFilterList(config.getWriterConfig().createFilterList());
		executor.setWriterSeed(writer);
		// start server
		executor.startServer();
		// run
		executor.start();

		monitor = new WaitMapMonitorHandler();
		monitor.start();
	}

	/**
	 * Nio客户端线程
	 * 
	 * @author 刘恭亮
	 * 
	 */
	private class NioClientThread extends Thread {
		private String channelId;
		private Selector selector = null;
		private ByteBuffer commBuffer = null;

		private String serverAddress = null;
		private int port = 8888;
		private int commBufferSize = 8192;
		private InetSocketAddress socketAddress = null;

		private final List writeQueue = new ArrayList(512);

		// Reader种子，所有读操作的Reader从它克隆
		private AbstractNioReader readerSeed;
		// Writer种子，所有写操作的Writer从它克隆
		private AbstractNioWriter writerSeed;

		private boolean run = true;

		@Override
		public void run() {
			// 开始服务
			int keyNum = 0;
			try {
				while (run) {
					// 将其他线程待注册的key加入
					addWriteKey();

					// select
					try {
						// *************************
						// 当另一个线程register时，如果无新的i/o到达，Selector所在线程会在select()上无限堵塞，
						// 造成另一线程register堵塞。
						// 改为select(10)可避免此问题。
						// 最佳方案是 改为由select线程来register

						keyNum = selector.select(1000);
					} catch (Exception e) {
						// e.printStackTrace();
						if (selector.isOpen()) {
							// logger.error("Selector.select() exception!", e);
							logger
									.error(
											MultiLanguageResourceBundle
													.getInstance()
													.getString(
															"SocketChannel.selector.select.error"),
											e);
							if (CommGateway.isExceptionMonitorSupport()) {
								ExceptionMonitor.dump(channelId, e);
							}

							continue;
						} else {
							// logger.fatal("Selector crashed!", e);
							logger.fatal(MultiLanguageResourceBundle
									.getInstance().getString(
											"SocketChannel.selector.crashed"),
									e);
							if (CommGateway.isExceptionMonitorSupport()) {
								onFatalException(selector, e, channelId);

							} else {
								onFatalException(selector, e);
							}

							break;
						}
					}

					if (keyNum == 0) {
						continue;
					} else {
						// 选择并处理
						selectAndHandle();
					}

				}
			} catch (Exception e) {
				// logger.fatal("Channel crashed!", e);
				logger.fatal(MultiLanguageResourceBundle.getInstance()
						.getString("SocketChannel.channel.crashed"), e);

				if (CommGateway.isExceptionMonitorSupport()) {
					onFatalException(this, e, channelId);
				} else {
					onFatalException(this, e);
				}
			} finally {
				close();
			}
		}

		public void selectAndHandle() {
			SelectionKey key = null;
			Iterator it = selector.selectedKeys().iterator();
			while (it.hasNext()) {
				key = (SelectionKey) it.next();
				it.remove();
				if (!key.isValid()) {
					// logger.error("key[" + key.toString()
					// + "] is invalid");
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("SocketChannel.key.invalid",
									new String[] { key.toString() }));
					continue;
				}
				try {
					handleKey(key);
				} catch (Exception e) {
					// logger.fatal("handleKey exception!", e);
					logger.fatal(MultiLanguageResourceBundle.getInstance()
							.getString("SocketChannel.handleKey.error"), e);
					ExceptionUtil.throwActualException(e);
				}
			}
		}

		public void startServer() {
			socketAddress = new InetSocketAddress(serverAddress, port);

			// 1. selector
			try {
				selector = Selector.open();
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.error("Open Selector failed!", e);
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString("SocketChannel.selector.open.error"), e);
				close();
				if (CommGateway.isExceptionMonitorSupport()) {
					ExceptionMonitor.dump(channelId, e);
				}
				ExceptionUtil.throwActualException(e);
			}

			// 2. 初始化通讯缓冲
			commBuffer = ByteBuffer.allocateDirect(commBufferSize);

		}

		private void handleKey(SelectionKey key) {
			SocketChannel channel = null;
			if (key.isConnectable()) {
				if (!key.isValid()) {
					key.cancel();
					return;
				}
				channel = (SocketChannel) key.channel();
				// 继续完成连接
				if (channel.isConnectionPending()) {
					try {
						if (!channel.finishConnect()) {
							return;
						}
					} catch (Exception e) {
						// System.out.println("连接失败：" + e.getMessage());
						// e.printStackTrace();
						// logger.error("connect error", e);
						logger.error(MultiLanguageResourceBundle.getInstance()
								.getString("SocketChannel.connect.failed"));
						try {
							channel.close();
						} catch (Exception e1) {
							// e1.printStackTrace();
						}
						if (CommGateway.isExceptionMonitorSupport()) {
							onConnectException(channel, ((ClientRequest) key
									.attachment()).requestMessage, e,
									channelId, serverAddress, port);
						} else {
							onConnectException(channel, ((ClientRequest) key
									.attachment()).requestMessage, e);
						}
						return;
					}
				}
				// 连接已完成则开始发送
				if (channel.isConnected()) {
					// channel.register(selector, ops)
					key.interestOps(SelectionKey.OP_WRITE);
				}
			} else if (key.isWritable()) {
				if (!key.isValid()) {
					key.cancel();
					return;
				}
				channel = (SocketChannel) key.channel();
				ClientRequest cr = (ClientRequest) key.attachment();
				boolean complete = false;
				try {
					complete = cr.writer.write(channel, commBuffer);
				} catch (Exception excp) {
					// System.out.println("发送失败：" + excp.getMessage());
					// excp.printStackTrace();
					// logger.error("writer.write failed!", excp);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("SocketChannel.write.failed "), excp);
					try {
						channel.close();
					} catch (Exception e) {
						// e.printStackTrace();
					}
					if (CommGateway.isExceptionMonitorSupport()) {
						onRequestMessageSendException(channel,
								cr.requestMessage, excp, channelId,
								serverAddress, port);
					} else {
						onRequestMessageSendException(channel,
								cr.requestMessage, excp);
					}
					return;
				}
				if (complete) {
					// logger.info("send request message :\n"
					// + CodeUtil.Bytes2FormattedText(cr.writer
					// .getMessage()));
					if (null != logger && logger.isInfoEnabled()) {
						byte[] msg4Log = null;
						if (CommGateway.isShieldSensitiveFields()) {
							msg4Log = SensitiveInfoFilter.filtSensitiveInfo(
									new String(cr.writer.getMessage()),
									CommGateway.getSensitiveFields(),
									CommGateway.getSensitiveReplaceChar())
									.getBytes();
						} else {
							msg4Log = cr.writer.getMessage();
						}
						logger
								.info(MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"SocketClientChannel.handleKey.sendRequest.success",
												new String[] { CodeUtil
														.Bytes2FormattedText(msg4Log) }));
					}
					if (!cr.async) {
						// 同步通讯
						// 发送完毕，准备接收
						// try {
						// channel.socket().setSoTimeout(cr.timeout);
						// } catch (SocketException e) {
						// // System.out.println("设置超时错误" + e.getMessage());
						// e.printStackTrace();
						// try {
						// channel.close();
						// } catch (Exception e1) {
						// e1.printStackTrace();
						// }
						// onResponseMessageReceiveException(channel,
						// cr.requestMessage, e);
						// return;
						// }
						cr.reader = (AbstractNioReader) readerSeed.clone();
						key.interestOps(SelectionKey.OP_READ);
						cr.startTime = System.currentTimeMillis();
						synchronized (waitMap) {
							waitMap.put(key, cr);
						}
					} else {
						// 异步通讯
						// 发送完毕 关闭连接
						key.cancel();
						try {
							channel.close();
						} catch (Exception e) {
							// e.printStackTrace();
						}
					}
					// 产生请求发送完毕事件
					onRequestMessageSent(channel, cr.requestMessage);

				}

			} else if (key.isReadable()) {
				if (!key.isValid()) {
					key.cancel();
					return;
				}
				channel = (SocketChannel) key.channel();
				ClientRequest cr = (ClientRequest) key.attachment();
				boolean complete = false;
				try {
					complete = cr.reader.read(channel, commBuffer);
				} catch (Exception e) {
					// System.out.println("接收失败：" + e.getMessage());
					// e.printStackTrace();
					// logger.error("reader.read failed!", e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("SocketChannel.read.failed"), e);
					try {
						channel.close();
					} catch (Exception e1) {
						// e1.printStackTrace();
					}

					if (CommGateway.isExceptionMonitorSupport()) {
						onResponseMessageReceiveException(channel,
								cr.requestMessage, e, channelId, serverAddress,
								port);

					} else {
						onResponseMessageReceiveException(channel,
								cr.requestMessage, e);
					}
					return;
				}

				if (complete) {
					// 接收完毕 关闭连接
					key.cancel();
					try {
						channel.close();
					} catch (Exception e) {
						// e.printStackTrace();
					}
					synchronized (waitMap) {
						waitMap.remove(key);
					}
					// logger.info("receive response message :\n"
					// + CodeUtil.Bytes2FormattedText(cr.reader
					// .getMessage()));
					if (null != logger && logger.isInfoEnabled()) {
						byte[] msg4Log = null;
						if (CommGateway.isShieldSensitiveFields()) {
							msg4Log = SensitiveInfoFilter.filtSensitiveInfo(
									new String(cr.reader.getMessage()),
									CommGateway.getSensitiveFields(),
									CommGateway.getSensitiveReplaceChar())
									.getBytes();
						} else {
							msg4Log = cr.reader.getMessage();
						}
						logger
								.info(MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"SocketClientChannel.handleKey.receiveResponse.success",
												new String[] { CodeUtil
														.Bytes2FormattedText(msg4Log) }));
					}
					// 产生应答消息到达事件
					onResponseMessageArrived(channel, cr.requestMessage,
							cr.reader.getMessage());

				}

			}

		}

		private void addWriteKey() {
			List tmp = null;
			synchronized (writeQueue) {
				if (writeQueue.size() == 0) {
					return;
				}

				tmp = new ArrayList(writeQueue.size());
				tmp.addAll(writeQueue);
				writeQueue.clear();
			}

			Iterator it = tmp.iterator();
			ClientRequest cr = null;
			while (it.hasNext()) {
				cr = (ClientRequest) it.next();
				// 准备建立连接
				SocketChannel client = null;
				try {
					client = SocketChannel.open();

					if (null != config.getConnectionConfig()
							.getLocalServerAddress()) {
						client.socket().bind(
								new InetSocketAddress(config
										.getConnectionConfig()
										.getLocalServerAddress(), config
										.getConnectionConfig().getLocalPort()));
					}

					client.configureBlocking(false);
					// client.register(selector, SelectionKey.OP_CONNECT, cr);
					client.connect(socketAddress);
					if (client.isConnected()) {
						// // 设置读写缓存大小，避免由于缓存太小导致的读写数据不全
						// client.socket().setReceiveBufferSize(commBufferSize);
						// client.socket().setSendBufferSize(commBufferSize);
						// // 设置读超时
						// client.socket().setSoTimeout(READ_TIME_OUT);
						client.register(selector, SelectionKey.OP_WRITE, cr);
					} else {
						client.register(selector, SelectionKey.OP_CONNECT, cr);
					}
				} catch (Exception e) {
					// e.printStackTrace();
					// logger.error("connect failed!", e);
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("SocketChannel.connect.failed"), e);
					if (client != null) {
						try {
							client.close();
						} catch (Exception e1) {
							// e1.printStackTrace();
						}
					}
					if (CommGateway.isExceptionMonitorSupport()) {
						onConnectException(client, cr.requestMessage, e,
								channelId, serverAddress, port);
					} else {
						onConnectException(client, cr.requestMessage, e);
					}
				}

				try {
					// 设置读缓冲大小
					client.socket().setReceiveBufferSize(commBufferSize);
				} catch (SocketException e1) {
					// e1.printStackTrace();
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("Socket.setReceiveBufferSize.error"));
				}

				try {
					// 设置写缓冲大小
					client.socket().setSendBufferSize(commBufferSize);
				} catch (SocketException e1) {
					// e1.printStackTrace();
					logger.error(MultiLanguageResourceBundle.getInstance()
							.getString("Socket.setSendBufferSize.error"));
				}

			}
		}

		/**
		 * 发送消息
		 * 
		 * @param message
		 */
		public void send(byte[] message, boolean isAsync, int timeout) {
			// 1. 组织请求
			ClientRequest cr = new ClientRequest();
			cr.requestMessage = message;
			cr.async = isAsync;
			cr.timeout = timeout;
			cr.writer = (AbstractNioWriter) writerSeed.clone();
			cr.writer.setMessage(cr.requestMessage);

			// 2. 放入队列
			synchronized (writeQueue) {
				writeQueue.add(cr);
			}

			// 3. 激活selector
			selector.wakeup();
		}

		public void close() {
			run = false;

			if (selector != null && selector.isOpen()) {
				try {
					selector.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		/**
		 * @return the commBuffer
		 */
		public ByteBuffer getCommBuffer() {
			return commBuffer;
		}

		/**
		 * @param commBuffer
		 *            the commBuffer to set
		 */
		public void setCommBuffer(ByteBuffer commBuffer) {
			this.commBuffer = commBuffer;
		}

		/**
		 * @return the commBufferSize
		 */
		public int getCommBufferSize() {
			return commBufferSize;
		}

		/**
		 * @param commBufferSize
		 *            the commBufferSize to set
		 */
		public void setCommBufferSize(int commBufferSize) {
			this.commBufferSize = commBufferSize;
		}

		/**
		 * @return the run
		 */
		public boolean isRun() {
			return run;
		}

		/**
		 * @param run
		 *            the run to set
		 */
		public void setRun(boolean run) {
			this.run = run;
		}

		/**
		 * @return the serverAddress
		 */
		public String getServerAddress() {
			return serverAddress;
		}

		/**
		 * @param serverAddress
		 *            the serverAddress to set
		 */
		public void setServerAddress(String serverAddress) {
			this.serverAddress = serverAddress;
		}

		/**
		 * @return the port
		 */
		public int getPort() {
			return port;
		}

		/**
		 * @param port
		 *            the port to set
		 */
		public void setPort(int port) {
			this.port = port;
		}

		/**
		 * @return the readerSeed
		 */
		public AbstractNioReader getReaderSeed() {
			return readerSeed;
		}

		/**
		 * @param readerSeed
		 *            the readerSeed to set
		 */
		public void setReaderSeed(AbstractNioReader readerSeed) {
			this.readerSeed = readerSeed;
		}

		/**
		 * @return the writerSeed
		 */
		public AbstractNioWriter getWriterSeed() {
			return writerSeed;
		}

		/**
		 * @param writerSeed
		 *            the writerSeed to set
		 */
		public void setWriterSeed(AbstractNioWriter writerSeed) {
			this.writerSeed = writerSeed;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

	}

	private class WaitMapMonitorHandler extends Thread {
		public void run() {
			while (run) {
				try {
					Thread.currentThread().sleep(10000);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}

				ClientRequest cr = null;
				synchronized (waitMap) {
					if (0 == waitMap.size()) {
						continue;
					}

					Iterator it = waitMap.keySet().iterator();
					SelectionKey key = null;
					while (it.hasNext()) {
						key = (SelectionKey) it.next();
						cr = (ClientRequest) waitMap.get(key);
						if (System.currentTimeMillis() - cr.startTime >= cr.timeout) {
							it.remove();
							try {
								key.channel().close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							onResponseMessageReceiveException(
									key.channel(),
									cr.requestMessage,
									new RuntimeException(
											MultiLanguageResourceBundle
													.getInstance()
													.getString(
															"WaitQueueMonitor.timeout")));
						}
					}
				}
			}
		}
	}

	private class ClientRequest {
		public byte[] requestMessage;
		public boolean async;
		public int timeout;
		public AbstractNioWriter writer;
		public AbstractNioReader reader;
		public long startTime;
	}

	public SocketChannelConfig getConnectorConfig() {
		return config;
	}

	public void setConnectorConfig(SocketChannelConfig config) {
		this.config = config;
	}

}
