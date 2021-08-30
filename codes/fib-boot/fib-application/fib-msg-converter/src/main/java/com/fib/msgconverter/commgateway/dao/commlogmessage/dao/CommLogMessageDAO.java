package com.fib.msgconverter.commgateway.dao.commlogmessage.dao;

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
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.base.config.DAOConfiguration;

@SuppressWarnings("all")
public class CommLogMessageDAO extends AbstractDAO {

	public CommLogMessageDAO() {
		super();
	}

	public CommLogMessageDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public CommLogMessageDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public void insert(CommLogMessage commLogMessage) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (commLogMessage == null) {
			throw new IllegalArgumentException("commLogMessage is NULL!");
		}
		if (commLogMessage.getLogId() != null
				&& commLogMessage.getLogId().length() > 20) {
			throw new IllegalArgumentException("logId too long"
					+ commLogMessage.getLogId());
		}
		if (commLogMessage.getMessageType() != null
				&& commLogMessage.getMessageType().length() < 2) {
			throw new IllegalArgumentException("messageType too short"
					+ commLogMessage.getMessageType());
		}
		if (commLogMessage.getMessageType() != null
				&& commLogMessage.getMessageType().length() > 2) {
			throw new IllegalArgumentException("messageType too long"
					+ commLogMessage.getMessageType());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into comm_log_message(log_id,message_type,message) values(?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, commLogMessage.getLogId());
			statment.setString(2, commLogMessage.getMessageType());
			statment.setBytes(3, commLogMessage.getMessage());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : log_id = \""
						+ commLogMessage.getLogId() + "\",message_type = \""
						+ commLogMessage.getMessageType() + "\",message = "
						+ CodeUtil.Bytes2FormattedText(commLogMessage.getMessage())+ " ");
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

	public void update(CommLogMessage commLogMessage) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (commLogMessage == null) {
			throw new IllegalArgumentException("commLogMessage is NULL!");
		}

