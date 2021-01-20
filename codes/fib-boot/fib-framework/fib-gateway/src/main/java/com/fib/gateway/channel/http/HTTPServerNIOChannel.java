package com.fib.gateway.channel.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

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
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.NHttpServiceHandler;
import org.apache.http.nio.entity.BufferingNHttpEntity;
import org.apache.http.nio.entity.ConsumingNHttpEntity;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.nio.entity.NHttpEntityWrapper;
import org.apache.http.nio.entity.ProducingNHttpEntity;
import org.apache.http.nio.protocol.NHttpRequestHandler;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

import com.fib.commons.util.ClassUtil;
import com.fib.gateway.channel.http.config.HTTPServerChannelConfig;
import com.fib.gateway.channel.http.config.HTTPServerChannelConfigParser;
import com.fib.gateway.message.util.ByteBuffer;
import com.fib.gateway.message.util.CodeUtil;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.channel.Channel;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;
import com.fib.gateway.session.Session;
import com.fib.gateway.session.SessionManager;


public class HTTPServerNIOChannel extends Channel {

	private HTTPServerChannelConfig config = null;
	/**
	 * HTTP服务处理器
	 */
	protected HTTPServiceHandler handler = null;
	/**
	 * HTTP参数
	 */
	protected BasicHttpParams params = null;
	/**
	 * HTTP请求校验器
	 */
	protected HttpExpectationVerifier expectationVerifier = null;
	/**
	 * HTTP应答工厂
	 */
	protected DefaultHttpResponseFactory responseFactory = null;
	/**
	 * 连接复用测试检测器
	 */
	protected DefaultConnectionReuseStrategy connStrategy = new DefaultConnectionReuseStrategy();

	public static final String CONN_STATE = "connect-state";

	public void closeSource(Object source) {
		NHttpServerConnection conn = (NHttpServerConnection) source;
		handler.closeConnection(conn);
	}

	public void loadConfig(InputStream in) {
		HTTPServerChannelConfigParser parser = new HTTPServerChannelConfigParser();
		config = parser.parse(in);
	}

	public void sendRequestMessage(byte[] requestMessage, boolean isSync,
			int timeout) {
		// 无此功能
	}

