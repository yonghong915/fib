package com.fib.msgconverter.commgateway.dao.socketconnector.dao;

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



public class SocketConnectorDAO extends AbstractDAO {

	public SocketConnectorDAO() {
		super();
	}

	public SocketConnectorDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public SocketConnectorDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(SocketConnector socketConnector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (socketConnector == null) {
			throw new IllegalArgumentException("socketConnector is NULL!");
		}
		if( socketConnector.getId() != null && socketConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+socketConnector.getId());
		}
		if( socketConnector.getId() != null && socketConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+socketConnector.getId());
		}
		if( socketConnector.getConnectorType()==null || "".equals(socketConnector.getConnectorType())){
			throw new IllegalArgumentException("connectorType is null");
		}
		if( socketConnector.getConnectorType() != null && socketConnector.getConnectorType().length() < 4 ){
			throw new IllegalArgumentException("connectorType too short"+socketConnector.getConnectorType());
		}
		if( socketConnector.getConnectorType() != null && socketConnector.getConnectorType().length() > 4 ){
			throw new IllegalArgumentException("connectorType too long"+socketConnector.getConnectorType());
		}
		if( socketConnector.getServerAddress() != null && socketConnector.getServerAddress().length() > 16 ){
			throw new IllegalArgumentException("serverAddress too long"+socketConnector.getServerAddress());
		}
		if( socketConnector.getLocalServerAddress() != null && socketConnector.getLocalServerAddress().length() > 16 ){
			throw new IllegalArgumentException("localServerAddress too long"+socketConnector.getLocalServerAddress());
		}
		if( socketConnector.getReaderId() != null && socketConnector.getReaderId().length() < 10 ){
			throw new IllegalArgumentException("readerId too short"+socketConnector.getReaderId());
		}
		if( socketConnector.getReaderId() != null && socketConnector.getReaderId().length() > 10 ){
			throw new IllegalArgumentException("readerId too long"+socketConnector.getReaderId());
		}
		if( socketConnector.getWriterId() != null && socketConnector.getWriterId().length() < 10 ){
			throw new IllegalArgumentException("writerId too short"+socketConnector.getWriterId());
		}
		if( socketConnector.getWriterId() != null && socketConnector.getWriterId().length() > 10 ){
			throw new IllegalArgumentException("writerId too long"+socketConnector.getWriterId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into socket_connector(id,connector_type,server_address,port,local_server_address,local_port,backlog,comm_buffer_size,reader_id,writer_id) values(?,?,?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, socketConnector.getId());
			statment.setString(2, socketConnector.getConnectorType());
			statment.setString(3, socketConnector.getServerAddress());
			statment.setInt(4, socketConnector.getPort());
			statment.setString(5, socketConnector.getLocalServerAddress());
			statment.setInt(6, socketConnector.getLocalPort());
			statment.setInt(7, socketConnector.getBacklog());
			statment.setInt(8, socketConnector.getCommBufferSize());
			statment.setString(9, socketConnector.getReaderId());
			statment.setString(10, socketConnector.getWriterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("SocketConnectorDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+socketConnector.getId() +"\",connector_type = \""+socketConnector.getConnectorType() +"\",server_address = \""+socketConnector.getServerAddress() +"\",port = "+socketConnector.getPort()+",local_server_address = \""+socketConnector.getLocalServerAddress() +"\",local_port = "+socketConnector.getLocalPort()+",backlog = "+socketConnector.getBacklog()+",comm_buffer_size = "+socketConnector.getCommBufferSize()+",reader_id = \""+socketConnector.getReaderId() +"\",writer_id = \""+socketConnector.getWriterId() +"\" ");
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


	public int[] insertBatch(List<SocketConnector> socketConnectorList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (socketConnectorList == null) {
			throw new IllegalArgumentException("socketConnectorList is NULL!");
		}
		for( SocketConnector socketConnector : socketConnectorList){
		if( socketConnector.getId() != null && socketConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+socketConnector.getId());
		}
		if( socketConnector.getId() != null && socketConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+socketConnector.getId());
		}
		if( socketConnector.getConnectorType()==null || "".equals(socketConnector.getConnectorType())){
			throw new IllegalArgumentException("connectorType is null");
		}
		if( socketConnector.getConnectorType() != null && socketConnector.getConnectorType().length() < 4 ){
			throw new IllegalArgumentException("connectorType too short"+socketConnector.getConnectorType());
		}
		if( socketConnector.getConnectorType() != null && socketConnector.getConnectorType().length() > 4 ){
			throw new IllegalArgumentException("connectorType too long"+socketConnector.getConnectorType());
		}
		if( socketConnector.getServerAddress() != null && socketConnector.getServerAddress().length() > 16 ){
			throw new IllegalArgumentException("serverAddress too long"+socketConnector.getServerAddress());
		}
		if( socketConnector.getLocalServerAddress() != null && socketConnector.getLocalServerAddress().length() > 16 ){
			throw new IllegalArgumentException("localServerAddress too long"+socketConnector.getLocalServerAddress());
		}
		if( socketConnector.getReaderId() != null && socketConnector.getReaderId().length() < 10 ){
			throw new IllegalArgumentException("readerId too short"+socketConnector.getReaderId());
		}
		if( socketConnector.getReaderId() != null && socketConnector.getReaderId().length() > 10 ){
			throw new IllegalArgumentException("readerId too long"+socketConnector.getReaderId());
		}
		if( socketConnector.getWriterId() != null && socketConnector.getWriterId().length() < 10 ){
			throw new IllegalArgumentException("writerId too short"+socketConnector.getWriterId());
		}
		if( socketConnector.getWriterId() != null && socketConnector.getWriterId().length() > 10 ){
			throw new IllegalArgumentException("writerId too long"+socketConnector.getWriterId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into socket_connector(id,connector_type,server_address,port,local_server_address,local_port,backlog,comm_buffer_size,reader_id,writer_id) values(?,?,?,?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( SocketConnector socketConnector : socketConnectorList){
			statment.setString(1, socketConnector.getId());
			statment.setString(2, socketConnector.getConnectorType());
			statment.setString(3, socketConnector.getServerAddress());
			statment.setInt(4, socketConnector.getPort());
			statment.setString(5, socketConnector.getLocalServerAddress());
			statment.setInt(6, socketConnector.getLocalPort());
			statment.setInt(7, socketConnector.getBacklog());
			statment.setInt(8, socketConnector.getCommBufferSize());
			statment.setString(9, socketConnector.getReaderId());
			statment.setString(10, socketConnector.getWriterId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("SocketConnectorDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(SocketConnector socketConnector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (socketConnector == null) {
			throw new IllegalArgumentException("socketConnector is NULL!");
		}

		PreparedStatement statment = null;
		if( socketConnector.getId() != null && socketConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+socketConnector.getId());
		}
		if( socketConnector.getId() != null && socketConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+socketConnector.getId());
		}
		if( socketConnector.getConnectorType()==null || "".equals(socketConnector.getConnectorType())){
			throw new IllegalArgumentException("connectorType is null");
		}
		if( socketConnector.getConnectorType() != null && socketConnector.getConnectorType().length() < 4 ){
			throw new IllegalArgumentException("connectorType too short"+socketConnector.getConnectorType());
		}
		if( socketConnector.getConnectorType() != null && socketConnector.getConnectorType().length() > 4 ){
			throw new IllegalArgumentException("connectorType too long"+socketConnector.getConnectorType());
		}
		if( socketConnector.getServerAddress() != null && socketConnector.getServerAddress().length() > 16 ){
			throw new IllegalArgumentException("serverAddress too long"+socketConnector.getServerAddress());
		}
		if( socketConnector.getLocalServerAddress() != null && socketConnector.getLocalServerAddress().length() > 16 ){
			throw new IllegalArgumentException("localServerAddress too long"+socketConnector.getLocalServerAddress());
		}
		if( socketConnector.getReaderId() != null && socketConnector.getReaderId().length() < 10 ){
			throw new IllegalArgumentException("readerId too short"+socketConnector.getReaderId());
		}
		if( socketConnector.getReaderId() != null && socketConnector.getReaderId().length() > 10 ){
			throw new IllegalArgumentException("readerId too long"+socketConnector.getReaderId());
		}
		if( socketConnector.getWriterId() != null && socketConnector.getWriterId().length() < 10 ){
			throw new IllegalArgumentException("writerId too short"+socketConnector.getWriterId());
		}
		if( socketConnector.getWriterId() != null && socketConnector.getWriterId().length() > 10 ){
			throw new IllegalArgumentException("writerId too long"+socketConnector.getWriterId());
		}
		try {
			String sql =  "UPDATE socket_connector SET id=?,connector_type=?,server_address=?,port=?,local_server_address=?,local_port=?,backlog=?,comm_buffer_size=?,reader_id=?,writer_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, socketConnector.getId());
			statment.setString(2, socketConnector.getConnectorType());
			statment.setString(3, socketConnector.getServerAddress());
			statment.setInt(4, socketConnector.getPort());
			statment.setString(5, socketConnector.getLocalServerAddress());
			statment.setInt(6, socketConnector.getLocalPort());
			statment.setInt(7, socketConnector.getBacklog());
			statment.setInt(8, socketConnector.getCommBufferSize());
			statment.setString(9, socketConnector.getReaderId());
			statment.setString(10, socketConnector.getWriterId());
			statment.setString(11, socketConnector.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("SocketConnectorDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+socketConnector.getId() +"\",connector_type = \""+socketConnector.getConnectorType() +"\",server_address = \""+socketConnector.getServerAddress() +"\",port = "+socketConnector.getPort()+",local_server_address = \""+socketConnector.getLocalServerAddress() +"\",local_port = "+socketConnector.getLocalPort()+",backlog = "+socketConnector.getBacklog()+",comm_buffer_size = "+socketConnector.getCommBufferSize()+",reader_id = \""+socketConnector.getReaderId() +"\",writer_id = \""+socketConnector.getWriterId() +"\" ");
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
			sql.append("UPDATE socket_connector SET ");
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
				if("connector_type".equalsIgnoreCase(tmpKey)){
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
				if("backlog".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("comm_buffer_size".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("reader_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("writer_id".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("SocketConnectorDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM socket_connector where id=? ";
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
				debug("SocketConnectorDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public SocketConnector selectByPK (String id)  {
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
		SocketConnector returnDTO = null;

		try {
			String sql =  "SELECT * FROM socket_connector where id=? ";
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
				debug("SocketConnectorDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new SocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setCommBufferSize(resultSet.getInt("comm_buffer_size"));
				returnDTO.setReaderId(resultSet.getString("reader_id"));
				returnDTO.setWriterId(resultSet.getString("writer_id"));
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
		SocketConnector returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from socket_connector order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SocketConnectorDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new SocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setCommBufferSize(resultSet.getInt("comm_buffer_size"));
				returnDTO.setReaderId(resultSet.getString("reader_id"));
				returnDTO.setWriterId(resultSet.getString("writer_id"));
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
		SocketConnector returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from socket_connector ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from socket_connector					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,connector_type,server_address,port,local_server_address,local_port,backlog,comm_buffer_size,reader_id,writer_id, rownumber() over(order by id) as rn from socket_connector ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from socket_connector order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connector_type,server_address,port,local_server_address,local_port,backlog,comm_buffer_size,reader_id,writer_id from socket_connector ) A where rownumber > "  + startNum ;
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
				debug("SocketConnectorDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new SocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setCommBufferSize(resultSet.getInt("comm_buffer_size"));
				returnDTO.setReaderId(resultSet.getString("reader_id"));
				returnDTO.setWriterId(resultSet.getString("writer_id"));
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
		SocketConnector returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from socket_connector where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SocketConnectorDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new SocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setCommBufferSize(resultSet.getInt("comm_buffer_size"));
				returnDTO.setReaderId(resultSet.getString("reader_id"));
				returnDTO.setWriterId(resultSet.getString("writer_id"));
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
		SocketConnector returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from socket_connector where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from socket_connector where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from socket_connector					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,connector_type,server_address,port,local_server_address,local_port,backlog,comm_buffer_size,reader_id,writer_id, rownumber() over(order by id) as rn from socket_connector where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connector_type,server_address,port,local_server_address,local_port,backlog,comm_buffer_size,reader_id,writer_id from socket_connector  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("SocketConnectorDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new SocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setLocalServerAddress(resultSet.getString("local_server_address"));
				returnDTO.setLocalPort(resultSet.getInt("local_port"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setCommBufferSize(resultSet.getInt("comm_buffer_size"));
				returnDTO.setReaderId(resultSet.getString("reader_id"));
				returnDTO.setWriterId(resultSet.getString("writer_id"));
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
			String sql =  "select  count(*) from socket_connector";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SocketConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from socket_connector where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SocketConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
