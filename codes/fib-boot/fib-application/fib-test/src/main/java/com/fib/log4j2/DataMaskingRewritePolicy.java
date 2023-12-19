package com.fib.log4j2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.SimpleMessage;

@Plugin(name = "DataMaskingRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public class DataMaskingRewritePolicy implements RewritePolicy {
	/*
	 * 脱敏符号
	 */
	private static final String ASTERISK = "****";

	/*
	 * 引号
	 */
	private static final String QUOTATION_MARK = "\"";

	// 使用静态内部类创建对象，节省空间
	private static class StaticDataMaskingRewritePolicy {
		private static final DataMaskingRewritePolicy dataMaskingRewritePolicy = new DataMaskingRewritePolicy();
	}

	// 需要加密的字段配置数组
	private static final String[] encryptionKeyArrays = { "password", "expireYear", "expireMonth", "cvv" };
	// 将数组转换为集合，方便查找
	private static final List<String> encryptionKeys = new ArrayList<>();

	public DataMaskingRewritePolicy() {
		// if (CollectionUtils.isEmpty(encryptionKeys)) {
		encryptionKeys.addAll(Arrays.asList(encryptionKeyArrays));
		// }
	}

	@Override
	public LogEvent rewrite(LogEvent logEvent) {
		if (!(logEvent instanceof Log4jLogEvent)) {
			return logEvent;
		}

		Log4jLogEvent log4jLogEvent = (Log4jLogEvent) logEvent;
		Message message = log4jLogEvent.getMessage();
		if (message instanceof ObjectMessage) {
			// SimpleMessage newMessage = decryptionSimpleMessage((SimpleMessage) message);
			// Log4jLogEvent.Builder builder =
			// log4jLogEvent.newBuilder().setMessage(newMessage);
			// return builder.build();
			ObjectMessage s = new ObjectMessage(message.getFormat() + " bbbbb");
			Log4jLogEvent.Builder builder = Log4jLogEvent.newBuilder().setMessage(s);
			return builder.build();
		}
		return log4jLogEvent;
	}

	private SimpleMessage decryptionSimpleMessage(SimpleMessage simpleMessage) {
		String messsage = simpleMessage.toString();
		String newMessage = messsage + "bbbbb";
		return new SimpleMessage(newMessage);
	}

	/**
	 * 单例模式创建(静态内部类模式)
	 *
	 * @return
	 */
	@PluginFactory
	public static DataMaskingRewritePolicy createPolicy() {
		return StaticDataMaskingRewritePolicy.dataMaskingRewritePolicy;
	}

}
