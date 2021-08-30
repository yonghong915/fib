package com.fib.msgconverter.commgateway.dao.joblog.dao;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.giantstone.base.dao.AbstractDAO;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.base.config.DAOConfiguration;



@SuppressWarnings("all")
public class JobLogDAO extends AbstractDAO {

	public JobLogDAO() {
		super();
	}

	public JobLogDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public JobLogDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public void insert(JobLog jobLog) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (jobLog == null) {
			throw new IllegalArgumentException("jobLog is NULL!");
		}
		if( jobLog.getJobId() != null && jobLog.getJobId().length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+jobLog.getJobId());
		}
		if( jobLog.getId() != null && jobLog.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+jobLog.getId());
		}
		if( jobLog.getExecuteDate() != null && jobLog.getExecuteDate().length() < 8 ){
			throw new IllegalArgumentException("executeDate too short"+jobLog.getExecuteDate());
		}
		if( jobLog.getExecuteDate() != null && jobLog.getExecuteDate().length() > 8 ){
			throw new IllegalArgumentException("executeDate too long"+jobLog.getExecuteDate());
		}
		if( jobLog.getExecuteTime() != null && jobLog.getExecuteTime().length() < 6 ){
			throw new IllegalArgumentException("executeTime too short"+jobLog.getExecuteTime());
		}
		if( jobLog.getExecuteTime() != null && jobLog.getExecuteTime().length() > 6 ){
			throw new IllegalArgumentException("executeTime too long"+jobLog.getExecuteTime());
		}
		if( jobLog.getState() != null && jobLog.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+jobLog.getState());
		}
		if( jobLog.getState() != null && jobLog.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+jobLog.getState());
		}
		if( jobLog.getLogId() != null && jobLog.getLogId().length() > 20 ){
			throw new IllegalArgumentException("logId too long"+jobLog.getLogId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into job_log(job_id,id,execute_date,execute_time,state,log_id) values(?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, jobLog.getJobId());
			statment.setString(2, jobLog.getId());
			statment.setString(3, jobLog.getExecuteDate());
			statment.setString(4, jobLog.getExecuteTime());
			statment.setString(5, jobLog.getState());
			statment.setString(6, jobLog.getLogId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("JobLogDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : job_id = \""+jobLog.getJobId() +"\",id = \""+jobLog.getId() +"\",execute_date = \""+jobLog.getExecuteDate() +"\",execute_time = \""+jobLog.getExecuteTime() +"\",state = \""+jobLog.getState() +"\",log_id = \""+jobLog.getLogId() +"\" ");
			}
			if( retFlag == 0){
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


	public void update(JobLog jobLog) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (jobLog == null) {
			throw new IllegalArgumentException("jobLog is NULL!");
		}

		PreparedStatement statment = null;
		if( jobLog.getJobId() != null && jobLog.getJobId().length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+jobLog.getJobId());
		}
		if( jobLog.getId() != null && jobLog.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+jobLog.getId());
		}
		if( jobLog.getExecuteDate() != null && jobLog.getExecuteDate().length() < 8 ){
			throw new IllegalArgumentException("executeDate too short"+jobLog.getExecuteDate());
		}
		if( jobLog.getExecuteDate() != null && jobLog.getExecuteDate().length() > 8 ){
			throw new IllegalArgumentException("executeDate too long"+jobLog.getExecuteDate());
		}
		if( jobLog.getExecuteTime() != null && jobLog.getExecuteTime().length() < 6 ){
			throw new IllegalArgumentException("executeTime too short"+jobLog.getExecuteTime());
		}
		if( jobLog.getExecuteTime() != null && jobLog.getExecuteTime().length() > 6 ){
			throw new IllegalArgumentException("executeTime too long"+jobLog.getExecuteTime());
		}
		if( jobLog.getState() != null && jobLog.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+jobLog.getState());
		}
		if( jobLog.getState() != null && jobLog.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+jobLog.getState());
		}
		if( jobLog.getLogId() != null && jobLog.getLogId().length() > 20 ){
			throw new IllegalArgumentException("logId too long"+jobLog.getLogId());
		}
		try {
			String sql =  "UPDATE job_log SET job_id=?,id=?,execute_date=?,execute_time=?,state=?,log_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, jobLog.getJobId());
			statment.setString(2, jobLog.getId());
			statment.setString(3, jobLog.getExecuteDate());
			statment.setString(4, jobLog.getExecuteTime());
			statment.setString(5, jobLog.getState());
			statment.setString(6, jobLog.getLogId());
			statment.setString(7, jobLog.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("JobLogDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : job_id = \""+jobLog.getJobId() +"\",id = \""+jobLog.getId() +"\",execute_date = \""+jobLog.getExecuteDate() +"\",execute_time = \""+jobLog.getExecuteTime() +"\",state = \""+jobLog.getState() +"\",log_id = \""+jobLog.getLogId() +"\" ");
			}
			if( retFlag == 0){
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
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (primaryKey == null) {
			throw new IllegalArgumentException( "primaryKey is NULL!");
		}
		if (updateFields == null) {
			throw new IllegalArgumentException( "updateFields is NULL!");
		}

		PreparedStatement statment = null;
		if(!primaryKey.containsKey("id") ){
			throw new IllegalArgumentException("id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE job_log SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("job_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("execute_date".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("execute_time".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("state".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("log_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("JobLogDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
				Iterator priIt = updateFields.keySet().iterator();
				while ( priIt.hasNext() ){
					tmpKey = (String)priIt.next();
					tmpStr = (String)updateFields.get(tmpKey);
					sbDebug.append(tmpKey);
					sbDebug.append(" = ");
					sbDebug.append(tmpStr);
					sbDebug.append(", ");
				}
				sbDebug.append("primaryKey :");
				priIt = primaryKey.keySet().iterator();
				while (priIt.hasNext()){
					tmpKey = (String)priIt.next();
					tmpStr = (String)primaryKey.get(tmpKey);
					sbDebug.append(tmpKey);
					sbDebug.append(" = ");
					sbDebug.append(tmpStr);
					sbDebug.append(", ");
				}
				debug(sbDebug.toString());
			}
			if(retFlag == 0){
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


	public void deleteByPK(String id) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}

		try {
			String sql = "DELETE FROM job_log where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,id);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("JobLogDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if( retFlag == 0){
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


	public JobLog selectByPK (String id)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		ResultSet resultSet = null;
		JobLog returnDTO = null;

		try {
			String sql =  "SELECT * FROM job_log where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,id);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("JobLogDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new JobLog();
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setExecuteDate(resultSet.getString("execute_date"));
				returnDTO.setExecuteTime(resultSet.getString("execute_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setLogId(resultSet.getString("log_id"));
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


	public List findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		JobLog returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from job_log order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobLogDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new JobLog();
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setExecuteDate(resultSet.getString("execute_date"));
				returnDTO.setExecuteTime(resultSet.getString("execute_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setLogId(resultSet.getString("log_id"));
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


	public List findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		JobLog returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from job_log ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from job_log					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select job_id,id,execute_date,execute_time,state,log_id, rownumber() over(order by id) as rn from job_log ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from job_log order by id limit "+startNum+","+pageLength;
			}
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobLogDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new JobLog();
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setExecuteDate(resultSet.getString("execute_date"));
				returnDTO.setExecuteTime(resultSet.getString("execute_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setLogId(resultSet.getString("log_id"));
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


	public List findByWhere (String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		JobLog returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from job_log where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobLogDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new JobLog();
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setExecuteDate(resultSet.getString("execute_date"));
				returnDTO.setExecuteTime(resultSet.getString("execute_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setLogId(resultSet.getString("log_id"));
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


	public List findByWhere (String where, int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		JobLog returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from job_log where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from job_log where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from job_log					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select job_id,id,execute_date,execute_time,state,log_id, rownumber() over(order by id) as rn from job_log where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}

			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobLogDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new JobLog();
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setExecuteDate(resultSet.getString("execute_date"));
				returnDTO.setExecuteTime(resultSet.getString("execute_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setLogId(resultSet.getString("log_id"));
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


	public long getTotalRecords(){
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}


		PreparedStatement statment = null;
		ResultSet resultSet = null;
		long totalRecords = 0;

		try {
			String sql =  "select  count(*) from job_log";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobLogDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}

			 if(resultSet.next()){
					totalRecords=resultSet.getInt(1);
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


	public long getTotalRecordsByWhere(String where){
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
			String sql =  "select  count(*) from job_log where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobLogDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}

			 if(resultSet.next()){
					totalRecords=resultSet.getInt(1);
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


	public List getJobByJobId(String jobId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( jobId != null && jobId.length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+jobId);
		}
		ResultSet resultSet = null;
		JobLog returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from job_log where job_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,jobId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("JobLogDAO.getJobByJobId() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : jobId = " + jobId);
			}


			while (resultSet.next()) {
				returnDTO = new JobLog();
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setExecuteDate(resultSet.getString("execute_date"));
				returnDTO.setExecuteTime(resultSet.getString("execute_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setLogId(resultSet.getString("log_id"));
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
