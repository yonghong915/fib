package com.fib.msgconverter.commgateway.dao.cronrule.dao;

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



public class CronRuleDAO extends AbstractDAO {

	public CronRuleDAO() {
		super();
	}

	public CronRuleDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public CronRuleDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(CronRule cronRule) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (cronRule == null) {
			throw new IllegalArgumentException("cronRule is NULL!");
		}
		if( cronRule.getId() != null && cronRule.getId().length() < 20 ){
			throw new IllegalArgumentException("id too short"+cronRule.getId());
		}
		if( cronRule.getId() != null && cronRule.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+cronRule.getId());
		}
		if( cronRule.getJobId() != null && cronRule.getJobId().length() < 20 ){
			throw new IllegalArgumentException("jobId too short"+cronRule.getJobId());
		}
		if( cronRule.getJobId() != null && cronRule.getJobId().length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+cronRule.getJobId());
		}
		if( cronRule.getState() != null && cronRule.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+cronRule.getState());
		}
		if( cronRule.getState() != null && cronRule.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+cronRule.getState());
		}
		if( cronRule.getExpression() != null && cronRule.getExpression().length() > 40 ){
			throw new IllegalArgumentException("expression too long"+cronRule.getExpression());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into cron_rule(id,job_id,state,expression) values(?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, cronRule.getId());
			statment.setString(2, cronRule.getJobId());
			statment.setString(3, cronRule.getState());
			statment.setString(4, cronRule.getExpression());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("CronRuleDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+cronRule.getId() +"\",job_id = \""+cronRule.getJobId() +"\",state = \""+cronRule.getState() +"\",expression = \""+cronRule.getExpression() +"\" ");
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


	public int[] insertBatch(List<CronRule> cronRuleList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (cronRuleList == null) {
			throw new IllegalArgumentException("cronRuleList is NULL!");
		}
		for( CronRule cronRule : cronRuleList){
		if( cronRule.getId() != null && cronRule.getId().length() < 20 ){
			throw new IllegalArgumentException("id too short"+cronRule.getId());
		}
		if( cronRule.getId() != null && cronRule.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+cronRule.getId());
		}
		if( cronRule.getJobId() != null && cronRule.getJobId().length() < 20 ){
			throw new IllegalArgumentException("jobId too short"+cronRule.getJobId());
		}
		if( cronRule.getJobId() != null && cronRule.getJobId().length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+cronRule.getJobId());
		}
		if( cronRule.getState() != null && cronRule.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+cronRule.getState());
		}
		if( cronRule.getState() != null && cronRule.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+cronRule.getState());
		}
		if( cronRule.getExpression() != null && cronRule.getExpression().length() > 40 ){
			throw new IllegalArgumentException("expression too long"+cronRule.getExpression());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into cron_rule(id,job_id,state,expression) values(?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( CronRule cronRule : cronRuleList){
			statment.setString(1, cronRule.getId());
			statment.setString(2, cronRule.getJobId());
			statment.setString(3, cronRule.getState());
			statment.setString(4, cronRule.getExpression());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("CronRuleDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(CronRule cronRule) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (cronRule == null) {
			throw new IllegalArgumentException("cronRule is NULL!");
		}

		PreparedStatement statment = null;
		if( cronRule.getId() != null && cronRule.getId().length() < 20 ){
			throw new IllegalArgumentException("id too short"+cronRule.getId());
		}
		if( cronRule.getId() != null && cronRule.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+cronRule.getId());
		}
		if( cronRule.getJobId() != null && cronRule.getJobId().length() < 20 ){
			throw new IllegalArgumentException("jobId too short"+cronRule.getJobId());
		}
		if( cronRule.getJobId() != null && cronRule.getJobId().length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+cronRule.getJobId());
		}
		if( cronRule.getState() != null && cronRule.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+cronRule.getState());
		}
		if( cronRule.getState() != null && cronRule.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+cronRule.getState());
		}
		if( cronRule.getExpression() != null && cronRule.getExpression().length() > 40 ){
			throw new IllegalArgumentException("expression too long"+cronRule.getExpression());
		}
		try {
			String sql =  "UPDATE cron_rule SET id=?,job_id=?,state=?,expression=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, cronRule.getId());
			statment.setString(2, cronRule.getJobId());
			statment.setString(3, cronRule.getState());
			statment.setString(4, cronRule.getExpression());
			statment.setString(5, cronRule.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("CronRuleDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+cronRule.getId() +"\",job_id = \""+cronRule.getJobId() +"\",state = \""+cronRule.getState() +"\",expression = \""+cronRule.getExpression() +"\" ");
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
			sql.append("UPDATE cron_rule SET ");
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
				if("job_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("state".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("expression".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("CronRuleDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
		if( id != null && id.length() < 20 ){
			throw new IllegalArgumentException("id too short"+id);
		}
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}

		try {
			String sql = "DELETE FROM cron_rule where id=? ";
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
				debug("CronRuleDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public CronRule selectByPK (String id)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() < 20 ){
			throw new IllegalArgumentException("id too short"+id);
		}
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		ResultSet resultSet = null;
		CronRule returnDTO = null;

		try {
			String sql =  "SELECT * FROM cron_rule where id=? ";
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
				debug("CronRuleDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new CronRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setExpression(resultSet.getString("expression"));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
		return returnDTO;
	}


	public List findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		CronRule returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from cron_rule order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("CronRuleDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new CronRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setExpression(resultSet.getString("expression"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
		return list;
	}


	public List findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		CronRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from cron_rule ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from cron_rule					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,job_id,state,expression, rownumber() over(order by id) as rn from cron_rule ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from cron_rule order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,job_id,state,expression from cron_rule ) A where rownumber > "  + startNum ;
			}else{
				throw new RuntimeException("the DAOCodeGenerator support mysql,oracle,mssql,db2,sqlserver2005. but batabase_type is "+ DAOConfiguration.getDATABASE_TYPE() );
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
				debug("CronRuleDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new CronRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setExpression(resultSet.getString("expression"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
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
		CronRule returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from cron_rule where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("CronRuleDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new CronRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setExpression(resultSet.getString("expression"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
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
		CronRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from cron_rule where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from cron_rule where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from cron_rule					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,job_id,state,expression, rownumber() over(order by id) as rn from cron_rule where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,job_id,state,expression from cron_rule  where " + where +" ) A where rownumber > "  + startNum ;
			}else{
				throw new RuntimeException("the DAOCodeGenerator support mysql,oracle,mssql,db2,sqlserver2005. but batabase_type is "+ DAOConfiguration.getDATABASE_TYPE() );
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
				debug("CronRuleDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new CronRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setExpression(resultSet.getString("expression"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
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
			String sql =  "select  count(*) from cron_rule";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("CronRuleDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}

			 if(resultSet.next()){
					totalRecords=resultSet.getInt(1);
				}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
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
			String sql =  "select  count(*) from cron_rule where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("CronRuleDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}

			 if(resultSet.next()){
					totalRecords=resultSet.getInt(1);
				}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {			if (null != resultSet) {
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
			}		}
		return totalRecords;
	}


	public List getCronRuleByJobId(String jobId ,String state ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( jobId != null && jobId.length() < 20 ){
			throw new IllegalArgumentException("jobId too short"+jobId);
		}
		if( jobId != null && jobId.length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+jobId);
		}
		if( state != null && state.length() < 2 ){
			throw new IllegalArgumentException("state too short"+state);
		}
		if( state != null && state.length() > 2 ){
			throw new IllegalArgumentException("state too long"+state);
		}
		ResultSet resultSet = null;
		CronRule returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from cron_rule where job_id=? and state=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,jobId);
			statment.setString(2,state);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("CronRuleDAO.getCronRuleByJobId() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : jobId = " + jobId+",state = " + state);
			}


			while (resultSet.next()) {
				returnDTO = new CronRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setJobId(resultSet.getString("job_id"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setExpression(resultSet.getString("expression"));
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

	public int deleteCronRuleByJobId(String jobId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( jobId != null && jobId.length() < 20 ){
			throw new IllegalArgumentException("jobId too short"+jobId);
		}
		if( jobId != null && jobId.length() > 20 ){
			throw new IllegalArgumentException("jobId too long"+jobId);
		}
		try {
			String sql = "delete from cron_rule where job_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,jobId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("CronRuleDAOdeleteCronRuleByJobId() spend "+(_endTime - _startTime)+"ms. retFlag = "+retFlag+" SQL:"+sql+"; parameter : jobId = " + jobId);
			}


			if (!isInTransaction()) {
				conn.commit();
			}
			return retFlag;		} catch (SQLException e) {
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
		return 0;
	}

}
