package com.fib.commons.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fib.commons.config.MessageSourceHandler;


@Component
public class I18nUtils {
	private MessageSourceHandler handler;

	private static MessageSourceHandler staticHandler;

	@Autowired
	public void setHandler(MessageSourceHandler handler) {
		this.handler = handler;
	}

	@PostConstruct
	public void init() {
		I18nUtils.staticHandler = this.handler;
	}

	/**
	 * 根据Key取国际化的值
	 */
	public static String getMessage(String messageKey, Object... args) {
		return staticHandler.getMessage(messageKey, args);
	}
}