	public void sendResponseMessage(byte[] responseMessage) {
		// 取得会话
		Session session = SessionManager.getSession(responseMessage);
		System.out.println("whs1"+session);
		
		if( null == session){
			// 超时关闭
			return;
		}
		NHttpServerConnection conn = (NHttpServerConnection) session
				.getSource();
		HttpResponse response = responseFactory.newHttpResponse(
				HttpVersion.HTTP_1_0, HttpStatus.SC_OK, conn.getContext());
		response.setEntity(new ByteArrayEntity(responseMessage));
		ServerConnState connState = (ServerConnState) conn.getContext()
				.getAttribute(CONN_STATE);
		connState.setResponse(response);
		connState.setResponseMessage(responseMessage);
		try {
			handler.sendResponse(conn);
		} catch (Exception e) {
			onResponseMessageSendException(conn, responseMessage, e);
		}
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	public void start() {
		// 创建应答工厂
		responseFactory = new DefaultHttpResponseFactory();
		// 组装HTTP参数
		params = new BasicHttpParams();
		// 设定超时时间
		if (0 < config.getTimeout()) {
			params.setLongParameter(CoreConnectionPNames.SO_TIMEOUT, config
					.getTimeout());
		}
		// 设定端口数据缓冲大小
		if (0 < config.getSocketBufferSize()) {
			params.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
					config.getSocketBufferSize());
			System.out.println("whs2"+config.getSocketBufferSize());
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
		if (null != config.getVerifierConfig()) {
			expectationVerifier = (HttpExpectationVerifier) ClassUtil
					.createClassInstance(config.getVerifierConfig()
							.getClassName());
		}
		// 创建处理器
		handler = new HTTPServiceHandler();
		// 创建IO事件派发器
		IOEventDispatch ioEventDispatch = new DefaultServerIOEventDispatch(
				handler, params);
		// 创建IO事件反应器
		RunnableIOReactor ioReactor = null;
		try {
			int reactorNum = 1;
			if (1 < config.getBacklog()) {
				reactorNum = config.getBacklog();
			}
			ioReactor = new RunnableIOReactor(reactorNum, params);
		} catch (IOReactorException e) {
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance()
							.getString(
									"HTTPServerChannel.createReactor.error",
									new String[] { mainConfig.getId(),
											e.getMessage() }), e);
		}
		try {
			// 监听端口
			ioReactor.listen(new InetSocketAddress(config.getPort()));
			// 启动
			ioReactor.setIoEventDispatch(ioEventDispatch);
			new Thread(ioReactor).start();
		} catch (Exception e) {
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance()
							.getString(
									"HTTPServerChannel.reactorStart.error",
									new String[] { mainConfig.getId(),
											e.getMessage() }), e);
		}
	}

	public HTTPServerChannelConfig getConfig() {
		return config;
	}

	public void setConfig(HTTPServerChannelConfig config) {
		this.config = config;
	}

	private class HTTPServiceHandler implements NHttpServiceHandler {
		/**
		 * 响应关闭事件
		 */
		public void closed(NHttpServerConnection conn) {
			HttpContext context = conn.getContext();

			ServerConnState connState = (ServerConnState) context
					.getAttribute(CONN_STATE);
			try {
				connState.reset();
			} catch (IOException ex) {
				// ex.printStackTrace();
			}
		}

		/**
		 * 响应连接事件
		 */
		public void connected(NHttpServerConnection conn) {
			HttpContext context = conn.getContext();

			ServerConnState connState = new ServerConnState();
			context.setAttribute(CONN_STATE, connState);
			context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
		}

		/**
		 * 响应Http异常事件
		 */
		public void exception(NHttpServerConnection conn, HttpException httpex) {
			if (conn.isResponseSubmitted()) {

				closeConnection(conn);
				return;
			}

			HttpContext context = conn.getContext();
			try {
				// 准备内部服务器错误应答
				HttpResponse response = responseFactory.newHttpResponse(
						HttpVersion.HTTP_1_0,
						HttpStatus.SC_INTERNAL_SERVER_ERROR, context);
				response.setParams(new DefaultedHttpParams(
						response.getParams(), params));
				handleException(httpex, response);
				response.setEntity(null);
				sendResponse(conn);

			} catch (IOException ex) {
				shutdownConnection(conn, ex);
			} catch (HttpException ex) {
				closeConnection(conn);
			}

		}

		/**
		 * 响应IO异常事件
		 */
		public void exception(NHttpServerConnection conn, IOException ex) {
			shutdownConnection(conn, ex);

		}

		/**
		 * 响应准备接受事件
		 */
		public void inputReady(NHttpServerConnection conn,
				ContentDecoder decoder) {
			// 取得服务连接状态
			HttpContext context = conn.getContext();
			ServerConnState connState = (ServerConnState) context
					.getAttribute(CONN_STATE);

			HttpRequest request = connState.getRequest();
			ConsumingNHttpEntity consumingEntity = connState
					.getConsumingEntity();

			try {
				// 读取content
				consumingEntity.consumeContent(decoder, conn);
				if (decoder.isCompleted()) {
					conn.suspendInput();
					processRequest(conn, request);
				}
			} catch (IOException ex) {
				shutdownConnection(conn, ex);
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString(
								"HTTPServerChannel.receiveRequest.IOException",
								new String[] { mainConfig.getId(),
										ex.getMessage() }), ex);
				onRequestMessageReceiveException(conn, ex);
			}

		}

		/**
		 * 响应准备输出事件
		 */
		public void outputReady(NHttpServerConnection conn,
				ContentEncoder encoder) {

			HttpContext context = conn.getContext();
			ServerConnState connState = (ServerConnState) context
					.getAttribute(CONN_STATE);

			HttpResponse response = conn.getHttpResponse();

			try {
				ProducingNHttpEntity entity = connState.getProducingEntity();
				entity.produceContent(encoder, conn);
			} catch (IOException ex) {
				shutdownConnection(conn, ex);
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString(
								"HTTPServerChannel.sendResponse.IOException",
								new String[] { mainConfig.getId(),
										ex.getMessage() }), ex);
				onResponseMessageSendException(conn, connState
						.getResponseMessage(), ex);
			} finally {
				try {
					if (encoder.isCompleted()) {
						connState.finishOutput();
						if (!connStrategy.keepAlive(response, context)) {
							conn.close();
						} else {
							// 准备处理新的request请求
							connState.reset();
							conn.requestInput();
						}
					}
				} catch (Exception e) {
					// e.printStackTrace();
					logger.error("eee",e);
				}
			}

		}

		/**
		 * 响应request到达事件
		 */
		public void requestReceived(NHttpServerConnection conn) {
			try {
				// 取得连接上下文
				HttpContext context = conn.getContext();
				// 取得服务连接状态
				ServerConnState connState = (ServerConnState) context
						.getAttribute(CONN_STATE);
				// 取得已接收的HTTP请求
				HttpRequest request = conn.getHttpRequest();
				request.setParams(new DefaultedHttpParams(request.getParams(),
						params));
				// 设置HTTP协议版本
				ProtocolVersion ver = request.getRequestLine()
						.getProtocolVersion();
				if (!ver.lessEquals(HttpVersion.HTTP_1_1)) {
					ver = HttpVersion.HTTP_1_1;
				}

				connState.setRequest(request);
				if (request instanceof HttpEntityEnclosingRequest) {
					// 是否启用"预期HTTP100"方式
					if (((HttpEntityEnclosingRequest) request).expectContinue()) {
						HttpResponse response = responseFactory
								.newHttpResponse(ver, HttpStatus.SC_CONTINUE,
										context);
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
							conn.submitResponse(response);
						} else {
							conn.resetInput();
							sendResponse(conn);
						}
					}
					// 含有HTTP参数集合的请求
//					BasicHttpEntityEnclosingRequest req = (BasicHttpEntityEnclosingRequest) request;

					ConsumingNHttpEntity consumingEntity = entityRequest(
							(HttpEntityEnclosingRequest) request, context);

					((HttpEntityEnclosingRequest) request)
							.setEntity(consumingEntity);
					connState.setConsumingEntity(consumingEntity);
				} else {
					// 只含有HTTP头的请求
					conn.suspendInput();
					processRequest(conn, request);
				}
			} catch (IOException ex) {
				shutdownConnection(conn, ex);
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString(
								"HTTPServerChannel.receiveRequest.IOException",
								new String[] { mainConfig.getId(),
										ex.getMessage() }), ex);
				onRequestMessageReceiveException(conn, ex);
			} catch (HttpException ex) {
				closeConnection(conn);
				logger
						.error(
								MultiLanguageResourceBundle
										.getInstance()
										.getString(
												"HTTPServerChannel.receiveRequest.HttpException",
												new String[] {
														mainConfig.getId(),
														ex.getMessage() }), ex);
				onRequestMessageReceiveException(conn, ex);
			}

		}

		/**
		 * 响应准备发送response事件
		 */
		public void responseReady(NHttpServerConnection conn) {

		}

		/**
		 * 响应超时事件
		 */
		public void timeout(NHttpServerConnection conn) {
			handleTimeout(conn);

		}

		/**
		 * 处理请求
		 * 
		 * @param conn
		 * @param request
		 * @throws IOException
		 * @throws HttpException
		 */
		private void processRequest(NHttpServerConnection conn,
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
					HttpResponse response = responseFactory.newHttpResponse(
							HttpVersion.HTTP_1_0,
							HttpStatus.SC_INTERNAL_SERVER_ERROR, conn
									.getContext());
					((ServerConnState) conn.getContext().getAttribute(
							CONN_STATE)).setResponse(response);
					onRequestMessageReceiveException(
							conn,
							new RuntimeException(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"HTTPServerChannel.receiveRequest.noParams",
													new String[] { mainConfig
															.getId() })));
					return;
				} else {
					String reqXml = "";//MapSerializer.serialize(map);
					logger.info(MultiLanguageResourceBundle.getInstance()
							.getString(
									"HTTPServerChannel.receiveRequest.success",
									new String[] { CodeUtil
											.Bytes2FormattedText(reqXml
													.getBytes()) }));
					// 更新服务连接状态
					((ServerConnState) conn.getContext().getAttribute(
							CONN_STATE)).setRequestMessage(reqXml.getBytes());
					// 产生请求到达事件
					onRequestMessageArrived(conn, reqXml.getBytes());
				}
			} catch (Exception e) {
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString(
								"HTTPServerChannel.processRequest.error",
								new String[] { mainConfig.getId(),
										e.getMessage() }), e);
				HttpResponse response = responseFactory.newHttpResponse(
						HttpVersion.HTTP_1_0,
						HttpStatus.SC_INTERNAL_SERVER_ERROR, conn.getContext());
				((ServerConnState) conn.getContext().getAttribute(CONN_STATE))
						.setResponse(response);
				onRequestMessageReceiveException(conn, e);
			}
		}

		private void sendResponse(final NHttpServerConnection conn)
				throws IOException, HttpException {
			HttpContext context = conn.getContext();
			ServerConnState connState = (ServerConnState) context
					.getAttribute(CONN_STATE);
			HttpRequest request = connState.getRequest();
			HttpResponse response = connState.getResponse();
			byte[] responseMessage = connState.getResponseMessage();
			// 关闭输入
			connState.finishInput();

			if (response.getEntity() != null
					&& !canResponseHaveBody(request, response)) {
				response.setEntity(null);
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (entity instanceof ProducingNHttpEntity) {
					connState.setProducingEntity((ProducingNHttpEntity) entity);
				} else {
					connState
							.setProducingEntity(new NHttpEntityWrapper(entity));
				}
			}

			conn.submitResponse(response);

			if (entity == null) {
				if (!connStrategy.keepAlive(response, context)) {
					conn.close();
				} else {
					// 准备处理新的request请求
					connState.reset();
					conn.requestInput();
				}
			}

			// 关闭连接
			connState.reset();
			closeConnection(conn);

			onResponseMessageSent(conn, responseMessage);
		}

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

		protected boolean canResponseHaveBody(final HttpRequest request,
				final HttpResponse response) {

			if (request != null
					&& "HEAD".equalsIgnoreCase(request.getRequestLine()
							.getMethod())) {
				return false;
			}

			int status = response.getStatusLine().getStatusCode();
			return status >= HttpStatus.SC_OK
					&& status != HttpStatus.SC_NO_CONTENT
					&& status != HttpStatus.SC_NOT_MODIFIED
					&& status != HttpStatus.SC_RESET_CONTENT;
		}

		private void handleException(final HttpException ex,
				final HttpResponse response) {
			int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			if (ex instanceof MethodNotSupportedException) {
				code = HttpStatus.SC_NOT_IMPLEMENTED;
			} else if (ex instanceof UnsupportedHttpVersionException) {
				code = HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED;
			} else if (ex instanceof ProtocolException) {
				code = HttpStatus.SC_BAD_REQUEST;
			}
			response.setStatusCode(code);

			byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
			NByteArrayEntity entity = new NByteArrayEntity(msg);
			entity.setContentType("text/plain; charset=US-ASCII");
			response.setEntity(entity);
		}

		protected void handleTimeout(final NHttpConnection conn) {
			try {
				if (conn.getStatus() == NHttpConnection.ACTIVE) {
					conn.close();
					if (conn.getStatus() == NHttpConnection.CLOSING) {
						conn.setSocketTimeout(250);
					}

				} else {
					conn.shutdown();
				}
			} catch (IOException ignore) {
				// ignore.printStackTrace();
			}
		}

		private ConsumingNHttpEntity entityRequest(
				final HttpEntityEnclosingRequest request,
				final HttpContext context) throws HttpException, IOException {
			return new BufferingNHttpEntity(request.getEntity(),
					new HeapByteBufferAllocator());
		}

		private void shutdownConnection(final NHttpConnection conn,
				final Throwable cause) {
			try {
				conn.shutdown();
			} catch (IOException ex) {
				// ex.printStackTrace();
			}
		}

		private void closeConnection(final NHttpConnection conn) {
			try {
				conn.close();
			} catch (IOException ex) {
				try {
					conn.shutdown();
				} catch (IOException excp) {
					// excp.printStackTrace();
				}
			}
		}
	}

	/**
	 * 服务连接状态
	 * 
	 * @author BaiFan
	 * 
	 */
	protected static class ServerConnState {

		private volatile NHttpRequestHandler requestHandler;
		private volatile HttpRequest request;
		private volatile ConsumingNHttpEntity consumingEntity;
		private volatile HttpResponse response;
		private volatile ProducingNHttpEntity producingEntity;
		private volatile IOException ioex;
		private volatile HttpException httpex;
		private volatile byte[] requestMessage;
		private volatile byte[] responseMessage;
		private volatile boolean handled;

		public void finishInput() throws IOException {
			if (consumingEntity != null) {
				consumingEntity.finish();
				consumingEntity = null;
			}
		}

		public void finishOutput() throws IOException {
			if (producingEntity != null) {
				producingEntity.finish();
				producingEntity = null;
			}
		}

		public void reset() throws IOException {
			finishInput();
			request = null;
			finishOutput();
			handled = false;
			response = null;
			ioex = null;
			httpex = null;
			requestHandler = null;
			requestMessage = null;
			responseMessage = null;
		}

		public NHttpRequestHandler getRequestHandler() {
			return requestHandler;
		}

		public void setRequestHandler(final NHttpRequestHandler requestHandler) {
			this.requestHandler = requestHandler;
		}

		public HttpRequest getRequest() {
			return request;
		}

		public void setRequest(final HttpRequest request) {
			this.request = request;
		}

		public ConsumingNHttpEntity getConsumingEntity() {
			return consumingEntity;
		}

		public void setConsumingEntity(
				final ConsumingNHttpEntity consumingEntity) {
			this.consumingEntity = consumingEntity;
		}

		public HttpResponse getResponse() {
			return response;
		}

		public void setResponse(final HttpResponse response) {
			this.response = response;
		}

		public ProducingNHttpEntity getProducingEntity() {
			return producingEntity;
		}

		public void setProducingEntity(
				final ProducingNHttpEntity producingEntity) {
			this.producingEntity = producingEntity;
		}

		public IOException getIOExepction() {
			return ioex;
		}

		public void setIOExepction(final IOException ex) {
			ioex = ex;
		}

		public HttpException getHttpExepction() {
			return httpex;
		}

		public void setHttpExepction(final HttpException ex) {
			httpex = ex;
		}

		public boolean isHandled() {
			return handled;
		}

		public void setHandled(boolean handled) {
			this.handled = handled;
		}

		public byte[] getRequestMessage() {
			return requestMessage;
		}

		public void setRequestMessage(byte[] requestMessage) {
			this.requestMessage = requestMessage;
		}

		public byte[] getResponseMessage() {
			return responseMessage;
		}

		public void setResponseMessage(byte[] responseMessage) {
			this.responseMessage = responseMessage;
		}

	}

	private static class RunnableIOReactor extends DefaultListeningIOReactor
			implements Runnable {
		public RunnableIOReactor(int workerCount, final HttpParams params)
				throws IOReactorException {
			super(workerCount, params);
		}

		public RunnableIOReactor(int workerCount,
				final ThreadFactory threadFactory, final HttpParams params)
				throws IOException {
			super(workerCount, threadFactory, params);
		}

		private IOEventDispatch ioEventDispatch;

		public IOEventDispatch getIoEventDispatch() {
			return ioEventDispatch;
		}

		public void setIoEventDispatch(IOEventDispatch ioEventDispatch) {
			this.ioEventDispatch = ioEventDispatch;
		}

		public void run() {
			try {
				super.execute(ioEventDispatch);
			} catch (Exception e) {
				ExceptionUtil.throwActualException(e);
			}
		}
	}
}
