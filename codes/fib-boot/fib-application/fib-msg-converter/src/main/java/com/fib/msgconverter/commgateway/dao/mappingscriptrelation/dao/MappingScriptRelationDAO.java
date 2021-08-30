package com.fib.msgconverter.commgateway.dao.mappingscriptrelation.dao;

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

public class MappingScriptRelationDAO extends AbstractDAO {

	public MappingScriptRelationDAO() {
		super();
	}

	public MappingScriptRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MappingScriptRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(MappingScriptRelation mappingScriptRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mappingScriptRelation == null) {
			throw new IllegalArgumentException("mappingScriptRelation is NULL!");
		}
		if (mappingScriptRelation.getMappingId() != null
				&& mappingScriptRelation.getMappingId().length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingScriptRelation.getMappingId());
		}
		if (mappingScriptRelation.getMappingId() != null
				&& mappingScriptRelation.getMappingId().length() > 10) {
			throw new IllegalArgumentException("mappingId too long"
					+ mappingScriptRelation.getMappingId());
		}
		if (mappingScriptRelation.getScriptId() != null
				&& mappingScriptRelation.getScriptId().length() < 10) {
			throw new IllegalArgumentException("scriptId too short"
					+ mappingScriptRelation.getScriptId());
		}
		if (mappingScriptRelation.getScriptId() != null
				&& mappingScriptRelation.getScriptId().length() > 10) {
			throw new IllegalArgumentException("scriptId too long"
					+ mappingScriptRelation.getScriptId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into mapping_script_relation(mapping_id,script_id,script_index) values(?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingScriptRelation.getMappingId());
			statment.setString(2, mappingScriptRelation.getScriptId());
			statment.setInt(3, mappingScriptRelation.getScriptIndex());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : mapping_id = \""
						+ mappingScriptRelation.getMappingId()
						+ "\",script_id = \""
						+ mappingScriptRelation.getScriptId()
						+ "\",script_index = "
						+ mappingScriptRelation.getScriptIndex() + " ");
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
			List<MappingScriptRelation> mappingScriptRelationList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (mappingScriptRelationList == null) {
			throw new IllegalArgumentException(
					"mappingScriptRelationList is NULL!");
		}
		for (MappingScriptRelation mappingScriptRelation : mappingScriptRelationList) {
			if (mappingScriptRelation.getMappingId() != null
					&& mappingScriptRelation.getMappingId().length() < 10) {
				throw new IllegalArgumentException("mappingId too short"
						+ mappingScriptRelation.getMappingId());
			}
			if (mappingScriptRelation.getMappingId() != null
					&& mappingScriptRelation.getMappingId().length() > 10) {
				throw new IllegalArgumentException("mappingId too long"
						+ mappingScriptRelation.getMappingId());
			}
			if (mappingScriptRelation.getScriptId() != null
					&& mappingScriptRelation.getScriptId().length() < 10) {
				throw new IllegalArgumentException("scriptId too short"
						+ mappingScriptRelation.getScriptId());
			}
			if (mappingScriptRelation.getScriptId() != null
					&& mappingScriptRelation.getScriptId().length() > 10) {
				throw new IllegalArgumentException("scriptId too long"
						+ mappingScriptRelation.getScriptId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into mapping_script_relation(mapping_id,script_id,script_index) values(?,?,?)";
			statment = conn.prepareStatement(sql);
			for (MappingScriptRelation mappingScriptRelation : mappingScriptRelationList) {
				statment.setString(1, mappingScriptRelation.getMappingId());
				statment.setString(2, mappingScriptRelation.getScriptId());
				statment.setInt(3, mappingScriptRelation.getScriptIndex());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.insertBatch() spend "
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

	public int update(MappingScriptRelation mappingScriptRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mappingScriptRelation == null) {
			throw new IllegalArgumentException("mappingScriptRelation is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingScriptRelation.getMappingId() != null
				&& mappingScriptRelation.getMappingId().length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingScriptRelation.getMappingId());
		}
		if (mappingScriptRelation.getMappingId() != null
				&& mappingScriptRelation.getMappingId().length() > 10) {
			throw new IllegalArgumentException("mappingId too long"
					+ mappingScriptRelation.getMappingId());
		}
		if (mappingScriptRelation.getScriptId() != null
				&& mappingScriptRelation.getScriptId().length() < 10) {
			throw new IllegalArgumentException("scriptId too short"
					+ mappingScriptRelation.getScriptId());
		}
		if (mappingScriptRelation.getScriptId() != null
				&& mappingScriptRelation.getScriptId().length() > 10) {
			throw new IllegalArgumentException("scriptId too long"
					+ mappingScriptRelation.getScriptId());
		}
		try {
			String sql = "UPDATE mapping_script_relation SET mapping_id=?,script_id=?,script_index=? where mapping_id=? and script_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingScriptRelation.getMappingId());
			statment.setString(2, mappingScriptRelation.getScriptId());
			statment.setInt(3, mappingScriptRelation.getScriptIndex());
			statment.setString(4, mappingScriptRelation.getMappingId());
			statment.setString(5, mappingScriptRelation.getScriptId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : mapping_id = \""
						+ mappingScriptRelation.getMappingId()
						+ "\",script_id = \""
						+ mappingScriptRelation.getScriptId()
						+ "\",script_index = "
						+ mappingScriptRelation.getScriptIndex() + " ");
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
		if (!primaryKey.containsKey("mapping_id")) {
			throw new IllegalArgumentException("mapping_id is null ");
		}
		if (!primaryKey.containsKey("script_id")) {
			throw new IllegalArgumentException("script_id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE mapping_script_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where mapping_id=? and script_id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("mapping_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("script_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("script_index".equalsIgnoreCase(tmpKey)) {
					statment.setInt(m++, (Integer) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("mapping_id"));
			statment.setString(m++, (String) primaryKey.get("script_id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug
						.append("MappingScriptRelationDAO.dynamicUpdate() spend "
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

	public int deleteByPK(String mappingId, String scriptId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mappingId == null) {
			throw new IllegalArgumentException("mappingId is NULL!");
		}
		if (scriptId == null) {
			throw new IllegalArgumentException("scriptId is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingId != null && mappingId.length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingId);
		}
		if (mappingId != null && mappingId.length() > 10) {
			throw new IllegalArgumentException("mappingId too long" + mappingId);
		}
		if (scriptId != null && scriptId.length() < 10) {
			throw new IllegalArgumentException("scriptId too short" + scriptId);
		}
		if (scriptId != null && scriptId.length() > 10) {
			throw new IllegalArgumentException("scriptId too long" + scriptId);
		}

		try {
			String sql = "DELETE FROM mapping_script_relation where mapping_id=? and script_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingId);
			statment.setString(2, scriptId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : mapping_id = \""
						+ mappingId + "\",script_id = \"" + scriptId + "\" ");
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

	public MappingScriptRelation selectByPK(String mappingId, String scriptId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingId != null && mappingId.length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingId);
		}
		if (mappingId != null && mappingId.length() > 10) {
			throw new IllegalArgumentException("mappingId too long" + mappingId);
		}
		if (scriptId != null && scriptId.length() < 10) {
			throw new IllegalArgumentException("scriptId too short" + scriptId);
		}
		if (scriptId != null && scriptId.length() > 10) {
			throw new IllegalArgumentException("scriptId too long" + scriptId);
		}
		ResultSet resultSet = null;
		MappingScriptRelation returnDTO = null;

		try {
			String sql = "SELECT * FROM mapping_script_relation where mapping_id=? and script_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingId);
			statment.setString(2, scriptId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : mapping_id = \"" + mappingId
						+ "\",script_id = \"" + scriptId + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new MappingScriptRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setScriptId(resultSet.getString("script_id"));
				returnDTO.setScriptIndex(resultSet.getInt("script_index"));
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
		MappingScriptRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from mapping_script_relation order by mapping_id,script_id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new MappingScriptRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setScriptId(resultSet.getString("script_id"));
				returnDTO.setScriptIndex(resultSet.getInt("script_index"));
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
		MappingScriptRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mapping_script_relation ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by mapping_id,script_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from mapping_script_relation					 ) t1)t2 order by mapping_id,script_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select mapping_id,script_id,script_index, rownumber() over(order by mapping_id,script_id) as rn from mapping_script_relation ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from mapping_script_relation order by mapping_id,script_id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by mapping_id,script_id) as rownumber, mapping_id,script_id,script_index from mapping_script_relation ) A where rownumber > "
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
				debug("MappingScriptRelationDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new MappingScriptRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setScriptId(resultSet.getString("script_id"));
				returnDTO.setScriptIndex(resultSet.getInt("script_index"));
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
		MappingScriptRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from mapping_script_relation where " + where
					+ " order by mapping_id,script_id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new MappingScriptRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setScriptId(resultSet.getString("script_id"));
				returnDTO.setScriptIndex(resultSet.getInt("script_index"));
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
		MappingScriptRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from mapping_script_relation where " + where
						+ " order by mapping_id,script_id limit " + startNum
						+ "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mapping_script_relation where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by mapping_id,script_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from mapping_script_relation					 where " + where
						+ " ) t1)t2 order by mapping_id,script_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select mapping_id,script_id,script_index, rownumber() over(order by mapping_id,script_id) as rn from mapping_script_relation where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by mapping_id,script_id) as rownumber, mapping_id,script_id,script_index from mapping_script_relation  where "
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
				debug("MappingScriptRelationDAO.findByWhere()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new MappingScriptRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setScriptId(resultSet.getString("script_id"));
				returnDTO.setScriptIndex(resultSet.getInt("script_index"));
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
			String sql = "select  count(*) from mapping_script_relation";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from mapping_script_relation where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.getTotalRecords() spend "
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

	public List getAllScriptByMappingId(String mappingId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingId != null && mappingId.length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingId);
		}
		if (mappingId != null && mappingId.length() > 10) {
			throw new IllegalArgumentException("mappingId too long" + mappingId);
		}
		ResultSet resultSet = null;
		MappingScriptRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from mapping_script_relation where mapping_id=? order by script_index";
			statment = conn.prepareStatement(sql);

			statment.setString(1, mappingId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("MappingScriptRelationDAO.getAllScriptByMappingId() spend "
						+ (_endTime - _startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : mappingId = " + mappingId);
			}

			while (resultSet.next()) {
				returnDTO = new MappingScriptRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setScriptId(resultSet.getString("script_id"));
				returnDTO.setScriptIndex(resultSet.getInt("script_index"));
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
