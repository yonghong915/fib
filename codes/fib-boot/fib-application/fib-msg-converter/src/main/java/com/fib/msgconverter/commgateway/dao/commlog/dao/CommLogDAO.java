package com.fib.msgconverter.commgateway.dao.commlog.dao;

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

@SuppressWarnings("all")
public class CommLogDAO extends AbstractDAO {

	public CommLogDAO() {
		super();
	}

	public CommLogDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public CommLogDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public void insert(CommLog commLog) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (commLog == null) {
			throw new IllegalArgumentException("commLog is NULL!");
		}
		if (commLog.getId() != null && commLog.getId().length() > 20) {
			throw new IllegalArgumentException("id too long" + commLog.getId());
		}
		if (commLog.getGatewayId() != null
				&& commLog.getGatewayId().length() > 80) {
			throw new IllegalArgumentException("gatewayId too long"
					+ commLog.getGatewayId());
		}
		if (commLog.getStartDate() != null
				&& commLog.getStartDate().length() < 8) {
			throw new IllegalArgumentException("startDate too short"
					+ commLog.getStartDate());
		}
		if (commLog.getStartDate() != null
				&& commLog.getStartDate().length() > 8) {
			throw new IllegalArgumentException("startDate too long"
					+ commLog.getStartDate());
		}
		if (commLog.getStartTime() != null
				&& commLog.getStartTime().length() < 6) {
			throw new IllegalArgumentException("startTime too short"
					+ commLog.getStartTime());
		}
		if (commLog.getStartTime() != null
				&& commLog.getStartTime().length() > 6) {
			throw new IllegalArgumentException("startTime too long"
					+ commLog.getStartTime());
		}
		if (commLog.getEndDate() != null && commLog.getEndDate().length() < 8) {
			throw new IllegalArgumentException("endDate too short"
					+ commLog.getEndDate());
		}
		if (commLog.getEndDate() != null && commLog.getEndDate().length() > 8) {
			throw new IllegalArgumentException("endDate too long"
					+ commLog.getEndDate());
		}
		if (commLog.getEndTime() != null && commLog.getEndTime().length() < 6) {
			throw new IllegalArgumentException("endTime too short"
					+ commLog.getEndTime());
		}
		if (commLog.getEndTime() != null && commLog.getEndTime().length() > 6) {
			throw new IllegalArgumentException("endTime too long"
					+ commLog.getEndTime());
		}
		if (commLog.getState() != null && commLog.getState().length() < 2) {
			throw new IllegalArgumentException("state too short"
					+ commLog.getState());
		}
		if (commLog.getState() != null && commLog.getState().length() > 2) {
			throw new IllegalArgumentException("state too long"
					+ commLog.getState());
		}
		if (commLog.getType() != null && commLog.getType().length() < 2) {
			throw new IllegalArgumentException("type too short"
					+ commLog.getType());
		}
		if (commLog.getType() != null && commLog.getType().length() > 2) {
			throw new IllegalArgumentException("type too long"
					+ commLog.getType());
		}
		if (commLog.getBranchCode() != null
				&& commLog.getBranchCode().length() > 10) {
			throw new IllegalArgumentException("branchCode too long"
					+ commLog.getBranchCode());
		}
		if (commLog.getAppCategory() != null
				&& commLog.getAppCategory().length() > 10) {
			throw new IllegalArgumentException("appCategory too long"
					+ commLog.getAppCategory());
		}
		if (commLog.getProcessorId() != null
				&& commLog.getProcessorId().length() > 80) {
			throw new IllegalArgumentException("processorId too long"
					+ commLog.getProcessorId());
		}
		if (commLog.getSourceChannelId() != null
				&& commLog.getSourceChannelId().length() > 80) {
			throw new IllegalArgumentException("sourceChannelId too long"
					+ commLog.getSourceChannelId());
		}
		if (commLog.getSourceChannelName() != null
				&& commLog.getSourceChannelName().length() > 80) {
			throw new IllegalArgumentException("sourceChannelName too long"
					+ commLog.getSourceChannelName());
		}
		if (commLog.getDestChannelId() != null
				&& commLog.getDestChannelId().length() > 80) {
			throw new IllegalArgumentException("destChannelId too long"
					+ commLog.getDestChannelId());
		}
		if (commLog.getDestChannelName() != null
				&& commLog.getDestChannelName().length() > 80) {
			throw new IllegalArgumentException("destChannelName too long"
					+ commLog.getDestChannelName());
		}
		if (commLog.getErrorType() != null
				&& commLog.getErrorType().length() < 4) {
			throw new IllegalArgumentException("errorType too short"
					+ commLog.getErrorType());
		}
		if (commLog.getErrorType() != null
				&& commLog.getErrorType().length() > 4) {
			throw new IllegalArgumentException("errorType too long"
					+ commLog.getErrorType());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into comm_log(id,gateway_id,start_date,start_time,end_date,end_time,state,type,branch_code,app_category,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, commLog.getId());
			statment.setString(2, commLog.getGatewayId());
			statment.setString(3, commLog.getStartDate());
			statment.setString(4, commLog.getStartTime());
			statment.setString(5, commLog.getEndDate());
			statment.setString(6, commLog.getEndTime());
			statment.setString(7, commLog.getState());
			statment.setString(8, commLog.getType());
			statment.setString(9, commLog.getBranchCode());
			statment.setString(10, commLog.getAppCategory());
			statment.setString(11, commLog.getProcessorId());
			statment.setString(12, commLog.getSourceChannelId());
			statment.setString(13, commLog.getSourceChannelName());
			statment.setString(14, commLog.getDestChannelId());
			statment.setString(15, commLog.getDestChannelName());
			statment.setString(16, commLog.getErrorType());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.insert() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + commLog.getId()
						+ "\",gateway_id = \"" + commLog.getGatewayId()
						+ "\",start_date = \"" + commLog.getStartDate()
						+ "\",start_time = \"" + commLog.getStartTime()
						+ "\",end_date = \"" + commLog.getEndDate()
						+ "\",end_time = \"" + commLog.getEndTime()
						+ "\",state = \"" + commLog.getState() + "\",type = \""
						+ commLog.getType() + "\",branch_code = \""
						+ commLog.getBranchCode() + "\",app_category = \""
						+ commLog.getAppCategory() + "\",processor_id = \""
						+ commLog.getProcessorId()
						+ "\",source_channel_id = \""
						+ commLog.getSourceChannelId()
						+ "\",source_channel_name = \""
						+ commLog.getSourceChannelName()
						+ "\",dest_channel_id = \""
						+ commLog.getDestChannelId()
						+ "\",dest_channel_name = \""
						+ commLog.getDestChannelName() + "\",error_type = \""
						+ commLog.getErrorType() + "\" ");
			}
			if (retFlag == 0) {
				throw new RuntimeException("insert failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}

	public void update(CommLog commLog) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (commLog == null) {
			throw new IllegalArgumentException("commLog is NULL!");
		}

		PreparedStatement statment = null;
		if (commLog.getId() != null && commLog.getId().length() > 20) {
			throw new IllegalArgumentException("id too long" + commLog.getId());
		}
		if (commLog.getGatewayId() != null
				&& commLog.getGatewayId().length() > 80) {
			throw new IllegalArgumentException("gatewayId too long"
					+ commLog.getGatewayId());
		}
		if (commLog.getStartDate() != null
				&& commLog.getStartDate().length() < 8) {
			throw new IllegalArgumentException("startDate too short"
					+ commLog.getStartDate());
		}
		if (commLog.getStartDate() != null
				&& commLog.getStartDate().length() > 8) {
			throw new IllegalArgumentException("startDate too long"
					+ commLog.getStartDate());
		}
		if (commLog.getStartTime() != null
				&& commLog.getStartTime().length() < 6) {
			throw new IllegalArgumentException("startTime too short"
					+ commLog.getStartTime());
		}
		if (commLog.getStartTime() != null
				&& commLog.getStartTime().length() > 6) {
			throw new IllegalArgumentException("startTime too long"
					+ commLog.getStartTime());
		}
		if (commLog.getEndDate() != null && commLog.getEndDate().length() < 8) {
			throw new IllegalArgumentException("endDate too short"
					+ commLog.getEndDate());
		}
		if (commLog.getEndDate() != null && commLog.getEndDate().length() > 8) {
			throw new IllegalArgumentException("endDate too long"
					+ commLog.getEndDate());
		}
		if (commLog.getEndTime() != null && commLog.getEndTime().length() < 6) {
			throw new IllegalArgumentException("endTime too short"
					+ commLog.getEndTime());
		}
		if (commLog.getEndTime() != null && commLog.getEndTime().length() > 6) {
			throw new IllegalArgumentException("endTime too long"
					+ commLog.getEndTime());
		}
		if (commLog.getState() != null && commLog.getState().length() < 2) {
			throw new IllegalArgumentException("state too short"
					+ commLog.getState());
		}
		if (commLog.getState() != null && commLog.getState().length() > 2) {
			throw new IllegalArgumentException("state too long"
					+ commLog.getState());
		}
		if (commLog.getType() != null && commLog.getType().length() < 2) {
			throw new IllegalArgumentException("type too short"
					+ commLog.getType());
		}
		if (commLog.getType() != null && commLog.getType().length() > 2) {
			throw new IllegalArgumentException("type too long"
					+ commLog.getType());
		}
		if (commLog.getBranchCode() != null
				&& commLog.getBranchCode().length() > 10) {
			throw new IllegalArgumentException("branchCode too long"
					+ commLog.getBranchCode());
		}
		if (commLog.getAppCategory() != null
				&& commLog.getAppCategory().length() > 10) {
			throw new IllegalArgumentException("appCategory too long"
					+ commLog.getAppCategory());
		}
		if (commLog.getProcessorId() != null
				&& commLog.getProcessorId().length() > 80) {
			throw new IllegalArgumentException("processorId too long"
					+ commLog.getProcessorId());
		}
		if (commLog.getSourceChannelId() != null
				&& commLog.getSourceChannelId().length() > 80) {
			throw new IllegalArgumentException("sourceChannelId too long"
					+ commLog.getSourceChannelId());
		}
		if (commLog.getSourceChannelName() != null
				&& commLog.getSourceChannelName().length() > 80) {
			throw new IllegalArgumentException("sourceChannelName too long"
					+ commLog.getSourceChannelName());
		}
		if (commLog.getDestChannelId() != null
				&& commLog.getDestChannelId().length() > 80) {
			throw new IllegalArgumentException("destChannelId too long"
					+ commLog.getDestChannelId());
		}
		if (commLog.getDestChannelName() != null
				&& commLog.getDestChannelName().length() > 80) {
			throw new IllegalArgumentException("destChannelName too long"
					+ commLog.getDestChannelName());
		}
		if (commLog.getErrorType() != null
				&& commLog.getErrorType().length() < 4) {
			throw new IllegalArgumentException("errorType too short"
					+ commLog.getErrorType());
		}
		if (commLog.getErrorType() != null
				&& commLog.getErrorType().length() > 4) {
			throw new IllegalArgumentException("errorType too long"
					+ commLog.getErrorType());
		}
		try {
			String sql = "UPDATE comm_log SET id=?,gateway_id=?,start_date=?,start_time=?,end_date=?,end_time=?,state=?,type=?,branch_code=?,app_category=?,processor_id=?,source_channel_id=?,source_channel_name=?,dest_channel_id=?,dest_channel_name=?,error_type=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, commLog.getId());
			statment.setString(2, commLog.getGatewayId());
			statment.setString(3, commLog.getStartDate());
			statment.setString(4, commLog.getStartTime());
			statment.setString(5, commLog.getEndDate());
			statment.setString(6, commLog.getEndTime());
			statment.setString(7, commLog.getState());
			statment.setString(8, commLog.getType());
			statment.setString(9, commLog.getBranchCode());
			statment.setString(10, commLog.getAppCategory());
			statment.setString(11, commLog.getProcessorId());
			statment.setString(12, commLog.getSourceChannelId());
			statment.setString(13, commLog.getSourceChannelName());
			statment.setString(14, commLog.getDestChannelId());
			statment.setString(15, commLog.getDestChannelName());
			statment.setString(16, commLog.getErrorType());
			statment.setString(17, commLog.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.update() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + commLog.getId()
						+ "\",gateway_id = \"" + commLog.getGatewayId()
						+ "\",start_date = \"" + commLog.getStartDate()
						+ "\",start_time = \"" + commLog.getStartTime()
						+ "\",end_date = \"" + commLog.getEndDate()
						+ "\",end_time = \"" + commLog.getEndTime()
						+ "\",state = \"" + commLog.getState() + "\",type = \""
						+ commLog.getType() + "\",branch_code = \""
						+ commLog.getBranchCode() + "\",app_category = \""
						+ commLog.getAppCategory() + "\",processor_id = \""
						+ commLog.getProcessorId()
						+ "\",source_channel_id = \""
						+ commLog.getSourceChannelId()
						+ "\",source_channel_name = \""
						+ commLog.getSourceChannelName()
						+ "\",dest_channel_id = \""
						+ commLog.getDestChannelId()
						+ "\",dest_channel_name = \""
						+ commLog.getDestChannelName() + "\",error_type = \""
						+ commLog.getErrorType() + "\" ");
			}
			if (retFlag == 0) {
				throw new RuntimeException("update failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}

	public void dynamicUpdate(Map primaryKey, Map updateFields) {
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
			sql.append("UPDATE comm_log SET ");
			Iterator it = updateFields.keySet().iterator();
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
				if ("gateway_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("start_date".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("start_time".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("end_date".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("end_time".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("state".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("type".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("branch_code".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("app_category".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("processor_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("source_channel_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("source_channel_name".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("dest_channel_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("dest_channel_name".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("error_type".equalsIgnoreCase(tmpKey)) {
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
				sbDebug.append("CommLogDAO.dynamicUpdate() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql.toString() + "; parameters : ");
				Iterator priIt = updateFields.keySet().iterator();
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
			if (retFlag == 0) {
				throw new RuntimeException("update failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}

	public void deleteByPK(String id) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is NULL!");
		}

		PreparedStatement statment = null;
		if (id != null && id.length() > 20) {
			throw new IllegalArgumentException("id too long" + id);
		}

		try {
			String sql = "DELETE FROM comm_log where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.deleteByPK() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (retFlag == 0) {
				throw new RuntimeException("delete failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}

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

	}

	public CommLog selectByPK(String id) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (id != null && id.length() > 20) {
			throw new IllegalArgumentException("id too long" + id);
		}
		ResultSet resultSet = null;
		CommLog returnDTO = null;

		try {
			String sql = "SELECT * FROM comm_log where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.selectByPK() spend " + (endTime - startTime)
						+ "ms. SQL:" + sql + "; parameters : id = \"" + id
						+ "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return returnDTO;
	}

	public List findAll() {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from comm_log order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.findAll() spend " + (endTime - startTime)
						+ "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List findAll(int pageNum, int pageLength) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from comm_log ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from comm_log					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,gateway_id,start_date,start_time,end_date,end_time,state,type,branch_code,app_category,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type, rownumber() over(order by id) as rn from comm_log ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from comm_log order by id limit " + startNum
						+ "," + pageLength;
			}
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.findAll()(pageNum:" + pageNum + ",row count "
						+ pageLength + ") spend " + (endTime - startTime)
						+ "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List findByWhere(String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from comm_log where " + where
					+ " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List findByWhere(String where, int pageNum, int pageLength) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from comm_log where " + where
						+ " order by id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from comm_log where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from comm_log					 where " + where
						+ " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,gateway_id,start_date,start_time,end_date,end_time,state,type,branch_code,app_category,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type, rownumber() over(order by id) as rn from comm_log where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			}

			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
			String sql = "select  count(*) from comm_log";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.getTotalRecords() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}

			if (resultSet.next()) {
				totalRecords = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
			String sql = "select  count(*) from comm_log where " + where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogDAO.getTotalRecords() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}

			if (resultSet.next()) {
				totalRecords = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return totalRecords;
	}

	public List getLogByState(String state) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (state != null && state.length() < 2) {
			throw new IllegalArgumentException("state too short" + state);
		}
		if (state != null && state.length() > 2) {
			throw new IllegalArgumentException("state too long" + state);
		}
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from comm_log where state=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, state);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("CommLogDAO.getLogByState() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : state = " + state);
			}

			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List getLogByType(String type) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (type != null && type.length() < 2) {
			throw new IllegalArgumentException("type too short" + type);
		}
		if (type != null && type.length() > 2) {
			throw new IllegalArgumentException("type too long" + type);
		}
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from comm_log where type=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, type);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("CommLogDAO.getLogByType() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : type = " + type);
			}

			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List getLogByBranch_code(String branchCode) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (branchCode != null && branchCode.length() > 10) {
			throw new IllegalArgumentException("branchCode too long"
					+ branchCode);
		}
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from comm_log where branch_code=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, branchCode);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("CommLogDAO.getLogByBranch_code() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : branchCode = " + branchCode);
			}

			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List getLogByApp_category(String appCategory) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (appCategory != null && appCategory.length() > 10) {
			throw new IllegalArgumentException("appCategory too long"
					+ appCategory);
		}
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from comm_log where app_category=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, appCategory);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("CommLogDAO.getLogByApp_category() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : appCategory = " + appCategory);
			}

			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public List getLogByProcessor_id(String processorId) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (processorId != null && processorId.length() > 80) {
			throw new IllegalArgumentException("processorId too long"
					+ processorId);
		}
		ResultSet resultSet = null;
		CommLog returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from comm_log where processor_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, processorId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("CommLogDAO.getLogByProcessor_id() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : processorId = " + processorId);
			}

			while (resultSet.next()) {
				returnDTO = new CommLog();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet
						.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet
						.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet
						.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet
						.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

}
