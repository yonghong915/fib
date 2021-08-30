package com.fib.msgconverter.commgateway.channel.config;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 通道连接器配置
 * 
 * 连接器定义了通道的通讯协议，如socket、mq等，可以扩展。
 * 
 * @author 刘恭亮
 * 
 */
public class ConnectorConfig {
	/**
	 * 通讯方向：客户端
	 */
	public static final int TYP_CLIENT = 0x21;

	/**
	 * 通讯方向：服务器端
	 */
	public static final int TYP_SERVER = 0x22;

	/**
	 * 通讯方向：双向，即为服务器，又为客户端
	 */
	public static final int TYP_DOUBLE = 0x23;

	public static int getChannelTypeByText(String channelTypeText) {
		if ("CLIENT".equalsIgnoreCase(channelTypeText)) {
			return TYP_CLIENT;
		} else if ("SERVER".equalsIgnoreCase(channelTypeText)) {
			return TYP_SERVER;
		} else if ("DOUBLE".equalsIgnoreCase(channelTypeText)) {
			return TYP_DOUBLE;
		} else {
			// throw new RuntimeException("Unsupport Channel Type :"
			// + channelTypeText);
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"ConnectorConfig.channelType.unsupport",
							new String[] { channelTypeText }));
		}
	}

	public static String getChannelTypeTextByType(int channelType) {
		switch (channelType) {
		case TYP_CLIENT:
			return "CLIENT";
		case TYP_SERVER:
			return "SERVER";
		case TYP_DOUBLE:
			return "DOUBLE";
		default:
			// throw new RuntimeException("Unsupport channel type:" +
			// channelType);
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"ConnectorConfig.channelType.unsupport",
							new String[] { "" + channelType }));
		}
	}

	/**
	 * 通讯方向：服务器、客户端、双向
	 */
	// private int type;
	/**
	 * 配置文件名
	 */
	private String config;

	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * @return the type
	 */
	// public int getType() {
	// return type;
	// }
	/**
	 * @param type
	 *            the type to set
	 */
	// public void setType(int type) {
	// this.type = type;
	// }
}
