package com.fib.msgconverter.commgateway.dao.serialnumber.dao;

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

@SuppressWarnings("all")
public class SerialNumberDAO extends AbstractDAO {

	public SerialNumberDAO() {
		super();
	}

	public SerialNumberDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public SerialNumberDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public void insert(SerialNumber serialNumber) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (serialNumber == null) {
			throw new IllegalArgumentException("serialNumber is NULL!");
		}
		if (serialNumber.getSnId() != null
				&& serialNumber.getSnId().length() > 20) {
			throw new IllegalArgumentException("snId too long"
					+ serialNumber.getSnId());
		}
		if (serialNumber.getCurNum() != null
				&& serialNumber.getCurNum().length() > 30) {
			throw new IllegalArgumentException("curNum too long"
					+ serialNumber.getCurNum());
		}
		if (serialNumber.getMaxNum() != null
				&& serialNumber.getMaxNum().length() > 30) {
			throw new IllegalArgumentException("maxNum too long"
					+ serialNumber.getMaxNum());
		}
		if (serialNumber.getMemo() != null
				&& serialNumber.getMemo().length() > 80) {
			throw new IllegalArgumentException("memo too long"
					+ serialNumber.getMemo());
		}

		PreparedStatement statment = null;
		try {
			String sql = "insert into serial_number(sn_id,cur_num,increment,max_num,batch_size,memo) values(?,?,?,?,?,?)";

			statment = conn.prepareStatement(sql);
			statment.setString(1, serialNumber.getSnId());
			statment.setString(2, serialNumber.getCurNum());
			statment.setInt(3, serialNumber.getIncrement());
			statment.setString(4, serialNumber.getMaxNum());
			statment.setInt(5, serialNumber.getBatchSize());
			statment.setString(6, serialNumber.getMemo());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.insert() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : sn_id = \"" + serialNumber.getSnId()
						+ "\",cur_num = \"" + serialNumber.getCurNum()
						+ "\",increment = " + serialNumber.getIncrement()
						+ ",max_num = \"" + serialNumber.getMaxNum()
						+ "\",batch_size = " + serialNumber.getBatchSize()
						+ ",memo = \"" + serialNumber.getMemo() + "\" ");
			}
			if (retFlag == 0) {
				throw new RuntimeException("insert failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}

	public void update(SerialNumber serialNumber) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (serialNumber == null) {
			throw new IllegalArgumentException("serialNumber is NULL!");
		}

		PreparedStatement statment = null;
		if (serialNumber.getSnId() != null
				&& serialNumber.getSnId().length() > 20) {
			throw new IllegalArgumentException("snId too long"
					+ serialNumber.getSnId());
		}
		if (serialNumber.getCurNum() != null
				&& serialNumber.getCurNum().length() > 30) {
			throw new IllegalArgumentException("curNum too long"
					+ serialNumber.getCurNum());
		}
		if (serialNumber.getMaxNum() != null
				&& serialNumber.getMaxNum().length() > 30) {
			throw new IllegalArgumentException("maxNum too long"
					+ serialNumber.getMaxNum());
		}
		if (serialNumber.getMemo() != null
				&& serialNumber.getMemo().length() > 80) {
			throw new IllegalArgumentException("memo too long"
					+ serialNumber.getMemo());
		}
		try {
			String sql = "UPDATE serial_number SET sn_id=?,cur_num=?,increment=?,max_num=?,batch_size=?,memo=? where sn_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, serialNumber.getSnId());
			statment.setString(2, serialNumber.getCurNum());
			statment.setInt(3, serialNumber.getIncrement());
			statment.setString(4, serialNumber.getMaxNum());
			statment.setInt(5, serialNumber.getBatchSize());
			statment.setString(6, serialNumber.getMemo());
			statment.setString(7, serialNumber.getSnId());

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.update() spend " + (endTime - startTime)
						+ "ms. retFlag = " + retFlag + " SQL:" + sql
						+ "; parameters : sn_id = \"" + serialNumber.getSnId()
						+ "\",cur_num = \"" + serialNumber.getCurNum()
						+ "\",increment = " + serialNumber.getIncrement()
						+ ",max_num = \"" + serialNumber.getMaxNum()
						+ "\",batch_size = " + serialNumber.getBatchSize()
						+ ",memo = \"" + serialNumber.getMemo() + "\" ");
			}
			if (retFlag == 0) {
				throw new RuntimeException("update failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}

	public void dynamicUpdate(Map primaryKey, Map updateFields) {
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
		if (!primaryKey.containsKey("sn_id")) {
			throw new IllegalArgumentException("sn_id is null ");
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE serial_number SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()) {
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where sn_id=? ");
			statment = conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()) {
				tmpKey = (String) it.next();
				if ("sn_id".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("cur_num".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("increment".equalsIgnoreCase(tmpKey)) {
					statment.setInt(m++, (Integer) updateFields.get(tmpKey));
				}
				if ("max_num".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
				if ("batch_size".equalsIgnoreCase(tmpKey)) {
					statment.setInt(m++, (Integer) updateFields.get(tmpKey));
				}
				if ("memo".equalsIgnoreCase(tmpKey)) {
					statment.setString(m++, (String) updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String) primaryKey.get("sn_id"));

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("SerialNumberDAO.dynamicUpdate() spend "
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
			if (retFlag == 0) {
				throw new RuntimeException("update failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}

	public void deleteByPK(String snId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (snId == null) {
			throw new IllegalArgumentException("snId is NULL!");
		}

		PreparedStatement statment = null;
		if (snId != null && snId.length() > 20) {
			throw new IllegalArgumentException("snId too long" + snId);
		}

		try {
			String sql = "DELETE FROM serial_number where sn_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, snId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();

			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.deleteByPK() spend "
						+ (endTime - startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameters : sn_id = \"" + snId
						+ "\" ");
			}
			if (retFlag == 0) {
				throw new RuntimeException("delete failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}

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

	}

	public SerialNumber selectByPK(String snId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (snId != null && snId.length() > 20) {
			throw new IllegalArgumentException("snId too long" + snId);
		}
		ResultSet resultSet = null;
		SerialNumber returnDTO = null;

		try {
			String sql = "SELECT * FROM serial_number where sn_id=? ";
			statment = conn.prepareStatement(sql);
			statment.setString(1, snId);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.selectByPK() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameters : sn_id = \"" + snId + "\" ");
			}
			if (resultSet.next()) {
				returnDTO = new SerialNumber();
				returnDTO.setSnId(resultSet.getString("sn_id"));
				returnDTO.setCurNum(resultSet.getString("cur_num"));
				returnDTO.setIncrement(resultSet.getInt("increment"));
				returnDTO.setMaxNum(resultSet.getString("max_num"));
				returnDTO.setBatchSize(resultSet.getInt("batch_size"));
				returnDTO.setMemo(resultSet.getString("memo"));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
		SerialNumber returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from serial_number order by sn_id";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.findAll() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new SerialNumber();
				returnDTO.setSnId(resultSet.getString("sn_id"));
				returnDTO.setCurNum(resultSet.getString("cur_num"));
				returnDTO.setIncrement(resultSet.getInt("increment"));
				returnDTO.setMaxNum(resultSet.getString("max_num"));
				returnDTO.setBatchSize(resultSet.getInt("batch_size"));
				returnDTO.setMemo(resultSet.getString("memo"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
		SerialNumber returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from serial_number ) A where rownum <= "
						+ endNum
						+ " ) where RN >="
						+ startNum
						+ " order by sn_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from serial_number					 ) t1)t2 order by sn_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select sn_id,cur_num,increment,max_num,batch_size,memo, rownumber() over(order by sn_id) as rn from serial_number ) as a1 where a1.rn between "
						+ startNum + " and " + endNum;
			} else if ("mysql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from serial_number order by sn_id limit "
						+ startNum + "," + pageLength;
			}
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.findAll()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}
			while (resultSet.next()) {
				returnDTO = new SerialNumber();
				returnDTO.setSnId(resultSet.getString("sn_id"));
				returnDTO.setCurNum(resultSet.getString("cur_num"));
				returnDTO.setIncrement(resultSet.getInt("increment"));
				returnDTO.setMaxNum(resultSet.getString("max_num"));
				returnDTO.setBatchSize(resultSet.getInt("batch_size"));
				returnDTO.setMemo(resultSet.getString("memo"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
		SerialNumber returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from serial_number where " + where
					+ " order by sn_id";
			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.findByWhere()() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new SerialNumber();
				returnDTO.setSnId(resultSet.getString("sn_id"));
				returnDTO.setCurNum(resultSet.getString("cur_num"));
				returnDTO.setIncrement(resultSet.getInt("increment"));
				returnDTO.setMaxNum(resultSet.getString("max_num"));
				returnDTO.setBatchSize(resultSet.getInt("batch_size"));
				returnDTO.setMemo(resultSet.getString("memo"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
		SerialNumber returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum - 1) * pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength - 1;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())) {
				sql = "select * from serial_number where " + where
						+ " order by sn_id limit " + startNum + ","
						+ pageLength;
			} else if ("oracle".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from(select A.*,ROWNUM RN from ( select * from serial_number where  "
						+ where
						+ " ) A where rownum <= "
						+ endNum
						+ " ) where RN >=" + startNum + " order by sn_id";
			} else if ("mssql".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select Top " + startNum
						+ " * from (select Top " + endNum
						+ " * from serial_number					 where " + where
						+ " ) t1)t2 order by sn_id";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from ( select sn_id,cur_num,increment,max_num,batch_size,memo, rownumber() over(order by sn_id) as rn from serial_number where "
						+ where
						+ " ) as a1 where a1.rn between "
						+ startNum
						+ " and " + endNum;
			}

			statment = conn.prepareStatement(sql);

			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.findByWhere()(pageNum:" + pageNum
						+ ",row count " + pageLength + ") spend "
						+ (endTime - startTime) + "ms. SQL:" + sql
						+ "; parameter : " + where);
			}
			while (resultSet.next()) {
				returnDTO = new SerialNumber();
				returnDTO.setSnId(resultSet.getString("sn_id"));
				returnDTO.setCurNum(resultSet.getString("cur_num"));
				returnDTO.setIncrement(resultSet.getInt("increment"));
				returnDTO.setMaxNum(resultSet.getString("max_num"));
				returnDTO.setBatchSize(resultSet.getInt("batch_size"));
				returnDTO.setMemo(resultSet.getString("memo"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
			String sql = "select  count(*) from serial_number";
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.getTotalRecords() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}

			if (resultSet.next()) {
				totalRecords = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
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
			String sql = "select  count(*) from serial_number where " + where;
			statment = conn.prepareStatement(sql);
			long startTime = 0, endTime = 0;
			if (DAOConfiguration.DEBUG) {
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.getTotalRecords() spend "
						+ (endTime - startTime) + "ms. SQL:" + sql + "; ");
			}

			if (resultSet.next()) {
				totalRecords = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
				} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return totalRecords;
	}

	public List<SerialNumber> getSerialNumberForUpdate(String snId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (snId != null && snId.length() > 20) {
			throw new IllegalArgumentException("snId too long" + snId);
		}
		ResultSet resultSet = null;
		SerialNumber returnDTO = null;
		List list = new ArrayList();
		try {
			// String sql =
			// "select * from serial_number where sn_id=? for update";
			String sql = null;
			if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())
					|| "sqlserver2005".equalsIgnoreCase(DAOConfiguration
							.getDATABASE_TYPE())) {
				sql = "select * from serial_number with (rowlock) where sn_id=?";
			} else if ("db2".equalsIgnoreCase(DAOConfiguration
					.getDATABASE_TYPE())) {
				sql = "select * from serial_number where sn_id=? WITH RS USE AND KEEP UPDATE LOCKS";
			} else {
				sql = "select * from serial_number where sn_id=? for update";
			}
			statment = conn.prepareStatement(sql);

			statment.setString(1, snId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("SerialNumberDAO.getSerialNumberForUpdate() spend "
						+ (_endTime - _startTime) + "ms. SQL:" + sql
						+ "; parameter : snId = " + snId);
			}

			while (resultSet.next()) {
				returnDTO = new SerialNumber();
				returnDTO.setSnId(resultSet.getString("sn_id"));
				returnDTO.setCurNum(resultSet.getString("cur_num"));
				returnDTO.setIncrement(resultSet.getInt("increment"));
				returnDTO.setMaxNum(resultSet.getString("max_num"));
				returnDTO.setBatchSize(resultSet.getInt("batch_size"));
				returnDTO.setMemo(resultSet.getString("memo"));
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

	public void updateSerialNumber(String curNum, String snId) {
		Connection conn = getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if (curNum != null && curNum.length() > 30) {
			throw new IllegalArgumentException("curNum too long" + curNum);
		}
		if (snId != null && snId.length() > 20) {
			throw new IllegalArgumentException("snId too long" + snId);
		}
		try {
			String sql = "update serial_number set cur_num=? where sn_id=?";
			statment = conn.prepareStatement(sql);

			statment.setString(1, curNum);
			statment.setString(2, snId);
			long _startTime = 0, _endTime = 0;
			if (DAOConfiguration.DEBUG) {
				_startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if (DAOConfiguration.DEBUG) {
				_endTime = System.currentTimeMillis();
				debug("SerialNumberDAOupdateSerialNumber() spend "
						+ (_endTime - _startTime) + "ms. retFlag = " + retFlag
						+ " SQL:" + sql + "; parameter : curNum = " + curNum
						+ ",snId = " + snId);
			}
			if (retFlag == 0) {
				throw new RuntimeException("update failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

}
