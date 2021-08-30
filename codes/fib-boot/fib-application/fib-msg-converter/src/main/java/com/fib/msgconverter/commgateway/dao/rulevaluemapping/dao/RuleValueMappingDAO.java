package com.fib.msgconverter.commgateway.dao.rulevaluemapping.dao;

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

public class RuleValueMappingDAO extends AbstractDAO {

	public RuleValueMappingDAO() {
		super();
	}

	public RuleValueMappingDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public RuleValueMappingDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(RuleValueMapping ruleValueMapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (ruleValueMapping == null) {
			throw new IllegalArgumentException("ruleValueMapping is NULL!");
		}
		if (ruleValueMapping.getId() != null
				&& ruleValueMapping.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ ruleValueMapping.getId());
		}
		if (ruleValueMapping.getId() != null
				&& ruleValueMapping.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ ruleValueMapping.getId());
		}
		if (ruleValueMapping.getAttrFrom() == null
				|| "".equals(ruleValueMapping.getAttrFrom())) {
			throw new IllegalArgumentException("attrFrom is null");
		}
		if (ruleValueMapping.getAttrFrom() != null
				&& ruleValueMapping.getAttrFrom().length() > 255) {
			throw new IllegalArgumentException("attrFrom too long"
					+ ruleValueMapping.getAttrFrom());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into rule_value_mapping(id,attr_from) values(?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, ruleValueMapping.getId());
			statment.setString(2, ruleValueMapping.getAttrFrom());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \""
						+ ruleValueMapping.getId() + "\",attr_from = \""
						+ ruleValueMapping.getAttrFrom() + "\" ");
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

	public int[] insertBatch(List<RuleValueMapping> ruleValueMappingList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (ruleValueMappingList == null) {
			throw new IllegalArgumentException("ruleValueMappingList is NULL!");
		}
		for (RuleValueMapping ruleValueMapping : ruleValueMappingList) {
			if (ruleValueMapping.getId() != null
					&& ruleValueMapping.getId().length() < 10) {
				throw new IllegalArgumentException("id too short"
						+ ruleValueMapping.getId());
			}
			if (ruleValueMapping.getId() != null
					&& ruleValueMapping.getId().length() > 10) {
				throw new IllegalArgumentException("id too long"
						+ ruleValueMapping.getId());
			}
			if (ruleValueMapping.getAttrFrom() == null
					|| "".equals(ruleValueMapping.getAttrFrom())) {
				throw new IllegalArgumentException("attrFrom is null");
			}
			if (ruleValueMapping.getAttrFrom() != null
					&& ruleValueMapping.getAttrFrom().length() > 255) {
				throw new IllegalArgumentException("attrFrom too long"
						+ ruleValueMapping.getAttrFrom());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into rule_value_mapping(id,attr_from) values(?,?)";
			statment = conn.prepareStatement(sql);
			for (RuleValueMapping ruleValueMapping : ruleValueMappingList) {
				statment.setString(1, ruleValueMapping.getId());
				statment.setString(2, ruleValueMapping.getAttrFrom());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.insertBatch() spend "
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

	public int update(RuleValueMapping ruleValueMapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (ruleValueMapping == null) {
			throw new IllegalArgumentException("ruleValueMapping is NULL!");
		}

		PreparedStatement statment = null;
		if (ruleValueMapping.getId() != null
				&& ruleValueMapping.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ ruleValueMapping.getId());
		}
		if (ruleValueMapping.getId() != null
				&& ruleValueMapping.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ ruleValueMapping.getId());
		}
		if (ruleValueMapping.getAttrFrom() == null
				|| "".equals(ruleValueMapping.getAttrFrom())) {
			throw new IllegalArgumentException("attrFrom is null");
		}
		if (ruleValueMapping.getAttrFrom() != null
				&& ruleValueMapping.getAttrFrom().length() > 255) {
			throw new IllegalArgumentException("attrFrom too long"
					+ ruleValueMapping.getAttrFrom());
		}
		try {
			String sql = "UPDATE rule_value_mapping SET id=?,attr_from=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, ruleValueMapping.getId());
			statment.setString(2, ruleValueMapping.getAttrFrom());
			statment.setString(3, ruleValueMapping.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \""
						+ ruleValueMapping.getId() + "\",attr_from = \""
						+ ruleValueMapping.getAttrFrom() + "\" ");
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
			sql.append("UPDATE rule_value_mapping SET ");
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
				if ("attr_from".equalsIgnoreCase(tmpKey)) {
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
				sbDebug.append("RuleValueMappingDAO.dynamicUpdate() spend "
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
			String sql = "DELETE FROM rule_value_mapping where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.deleteByPK() spend "
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

	public RuleValueMapping selectByPK(String id) {
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
		RuleValueMapping returnDTO = null;

		try {
			String sql = "SELECT * FROM rule_value_mapping where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new RuleValueMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
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
		RuleValueMapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from rule_value_mapping order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
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
		RuleValueMapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from rule_value_mapping ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from rule_value_mapping					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,attr_from, rownumber() over(order by id) as rn from rule_value_mapping ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from rule_value_mapping order by id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,attr_from from rule_value_mapping ) A where rownumber > "
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
				debug("RuleValueMappingDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
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
		RuleValueMapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from rule_value_mapping where " + where
					+ " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
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
		RuleValueMapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from rule_value_mapping where " + where
						+ " order by id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from rule_value_mapping where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from rule_value_mapping					 where " + where
						+ " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,attr_from, rownumber() over(order by id) as rn from rule_value_mapping where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,attr_from from rule_value_mapping  where "
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
				debug("RuleValueMappingDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
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
			String sql = "select  count(*) from rule_value_mapping";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from rule_value_mapping where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueMappingDAO.getTotalRecords() spend "
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
