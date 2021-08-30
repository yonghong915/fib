package com.fib.msgconverter.commgateway.channel.http.config.database;

import java.sql.Connection;

import com.fib.msgconverter.commgateway.channel.http.config.HTTPServerChannelConfig;
import com.fib.msgconverter.commgateway.channel.http.config.VerifierConfig;
import com.fib.msgconverter.commgateway.dao.httpserverconnector.dao.HttpServerConnector;
import com.fib.msgconverter.commgateway.dao.httpserverconnector.dao.HttpServerConnectorDAO;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

public class HTTPServerChannelConfigLoader {
	public static final String TRUE = "0";
	public static final String FALSE = "1";

	public HTTPServerChannelConfig loadConfig(String connectorId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			HttpServerConnectorDAO httpServerConnectorDao = new HttpServerConnectorDAO();
			httpServerConnectorDao.setConnection(conn);
			HttpServerConnector httpServerConnectorDto = httpServerConnectorDao
					.selectByPK(connectorId);

			HTTPServerChannelConfig httpServerChannelConfig = new HTTPServerChannelConfig();
			httpServerChannelConfig.setBacklog(httpServerConnectorDto
					.getBacklog());
			httpServerChannelConfig.setContentCharset(httpServerConnectorDto
					.getContentCharset());
			httpServerChannelConfig.setElementCharset(httpServerConnectorDto
					.getElementCharset());
			httpServerChannelConfig.setPort(httpServerConnectorDto.getPort());
			httpServerChannelConfig.setSocketBufferSize(httpServerConnectorDto
					.getSocketBufferSize());
			if (TRUE.equals(httpServerConnectorDto.getStaleConnectionCheck())) {
				httpServerChannelConfig.setStaleConnectionCheck(true);
			} else if (FALSE.equals(httpServerConnectorDto
					.getStaleConnectionCheck())) {
				httpServerChannelConfig.setStaleConnectionCheck(false);
			}
			if (TRUE.equals(httpServerConnectorDto.getTcpNodelay())) {
				httpServerChannelConfig.setTcpNoDelay(true);
			} else if (FALSE.equals(httpServerConnectorDto.getTcpNodelay())) {
				httpServerChannelConfig.setTcpNoDelay(false);
			}
			httpServerChannelConfig.setTimeout(httpServerConnectorDto
					.getTimeout());
			if (null != httpServerConnectorDto.getVerifierClassName()) {
				VerifierConfig verifierConfig = new VerifierConfig();
				verifierConfig.setClassName(httpServerConnectorDto
						.getVerifierClassName());
				httpServerChannelConfig.setVerifierConfig(verifierConfig);
			}
			conn.commit();
			return httpServerChannelConfig;
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
