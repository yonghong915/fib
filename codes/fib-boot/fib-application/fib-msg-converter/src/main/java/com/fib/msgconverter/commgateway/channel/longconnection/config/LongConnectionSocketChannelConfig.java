package com.fib.msgconverter.commgateway.channel.longconnection.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.nio.config.ReaderConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.WriterConfig;

/**
 * 长连接通道配置
 * 
 * @author 刘恭亮
 * 
 */
public class LongConnectionSocketChannelConfig {
	/**
	 * 连接配置
	 */
	private Map<String, ConnectionConfig> connectionConfigs = new HashMap<>();

	/**
	 * 登陆配置
	 */
	private LoginConfig loginConfig;

	/**
	 * 心跳包配置
	 */
	private List<HeartbeatConfig> heartbeatConfigs = new ArrayList<>();

	/**
	 * 报文符号表配置
	 */
	private Map<String,MessageSymbol> messageSymbolTable = new HashMap<>(5);

	/**
	 * 消息接收器配置
	 */
	private ReaderConfig readerConfig;

	/**
	 * 消息发送器配置
	 */
	private WriterConfig writerConfig;

	/**
	 * 请求流水号识别器ID
	 */
	private String requestSerialNumberRecognizerId;

	/**
	 * 应答流水号识别器ID
	 */
	private String responseSerialNumberRecognizerId;

	/**
	 * 报文识别器配置
	 */
	private CodeRecognizerConfig codeRecognizerConfig;

	/**
	 * 请求流水号识别器
	 */
	private AbstractMessageRecognizer requestSerialNumberRecognizer;

	/**
	 * 应答流水号识别器
	 */
	private AbstractMessageRecognizer responseSerialNumberRecognizer;

	/**
	 * 报文码识别器，用于判断报文是请求报文还是应答报文
	 */
	private AbstractMessageRecognizer codeRecognizer;

	/**
	 * @return the connectionConfigs
	 */
	public Map<String, ConnectionConfig> getConnectionConfigs() {
		return connectionConfigs;
	}

	public ConnectionConfig getConnectionConfig(String connectionId) {
		return (ConnectionConfig) connectionConfigs.get(connectionId);
	}

	/**
	 * @param connectionConfigs the connectionConfigs to set
	 */
	public void setConnectionConfig(String connectionId, ConnectionConfig connectionConfig) {
		connectionConfigs.put(connectionId, connectionConfig);
	}

	public void setConnectionConfigs(Map<String, ConnectionConfig> connectionConfigs) {
		this.connectionConfigs = connectionConfigs;
	}

	/**
	 * @return the heartbeatConfigs
	 */
	public List<HeartbeatConfig> getHeartbeatConfigs() {
		return heartbeatConfigs;
	}

	/**
	 * @param heartbeatConfigs the heartbeatConfigs to set
	 */
	public void setHeartbeatConfigs(List<HeartbeatConfig> heartbeatConfigs) {
		this.heartbeatConfigs = heartbeatConfigs;
	}

	/**
	 * @return the readerConfig
	 */
	public ReaderConfig getReaderConfig() {
		return readerConfig;
	}

	/**
	 * @param readerConfig the readerConfig to set
	 */
	public void setReaderConfig(ReaderConfig readerConfig) {
		this.readerConfig = readerConfig;
	}

	/**
	 * @return the writerConfig
	 */
	public WriterConfig getWriterConfig() {
		return writerConfig;
	}

	/**
	 * @param writerConfig the writerConfig to set
	 */
	public void setWriterConfig(WriterConfig writerConfig) {
		this.writerConfig = writerConfig;
	}

	/**
	 * @return the loginConfig
	 */
	public LoginConfig getLoginConfig() {
		return loginConfig;
	}

	/**
	 * @param loginConfig the loginConfig to set
	 */
	public void setLoginConfig(LoginConfig loginConfig) {
		this.loginConfig = loginConfig;
	}

	public AbstractMessageRecognizer getRequestSerialNumberRecognizer() {
		return requestSerialNumberRecognizer;
	}

	public void setRequestSerialNumberRecognizer(AbstractMessageRecognizer requestSerialNumberRecognizer) {
		this.requestSerialNumberRecognizer = requestSerialNumberRecognizer;
	}

	public AbstractMessageRecognizer getResponseSerialNumberRecognizer() {
		return responseSerialNumberRecognizer;
	}

	public void setResponseSerialNumberRecognizer(AbstractMessageRecognizer responseSerialNumberRecognizer) {
		this.responseSerialNumberRecognizer = responseSerialNumberRecognizer;
	}

	public AbstractMessageRecognizer getCodeRecognizer() {
		return codeRecognizer;
	}

	public void setCodeRecognizer(AbstractMessageRecognizer codeRecognizer) {
		this.codeRecognizer = codeRecognizer;
	}

	public String getRequestSerialNumberRecognizerId() {
		return requestSerialNumberRecognizerId;
	}

	public void setRequestSerialNumberRecognizerId(String requestSerialNumberRecognizerId) {
		this.requestSerialNumberRecognizerId = requestSerialNumberRecognizerId;
	}

	public String getResponseSerialNumberRecognizerId() {
		return responseSerialNumberRecognizerId;
	}

	public void setResponseSerialNumberRecognizerId(String responseSerialNumberRecognizerId) {
		this.responseSerialNumberRecognizerId = responseSerialNumberRecognizerId;
	}

	public CodeRecognizerConfig getCodeRecognizerConfig() {
		return codeRecognizerConfig;
	}

	public void setCodeRecognizerConfig(CodeRecognizerConfig codeRecognizerConfig) {
		this.codeRecognizerConfig = codeRecognizerConfig;
	}

	public Map<String,MessageSymbol> getMessageSymbolTable() {
		return messageSymbolTable;
	}

	public void setMessageSymbolTable(Map<String,MessageSymbol> messageSymbolTable) {
		this.messageSymbolTable = messageSymbolTable;
	}

}
