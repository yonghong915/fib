package com.fib.msgconverter.commgateway.dao.mapping.dao;

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

public class MappingDAO extends AbstractDAO {

	public MappingDAO() {
		super();
	}

	public MappingDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MappingDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Mapping mapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mapping == null) {
			throw new IllegalArgumentException("mapping is NULL!");
		}
		if (mapping.getId() != null && mapping.getId().length() < 10) {
			throw new IllegalArgumentException("id too short" + mapping.getId());
		}
		if (mapping.getId() != null && mapping.getId().length() > 10) {
			throw new IllegalArgumentException("id too long" + mapping.getId());
		}
		if (mapping.getMappingId() == null || "".equals(mapping.getMappingId())) {
			throw new IllegalArgumentException("mappingId is null");
		}
		if (mapping.getMappingId() != null
				&& mapping.getMappingId().length() > 255) {
			throw new IllegalArgumentException("mappingId too long"
					+ mapping.getMappingId());
		}
		if (mapping.getMappingGroupId() == null
				|| "".equals(mapping.getMappingGroupId())) {
			throw new IllegalArgumentException("mappingGroupId is null");
		}
		if (mapping.getMappingGroupId() != null
				&& mapping.getMappingGroupId().length() > 255) {
			throw new IllegalArgumentException("mappingGroupId too long"
					+ mapping.getMappingGroupId());
		}
		if (mapping.getTargetType() != null
				&& mapping.getTargetType().length() < 4) {
			throw new IllegalArgumentException("targetType too short"
					+ mapping.getTargetType());
		}
		if (mapping.getTargetType() != null
				&& mapping.getTargetType().length() > 4) {
			throw new IllegalArgumentException("targetType too long"
					+ mapping.getTargetType());
		}
		if (mapping.getTargetBeanClass() != null
				&& mapping.getTargetBeanClass().length() > 255) {
			throw new IllegalArgumentException("targetBeanClass too long"
					+ mapping.getTargetBeanClass());
		}
		if (mapping.getHasScript() != null
				&& mapping.getHasScript().length() < 1) {
			throw new IllegalArgumentException("hasScript too short"
					+ mapping.getHasScript());
		}
		if (mapping.getHasScript() != null
				&& mapping.getHasScript().length() > 1) {
			throw new IllegalArgumentException("hasScript too long"
					+ mapping.getHasScript());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into mapping(id,mapping_id,mapping_group_id,target_type,target_bean_class,has_script) values(?,?,?,?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, mapping.getId());
			statment.setString(2, mapping.getMappingId());
			statment.setString(3, mapping.getMappingGroupId());
			statment.setString(4, mapping.getTargetType());
			statment.setString(5, mapping.getTargetBeanClass());
			statment.setString(6, mapping.getHasScript());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.insert() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + mapping.getId()
						+ "\",mapping_id = \"" + mapping.getMappingId()
						+ "\",mapping_group_id = \""
						+ mapping.getMappingGroupId() + "\",target_type = \""
						+ mapping.getTargetType() + "\",target_bean_class = \""
						+ mapping.getTargetBeanClass() + "\",has_script = \""
						+ mapping.getHasScript() + "\" ");
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

	public int[] insertBatch(List<Mapping> mappingList) {
		// 获得连接对象
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (mappingList == null) {
			throw new IllegalArgumentException("mappingList is NULL!");
		}
		for (Mapping mapping : mappingList) {
			if (mapping.getId() != null && mapping.getId().length() < 10) {
				throw new IllegalArgumentException("id too short"
						+ mapping.getId());
			}
			if (mapping.getId() != null && mapping.getId().length() > 10) {
				throw new IllegalArgumentException("id too long"
						+ mapping.getId());
			}
			if (mapping.getMappingId() == null
					|| "".equals(mapping.getMappingId())) {
				throw new IllegalArgumentException("mappingId is null");
			}
			if (mapping.getMappingId() != null
					&& mapping.getMappingId().length() > 255) {
				throw new IllegalArgumentException("mappingId too long"
						+ mapping.getMappingId());
			}
			if (mapping.getMappingGroupId() == null
					|| "".equals(mapping.getMappingGroupId())) {
				throw new IllegalArgumentException("mappingGroupId is null");
			}
			if (mapping.getMappingGroupId() != null
					&& mapping.getMappingGroupId().length() > 255) {
				throw new IllegalArgumentException("mappingGroupId too long"
						+ mapping.getMappingGroupId());
			}
			if (mapping.getTargetType() != null
					&& mapping.getTargetType().length() < 4) {
				throw new IllegalArgumentException("targetType too short"
						+ mapping.getTargetType());
			}
			if (mapping.getTargetType() != null
					&& mapping.getTargetType().length() > 4) {
				throw new IllegalArgumentException("targetType too long"
						+ mapping.getTargetType());
			}
			if (mapping.getTargetBeanClass() != null
					&& mapping.getTargetBeanClass().length() > 255) {
				throw new IllegalArgumentException("targetBeanClass too long"
						+ mapping.getTargetBeanClass());
			}
			if (mapping.getHasScript() != null
					&& mapping.getHasScript().length() < 1) {
				throw new IllegalArgumentException("hasScript too short"
						+ mapping.getHasScript());
			}
			if (mapping.getHasScript() != null
					&& mapping.getHasScript().length() > 1) {
				throw new IllegalArgumentException("hasScript too long"
						+ mapping.getHasScript());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into mapping(id,mapping_id,mapping_group_id,target_type,target_bean_class,has_script) values(?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql);
			for (Mapping mapping : mappingList) {
				statment.setString(1, mapping.getId());
				statment.setString(2, mapping.getMappingId());
				statment.setString(3, mapping.getMappingGroupId());
				statment.setString(4, mapping.getTargetType());
				statment.setString(5, mapping.getTargetBeanClass());
				statment.setString(6, mapping.getHasScript());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.insertBatch() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql + ";");
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

	public int update(Mapping mapping) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mapping == null) {
			throw new IllegalArgumentException("mapping is NULL!");
		}

		PreparedStatement statment = null;
		if (mapping.getId() != null && mapping.getId().length() < 10) {
			throw new IllegalArgumentException("id too short" + mapping.getId());
		}
		if (mapping.getId() != null && mapping.getId().length() > 10) {
			throw new IllegalArgumentException("id too long" + mapping.getId());
		}
		if (mapping.getMappingId() == null || "".equals(mapping.getMappingId())) {
			throw new IllegalArgumentException("mappingId is null");
		}
		if (mapping.getMappingId() != null
				&& mapping.getMappingId().length() > 255) {
			throw new IllegalArgumentException("mappingId too long"
					+ mapping.getMappingId());
		}
		if (mapping.getMappingGroupId() == null
				|| "".equals(mapping.getMappingGroupId())) {
			throw new IllegalArgumentException("mappingGroupId is null");
		}
		if (mapping.getMappingGroupId() != null
				&& mapping.getMappingGroupId().length() > 255) {
			throw new IllegalArgumentException("mappingGroupId too long"
					+ mapping.getMappingGroupId());
		}
		if (mapping.getTargetType() != null
				&& mapping.getTargetType().length() < 4) {
			throw new IllegalArgumentException("targetType too short"
					+ mapping.getTargetType());
		}
		if (mapping.getTargetType() != null
				&& mapping.getTargetType().length() > 4) {
			throw new IllegalArgumentException("targetType too long"
					+ mapping.getTargetType());
		}
		if (mapping.getTargetBeanClass() != null
				&& mapping.getTargetBeanClass().length() > 255) {
			throw new IllegalArgumentException("targetBeanClass too long"
					+ mapping.getTargetBeanClass());
		}
		if (mapping.getHasScript() != null
				&& mapping.getHasScript().length() < 1) {
			throw new IllegalArgumentException("hasScript too short"
					+ mapping.getHasScript());
		}
		if (mapping.getHasScript() != null
				&& mapping.getHasScript().length() > 1) {
			throw new IllegalArgumentException("hasScript too long"
					+ mapping.getHasScript());
		}
		try {
			String sql = "UPDATE mapping SET id=?,mapping_id=?,mapping_group_id=?,target_type=?,target_bean_class=?,has_script=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, mapping.getId());
			statment.setString(2, mapping.getMappingId());
			statment.setString(3, mapping.getMappingGroupId());
			statment.setString(4, mapping.getTargetType());
			statment.setString(5, mapping.getTargetBeanClass());
			statment.setString(6, mapping.getHasScript());
			statment.setString(7, mapping.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.update() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + mapping.getId()
						+ "\",mapping_id = \"" + mapping.getMappingId()
						+ "\",mapping_group_id = \""
						+ mapping.getMappingGroupId() + "\",target_type = \""
						+ mapping.getTargetType() + "\",target_bean_class = \""
						+ mapping.getTargetBeanClass() + "\",has_script = \""
						+ mapping.getHasScript() + "\" ");
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
			sql.append("UPDATE mapping SET ");
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
				if ("mapping_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("mapping_group_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("target_type".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("target_bean_class".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("has_script".equalsIgnoreCase(tmpKey)) {
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
				sbDebug.append("MappingDAO.dynamicUpdate() spend "
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
			String sql = "DELETE FROM mapping where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.deleteByPK() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
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

	public Mapping selectByPK(String id) {
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
		Mapping returnDTO = null;

		try {
			String sql = "SELECT * FROM mapping where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.selectByPK() spend " + (endTime - startTime)
						+ "ms. SQL:" + sql + "; parameters : id = \"" + id
						+ "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Mapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setMappingGroupId(resultSet
						.getString("mapping_group_id"));
				returnDTO.setTargetType(resultSet.getString("target_type"));
				returnDTO.setTargetBeanClass(resultSet
						.getString("target_bean_class"));
				returnDTO.setHasScript(resultSet.getString("has_script"));
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
		Mapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from mapping order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.findAll() spend " + (endTime - startTime)
						+ "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new Mapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setMappingGroupId(resultSet
						.getString("mapping_group_id"));
				returnDTO.setTargetType(resultSet.getString("target_type"));
				returnDTO.setTargetBeanClass(resultSet
						.getString("target_bean_class"));
				returnDTO.setHasScript(resultSet.getString("has_script"));
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
		Mapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mapping ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from mapping					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,mapping_id,mapping_group_id,target_type,target_bean_class,has_script, rownumber() over(order by id) as rn from mapping ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from mapping order by id limit " + startNum
						+ "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,mapping_id,mapping_group_id,target_type,target_bean_class,has_script from mapping ) A where rownumber > "
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
				debug("MappingDAO.findAll()(pageNum:" + pageNum + ",row count "
						+ pageLength + ") spend " + (endTime - startTime)
						+ "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new Mapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setMappingGroupId(resultSet
						.getString("mapping_group_id"));
				returnDTO.setTargetType(resultSet.getString("target_type"));
				returnDTO.setTargetBeanClass(resultSet
						.getString("target_bean_class"));
				returnDTO.setHasScript(resultSet.getString("has_script"));
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
		Mapping returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from mapping where " + where
					+ " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new Mapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setMappingGroupId(resultSet
						.getString("mapping_group_id"));
				returnDTO.setTargetType(resultSet.getString("target_type"));
				returnDTO.setTargetBeanClass(resultSet
						.getString("target_bean_class"));
				returnDTO.setHasScript(resultSet.getString("has_script"));
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
		Mapping returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from mapping where " + where
						+ " order by id limit " + startNum + "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mapping where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from mapping					 where " + where
						+ " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select id,mapping_id,mapping_group_id,target_type,target_bean_class,has_script, rownumber() over(order by id) as rn from mapping where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select top "
						+ pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,mapping_id,mapping_group_id,target_type,target_bean_class,has_script from mapping  where "
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
				debug("MappingDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new Mapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setMappingGroupId(resultSet
						.getString("mapping_group_id"));
				returnDTO.setTargetType(resultSet.getString("target_type"));
				returnDTO.setTargetBeanClass(resultSet
						.getString("target_bean_class"));
				returnDTO.setHasScript(resultSet.getString("has_script"));
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
			String sql = "select  count(*) from mapping";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.getTotalRecords() spend "
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
			String sql = "select  count(*) from mapping where " + where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("MappingDAO.getTotalRecords() spend "
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

	public List getAllMappingInGroup(String mappingGroupId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (mappingGroupId == null || "".equals(mappingGroupId)) {
			throw new IllegalArgumentException("mappingGroupId is null");
		}
		if (mappingGroupId != null && mappingGroupId.length() > 255) {
			throw new IllegalArgumentException("mappingGroupId too long"
					+ mappingGroupId);
		}
		ResultSet resultSet = null;
		Mapping returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from mapping where mapping_group_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, mappingGroupId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("MappingDAO.getAllMappingInGroup() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : mappingGroupId = " + mappingGroupId);
			}

			while (resultSet.next()) {
				returnDTO = new Mapping();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMappingId(resultSet.getString("mapping_id"));
				returnDTO.setMappingGroupId(resultSet
						.getString("mapping_group_id"));
				returnDTO.setTargetType(resultSet.getString("target_type"));
				returnDTO.setTargetBeanClass(resultSet
						.getString("target_bean_class"));
				returnDTO.setHasScript(resultSet.getString("has_script"));
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
