package com.fib.msgconverter.commgateway.client;

import java.util.Map;

import org.apache.log4j.Logger;

import com.giantstone.common.map.client.MapClient;

public class GatewayClient {
	private String ip;
	private int port;
	private int timeout = 30000;
	private MapClient client;
	private Logger logger = null;
	private static boolean DEBUG = false;
	private static final String EXTERNALSERIALNUM = "EXTERNALSERIALNUM";
	private boolean useException = false;

	public boolean isUseException() {
		return useException;
	}

	public void setUseException(boolean useException) {
		this.useException = useException;
	}

	public GatewayClient() {

	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param timeout
	 */
	public GatewayClient(String ip, int port, int timeout) {
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}

	/**
	 * 同步方式发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @return 应答数据Map
	 * @throws RuntimeException
	 */
	public Map send(Map map) {
		return send(map, false, null);
	}

	/**
	 * 同步方式发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param serialNumber
	 *            外部流水号
	 * @return 应答数据Map
	 * @throws RuntimeException
	 */
	public Map send(Map map, String serialNumber) {
		return send(map, serialNumber, false, null);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param asyn
	 *            异步
	 * @return 应答数据Map
	 * @throws RuntimeException
	 */
	public Map send(Map map, boolean isAsyn) {
		return send(map, isAsyn, null);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param asyn
	 *            异步
	 * @param serialNumber
	 *            流水号
	 * @return 应答数据Map
	 * @throws RuntimeException
	 */
	public Map send(Map map, String serialNumber, boolean isAsyn) {
		return send(map, serialNumber, isAsyn, null);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param isAsyn
	 *            异步标志
	 * @param timeout
	 *            超时
	 * @return
	 */
	public Map send(Map map, boolean isAsyn, int timeout) {
		return send(map, isAsyn, null, timeout);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param isAsyn
	 *            异步标志
	 * @param timeout
	 *            超时
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	public Map send(Map map, String serialNumber, boolean isAsyn, int timeout) {
		return send(map, serialNumber, isAsyn, null, timeout);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param isAsyn
	 *            异步标志
	 * @param charset
	 *            按某个字符集组码发送,并按该字符集解码回应
	 * @param timeout
	 *            超时
	 * @return
	 */
	public Map send(Map map, boolean isAsyn, String charset, int timeout) {
		this.timeout = timeout;
		return send(map, isAsyn, charset);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param isAsyn
	 *            异步标志
	 * @param charset
	 *            按某个字符集组码发送,并按该字符集解码回应
	 * @param timeout
	 *            超时
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	public Map send(Map map, String serialNumber, boolean isAsyn,
			String charset, int timeout) {
		this.timeout = timeout;
		return send(map, serialNumber, isAsyn, charset);

	}
	

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param asyn
	 *            异步
	 * @param charset
	 *            按某个字符集组码发送,并按该字符集解码回应
	 * @return 应答数据Map
	 */
	public Map send(Map map, boolean isAsyn, String charset) {
		return send(map, isAsyn, charset, null, useException);
	}
	
	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param isAsyn
	 *            异步标志
	 * @param charset
	 *            按某个字符集组码发送,并按该字符集解码回应
	 * @param serialNumber
	 *            流水号
	 * @return
	 */
	public Map send(Map map, String serialNumber, boolean isAsyn, String charset) {
		return send(map, isAsyn, charset, serialNumber, useException);
	}

	/**
	 * 发送Map到服务器
	 * 
	 * @param map
	 *            交易数据Map
	 * @param isAsyn
	 *            异步
	 * @param charset
	 *            按某个字符集组码发送,并按该字符集解码回应
	 * @param serialNumber
	 *            流水号,当值为空且useException=true时,默认流水号为TransactionId
	 * @param useException
	 *            是否与异常处理平台集成
	 * @return 应答数据Map
	 */
	public Map send(Map map, boolean isAsyn, String charset,
			String serialNumber, boolean useException) {
		if (null == ip) {
			throw new RuntimeException("ip is NULL!");
		}
		if (0 == ip.length()) {
			throw new RuntimeException("ip is Blank!");
		}
		if (0 == port) {
			throw new RuntimeException("port is NULL!");
		}
		if (null == map) {
			throw new RuntimeException("map is NULL!");
		}
		if (null != serialNumber && 32 < serialNumber.length()) {
			throw new RuntimeException(
					"serialNumber's length must be less than 32!serialNumber is "
							+ serialNumber);
		}
		if (map.containsKey(EXTERNALSERIALNUM)) {
			throw new RuntimeException(EXTERNALSERIALNUM + " is keyword!");
		}

		client = new MapClient(ip, port, timeout);
		client.setLogger(logger);
		client.setDEBUG(DEBUG);
		return client.send(map, isAsyn, charset, serialNumber, useException);

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}

}
