package com.fib.msgconverter.commgateway.channel.http.config;

public class HTTPServerChannelConfig {
	private String portString;
	private int port;
	private int timeout = 60000;
	private int socketBufferSize;
	private boolean staleConnectionCheck = false;
	private boolean tcpNoDelay = true;
	private String elementCharset;
	private String contentCharset;
	private int backlog = 1;
	private VerifierConfig verifierConfig;

	// private String timeoutString;
	// private String socketBufferSizeString;
	// private String staleConnectionCheckString = "false";
	// private String tcpNoDelayString = "true";
	// private String elementCharsetString;
	// private String contentCharsetString;
	// private String backlogString = "1";

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	// public String getTimeoutString() {
	// return timeoutString;
	// }
	//
	// public void setTimeoutString(String timeoutString) {
	// this.timeoutString = timeoutString;
	// }

	public int getSocketBufferSize() {
		return socketBufferSize;
	}

	public void setSocketBufferSize(int socketBufferSize) {
		this.socketBufferSize = socketBufferSize;
	}

	// public String getSocketBufferSizeString() {
	// return socketBufferSizeString;
	// }
	//
	// public void setSocketBufferSizeString(String socketBufferSizeString) {
	// this.socketBufferSizeString = socketBufferSizeString;
	// }

	public boolean isStaleConnectionCheck() {
		return staleConnectionCheck;
	}

	public void setStaleConnectionCheck(boolean staleConnectionCheck) {
		this.staleConnectionCheck = staleConnectionCheck;
	}

	// public String getStaleConnectionCheckString() {
	// return staleConnectionCheckString;
	// }
	//
	// public void setStaleConnectionCheckString(String
	// staleConnectionCheckString) {
	// this.staleConnectionCheckString = staleConnectionCheckString;
	// }

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	// public String getTcpNoDelayString() {
	// return tcpNoDelayString;
	// }
	//
	// public void setTcpNoDelayString(String tcpNoDelayString) {
	// this.tcpNoDelayString = tcpNoDelayString;
	// }

	public String getElementCharset() {
		return elementCharset;
	}

	public void setElementCharset(String elementCharset) {
		this.elementCharset = elementCharset;
	}

	// public String getElementCharsetString() {
	// return elementCharsetString;
	// }
	//
	// public void setElementCharsetString(String elementCharsetString) {
	// this.elementCharsetString = elementCharsetString;
	// }

	public String getContentCharset() {
		return contentCharset;
	}

	public void setContentCharset(String contentCharset) {
		this.contentCharset = contentCharset;
	}

	// public String getContentCharsetString() {
	// return contentCharsetString;
	// }
	//
	// public void setContentCharsetString(String contentCharsetString) {
	// this.contentCharsetString = contentCharsetString;
	// }

	public int getBacklog() {
		return backlog;
	}

	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	// public String getBacklogString() {
	// return backlogString;
	// }
	//
	// public void setBacklogString(String backlogString) {
	// this.backlogString = backlogString;
	// }

	public VerifierConfig getVerifierConfig() {
		return verifierConfig;
	}

	public void setVerifierConfig(VerifierConfig verifierConfig) {
		this.verifierConfig = verifierConfig;
	}

	public String getPortString() {
		return portString;
	}

	public void setPortString(String portString) {
		this.portString = portString;
	}

}
