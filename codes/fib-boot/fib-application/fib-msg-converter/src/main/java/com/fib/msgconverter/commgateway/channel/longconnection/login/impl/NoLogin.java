package com.fib.msgconverter.commgateway.channel.longconnection.login.impl;

import com.fib.msgconverter.commgateway.channel.longconnection.Connection;
import com.fib.msgconverter.commgateway.channel.longconnection.login.AbstractLogin;

/**
 * 无需登陆。当长连接通道无需登陆时配置此登陆器。
 * @author 刘恭亮
 *
 */
public class NoLogin extends AbstractLogin{

	public boolean login(Connection sendConnection, Connection receiveConnection) {
		return true;
	}

}
