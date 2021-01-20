package com.fib.gateway.channel.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.UnsupportedHttpVersionException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpDateGenerator;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import com.fib.commons.serializer.SerializationUtils;
import com.fib.commons.serializer.xml.XmlSerializer;
import com.fib.gateway.channel.http.config.HTTPServerChannelConfig;
import com.fib.gateway.channel.http.config.HTTPServerChannelConfigParser;
import com.fib.gateway.message.util.ByteBuffer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.channel.Channel;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.session.Session;
import com.fib.gateway.session.SessionManager;


/**
 * HTTP服务通道
 * 
 * @author BaiFan
 * 
 */
public class HTTPServerChannel extends Channel {
	protected HTTPServerChannelConfig config = null;
	/**
	 * HTTP参数
	 */
	protected static BasicHttpParams params = null;
	/**
	 * HTTP应答工厂
	 */
	protected DefaultHttpResponseFactory responseFactory = null;
	/**
	 * 活动线程组
	 */
	protected ThreadGroup handlerGroup = null;
	/**
	 * 服务端口
	 */
	protected ServerSocket serverSocket = null;
	/**
	 * HTTP请求校验器
	 */
	protected HttpExpectationVerifier expectationVerifier = null;
	/**
	 * 服务接入处理器
	 */
	protected ServiceHandler serviceHandler = new ServiceHandler();
	/**
	 * 启动标志
	 */
	protected boolean run = true;

	public void closeSource(Object source) {
		// TODO Auto-generated method stub

	}

	public void loadConfig(InputStream in) {
		
			HTTPServerChannelConfigParser parser = new HTTPServerChannelConfigParser();
			config = parser.parse(in);
		
	}

	public void sendRequestMessage(byte[] requestMessage, boolean isSync,
			int timeout) {

	}

	public void sendResponseMessage(byte[] responseMessage) {
		// 取得会话
		Session session = SessionManager.getSession(responseMessage);
		if( null == session){
			// 超时关闭
			return;
		}

		CustomerHttpServerConnection conn = (CustomerHttpServerConnection) session
				.getSource();
		conn.responseMessage = responseMessage;

		ProcessResponseHandler prHandler = new ProcessResponseHandler();
		prHandler.conn = conn;
		prHandler.start();
	}

