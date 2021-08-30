package com.fib.msgconverter.commgateway.dao.job.dao;

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
public class JobDAO extends AbstractDAO {

	public JobDAO() {
		super();
	}

	public JobDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public JobDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public void insert(Job job) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (job == null) {
			throw new IllegalArgumentException("job is NULL!");
		}
		if( job.getId() != null && job.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+job.getId());
		}
		if( job.getLogId() != null && job.getLogId().length() > 20 ){
			throw new IllegalArgumentException("logId too long"+job.getLogId());
		}
		if( job.getType() != null && job.getType().length() < 2 ){
			throw new IllegalArgumentException("type too short"+job.getType());
		}
		if( job.getType() != null && job.getType().length() > 2 ){
			throw new IllegalArgumentException("type too long"+job.getType());
		}
		if( job.getState() != null && job.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+job.getState());
		}
		if( job.getState() != null && job.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+job.getState());
		}
		if( job.getStartDate() != null && job.getStartDate().length() < 8 ){
			throw new IllegalArgumentException("startDate too short"+job.getStartDate());
		}
		if( job.getStartDate() != null && job.getStartDate().length() > 8 ){
			throw new IllegalArgumentException("startDate too long"+job.getStartDate());
		}
		if( job.getStartTime() != null && job.getStartTime().length() < 6 ){
			throw new IllegalArgumentException("startTime too short"+job.getStartTime());
		}
		if( job.getStartTime() != null && job.getStartTime().length() > 6 ){
			throw new IllegalArgumentException("startTime too long"+job.getStartTime());
		}
		if( job.getBranchCode() != null && job.getBranchCode().length() > 10 ){
			throw new IllegalArgumentException("branchCode too long"+job.getBranchCode());
		}
		if( job.getAppCategory() != null && job.getAppCategory().length() > 10 ){
			throw new IllegalArgumentException("appCategory too long"+job.getAppCategory());
		}
		if( job.getScheduleType() != null && job.getScheduleType().length() < 2 ){
			throw new IllegalArgumentException("scheduleType too short"+job.getScheduleType());
		}
		if( job.getScheduleType() != null && job.getScheduleType().length() > 2 ){
			throw new IllegalArgumentException("scheduleType too long"+job.getScheduleType());
		}
		if( job.getJobClassName() != null && job.getJobClassName().length() > 1024 ){
			throw new IllegalArgumentException("jobClassName too long"+job.getJobClassName());
		}
		if( job.getProcessId() != null && job.getProcessId().length() > 80 ){
			throw new IllegalArgumentException("processId too long"+job.getProcessId());
		}
		if( job.getScheduleEndFlag() != null && job.getScheduleEndFlag().length() > 4 ){
			throw new IllegalArgumentException("scheduleEndFlag too long"+job.getScheduleEndFlag());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into job(id,log_id,type,state,start_date,start_time,branch_code,app_category,schedule_type,current_times,job_interval,max_times,job_class_name,process_id,request_message_from,schedule_end_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, job.getId());
			statment.setString(2, job.getLogId());
			statment.setString(3, job.getType());
			statment.setString(4, job.getState());
			statment.setString(5, job.getStartDate());
			statment.setString(6, job.getStartTime());
			statment.setString(7, job.getBranchCode());
			statment.setString(8, job.getAppCategory());
			statment.setString(9, job.getScheduleType());
			statment.setInt(10, job.getCurrentTimes());
			statment.setLong(11, job.getJobInterval());
			statment.setInt(12, job.getMaxTimes());
			statment.setString(13, job.getJobClassName());
			statment.setString(14, job.getProcessId());
			statment.setInt(15, job.getRequestMessageFrom());
			statment.setString(16, job.getScheduleEndFlag());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("JobDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+job.getId() +"\",log_id = \""+job.getLogId() +"\",type = \""+job.getType() +"\",state = \""+job.getState() +"\",start_date = \""+job.getStartDate() +"\",start_time = \""+job.getStartTime() +"\",branch_code = \""+job.getBranchCode() +"\",app_category = \""+job.getAppCategory() +"\",schedule_type = \""+job.getScheduleType() +"\",current_times = "+job.getCurrentTimes()+",job_interval = "+job.getJobInterval()+",max_times = "+job.getMaxTimes()+",job_class_name = \""+job.getJobClassName() +"\",process_id = \""+job.getProcessId() +"\",request_message_from = "+job.getRequestMessageFrom()+",schedule_end_flag = \""+job.getScheduleEndFlag() +"\" ");
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


	public void update(Job job) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (job == null) {
			throw new IllegalArgumentException("job is NULL!");
		}

		PreparedStatement statment = null;
		if( job.getId() != null && job.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+job.getId());
		}
		if( job.getLogId() != null && job.getLogId().length() > 20 ){
			throw new IllegalArgumentException("logId too long"+job.getLogId());
		}
		if( job.getType() != null && job.getType().length() < 2 ){
			throw new IllegalArgumentException("type too short"+job.getType());
		}
		if( job.getType() != null && job.getType().length() > 2 ){
			throw new IllegalArgumentException("type too long"+job.getType());
		}
		if( job.getState() != null && job.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+job.getState());
		}
		if( job.getState() != null && job.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+job.getState());
		}
		if( job.getStartDate() != null && job.getStartDate().length() < 8 ){
			throw new IllegalArgumentException("startDate too short"+job.getStartDate());
		}
		if( job.getStartDate() != null && job.getStartDate().length() > 8 ){
			throw new IllegalArgumentException("startDate too long"+job.getStartDate());
		}
		if( job.getStartTime() != null && job.getStartTime().length() < 6 ){
			throw new IllegalArgumentException("startTime too short"+job.getStartTime());
		}
		if( job.getStartTime() != null && job.getStartTime().length() > 6 ){
			throw new IllegalArgumentException("startTime too long"+job.getStartTime());
		}
		if( job.getBranchCode() != null && job.getBranchCode().length() > 10 ){
			throw new IllegalArgumentException("branchCode too long"+job.getBranchCode());
		}
		if( job.getAppCategory() != null && job.getAppCategory().length() > 10 ){
			throw new IllegalArgumentException("appCategory too long"+job.getAppCategory());
		}
		if( job.getScheduleType() != null && job.getScheduleType().length() < 2 ){
			throw new IllegalArgumentException("scheduleType too short"+job.getScheduleType());
		}
		if( job.getScheduleType() != null && job.getScheduleType().length() > 2 ){
			throw new IllegalArgumentException("scheduleType too long"+job.getScheduleType());
		}
		if( job.getJobClassName() != null && job.getJobClassName().length() > 1024 ){
			throw new IllegalArgumentException("jobClassName too long"+job.getJobClassName());
		}
		if( job.getProcessId() != null && job.getProcessId().length() > 80 ){
			throw new IllegalArgumentException("processId too long"+job.getProcessId());
		}
		if( job.getScheduleEndFlag() != null && job.getScheduleEndFlag().length() > 4 ){
			throw new IllegalArgumentException("scheduleEndFlag too long"+job.getScheduleEndFlag());
		}
		try {
			String sql =  "UPDATE job SET id=?,log_id=?,type=?,state=?,start_date=?,start_time=?,branch_code=?,app_category=?,schedule_type=?,current_times=?,job_interval=?,max_times=?,job_class_name=?,process_id=?,request_message_from=?,schedule_end_flag=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, job.getId());
			statment.setString(2, job.getLogId());
			statment.setString(3, job.getType());
			statment.setString(4, job.getState());
			statment.setString(5, job.getStartDate());
			statment.setString(6, job.getStartTime());
			statment.setString(7, job.getBranchCode());
			statment.setString(8, job.getAppCategory());
			statment.setString(9, job.getScheduleType());
			statment.setInt(10, job.getCurrentTimes());
			statment.setLong(11, job.getJobInterval());
			statment.setInt(12, job.getMaxTimes());
			statment.setString(13, job.getJobClassName());
			statment.setString(14, job.getProcessId());
			statment.setInt(15, job.getRequestMessageFrom());
			statment.setString(16, job.getScheduleEndFlag());
			statment.setString(17, job.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("JobDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+job.getId() +"\",log_id = \""+job.getLogId() +"\",type = \""+job.getType() +"\",state = \""+job.getState() +"\",start_date = \""+job.getStartDate() +"\",start_time = \""+job.getStartTime() +"\",branch_code = \""+job.getBranchCode() +"\",app_category = \""+job.getAppCategory() +"\",schedule_type = \""+job.getScheduleType() +"\",current_times = "+job.getCurrentTimes()+",job_interval = "+job.getJobInterval()+",max_times = "+job.getMaxTimes()+",job_class_name = \""+job.getJobClassName() +"\",process_id = \""+job.getProcessId() +"\",request_message_from = "+job.getRequestMessageFrom()+",schedule_end_flag = \""+job.getScheduleEndFlag() +"\" ");
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
			sql.append("UPDATE job SET ");
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
				if("id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("log_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("state".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("start_date".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("start_time".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("branch_code".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("app_category".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("schedule_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("current_times".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("job_interval".equalsIgnoreCase(tmpKey)){
					statment.setLong(m++, (Long)updateFields.get(tmpKey));
				}
				if("max_times".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("job_class_name".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("process_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("request_message_from".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("schedule_end_flag".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("JobDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM job where id=? ";
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
				debug("JobDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public Job selectByPK (String id)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		ResultSet resultSet = null;
		Job returnDTO = null;

		try {
			String sql =  "SELECT * FROM job where id=? ";
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
				debug("JobDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Job();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setScheduleType(resultSet.getString("schedule_type"));
				returnDTO.setCurrentTimes(resultSet.getInt("current_times"));
				returnDTO.setJobInterval(resultSet.getLong("job_interval"));
				returnDTO.setMaxTimes(resultSet.getInt("max_times"));
				returnDTO.setJobClassName(resultSet.getString("job_class_name"));
				returnDTO.setProcessId(resultSet.getString("process_id"));
				returnDTO.setRequestMessageFrom(resultSet.getInt("request_message_from"));
				returnDTO.setScheduleEndFlag(resultSet.getString("schedule_end_flag"));
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
		Job returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from job order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Job();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setScheduleType(resultSet.getString("schedule_type"));
				returnDTO.setCurrentTimes(resultSet.getInt("current_times"));
				returnDTO.setJobInterval(resultSet.getLong("job_interval"));
				returnDTO.setMaxTimes(resultSet.getInt("max_times"));
				returnDTO.setJobClassName(resultSet.getString("job_class_name"));
				returnDTO.setProcessId(resultSet.getString("process_id"));
				returnDTO.setRequestMessageFrom(resultSet.getInt("request_message_from"));
				returnDTO.setScheduleEndFlag(resultSet.getString("schedule_end_flag"));
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
		Job returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from job ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from job					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,log_id,type,state,start_date,start_time,branch_code,app_category,schedule_type,current_times,job_interval,max_times,job_class_name,process_id,request_message_from,schedule_end_flag, rownumber() over(order by id) as rn from job ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from job order by id limit "+startNum+","+pageLength;
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
				debug("JobDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Job();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setScheduleType(resultSet.getString("schedule_type"));
				returnDTO.setCurrentTimes(resultSet.getInt("current_times"));
				returnDTO.setJobInterval(resultSet.getLong("job_interval"));
				returnDTO.setMaxTimes(resultSet.getInt("max_times"));
				returnDTO.setJobClassName(resultSet.getString("job_class_name"));
				returnDTO.setProcessId(resultSet.getString("process_id"));
				returnDTO.setRequestMessageFrom(resultSet.getInt("request_message_from"));
				returnDTO.setScheduleEndFlag(resultSet.getString("schedule_end_flag"));
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
		Job returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from job where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Job();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setScheduleType(resultSet.getString("schedule_type"));
				returnDTO.setCurrentTimes(resultSet.getInt("current_times"));
				returnDTO.setJobInterval(resultSet.getLong("job_interval"));
				returnDTO.setMaxTimes(resultSet.getInt("max_times"));
				returnDTO.setJobClassName(resultSet.getString("job_class_name"));
				returnDTO.setProcessId(resultSet.getString("process_id"));
				returnDTO.setRequestMessageFrom(resultSet.getInt("request_message_from"));
				returnDTO.setScheduleEndFlag(resultSet.getString("schedule_end_flag"));
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
		Job returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from job where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from job where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from job					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,log_id,type,state,start_date,start_time,branch_code,app_category,schedule_type,current_times,job_interval,max_times,job_class_name,process_id,request_message_from,schedule_end_flag, rownumber() over(order by id) as rn from job where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
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
				debug("JobDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Job();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setScheduleType(resultSet.getString("schedule_type"));
				returnDTO.setCurrentTimes(resultSet.getInt("current_times"));
				returnDTO.setJobInterval(resultSet.getLong("job_interval"));
				returnDTO.setMaxTimes(resultSet.getInt("max_times"));
				returnDTO.setJobClassName(resultSet.getString("job_class_name"));
				returnDTO.setProcessId(resultSet.getString("process_id"));
				returnDTO.setRequestMessageFrom(resultSet.getInt("request_message_from"));
				returnDTO.setScheduleEndFlag(resultSet.getString("schedule_end_flag"));
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
			String sql =  "select  count(*) from job";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from job where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("JobDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getJobByState(String state ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( state != null && state.length() < 2 ){
			throw new IllegalArgumentException("state too short"+state);
		}
		if( state != null && state.length() > 2 ){
			throw new IllegalArgumentException("state too long"+state);
		}
		ResultSet resultSet = null;
		Job returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from job where state=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,state);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("JobDAO.getJobByState() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : state = " + state);
			}


			while (resultSet.next()) {
				returnDTO = new Job();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLogId(resultSet.getString("log_id"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setBranchCode(resultSet.getString("branch_code"));
				returnDTO.setAppCategory(resultSet.getString("app_category"));
				returnDTO.setScheduleType(resultSet.getString("schedule_type"));
				returnDTO.setCurrentTimes(resultSet.getInt("current_times"));
				returnDTO.setJobInterval(resultSet.getLong("job_interval"));
				returnDTO.setMaxTimes(resultSet.getInt("max_times"));
				returnDTO.setJobClassName(resultSet.getString("job_class_name"));
				returnDTO.setProcessId(resultSet.getString("process_id"));
				returnDTO.setRequestMessageFrom(resultSet.getInt("request_message_from"));
				returnDTO.setScheduleEndFlag(resultSet.getString("schedule_end_flag"));
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

	public void updateJobState(String state ,String id ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( state != null && state.length() < 2 ){
			throw new IllegalArgumentException("state too short"+state);
		}
		if( state != null && state.length() > 2 ){
			throw new IllegalArgumentException("state too long"+state);
		}
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		try {
			String sql = "update job set state=? where id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,state);
			statment.setString(2,id);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("JobDAOupdateJobState() spend "+(_endTime - _startTime)+"ms. retFlag = "+retFlag+" SQL:"+sql+"; parameter : state = " + state+",id = " + id);
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	public void updateCurrentTimes(int currentTimes ,String id ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		try {
			String sql = "update job set current_times=? where id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setInt(1,currentTimes);
			statment.setString(2,id);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("JobDAOupdateCurrentTimes() spend "+(_endTime - _startTime)+"ms. retFlag = "+retFlag+" SQL:"+sql+"; parameter : currentTimes = " + currentTimes+",id = " + id);
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
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

}
