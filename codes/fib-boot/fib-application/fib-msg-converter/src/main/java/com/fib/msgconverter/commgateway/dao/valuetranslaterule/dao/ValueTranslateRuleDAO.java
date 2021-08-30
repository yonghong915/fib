package com.fib.msgconverter.commgateway.dao.valuetranslaterule.dao;

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

public class ValueTranslateRuleDAO extends AbstractDAO {

	public ValueTranslateRuleDAO() {
		super();
	}

	public ValueTranslateRuleDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ValueTranslateRuleDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(ValueTranslateRule valueTranslateRule) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (valueTranslateRule == null) {
			throw new IllegalArgumentException("valueTranslateRule is NULL!");
		}
		if (valueTranslateRule.getId() != null
				&& valueTranslateRule.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ valueTranslateRule.getId());
		}
		if (valueTranslateRule.getId() != null
				&& valueTranslateRule.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ valueTranslateRule.getId());
		}
		if (valueTranslateRule.getValueFrom() == null
				|| "".equals(valueTranslateRule.getValueFrom())) {
			throw new IllegalArgumentException("valueFrom is null");
		}
		if (valueTranslateRule.getValueFrom() != null
				&& valueTranslateRule.getValueFrom().length() > 255) {
			throw new IllegalArgumentException("valueFrom too long"
					+ valueTranslateRule.getValueFrom());
		}
		if (valueTranslateRule.getValueTo() == null
				|| "".equals(valueTranslateRule.getValueTo())) {
			throw new IllegalArgumentException("valueTo is null");
		}
		if (valueTranslateRule.getValueTo() != null
				&& valueTranslateRule.getValueTo().length() > 255) {
			throw new IllegalArgumentException("valueTo too long"
					+ valueTranslateRule.getValueTo());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into value_translate_rule(id,value_from,value_to) values(?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, valueTranslateRule.getId());
			statment.setString(2, valueTranslateRule.getValueFrom());
			statment.setString(3, valueTranslateRule.getValueTo());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.insert() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \""
						+ valueTranslateRule.getId() + "\",value_from = \""
						+ valueTranslateRule.getValueFrom()
						+ "\",value_to = \"" + valueTranslateRule.getValueTo()
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

	public int[] insertBatch(List<ValueTranslateRule> valueTranslateRuleList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (valueTranslateRuleList == null) {
			throw new IllegalArgumentException(
					"valueTranslateRuleList is NULL!");
		}
		for (ValueTranslateRule valueTranslateRule : valueTranslateRuleList) {
			if (valueTranslateRule.getId() != null
					&& valueTranslateRule.getId().length() < 10) {
				throw new IllegalArgumentException("id too short"
						+ valueTranslateRule.getId());
			}
			if (valueTranslateRule.getId() != null
					&& valueTranslateRule.getId().length() > 10) {
				throw new IllegalArgumentException("id too long"
						+ valueTranslateRule.getId());
			}
			if (valueTranslateRule.getValueFrom() == null
					|| "".equals(valueTranslateRule.getValueFrom())) {
				throw new IllegalArgumentException("valueFrom is null");
			}
			if (valueTranslateRule.getValueFrom() != null
					&& valueTranslateRule.getValueFrom().length() > 255) {
				throw new IllegalArgumentException("valueFrom too long"
						+ valueTranslateRule.getValueFrom());
			}
			if (valueTranslateRule.getValueTo() == null
					|| "".equals(valueTranslateRule.getValueTo())) {
				throw new IllegalArgumentException("valueTo is null");
			}
			if (valueTranslateRule.getValueTo() != null
					&& valueTranslateRule.getValueTo().length() > 255) {
				throw new IllegalArgumentException("valueTo too long"
						+ valueTranslateRule.getValueTo());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into value_translate_rule(id,value_from,value_to) values(?,?,?)";
			statment = conn.prepareStatement(sql);
			for (ValueTranslateRule valueTranslateRule : valueTranslateRuleList) {
				statment.setString(1, valueTranslateRule.getId());
				statment.setString(2, valueTranslateRule.getValueFrom());
				statment.setString(3, valueTranslateRule.getValueTo());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.insertBatch() spend "
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

	public int update(ValueTranslateRule valueTranslateRule) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (valueTranslateRule == null) {
			throw new IllegalArgumentException("valueTranslateRule is NULL!");
		}

		PreparedStatement statment = null;
		if (valueTranslateRule.getId() != null
				&& valueTranslateRule.getId().length() < 10) {
			throw new IllegalArgumentException("id too short"
					+ valueTranslateRule.getId());
		}
		if (valueTranslateRule.getId() != null
				&& valueTranslateRule.getId().length() > 10) {
			throw new IllegalArgumentException("id too long"
					+ valueTranslateRule.getId());
		}
		if (valueTranslateRule.getValueFrom() == null
				|| "".equals(valueTranslateRule.getValueFrom())) {
			throw new IllegalArgumentException("valueFrom is null");
		}
		if (valueTranslateRule.getValueFrom() != null
				&& valueTranslateRule.getValueFrom().length() > 255) {
			throw new IllegalArgumentException("valueFrom too long"
					+ valueTranslateRule.getValueFrom());
		}
		if (valueTranslateRule.getValueTo() == null
				|| "".equals(valueTranslateRule.getValueTo())) {
			throw new IllegalArgumentException("valueTo is null");
		}
		if (valueTranslateRule.getValueTo() != null
				&& valueTranslateRule.getValueTo().length() > 255) {
			throw new IllegalArgumentException("valueTo too long"
					+ valueTranslateRule.getValueTo());
		}
		try {
			String sql = "UPDATE value_translate_rule SET id=?,value_from=?,value_to=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, valueTranslateRule.getId());
			statment.setString(2, valueTranslateRule.getValueFrom());
			statment.setString(3, valueTranslateRule.getValueTo());
			statment.setString(4, valueTranslateRule.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.update() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : id = \""
						+ valueTranslateRule.getId() + "\",value_from = \""
						+ valueTranslateRule.getValueFrom()
						+ "\",value_to = \"" + valueTranslateRule.getValueTo()
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
		if (!primaryKey.containsKey("id")) {
			throw new IllegalArgumentException("id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE value_translate_rule SET ");
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
				if ("value_from".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("value_to".equalsIgnoreCase(tmpKey)) {
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
				sbDebug.append("ValueTranslateRuleDAO.dynamicUpdate() spend "
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
			String sql = "DELETE FROM value_translate_rule where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.deleteByPK() spend "
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

	public ValueTranslateRule selectByPK(String id) {
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
		ValueTranslateRule returnDTO = null;

		try {
			String sql = "SELECT * FROM value_translate_rule where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new ValueTranslateRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setValueFrom(resultSet.getString("value_from"));
				returnDTO.setValueTo(resultSet.getString("value_to"));
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
		ValueTranslateRule returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from value_translate_rule order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setValueFrom(resultSet.getString("value_from"));
				returnDTO.setValueTo(resultSet.getString("value_to"));
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
		ValueTranslateRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from value_translate_rule ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top "
						+ startNum
						+ " * from (select Top "
						+ endNum
						+ " * from value_translate_rule					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,value_from,value_to, rownumber() over(order by id) as rn from value_translate_rule ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from value_translate_rule order by id limit "
						+ startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,value_from,value_to from value_translate_rule ) A where rownumber > "
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
				debug("ValueTranslateRuleDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setValueFrom(resultSet.getString("value_from"));
				returnDTO.setValueTo(resultSet.getString("value_to"));
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
		ValueTranslateRule returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from value_translate_rule where " + where
					+ " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setValueFrom(resultSet.getString("value_from"));
				returnDTO.setValueTo(resultSet.getString("value_to"));
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
		ValueTranslateRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from value_translate_rule where " + where
						+ " order by id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from value_translate_rule where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from value_translate_rule					 where " + where
						+ " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,value_from,value_to, rownumber() over(order by id) as rn from value_translate_rule where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,value_from,value_to from value_translate_rule  where "
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
				debug("ValueTranslateRuleDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new ValueTranslateRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setValueFrom(resultSet.getString("value_from"));
				returnDTO.setValueTo(resultSet.getString("value_to"));
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
			String sql = "select  count(*) from value_translate_rule";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from value_translate_rule where "
					+ where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("ValueTranslateRuleDAO.getTotalRecords() spend "
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
