package com.fib.gateway.channel.nio.config;

import java.util.List;

import com.fib.gateway.channel.config.base.ConnectionConfig;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class SocketServerChannelConfigParser extends SocketChannelConfigParser {
	protected void configCheck() {
		// type
		if (ConnectionConfig.CONN_TYP_SERVER != config.getConnectionConfig().getType()) {
			// throw new RuntimeException(
			// "SocketServerChannel's 'type' attribute must be 'server'!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("SocketServerChannelConfigParser.configCheck.type.mustServer"));
		}

		// backlog
		if (config.getConnectionConfig().getBacklog() <= 0) {
			// throw new RuntimeException(
			// "SocketServerChannel must have 'backlog' attribute!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("SocketServerChannelConfigParser.configCheck.backlog.mustHave"));
		}

		// port
		if (config.getConnectionConfig().getPort() <= 0) {
			// throw new RuntimeException(
			// "SocketServerChannel must have 'port' attribute!");
			throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
					.getString("SocketChannelConfigParser.configCheck.port.mustHave"));
		}

		// reader.filter-list
		List list = config.getReaderConfig().getFilterConfigList();
		for (int i = 0; i < list.size(); i++) {
			String className = ((FilterConfig) list.get(i)).getClassName();
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Can't find class[" + className + "]!" + e.getMessage(), e);
			}
		}
		// writer.filter-list
		list = config.getReaderConfig().getFilterConfigList();
		for (int i = 0; i < list.size(); i++) {
			String className = ((FilterConfig) list.get(i)).getClassName();
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				// throw new RuntimeException("Can't find class[" + className
				// + "]!" + e.getMessage(), e);
				throw new RuntimeException(MultiLanguageResourceBundle.getInstance().getString(
						"SocketChannelConfigParser.configCheck.class.notFound",
						new String[] { className, e.getMessage() }), e);
			}
		}
	}
}
