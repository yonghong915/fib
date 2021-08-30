package com.fib.msgconverter.commgateway.dao.gateway.dao;

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



public class GatewayDAO extends AbstractDAO {

	public GatewayDAO() {
		super();
	}

	public GatewayDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public GatewayDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Gateway gateway) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (gateway == null) {
			throw new IllegalArgumentException("gateway is NULL!");
		}
		if( gateway.getId() != null && gateway.getId().length() > 255 ){
			throw new IllegalArgumentException("id too long"+gateway.getId());
		}
		if( gateway.getName() != null && gateway.getName().length() > 255 ){
			throw new IllegalArgumentException("name too long"+gateway.getName());
		}
		if( gateway.getLoggerName() != null && gateway.getLoggerName().length() > 255 ){
			throw new IllegalArgumentException("loggerName too long"+gateway.getLoggerName());
		}
		if( gateway.getLogLevelIntoDb() != null && gateway.getLogLevelIntoDb().length() > 8 ){
			throw new IllegalArgumentException("logLevelIntoDb too long"+gateway.getLogLevelIntoDb());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into gateway(id,name,monitor_port,max_handler_number,session_timeout,logger_name,log_level_into_db) values(?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, gateway.getId());
			statment.setString(2, gateway.getName());
			statment.setInt(3, gateway.getMonitorPort());
			statment.setInt(4, gateway.getMaxHandlerNumber());
			statment.setInt(5, gateway.getSessionTimeout());
			statment.setString(6, gateway.getLoggerName());
			statment.setString(7, gateway.getLogLevelIntoDb());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+gateway.getId() +"\",name = \""+gateway.getName() +"\",monitor_port = "+gateway.getMonitorPort()+",max_handler_number = "+gateway.getMaxHandlerNumber()+",session_timeout = "+gateway.getSessionTimeout()+",logger_name = \""+gateway.getLoggerName() +"\",log_level_into_db = \""+gateway.getLogLevelIntoDb() +"\" ");
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


	public int[] insertBatch(List<Gateway> gatewayList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (gatewayList == null) {
			throw new IllegalArgumentException("gatewayList is NULL!");
		}
		for( Gateway gateway : gatewayList){
		if( gateway.getId() != null && gateway.getId().length() > 255 ){
			throw new IllegalArgumentException("id too long"+gateway.getId());
		}
		if( gateway.getName() != null && gateway.getName().length() > 255 ){
			throw new IllegalArgumentException("name too long"+gateway.getName());
		}
		if( gateway.getLoggerName() != null && gateway.getLoggerName().length() > 255 ){
			throw new IllegalArgumentException("loggerName too long"+gateway.getLoggerName());
		}
		if( gateway.getLogLevelIntoDb() != null && gateway.getLogLevelIntoDb().length() > 8 ){
			throw new IllegalArgumentException("logLevelIntoDb too long"+gateway.getLogLevelIntoDb());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into gateway(id,name,monitor_port,max_handler_number,session_timeout,logger_name,log_level_into_db) values(?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( Gateway gateway : gatewayList){
			statment.setString(1, gateway.getId());
			statment.setString(2, gateway.getName());
			statment.setInt(3, gateway.getMonitorPort());
			statment.setInt(4, gateway.getMaxHandlerNumber());
			statment.setInt(5, gateway.getSessionTimeout());
			statment.setString(6, gateway.getLoggerName());
			statment.setString(7, gateway.getLogLevelIntoDb());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(Gateway gateway) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (gateway == null) {
			throw new IllegalArgumentException("gateway is NULL!");
		}

		PreparedStatement statment = null;
		if( gateway.getId() != null && gateway.getId().length() > 255 ){
			throw new IllegalArgumentException("id too long"+gateway.getId());
		}
		if( gateway.getName() != null && gateway.getName().length() > 255 ){
			throw new IllegalArgumentException("name too long"+gateway.getName());
		}
		if( gateway.getLoggerName() != null && gateway.getLoggerName().length() > 255 ){
			throw new IllegalArgumentException("loggerName too long"+gateway.getLoggerName());
		}
		if( gateway.getLogLevelIntoDb() != null && gateway.getLogLevelIntoDb().length() > 8 ){
			throw new IllegalArgumentException("logLevelIntoDb too long"+gateway.getLogLevelIntoDb());
		}
		try {
			String sql =  "UPDATE gateway SET id=?,name=?,monitor_port=?,max_handler_number=?,session_timeout=?,logger_name=?,log_level_into_db=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, gateway.getId());
			statment.setString(2, gateway.getName());
			statment.setInt(3, gateway.getMonitorPort());
			statment.setInt(4, gateway.getMaxHandlerNumber());
			statment.setInt(5, gateway.getSessionTimeout());
			statment.setString(6, gateway.getLoggerName());
			statment.setString(7, gateway.getLogLevelIntoDb());
			statment.setString(8, gateway.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+gateway.getId() +"\",name = \""+gateway.getName() +"\",monitor_port = "+gateway.getMonitorPort()+",max_handler_number = "+gateway.getMaxHandlerNumber()+",session_timeout = "+gateway.getSessionTimeout()+",logger_name = \""+gateway.getLoggerName() +"\",log_level_into_db = \""+gateway.getLogLevelIntoDb() +"\" ");
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
			sql.append("UPDATE gateway SET ");
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
				if("name".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("monitor_port".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("max_handler_number".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("session_timeout".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("logger_name".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("log_level_into_db".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("GatewayDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
		if( id != null && id.length() > 255 ){
			throw new IllegalArgumentException("id too long"+id);
		}

		try {
			String sql = "DELETE FROM gateway where id=? ";
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
				debug("GatewayDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public Gateway selectByPK (String id)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 255 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		ResultSet resultSet = null;
		Gateway returnDTO = null;

		try {
			String sql =  "SELECT * FROM gateway where id=? ";
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
				debug("GatewayDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Gateway();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setMonitorPort(resultSet.getInt("monitor_port"));
				returnDTO.setMaxHandlerNumber(resultSet.getInt("max_handler_number"));
				returnDTO.setSessionTimeout(resultSet.getInt("session_timeout"));
				returnDTO.setLoggerName(resultSet.getString("logger_name"));
				returnDTO.setLogLevelIntoDb(resultSet.getString("log_level_into_db"));
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
		Gateway returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from gateway order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Gateway();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setMonitorPort(resultSet.getInt("monitor_port"));
				returnDTO.setMaxHandlerNumber(resultSet.getInt("max_handler_number"));
				returnDTO.setSessionTimeout(resultSet.getInt("session_timeout"));
				returnDTO.setLoggerName(resultSet.getString("logger_name"));
				returnDTO.setLogLevelIntoDb(resultSet.getString("log_level_into_db"));
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
		Gateway returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from gateway ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from gateway					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,name,monitor_port,max_handler_number,session_timeout,logger_name,log_level_into_db, rownumber() over(order by id) as rn from gateway ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from gateway order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,name,monitor_port,max_handler_number,session_timeout,logger_name,log_level_into_db from gateway ) A where rownumber > "  + startNum ;
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
				debug("GatewayDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Gateway();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setMonitorPort(resultSet.getInt("monitor_port"));
				returnDTO.setMaxHandlerNumber(resultSet.getInt("max_handler_number"));
				returnDTO.setSessionTimeout(resultSet.getInt("session_timeout"));
				returnDTO.setLoggerName(resultSet.getString("logger_name"));
				returnDTO.setLogLevelIntoDb(resultSet.getString("log_level_into_db"));
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
		Gateway returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from gateway where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Gateway();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setMonitorPort(resultSet.getInt("monitor_port"));
				returnDTO.setMaxHandlerNumber(resultSet.getInt("max_handler_number"));
				returnDTO.setSessionTimeout(resultSet.getInt("session_timeout"));
				returnDTO.setLoggerName(resultSet.getString("logger_name"));
				returnDTO.setLogLevelIntoDb(resultSet.getString("log_level_into_db"));
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
		Gateway returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from gateway where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from gateway where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from gateway					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,name,monitor_port,max_handler_number,session_timeout,logger_name,log_level_into_db, rownumber() over(order by id) as rn from gateway where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,name,monitor_port,max_handler_number,session_timeout,logger_name,log_level_into_db from gateway  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("GatewayDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Gateway();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setMonitorPort(resultSet.getInt("monitor_port"));
				returnDTO.setMaxHandlerNumber(resultSet.getInt("max_handler_number"));
				returnDTO.setSessionTimeout(resultSet.getInt("session_timeout"));
				returnDTO.setLoggerName(resultSet.getString("logger_name"));
				returnDTO.setLogLevelIntoDb(resultSet.getString("log_level_into_db"));
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
			String sql =  "select  count(*) from gateway";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from gateway where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


}
