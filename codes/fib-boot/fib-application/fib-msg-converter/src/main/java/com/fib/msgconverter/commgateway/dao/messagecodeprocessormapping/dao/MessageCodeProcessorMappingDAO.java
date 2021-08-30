package com.fib.msgconverter.commgateway.dao.messagecodeprocessormapping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.giantstone.base.config.DAOConfiguration;
import com.giantstone.base.dao.AbstractDAO;
import com.giantstone.common.util.ExceptionUtil;

public class MessageCodeProcessorMappingDAO extends AbstractDAO {

	public MessageCodeProcessorMappingDAO() {
		super();
	}

	public MessageCodeProcessorMappingDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MessageCodeProcessorMappingDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(MessageCodeProcessorMapping messageCodeProcessorMapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (messageCodeProcessorMapping == null) {
			throw new IllegalArgumentException(
					"messageCodeProcessorMapping is NULL!");
		}
		if (messageCodeProcessorMapping.getChannelId() != null
				&& messageCodeProcessorMapping.getChannelId().length() < 10) {
			throw new IllegalArgumentException("channelId too short"
					+ messageCodeProcessorMapping.getChannelId());
		}
		if (messageCodeProcessorMapping.getChannelId() != null
				&& messageCodeProcessorMapping.getChannelId().length() > 10) {
			throw new IllegalArgumentException("channelId too long"
					+ messageCodeProcessorMapping.getChannelId());
		}
		if (messageCodeProcessorMapping.getMessageCode() == null
				|| "".equals(messageCodeProcessorMapping.getMessageCode())) {
			throw new IllegalArgumentException("messageCode is null");
		}
		if (messageCodeProcessorMapping.getMessageCode() != null
				&& messageCodeProcessorMapping.getMessageCode().length() > 255) {
			throw new IllegalArgumentException("messageCode too long"
					+ messageCodeProcessorMapping.getMessageCode());
		}
		if (messageCodeProcessorMapping.getProcessorId() != null
				&& messageCodeProcessorMapping.getProcessorId().length() < 10) {
			throw new IllegalArgumentException("processorId too short"
					+ messageCodeProcessorMapping.getProcessorId());
		}
		if (messageCodeProcessorMapping.getProcessorId() != null
				&& messageCodeProcessorMapping.getProcessorId().length() > 10) {
			throw new IllegalArgumentException("processorId too long"
					+ messageCodeProcessorMapping.getProcessorId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into message_code_processor_mapping(channel_id,message_code,processor_id) values(?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, messageCodeProcessorMapping.getChannelId());
			statment.setString(2, messageCodeProcessorMapping.getMessageCode());
			statment.setString(3, messageCodeProcessorMapping.getProcessorId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : channel_id = \""
						+ messageCodeProcessorMapping.getChannelId()
						+ "\",message_code = \""
						+ messageCodeProcessorMapping.getMessageCode()
						+ "\",processor_id = \""
						+ messageCodeProcessorMapping.getProcessorId() + "\" ");
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

	public int[] insertBatch(
			List<MessageCodeProcessorMapping> messageCodeProcessorMappingList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (messageCodeProcessorMappingList == null) {
			throw new IllegalArgumentException(
					"messageCodeProcessorMappingList is NULL!");
		}
		for (MessageCodeProcessorMapping messageCodeProcessorMapping : messageCodeProcessorMappingList) {
			if (messageCodeProcessorMapping.getChannelId() != null
					&& messageCodeProcessorMapping.getChannelId().length() < 10) {
				throw new IllegalArgumentException("channelId too short"
						+ messageCodeProcessorMapping.getChannelId());
			}
			if (messageCodeProcessorMapping.getChannelId() != null
					&& messageCodeProcessorMapping.getChannelId().length() > 10) {
				throw new IllegalArgumentException("channelId too long"
						+ messageCodeProcessorMapping.getChannelId());
			}
			if (messageCodeProcessorMapping.getMessageCode() == null
					|| "".equals(messageCodeProcessorMapping.getMessageCode())) {
				throw new IllegalArgumentException("messageCode is null");
			}
			if (messageCodeProcessorMapping.getMessageCode() != null
					&& messageCodeProcessorMapping.getMessageCode().length() > 255) {
				throw new IllegalArgumentException("messageCode too long"
						+ messageCodeProcessorMapping.getMessageCode());
			}
			if (messageCodeProcessorMapping.getProcessorId() != null
					&& messageCodeProcessorMapping.getProcessorId().length() < 10) {
				throw new IllegalArgumentException("processorId too short"
						+ messageCodeProcessorMapping.getProcessorId());
			}
			if (messageCodeProcessorMapping.getProcessorId() != null
					&& messageCodeProcessorMapping.getProcessorId().length() > 10) {
				throw new IllegalArgumentException("processorId too long"
						+ messageCodeProcessorMapping.getProcessorId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into message_code_processor_mapping(channel_id,message_code,processor_id) values(?,?,?)";
			statment = conn.prepareStatement(sql);
			for (MessageCodeProcessorMapping messageCodeProcessorMapping : messageCodeProcessorMappingList) {
				statment.setString(1, messageCodeProcessorMapping
						.getChannelId());
				statment.setString(2, messageCodeProcessorMapping
						.getMessageCode());
				statment.setString(3, messageCodeProcessorMapping
						.getProcessorId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.insertBatch() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + ";");
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

	public int update(MessageCodeProcessorMapping messageCodeProcessorMapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (messageCodeProcessorMapping == null) {
			throw new IllegalArgumentException(
					"messageCodeProcessorMapping is NULL!");
		}

		PreparedStatement statment = null;
		if (messageCodeProcessorMapping.getChannelId() != null
				&& messageCodeProcessorMapping.getChannelId().length() < 10) {
			throw new IllegalArgumentException("channelId too short"
					+ messageCodeProcessorMapping.getChannelId());
		}
		if (messageCodeProcessorMapping.getChannelId() != null
				&& messageCodeProcessorMapping.getChannelId().length() > 10) {
			throw new IllegalArgumentException("channelId too long"
					+ messageCodeProcessorMapping.getChannelId());
		}
		if (messageCodeProcessorMapping.getMessageCode() == null
				|| "".equals(messageCodeProcessorMapping.getMessageCode())) {
			throw new IllegalArgumentException("messageCode is null");
		}
		if (messageCodeProcessorMapping.getMessageCode() != null
				&& messageCodeProcessorMapping.getMessageCode().length() > 255) {
			throw new IllegalArgumentException("messageCode too long"
					+ messageCodeProcessorMapping.getMessageCode());
		}
		if (messageCodeProcessorMapping.getProcessorId() != null
				&& messageCodeProcessorMapping.getProcessorId().length() < 10) {
			throw new IllegalArgumentException("processorId too short"
					+ messageCodeProcessorMapping.getProcessorId());
		}
		if (messageCodeProcessorMapping.getProcessorId() != null
				&& messageCodeProcessorMapping.getProcessorId().length() > 10) {
			throw new IllegalArgumentException("processorId too long"
					+ messageCodeProcessorMapping.getProcessorId());
		}
		try {
			String sql = "UPDATE message_code_processor_mapping SET channel_id=?,message_code=?,processor_id=? where channel_id=? and message_code=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, messageCodeProcessorMapping.getChannelId());
			statment.setString(2, messageCodeProcessorMapping.getMessageCode());
			statment.setString(3, messageCodeProcessorMapping.getProcessorId());
			statment.setString(4, messageCodeProcessorMapping.getChannelId());
			statment.setString(5, messageCodeProcessorMapping.getMessageCode());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : channel_id = \""
						+ messageCodeProcessorMapping.getChannelId()
						+ "\",message_code = \""
						+ messageCodeProcessorMapping.getMessageCode()
						+ "\",processor_id = \""
						+ messageCodeProcessorMapping.getProcessorId() + "\" ");
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

	public int dynamicUpdate(Map primaryKey, Map updateFields) {
		Connection conn = getConnection();
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
		if (!primaryKey.containsKey("channel_id")) {
			throw new IllegalArgumentException("channel_id is null ");
		}
		if (!primaryKey.containsKey("message_code")) {
			throw new IllegalArgumentException("message_code is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE message_code_processor_mapping SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where channel_id=? and message_code=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("channel_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("message_code".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("processor_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("channel_id"));
			statment.setString(m++, (String) primaryKey.get("message_code"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug
						.append("MessageCodeProcessorMappingDAO.dynamicUpdate() spend "
								+ (endTime - startTime)
								+ "ms. retFlag = "
								+ retFlag
								+ " SQL:"
								+ sql.toString()
								+ "; parameters : ");
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

	public int deleteByPK(String channelId, String messageCode) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (channelId == null) {
			throw new IllegalArgumentException("channelId is NULL!");
		}
		if (messageCode == null) {
			throw new IllegalArgumentException("messageCode is NULL!");
		}

		PreparedStatement statment = null;
		if (channelId != null && channelId.length() < 10) {
			throw new IllegalArgumentException("channelId too short"
					+ channelId);
		}
		if (channelId != null && channelId.length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelId);
		}
		if (messageCode == null || "".equals(messageCode)) {
			throw new IllegalArgumentException("messageCode is null");
		}
		if (messageCode != null && messageCode.length() > 255) {
			throw new IllegalArgumentException("messageCode too long"
					+ messageCode);
		}

		try {
			String sql = "DELETE FROM message_code_processor_mapping where channel_id=? and message_code=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, channelId);
			statment.setString(2, messageCode);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : channel_id = \""
						+ channelId + "\",message_code = \"" + messageCode
						+ "\" ");
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

	public MessageCodeProcessorMapping selectByPK(String channelId,
			String messageCode) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (channelId != null && channelId.length() < 10) {
			throw new IllegalArgumentException("channelId too short"
					+ channelId);
		}
		if (channelId != null && channelId.length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelId);
		}
		if (messageCode == null || "".equals(messageCode)) {
			throw new IllegalArgumentException("messageCode is null");
		}
		if (messageCode != null && messageCode.length() > 255) {
			throw new IllegalArgumentException("messageCode too long"
					+ messageCode);
		}
		ResultSet resultSet = null;
		MessageCodeProcessorMapping returnDTO = null;

		try {
			String sql = "SELECT * FROM message_code_processor_mapping where channel_id=? and message_code=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, channelId);
			statment.setString(2, messageCode);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : channel_id = \"" + channelId
						+ "\",message_code = \"" + messageCode + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new MessageCodeProcessorMapping();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setMessageCode(resultSet.getString("message_code"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
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

	public List findAll() {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MessageCodeProcessorMapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from message_code_processor_mapping order by channel_id,message_code";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new MessageCodeProcessorMapping();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setMessageCode(resultSet.getString("message_code"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
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

	public List findAll(int pageNum, int pageLength) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MessageCodeProcessorMapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from message_code_processor_mapping ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by channel_id,message_code";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from message_code_processor_mapping					 ) t1)t2 order by channel_id,message_code";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select channel_id,message_code,processor_id, rownumber() over(order by channel_id,message_code) as rn from message_code_processor_mapping ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from message_code_processor_mapping order by channel_id,message_code limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by channel_id,message_code) as rownumber, channel_id,message_code,processor_id from message_code_processor_mapping ) A where rownumber > "
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
				debug("MessageCodeProcessorMappingDAO.findAll()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new MessageCodeProcessorMapping();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setMessageCode(resultSet.getString("message_code"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
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

	public List findByWhere(String where) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MessageCodeProcessorMapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from message_code_processor_mapping where "
					+ where + " order by channel_id,message_code";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new MessageCodeProcessorMapping();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setMessageCode(resultSet.getString("message_code"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
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

	public List findByWhere(String where, int pageNum, int pageLength) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MessageCodeProcessorMapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from message_code_processor_mapping where "
						+ where + " order by channel_id,message_code limit "
						+ startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from message_code_processor_mapping where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by channel_id,message_code";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from message_code_processor_mapping					 where "
						+ where + " ) t1)t2 order by channel_id,message_code";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select channel_id,message_code,processor_id, rownumber() over(order by channel_id,message_code) as rn from message_code_processor_mapping where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by channel_id,message_code) as rownumber, channel_id,message_code,processor_id from message_code_processor_mapping  where "
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
				debug("MessageCodeProcessorMappingDAO.findByWhere()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new MessageCodeProcessorMapping();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setMessageCode(resultSet.getString("message_code"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
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
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		long totalRecords = 0;

		try {
			String sql = "select  count(*) from message_code_processor_mapping";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.getTotalRecords() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
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
		Connection conn = getConnection();
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
			String sql = "select  count(*) from message_code_processor_mapping where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.getTotalRecords() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
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

	public List<MessageCodeProcessorMapping> getAllMsgTypProcMapping4Channel(String channelId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (channelId != null && channelId.length() < 10) {
			throw new IllegalArgumentException("channelId too short"
					+ channelId);
		}
		if (channelId != null && channelId.length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelId);
		}
		ResultSet resultSet = null;
		MessageCodeProcessorMapping returnDTO = null;
		List<MessageCodeProcessorMapping> list = new ArrayList<>();
		try {
			String sql = "select * from message_code_processor_mapping where channel_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, channelId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("MessageCodeProcessorMappingDAO.getAllMsgTypProcMapping4Channel() spend "
						+ (_endTime - _startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : channelId = " + channelId);
			}

			while (resultSet.next()) {
				returnDTO = new MessageCodeProcessorMapping();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setMessageCode(resultSet.getString("message_code"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
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
