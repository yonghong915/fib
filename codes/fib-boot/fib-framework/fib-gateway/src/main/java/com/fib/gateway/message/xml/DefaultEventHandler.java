package com.fib.gateway.message.xml;

import java.util.Map;

import com.fib.commons.exception.CommonException;
import com.fib.commons.serializer.SerializationUtils;
import com.fib.commons.serializer.json.JsonSerializer;
import com.fib.gateway.message.MessagePacker;
import com.fib.gateway.message.MessageParser;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.xml.config.processor.Processor;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-24
 */
public class DefaultEventHandler {
	protected Object parseMessage(int type, String messageBeanGroupId, String messageId, byte[] message) {
		if (Processor.MSG_OBJ_TYP_MB == type) {
			MessageParser parser = new MessageParser();
			parser.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
			parser.setMessageData(message);
			return parser.parse();
		} else {
			return SerializationUtils.getInstance().loadSerializerInstance(JsonSerializer.class).deserialize(message,
					Map.class);
		}
	}

	protected byte[] packMessage(String messageBeanGroupId, String messageId, Object data) {
		if (data instanceof MessageBean) {
			((MessageBean) data).validate();
			MessagePacker packer = new MessagePacker();
			packer.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
			packer.setMessageBean((MessageBean) data);
			return packer.pack();
		} else if (data instanceof Map) {
			byte[] ret = null;
			try {
				ret = SerializationUtils.getInstance().loadSerializerInstance(JsonSerializer.class).serialize(data);
			} catch (Exception e) {
				//
			}
			return ret;
		} else {
			throw new CommonException("aaa");
		}
	}
}
