package com.fib.msgconverter.commgateway.channel.http;

import java.io.InputStream;
import java.net.ConnectException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpStatus;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.http.config.HTTPClientChannelConfig;
import com.fib.msgconverter.commgateway.channel.http.config.HTTPClientChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.http.config.database.HTTPClientChannelConfigLoader;
import com.fib.msgconverter.commgateway.util.SensitiveInfoFilter;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;

/**
 * HTTP客户端通道
 * 
 * @author 刘恭亮
 * 
 */
public abstract class HTTPClientChannel extends Channel {
	protected HTTPClientChannelConfig config;

	public void setConnectorConfig(HTTPClientChannelConfig newConfig) {
		config = newConfig;
	}

	protected ThreadGroup handlerGroup = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fib.msgconverter.commgateway.channel.Channel#closeSource(java.lang.
	 * Object)
	 */
	public void closeSource(Object source) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fib.msgconverter.commgateway.channel.Channel#loadConfig()
	 */
	public void loadConfig(InputStream in) {
		if (CommGateway.isConfigDBSupport()) {
			HTTPClientChannelConfigLoader loader = new HTTPClientChannelConfigLoader();
			config = loader.loadConfig(channelConfig.getConnectorId());
		} else {
			HTTPClientChannelConfigParser parser = new HTTPClientChannelConfigParser();
			config = parser.parse(in);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fib.msgconverter.commgateway.channel.Channel#sendRequestMessage(byte[],
	 * int)
	 */
	public void sendRequestMessage(byte[] requestMessage, boolean async, int timeout) {
		int activeCount = handlerGroup.activeCount();
		if (activeCount >= config.getMaxHandlerNumber()) {
			// String errorMessage = "activeCount=" + activeCount
			// + ". There has no handler to send request message!";
			String errorMessage = MultiLanguageResourceBundle.getInstance()
					.getString("HTTPClientChannel.sendRequestMessage.noHandler", new String[] { "" + activeCount });
			logger.error(errorMessage);
			onRequestMessageSendException(handlerGroup, requestMessage, new RuntimeException(errorMessage));
			return;
			// 下列处理可能会造成已超时请求堆积
			// // 已无处理线程，扔回事件队列 TODO
			// Session session = SessionManager.getSession(requestMessage);
			// // session.getSourceChannel().onRequestMessageArrived(
			// // session.getSource(), session.getSourceRequestMessage());
			// Event e = new Event(Event.EVENT_REQUEST_ARRIVED, session
			// .getSourceChannel(), session.getSource(), session
			// .getSourceRequestMessage(), (byte[]) null);
			// eventQueue.postEvent(e);
		}
		// 启动HTTP客户端线程
		HTTPClientHandler handler = new HTTPClientHandler();
		handler.setRequestMessage(requestMessage);
		handler.setAsync(async);
		handler.setTimeout(timeout);
		handler.setChannelId(getId());
		try {
			handler.setParameters(createParamters(requestMessage));
		} catch (Exception e) {
			// logger.error("create paramters failed!", e);
			logger.error(MultiLanguageResourceBundle.getInstance()
					.getString("HTTPClientChannel.sendRequestMessage.createParameter.failed"), e);
			onRequestMessageSendException(handler, requestMessage, e);
			return;
		}
		Thread t = new Thread(handlerGroup, handler);
		t.start();
	}

	/**
	 * 组织HTTP请求的参数，通过Map返回。参数名和值都必须是字符串。
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected abstract Map<String, String> createParamters(byte[] requestMessage);

	/**
	 * 解析HTTP服务器返回的响应数据
	 * 
	 * @param responseBody
	 * @return
	 */
	protected byte[] parseResponseBody(byte[] responseBody) {
		return responseBody;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fib.msgconverter.commgateway.channel.Channel#sendResponseMessage(byte[])
	 */
	public void sendResponseMessage(byte[] responseMessage) {
		// throw new RuntimeException("HTTPClientChannel [" + mainConfig.getId()
		// + "] can't send response message!");
		throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
				.getString("HTTPClientChannel.sendResponseMessage.error", new String[] { mainConfig.getId() }));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fib.msgconverter.commgateway.channel.Channel#shutdown()
	 */
	public void shutdown() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fib.msgconverter.commgateway.channel.Channel#start()
	 */
	public void start() {
		// 初始化线程组
		handlerGroup = new ThreadGroup(mainConfig.getId());
	}

	/**
	 * HTTP客户端请求处理器
	 * 
	 * @author 刘恭亮
	 * 
	 */
	public class HTTPClientHandler implements Runnable {
		private String channelId;
		private String dumpIp;
		private int dumpPort;
		private byte[] requestMessage = null;
		private boolean async;
		private int timeout = 30000;
		private Map parameters = null;

		private void initExceptionUtil(String url) {
			if (CommGateway.isExceptionMonitorSupport()) {
				int startIndex = 0;
				if (-1 != url.indexOf("//")) {
					startIndex = url.indexOf("//") + 2;
					int endIndex = 0;
					if (-1 != url.indexOf("/", startIndex)) {
						endIndex = url.indexOf("/", startIndex);
					}
					String tmp = url.substring(startIndex, endIndex);
					if (-1 != tmp.indexOf(":")) {
						try {
							dumpIp = tmp.substring(0, tmp.indexOf(":"));
							dumpPort = Integer.parseInt(tmp.substring(tmp.indexOf(":") + 1));
						} catch (Exception e) {

						}
					} else {
						dumpIp = tmp;
						dumpPort = 80;
					}
				}
			}
		}

		public void run() {

			// 1. 组织url参数
			PostMethod method = null;
			try {
				String url = config.getUrl();
				initExceptionUtil(url);

				if ("https:".equals(url.substring(0, url.indexOf("//")))) {
					Protocol myhttps = new Protocol("https", new NoPKISocketFactory(), 443);
					Protocol.registerProtocol("https", myhttps);
				}
				method = new PostMethod(config.getUrl());
				Iterator it = parameters.entrySet().iterator();
				Map.Entry entry = null;
				while (it.hasNext()) {
					entry = (Entry) it.next();
					method.addParameter((String) entry.getKey(), (String) entry.getValue());
				}
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.error(
				// "create PostMethod and add Parameters error! Parameters :"
				// + parameters, e);
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString("HTTPClientHandler.run.createMethod.error", new String[] { "" + parameters }));
				// 产生错误事件
				onRequestMessageSendException(this, requestMessage, e);
				return;
			}

			// 2. 提交请求到HTTP服务器，处理结果
			try {
				HttpClientParams httpClientParams = new HttpClientParams();
				httpClientParams.setSoTimeout(timeout);
				if (config.getContentCharset() != null) {
					httpClientParams.setContentCharset(config.getContentCharset());
				}

				HttpClient client = new HttpClient(httpClientParams, new SimpleHttpConnectionManager(true));
				if (null != logger && logger.isInfoEnabled()) {
					if (null != requestMessage) {
						byte[] msg4Log = null;
						if (CommGateway.isShieldSensitiveFields()) {
							msg4Log = SensitiveInfoFilter.filtSensitiveInfo(new String(requestMessage),
									CommGateway.getSensitiveFields(), CommGateway.getSensitiveReplaceChar()).getBytes();
						} else {
							msg4Log = requestMessage;
						}
						logger.info(MultiLanguageResourceBundle.getInstance().getString(
								"SocketClientChannel.handleKey.sendRequest.success",
								new String[] { CodeUtil.Bytes2FormattedText(msg4Log) }));
					}
				}
				// 2.1. 提交请求
				int statusCode = client.executeMethod(method);

				// System.out.println("statusCode= " + statusCode);
				// 2.2. 判断状态码
				if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
					// 后台返回失败，产生错误事件
					if (CommGateway.isExceptionMonitorSupport()) {
						onResponseMessageReceiveException(this, requestMessage,
								new RuntimeException(method.getStatusLine().toString()), channelId, dumpIp, dumpPort);
					} else {
						onResponseMessageReceiveException(this, requestMessage,
								new RuntimeException(method.getStatusLine().toString()));
					}
					return;
				} else {
					onRequestMessageSent(this, requestMessage);
				}

				if (!async) {
					// System.out.println("response="
					// + method.getResponseBodyAsString());
					// 2.3. 解析应答
					byte[] responseMessage = parseResponseBody(method.getResponseBody());
					if (logger.isInfoEnabled()) {
						if (null != responseMessage) {
							byte[] msg4Log = null;
							if (CommGateway.isShieldSensitiveFields()) {
								msg4Log = SensitiveInfoFilter
										.filtSensitiveInfo(new String(responseMessage),
												CommGateway.getSensitiveFields(), CommGateway.getSensitiveReplaceChar())
										.getBytes();
							} else {
								msg4Log = responseMessage;
							}
							logger.info(MultiLanguageResourceBundle.getInstance().getString(
									"SocketClientChannel.handleKey.receiveResponse.success",
									new String[] { CodeUtil.Bytes2FormattedText(msg4Log) }));
						}
					}
					// 2.4. 产生成功事件
					onResponseMessageArrived(this, requestMessage, responseMessage);
				}

			} catch (ConnectException e) {
				// e.printStackTrace();
				// logger.error("connect error!", e);
				logger.error(MultiLanguageResourceBundle.getInstance().getString("HTTPClientHandler.run.connect.error"),
						e);
				// 产生错误事件

				if (CommGateway.isExceptionMonitorSupport()) {
					onConnectException(this, requestMessage, e, channelId, dumpIp, dumpPort);
				} else {
					onConnectException(this, requestMessage, e);
				}
				return;
			} catch (Exception e) {
				// e.printStackTrace();
				// logger.error("communicate error!", e);
				logger.error(
						MultiLanguageResourceBundle.getInstance().getString("HTTPClientHandler.run.communicate.error"),
						e);
				// 产生错误事件
				if (CommGateway.isExceptionMonitorSupport()) {
					onRequestMessageSendException(this, requestMessage, e, channelId, dumpIp, dumpPort);
				} else {
					onRequestMessageSendException(this, requestMessage, e);
				}

				return;
			} finally {
				method.releaseConnection();
			}

		}

		public String toString() {
			StringBuffer buf = new StringBuffer(1024);
			buf.append("HTTP Request: ");
			buf.append(config.getUrl());
			buf.append("?");
			Iterator it = parameters.entrySet().iterator();
			Map.Entry entry = null;
			while (it.hasNext()) {
				entry = (Entry) it.next();
				buf.append(entry.getKey());
				buf.append("=");
				buf.append(entry.getValue());
				if (it.hasNext()) {
					buf.append("&");
				}
			}

			return buf.toString();
		}

		/**
		 * @return the requestMessage
		 */
		public byte[] getRequestMessage() {
			return requestMessage;
		}

		/**
		 * @param requestMessage the requestMessage to set
		 */
		public void setRequestMessage(byte[] requestMessage) {
			this.requestMessage = requestMessage;
		}

		/**
		 * @return the timeout
		 */
		public int getTimeout() {
			return timeout;
		}

		/**
		 * @param timeout the timeout to set
		 */
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}

		/**
		 * @return the parameters
		 */
		public Map getParameters() {
			return parameters;
		}

		/**
		 * @param parameters the parameters to set
		 */
		public void setParameters(Map parameters) {
			this.parameters = parameters;
		}

		public boolean isAsync() {
			return async;
		}

		public void setAsync(boolean async) {
			this.async = async;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

	}

	public HTTPClientChannelConfig getConnectorConfig() {
		return config;
	}
}