package com.fib.msgconverter.commgateway.channel.bosentconnection;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.bosentconnection.config.BosentClientChannelConfig;
import com.fib.msgconverter.commgateway.channel.bosentconnection.config.BosentClientChannelConfigParser;
import com.giantstone.common.map.serializer.MapSerializer;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.crm.servicecall.HttpDispacher;

public class BosentClientChannel extends Channel {
	protected BosentClientChannelConfig config;

	protected ThreadGroup handlerGroup = null;

	@Override
	public void closeSource(Object source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadConfig(InputStream in) {
		BosentClientChannelConfigParser parser = new BosentClientChannelConfigParser();
		config = parser.parse(in);

	}

	/**
	 * 解析服务器返回的响应数据
	 * 
	 * @param responseBody
	 * @return
	 */
	protected byte[] parseResponseBody(byte[] responseBody) {
		return responseBody;
	}

	@Override
	public void sendRequestMessage(byte[] requestMessage, boolean async,
			int timeout) {

		int activeCount = handlerGroup.activeCount();
		if (activeCount >= config.getMaxHandlerNumber()) {
			String errorMessage = "activeCount=" + activeCount
					+ ". There has no handler to send request message!";
			logger.error(errorMessage);
			onRequestMessageSendException(handlerGroup, requestMessage,
					new RuntimeException(errorMessage));
			return;
		}
		// 启动客户端线程
		BosentClientHandler handler = new BosentClientHandler();
		handler.setRequestMessage(requestMessage);
		handler.setAsync(async);
		handler.setTimeout(timeout);
		handler.setChannelId(getId());
		try {
			if (null != config.getContentCharset()) {
				handler.setParameters(MapSerializer.deserialize(new String(
						requestMessage, config.getContentCharset())));
			} else {
				handler.setParameters(MapSerializer.deserialize(new String(
						requestMessage)));
			}
		} catch (Exception e) {
			logger.error("create paramters failed!", e);

			onRequestMessageSendException(handler, requestMessage, e);
			return;
		}
		Thread t = new Thread(handlerGroup, handler);
		t.start();

	}

	@Override
	public void sendResponseMessage(byte[] responseMessage) {
		throw new RuntimeException("BosentClientChannel [" + mainConfig.getId()
				+ "] can't send response message!");

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// 初始化线程组
		handlerGroup = new ThreadGroup(mainConfig.getId());

	}

	public class BosentClientHandler implements Runnable {
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
							dumpPort = Integer.parseInt(tmp.substring(tmp
									.indexOf(":") + 1));
						} catch (Exception e) {

						}
					} else {
						dumpIp = tmp.substring(0, tmp.indexOf(":"));
						dumpPort = 80;
					}
				}
			}
		}

		public void run() {

			try {
				String url = config.getUrl();
				initExceptionUtil(url);

				HttpDispacher hdCall = new HttpDispacher();
				hdCall.setServerUrl(url);
				// hdCall.setServerName(config.getServerId());
				if (null != config.getUserName()) {
					hdCall.setUserName(config.getUserName());
				}
				if (null != config.getPassword()) {
					hdCall.setPassword(config.getPassword());
				}
				if (null != config.getContentCharset()) {
					hdCall.setCharSet(config.getContentCharset());
				}

				Map outPara = hdCall.runSync(config.getServerId(), parameters);

				onRequestMessageSent(this, requestMessage);

				if (!async) {
					byte[] responseMessage = null;
					if (null != config.getContentCharset()) {
						responseMessage = MapSerializer.serialize(outPara)
								.getBytes(config.getContentCharset());
					} else {
						responseMessage = MapSerializer.serialize(outPara)
								.getBytes();
					}
					// 解析应答
					if (logger.isInfoEnabled()) {
						logger
								.info("receive response message :\n"
										+ CodeUtil
												.Bytes2FormattedText(responseMessage));
					}
					// 2.4. 产生成功事件
					onResponseMessageArrived(this, requestMessage,
							responseMessage);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("communicate error!", e);

				// 产生错误事件				
				if (CommGateway.isExceptionMonitorSupport()) {
					onRequestMessageSendException(this, requestMessage, e,
							channelId, dumpIp, dumpPort);					
				}else{
					onRequestMessageSendException(this, requestMessage, e);
				}
				return;
			}
		}

		public String toString() {
			StringBuffer buf = new StringBuffer(1024);
			buf.append("Bosent Request: ");
			buf.append(config.getUrl());
			buf.append("?");
			buf.append("serverId=");
			buf.append(config.getServerId());
			if (null != config.getUserName()) {
				buf.append("&");
				buf.append("userName=");
				buf.append(config.getUserName());
			}
			if (null != config.getPassword()) {
				buf.append("&");
				buf.append("password=");
				buf.append(config.getPassword());
			}
			if (null != config.getContentCharset()) {
				buf.append("&");
				buf.append("charset=");
				buf.append(config.getContentCharset());
			}
			Iterator it = parameters.entrySet().iterator();
			Map.Entry entry = null;
			if (it.hasNext()) {
				buf.append("&");
			}
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
		 * @param requestMessage
		 *            the requestMessage to set
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
		 * @param timeout
		 *            the timeout to set
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
		 * @param parameters
		 *            the parameters to set
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

}
