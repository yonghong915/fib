package com.fib.msgconverter.commgateway.channel.config.base;

import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;

/**
 * 连接配置
 * 
 * @author 刘恭亮
 * 
 */
public class ConnectionConfig {

	/**
	 * 连接类型：服务器端
	 */
	public static final int CONN_TYP_SERVER = 1000;
	public static final String CONN_TYP_SERVER_TXT = "SERVER";
	public static final String CONN_TYP_SERVER_SHOW_TXT = "主动连接对方";

	/**
	 * 连接类型：客户端
	 */
	public static final int CONN_TYP_CLIENT = 1001;
	public static final String CONN_TYP_CLIENT_TXT = "CLIENT";
	public static final String CONN_TYP_CLIENT_SHOW_TXT = "等待对方连接";

	public static String getTypeText(int type) {
		switch (type) {
		case CONN_TYP_SERVER:
			return CONN_TYP_SERVER_TXT;
		case CONN_TYP_CLIENT:
			return CONN_TYP_CLIENT_TXT;
		default:
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"ConnectionConfig.getTypeByText.connectionType.unsupport",
									new String[] { type + "" }));
		}
	}

	public static int getTypeByText(String typeText) {
		if (CONN_TYP_SERVER_TXT.equalsIgnoreCase(typeText)) {
			return CONN_TYP_SERVER;
		} else if (CONN_TYP_CLIENT_TXT.equalsIgnoreCase(typeText)) {
			return CONN_TYP_CLIENT;
		} else {
			// throw new RuntimeException("Unsupport Connection Type[" +
			// typeText
			// + "]!");
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"ConnectionConfig.getTypeByText.connectionType.unsupport",
									new String[] { typeText }));
		}
	}

	// public static String getTypeShowTextByType(int type) {
	// switch (type) {
	// case CONN_TYP_SERVER:
	// return CONN_TYP_SERVER_SHOW_TXT;
	// case CONN_TYP_CLIENT:
	// return CONN_TYP_CLIENT_SHOW_TXT;
	// default:
	// throw new RuntimeException(
	// MultiLanguageResourceBundle
	// .getInstance()
	// .getString(
	// "ConnectionConfig.getTypeByText.connectionType.unsupport",
	// new String[] { type + "" }));
	// }
	// }

	/**
	 * 通讯方向：发送方
	 */
	public static final int COMM_DIRECTION_SEND = 2000;
	public static final String COMM_DIRECTION_SEND_TXT = "send";
	public static final String COMM_DIRECTION_SEND_SHOW_TXT = "发送方";

	/**
	 * 通讯方向：接收方
	 */
	public static final int COMM_DIRECTION_RECEIVE = 2001;
	public static final String COMM_DIRECTION_RECEIVE_TXT = "receive";
	public static final String COMM_DIRECTION_RECEIVE_SHOW_TXT = "接收方";

	/**
	 * 通讯方向：收发双向
	 */
	public static final int COMM_DIRECTION_DOUBLE = 2002;
	public static final String COMM_DIRECTION_DOUBLE_TXT = "double";
	public static final String COMM_DIRECTION_DOUBLE_SHOW_TXT = "双向收发";

	public static int getDirectionByText(String directionText) {
		if (COMM_DIRECTION_SEND_TXT.equalsIgnoreCase(directionText)) {
			return COMM_DIRECTION_SEND;
		} else if (COMM_DIRECTION_RECEIVE_TXT.equalsIgnoreCase(directionText)) {
			return COMM_DIRECTION_RECEIVE;
		} else if (COMM_DIRECTION_DOUBLE_TXT.equalsIgnoreCase(directionText)) {
			return COMM_DIRECTION_DOUBLE;
		} else {
			// throw new RuntimeException("Unsupport Direction Type["
			// + directionText + "]!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"ConnectionConfig.directionType.unsupport",
							new String[] { directionText }));
		}
	}

	public static String getDirectionText(int direction) {
		switch (direction) {
		case COMM_DIRECTION_SEND:
			return COMM_DIRECTION_SEND_TXT;
		case COMM_DIRECTION_RECEIVE:
			return COMM_DIRECTION_RECEIVE_TXT;
		case COMM_DIRECTION_DOUBLE:
			return COMM_DIRECTION_DOUBLE_TXT;
		default:
			// throw new RuntimeException("Unsupport Direction Type[" +
			// direction
			// + "]!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"ConnectionConfig.directionType.unsupport",
							new String[] { "" + direction }));
		}
	}

	// public static String getDirectonShowTextByDirection(int direction) {
	// switch (direction) {
	// case COMM_DIRECTION_SEND:
	// return COMM_DIRECTION_SEND_SHOW_TXT;
	// case COMM_DIRECTION_RECEIVE:
	// return COMM_DIRECTION_RECEIVE_SHOW_TXT;
	// case COMM_DIRECTION_DOUBLE:
	// return COMM_DIRECTION_DOUBLE_SHOW_TXT;
	// default:
	// // throw new RuntimeException("Unsupport Direction Type[" +
	// // direction
	// // + "]!");
	// throw new RuntimeException(MultiLanguageResourceBundle
	// .getInstance().getString(
	// "ConnectionConfig.directionType.unsupport",
	// new String[] { "" + direction }));
	// }
	// }

	public static String getDirectionShowText(String directionText) {
		if (COMM_DIRECTION_SEND_TXT.equals(directionText)) {
			return COMM_DIRECTION_SEND_SHOW_TXT;
		} else if (COMM_DIRECTION_RECEIVE_TXT.equals(directionText)) {
			return COMM_DIRECTION_RECEIVE_SHOW_TXT;
		} else if (COMM_DIRECTION_DOUBLE_TXT.equals(directionText)) {
			return COMM_DIRECTION_DOUBLE_SHOW_TXT;
		} else {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"ConnectionConfig.directionType.unsupport",
							new String[] { directionText }));
		}
	}

	/**
	 * 默认通讯缓冲大小
	 */
	public static final int DEFAULT_COMM_BUFFER_SIZE = 8192;

	/**
	 * 连接id。仅用于长连接。
	 */
	private String id;

	/**
	 * 连接类型
	 */
	private int type;

	/**
	 * 端口号
	 */
	private int port;
	private String portString;

	/**
	 * 允许排队的连接个数。仅用于服务器端。
	 */
	private int backlog;

	/**
	 * 通讯方向
	 */
	private int direction;

	/**
	 * 服务器地址。仅用于客户端。
	 */
	private String serverAddress;
	private String serverAddressString;

	/**
	 * 通讯缓冲大小
	 */
	private int commBufferSize = DEFAULT_COMM_BUFFER_SIZE;

	/**
	 * 本地端口地址，仅适用于需要绑定本地指定连接端口的情况。
	 */
	private int localPort = 0;
	private String localPortString;

	/**
	 * 本地绑定地址，仅适用于需要绑定本地指定连接端口的情况。
	 */
	private String localServerAddress;
	private String localServerAddressString;

	public String getServerAddressString() {
		return serverAddressString;
	}

	public void setServerAddressString(String serverAddressString) {
		this.serverAddressString = serverAddressString;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the backlog
	 */
	public int getBacklog() {
		return backlog;
	}

	/**
	 * @param backlog
	 *            the backlog to set
	 */
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * @param serverAddress
	 *            the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the commBufferSize
	 */
	public int getCommBufferSize() {
		return commBufferSize;
	}

	/**
	 * @param commBufferSize
	 *            the commBufferSize to set
	 */
	public void setCommBufferSize(int commBufferSize) {
		this.commBufferSize = commBufferSize;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getPortString() {
		return portString;
	}

	public void setPortString(String portString) {
		this.portString = portString;
	}

	public String getLocalPortString() {
		return localPortString;
	}

	public void setLocalPortString(String localPortString) {
		this.localPortString = localPortString;
	}

	public String getLocalServerAddress() {
		return localServerAddress;
	}

	public void setLocalServerAddress(String localServerAddress) {
		this.localServerAddress = localServerAddress;
	}

	public String getLocalServerAddressString() {
		return localServerAddressString;
	}

	public void setLocalServerAddressString(String localServerAddressString) {
		this.localServerAddressString = localServerAddressString;
	}

}
