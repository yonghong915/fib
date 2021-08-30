package com.fib.msgconverter.commgateway.dao.fieldmappingrule.dao;

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

public class FieldMappingRuleDAO extends AbstractDAO {

	public FieldMappingRuleDAO() {
		super();
	}

	public FieldMappingRuleDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public FieldMappingRuleDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(FieldMappingRule fieldMappingRule) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (fieldMappingRule == null) {
			throw new IllegalArgumentException("fieldMappingRule is NULL!");
		}
		if (fieldMappingRule.getId() != null
				&& fieldMappingRule.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ fieldMappingRule.getId());
		}
		if (fieldMappingRule.getId() != null
				&& fieldMappingRule.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ fieldMappingRule.getId());
		}
		if (fieldMappingRule.getMappingType() == null
				|| "".equals(fieldMappingRule.getMappingType())) {
			throw new IllegalArgumentException("mappingType is null");
		}
		if (fieldMappingRule.getMappingType() != null
				&& fieldMappingRule.getMappingType().length() < 4) {
			throw new IllegalArgumentException("mappingType too short"
					+ fieldMappingRule.getMappingType());
		}
		if (fieldMappingRule.getMappingType() != null
				&& fieldMappingRule.getMappingType().length() > 4) {
			throw new IllegalArgumentException("mappingType too long"
					+ fieldMappingRule.getMappingType());
		}
		if (fieldMappingRule.getAttrTo() == null
				|| "".equals(fieldMappingRule.getAttrTo())) {
			throw new IllegalArgumentException("attrTo is null");
		}
		if (fieldMappingRule.getAttrTo() != null
				&& fieldMappingRule.getAttrTo().length() > 255) {
			throw new IllegalArgumentException("attrTo too long"
					+ fieldMappingRule.getAttrTo());
		}
		if (fieldMappingRule.getForceTypeConversion() != null
				&& fieldMappingRule.getForceTypeConversion().length() < 1) {
			throw new IllegalArgumentException("forceTypeConversion too short"
					+ fieldMappingRule.getForceTypeConversion());
		}
		if (fieldMappingRule.getForceTypeConversion() != null
				&& fieldMappingRule.getForceTypeConversion().length() > 1) {
			throw new IllegalArgumentException("forceTypeConversion too long"
					+ fieldMappingRule.getForceTypeConversion());
		}
		if (fieldMappingRule.getTargetDataType() != null
				&& fieldMappingRule.getTargetDataType().length() > 8) {
			throw new IllegalArgumentException("targetDataType too long"
					+ fieldMappingRule.getTargetDataType());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into field_mapping_rule(id,mapping_type,attr_to,force_type_conversion,target_data_type) values(?,?,?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, fieldMappingRule.getId());
			statment.setString(2, fieldMappingRule.getMappingType());
			statment.setString(3, fieldMappingRule.getAttrTo());
			statment.setString(4, fieldMappingRule.getForceTypeConversion());
			statment.setString(5, fieldMappingRule.getTargetDataType());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \""
						+ fieldMappingRule.getId() + "\",mapping_type = \""
						+ fieldMappingRule.getMappingType() + "\",attr_to = \""
						+ fieldMappingRule.getAttrTo()
						+ "\",force_type_conversion = \""
						+ fieldMappingRule.getForceTypeConversion()
						+ "\",target_data_type = \""
						+ fieldMappingRule.getTargetDataType() + "\" ");
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

	public int[] insertBatch(List<FieldMappingRule> fieldMappingRuleList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (fieldMappingRuleList == null) {
			throw new IllegalArgumentException("fieldMappingRuleList is NULL!");
		}
		for (FieldMappingRule fieldMappingRule : fieldMappingRuleList) {
			if (fieldMappingRule.getId() != null
					&& fieldMappingRule.getId().length() < 10) {
				throw new IllegalArgumentException("id too short"
						+ fieldMappingRule.getId());
			}
			if (fieldMappingRule.getId() != null
					&& fieldMappingRule.getId().length() > 10) {
				throw new IllegalArgumentException("id too long"
						+ fieldMappingRule.getId());
			}
			if (fieldMappingRule.getMappingType() == null
					|| "".equals(fieldMappingRule.getMappingType())) {
				throw new IllegalArgumentException("mappingType is null");
			}
			if (fieldMappingRule.getMappingType() != null
					&& fieldMappingRule.getMappingType().length() < 4) {
				throw new IllegalArgumentException("mappingType too short"
						+ fieldMappingRule.getMappingType());
			}
			if (fieldMappingRule.getMappingType() != null
					&& fieldMappingRule.getMappingType().length() > 4) {
				throw new IllegalArgumentException("mappingType too long"
						+ fieldMappingRule.getMappingType());
			}
			if (fieldMappingRule.getAttrTo() == null
					|| "".equals(fieldMappingRule.getAttrTo())) {
				throw new IllegalArgumentException("attrTo is null");
			}
			if (fieldMappingRule.getAttrTo() != null
					&& fieldMappingRule.getAttrTo().length() > 255) {
				throw new IllegalArgumentException("attrTo too long"
						+ fieldMappingRule.getAttrTo());
			}
			if (fieldMappingRule.getForceTypeConversion() != null
					&& fieldMappingRule.getForceTypeConversion().length() < 1) {
				throw new IllegalArgumentException(
						"forceTypeConversion too short"
								+ fieldMappingRule.getForceTypeConversion());
			}
			if (fieldMappingRule.getForceTypeConversion() != null
					&& fieldMappingRule.getForceTypeConversion().length() > 1) {
				throw new IllegalArgumentException(
						"forceTypeConversion too long"
								+ fieldMappingRule.getForceTypeConversion());
			}
			if (fieldMappingRule.getTargetDataType() != null
					&& fieldMappingRule.getTargetDataType().length() > 8) {
				throw new IllegalArgumentException("targetDataType too long"
						+ fieldMappingRule.getTargetDataType());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into field_mapping_rule(id,mapping_type,attr_to,force_type_conversion,target_data_type) values(?,?,?,?,?)";
			statment = conn.prepareStatement(sql);
			for (FieldMappingRule fieldMappingRule : fieldMappingRuleList) {
				statment.setString(1, fieldMappingRule.getId());
				statment.setString(2, fieldMappingRule.getMappingType());
				statment.setString(3, fieldMappingRule.getAttrTo());
				statment
						.setString(4, fieldMappingRule.getForceTypeConversion());
				statment.setString(5, fieldMappingRule.getTargetDataType());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.insertBatch() spend "
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

	public int update(FieldMappingRule fieldMappingRule) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (fieldMappingRule == null) {
			throw new IllegalArgumentException("fieldMappingRule is NULL!");
		}

		PreparedStatement statment = null;
		if (fieldMappingRule.getId() != null
				&& fieldMappingRule.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ fieldMappingRule.getId());
		}
		if (fieldMappingRule.getId() != null
				&& fieldMappingRule.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ fieldMappingRule.getId());
		}
		if (fieldMappingRule.getMappingType() == null
				|| "".equals(fieldMappingRule.getMappingType())) {
			throw new IllegalArgumentException("mappingType is null");
		}
		if (fieldMappingRule.getMappingType() != null
				&& fieldMappingRule.getMappingType().length() < 4) {
			throw new IllegalArgumentException("mappingType too short"
					+ fieldMappingRule.getMappingType());
		}
		if (fieldMappingRule.getMappingType() != null
				&& fieldMappingRule.getMappingType().length() > 4) {
			throw new IllegalArgumentException("mappingType too long"
					+ fieldMappingRule.getMappingType());
		}
		if (fieldMappingRule.getAttrTo() == null
				|| "".equals(fieldMappingRule.getAttrTo())) {
			throw new IllegalArgumentException("attrTo is null");
		}
		if (fieldMappingRule.getAttrTo() != null
				&& fieldMappingRule.getAttrTo().length() > 255) {
			throw new IllegalArgumentException("attrTo too long"
					+ fieldMappingRule.getAttrTo());
		}
		if (fieldMappingRule.getForceTypeConversion() != null
				&& fieldMappingRule.getForceTypeConversion().length() < 1) {
			throw new IllegalArgumentException("forceTypeConversion too short"
					+ fieldMappingRule.getForceTypeConversion());
		}
		if (fieldMappingRule.getForceTypeConversion() != null
				&& fieldMappingRule.getForceTypeConversion().length() > 1) {
			throw new IllegalArgumentException("forceTypeConversion too long"
					+ fieldMappingRule.getForceTypeConversion());
		}
		if (fieldMappingRule.getTargetDataType() != null
				&& fieldMappingRule.getTargetDataType().length() > 8) {
			throw new IllegalArgumentException("targetDataType too long"
					+ fieldMappingRule.getTargetDataType());
		}
		try {
			String sql = "UPDATE field_mapping_rule SET id=?,mapping_type=?,attr_to=?,force_type_conversion=?,target_data_type=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, fieldMappingRule.getId());
			statment.setString(2, fieldMappingRule.getMappingType());
			statment.setString(3, fieldMappingRule.getAttrTo());
			statment.setString(4, fieldMappingRule.getForceTypeConversion());
			statment.setString(5, fieldMappingRule.getTargetDataType());
			statment.setString(6, fieldMappingRule.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \""
						+ fieldMappingRule.getId() + "\",mapping_type = \""
						+ fieldMappingRule.getMappingType() + "\",attr_to = \""
						+ fieldMappingRule.getAttrTo()
						+ "\",force_type_conversion = \""
						+ fieldMappingRule.getForceTypeConversion()
						+ "\",target_data_type = \""
						+ fieldMappingRule.getTargetDataType() + "\" ");
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
		if (!primaryKey.containsKey("id")) {
			throw new IllegalArgumentException("id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE field_mapping_rule SET ");
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
				if ("mapping_type".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("attr_to".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("force_type_conversion".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("target_data_type".equalsIgnoreCase(tmpKey)) {
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
				sbDebug.append("FieldMappingRuleDAO.dynamicUpdate() spend "
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
		Connection conn = getConnection();
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
			String sql = "DELETE FROM field_mapping_rule where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \"" + id + "\" ");
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

	public FieldMappingRule selectByPK(String id) {
		Connection conn = getConnection();
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
		FieldMappingRule returnDTO = null;

		try {
			String sql = "SELECT * FROM field_mapping_rule where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new FieldMappingRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingType(resultSet.getString("mapping_type"));
				returnDTO.setAttrTo(resultSet.getString("attr_to"));
				returnDTO.setForceTypeConversion(resultSet
						.getString("force_type_conversion"));
				returnDTO.setTargetDataType(resultSet
						.getString("target_data_type"));
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
		FieldMappingRule returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from field_mapping_rule order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new FieldMappingRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingType(resultSet.getString("mapping_type"));
				returnDTO.setAttrTo(resultSet.getString("attr_to"));
				returnDTO.setForceTypeConversion(resultSet
						.getString("force_type_conversion"));
				returnDTO.setTargetDataType(resultSet
						.getString("target_data_type"));
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
		FieldMappingRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from field_mapping_rule ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from field_mapping_rule					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,mapping_type,attr_to,force_type_conversion,target_data_type, rownumber() over(order by id) as rn from field_mapping_rule ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from field_mapping_rule order by id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,mapping_type,attr_to,force_type_conversion,target_data_type from field_mapping_rule ) A where rownumber > "
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
				debug("FieldMappingRuleDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new FieldMappingRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingType(resultSet.getString("mapping_type"));
				returnDTO.setAttrTo(resultSet.getString("attr_to"));
				returnDTO.setForceTypeConversion(resultSet
						.getString("force_type_conversion"));
				returnDTO.setTargetDataType(resultSet
						.getString("target_data_type"));
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
		FieldMappingRule returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from field_mapping_rule where " + where
					+ " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new FieldMappingRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingType(resultSet.getString("mapping_type"));
				returnDTO.setAttrTo(resultSet.getString("attr_to"));
				returnDTO.setForceTypeConversion(resultSet
						.getString("force_type_conversion"));
				returnDTO.setTargetDataType(resultSet
						.getString("target_data_type"));
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
		FieldMappingRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from field_mapping_rule where " + where
						+ " order by id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from field_mapping_rule where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from field_mapping_rule					 where " + where
						+ " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,mapping_type,attr_to,force_type_conversion,target_data_type, rownumber() over(order by id) as rn from field_mapping_rule where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,mapping_type,attr_to,force_type_conversion,target_data_type from field_mapping_rule  where "
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
				debug("FieldMappingRuleDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new FieldMappingRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingType(resultSet.getString("mapping_type"));
				returnDTO.setAttrTo(resultSet.getString("attr_to"));
				returnDTO.setForceTypeConversion(resultSet
						.getString("force_type_conversion"));
				returnDTO.setTargetDataType(resultSet
						.getString("target_data_type"));
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
			String sql = "select  count(*) from field_mapping_rule";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from field_mapping_rule where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("FieldMappingRuleDAO.getTotalRecords() spend "
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

}
