package com.fib.msgconverter.commgateway.dao.valuetranslaterulerelation.dao;

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

public class ValueTranslateRuleRelationDAO extends AbstractDAO {

	public ValueTranslateRuleRelationDAO() {
		super();
	}

	public ValueTranslateRuleRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ValueTranslateRuleRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(ValueTranslateRuleRelation valueTranslateRuleRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (valueTranslateRuleRelation == null) {
			throw new IllegalArgumentException(
					"valueTranslateRuleRelation is NULL!");
		}
		if (valueTranslateRuleRelation.getGroupId() != null
				&& valueTranslateRuleRelation.getGroupId().length() < 10) {
			throw new IllegalArgumentException("groupId too short"
					+ valueTranslateRuleRelation.getGroupId());
		}
		if (valueTranslateRuleRelation.getGroupId() != null
				&& valueTranslateRuleRelation.getGroupId().length() > 10) {
			throw new IllegalArgumentException("groupId too long"
					+ valueTranslateRuleRelation.getGroupId());
		}
		if (valueTranslateRuleRelation.getRuleId() != null
				&& valueTranslateRuleRelation.getRuleId().length() < 10) {
			throw new IllegalArgumentException("ruleId too short"
					+ valueTranslateRuleRelation.getRuleId());
		}
		if (valueTranslateRuleRelation.getRuleId() != null
				&& valueTranslateRuleRelation.getRuleId().length() > 10) {
			throw new IllegalArgumentException("ruleId too long"
					+ valueTranslateRuleRelation.getRuleId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into value_translate_rule_relation(group_id,rule_id) values(?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, valueTranslateRuleRelation.getGroupId());
			statment.setString(2, valueTranslateRuleRelation.getRuleId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : group_id = \""
						+ valueTranslateRuleRelation.getGroupId()
						+ "\",rule_id = \""
						+ valueTranslateRuleRelation.getRuleId() + "\" ");
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
			List<ValueTranslateRuleRelation> valueTranslateRuleRelationList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (valueTranslateRuleRelationList == null) {
			throw new IllegalArgumentException(
					"valueTranslateRuleRelationList is NULL!");
		}
		for (ValueTranslateRuleRelation valueTranslateRuleRelation : valueTranslateRuleRelationList) {
			if (valueTranslateRuleRelation.getGroupId() != null
					&& valueTranslateRuleRelation.getGroupId().length() < 10) {
				throw new IllegalArgumentException("groupId too short"
						+ valueTranslateRuleRelation.getGroupId());
			}
			if (valueTranslateRuleRelation.getGroupId() != null
					&& valueTranslateRuleRelation.getGroupId().length() > 10) {
				throw new IllegalArgumentException("groupId too long"
						+ valueTranslateRuleRelation.getGroupId());
			}
			if (valueTranslateRuleRelation.getRuleId() != null
					&& valueTranslateRuleRelation.getRuleId().length() < 10) {
				throw new IllegalArgumentException("ruleId too short"
						+ valueTranslateRuleRelation.getRuleId());
			}
			if (valueTranslateRuleRelation.getRuleId() != null
					&& valueTranslateRuleRelation.getRuleId().length() > 10) {
				throw new IllegalArgumentException("ruleId too long"
						+ valueTranslateRuleRelation.getRuleId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into value_translate_rule_relation(group_id,rule_id) values(?,?)";
			statment = conn.prepareStatement(sql);
			for (ValueTranslateRuleRelation valueTranslateRuleRelation : valueTranslateRuleRelationList) {
				statment.setString(1, valueTranslateRuleRelation.getGroupId());
				statment.setString(2, valueTranslateRuleRelation.getRuleId());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.insertBatch() spend "
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

	public int update(ValueTranslateRuleRelation valueTranslateRuleRelation) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (valueTranslateRuleRelation == null) {
			throw new IllegalArgumentException(
					"valueTranslateRuleRelation is NULL!");
		}

		PreparedStatement statment = null;
		if (valueTranslateRuleRelation.getGroupId() != null
				&& valueTranslateRuleRelation.getGroupId().length() < 10) {
			throw new IllegalArgumentException("groupId too short"
					+ valueTranslateRuleRelation.getGroupId());
		}
		if (valueTranslateRuleRelation.getGroupId() != null
				&& valueTranslateRuleRelation.getGroupId().length() > 10) {
			throw new IllegalArgumentException("groupId too long"
					+ valueTranslateRuleRelation.getGroupId());
		}
		if (valueTranslateRuleRelation.getRuleId() != null
				&& valueTranslateRuleRelation.getRuleId().length() < 10) {
			throw new IllegalArgumentException("ruleId too short"
					+ valueTranslateRuleRelation.getRuleId());
		}
		if (valueTranslateRuleRelation.getRuleId() != null
				&& valueTranslateRuleRelation.getRuleId().length() > 10) {
			throw new IllegalArgumentException("ruleId too long"
					+ valueTranslateRuleRelation.getRuleId());
		}
		try {
			String sql = "UPDATE value_translate_rule_relation SET group_id=?,rule_id=? where group_id=? and rule_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, valueTranslateRuleRelation.getGroupId());
			statment.setString(2, valueTranslateRuleRelation.getRuleId());
			statment.setString(3, valueTranslateRuleRelation.getGroupId());
			statment.setString(4, valueTranslateRuleRelation.getRuleId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : group_id = \""
						+ valueTranslateRuleRelation.getGroupId()
						+ "\",rule_id = \""
						+ valueTranslateRuleRelation.getRuleId() + "\" ");
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
		if (!primaryKey.containsKey("group_id")) {
			throw new IllegalArgumentException("group_id is null ");
		}
		if (!primaryKey.containsKey("rule_id")) {
			throw new IllegalArgumentException("rule_id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE value_translate_rule_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where group_id=? and rule_id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("group_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("rule_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("group_id"));
			statment.setString(m++, (String) primaryKey.get("rule_id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug
						.append("ValueTranslateRuleRelationDAO.dynamicUpdate() spend "
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

	public int deleteByPK(String groupId, String ruleId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (groupId == null) {
			throw new IllegalArgumentException("groupId is NULL!");
		}
		if (ruleId == null) {
			throw new IllegalArgumentException("ruleId is NULL!");
		}

		PreparedStatement statment = null;
		if (groupId != null && groupId.length() < 10) {
			throw new IllegalArgumentException("groupId too short" + groupId);
		}
		if (groupId != null && groupId.length() > 10) {
			throw new IllegalArgumentException("groupId too long" + groupId);
		}
		if (ruleId != null && ruleId.length() < 10) {
			throw new IllegalArgumentException("ruleId too short" + ruleId);
		}
		if (ruleId != null && ruleId.length() > 10) {
			throw new IllegalArgumentException("ruleId too long" + ruleId);
		}

		try {
			String sql = "DELETE FROM value_translate_rule_relation where group_id=? and rule_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, groupId);
			statment.setString(2, ruleId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : group_id = \""
						+ groupId + "\",rule_id = \"" + ruleId + "\" ");
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

	public ValueTranslateRuleRelation selectByPK(String groupId, String ruleId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (groupId != null && groupId.length() < 10) {
			throw new IllegalArgumentException("groupId too short" + groupId);
		}
		if (groupId != null && groupId.length() > 10) {
			throw new IllegalArgumentException("groupId too long" + groupId);
		}
		if (ruleId != null && ruleId.length() < 10) {
			throw new IllegalArgumentException("ruleId too short" + ruleId);
		}
		if (ruleId != null && ruleId.length() > 10) {
			throw new IllegalArgumentException("ruleId too long" + ruleId);
		}
		ResultSet resultSet = null;
		ValueTranslateRuleRelation returnDTO = null;

		try {
			String sql = "SELECT * FROM value_translate_rule_relation where group_id=? and rule_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, groupId);
			statment.setString(2, ruleId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : group_id = \"" + groupId
						+ "\",rule_id = \"" + ruleId + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new ValueTranslateRuleRelation();
				returnDTO.setGroupId(resultSet.getString("group_id"));
				returnDTO.setRuleId(resultSet.getString("rule_id"));
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
		ValueTranslateRuleRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from value_translate_rule_relation order by group_id,rule_id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRuleRelation();
				returnDTO.setGroupId(resultSet.getString("group_id"));
				returnDTO.setRuleId(resultSet.getString("rule_id"));
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
		ValueTranslateRuleRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from value_translate_rule_relation ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by group_id,rule_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from value_translate_rule_relation					 ) t1)t2 order by group_id,rule_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select group_id,rule_id, rownumber() over(order by group_id,rule_id) as rn from value_translate_rule_relation ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from value_translate_rule_relation order by group_id,rule_id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by group_id,rule_id) as rownumber, group_id,rule_id from value_translate_rule_relation ) A where rownumber > "
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
				debug("ValueTranslateRuleRelationDAO.findAll()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRuleRelation();
				returnDTO.setGroupId(resultSet.getString("group_id"));
				returnDTO.setRuleId(resultSet.getString("rule_id"));
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
		ValueTranslateRuleRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from value_translate_rule_relation where "
					+ where + " order by group_id,rule_id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRuleRelation();
				returnDTO.setGroupId(resultSet.getString("group_id"));
				returnDTO.setRuleId(resultSet.getString("rule_id"));
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
		ValueTranslateRuleRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from value_translate_rule_relation where "
						+ where + " order by group_id,rule_id limit "
						+ startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from value_translate_rule_relation where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by group_id,rule_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from value_translate_rule_relation					 where "
						+ where + " ) t1)t2 order by group_id,rule_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select group_id,rule_id, rownumber() over(order by group_id,rule_id) as rn from value_translate_rule_relation where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by group_id,rule_id) as rownumber, group_id,rule_id from value_translate_rule_relation  where "
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
				debug("ValueTranslateRuleRelationDAO.findByWhere()(pageNum:"
						+ pageNum + ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRuleRelation();
				returnDTO.setGroupId(resultSet.getString("group_id"));
				returnDTO.setRuleId(resultSet.getString("rule_id"));
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
			String sql = "select  count(*) from value_translate_rule_relation";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from value_translate_rule_relation where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.getTotalRecords() spend "
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

	public List getAllTranslateRuleByGroupId(String groupId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (groupId != null && groupId.length() < 10) {
			throw new IllegalArgumentException("groupId too short" + groupId);
		}
		if (groupId != null && groupId.length() > 10) {
			throw new IllegalArgumentException("groupId too long" + groupId);
		}
		ResultSet resultSet = null;
		ValueTranslateRuleRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from value_translate_rule_relation where group_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, groupId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleRelationDAO.getAllTranslateRuleByGroupId() spend "
						+ (_endTime - _startTime)
						+ "ms. SQL:"
						+ sql
						+ "; parameter : groupId = " + groupId);
			}

			while (resultSet.next()) {
				returnDTO = new ValueTranslateRuleRelation();
				returnDTO.setGroupId(resultSet.getString("group_id"));
				returnDTO.setRuleId(resultSet.getString("rule_id"));
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
