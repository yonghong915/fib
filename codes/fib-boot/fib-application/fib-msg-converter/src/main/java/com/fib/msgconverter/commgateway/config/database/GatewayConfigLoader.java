package com.fib.msgconverter.commgateway.config.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.fib.msgconverter.commgateway.config.ChannelMainConfig;
import com.fib.msgconverter.commgateway.config.GatewayConfig;
import com.fib.msgconverter.commgateway.config.ModuleConfig;
import com.fib.msgconverter.commgateway.dao.channel.dao.Channel;
import com.fib.msgconverter.commgateway.dao.channel.dao.ChannelDAO;
import com.fib.msgconverter.commgateway.dao.gateway.dao.Gateway;
import com.fib.msgconverter.commgateway.dao.gateway.dao.GatewayDAO;
import com.fib.msgconverter.commgateway.dao.gatewaychannelrelation.dao.GatewayChannelRelation;
import com.fib.msgconverter.commgateway.dao.gatewaychannelrelation.dao.GatewayChannelRelationDAO;
import com.fib.msgconverter.commgateway.dao.module.dao.ModuleDAO;
import com.fib.msgconverter.commgateway.dao.moduleparameterrelation.dao.ModuleParameterRelation;
import com.fib.msgconverter.commgateway.dao.moduleparameterrelation.dao.ModuleParameterRelationDAO;
import com.fib.msgconverter.commgateway.dao.parameter.dao.Parameter;
import com.fib.msgconverter.commgateway.dao.parameter.dao.ParameterDAO;
import com.fib.msgconverter.commgateway.session.config.SessionConfig;
import com.giantstone.common.database.ConnectionManager;
import com.fib.msgconverter.commgateway.dao.module.dao.Module;

public class GatewayConfigLoader {
	public static final String TRUE = "0";
	private String gatewayId = null;
	private GatewayConfig gatewayConfig = null;

	public GatewayConfig loadConfig(String gatewayId) {
		this.gatewayId = gatewayId;
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			loadGatewayConfig(conn);
			conn.commit();
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			throw new RuntimeException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return gatewayConfig;
	}

	private void loadGatewayConfig(Connection conn) {
		gatewayConfig = new GatewayConfig();
		GatewayDAO gwDao = new GatewayDAO();
		gwDao.setConnection(conn);

		Gateway gwDto = gwDao.selectByPK(gatewayId);
		if (null == gwDto) {
			throw new RuntimeException(
					"there is no record in table gateway!id = " + gatewayId);
		}
		gatewayConfig.setId(gatewayId);
		gatewayConfig.setEventHandlerNumber(gwDto.getMaxHandlerNumber());
		gatewayConfig.setLoggerName(gwDto.getLoggerName());
		gatewayConfig.setMonitorPort(gwDto.getMonitorPort());
		gatewayConfig.setName(gwDto.getName());
		if (0 < gwDto.getSessionTimeout() || null != gwDto.getLogLevelIntoDb()) {
			SessionConfig sessionConfig = new SessionConfig();
			if (0 < gwDto.getSessionTimeout()) {
				sessionConfig.setTimeout(gwDto.getSessionTimeout());
			}
			if (null != gwDto.getLogLevelIntoDb()) {
				sessionConfig.setLevelIntoDBText(gwDto.getLogLevelIntoDb());
			}
			gatewayConfig.setSessionConfig(sessionConfig);
		}

		loadModules(conn);

		loadChannelMainConfig(conn);
	}

	private void loadChannelMainConfig(Connection conn) {
		GatewayChannelRelationDAO gcrDao = new GatewayChannelRelationDAO();
		gcrDao.setConnection(conn);

		List<GatewayChannelRelation> gcrList = gcrDao
				.getAllChannelInGw(gatewayId);
		ChannelDAO channelDao = new ChannelDAO();
		channelDao.setConnection(conn);
		Channel channel = null;
		for (int i = 0; i < gcrList.size(); i++) {
			channel = channelDao.selectByPK(gcrList.get(i).getChannelId());
			ChannelMainConfig mainConfig = new ChannelMainConfig();
			mainConfig.setDatabaseChannelId(channel.getId());
			mainConfig.setDeploy(channel.getDeployPath());
			mainConfig.setId(channel.getChannelId());
			mainConfig.setName(channel.getName());
			if (TRUE.equals(channel.getStartup())) {
				mainConfig.setStartup(true);
			} else {
				mainConfig.setStartup(false);
			}
			gatewayConfig.setChannel(mainConfig.getId(), mainConfig);
		}
	}

	private void loadModules(Connection conn) {
		ModuleDAO moduleDao = new ModuleDAO();
		moduleDao.setConnection(conn);

		List<Module> moduleList = moduleDao.getAllModuleInGW(gatewayId);
		List list = null;
		if (null != moduleList && 0 < moduleList.size()) {
			list = new ArrayList();
			for (int i = 0; i < moduleList.size(); i++) {
				list.add(exchangeModule2ModuleConfig(moduleList.get(i), conn));
			}
		}
		gatewayConfig.setModules(list);
	}

	private ModuleConfig exchangeModule2ModuleConfig(Module module,
			Connection conn) {
		ModuleConfig moduleConfig = new ModuleConfig();
		moduleConfig.setClassName(module.getModuleClass());

		ModuleParameterRelationDAO mprDao = new ModuleParameterRelationDAO();
		mprDao.setConnection(conn);
		List<ModuleParameterRelation> mprList = mprDao
				.getAllParameter4Module(module.getId());
		ParameterDAO paramDao = new ParameterDAO();
		paramDao.setConnection(conn);
		for (int i = 0; i < mprList.size(); i++) {
			Parameter param = paramDao.selectByPK(mprList.get(i)
					.getParameterId());
			moduleConfig.setParameter(param.getParameterName(), param
					.getParameterValue());
		}

		return moduleConfig;
	}
}
