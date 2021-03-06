package com.fib.gateway.message.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Message;

import lombok.Data;

/**
 * 报文解析器基类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public abstract class AbstractMessageParser {
	protected Message message = null;
	protected Map variableCache = new HashMap(5);
	protected byte[] messageData = null;
	protected MessageBean messageBean = null;
	protected MessageBean parentBean = null;
	private String defaultCharset = System.getProperty("file.encoding");
	protected Map<String, Object> fieldIndexCache = new TreeMap<>();
	protected int indexOffSet = 0;
	protected boolean needIndex = false;

	public abstract MessageBean parse();
	protected int parse(int var1) {
		return 0;
	}
	
	protected void loadVariable() {
		
	}
}
