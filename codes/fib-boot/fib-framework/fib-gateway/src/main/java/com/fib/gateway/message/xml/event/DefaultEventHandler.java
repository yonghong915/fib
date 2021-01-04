package com.fib.gateway.message.xml.event;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.commons.exception.CommonException;
import com.fib.commons.serializer.SerializationUtils;
import com.fib.commons.serializer.json.JsonSerializer;
import com.fib.gateway.message.MessagePacker;
import com.fib.gateway.message.MessageParser;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.metadata.MessageMetadataManager;
import com.fib.gateway.message.xml.config.processor.Processor;

public class DefaultEventHandler implements EventHandler {
	private Logger logger = LoggerFactory.getLogger(DefaultEventHandler.class);

	@Override
	public void handleException(Event event) {
		//

	}

	@Override
	public void handleResponseSendError(Event event) {
		//

	}

	@Override
	public void handleRequestSent(Event event) {
		//

	}

	@Override
	public void handleResponseArrived(Event event) {
		//

	}

	@Override
	public void handleRequestArrived(Event event) {
		String messageBeanGroupId = "";
		String messageId = "";
		byte[] requestMessage = event.getRequestMessage();
		Object sourceRequestData = parseMessage(Processor.MSG_OBJ_TYP_MB, messageBeanGroupId, messageId, "UTF-8",
				requestMessage);
		logger.info("sourceRequestData={}", sourceRequestData);
	}

	@Override
	public void handleResponseSent(Event event) {
		//
	}

	protected Object parseMessage(int type, String messageBeanGroupId, String messageId, String mapCharset,
			byte[] message) {
		if (Processor.MSG_OBJ_TYP_MB == type) {
			MessageParser parser = new MessageParser();
			parser.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
			parser.setMessageData(message);
			return parser.parse();
		} else {
			return SerializationUtils.getInstance().loadSerializerInstance(JsonSerializer.class).deserialize(message,
					Object.class);
		}
	}

	protected byte[] packMessage(String messageBeanGroupId, String messageId, Object data, String encoding) {
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
			throw new CommonException("");
		}
	}
}
