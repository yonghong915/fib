package com.fib.gateway.message.packer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.Message;

import lombok.Data;

/**
 * 消息组包器基类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Data
public abstract class AbstractMessagePacker {
	/***/
	protected Message message = null;

	/***/
	protected MessageBean messageBean = null;

	/***/
	protected MessageBean parentBean = null;

	protected ByteBuffer buf = null;

	protected Map<String, Object> variableCache = new HashMap<>(5);

	private String defaultCharset = System.getProperty("file.encoding");

	public abstract byte[] pack();
}