	public void shutdown() {
		run = false;
		try {
			serviceHandler.interrupt();
			serviceHandler.join(500);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		if (null != serverSocket) {
			try {
				serverSocket.close();
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		handlerGroup.interrupt();
	}

	public void start() {
		run = true;
		// 创建应答工厂
		responseFactory = new DefaultHttpResponseFactory();
		// 组装HTTP参数
		params = new BasicHttpParams();
		// 设定超时时间
		if (0 < config.getTimeout()) {
			params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, config
					.getTimeout());
		}
		// 设定端口数据缓冲大小
		if (0 < config.getSocketBufferSize()) {
			params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
					config.getSocketBufferSize());
		}
		// 是否启用钝态连接检测
		params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK,
				config.isStaleConnectionCheck());
		// 是否启用Nagle算法
		params.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, config
				.isTcpNoDelay());
		// 设定HTTP Element编码格式
		if (null != config.getElementCharset()) {
			params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, config
					.getElementCharset());
		}
		// 设定HTTP Content编码格式
		if (null != config.getContentCharset()) {
			params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, config
					.getContentCharset());
		}

		handlerGroup = new ThreadGroup(mainConfig.getId());

		serviceHandler = new ServiceHandler();
		serviceHandler.start();
	}

	/**
	 * 服务接入线程
	 * 
	 * @author BaiFan
	 * 
	 */
	private class ServiceHandler extends Thread {
		public void run() {
			Socket socket = null;
			try {
				serverSocket = new ServerSocket(config.getPort());
			} catch (IOException e) {
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"HTTPServerChannel.listen.failed",
								new String[] { mainConfig.getId(),
										e.getMessage() }), e);
			}
			while (run) {
				if (config.getBacklog() < handlerGroup.activeCount()) {
					continue;
				}
				try {
					socket = serverSocket.accept();
					ProcessRequestHandler handler = new ProcessRequestHandler();
					handler.socket = socket;
					Thread t = new Thread(handlerGroup, handler);

					t.start();
				} catch (Exception e) {
//					logger.error(MultiLanguageResourceBundle.getInstance()
//							.getString(
//									"HTTPServerChannel.accept.failed",
//									new String[] { mainConfig.getId(),
//											e.getMessage() }), e);
					if (null != socket) {
						try {
							socket.close();
						} catch (Exception ex) {
							// ex.printStackTrace();
						}
					}
					onAcceptException(this, e);
				}
			}

		}
	}

	/**
	 * 请求处理线程
	 * 
	 * @author BaiFan
	 * 
	 */
	private class ProcessRequestHandler implements Runnable {
		public Socket socket = null;

		public void run() {
			CustomerHttpServerConnection conn = null;
			try {
				conn = new CustomerHttpServerConnection();
				conn.bind(socket, params);
			} catch (Exception e) {
//				logger.error(MultiLanguageResourceBundle.getInstance()
//						.getString(
//								"HTTPServerChannel.bind.failed",
//								new String[] { mainConfig.getId(),
//										e.getMessage() }), e);
				onAcceptException(this, e);
			}
			try {
				HttpResponse response = null;
				HttpContext context = new BasicHttpContext();
				context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
				conn.context = context;
				// 接收http头
				HttpRequest request = conn.receiveRequestHeader();
				ProtocolVersion ver = request.getRequestLine()
						.getProtocolVersion();
				if (!ver.lessEquals(HttpVersion.HTTP_1_1)) {
					ver = HttpVersion.HTTP_1_1;
				}
				// 是否仅存在Http头
				if (request instanceof HttpEntityEnclosingRequest) {
					if (((HttpEntityEnclosingRequest) request).expectContinue()) {
						response = responseFactory.newHttpResponse(ver,
								HttpStatus.SC_CONTINUE, context);
						response.setParams(new DefaultedHttpParams(response
								.getParams(), params));

						if (expectationVerifier != null) {
							try {
								expectationVerifier.verify(request, response,
										context);
							} catch (HttpException ex) {
								response = responseFactory.newHttpResponse(
										HttpVersion.HTTP_1_0,
										HttpStatus.SC_INTERNAL_SERVER_ERROR,
										context);
								response.setParams(new DefaultedHttpParams(
										response.getParams(), params));
								handleException(ex, response);
							}
						}
						if (response.getStatusLine().getStatusCode() < 200) {
							// 启用"预期HTTP100"方式
							conn.sendResponseHeader(response);
							conn.flush();
							response = null;
							conn
									.receiveRequestEntity((HttpEntityEnclosingRequest) request);
						}
					} else {
						conn
								.receiveRequestEntity((HttpEntityEnclosingRequest) request);
					}
					processRequest(conn, request);
				} else {
					processRequest(conn, request);
				}
			} catch (Exception e) {
				if (null != conn) {
					try {
						conn.shutdown();
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}

				if (null != socket) {
					try {
						socket.close();
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}
//				logger.error(MultiLanguageResourceBundle.getInstance()
//						.getString(
//								"HTTPServerChannel.processRequest.error",
//								new String[] { mainConfig.getId(),
//										e.getMessage() }), e);
				onRequestMessageReceiveException(conn, e);
			}
		}

		protected void processRequest(CustomerHttpServerConnection conn,
				HttpRequest request) {

			Map map = null;
			try {
				if ("GET".equals(request.getRequestLine().getMethod())) {
					String uri = request.getRequestLine().getUri();
					map = deserializeHttpRequestString(uri.substring(1));
				} else if ("POST".equals(request.getRequestLine().getMethod())) {
					BasicHttpEntityEnclosingRequest req = (BasicHttpEntityEnclosingRequest) request;

					HttpEntity entity = req.getEntity();

					byte[] msg = EntityUtils.toByteArray(entity);

					map = deserializeHttpRequestString(new String(msg));
				}

				if (null == map) {
					// 抛异常
					throw new RuntimeException(
							MultiLanguageResourceBundle
									.getInstance()
									.getString(
											"HTTPServerChannel.receiveRequest.noParams",
											new String[] { mainConfig.getId() }));
				} else {
					//String reqXml = MapSerializer.serialize(map);
//					logger.info(MultiLanguageResourceBundle.getInstance()
//							.getString(
//									"HTTPServerChannel.receiveRequest.success",
//									new String[] { CodeUtil
//											.Bytes2FormattedText(reqXml
//													.getBytes()) }));
					//conn.requestMessage = reqXml.getBytes();
					conn.requestMessage=SerializationUtils.getInstance().loadSerializerInstance(XmlSerializer.class).serialize(map);
					// 产生请求到达事件
					onRequestMessageArrived(conn, conn.requestMessage);
				}
			} catch (Exception e) {
				ExceptionUtil.throwActualException(e);
			}

		}

		/**
		 * 反序列化Http请求参数字符串
		 * 
		 * @param request
		 * @return
		 */
		protected Map deserializeHttpRequestString(String request) {
			if (null == request || !request.contains("=")) {
				return null;
			}
			String[] entities = request.split("&");
			Map map = new HashMap();
			for (int i = 0; i < entities.length; i++) {
				int separatorIndex = entities[i].indexOf('=');
				String key = entities[i].substring(0, separatorIndex);
				String value = entities[i].substring(separatorIndex + 1);
				map.put(key, formatHttpEntity(value.getBytes()));
			}

			return map;
		}

		/**
		 * 将Http请求字符串中类似%BD的字符串转换为(byte)0xBD
		 * 
		 * @param httpEntity
		 * @return
		 */
		protected String formatHttpEntity(byte[] httpEntity) {
			int length = httpEntity.length;
			int index = 0;
			StringBuffer buf = new StringBuffer();
			while (index < length) {
				if ((byte) 0x25 == httpEntity[index]) {
					ByteBuffer byteBuf = new ByteBuffer();
					while (index < length && (byte) 0x25 == httpEntity[index]) {
						byteBuf.append(httpEntity[++index]);
						byteBuf.append(httpEntity[++index]);
						index++;
					}
					try {
						if (null != config.getContentCharset()) {
							buf.append(new String(CodeUtil.HextoByte(byteBuf
									.toBytes()), config.getContentCharset()));
						} else {
							buf.append(new String(CodeUtil.HextoByte(byteBuf
									.toBytes())));
						}
					} catch (UnsupportedEncodingException e) {
						ExceptionUtil.throwActualException(e);
					}
				} else {
					buf.append((char) httpEntity[index]);
					index++;
				}
			}

			return buf.toString();
		}

		protected void handleException(final HttpException ex,
				final HttpResponse response) {
			if (ex instanceof MethodNotSupportedException) {
				response.setStatusCode(HttpStatus.SC_NOT_IMPLEMENTED);
			} else if (ex instanceof UnsupportedHttpVersionException) {
				response
						.setStatusCode(HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED);
			} else if (ex instanceof ProtocolException) {
				response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
			} else {
				response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			}
			byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
			ByteArrayEntity entity = new ByteArrayEntity(msg);
			entity.setContentType("text/plain; charset=US-ASCII");
			response.setEntity(entity);
		}
	}

	/**
	 * 应答处理线程
	 * 
	 * @author BaiFan
	 * 
	 */
	private class ProcessResponseHandler extends Thread {
		public CustomerHttpServerConnection conn = null;

		public void run() {
			HttpResponse response = responseFactory.newHttpResponse(
					HttpVersion.HTTP_1_0, HttpStatus.SC_OK, conn.context);

			HttpEntity entity = new ByteArrayEntity(conn.responseMessage);
			response.setEntity(entity);
			sendResponse(response, conn);
			try {
				conn.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}

		protected void sendResponse(HttpResponse response,
				CustomerHttpServerConnection conn) {
			try {
				// 应答头中添加应答内容长度
				if (response.getEntity() != null) {
					response.addHeader(HTTP.CONTENT_LEN, Long.toString(response
							.getEntity().getContentLength()));
				} else {
					response.addHeader(HTTP.CONTENT_LEN, "0");
				}
				String httpdate = new HttpDateGenerator().getCurrentDate();
				response.setHeader(HTTP.DATE_HEADER, httpdate);
				// 发送应答
				conn.sendResponseHeader(response);
				conn.sendResponseEntity(response);
				conn.flush();
				onResponseMessageSent(conn, conn.responseMessage);
			} catch (Exception e) {
//				logger.error(MultiLanguageResourceBundle.getInstance()
//						.getString(
//								"HTTPServerChannel.sendResponse.failed",
//								new String[] { mainConfig.getId(),
//										e.getMessage() }), e);
				onResponseMessageSendException(conn, conn.requestMessage, e);
			}
		}
	}

	public class CustomerHttpServerConnection extends
			DefaultHttpServerConnection {
		public byte[] requestMessage;

		public byte[] responseMessage;

		public HttpContext context;

		public CustomerHttpServerConnection() {
			super();
		}
	}

	public HTTPServerChannelConfig getConnectorConfig() {
		return config;
	}

	public void setConnectorConfig(HTTPServerChannelConfig config) {
		this.config = config;
	}

}
