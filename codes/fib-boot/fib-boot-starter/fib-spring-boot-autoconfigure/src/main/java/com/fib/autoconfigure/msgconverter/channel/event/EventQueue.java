package com.fib.autoconfigure.msgconverter.channel.event;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.autoconfigure.disruptor.DisruptorTemplate;
import com.fib.autoconfigure.disruptor.executor.Executor;
import com.fib.commons.classloader.CustomClassLoader;

import cn.hutool.core.io.FileUtil;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-05-20 11:37:01
 */

@Component
public class EventQueue {

	@Autowired
	private DisruptorTemplate disruptorTemplate;

	public void postEvent(Event event) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		disruptorTemplate.publishEvent(msg -> {
			Thread.currentThread().setContextClassLoader(cl);
			if (null != event) {
				handleEvent(event);
			}
		}, event);
	}

	private void handleEvent(Event event) {
		EventHandler eventHandler = null;
		if (EventType.REQUEST_ARRIVED == event.getEventType()) {
			eventHandler = event.getSourceChannel().getEventHandler();
		} else {
		}

		if (null == eventHandler) {
			return;
		}

		switch (event.getEventType()) {
		case ACCEPT_ERROR, REQUEST_RECEIVE_ERROR:
			break;

		case REQUEST_SEND_ERROR:
			break;

		case REQUEST_ARRIVED:
			eventHandler.handleRequestArrived(event);
			break;
		default:
		}
	}
}
