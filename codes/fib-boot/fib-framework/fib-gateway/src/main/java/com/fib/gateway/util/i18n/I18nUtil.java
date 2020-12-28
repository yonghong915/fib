package com.fib.gateway.util.i18n;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;

/**
 * 国际化工具类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-28
 */
@Component
public final class I18nUtil {
	private static Logger logger = LoggerFactory.getLogger(I18nUtil.class);

	private static MessageSource messageSource;

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		I18nUtil.messageSource = messageSource;
	}

	private I18nUtil() {

	}

	public static String getMessage(String code) {
		return getMessage(code);
	}

	public static String getMessage(String code, Object... args) {
		return getMessage(code, args, "");
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param code
	 * @param args
	 * @param defaultMessage
	 * @return
	 */
	public static String getMessage(String code, Object[] args, String defaultMessage) {
		Locale locale = LocaleContextHolder.getLocale();
		String message = "";
		try {
			return messageSource.getMessage(code, args, locale);
		} catch (Exception e) {
			logger.error("Failed to obtain international message.", e);
		}
		return StrUtil.isEmpty(message) ? defaultMessage : message;
	}
}
