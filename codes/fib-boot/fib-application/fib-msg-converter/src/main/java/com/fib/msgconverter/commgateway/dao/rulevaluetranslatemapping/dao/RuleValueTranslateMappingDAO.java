package com.fib.msgconverter.commgateway.dao.rulevaluetranslatemapping.dao;

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

public class RuleValueTranslateMappingDAO extends AbstractDAO {

	public RuleValueTranslateMappingDAO() {
		super();
	}

	public RuleValueTranslateMappingDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public RuleValueTranslateMappingDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(RuleValueTranslateMapping ruleValueTranslateMapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (ruleValueTranslateMapping == null) {
			throw new IllegalArgumentException(
					"ruleValueTranslateMapping is NULL!");
		}
		if (ruleValueTranslateMapping.getId() != null
				&& ruleValueTranslateMapping.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ ruleValueTranslateMapping.getId());
		}
		if (ruleValueTranslateMapping.getId() != null
				&& ruleValueTranslateMapping.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ ruleValueTranslateMapping.getId());
		}
		if (ruleValueTranslateMapping.getAttrFrom() == null
				|| "".equals(ruleValueTranslateMapping.getAttrFrom())) {
			throw new IllegalArgumentException("attrFrom is null");
		}
		if (ruleValueTranslateMapping.getAttrFrom() != null
				&& ruleValueTranslateMapping.getAttrFrom().length() > 255) {
			throw new IllegalArgumentException("attrFrom too long"
					+ ruleValueTranslateMapping.getAttrFrom());
		}
		if (ruleValueTranslateMapping.getValueTranslateRuleGroupId() != null
				&& ruleValueTranslateMapping.getValueTranslateRuleGroupId()
						.length() < 10) {
			throw new IllegalArgumentException(
					"valueTranslateRuleGroupId too short"
							+ ruleValueTranslateMapping
									.getValueTranslateRuleGroupId());
		}
		if (ruleValueTranslateMapping.getValueTranslateRuleGroupId() != null
				&& ruleValueTranslateMapping.getValueTranslateRuleGroupId()
						.length() > 10) {
			throw new IllegalArgumentException(
					"valueTranslateRuleGroupId too long"
							+ ruleValueTranslateMapping
									.getValueTranslateRuleGroupId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into rule_value_translate_mapping(id,attr_from,value_translate_rule_group_id) values(?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, ruleValueTranslateMapping.getId());
			statment.setString(2, ruleValueTranslateMapping.getAttrFrom());
			statment.setString(3, ruleValueTranslateMapping
					.getValueTranslateRuleGroupId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.insert() spend "
						+ (endTime - startTime)
						+ "ms. retFlag = "
						+ retFlag
						+ " SQL:"
						+ sql
						+ "; parameters : id = \""
						+ ruleValueTranslateMapping.getId()
						+ "\",attr_from = \""
						+ ruleValueTranslateMapping.getAttrFrom()
						+ "\",value_translate_rule_group_id = \""
						+ ruleValueTranslateMapping
								.getValueTranslateRuleGroupId() + "\" ");
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
			List<RuleValueTranslateMapping> ruleValueTranslateMappingList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (ruleValueTranslateMappingList == null) {
			throw new IllegalArgumentException(
					"ruleValueTranslateMappingList is NULL!");
		}
		for (RuleValueTranslateMapping ruleValueTranslateMapping : ruleValueTranslateMappingList) {
			if (ruleValueTranslateMapping.getId() != null
					&& ruleValueTranslateMapping.getId().length() < 10) {
				throw new IllegalArgumentException("id too short"
						+ ruleValueTranslateMapping.getId());
			}
			if (ruleValueTranslateMapping.getId() != null
					&& ruleValueTranslateMapping.getId().length() > 10) {
				throw new IllegalArgumentException("id too long"
						+ ruleValueTranslateMapping.getId());
			}
			if (ruleValueTranslateMapping.getAttrFrom() == null
					|| "".equals(ruleValueTranslateMapping.getAttrFrom())) {
				throw new IllegalArgumentException("attrFrom is null");
			}
			if (ruleValueTranslateMapping.getAttrFrom() != null
					&& ruleValueTranslateMapping.getAttrFrom().length() > 255) {
				throw new IllegalArgumentException("attrFrom too long"
						+ ruleValueTranslateMapping.getAttrFrom());
			}
			if (ruleValueTranslateMapping.getValueTranslateRuleGroupId() != null
					&& ruleValueTranslateMapping.getValueTranslateRuleGroupId()
							.length() < 10) {
				throw new IllegalArgumentException(
						"valueTranslateRuleGroupId too short"
								+ ruleValueTranslateMapping
										.getValueTranslateRuleGroupId());
			}
			if (ruleValueTranslateMapping.getValueTranslateRuleGroupId() != null
					&& ruleValueTranslateMapping.getValueTranslateRuleGroupId()
							.length() > 10) {
				throw new IllegalArgumentException(
						"valueTranslateRuleGroupId too long"
								+ ruleValueTranslateMapping
										.getValueTranslateRuleGroupId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into rule_value_translate_mapping(id,attr_from,value_translate_rule_group_id) values(?,?,?)";
			statment = conn.prepareStatement(sql);
			for (RuleValueTranslateMapping ruleValueTranslateMapping : ruleValueTranslateMappingList) {
				statment.setString(1, ruleValueTranslateMapping.getId());
				statment.setString(2, ruleValueTranslateMapping.getAttrFrom());
				statment.setString(3, ruleValueTranslateMapping
						.getValueTranslateRuleGroupId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.insertBatch() spend "
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

	public int update(RuleValueTranslateMapping ruleValueTranslateMapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (ruleValueTranslateMapping == null) {
			throw new IllegalArgumentException(
					"ruleValueTranslateMapping is NULL!");
		}

		PreparedStatement statment = null;
		if (ruleValueTranslateMapping.getId() != null
				&& ruleValueTranslateMapping.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ ruleValueTranslateMapping.getId());
		}
		if (ruleValueTranslateMapping.getId() != null
				&& ruleValueTranslateMapping.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ ruleValueTranslateMapping.getId());
		}
		if (ruleValueTranslateMapping.getAttrFrom() == null
				|| "".equals(ruleValueTranslateMapping.getAttrFrom())) {
			throw new IllegalArgumentException("attrFrom is null");
		}
		if (ruleValueTranslateMapping.getAttrFrom() != null
				&& ruleValueTranslateMapping.getAttrFrom().length() > 255) {
			throw new IllegalArgumentException("attrFrom too long"
					+ ruleValueTranslateMapping.getAttrFrom());
		}
		if (ruleValueTranslateMapping.getValueTranslateRuleGroupId() != null
				&& ruleValueTranslateMapping.getValueTranslateRuleGroupId()
						.length() < 10) {
			throw new IllegalArgumentException(
					"valueTranslateRuleGroupId too short"
							+ ruleValueTranslateMapping
									.getValueTranslateRuleGroupId());
		}
		if (ruleValueTranslateMapping.getValueTranslateRuleGroupId() != null
				&& ruleValueTranslateMapping.getValueTranslateRuleGroupId()
						.length() > 10) {
			throw new IllegalArgumentException(
					"valueTranslateRuleGroupId too long"
							+ ruleValueTranslateMapping
									.getValueTranslateRuleGroupId());
		}
		try {
			String sql = "UPDATE rule_value_translate_mapping SET id=?,attr_from=?,value_translate_rule_group_id=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, ruleValueTranslateMapping.getId());
			statment.setString(2, ruleValueTranslateMapping.getAttrFrom());
			statment.setString(3, ruleValueTranslateMapping
					.getValueTranslateRuleGroupId());
			statment.setString(4, ruleValueTranslateMapping.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.update() spend "
						+ (endTime - startTime)
						+ "ms. retFlag = "
						+ retFlag
						+ " SQL:"
						+ sql
						+ "; parameters : id = \""
						+ ruleValueTranslateMapping.getId()
						+ "\",attr_from = \""
						+ ruleValueTranslateMapping.getAttrFrom()
						+ "\",value_translate_rule_group_id = \""
						+ ruleValueTranslateMapping
								.getValueTranslateRuleGroupId() + "\" ");
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
			sql.append("UPDATE rule_value_translate_mapping SET ");
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
				if ("value_translate_rule_group_id".equalsIgnoreCase(tmpKey)) {
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
				sbDebug
						.append("RuleValueTranslateMappingDAO.dynamicUpdate() spend "
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
			String sql = "DELETE FROM rule_value_translate_mapping where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.deleteByPK() spend "
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

	public RuleValueTranslateMapping selectByPK(String id) {
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
		RuleValueTranslateMapping returnDTO = null;

		try {
			String sql = "SELECT * FROM rule_value_translate_mapping where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new RuleValueTranslateMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
				returnDTO.setValueTranslateRuleGroupId(resultSet
						.getString("value_translate_rule_group_id"));
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
		RuleValueTranslateMapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from rule_value_translate_mapping order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueTranslateMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
				returnDTO.setValueTranslateRuleGroupId(resultSet
						.getString("value_translate_rule_group_id"));
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
		RuleValueTranslateMapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from rule_value_translate_mapping ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from rule_value_translate_mapping					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,attr_from,value_translate_rule_group_id, rownumber() over(order by id) as rn from rule_value_translate_mapping ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from rule_value_translate_mapping order by id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,attr_from,value_translate_rule_group_id from rule_value_translate_mapping ) A where rownumber > "
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
				debug("RuleValueTranslateMappingDAO.findAll()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueTranslateMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
				returnDTO.setValueTranslateRuleGroupId(resultSet
						.getString("value_translate_rule_group_id"));
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
		RuleValueTranslateMapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from rule_value_translate_mapping where "
					+ where + " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueTranslateMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
				returnDTO.setValueTranslateRuleGroupId(resultSet
						.getString("value_translate_rule_group_id"));
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
		RuleValueTranslateMapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from rule_value_translate_mapping where "
						+ where + " order by id limit " + startNum + ","
						+ pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from rule_value_translate_mapping where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from rule_value_translate_mapping					 where "
						+ where + " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,attr_from,value_translate_rule_group_id, rownumber() over(order by id) as rn from rule_value_translate_mapping where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,attr_from,value_translate_rule_group_id from rule_value_translate_mapping  where "
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
				debug("RuleValueTranslateMappingDAO.findByWhere()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new RuleValueTranslateMapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setAttrFrom(resultSet.getString("attr_from"));
				returnDTO.setValueTranslateRuleGroupId(resultSet
						.getString("value_translate_rule_group_id"));
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
			String sql = "select  count(*) from rule_value_translate_mapping";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from rule_value_translate_mapping where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("RuleValueTranslateMappingDAO.getTotalRecords() spend "
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
