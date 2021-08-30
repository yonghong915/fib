package com.fib.msgconverter.commgateway.channel.longconnection.login;

import com.fib.msgconverter.commgateway.channel.longconnection.Connection;

/**
 * 长连接通道登陆程序接口
 * 
 * @author 刘恭亮
 *
 */
public interface Login {
	/**
	 * 登陆
	 * 
	 * @param sendConnection    发送连接
	 * @param receiveConnection 接收连接
	 * @return true登陆成功，false登陆失败
	 */
	public boolean login(Connection sendConnection, Connection receiveConnection);
}
