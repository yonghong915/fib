package com.fib.msgconverter.commgateway.dao.mappingfieldrelation.dao;

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

public class MappingFieldRelationDAO extends AbstractDAO {

	public MappingFieldRelationDAO() {
		super();
	}

	public MappingFieldRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MappingFieldRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(MappingFieldRelation mappingFieldRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mappingFieldRelation == null) {
			throw new IllegalArgumentException("mappingFieldRelation is NULL!");
		}
		if (mappingFieldRelation.getMappingId() != null
				&& mappingFieldRelation.getMappingId().length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingFieldRelation.getMappingId());
		}
		if (mappingFieldRelation.getMappingId() != null
				&& mappingFieldRelation.getMappingId().length() > 10) {
			throw new IllegalArgumentException("mappingId too long"
					+ mappingFieldRelation.getMappingId());
		}
		if (mappingFieldRelation.getFieldMappingRuleId() != null
				&& mappingFieldRelation.getFieldMappingRuleId().length() < 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too short"
					+ mappingFieldRelation.getFieldMappingRuleId());
		}
		if (mappingFieldRelation.getFieldMappingRuleId() != null
				&& mappingFieldRelation.getFieldMappingRuleId().length() > 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too long"
					+ mappingFieldRelation.getFieldMappingRuleId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into mapping_field_relation(mapping_id,field_mapping_rule_id) values(?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingFieldRelation.getMappingId());
			statment.setString(2, mappingFieldRelation.getFieldMappingRuleId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : mapping_id = \""
						+ mappingFieldRelation.getMappingId()
						+ "\",field_mapping_rule_id = \""
						+ mappingFieldRelation.getFieldMappingRuleId() + "\" ");
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

	public int[] insertBatch(List<MappingFieldRelation> mappingFieldRelationList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (mappingFieldRelationList == null) {
			throw new IllegalArgumentException(
					"mappingFieldRelationList is NULL!");
		}
		for (MappingFieldRelation mappingFieldRelation : mappingFieldRelationList) {
			if (mappingFieldRelation.getMappingId() != null
					&& mappingFieldRelation.getMappingId().length() < 10) {
				throw new IllegalArgumentException("mappingId too short"
						+ mappingFieldRelation.getMappingId());
			}
			if (mappingFieldRelation.getMappingId() != null
					&& mappingFieldRelation.getMappingId().length() > 10) {
				throw new IllegalArgumentException("mappingId too long"
						+ mappingFieldRelation.getMappingId());
			}
			if (mappingFieldRelation.getFieldMappingRuleId() != null
					&& mappingFieldRelation.getFieldMappingRuleId().length() < 10) {
				throw new IllegalArgumentException(
						"fieldMappingRuleId too short"
								+ mappingFieldRelation.getFieldMappingRuleId());
			}
			if (mappingFieldRelation.getFieldMappingRuleId() != null
					&& mappingFieldRelation.getFieldMappingRuleId().length() > 10) {
				throw new IllegalArgumentException(
						"fieldMappingRuleId too long"
								+ mappingFieldRelation.getFieldMappingRuleId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into mapping_field_relation(mapping_id,field_mapping_rule_id) values(?,?)";
			statment = conn.prepareStatement(sql);
			for (MappingFieldRelation mappingFieldRelation : mappingFieldRelationList) {
				statment.setString(1, mappingFieldRelation.getMappingId());
				statment.setString(2, mappingFieldRelation
						.getFieldMappingRuleId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.insertBatch() spend "
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

	public int update(MappingFieldRelation mappingFieldRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mappingFieldRelation == null) {
			throw new IllegalArgumentException("mappingFieldRelation is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingFieldRelation.getMappingId() != null
				&& mappingFieldRelation.getMappingId().length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingFieldRelation.getMappingId());
		}
		if (mappingFieldRelation.getMappingId() != null
				&& mappingFieldRelation.getMappingId().length() > 10) {
			throw new IllegalArgumentException("mappingId too long"
					+ mappingFieldRelation.getMappingId());
		}
		if (mappingFieldRelation.getFieldMappingRuleId() != null
				&& mappingFieldRelation.getFieldMappingRuleId().length() < 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too short"
					+ mappingFieldRelation.getFieldMappingRuleId());
		}
		if (mappingFieldRelation.getFieldMappingRuleId() != null
				&& mappingFieldRelation.getFieldMappingRuleId().length() > 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too long"
					+ mappingFieldRelation.getFieldMappingRuleId());
		}
		try {
			String sql = "UPDATE mapping_field_relation SET mapping_id=?,field_mapping_rule_id=? where mapping_id=? and field_mapping_rule_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingFieldRelation.getMappingId());
			statment.setString(2, mappingFieldRelation.getFieldMappingRuleId());
			statment.setString(3, mappingFieldRelation.getMappingId());
			statment.setString(4, mappingFieldRelation.getFieldMappingRuleId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : mapping_id = \""
						+ mappingFieldRelation.getMappingId()
						+ "\",field_mapping_rule_id = \""
						+ mappingFieldRelation.getFieldMappingRuleId() + "\" ");
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

	public int dynamicUpdate(Map<String,String> primaryKey, Map<String,String> updateFields) {
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
		if (!primaryKey.containsKey("field_mapping_rule_id")) {
			throw new IllegalArgumentException("field_mapping_rule_id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE mapping_field_relation SET ");
			Iterator<String> it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where mapping_id=? and field_mapping_rule_id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("mapping_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("field_mapping_rule_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("mapping_id"));
			statment.setString(m++, (String) primaryKey
					.get("field_mapping_rule_id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("MappingFieldRelationDAO.dynamicUpdate() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
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

	public int deleteByPK(String mappingId, String fieldMappingRuleId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mappingId == null) {
			throw new IllegalArgumentException("mappingId is NULL!");
		}
		if (fieldMappingRuleId == null) {
			throw new IllegalArgumentException("fieldMappingRuleId is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingId != null && mappingId.length() < 10) {
			throw new IllegalArgumentException("mappingId too short"
					+ mappingId);
		}
		if (mappingId != null && mappingId.length() > 10) {
			throw new IllegalArgumentException("mappingId too long" + mappingId);
		}
		if (fieldMappingRuleId != null && fieldMappingRuleId.length() < 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too short"
					+ fieldMappingRuleId);
		}
		if (fieldMappingRuleId != null && fieldMappingRuleId.length() > 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too long"
					+ fieldMappingRuleId);
		}

		try {
			String sql = "DELETE FROM mapping_field_relation where mapping_id=? and field_mapping_rule_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingId);
			statment.setString(2, fieldMappingRuleId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : mapping_id = \""
						+ mappingId + "\",field_mapping_rule_id = \""
						+ fieldMappingRuleId + "\" ");
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

	public MappingFieldRelation selectByPK(String mappingId,
			String fieldMappingRuleId) {
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
		if (fieldMappingRuleId != null && fieldMappingRuleId.length() < 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too short"
					+ fieldMappingRuleId);
		}
		if (fieldMappingRuleId != null && fieldMappingRuleId.length() > 10) {
			throw new IllegalArgumentException("fieldMappingRuleId too long"
					+ fieldMappingRuleId);
		}
		ResultSet resultSet = null;
		MappingFieldRelation returnDTO = null;

		try {
			String sql = "SELECT * FROM mapping_field_relation where mapping_id=? and field_mapping_rule_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mappingId);
			statment.setString(2, fieldMappingRuleId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : mapping_id = \"" + mappingId
						+ "\",field_mapping_rule_id = \"" + fieldMappingRuleId
						+ "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new MappingFieldRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setFieldMappingRuleId(resultSet
						.getString("field_mapping_rule_id"));
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

	public List<MappingFieldRelation> findAll() {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MappingFieldRelation returnDTO = null;
		List<MappingFieldRelation> list = new ArrayList<>();

		try {
			String sql = "select * from mapping_field_relation order by mapping_id,field_mapping_rule_id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new MappingFieldRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setFieldMappingRuleId(resultSet
						.getString("field_mapping_rule_id"));
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

	public List<MappingFieldRelation> findAll(int pageNum, int pageLength) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MappingFieldRelation returnDTO = null;
		List<MappingFieldRelation> list = new ArrayList<>();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mapping_field_relation ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by mapping_id,field_mapping_rule_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from mapping_field_relation					 ) t1)t2 order by mapping_id,field_mapping_rule_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select mapping_id,field_mapping_rule_id, rownumber() over(order by mapping_id,field_mapping_rule_id) as rn from mapping_field_relation ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from mapping_field_relation order by mapping_id,field_mapping_rule_id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by mapping_id,field_mapping_rule_id) as rownumber, mapping_id,field_mapping_rule_id from mapping_field_relation ) A where rownumber > "
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
				debug("MappingFieldRelationDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new MappingFieldRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setFieldMappingRuleId(resultSet
						.getString("field_mapping_rule_id"));
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

	public List<MappingFieldRelation> findByWhere(String where) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MappingFieldRelation returnDTO = null;
		List<MappingFieldRelation> list = new ArrayList<>();

		try {
			String sql = "select * from mapping_field_relation where " + where
					+ " order by mapping_id,field_mapping_rule_id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new MappingFieldRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setFieldMappingRuleId(resultSet
						.getString("field_mapping_rule_id"));
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

	public List<MappingFieldRelation> findByWhere(String where, int pageNum, int pageLength) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		MappingFieldRelation returnDTO = null;
		List<MappingFieldRelation> list = new ArrayList<>();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from mapping_field_relation where " + where
						+ " order by mapping_id,field_mapping_rule_id limit "
						+ startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mapping_field_relation where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by mapping_id,field_mapping_rule_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from mapping_field_relation					 where " + where
						+ " ) t1)t2 order by mapping_id,field_mapping_rule_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select mapping_id,field_mapping_rule_id, rownumber() over(order by mapping_id,field_mapping_rule_id) as rn from mapping_field_relation where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by mapping_id,field_mapping_rule_id) as rownumber, mapping_id,field_mapping_rule_id from mapping_field_relation  where "
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
				debug("MappingFieldRelationDAO.findByWhere()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new MappingFieldRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setFieldMappingRuleId(resultSet
						.getString("field_mapping_rule_id"));
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
			String sql = "select  count(*) from mapping_field_relation";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from mapping_field_relation where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.getTotalRecords() spend "
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

	public List<MappingFieldRelation> getAllFieldRuleByMappingId(String mappingId) {
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
		MappingFieldRelation returnDTO = null;
		List<MappingFieldRelation> list = new ArrayList<>();
		try {
			String sql = "select * from mapping_field_relation where mapping_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, mappingId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("MappingFieldRelationDAO.getAllFieldRuleByMappingId() spend "
						+ (_endTime - _startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : mappingId = " + mappingId);
			}

			while (resultSet.next()) {
				returnDTO = new MappingFieldRelation();
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setFieldMappingRuleId(resultSet
						.getString("field_mapping_rule_id"));
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
