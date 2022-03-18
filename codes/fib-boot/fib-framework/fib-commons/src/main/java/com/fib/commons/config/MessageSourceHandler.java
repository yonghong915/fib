package com.fib.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;

@Component
public class MessageSourceHandler {

	private MessageSource messageSource;

	@Autowired
	public MessageSourceHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * 根据Key取国际化的值
	 */
	public String getMessage(String messageKey, Object... args) {
		String rtnMsg = messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
		return StrUtil.isEmptyIfStr(rtnMsg) ? messageKey : rtnMsg;
	}
}