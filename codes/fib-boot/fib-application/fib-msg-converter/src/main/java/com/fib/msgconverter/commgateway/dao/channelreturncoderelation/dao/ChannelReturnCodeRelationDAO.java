package com.fib.msgconverter.commgateway.dao.channelreturncoderelation.dao;

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

public class ChannelReturnCodeRelationDAO extends AbstractDAO {

	public ChannelReturnCodeRelationDAO() {
		super();
	}

	public ChannelReturnCodeRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ChannelReturnCodeRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(ChannelReturnCodeRelation channelReturnCodeRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (channelReturnCodeRelation == null) {
			throw new IllegalArgumentException("channelReturnCodeRelation is NULL!");
		}
		if (channelReturnCodeRelation.getChannelId() != null
				&& channelReturnCodeRelation.getChannelId().length() < 10) {
			throw new IllegalArgumentException("channelId too short" + channelReturnCodeRelation.getChannelId());
		}
		if (channelReturnCodeRelation.getChannelId() != null
				&& channelReturnCodeRelation.getChannelId().length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelReturnCodeRelation.getChannelId());
		}
		if (channelReturnCodeRelation.getReturnCodeId() != null
				&& channelReturnCodeRelation.getReturnCodeId().length() < 10) {
			throw new IllegalArgumentException("returnCodeId too short" + channelReturnCodeRelation.getReturnCodeId());
		}
		if (channelReturnCodeRelation.getReturnCodeId() != null
				&& channelReturnCodeRelation.getReturnCodeId().length() > 10) {
			throw new IllegalArgumentException("returnCodeId too long" + channelReturnCodeRelation.getReturnCodeId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into channel_return_code_relation(channel_id,return_code_id) values(?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, channelReturnCodeRelation.getChannelId());
			statment.setString(2, channelReturnCodeRelation.getReturnCodeId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.insert() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + "; parameters : channel_id = \""
						+ channelReturnCodeRelation.getChannelId() + "\",return_code_id = \""
						+ channelReturnCodeRelation.getReturnCodeId() + "\" ");
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

	public int[] insertBatch(List<ChannelReturnCodeRelation> channelReturnCodeRelationList) {
		// 获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (channelReturnCodeRelationList == null) {
			throw new IllegalArgumentException("channelReturnCodeRelationList is NULL!");
		}
		for (ChannelReturnCodeRelation channelReturnCodeRelation : channelReturnCodeRelationList) {
			if (channelReturnCodeRelation.getChannelId() != null
					&& channelReturnCodeRelation.getChannelId().length() < 10) {
				throw new IllegalArgumentException("channelId too short" + channelReturnCodeRelation.getChannelId());
			}
			if (channelReturnCodeRelation.getChannelId() != null
					&& channelReturnCodeRelation.getChannelId().length() > 10) {
				throw new IllegalArgumentException("channelId too long" + channelReturnCodeRelation.getChannelId());
			}
			if (channelReturnCodeRelation.getReturnCodeId() != null
					&& channelReturnCodeRelation.getReturnCodeId().length() < 10) {
				throw new IllegalArgumentException(
						"returnCodeId too short" + channelReturnCodeRelation.getReturnCodeId());
			}
			if (channelReturnCodeRelation.getReturnCodeId() != null
					&& channelReturnCodeRelation.getReturnCodeId().length() > 10) {
				throw new IllegalArgumentException(
						"returnCodeId too long" + channelReturnCodeRelation.getReturnCodeId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into channel_return_code_relation(channel_id,return_code_id) values(?,?)";
			statment = conn.prepareStatement(sql);
			for (ChannelReturnCodeRelation channelReturnCodeRelation : channelReturnCodeRelationList) {
				statment.setString(1, channelReturnCodeRelation.getChannelId());
				statment.setString(2, channelReturnCodeRelation.getReturnCodeId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.insertBatch() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + ";");
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

	public int update(ChannelReturnCodeRelation channelReturnCodeRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (channelReturnCodeRelation == null) {
			throw new IllegalArgumentException("channelReturnCodeRelation is NULL!");
		}

		PreparedStatement statment = null;
		if (channelReturnCodeRelation.getChannelId() != null
				&& channelReturnCodeRelation.getChannelId().length() < 10) {
			throw new IllegalArgumentException("channelId too short" + channelReturnCodeRelation.getChannelId());
		}
		if (channelReturnCodeRelation.getChannelId() != null
				&& channelReturnCodeRelation.getChannelId().length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelReturnCodeRelation.getChannelId());
		}
		if (channelReturnCodeRelation.getReturnCodeId() != null
				&& channelReturnCodeRelation.getReturnCodeId().length() < 10) {
			throw new IllegalArgumentException("returnCodeId too short" + channelReturnCodeRelation.getReturnCodeId());
		}
		if (channelReturnCodeRelation.getReturnCodeId() != null
				&& channelReturnCodeRelation.getReturnCodeId().length() > 10) {
			throw new IllegalArgumentException("returnCodeId too long" + channelReturnCodeRelation.getReturnCodeId());
		}
		try {
			String sql = "UPDATE channel_return_code_relation SET channel_id=?,return_code_id=? where channel_id=? and return_code_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, channelReturnCodeRelation.getChannelId());
			statment.setString(2, channelReturnCodeRelation.getReturnCodeId());
			statment.setString(3, channelReturnCodeRelation.getChannelId());
			statment.setString(4, channelReturnCodeRelation.getReturnCodeId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.update() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + "; parameters : channel_id = \""
						+ channelReturnCodeRelation.getChannelId() + "\",return_code_id = \""
						+ channelReturnCodeRelation.getReturnCodeId() + "\" ");
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
		if (!primaryKey.containsKey("channel_id")) {
			throw new IllegalArgumentException("channel_id is null ");
		}
		if (!primaryKey.containsKey("return_code_id")) {
			throw new IllegalArgumentException("return_code_id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE channel_return_code_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where channel_id=? and return_code_id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("channel_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("return_code_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("channel_id"));
			statment.setString(m++, (String) primaryKey.get("return_code_id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("ChannelReturnCodeRelationDAO.dynamicUpdate() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql.toString() + "; parameters : ");
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

	public int deleteByPK(String channelId, String returnCodeId) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (channelId == null) {
			throw new IllegalArgumentException("channelId is NULL!");
		}
		if (returnCodeId == null) {
			throw new IllegalArgumentException("returnCodeId is NULL!");
		}

		PreparedStatement statment = null;
		if (channelId != null && channelId.length() < 10) {
			throw new IllegalArgumentException("channelId too short" + channelId);
		}
		if (channelId != null && channelId.length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelId);
		}
		if (returnCodeId != null && returnCodeId.length() < 10) {
			throw new IllegalArgumentException("returnCodeId too short" + returnCodeId);
		}
		if (returnCodeId != null && returnCodeId.length() > 10) {
			throw new IllegalArgumentException("returnCodeId too long" + returnCodeId);
		}

		try {
			String sql = "DELETE FROM channel_return_code_relation where channel_id=? and return_code_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, channelId);
			statment.setString(2, returnCodeId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.deleteByPK() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + "; parameters : channel_id = \"" + channelId
						+ "\",return_code_id = \"" + returnCodeId + "\" ");
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

	public ChannelReturnCodeRelation selectByPK(String channelId, String returnCodeId) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (channelId != null && channelId.length() < 10) {
			throw new IllegalArgumentException("channelId too short" + channelId);
		}
		if (channelId != null && channelId.length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelId);
		}
		if (returnCodeId != null && returnCodeId.length() < 10) {
			throw new IllegalArgumentException("returnCodeId too short" + returnCodeId);
		}
		if (returnCodeId != null && returnCodeId.length() > 10) {
			throw new IllegalArgumentException("returnCodeId too long" + returnCodeId);
		}
		ResultSet resultSet = null;
		ChannelReturnCodeRelation returnDTO = null;

		try {
			String sql = "SELECT * FROM channel_return_code_relation where channel_id=? and return_code_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, channelId);
			statment.setString(2, returnCodeId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.selectByPK() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : channel_id = \"" + channelId + "\",return_code_id = \"" + returnCodeId
						+ "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new ChannelReturnCodeRelation();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setReturnCodeId(resultSet.getString("return_code_id"));
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
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ChannelReturnCodeRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from channel_return_code_relation order by channel_id,return_code_id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.findAll() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; ");
			}
			while (resultSet.next()) {
				returnDTO = new ChannelReturnCodeRelation();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setReturnCodeId(resultSet.getString("return_code_id"));
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
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ChannelReturnCodeRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from channel_return_code_relation ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by channel_id,return_code_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum + " * from (select Top " + endNum
						+ " * from channel_return_code_relation					 ) t1)t2 order by channel_id,return_code_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select channel_id,return_code_id, rownumber() over(order by channel_id,return_code_id) as rn from channel_return_code_relation ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from channel_return_code_relation order by channel_id,return_code_id limit " + startNum
						+ "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select top " + pageLength
						+ " * from(select row_number() over (order by channel_id,return_code_id) as rownumber, channel_id,return_code_id from channel_return_code_relation ) A where rownumber > "
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
				debug("ChannelReturnCodeRelationDAO.findAll()(pageNum:" + pageNum + ",row count " + pageLength
						+ ") spend " + (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new ChannelReturnCodeRelation();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setReturnCodeId(resultSet.getString("return_code_id"));
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
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ChannelReturnCodeRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from channel_return_code_relation where " + where
					+ " order by channel_id,return_code_id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.findByWhere()() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new ChannelReturnCodeRelation();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setReturnCodeId(resultSet.getString("return_code_id"));
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
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ChannelReturnCodeRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from channel_return_code_relation where " + where
						+ " order by channel_id,return_code_id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from channel_return_code_relation where  "
						+ where + " ) A where rownum <= " + endNum + " ) where RN >=" + startNum
						+ " order by channel_id,return_code_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum + " * from (select Top " + endNum
						+ " * from channel_return_code_relation					 where " + where
						+ " ) t1)t2 order by channel_id,return_code_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select channel_id,return_code_id, rownumber() over(order by channel_id,return_code_id) as rn from channel_return_code_relation where "
						+ where + " ) as a1 where a1.rn between " + startNum + " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select top " + pageLength
						+ " * from(select row_number() over (order by channel_id,return_code_id) as rownumber, channel_id,return_code_id from channel_return_code_relation  where "
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
				debug("ChannelReturnCodeRelationDAO.findByWhere()(pageNum:" + pageNum + ",row count " + pageLength
						+ ") spend " + (endTime - startTime) + "ms. SQL:" + sql + "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new ChannelReturnCodeRelation();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setReturnCodeId(resultSet.getString("return_code_id"));
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
			String sql = "select  count(*) from channel_return_code_relation";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.getTotalRecords() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; ");
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
			String sql = "select  count(*) from channel_return_code_relation where " + where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.getTotalRecords() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; ");
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

	public List<ChannelReturnCodeRelation> getAllReturnCode4Channel(String channelId) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (channelId != null && channelId.length() < 10) {
			throw new IllegalArgumentException("channelId too short" + channelId);
		}
		if (channelId != null && channelId.length() > 10) {
			throw new IllegalArgumentException("channelId too long" + channelId);
		}
		ResultSet resultSet = null;
		ChannelReturnCodeRelation returnDTO = null;
		List<ChannelReturnCodeRelation> list = new ArrayList<>();
		try {
			String sql = "select * from channel_return_code_relation where channel_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, channelId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("ChannelReturnCodeRelationDAO.getAllReturnCode4Channel() spend " + (_endTime - _startTime)
						+ "ms. SQL:" + sql + "; parameter : channelId = " + channelId);
			}

			while (resultSet.next()) {
				returnDTO = new ChannelReturnCodeRelation();
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setReturnCodeId(resultSet.getString("return_code_id"));
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