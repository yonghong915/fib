/**
 * 北京长信通信息技术有限公司
 * 2008-11-19 下午05:58:27
 */
package com.fib.msgconverter.commgateway.module.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.module.Module;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 数据库连接管理器组件
 * 
 * @author 刘恭亮
 * 
 */
public class DBConnectionMananger extends Module {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.module.Module#initialize()
	 */
	public void initialize() {
		if (null != getParameters()) {
			String configFile = (String) getParameters().get("config-file");

			if (null != configFile) {
				ConnectionManager.setConfigFileName(configFile);
			}
		}
		// 初始化数据库连接管理器
		Connection conn = ConnectionManager.getInstance().getConnection();
		try {
			conn.close();
		} catch (SQLException e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}

		CommGateway.setDatabaseSupport(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giantstone.commgateway.module.Module#shutdown()
	 */
	public void shutdown() {
		// do nothing
	}

}
