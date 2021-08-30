package com.fib.msgconverter.commgateway.event;

import org.apache.log4j.Logger;

public abstract class EventHandler {
	private Logger logger;

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
			System.out.println(event.toString());
			return;
		}
		switch (event.getEventType()) {
		case Event.EVENT_ACCEPT_ERROR:
		case Event.EVENT_REQUEST_RECEIVE_ERROR:
		case Event.EVENT_RESPONSE_RECEIVE_ERROR:
		case Event.EVENT_REQUEST_SEND_ERROR:
		case Event.EVENT_RESPONSE_SEND_ERROR:
		case Event.EVENT_CONNECT_ERROR:
		case Event.EVENT_EXCEPTION:
			logger.error(event.toString());
			break;
		case Event.EVENT_REQUEST_ARRIVED:
		case Event.EVENT_RESPONSE_ARRIVED:
		case Event.EVENT_RESPONSE_SENT:
		case Event.EVENT_REQUEST_SENT:
			logger.info(event.toString());
			break;
		case Event.EVENT_FATAL_EXCEPTION:
			logger.fatal(event.toString());
			break;
		default:
			logger.debug(event.toString());
		}
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger getLogger() {
		return this.logger;
	}
}