		PreparedStatement statment = null;
		if (commLogMessage.getLogId() != null
				&& commLogMessage.getLogId().length() > 20) {
			throw new IllegalArgumentException("logId too long"
					+ commLogMessage.getLogId());
		}
		if (commLogMessage.getMessageType() != null
				&& commLogMessage.getMessageType().length() < 2) {
			throw new IllegalArgumentException("messageType too short"
					+ commLogMessage.getMessageType());
		}
		if (commLogMessage.getMessageType() != null
				&& commLogMessage.getMessageType().length() > 2) {
			throw new IllegalArgumentException("messageType too long"
					+ commLogMessage.getMessageType());
		}
		try {
			String sql = "UPDATE comm_log_message SET log_id=?,message_type=?,message=? where log_id=? and message_type=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, commLogMessage.getLogId());
			statment.setString(2, commLogMessage.getMessageType());
			statment.setBytes(3, commLogMessage.getMessage());
			statment.setString(4, commLogMessage.getLogId());
			statment.setString(5, commLogMessage.getMessageType());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : log_id = \""
						+ commLogMessage.getLogId() + "\",message_type = \""
						+ commLogMessage.getMessageType() + "\",message = "
						+ CodeUtil.Bytes2FormattedText(commLogMessage.getMessage()) + " ");
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
		if (!primaryKey.containsKey("log_id")) {
			throw new IllegalArgumentException("log_id is null ");
		}
		if (!primaryKey.containsKey("message_type")) {
			throw new IllegalArgumentException("message_type is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE comm_log_message SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where log_id=? and message_type=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("log_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("message_type".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("message".equalsIgnoreCase(tmpKey)) {
					statment.setBytes(m++, (byte[]) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("log_id"));
			statment.setString(m++, (String) primaryKey.get("message_type"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("CommLogMessageDAO.dynamicUpdate() spend "
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

	public void deleteByPK(String logId, String messageType) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (logId == null) {
			throw new IllegalArgumentException("logId is NULL!");
		}
		if (messageType == null) {
			throw new IllegalArgumentException("messageType is NULL!");
		}

		PreparedStatement statment = null;
		if (logId != null && logId.length() > 20) {
			throw new IllegalArgumentException("logId too long" + logId);
		}
		if (messageType != null && messageType.length() < 2) {
			throw new IllegalArgumentException("messageType too short"
					+ messageType);
		}
		if (messageType != null && messageType.length() > 2) {
			throw new IllegalArgumentException("messageType too long"
					+ messageType);
		}

		try {
			String sql = "DELETE FROM comm_log_message where log_id=? and message_type=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, logId);
			statment.setString(2, messageType);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : log_id = \"" + logId
						+ "\",message_type = \"" + messageType + "\" ");
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

	public CommLogMessage selectByExternalSerialNum(String externalSerialNum,
			String messageType) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (null != externalSerialNum && externalSerialNum.length() > 20) {
			throw new IllegalArgumentException("externalSerialNum too long"
					+ externalSerialNum);
		}
		if (null != messageType && messageType.length() < 2) {
			throw new IllegalArgumentException("messageType too short"
					+ messageType);
		}
		if (null != messageType && messageType.length() > 2) {
			throw new IllegalArgumentException("messageType too long"
					+ messageType);
		}
		ResultSet resultSet = null;
		CommLogMessage returnDTO = null;
		try {
			
			String sql = "SELECT * FROM comm_log_message a, comm_log b where a.log_id=b.log_id and b.external_serial_num=? and a.message_type=?";
			statment = conn.prepareStatement(sql);
			statment.setString(1, externalSerialNum);
			statment.setString(2, messageType);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : externalSerialNum = \"" + externalSerialNum
						+ "\",message_type = \"" + messageType + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new CommLogMessage();
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setMessageType(resultSet.getString("message_type"));
				returnDTO.setMessage(resultSet.getBytes("message"));
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

	public CommLogMessage selectByPK(String logId, String messageType) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (logId != null && logId.length() > 20) {
			throw new IllegalArgumentException("logId too long" + logId);
		}
		if (messageType != null && messageType.length() < 2) {
			throw new IllegalArgumentException("messageType too short"
					+ messageType);
		}
		if (messageType != null && messageType.length() > 2) {
			throw new IllegalArgumentException("messageType too long"
					+ messageType);
		}
		ResultSet resultSet = null;
		CommLogMessage returnDTO = null;

		try {
			String sql = "SELECT * FROM comm_log_message where log_id=? and message_type=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, logId);
			statment.setString(2, messageType);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : log_id = \"" + logId
						+ "\",message_type = \"" + messageType + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new CommLogMessage();
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setMessageType(resultSet.getString("message_type"));
				returnDTO.setMessage(resultSet.getBytes("message"));
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
		CommLogMessage returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from comm_log_message order by log_id,message_type";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new CommLogMessage();
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setMessageType(resultSet.getString("message_type"));
				returnDTO.setMessage(resultSet.getBytes("message"));
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
		CommLogMessage returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from comm_log_message ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by log_id,message_type";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from comm_log_message					 ) t1)t2 order by log_id,message_type";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select log_id,message_type,message, rownumber() over(order by log_id,message_type) as rn from comm_log_message ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from comm_log_message order by log_id,message_type limit "
						+ startNum + "," + pageLength;
			}
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new CommLogMessage();
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setMessageType(resultSet.getString("message_type"));
				returnDTO.setMessage(resultSet.getBytes("message"));
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
		CommLogMessage returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from comm_log_message where " + where
					+ " order by log_id,message_type";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new CommLogMessage();
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setMessageType(resultSet.getString("message_type"));
				returnDTO.setMessage(resultSet.getBytes("message"));
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
		CommLogMessage returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from comm_log_message where " + where
						+ " order by log_id,message_type limit " + startNum
						+ "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from comm_log_message where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by log_id,message_type";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from comm_log_message					 where " + where
						+ " ) t1)t2 order by log_id,message_type";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select log_id,message_type,message, rownumber() over(order by log_id,message_type) as rn from comm_log_message where "
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
				debug("CommLogMessageDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new CommLogMessage();
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setMessageType(resultSet.getString("message_type"));
				returnDTO.setMessage(resultSet.getBytes("message"));
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
			String sql = "select  count(*) from comm_log_message";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from comm_log_message where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("CommLogMessageDAO.getTotalRecords() spend "
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

}
