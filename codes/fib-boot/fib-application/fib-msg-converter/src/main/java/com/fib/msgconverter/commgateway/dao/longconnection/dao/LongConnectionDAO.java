package com.fib.msgconverter.commgateway.dao.longconnection.dao;

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



public class LongConnectionDAO extends AbstractDAO {

	public LongConnectionDAO() {
		super();
	}

	public LongConnectionDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public LongConnectionDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(LongConnection longConnection) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnection == null) {
			throw new IllegalArgumentException("longConnection is NULL!");
		}
		if( longConnection.getId() != null && longConnection.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+longConnection.getId());
		}
		if( longConnection.getId() != null && longConnection.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+longConnection.getId());
		}
		if( longConnection.getConnectionId() != null && longConnection.getConnectionId().length() > 255 ){
			throw new IllegalArgumentException("connectionId too long"+longConnection.getConnectionId());
		}
		if( longConnection.getDirection() != null && longConnection.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+longConnection.getDirection());
		}
		if( longConnection.getDirection() != null && longConnection.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+longConnection.getDirection());
		}
		if( longConnection.getServerAddress() != null && longConnection.getServerAddress().length() > 16 ){
			throw new IllegalArgumentException("serverAddress too long"+longConnection.getServerAddress());
		}
		if( longConnection.getLocalServerAddress() != null && longConnection.getLocalServerAddress().length() > 16 ){
			throw new IllegalArgumentException("localServerAddress too long"+longConnection.getLocalServerAddress());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into long_connection(id,connection_id,direction,server_address,port,local_server_address,local_port) values(?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, longConnection.getId());
			statment.setString(2, longConnection.getConnectionId());
			statment.setString(3, longConnection.getDirection());
			statment.setString(4, longConnection.getServerAddress());
			statment.setInt(5, longConnection.getPort());
			statment.setString(6, longConnection.getLocalServerAddress());
			statment.setInt(7, longConnection.getLocalPort());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectionDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+longConnection.getId() +"\",connection_id = \""+longConnection.getConnectionId() +"\",direction = \""+longConnection.getDirection() +"\",server_address = \""+longConnection.getServerAddress() +"\",port = "+longConnection.getPort()+",local_server_address = \""+longConnection.getLocalServerAddress() +"\",local_port = "+longConnection.getLocalPort()+" ");
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


	public int[] insertBatch(List<LongConnection> longConnectionList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (longConnectionList == null) {
			throw new IllegalArgumentException("longConnectionList is NULL!");
		}
		for( LongConnection longConnection : longConnectionList){
		if( longConnection.getId() != null && longConnection.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+longConnection.getId());
		}
		if( longConnection.getId() != null && longConnection.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+longConnection.getId());
		}
		if( longConnection.getConnectionId() != null && longConnection.getConnectionId().length() > 255 ){
			throw new IllegalArgumentException("connectionId too long"+longConnection.getConnectionId());
		}
		if( longConnection.getDirection() != null && longConnection.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+longConnection.getDirection());
		}
		if( longConnection.getDirection() != null && longConnection.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+longConnection.getDirection());
		}
		if( longConnection.getServerAddress() != null && longConnection.getServerAddress().length() > 16 ){
			throw new IllegalArgumentException("serverAddress too long"+longConnection.getServerAddress());
		}
		if( longConnection.getLocalServerAddress() != null && longConnection.getLocalServerAddress().length() > 16 ){
			throw new IllegalArgumentException("localServerAddress too long"+longConnection.getLocalServerAddress());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into long_connection(id,connection_id,direction,server_address,port,local_server_address,local_port) values(?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( LongConnection longConnection : longConnectionList){
			statment.setString(1, longConnection.getId());
			statment.setString(2, longConnection.getConnectionId());
			statment.setString(3, longConnection.getDirection());
			statment.setString(4, longConnection.getServerAddress());
			statment.setInt(5, longConnection.getPort());
			statment.setString(6, longConnection.getLocalServerAddress());
			statment.setInt(7, longConnection.getLocalPort());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectionDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(LongConnection longConnection) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longConnection == null) {
			throw new IllegalArgumentException("longConnection is NULL!");
		}

		PreparedStatement statment = null;
		if( longConnection.getId() != null && longConnection.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+longConnection.getId());
		}
		if( longConnection.getId() != null && longConnection.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+longConnection.getId());
		}
		if( longConnection.getConnectionId() != null && longConnection.getConnectionId().length() > 255 ){
			throw new IllegalArgumentException("connectionId too long"+longConnection.getConnectionId());
		}
		if( longConnection.getDirection() != null && longConnection.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+longConnection.getDirection());
		}
		if( longConnection.getDirection() != null && longConnection.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+longConnection.getDirection());
		}
		if( longConnection.getServerAddress() != null && longConnection.getServerAddress().length() > 16 ){
			throw new IllegalArgumentException("serverAddress too long"+longConnection.getServerAddress());
		}
		if( longConnection.getLocalServerAddress() != null && longConnection.getLocalServerAddress().length() > 16 ){
			throw new IllegalArgumentException("localServerAddress too long"+longConnection.getLocalServerAddress());
		}
		try {
			String sql =  "UPDATE long_connection SET id=?,connection_id=?,direction=?,server_address=?,port=?,local_server_address=?,local_port=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, longConnection.getId());
			statment.setString(2, longConnection.getConnectionId());
			statment.setString(3, longConnection.getDirection());
			statment.setString(4, longConnection.getServerAddress());
			statment.setInt(5, longConnection.getPort());
			statment.setString(6, longConnection.getLocalServerAddress());
			statment.setInt(7, longConnection.getLocalPort());
			statment.setString(8, longConnection.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongConnectionDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+longConnection.getId() +"\",connection_id = \""+longConnection.getConnectionId() +"\",direction = \""+longConnection.getDirection() +"\",server_address = \""+longConnection.getServerAddress() +"\",port = "+longConnection.getPort()+",local_server_address = \""+longConnection.getLocalServerAddress() +"\",local_port = "+longConnection.getLocalPort()+" ");
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
			sql.append("UPDATE long_connection SET ");
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
				if("connection_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("direction".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("server_address".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("port".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("local_server_address".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("local_port".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
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
				sbDebug.append("LongConnectionDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
		if( id != null && id.length() < 10 ){
			throw new IllegalArgumentException("id too short"+id);
		}
		if( id != null && id.length() > 10 ){
			throw new IllegalArgumentException("id too long"+id);
		}

		try {
			String sql = "DELETE FROM long_connection where id=? ";
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
				debug("LongConnectionDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public LongConnection selectByPK (String id)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() < 10 ){
			throw new IllegalArgumentException("id too short"+id);
		}
		if( id != null && id.length() > 10 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		ResultSet resultSet = null;
		LongConnection returnDTO = null;

		try {
			String sql =  "SELECT * FROM long_connection where id=? ";
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
				debug("LongConnectionDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new LongConnection();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
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
		LongConnection returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from long_connection order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectionDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongConnection();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
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
		LongConnection returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_connection ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from long_connection					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,connection_id,direction,server_address,port,local_server_address,local_port, rownumber() over(order by id) as rn from long_connection ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from long_connection order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connection_id,direction,server_address,port,local_server_address,local_port from long_connection ) A where rownumber > "  + startNum ;
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
				debug("LongConnectionDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongConnection();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
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
		LongConnection returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from long_connection where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectionDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new LongConnection();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
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
		LongConnection returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from long_connection where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_connection where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from long_connection					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,connection_id,direction,server_address,port,local_server_address,local_port, rownumber() over(order by id) as rn from long_connection where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connection_id,direction,server_address,port,local_server_address,local_port from long_connection  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("LongConnectionDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new LongConnection();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
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
			String sql =  "select  count(*) from long_connection";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectionDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from long_connection where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongConnectionDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
