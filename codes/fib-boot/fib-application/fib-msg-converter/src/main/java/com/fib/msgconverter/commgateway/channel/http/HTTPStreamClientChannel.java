package com.fib.msgconverter.commgateway.channel.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.ConnectException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.http.config.HTTPClientChannelConfig;
import com.fib.msgconverter.commgateway.channel.http.config.HTTPClientChannelConfigParser;
import com.fib.msgconverter.commgateway.channel.http.config.database.HTTPClientChannelConfigLoader;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.util.CodeUtil;
import com.fib.msgconverter.commgateway.util.A;

public class HTTPStreamClientChannel extends Channel {
	protected HTTPClientChannelConfig config;
	protected ThreadGroup handlerGroup = null;
	public static final String NEW_LINE = System.getProperty("line.separator");

	public HTTPStreamClientChannel() {
	}

	public void setConnectorConfig(HTTPClientChannelConfig var1) {
		this.config = var1;
	}

	public void closeSource(Object var1) {
	}

	public void loadConfig(InputStream var1) {
		if (CommGateway.isConfigDBSupport()) {
			HTTPClientChannelConfigLoader var2 = new HTTPClientChannelConfigLoader();
			this.config = var2.loadConfig(this.channelConfig.getConnectorId());
		} else {
			HTTPClientChannelConfigParser var3 = new HTTPClientChannelConfigParser();
			this.config = var3.parse(var1);
		}

	}

	public void sendRequestMessage(byte[] var1, boolean var2, int var3) {
		int var4 = this.handlerGroup.activeCount();
		if (var4 >= this.config.getMaxHandlerNumber()) {
			String var7 = MultiLanguageResourceBundle.getInstance()
					.getString("HTTPClientChannel.sendRequestMessage.noHandler", new String[] { "" + var4 });
			this.logger.error(var7);
			this.onRequestMessageSendException(this.handlerGroup, var1, new RuntimeException(var7));
		} else {
			HTTPStreamClientChannel.HTTPClientHandler var5 = new HTTPStreamClientChannel.HTTPClientHandler();
			var5.setRequestMessage(var1);
			var5.setAsync(var2);
			var5.setTimeout(var3);
			var5.setChannelId(this.getId());
			Thread var6 = new Thread(this.handlerGroup, var5);
			var6.start();
		}
	}

	protected byte[] parseResponseBody(byte[] var1) {
		return var1;
	}

