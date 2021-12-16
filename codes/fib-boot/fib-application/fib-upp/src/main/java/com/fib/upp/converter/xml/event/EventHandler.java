package com.fib.upp.converter.xml.event;

import org.slf4j.Logger;

/**
 * 
 * @author fangyh
 *
 */
public abstract class EventHandler {
	protected Logger logger;

	public abstract void handleException(Event event);

	public abstract void handleResponseSendError(Event event);

	public abstract void handleRequestSent(Event event);

	public abstract void handleResponseArrived(Event event);

	public abstract void handleRequestArrived(Event event);

	public abstract void handleResponseSent(Event event);

	/**
	 * 记录事件。当Session超时或已结束时调用
	 * 
	 * @param event
	 */
	protected void logEvent(Event event) {
		if (null == logger) {
			return;
		}
		switch (event.getEventType()) {
		case Event.EVENT_ACCEPT_ERROR, Event.EVENT_REQUEST_RECEIVE_ERROR, Event.EVENT_RESPONSE_RECEIVE_ERROR, Event.EVENT_REQUEST_SEND_ERROR, Event.EVENT_RESPONSE_SEND_ERROR, Event.EVENT_CONNECT_ERROR, Event.EVENT_EXCEPTION:
			logger.error("{}", event);
			break;
		case Event.EVENT_REQUEST_ARRIVED, Event.EVENT_RESPONSE_ARRIVED, Event.EVENT_RESPONSE_SENT, Event.EVENT_REQUEST_SENT:
			logger.info("{}", event);
			break;
		case Event.EVENT_FATAL_EXCEPTION:
			logger.error("{}", event);
			break;
		default:
			logger.debug("{}", event);
		}
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return this.logger;
	}
}
