package com.fib.msgconverter.commgateway.channel.http.config.database;

import java.sql.Connection;

import com.fib.msgconverter.commgateway.channel.http.config.HTTPClientChannelConfig;
import com.fib.msgconverter.commgateway.dao.httpclientconnector.dao.HttpClientConnector;
import com.fib.msgconverter.commgateway.dao.httpclientconnector.dao.HttpClientConnectorDAO;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

public class HTTPClientChannelConfigLoader {
	public HTTPClientChannelConfig loadConfig(String connectorId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			HttpClientConnectorDAO hccDao = new HttpClientConnectorDAO();
			hccDao.setConnection(conn);

			HttpClientConnector httpClientConnectorDto = hccDao
					.selectByPK(connectorId);
			HTTPClientChannelConfig httpClientChannelConfig = new HTTPClientChannelConfig();
			httpClientChannelConfig.setContentCharset(httpClientConnectorDto
					.getContentCharset());
			httpClientChannelConfig.setMaxHandlerNumber(httpClientConnectorDto
					.getMaxHandlerNumber());
			httpClientChannelConfig.setUrl(httpClientConnectorDto.getUrl());

			conn.commit();
			return httpClientChannelConfig;
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
