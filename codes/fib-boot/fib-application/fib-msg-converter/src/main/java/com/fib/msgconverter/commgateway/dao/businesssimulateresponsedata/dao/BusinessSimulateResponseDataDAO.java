package com.fib.msgconverter.commgateway.dao.businesssimulateresponsedata.dao;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.giantstone.base.dao.AbstractDAO;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.base.config.DAOConfiguration;

public class BusinessSimulateResponseDataDAO extends AbstractDAO {

	public BusinessSimulateResponseDataDAO() {
		super();
	}

	public BusinessSimulateResponseDataDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public BusinessSimulateResponseDataDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(BusinessSimulateResponseData businessSimulateResponseData) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (businessSimulateResponseData == null) {
			throw new IllegalArgumentException("businessSimulateResponseData is NULL!");
		}
		if (businessSimulateResponseData.getId() != null && businessSimulateResponseData.getId().length() > 255) {
			throw new IllegalArgumentException("id too long" + businessSimulateResponseData.getId());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into business_simulate_response_data(id,message_data) values(?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, businessSimulateResponseData.getId());
			statment.setBytes(2, businessSimulateResponseData.getMessageData());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.insert() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + "; parameters : id = \"" + businessSimulateResponseData.getId()
						+ "\",message_data = " + businessSimulateResponseData.getMessageData() + " ");
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

