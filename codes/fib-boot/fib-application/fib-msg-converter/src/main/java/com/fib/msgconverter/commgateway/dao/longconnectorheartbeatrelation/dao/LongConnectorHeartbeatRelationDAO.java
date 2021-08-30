package com.fib.msgconverter.commgateway.dao.longconnectorheartbeatrelation.dao;

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

public class LongConnectorHeartbeatRelationDAO extends AbstractDAO {

	public LongConnectorHeartbeatRelationDAO() {
		super();
	}

	public LongConnectorHeartbeatRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public LongConnectorHeartbeatRelationDAO(boolean inTransaction,
			Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(
			LongConnectorHeartbeatRelation longConnectorHeartbeatRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnectorHeartbeatRelation == null) {
			throw new IllegalArgumentException(
					"longConnectorHeartbeatRelation is NULL!");
		}
		if (longConnectorHeartbeatRelation.getLongConnectorId() != null
				&& longConnectorHeartbeatRelation.getLongConnectorId().length() < 10) {
			throw new IllegalArgumentException("longConnectorId too short"
					+ longConnectorHeartbeatRelation.getLongConnectorId());
		}
		if (longConnectorHeartbeatRelation.getLongConnectorId() != null
				&& longConnectorHeartbeatRelation.getLongConnectorId().length() > 10) {
			throw new IllegalArgumentException("longConnectorId too long"
					+ longConnectorHeartbeatRelation.getLongConnectorId());
		}
		if (longConnectorHeartbeatRelation.getHeartbeatId() != null
				&& longConnectorHeartbeatRelation.getHeartbeatId().length() < 10) {
			throw new IllegalArgumentException("heartbeatId too short"
					+ longConnectorHeartbeatRelation.getHeartbeatId());
		}
		if (longConnectorHeartbeatRelation.getHeartbeatId() != null
				&& longConnectorHeartbeatRelation.getHeartbeatId().length() > 10) {
			throw new IllegalArgumentException("heartbeatId too long"
					+ longConnectorHeartbeatRelation.getHeartbeatId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into long_connector_heartbeat_relation(long_connector_id,heartbeat_id) values(?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, longConnectorHeartbeatRelation
					.getLongConnectorId());
			statment.setString(2, longConnectorHeartbeatRelation
					.getHeartbeatId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql
						+ "; parameters : long_connector_id = \""
						+ longConnectorHeartbeatRelation.getLongConnectorId()
						+ "\",heartbeat_id = \""
						+ longConnectorHeartbeatRelation.getHeartbeatId()
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

	public int[] insertBatch(
			List<LongConnectorHeartbeatRelation> longConnectorHeartbeatRelationList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (longConnectorHeartbeatRelationList == null) {
			throw new IllegalArgumentException(
					"longConnectorHeartbeatRelationList is NULL!");
		}
		for (LongConnectorHeartbeatRelation longConnectorHeartbeatRelation : longConnectorHeartbeatRelationList) {
			if (longConnectorHeartbeatRelation.getLongConnectorId() != null
					&& longConnectorHeartbeatRelation.getLongConnectorId()
							.length() < 10) {
				throw new IllegalArgumentException("longConnectorId too short"
						+ longConnectorHeartbeatRelation.getLongConnectorId());
			}
			if (longConnectorHeartbeatRelation.getLongConnectorId() != null
					&& longConnectorHeartbeatRelation.getLongConnectorId()
							.length() > 10) {
				throw new IllegalArgumentException("longConnectorId too long"
						+ longConnectorHeartbeatRelation.getLongConnectorId());
			}
			if (longConnectorHeartbeatRelation.getHeartbeatId() != null
					&& longConnectorHeartbeatRelation.getHeartbeatId().length() < 10) {
				throw new IllegalArgumentException("heartbeatId too short"
						+ longConnectorHeartbeatRelation.getHeartbeatId());
			}
			if (longConnectorHeartbeatRelation.getHeartbeatId() != null
					&& longConnectorHeartbeatRelation.getHeartbeatId().length() > 10) {
				throw new IllegalArgumentException("heartbeatId too long"
						+ longConnectorHeartbeatRelation.getHeartbeatId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into long_connector_heartbeat_relation(long_connector_id,heartbeat_id) values(?,?)";
			statment = conn.prepareStatement(sql);
			for (LongConnectorHeartbeatRelation longConnectorHeartbeatRelation : longConnectorHeartbeatRelationList) {
				statment.setString(1, longConnectorHeartbeatRelation
						.getLongConnectorId());
				statment.setString(2, longConnectorHeartbeatRelation
						.getHeartbeatId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.insertBatch() spend "
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

	public int update(
			LongConnectorHeartbeatRelation longConnectorHeartbeatRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnectorHeartbeatRelation == null) {
			throw new IllegalArgumentException(
					"longConnectorHeartbeatRelation is NULL!");
		}

		PreparedStatement statment = null;
		if (longConnectorHeartbeatRelation.getLongConnectorId() != null
				&& longConnectorHeartbeatRelation.getLongConnectorId().length() < 10) {
			throw new IllegalArgumentException("longConnectorId too short"
					+ longConnectorHeartbeatRelation.getLongConnectorId());
		}
		if (longConnectorHeartbeatRelation.getLongConnectorId() != null
				&& longConnectorHeartbeatRelation.getLongConnectorId().length() > 10) {
			throw new IllegalArgumentException("longConnectorId too long"
					+ longConnectorHeartbeatRelation.getLongConnectorId());
		}
		if (longConnectorHeartbeatRelation.getHeartbeatId() != null
				&& longConnectorHeartbeatRelation.getHeartbeatId().length() < 10) {
			throw new IllegalArgumentException("heartbeatId too short"
					+ longConnectorHeartbeatRelation.getHeartbeatId());
		}
		if (longConnectorHeartbeatRelation.getHeartbeatId() != null
				&& longConnectorHeartbeatRelation.getHeartbeatId().length() > 10) {
			throw new IllegalArgumentException("heartbeatId too long"
					+ longConnectorHeartbeatRelation.getHeartbeatId());
		}
		try {
			String sql = "UPDATE long_connector_heartbeat_relation SET long_connector_id=?,heartbeat_id=? where long_connector_id=? and heartbeat_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, longConnectorHeartbeatRelation
					.getLongConnectorId());
			statment.setString(2, longConnectorHeartbeatRelation
					.getHeartbeatId());
			statment.setString(3, longConnectorHeartbeatRelation
					.getLongConnectorId());
			statment.setString(4, longConnectorHeartbeatRelation
					.getHeartbeatId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql
						+ "; parameters : long_connector_id = \""
						+ longConnectorHeartbeatRelation.getLongConnectorId()
						+ "\",heartbeat_id = \""
						+ longConnectorHeartbeatRelation.getHeartbeatId()
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
		if (!primaryKey.containsKey("long_connector_id")) {
			throw new IllegalArgumentException("long_connector_id is null ");
		}
		if (!primaryKey.containsKey("heartbeat_id")) {
			throw new IllegalArgumentException("heartbeat_id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE long_connector_heartbeat_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where long_connector_id=? and heartbeat_id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("long_connector_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("heartbeat_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey
					.get("long_connector_id"));
			statment.setString(m++, (String) primaryKey.get("heartbeat_id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug
						.append("LongConnectorHeartbeatRelationDAO.dynamicUpdate() spend "
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

	public int deleteByPK(String longConnectorId, String heartbeatId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnectorId == null) {
			throw new IllegalArgumentException("longConnectorId is NULL!");
		}
		if (heartbeatId == null) {
			throw new IllegalArgumentException("heartbeatId is NULL!");
		}

		PreparedStatement statment = null;
		if (longConnectorId != null && longConnectorId.length() < 10) {
			throw new IllegalArgumentException("longConnectorId too short"
					+ longConnectorId);
		}
		if (longConnectorId != null && longConnectorId.length() > 10) {
			throw new IllegalArgumentException("longConnectorId too long"
					+ longConnectorId);
		}
		if (heartbeatId != null && heartbeatId.length() < 10) {
			throw new IllegalArgumentException("heartbeatId too short"
					+ heartbeatId);
		}
		if (heartbeatId != null && heartbeatId.length() > 10) {
			throw new IllegalArgumentException("heartbeatId too long"
					+ heartbeatId);
		}

		try {
			String sql = "DELETE FROM long_connector_heartbeat_relation where long_connector_id=? and heartbeat_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, longConnectorId);
			statment.setString(2, heartbeatId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql
						+ "; parameters : long_connector_id = \""
						+ longConnectorId + "\",heartbeat_id = \""
						+ heartbeatId + "\" ");
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

	public LongConnectorHeartbeatRelation selectByPK(String longConnectorId,
			String heartbeatId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (longConnectorId != null && longConnectorId.length() < 10) {
			throw new IllegalArgumentException("longConnectorId too short"
					+ longConnectorId);
		}
		if (longConnectorId != null && longConnectorId.length() > 10) {
			throw new IllegalArgumentException("longConnectorId too long"
					+ longConnectorId);
		}
		if (heartbeatId != null && heartbeatId.length() < 10) {
			throw new IllegalArgumentException("heartbeatId too short"
					+ heartbeatId);
		}
		if (heartbeatId != null && heartbeatId.length() > 10) {
			throw new IllegalArgumentException("heartbeatId too long"
					+ heartbeatId);
		}
		ResultSet resultSet = null;
		LongConnectorHeartbeatRelation returnDTO = null;

		try {
			String sql = "SELECT * FROM long_connector_heartbeat_relation where long_connector_id=? and heartbeat_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, longConnectorId);
			statment.setString(2, heartbeatId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : long_connector_id = \""
						+ longConnectorId + "\",heartbeat_id = \""
						+ heartbeatId + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new LongConnectorHeartbeatRelation();
				returnDTO.setLongConnectorId(resultSet
						.getString("long_connector_id"));
				returnDTO.setHeartbeatId(resultSet.getString("heartbeat_id"));
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
		LongConnectorHeartbeatRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from long_connector_heartbeat_relation order by long_connector_id,heartbeat_id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorHeartbeatRelation();
				returnDTO.setLongConnectorId(resultSet
						.getString("long_connector_id"));
				returnDTO.setHeartbeatId(resultSet.getString("heartbeat_id"));
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
		LongConnectorHeartbeatRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_connector_heartbeat_relation ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by long_connector_id,heartbeat_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from long_connector_heartbeat_relation					 ) t1)t2 order by long_connector_id,heartbeat_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select long_connector_id,heartbeat_id, rownumber() over(order by long_connector_id,heartbeat_id) as rn from long_connector_heartbeat_relation ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from long_connector_heartbeat_relation order by long_connector_id,heartbeat_id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by long_connector_id,heartbeat_id) as rownumber, long_connector_id,heartbeat_id from long_connector_heartbeat_relation ) A where rownumber > "
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
				debug("LongConnectorHeartbeatRelationDAO.findAll()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorHeartbeatRelation();
				returnDTO.setLongConnectorId(resultSet
						.getString("long_connector_id"));
				returnDTO.setHeartbeatId(resultSet.getString("heartbeat_id"));
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
		LongConnectorHeartbeatRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from long_connector_heartbeat_relation where "
					+ where + " order by long_connector_id,heartbeat_id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.findByWhere()() spend "
						+ (endTime - startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorHeartbeatRelation();
				returnDTO.setLongConnectorId(resultSet
						.getString("long_connector_id"));
				returnDTO.setHeartbeatId(resultSet.getString("heartbeat_id"));
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
		LongConnectorHeartbeatRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from long_connector_heartbeat_relation where "
						+ where
						+ " order by long_connector_id,heartbeat_id limit "
						+ startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_connector_heartbeat_relation where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by long_connector_id,heartbeat_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from long_connector_heartbeat_relation					 where "
						+ where
						+ " ) t1)t2 order by long_connector_id,heartbeat_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select long_connector_id,heartbeat_id, rownumber() over(order by long_connector_id,heartbeat_id) as rn from long_connector_heartbeat_relation where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by long_connector_id,heartbeat_id) as rownumber, long_connector_id,heartbeat_id from long_connector_heartbeat_relation  where "
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
				debug("LongConnectorHeartbeatRelationDAO.findByWhere()(pageNum:"
						+ pageNum
						+ ",row count "
						+ pageLength
						+ ") spend "
						+ (endTime - startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorHeartbeatRelation();
				returnDTO.setLongConnectorId(resultSet
						.getString("long_connector_id"));
				returnDTO.setHeartbeatId(resultSet.getString("heartbeat_id"));
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
			String sql = "select  count(*) from long_connector_heartbeat_relation";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from long_connector_heartbeat_relation where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.getTotalRecords() spend "
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

	public List getAllHeartbeat4LongConnector(String longConnectorId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (longConnectorId != null && longConnectorId.length() < 10) {
			throw new IllegalArgumentException("longConnectorId too short"
					+ longConnectorId);
		}
		if (longConnectorId != null && longConnectorId.length() > 10) {
			throw new IllegalArgumentException("longConnectorId too long"
					+ longConnectorId);
		}
		ResultSet resultSet = null;
		LongConnectorHeartbeatRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from long_connector_heartbeat_relation where long_connector_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, longConnectorId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("LongConnectorHeartbeatRelationDAO.getAllHeartbeat4LongConnector() spend "
						+ (_endTime - _startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : longConnectorId = " + longConnectorId);
			}

			while (resultSet.next()) {
				returnDTO = new LongConnectorHeartbeatRelation();
				returnDTO.setLongConnectorId(resultSet
						.getString("long_connector_id"));
				returnDTO.setHeartbeatId(resultSet.getString("heartbeat_id"));
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
