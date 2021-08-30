package com.fib.msgconverter.commgateway.channel.longconnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;

/**
 * 长连接
 * 
 * @author 刘恭亮
 * 
 */
public abstract class Connection {
	protected String id = null;
	protected boolean connect = false;
	protected ConnectionConfig conf = null;
	protected Socket socket = null;
	protected InputStream is = null;
	protected OutputStream os = null;
	protected long connectedTime;

	public abstract void connect();

	public void close() {
		connect = false;
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the conf
	 */
	public ConnectionConfig getConf() {
		return conf;
	}

	/**
	 * @param conf the conf to set
	 */
	public void setConf(ConnectionConfig conf) {
		this.conf = conf;
	}

	/**
	 * @return the connect
	 */
	public boolean isConnect() {
		return connect;
	}

	/**
	 * @return the is
	 */
	public InputStream getIs() {
		return is;
	}

	/**
	 * @return the os
	 */
	public OutputStream getOs() {
		return os;
	}

	public long getConnectedTime() {
		return connectedTime;
	}

	public void setConnectedTime(long connectedTime) {
		this.connectedTime = connectedTime;
	}
}