	public int[] insertBatch(List<BusinessSimulateResponseData> businessSimulateResponseDataList) {
		// 获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		// 对输入参数的合法性进行效验
		if (businessSimulateResponseDataList == null) {
			throw new IllegalArgumentException("businessSimulateResponseDataList is NULL!");
		}
		for (BusinessSimulateResponseData businessSimulateResponseData : businessSimulateResponseDataList) {
			if (businessSimulateResponseData.getId() != null && businessSimulateResponseData.getId().length() > 255) {
				throw new IllegalArgumentException("id too long" + businessSimulateResponseData.getId());
			}
		}
		PreparedStatement statment = null;
		try {
			String sql = "insert into business_simulate_response_data(id,message_data) values(?,?)";
			statment = conn.prepareStatement(sql);
			for (BusinessSimulateResponseData businessSimulateResponseData : businessSimulateResponseDataList) {
				statment.setString(1, businessSimulateResponseData.getId());
				statment.setBytes(2, businessSimulateResponseData.getMessageData());

				statment.addBatch();
			}
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.insertBatch() spend " + (endTime - startTime) + "ms. retFlag = "
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

	public int update(BusinessSimulateResponseData businessSimulateResponseData) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (businessSimulateResponseData == null) {
			throw new IllegalArgumentException("businessSimulateResponseData is NULL!");
		}

		PreparedStatement statment = null;
		if (businessSimulateResponseData.getId() != null && businessSimulateResponseData.getId().length() > 255) {
			throw new IllegalArgumentException("id too long" + businessSimulateResponseData.getId());
		}
		try {
			String sql = "UPDATE business_simulate_response_data SET id=?,message_data=? where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, businessSimulateResponseData.getId());
			statment.setBytes(2, businessSimulateResponseData.getMessageData());
			statment.setString(3, businessSimulateResponseData.getId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.update() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + "; parameters : id = \"" + businessSimulateResponseData.getId()
						+ "\",message_data = " + businessSimulateResponseData.getMessageData() + " ");
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

	public int dynamicUpdate(Map<String,String> primaryKey, Map<String,Object> updateFields) {
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
		if (!primaryKey.containsKey("id")) {
			throw new IllegalArgumentException("id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE business_simulate_response_data SET ");
			Iterator<String> it = updateFields.keySet().iterator();
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
				if ("message_data".equalsIgnoreCase(tmpKey)) {
					statment.setBytes(m++, (byte[]) updateFields.get(tmpKey));
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
				sbDebug.append("BusinessSimulateResponseDataDAO.dynamicUpdate() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql.toString() + "; parameters : ");
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

	public int deleteByPK(String id) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is NULL!");
		}

		PreparedStatement statment = null;
		if (id != null && id.length() > 255) {
			throw new IllegalArgumentException("id too long" + id);
		}

		try {
			String sql = "DELETE FROM business_simulate_response_data where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.deleteByPK() spend " + (endTime - startTime) + "ms. retFlag = "
						+ retFlag + " SQL:" + sql + "; parameters : id = \"" + id + "\" ");
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

	public BusinessSimulateResponseData selectByPK(String id) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (id != null && id.length() > 255) {
			throw new IllegalArgumentException("id too long" + id);
		}
		ResultSet resultSet = null;
		BusinessSimulateResponseData returnDTO = null;

		try {
			String sql = "SELECT * FROM business_simulate_response_data where id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, id);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.selectByPK() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : id = \"" + id + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new BusinessSimulateResponseData();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageData(resultSet.getBytes("message_data"));
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

	public List<BusinessSimulateResponseData> findAll() {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		BusinessSimulateResponseData returnDTO = null;
		List<BusinessSimulateResponseData> list = new ArrayList<>();

		try {
			String sql = "select * from business_simulate_response_data order by id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.findAll() spend " + (endTime - startTime) + "ms. SQL:" + sql
						+ "; ");
			}
			while (resultSet.next()) {
				returnDTO = new BusinessSimulateResponseData();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageData(resultSet.getBytes("message_data"));
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

	public List<BusinessSimulateResponseData> findAll(int pageNum, int pageLength) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		BusinessSimulateResponseData returnDTO = null;
		List<BusinessSimulateResponseData> list = new ArrayList<>();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from business_simulate_response_data ) A where rownum <= "
						+ endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum + " * from (select Top " + endNum
						+ " * from business_simulate_response_data					 ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select id,message_data, rownumber() over(order by id) as rn from business_simulate_response_data ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from business_simulate_response_data order by id limit " + startNum + "," + pageLength;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select top " + pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,message_data from business_simulate_response_data ) A where rownumber > "
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
				debug("BusinessSimulateResponseDataDAO.findAll()(pageNum:" + pageNum + ",row count " + pageLength
						+ ") spend " + (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new BusinessSimulateResponseData();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageData(resultSet.getBytes("message_data"));
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

	public List<BusinessSimulateResponseData> findByWhere(String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		BusinessSimulateResponseData returnDTO = null;
		List<BusinessSimulateResponseData> list = new ArrayList<>();

		try {
			String sql = "select * from business_simulate_response_data where " + where + " order by id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.findByWhere()() spend " + (endTime - startTime) + "ms. SQL:"
						+ sql + "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new BusinessSimulateResponseData();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageData(resultSet.getBytes("message_data"));
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

	public List<BusinessSimulateResponseData> findByWhere(String where, int pageNum, int pageLength) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		BusinessSimulateResponseData returnDTO = null;
		List<BusinessSimulateResponseData> list = new ArrayList<>();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from business_simulate_response_data where " + where + " order by id limit " + startNum
						+ "," + pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from business_simulate_response_data where  "
						+ where + " ) A where rownum <= " + endNum + " ) where RN >=" + startNum + " order by id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum + " * from (select Top " + endNum
						+ " * from business_simulate_response_data					 where " + where
						+ " ) t1)t2 order by id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from ( select id,message_data, rownumber() over(order by id) as rn from business_simulate_response_data where "
						+ where + " ) as a1 where a1.rn between " + startNum + " and " + endNum;
			} else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select top " + pageLength
						+ " * from(select row_number() over (order by id) as rownumber, id,message_data from business_simulate_response_data  where "
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
				debug("BusinessSimulateResponseDataDAO.findByWhere()(pageNum:" + pageNum + ",row count " + pageLength
						+ ") spend " + (endTime - startTime) + "ms. SQL:" + sql + "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new BusinessSimulateResponseData();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageData(resultSet.getBytes("message_data"));
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
			String sql = "select  count(*) from business_simulate_response_data";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.getTotalRecords() spend " + (endTime - startTime) + "ms. SQL:"
						+ sql + "; ");
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
			String sql = "select  count(*) from business_simulate_response_data where " + where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("BusinessSimulateResponseDataDAO.getTotalRecords() spend " + (endTime - startTime) + "ms. SQL:"
						+ sql + "; ");
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