	public void sendResponseMessage(byte[] var1) {
		throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
				.getString("HTTPClientChannel.sendResponseMessage.error", new String[] { this.mainConfig.getId() }));
	}

	public void shutdown() {
	}

	public void start() {
		this.handlerGroup = new ThreadGroup(this.mainConfig.getId());
	}

	public HTTPClientChannelConfig getConnectorConfig() {
		return this.config;
	}

	public class HTTPClientHandler implements Runnable {
		private String channelId;
		private String dumpIp;
		private int dumpPort;
		private byte[] requestMessage = null;
		private boolean async;
		private int timeout = 30000;

		public HTTPClientHandler() {
		}

		private void initExceptionUtil(String var1) {
			if (CommGateway.isExceptionMonitorSupport()) {
				boolean var2 = false;
				if (-1 != var1.indexOf("//")) {
					int var7 = var1.indexOf("//") + 2;
					int var3 = 0;
					if (-1 != var1.indexOf("/", var7)) {
						var3 = var1.indexOf("/", var7);
					}

					String var4 = var1.substring(var7, var3);
					if (-1 != var4.indexOf(":")) {
						try {
							this.dumpIp = var4.substring(0, var4.indexOf(":"));
							this.dumpPort = Integer.parseInt(var4.substring(var4.indexOf(":") + 1));
						} catch (Exception var6) {
							;
						}
					} else {
						this.dumpIp = var4;
						this.dumpPort = 80;
					}
				}
			}

		}

		public void run() {
			PostMethod var1 = null;

			try {
				String var2 = HTTPStreamClientChannel.this.config.getUrl();
				this.initExceptionUtil(var2);
				if ("https:".equals(var2.substring(0, var2.indexOf("//")))) {
					Protocol var3 = new Protocol("https", new NoPKISocketFactory(), 443);
					Protocol.registerProtocol("https", var3);
				}

				var1 = new PostMethod(HTTPStreamClientChannel.this.config.getUrl());
				ByteArrayInputStream var17 = new ByteArrayInputStream(this.requestMessage);
				var1.setRequestBody(var17);
			} catch (Exception var15) {
				HTTPStreamClientChannel.this.logger.error(
						MultiLanguageResourceBundle.getInstance().getString("HTTPClientHandler.run.createMethod.error",
								new String[] { CodeUtil.Bytes2FormattedText(this.requestMessage) }));
				HTTPStreamClientChannel.this.onRequestMessageSendException(this, this.requestMessage, var15);
				return;
			}

			try {
				HttpClientParams var16 = new HttpClientParams();
				var16.setSoTimeout(this.timeout);
				if (HTTPStreamClientChannel.this.config.getContentCharset() != null) {
					var16.setContentCharset(HTTPStreamClientChannel.this.config.getContentCharset());
				}

				HttpClient var18 = new HttpClient(var16, new SimpleHttpConnectionManager(true));
				if (null != HTTPStreamClientChannel.this.logger && HTTPStreamClientChannel.this.logger.isInfoEnabled()
						&& null != this.requestMessage) {
					Object var4 = null;
					byte[] var19;
					if (CommGateway.isShieldSensitiveFields()) {
						var19 = A.A(new String(this.requestMessage), CommGateway.getSensitiveFields(),
								CommGateway.getSensitiveReplaceChar()).getBytes();
					} else {
						var19 = this.requestMessage;
					}

					HTTPStreamClientChannel.this.logger.info(MultiLanguageResourceBundle.getInstance().getString(
							"SocketClientChannel.handleKey.sendRequest.success",
							new String[] { CodeUtil.Bytes2FormattedText(var19) }));
				}

				int var20 = var18.executeMethod(var1);
				if (var20 == 200 || var20 == 302) {
					HTTPStreamClientChannel.this.onRequestMessageSent(this, this.requestMessage);
					if (this.async) {
						return;
					}

					byte[] var5 = HTTPStreamClientChannel.this.parseResponseBody(var1.getResponseBody());
					if (HTTPStreamClientChannel.this.logger.isInfoEnabled() && null != var5) {
						Object var6 = null;
						byte[] var21;
						if (CommGateway.isShieldSensitiveFields()) {
							var21 = A.A(new String(var5), CommGateway.getSensitiveFields(),
									CommGateway.getSensitiveReplaceChar()).getBytes();
						} else {
							var21 = var5;
						}

						HTTPStreamClientChannel.this.logger.info(MultiLanguageResourceBundle.getInstance().getString(
								"SocketClientChannel.handleKey.receiveResponse.success",
								new String[] { CodeUtil.Bytes2FormattedText(var21) }));
					}

					HTTPStreamClientChannel.this.onResponseMessageArrived(this, this.requestMessage, var5);
					return;
				}

				if (CommGateway.isExceptionMonitorSupport()) {
					HTTPStreamClientChannel.this.onResponseMessageReceiveException(this, this.requestMessage,
							new RuntimeException(var1.getStatusLine().toString()), this.channelId, this.dumpIp,
							this.dumpPort);
				} else {
					HTTPStreamClientChannel.this.onResponseMessageReceiveException(this, this.requestMessage,
							new RuntimeException(var1.getStatusLine().toString()));
				}

				return;
			} catch (ConnectException var12) {
				HTTPStreamClientChannel.this.logger.error(
						MultiLanguageResourceBundle.getInstance().getString("HTTPClientHandler.run.connect.error"),
						var12);
				if (CommGateway.isExceptionMonitorSupport()) {
					HTTPStreamClientChannel.this.onConnectException(this, this.requestMessage, var12, this.channelId,
							this.dumpIp, this.dumpPort);
				} else {
					HTTPStreamClientChannel.this.onConnectException(this, this.requestMessage, var12);
				}

				return;
			} catch (Exception var13) {
				HTTPStreamClientChannel.this.logger.error(
						MultiLanguageResourceBundle.getInstance().getString("HTTPClientHandler.run.communicate.error"),
						var13);
				if (CommGateway.isExceptionMonitorSupport()) {
					HTTPStreamClientChannel.this.onRequestMessageSendException(this, this.requestMessage, var13,
							this.channelId, this.dumpIp, this.dumpPort);
				} else {
					HTTPStreamClientChannel.this.onRequestMessageSendException(this, this.requestMessage, var13);
				}
			} finally {
				var1.releaseConnection();
			}

		}

		public String toString() {
			StringBuffer var1 = new StringBuffer(1024);
			var1.append("HTTP Request: ");
			var1.append(HTTPStreamClientChannel.this.config.getUrl());
			var1.append(HTTPStreamClientChannel.NEW_LINE);
			var1.append("Message: ");
			var1.append(CodeUtil.Bytes2FormattedText(this.requestMessage));
			return var1.toString();
		}

		public byte[] getRequestMessage() {
			return this.requestMessage;
		}

		public void setRequestMessage(byte[] var1) {
			this.requestMessage = var1;
		}

		public int getTimeout() {
			return this.timeout;
		}

		public void setTimeout(int var1) {
			this.timeout = var1;
		}

		public boolean isAsync() {
			return this.async;
		}

		public void setAsync(boolean var1) {
			this.async = var1;
		}

		public void setChannelId(String var1) {
			this.channelId = var1;
		}
	}
}
