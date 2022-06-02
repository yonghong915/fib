package com.fib.autoconfigure.msgconverter.channel.event.impl;


import com.fib.autoconfigure.msgconverter.channel.Channel;
import com.fib.autoconfigure.msgconverter.channel.config.ChannelConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.MessageHandlerConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.MessageTransformerConfig;
import com.fib.autoconfigure.msgconverter.channel.config.processor.Processor;
import com.fib.autoconfigure.msgconverter.channel.config.route.RouteRule;
import com.fib.autoconfigure.msgconverter.channel.event.Event;
import com.fib.autoconfigure.msgconverter.channel.event.EventHandler;
import com.fib.autoconfigure.msgconverter.channel.message.recognizer.MessageRecognizer;
import com.fib.autoconfigure.msgconverter.message.MessageParser;
import com.fib.autoconfigure.msgconverter.message.metadata.handler.MessageMetadataManager;
import com.fib.autoconfigure.msgconverter.util.EnumConstants;

/**
 * 默认事件处理
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 11:53:23
 */
public class DefaultEventHandler implements EventHandler {

	@Override
	public void handleRequestArrived(Event event) {
		Channel sourceChannel = event.getSourceChannel();
		String sourceChannelId = sourceChannel.getId();
		byte[] requestMessage = event.getRequestMessage();

		// 3. message-type
		MessageRecognizer recognizer = sourceChannel.getMessageTypeRecognizer();
		String messageType = null;
		messageType = recognizer.recognize(requestMessage);
		ChannelConfig channelConfig = sourceChannel.getChannelConfig();
		Processor processor = null;
		if (channelConfig.getMessageTypeRecognizerConfig().getMessageTypeProcessorMap().containsKey(messageType)) {
			String processorId = channelConfig.getMessageTypeRecognizerConfig().getMessageTypeProcessorMap().get(messageType);

			// 4.2 取出相应processor
			processor = channelConfig.getProcessorTable().get(processorId);
		} else {
			processor = channelConfig.getDefaultProcessor();
		}

		RouteRule rule = channelConfig.getRouteTable().get(processor.getRouteRuleId());
		EnumConstants.ProcessorType processorType = processor.getType();

		MessageHandlerConfig requestHandler = processor.getRequestMessageHandlerConfig();
		MessageTransformerConfig requestConfig = processor.getRequestMessageConfig();

		Channel destChannel = null;
		String messageBeanGroupId = sourceChannel.getChannelConfig().getMessageBeanGroupId();
		// 解析报文
		Object sourceRequestData = null;

		if (!(EnumConstants.ProcessorType.TRANSMIT == processorType
				&& EnumConstants.MessageObjectType.MESSAGE_BEAN == processor.getSourceChannelMessageObjectType())) {
			sourceRequestData = parseMessage(processor.getSourceChannelMessageObjectType(), messageBeanGroupId, requestConfig.getSourceMessageId(),
					processor.getSourceMapCharset(), requestMessage);
		}

		if (EnumConstants.ProcessorType.TRANSMIT == processorType) {
			//
		} else if (EnumConstants.ProcessorType.LOCAL == processor.getType()) {
			//
		} else {
			//
		}
	}

	/**
	 * 报文解包
	 * 
	 * @param messageMetadata
	 * @param message
	 * @return
	 */
	protected Object parseMessage(EnumConstants.MessageObjectType type, String messageBeanGroupId, String messageId, String mapCharset,
			byte[] message) {
		if (EnumConstants.MessageObjectType.MESSAGE_BEAN == type) {
			MessageParser parser = new MessageParser();
			parser.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
			parser.setMessageData(message);

			return parser.parse();
		} else {
			// return MapSerializer.deserialize(new String(message, mapCharset));
			return null;
		}
	}
}
