package com.fib.gateway.message.xml.event;

public interface EventHandler {
	public abstract void handleException(Event event);

	public abstract void handleResponseSendError(Event event);

	public abstract void handleRequestSent(Event event);

	public abstract void handleResponseArrived(Event event);

	public abstract void handleRequestArrived(Event event);

	public abstract void handleResponseSent(Event event);
}
