package com.fib.msgconverter.commgateway.channel.nio.config.database;

import java.sql.Connection;

import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.ReaderConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.SocketChannelConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.WriterConfig;
import com.fib.msgconverter.commgateway.dao.socketconnector.dao.SocketConnector;
import com.fib.msgconverter.commgateway.dao.socketconnector.dao.SocketConnectorDAO;
import com.fib.msgconverter.commgateway.util.database.DBConfigUtil;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

public class SocketChannelConfigLoader {
	public SocketChannelConfig loadConfig(String connectorId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			SocketConnectorDAO socketConnectorDao = new SocketConnectorDAO();
			socketConnectorDao.setConnection(conn);
			SocketConnector socketConnectorDto = socketConnectorDao
					.selectByPK(connectorId);

			SocketChannelConfig socketChannelConfig = new SocketChannelConfig();
			ConnectionConfig connectionConfig = new ConnectionConfig();
			connectionConfig.setBacklog(socketConnectorDto.getBacklog());
			connectionConfig.setCommBufferSize(socketConnectorDto
					.getCommBufferSize());
			connectionConfig.setLocalPort(socketConnectorDto.getLocalPort());
			connectionConfig.setLocalServerAddress(socketConnectorDto
					.getLocalServerAddress());
			connectionConfig.setPort(socketConnectorDto.getPort());
			connectionConfig.setServerAddress(socketConnectorDto
					.getServerAddress());
			socketChannelConfig.setConnectionConfig(connectionConfig);
			if (null != socketConnectorDto.getReaderId()) {
				socketChannelConfig.setReaderConfig((ReaderConfig) DBConfigUtil
						.transformReaderOrWriterConfig(socketConnectorDto
								.getReaderId(), true, conn));
			}
			if (null != socketConnectorDto.getWriterId()) {
				socketChannelConfig.setWriterConfig((WriterConfig) DBConfigUtil
						.transformReaderOrWriterConfig(socketConnectorDto
								.getWriterId(), false, conn));
			}
			conn.commit();
			return socketChannelConfig;
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
