/**
 * 北京长信通信息技术有限公司
 * 2008-8-30 上午10:18:32
 */
package com.fib.msgconverter.commgateway.channel.nio.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 客户端Socket通道配置解析器
 * 
 * @author 刘恭亮
 */
public class SocketClientChannelConfigParser extends SocketChannelConfigParser {

	protected void configCheck() {
		// type
		if (ConnectionConfig.CONN_TYP_CLIENT != config.getConnectionConfig()
				.getType()) {
			// throw new RuntimeException(
			// "SocketClientChannel's 'type' attribute must be 'client'!");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"SocketClientChannelConfigParser.configCheck.type.mustClient"));
		}

		// server-address
		String ip = config.getConnectionConfig().getServerAddress();
		if (ip == null) {
			// throw new RuntimeException(
			// "SocketServerChannel must have 'server-address' attribute!");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"SocketClientChannelConfigParser.configCheck.serverAddress.mustHave"));
		}
		try {
			InetAddress.getAllByName(ip);
		} catch (UnknownHostException e) {
			// throw new RuntimeException(
			// "SocketServerChannel's 'server-address'[" + ip
			// + "] is wrong! :" + e.getMessage(), e);
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"SocketClientChannelConfigParser.configCheck.serverAddress.wrong",
									new String[] { ip, e.getMessage() }), e);
		}

		// port
		if (config.getConnectionConfig().getPort() <= 0) {
			// throw new RuntimeException(
			// "SocketServerChannel must have 'port' attribute!");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"SocketChannelConfigParser.configCheck.port.mustHave"));
		}

		// reader.filter-list
		List list = config.getReaderConfig().getFilterConfigList();
		for (int i = 0; i < list.size(); i++) {
			String className = ((FilterConfig) list.get(i)).getClassName();
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// throw new RuntimeException("Can't find class[" + className
				// + "]!" + e.getMessage(), e);
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"SocketChannelConfigParser.configCheck.class.notFound",
										new String[] { className,
												e.getMessage() }), e);
			}
		}
		// writer.filter-list
		list = config.getWriterConfig().getFilterConfigList();
		for (int i = 0; i < list.size(); i++) {
			String className = ((FilterConfig) list.get(i)).getClassName();
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// throw new RuntimeException("Can't find class[" + className
				// + "]!" + e.getMessage(), e);
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"SocketChannelConfigParser.configCheck.class.notFound",
										new String[] { className,
												e.getMessage() }), e);
			}
		}
	}

}
