package com.fib.msgconverter.commgateway.dao.channel.dao;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.giantstone.base.dao.AbstractDAO;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.base.config.DAOConfiguration;

public class ChannelDAO extends AbstractDAO {

	public ChannelDAO() {
		super();
	}

	public ChannelDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ChannelDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Channel channel) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (channel == null) {
			throw new IllegalArgumentException("channel is NULL!");
		}
		if (channel.getId() != null && channel.getId().length() < 10) {
			throw new IllegalArgumentException("id too short" + channel.getId());
		}
		if (channel.getId() != null && channel.getId().length() > 10) {
			throw new IllegalArgumentException("id too long" + channel.getId());
		}
		if (channel.getName() != null && channel.getName().length() > 255) {
			throw new IllegalArgumentException("name too long" + channel.getName());
		}
		if (channel.getChannelId() == null || "".equals(channel.getChannelId())) {
			throw new IllegalArgumentException("channelId is null");
		}
		if (channel.getChannelId() != null && channel.getChannelId().length() > 255) {
			throw new IllegalArgumentException("channelId too long" + channel.getChannelId());
		}
		if (channel.getStartup() == null || "".equals(channel.getStartup())) {
			throw new IllegalArgumentException("startup is null");
		}
		if (channel.getStartup() != null && channel.getStartup().length() < 1) {
			throw new IllegalArgumentException("startup too short" + channel.getStartup());
		}
		if (channel.getStartup() != null && channel.getStartup().length() > 1) {
			throw new IllegalArgumentException("startup too long" + channel.getStartup());
		}
		if (channel.getClassName() == null || "".equals(channel.getClassName())) {
			throw new IllegalArgumentException("className is null");
		}
		if (channel.getClassName() != null && channel.getClassName().length() > 255) {
			throw new IllegalArgumentException("className too long" + channel.getClassName());
		}
		if (channel.getMode() != null && channel.getMode().length() < 4) {
			throw new IllegalArgumentException("mode too short" + channel.getMode());
		}
		if (channel.getMode() != null && channel.getMode().length() > 4) {
			throw new IllegalArgumentException("mode too long" + channel.getMode());
		}
		if (channel.getDescription() != null && channel.getDescription().length() > 255) {
			throw new IllegalArgumentException("description too long" + channel.getDescription());
		}
		if (channel.getMessageCodeRecognizerId() != null && channel.getMessageCodeRecognizerId().length() < 10) {
			throw new IllegalArgumentException(
					"messageCodeRecognizerId too short" + channel.getMessageCodeRecognizerId());
		}
		if (channel.getMessageCodeRecognizerId() != null && channel.getMessageCodeRecognizerId().length() > 10) {
			throw new IllegalArgumentException(
					"messageCodeRecognizerId too long" + channel.getMessageCodeRecognizerId());
		}
		if (channel.getReturnCodeRecognizerId() != null && channel.getReturnCodeRecognizerId().length() < 10) {
			throw new IllegalArgumentException(
					"returnCodeRecognizerId too short" + channel.getReturnCodeRecognizerId());
		}
		if (channel.getReturnCodeRecognizerId() != null && channel.getReturnCodeRecognizerId().length() > 10) {
			throw new IllegalArgumentException("returnCodeRecognizerId too long" + channel.getReturnCodeRecognizerId());
		}
		if (channel.getMbMappingGroup() != null && channel.getMbMappingGroup().length() > 255) {
			throw new IllegalArgumentException("mbMappingGroup too long" + channel.getMbMappingGroup());
		}
		if (channel.getConnectorId() != null && channel.getConnectorId().length() < 10) {
			throw new IllegalArgumentException("connectorId too short" + channel.getConnectorId());
		}
		if (channel.getConnectorId() != null && channel.getConnectorId().length() > 10) {
			throw new IllegalArgumentException("connectorId too long" + channel.getConnectorId());
		}
		if (channel.getChannelType() != null && channel.getChannelType().length() > 16) {
			throw new IllegalArgumentException("channelType too long" + channel.getChannelType());
		}
		if (channel.getDeployPath() == null || "".equals(channel.getDeployPath())) {
			throw new IllegalArgumentException("deployPath is null");
		}
		if (channel.getDeployPath() != null && channel.getDeployPath().length() > 255) {
			throw new IllegalArgumentException("deployPath too long" + channel.getDeployPath());
		}
		if (channel.getEventHandlerClassName() != null && channel.getEventHandlerClassName().length() > 255) {
			throw new IllegalArgumentException("eventHandlerClassName too long" + channel.getEventHandlerClassName());
		}
		if (channel.getDefaultProcessorId() != null && channel.getDefaultProcessorId().length() < 10) {
			throw new IllegalArgumentException("defaultProcessorId too short" + channel.getDefaultProcessorId());
		}
		if (channel.getDefaultProcessorId() != null && channel.getDefaultProcessorId().length() > 10) {
			throw new IllegalArgumentException("defaultProcessorId too long" + channel.getDefaultProcessorId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into channel(id,name,channel_id,startup,class_name,mode,description,message_code_recognizer_id,return_code_recognizer_id,mb_mapping_group,connector_id,channel_type,deploy_path,event_handler_class_name,default_processor_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, channel.getId());
			statment.setString(2, channel.getName());
			statment.setString(3, channel.getChannelId());
			statment.setString(4, channel.getStartup());
			statment.setString(5, channel.getClassName());
			statment.setString(6, channel.getMode());
			statment.setString(7, channel.getDescription());
			statment.setString(8, channel.getMessageCodeRecognizerId());
			statment.setString(9, channel.getReturnCodeRecognizerId());
			statment.setString(10, channel.getMbMappingGroup());
			statment.setString(11, channel.getConnectorId());
			statment.setString(12, channel.getChannelType());
			statment.setString(13, channel.getDeployPath());
			statment.setString(14, channel.getEventHandlerClassName());
			statment.setString(15, channel.getDefaultProcessorId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.insert() spend " + (endTime - startTime) + "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + channel.getId() + "\",name = \"" + channel.getName()
						+ "\",channel_id = \"" + channel.getChannelId() + "\",startup = \"" + channel.getStartup()
						+ "\",class_name = \"" + channel.getClassName() + "\",mode = \"" + channel.getMode()
						+ "\",description = \"" + channel.getDescription() + "\",message_code_recognizer_id = \""
						+ channel.getMessageCodeRecognizerId() + "\",return_code_recognizer_id = \""
						+ channel.getReturnCodeRecognizerId() + "\",mb_mapping_group = \"" + channel.getMbMappingGroup()
						+ "\",connector_id = \"" + channel.getConnectorId() + "\",channel_type = \""
						+ channel.getChannelType() + "\",deploy_path = \"" + channel.getDeployPath()
						+ "\",event_handler_class_name = \"" + channel.getEventHandlerClassName()
						+ "\",default_processor_id = \"" + channel.getDefaultProcessorId() + "\" ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
			return retFlag;

		} catch (SQLException e) {
			e.printStackTrace(System.err);
			if (!isInTransaction()) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return 0;
	}

	public int[] insertBatch(List<Channel> channelList) {
		// 获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (channelList == null) {
			throw new IllegalArgumentException("channelList is NULL!");
		}
		for (Channel channel : channelList) {
			if (channel.getId() != null && channel.getId().length() < 10) {
				throw new IllegalArgumentException("id too short" + channel.getId());
			}
			if (channel.getId() != null && channel.getId().length() > 10) {
				throw new IllegalArgumentException("id too long" + channel.getId());
			}
			if (channel.getName() != null && channel.getName().length() > 255) {
				throw new IllegalArgumentException("name too long" + channel.getName());
			}
			if (channel.getChannelId() == null || "".equals(channel.getChannelId())) {
				throw new IllegalArgumentException("channelId is null");
			}
			if (channel.getChannelId() != null && channel.getChannelId().length() > 255) {
				throw new IllegalArgumentException("channelId too long" + channel.getChannelId());
			}
			if (channel.getStartup() == null || "".equals(channel.getStartup())) {
				throw new IllegalArgumentException("startup is null");
			}
			if (channel.getStartup() != null && channel.getStartup().length() < 1) {
				throw new IllegalArgumentException("startup too short" + channel.getStartup());
			}
			if (channel.getStartup() != null && channel.getStartup().length() > 1) {
				throw new IllegalArgumentException("startup too long" + channel.getStartup());
			}
			if (channel.getClassName() == null || "".equals(channel.getClassName())) {
				throw new IllegalArgumentException("className is null");
			}
			if (channel.getClassName() != null && channel.getClassName().length() > 255) {
				throw new IllegalArgumentException("className too long" + channel.getClassName());
			}
			if (channel.getMode() != null && channel.getMode().length() < 4) {
				throw new IllegalArgumentException("mode too short" + channel.getMode());
			}
			if (channel.getMode() != null && channel.getMode().length() > 4) {
				throw new IllegalArgumentException("mode too long" + channel.getMode());
			}
			if (channel.getDescription() != null && channel.getDescription().length() > 255) {
				throw new IllegalArgumentException("description too long" + channel.getDescription());
			}
			if (channel.getMessageCodeRecognizerId() != null && channel.getMessageCodeRecognizerId().length() < 10) {
				throw new IllegalArgumentException(
						"messageCodeRecognizerId too short" + channel.getMessageCodeRecognizerId());
			}
			if (channel.getMessageCodeRecognizerId() != null && channel.getMessageCodeRecognizerId().length() > 10) {
				throw new IllegalArgumentException(
						"messageCodeRecognizerId too long" + channel.getMessageCodeRecognizerId());
			}
			if (channel.getReturnCodeRecognizerId() != null && channel.getReturnCodeRecognizerId().length() < 10) {
				throw new IllegalArgumentException(
						"returnCodeRecognizerId too short" + channel.getReturnCodeRecognizerId());
			}
			if (channel.getReturnCodeRecognizerId() != null && channel.getReturnCodeRecognizerId().length() > 10) {
				throw new IllegalArgumentException(
						"returnCodeRecognizerId too long" + channel.getReturnCodeRecognizerId());
			}
			if (channel.getMbMappingGroup() != null && channel.getMbMappingGroup().length() > 255) {
				throw new IllegalArgumentException("mbMappingGroup too long" + channel.getMbMappingGroup());
			}
			if (channel.getConnectorId() != null && channel.getConnectorId().length() < 10) {
				throw new IllegalArgumentException("connectorId too short" + channel.getConnectorId());
			}
			if (channel.getConnectorId() != null && channel.getConnectorId().length() > 10) {
				throw new IllegalArgumentException("connectorId too long" + channel.getConnectorId());
			}
			if (channel.getChannelType() != null && channel.getChannelType().length() > 16) {
				throw new IllegalArgumentException("channelType too long" + channel.getChannelType());
			}
			if (channel.getDeployPath() == null || "".equals(channel.getDeployPath())) {
				throw new IllegalArgumentException("deployPath is null");
			}
			if (channel.getDeployPath() != null && channel.getDeployPath().length() > 255) {
				throw new IllegalArgumentException("deployPath too long" + channel.getDeployPath());
			}
			if (channel.getEventHandlerClassName() != null && channel.getEventHandlerClassName().length() > 255) {
				throw new IllegalArgumentException(
						"eventHandlerClassName too long" + channel.getEventHandlerClassName());
			}
			if (channel.getDefaultProcessorId() != null && channel.getDefaultProcessorId().length() < 10) {
				throw new IllegalArgumentException("defaultProcessorId too short" + channel.getDefaultProcessorId());
			}
			if (channel.getDefaultProcessorId() != null && channel.getDefaultProcessorId().length() > 10) {
				throw new IllegalArgumentException("defaultProcessorId too long" + channel.getDefaultProcessorId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into channel(id,name,channel_id,startup,class_name,mode,description,message_code_recognizer_id,return_code_recognizer_id,mb_mapping_group,connector_id,channel_type,deploy_path,event_handler_class_name,default_processor_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql);
			for (Channel channel : channelList) {
				statment.setString(1, channel.getId());
				statment.setString(2, channel.getName());
				statment.setString(3, channel.getChannelId());
				statment.setString(4, channel.getStartup());
				statment.setString(5, channel.getClassName());
				statment.setString(6, channel.getMode());
				statment.setString(7, channel.getDescription());
				statment.setString(8, channel.getMessageCodeRecognizerId());
				statment.setString(9, channel.getReturnCodeRecognizerId());
				statment.setString(10, channel.getMbMappingGroup());
				statment.setString(11, channel.getConnectorId());
				statment.setString(12, channel.getChannelType());
				statment.setString(13, channel.getDeployPath());
				statment.setString(14, channel.getEventHandlerClassName());
				statment.setString(15, channel.getDefaultProcessorId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.insertBatch() spend " + (endTime - startTime) + "ms. retFlag = " + retFlag + " SQL:"
						+ sql + ";");
			}
			return retFlag;
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			if (!isInTransaction()) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			ExceptionUtil.throwActualException(e);
			return null;
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
	}

	public int update(Channel channel) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (channel == null) {
			throw new IllegalArgumentException("channel is NULL!");
		}

		PreparedStatement statment = null;
		if (channel.getId() != null && channel.getId().length() < 10) {
			throw new IllegalArgumentException("id too short" + channel.getId());
		}
		if (channel.getId() != null && channel.getId().length() > 10) {
			throw new IllegalArgumentException("id too long" + channel.getId());
		}
		if (channel.getName() != null && channel.getName().length() > 255) {
			throw new IllegalArgumentException("name too long" + channel.getName());
		}
		if (channel.getChannelId() == null || "".equals(channel.getChannelId())) {
			throw new IllegalArgumentException("channelId is null");
		}
		if (channel.getChannelId() != null && channel.getChannelId().length() > 255) {
			throw new IllegalArgumentException("channelId too long" + channel.getChannelId());
		}
		if (channel.getStartup() == null || "".equals(channel.getStartup())) {
			throw new IllegalArgumentException("startup is null");
		}
		if (channel.getStartup() != null && channel.getStartup().length() < 1) {
			throw new IllegalArgumentException("startup too short" + channel.getStartup());
		}
		if (channel.getStartup() != null && channel.getStartup().length() > 1) {
			throw new IllegalArgumentException("startup too long" + channel.getStartup());
		}
		if (channel.getClassName() == null || "".equals(channel.getClassName())) {
			throw new IllegalArgumentException("className is null");
		}
		if (channel.getClassName() != null && channel.getClassName().length() > 255) {
			throw new IllegalArgumentException("className too long" + channel.getClassName());
		}
		if (channel.getMode() != null && channel.getMode().length() < 4) {
			throw new IllegalArgumentException("mode too short" + channel.getMode());
		}
		if (channel.getMode() != null && channel.getMode().length() > 4) {
			throw new IllegalArgumentException("mode too long" + channel.getMode());
		}
		if (channel.getDescription() != null && channel.getDescription().length() > 255) {
			throw new IllegalArgumentException("description too long" + channel.getDescription());
		}
		if (channel.getMessageCodeRecognizerId() != null && channel.getMessageCodeRecognizerId().length() < 10) {
			throw new IllegalArgumentException(
					"messageCodeRecognizerId too short" + channel.getMessageCodeRecognizerId());
		}
		if (channel.getMessageCodeRecognizerId() != null && channel.getMessageCodeRecognizerId().length() > 10) {
			throw new IllegalArgumentException(
					"messageCodeRecognizerId too long" + channel.getMessageCodeRecognizerId());
		}
		if (channel.getReturnCodeRecognizerId() != null && channel.getReturnCodeRecognizerId().length() < 10) {
			throw new IllegalArgumentException(
					"returnCodeRecognizerId too short" + channel.getReturnCodeRecognizerId());
		}
		if (channel.getReturnCodeRecognizerId() != null && channel.getReturnCodeRecognizerId().length() > 10) {
			throw new IllegalArgumentException("returnCodeRecognizerId too long" + channel.getReturnCodeRecognizerId());
		}
		if (channel.getMbMappingGroup() != null && channel.getMbMappingGroup().length() > 255) {
			throw new IllegalArgumentException("mbMappingGroup too long" + channel.getMbMappingGroup());
		}
		if (channel.getConnectorId() != null && channel.getConnectorId().length() < 10) {
			throw new IllegalArgumentException("connectorId too short" + channel.getConnectorId());
		}
		if (channel.getConnectorId() != null && channel.getConnectorId().length() > 10) {
			throw new IllegalArgumentException("connectorId too long" + channel.getConnectorId());
		}
		if (channel.getChannelType() != null && channel.getChannelType().length() > 16) {
			throw new IllegalArgumentException("channelType too long" + channel.getChannelType());
		}
		if (channel.getDeployPath() == null || "".equals(channel.getDeployPath())) {
			throw new IllegalArgumentException("deployPath is null");
		}
		if (channel.getDeployPath() != null && channel.getDeployPath().length() > 255) {
			throw new IllegalArgumentException("deployPath too long" + channel.getDeployPath());
		}
		if (channel.getEventHandlerClassName() != null && channel.getEventHandlerClassName().length() > 255) {
			throw new IllegalArgumentException("eventHandlerClassName too long" + channel.getEventHandlerClassName());
		}
		if (channel.getDefaultProcessorId() != null && channel.getDefaultProcessorId().length() < 10) {
			throw new IllegalArgumentException("defaultProcessorId too short" + channel.getDefaultProcessorId());
		}
		if (channel.getDefaultProcessorId() != null && channel.getDefaultProcessorId().length() > 10) {
			throw new IllegalArgumentException("defaultProcessorId too long" + channel.getDefaultProcessorId());
		}
		try {
			String sql = "UPDATE channel SET id=?,name=?,channel_id=?,startup=?,class_name=?,mode=?,description=?,message_code_recognizer_id=?,return_code_recognizer_id=?,mb_mapping_group=?,connector_id=?,channel_type=?,deploy_path=?,event_handler_class_name=?,default_processor_id=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, channel.getId());
			statment.setString(2, channel.getName());
			statment.setString(3, channel.getChannelId());
			statment.setString(4, channel.getStartup());
			statment.setString(5, channel.getClassName());
			statment.setString(6, channel.getMode());
			statment.setString(7, channel.getDescription());
			statment.setString(8, channel.getMessageCodeRecognizerId());
			statment.setString(9, channel.getReturnCodeRecognizerId());
			statment.setString(10, channel.getMbMappingGroup());
			statment.setString(11, channel.getConnectorId());
			statment.setString(12, channel.getChannelType());
			statment.setString(13, channel.getDeployPath());
			statment.setString(14, channel.getEventHandlerClassName());
			statment.setString(15, channel.getDefaultProcessorId());
			statment.setString(16, channel.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.update() spend " + (endTime - startTime) + "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + channel.getId() + "\",name = \"" + channel.getName()
						+ "\",channel_id = \"" + channel.getChannelId() + "\",startup = \"" + channel.getStartup()
						+ "\",class_name = \"" + channel.getClassName() + "\",mode = \"" + channel.getMode()
						+ "\",description = \"" + channel.getDescription() + "\",message_code_recognizer_id = \""
						+ channel.getMessageCodeRecognizerId() + "\",return_code_recognizer_id = \""
						+ channel.getReturnCodeRecognizerId() + "\",mb_mapping_group = \"" + channel.getMbMappingGroup()
						+ "\",connector_id = \"" + channel.getConnectorId() + "\",channel_type = \""
						+ channel.getChannelType() + "\",deploy_path = \"" + channel.getDeployPath()
						+ "\",event_handler_class_name = \"" + channel.getEventHandlerClassName()
						+ "\",default_processor_id = \"" + channel.getDefaultProcessorId() + "\" ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
			return retFlag;

		} catch (SQLException e) {
			e.printStackTrace(System.err);
			if (!isInTransaction()) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return 0;
	}

	public int dynamicUpdate(Map<String,String> primaryKey, Map<String,Object> updateFields) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (primaryKey == null) {
			throw new IllegalArgumentException("primaryKey is NULL!");
		}
		if (updateFields == null) {
			throw new IllegalArgumentException("updateFields is NULL!");
		}

		PreparedStatement statment = null;
		if (!primaryKey.containsKey("id")) {
			throw new IllegalArgumentException("id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE channel SET ");
			Iterator<String> it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("name".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("channel_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("startup".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("class_name".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("mode".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("description".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("message_code_recognizer_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("return_code_recognizer_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("mb_mapping_group".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("connector_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("channel_type".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("deploy_path".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("event_handler_class_name".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("default_processor_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("ChannelDAO.dynamicUpdate() spend " + (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql.toString() + "; parameters : ");
				Iterator<String> priIt = updateFields.keySet().iterator();
				while (priIt.hasNext()) {
					tmpKey = (String) priIt.next();
					tmpStr = (String) updateFields.get(tmpKey);
					sbDebug.append(tmpKey);
					sbDebug.append(" = ");
					sbDebug.append(tmpStr);
					sbDebug.append(", ");
				}
				sbDebug.append("primaryKey :");
				priIt = primaryKey.keySet().iterator();
				while (priIt.hasNext()) {
					tmpKey = (String) priIt.next();
					tmpStr = (String) primaryKey.get(tmpKey);
					sbDebug.append(tmpKey);
					sbDebug.append(" = ");
					sbDebug.append(tmpStr);
					sbDebug.append(", ");
				}
				debug(sbDebug.toString());
			}

			if (!isInTransaction()) {
				conn.commit();
			}
			return retFlag;
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			if (!isInTransaction()) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return 0;
	}

	public int deleteByPK(String id) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is NULL!");
		}

		PreparedStatement statment = null;
		if (id != null && id.length() < 10) {
			throw new IllegalArgumentException("id too short" + id);
		}
		if (id != null && id.length() > 10) {
			throw new IllegalArgumentException("id too long" + id);
		}

		try {
			String sql = "DELETE FROM channel where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.deleteByPK() spend " + (endTime - startTime) + "ms. retFlag = " + retFlag + " SQL:"
						+ sql + "; parameters : id = \"" + id + "\" ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
			return retFlag;
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			if (!isInTransaction()) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return 0;
	}

	public Channel selectByPK(String id) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (id != null && id.length() < 10) {
			throw new IllegalArgumentException("id too short" + id);
		}
		if (id != null && id.length() > 10) {
			throw new IllegalArgumentException("id too long" + id);
		}
		ResultSet resultSet = null;
		Channel returnDTO = null;

		try {
			String sql = "SELECT * FROM channel where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.selectByPK() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Channel();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setStartup(resultSet.getString("startup"));
				returnDTO.setClassName(resultSet.getString("class_name"));
				returnDTO.setMode(resultSet.getString("mode"));
				returnDTO.setDescription(resultSet.getString("description"));
				returnDTO.setMessageCodeRecognizerId(resultSet.getString("message_code_recognizer_id"));
				returnDTO.setReturnCodeRecognizerId(resultSet.getString("return_code_recognizer_id"));
				returnDTO.setMbMappingGroup(resultSet.getString("mb_mapping_group"));
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setChannelType(resultSet.getString("channel_type"));
				returnDTO.setDeployPath(resultSet.getString("deploy_path"));
				returnDTO.setEventHandlerClassName(resultSet.getString("event_handler_class_name"));
				returnDTO.setDefaultProcessorId(resultSet.getString("default_processor_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return returnDTO;
	}

	public List<Channel> findAll() {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Channel returnDTO = null;
		List<Channel> list = new ArrayList<>();

		try {
			String sql = "select * from channel order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.findAll() spend " + (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new Channel();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setStartup(resultSet.getString("startup"));
				returnDTO.setClassName(resultSet.getString("class_name"));
				returnDTO.setMode(resultSet.getString("mode"));
				returnDTO.setDescription(resultSet.getString("description"));
				returnDTO.setMessageCodeRecognizerId(resultSet.getString("message_code_recognizer_id"));
				returnDTO.setReturnCodeRecognizerId(resultSet.getString("return_code_recognizer_id"));
				returnDTO.setMbMappingGroup(resultSet.getString("mb_mapping_group"));
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setChannelType(resultSet.getString("channel_type"));
				returnDTO.setDeployPath(resultSet.getString("deploy_path"));
				returnDTO.setEventHandlerClassName(resultSet.getString("event_handler_class_name"));
				returnDTO.setDefaultProcessorId(resultSet.getString("default_processor_id"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List<Channel> findAll(int pageNum, int pageLength) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Channel returnDTO = null;
		List<Channel> list = new ArrayList<>();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from channel ) A where rownum <= " + endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum + " * from (select Top " + endNum
						+ " * from channel					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select id,name,channel_id,startup,class_name,mode,description,message_code_recognizer_id,return_code_recognizer_id,mb_mapping_group,connector_id,channel_type,deploy_path,event_handler_class_name,default_processor_id, rownumber() over(order by id) as rn from channel ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from channel order by id limit " + startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select top " + pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,name,channel_id,startup,class_name,mode,description,message_code_recognizer_id,return_code_recognizer_id,mb_mapping_group,connector_id,channel_type,deploy_path,event_handler_class_name,default_processor_id from channel ) A where rownumber > "
						+ startNum;
			} else {
				throw new RuntimeException(
						"the DAOCodeGenerator support mysql,oracle,mssql,db2,sqlserver2005. but batabase_type is "
								+ DAOConfiguration.getDATABASE_TYPE());
			}

			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.findAll()(pageNum:" + pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new Channel();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setStartup(resultSet.getString("startup"));
				returnDTO.setClassName(resultSet.getString("class_name"));
				returnDTO.setMode(resultSet.getString("mode"));
				returnDTO.setDescription(resultSet.getString("description"));
				returnDTO.setMessageCodeRecognizerId(resultSet.getString("message_code_recognizer_id"));
				returnDTO.setReturnCodeRecognizerId(resultSet.getString("return_code_recognizer_id"));
				returnDTO.setMbMappingGroup(resultSet.getString("mb_mapping_group"));
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setChannelType(resultSet.getString("channel_type"));
				returnDTO.setDeployPath(resultSet.getString("deploy_path"));
				returnDTO.setEventHandlerClassName(resultSet.getString("event_handler_class_name"));
				returnDTO.setDefaultProcessorId(resultSet.getString("default_processor_id"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List<Channel> findByWhere(String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Channel returnDTO = null;
		List<Channel> list = new ArrayList<>();

		try {
			String sql = "select * from channel where " + where + " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.findByWhere()() spend " + (endTime - startTime) + "ms. SQL:" + sql + "; parameter : "
						+ where);
			}
			while (resultSet.next()) {
				returnDTO = new Channel();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setStartup(resultSet.getString("startup"));
				returnDTO.setClassName(resultSet.getString("class_name"));
				returnDTO.setMode(resultSet.getString("mode"));
				returnDTO.setDescription(resultSet.getString("description"));
				returnDTO.setMessageCodeRecognizerId(resultSet.getString("message_code_recognizer_id"));
				returnDTO.setReturnCodeRecognizerId(resultSet.getString("return_code_recognizer_id"));
				returnDTO.setMbMappingGroup(resultSet.getString("mb_mapping_group"));
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setChannelType(resultSet.getString("channel_type"));
				returnDTO.setDeployPath(resultSet.getString("deploy_path"));
				returnDTO.setEventHandlerClassName(resultSet.getString("event_handler_class_name"));
				returnDTO.setDefaultProcessorId(resultSet.getString("default_processor_id"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List<Channel> findByWhere(String where, int pageNum, int pageLength) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Channel returnDTO = null;
		List<Channel> list = new ArrayList<>();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from channel where " + where + " order by id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from channel where  " + where
						+ " ) A where rownum <= " + endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum + " * from (select Top " + endNum
						+ " * from channel					 where " + where + " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select id,name,channel_id,startup,class_name,mode,description,message_code_recognizer_id,return_code_recognizer_id,mb_mapping_group,connector_id,channel_type,deploy_path,event_handler_class_name,default_processor_id, rownumber() over(order by id) as rn from channel where "
						+ where + " ) as a1 where a1.rn between " + startNum + " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select top " + pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,name,channel_id,startup,class_name,mode,description,message_code_recognizer_id,return_code_recognizer_id,mb_mapping_group,connector_id,channel_type,deploy_path,event_handler_class_name,default_processor_id from channel  where "
						+ where + " ) A where rownumber > " + startNum;
			} else {
				throw new RuntimeException(
						"the DAOCodeGenerator support mysql,oracle,mssql,db2,sqlserver2005. but batabase_type is "
								+ DAOConfiguration.getDATABASE_TYPE());
			}

			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.findByWhere()(pageNum:" + pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new Channel();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setStartup(resultSet.getString("startup"));
				returnDTO.setClassName(resultSet.getString("class_name"));
				returnDTO.setMode(resultSet.getString("mode"));
				returnDTO.setDescription(resultSet.getString("description"));
				returnDTO.setMessageCodeRecognizerId(resultSet.getString("message_code_recognizer_id"));
				returnDTO.setReturnCodeRecognizerId(resultSet.getString("return_code_recognizer_id"));
				returnDTO.setMbMappingGroup(resultSet.getString("mb_mapping_group"));
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setChannelType(resultSet.getString("channel_type"));
				returnDTO.setDeployPath(resultSet.getString("deploy_path"));
				returnDTO.setEventHandlerClassName(resultSet.getString("event_handler_class_name"));
				returnDTO.setDefaultProcessorId(resultSet.getString("default_processor_id"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public long getTotalRecords() {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		long totalRecords = 0;

		try {
			String sql = "select  count(*) from channel";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.getTotalRecords() spend " + (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}

			if (resultSet.next()) {
				totalRecords = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return totalRecords;
	}

	public long getTotalRecordsByWhere(String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		long totalRecords = 0;

		try {
			String sql = "select  count(*) from channel where " + where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelDAO.getTotalRecords() spend " + (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}

			if (resultSet.next()) {
				totalRecords = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}

			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
		}
		return totalRecords;
	}
}
