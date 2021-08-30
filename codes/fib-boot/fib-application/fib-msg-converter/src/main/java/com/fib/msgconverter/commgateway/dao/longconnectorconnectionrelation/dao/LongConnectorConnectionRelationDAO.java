package com.fib.msgconverter.commgateway.dao.longconnectorconnectionrelation.dao;

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



public class LongConnectorConnectionRelationDAO extends AbstractDAO {

	public LongConnectorConnectionRelationDAO() {
		super();
	}

	public LongConnectorConnectionRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public LongConnectorConnectionRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(LongConnectorConnectionRelation longConnectorConnectionRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnectorConnectionRelation == null) {
			throw new IllegalArgumentException("longConnectorConnectionRelation is NULL!");
		}
		if( longConnectorConnectionRelation.getLongConnectorId() != null && longConnectorConnectionRelation.getLongConnectorId().length() < 10 ){
			throw new IllegalArgumentException("longConnectorId too short"+longConnectorConnectionRelation.getLongConnectorId());
		}
		if( longConnectorConnectionRelation.getLongConnectorId() != null && longConnectorConnectionRelation.getLongConnectorId().length() > 10 ){
			throw new IllegalArgumentException("longConnectorId too long"+longConnectorConnectionRelation.getLongConnectorId());
		}
		if( longConnectorConnectionRelation.getConnectionId() != null && longConnectorConnectionRelation.getConnectionId().length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+longConnectorConnectionRelation.getConnectionId());
		}
		if( longConnectorConnectionRelation.getConnectionId() != null && longConnectorConnectionRelation.getConnectionId().length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+longConnectorConnectionRelation.getConnectionId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into long_connector_connection_relation(long_connector_id,connection_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, longConnectorConnectionRelation.getLongConnectorId());
			statment.setString(2, longConnectorConnectionRelation.getConnectionId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : long_connector_id = \""+longConnectorConnectionRelation.getLongConnectorId() +"\",connection_id = \""+longConnectorConnectionRelation.getConnectionId() +"\" ");
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


	public int[] insertBatch(List<LongConnectorConnectionRelation> longConnectorConnectionRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (longConnectorConnectionRelationList == null) {
			throw new IllegalArgumentException("longConnectorConnectionRelationList is NULL!");
		}
		for( LongConnectorConnectionRelation longConnectorConnectionRelation : longConnectorConnectionRelationList){
		if( longConnectorConnectionRelation.getLongConnectorId() != null && longConnectorConnectionRelation.getLongConnectorId().length() < 10 ){
			throw new IllegalArgumentException("longConnectorId too short"+longConnectorConnectionRelation.getLongConnectorId());
		}
		if( longConnectorConnectionRelation.getLongConnectorId() != null && longConnectorConnectionRelation.getLongConnectorId().length() > 10 ){
			throw new IllegalArgumentException("longConnectorId too long"+longConnectorConnectionRelation.getLongConnectorId());
		}
		if( longConnectorConnectionRelation.getConnectionId() != null && longConnectorConnectionRelation.getConnectionId().length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+longConnectorConnectionRelation.getConnectionId());
		}
		if( longConnectorConnectionRelation.getConnectionId() != null && longConnectorConnectionRelation.getConnectionId().length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+longConnectorConnectionRelation.getConnectionId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into long_connector_connection_relation(long_connector_id,connection_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( LongConnectorConnectionRelation longConnectorConnectionRelation : longConnectorConnectionRelationList){
			statment.setString(1, longConnectorConnectionRelation.getLongConnectorId());
			statment.setString(2, longConnectorConnectionRelation.getConnectionId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(LongConnectorConnectionRelation longConnectorConnectionRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnectorConnectionRelation == null) {
			throw new IllegalArgumentException("longConnectorConnectionRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( longConnectorConnectionRelation.getLongConnectorId() != null && longConnectorConnectionRelation.getLongConnectorId().length() < 10 ){
			throw new IllegalArgumentException("longConnectorId too short"+longConnectorConnectionRelation.getLongConnectorId());
		}
		if( longConnectorConnectionRelation.getLongConnectorId() != null && longConnectorConnectionRelation.getLongConnectorId().length() > 10 ){
			throw new IllegalArgumentException("longConnectorId too long"+longConnectorConnectionRelation.getLongConnectorId());
		}
		if( longConnectorConnectionRelation.getConnectionId() != null && longConnectorConnectionRelation.getConnectionId().length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+longConnectorConnectionRelation.getConnectionId());
		}
		if( longConnectorConnectionRelation.getConnectionId() != null && longConnectorConnectionRelation.getConnectionId().length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+longConnectorConnectionRelation.getConnectionId());
		}
		try {
			String sql =  "UPDATE long_connector_connection_relation SET long_connector_id=?,connection_id=? where long_connector_id=? and connection_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, longConnectorConnectionRelation.getLongConnectorId());
			statment.setString(2, longConnectorConnectionRelation.getConnectionId());
			statment.setString(3, longConnectorConnectionRelation.getLongConnectorId());			statment.setString(4, longConnectorConnectionRelation.getConnectionId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : long_connector_id = \""+longConnectorConnectionRelation.getLongConnectorId() +"\",connection_id = \""+longConnectorConnectionRelation.getConnectionId() +"\" ");
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
		if(!primaryKey.containsKey("long_connector_id") ){
			throw new IllegalArgumentException("long_connector_id is null ");  
		}
		if(!primaryKey.containsKey("connection_id") ){
			throw new IllegalArgumentException("connection_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE long_connector_connection_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where long_connector_id=? and connection_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("long_connector_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("connection_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("long_connector_id"));
			statment.setString(m++, (String)primaryKey.get("connection_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("LongConnectorConnectionRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String longConnectorId,String connectionId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnectorId == null) {
			throw new IllegalArgumentException("longConnectorId is NULL!");
		}
		if (connectionId == null) {
			throw new IllegalArgumentException("connectionId is NULL!");
		}

		PreparedStatement statment = null;
		if( longConnectorId != null && longConnectorId.length() < 10 ){
			throw new IllegalArgumentException("longConnectorId too short"+longConnectorId);
		}
		if( longConnectorId != null && longConnectorId.length() > 10 ){
			throw new IllegalArgumentException("longConnectorId too long"+longConnectorId);
		}
		if( connectionId != null && connectionId.length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+connectionId);
		}
		if( connectionId != null && connectionId.length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+connectionId);
		}

		try {
			String sql = "DELETE FROM long_connector_connection_relation where long_connector_id=? and connection_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,longConnectorId);
			statment.setString(2,connectionId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : long_connector_id = \""+longConnectorId+"\",connection_id = \""+connectionId+"\" ");
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


	public LongConnectorConnectionRelation selectByPK (String longConnectorId,String connectionId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( longConnectorId != null && longConnectorId.length() < 10 ){
			throw new IllegalArgumentException("longConnectorId too short"+longConnectorId);
		}
		if( longConnectorId != null && longConnectorId.length() > 10 ){
			throw new IllegalArgumentException("longConnectorId too long"+longConnectorId);
		}
		if( connectionId != null && connectionId.length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+connectionId);
		}
		if( connectionId != null && connectionId.length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+connectionId);
		}
		ResultSet resultSet = null;
		LongConnectorConnectionRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM long_connector_connection_relation where long_connector_id=? and connection_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,longConnectorId);
			statment.setString(2,connectionId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : long_connector_id = \""+longConnectorId+"\",connection_id = \""+connectionId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new LongConnectorConnectionRelation();
				returnDTO.setLongConnectorId(resultSet.getString("long_connector_id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
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
		LongConnectorConnectionRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from long_connector_connection_relation order by long_connector_id,connection_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorConnectionRelation();
				returnDTO.setLongConnectorId(resultSet.getString("long_connector_id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
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
		LongConnectorConnectionRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_connector_connection_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by long_connector_id,connection_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from long_connector_connection_relation					 ) t1)t2 order by long_connector_id,connection_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select long_connector_id,connection_id, rownumber() over(order by long_connector_id,connection_id) as rn from long_connector_connection_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from long_connector_connection_relation order by long_connector_id,connection_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by long_connector_id,connection_id) as rownumber, long_connector_id,connection_id from long_connector_connection_relation ) A where rownumber > "  + startNum ;
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
				debug("LongConnectorConnectionRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorConnectionRelation();
				returnDTO.setLongConnectorId(resultSet.getString("long_connector_id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
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
		LongConnectorConnectionRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from long_connector_connection_relation where " + where +" order by long_connector_id,connection_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorConnectionRelation();
				returnDTO.setLongConnectorId(resultSet.getString("long_connector_id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
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
		LongConnectorConnectionRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from long_connector_connection_relation where " + where +" order by long_connector_id,connection_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_connector_connection_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by long_connector_id,connection_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from long_connector_connection_relation					 where " + where + " ) t1)t2 order by long_connector_id,connection_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select long_connector_id,connection_id, rownumber() over(order by long_connector_id,connection_id) as rn from long_connector_connection_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by long_connector_id,connection_id) as rownumber, long_connector_id,connection_id from long_connector_connection_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("LongConnectorConnectionRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new LongConnectorConnectionRelation();
				returnDTO.setLongConnectorId(resultSet.getString("long_connector_id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
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
			String sql =  "select  count(*) from long_connector_connection_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from long_connector_connection_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllConnection4LongConnector(String longConnectorId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( longConnectorId != null && longConnectorId.length() < 10 ){
			throw new IllegalArgumentException("longConnectorId too short"+longConnectorId);
		}
		if( longConnectorId != null && longConnectorId.length() > 10 ){
			throw new IllegalArgumentException("longConnectorId too long"+longConnectorId);
		}
		ResultSet resultSet = null;
		LongConnectorConnectionRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from long_connector_connection_relation where long_connector_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,longConnectorId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("LongConnectorConnectionRelationDAO.getAllConnection4LongConnector() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : longConnectorId = " + longConnectorId);
			}


			while (resultSet.next()) {
				returnDTO = new LongConnectorConnectionRelation();
				returnDTO.setLongConnectorId(resultSet.getString("long_connector_id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
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
