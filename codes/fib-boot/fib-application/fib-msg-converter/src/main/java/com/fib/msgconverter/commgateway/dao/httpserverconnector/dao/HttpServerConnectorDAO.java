package com.fib.msgconverter.commgateway.dao.httpserverconnector.dao;

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



public class HttpServerConnectorDAO extends AbstractDAO {

	public HttpServerConnectorDAO() {
		super();
	}

	public HttpServerConnectorDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public HttpServerConnectorDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(HttpServerConnector httpServerConnector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (httpServerConnector == null) {
			throw new IllegalArgumentException("httpServerConnector is NULL!");
		}
		if( httpServerConnector.getId() != null && httpServerConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+httpServerConnector.getId());
		}
		if( httpServerConnector.getId() != null && httpServerConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+httpServerConnector.getId());
		}
		if( httpServerConnector.getStaleConnectionCheck() != null && httpServerConnector.getStaleConnectionCheck().length() < 1 ){
			throw new IllegalArgumentException("staleConnectionCheck too short"+httpServerConnector.getStaleConnectionCheck());
		}
		if( httpServerConnector.getStaleConnectionCheck() != null && httpServerConnector.getStaleConnectionCheck().length() > 1 ){
			throw new IllegalArgumentException("staleConnectionCheck too long"+httpServerConnector.getStaleConnectionCheck());
		}
		if( httpServerConnector.getTcpNodelay() != null && httpServerConnector.getTcpNodelay().length() < 1 ){
			throw new IllegalArgumentException("tcpNodelay too short"+httpServerConnector.getTcpNodelay());
		}
		if( httpServerConnector.getTcpNodelay() != null && httpServerConnector.getTcpNodelay().length() > 1 ){
			throw new IllegalArgumentException("tcpNodelay too long"+httpServerConnector.getTcpNodelay());
		}
		if( httpServerConnector.getElementCharset() != null && httpServerConnector.getElementCharset().length() > 32 ){
			throw new IllegalArgumentException("elementCharset too long"+httpServerConnector.getElementCharset());
		}
		if( httpServerConnector.getContentCharset() != null && httpServerConnector.getContentCharset().length() > 32 ){
			throw new IllegalArgumentException("contentCharset too long"+httpServerConnector.getContentCharset());
		}
		if( httpServerConnector.getVerifierClassName() != null && httpServerConnector.getVerifierClassName().length() > 255 ){
			throw new IllegalArgumentException("verifierClassName too long"+httpServerConnector.getVerifierClassName());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into http_server_connector(id,port,timeout,socket_buffer_size,stale_connection_check,tcp_nodelay,element_charset,content_charset,backlog,verifier_class_name) values(?,?,?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, httpServerConnector.getId());
			statment.setInt(2, httpServerConnector.getPort());
			statment.setInt(3, httpServerConnector.getTimeout());
			statment.setInt(4, httpServerConnector.getSocketBufferSize());
			statment.setString(5, httpServerConnector.getStaleConnectionCheck());
			statment.setString(6, httpServerConnector.getTcpNodelay());
			statment.setString(7, httpServerConnector.getElementCharset());
			statment.setString(8, httpServerConnector.getContentCharset());
			statment.setInt(9, httpServerConnector.getBacklog());
			statment.setString(10, httpServerConnector.getVerifierClassName());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("HttpServerConnectorDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+httpServerConnector.getId() +"\",port = "+httpServerConnector.getPort()+",timeout = "+httpServerConnector.getTimeout()+",socket_buffer_size = "+httpServerConnector.getSocketBufferSize()+",stale_connection_check = \""+httpServerConnector.getStaleConnectionCheck() +"\",tcp_nodelay = \""+httpServerConnector.getTcpNodelay() +"\",element_charset = \""+httpServerConnector.getElementCharset() +"\",content_charset = \""+httpServerConnector.getContentCharset() +"\",backlog = "+httpServerConnector.getBacklog()+",verifier_class_name = \""+httpServerConnector.getVerifierClassName() +"\" ");
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


	public int[] insertBatch(List<HttpServerConnector> httpServerConnectorList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (httpServerConnectorList == null) {
			throw new IllegalArgumentException("httpServerConnectorList is NULL!");
		}
		for( HttpServerConnector httpServerConnector : httpServerConnectorList){
		if( httpServerConnector.getId() != null && httpServerConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+httpServerConnector.getId());
		}
		if( httpServerConnector.getId() != null && httpServerConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+httpServerConnector.getId());
		}
		if( httpServerConnector.getStaleConnectionCheck() != null && httpServerConnector.getStaleConnectionCheck().length() < 1 ){
			throw new IllegalArgumentException("staleConnectionCheck too short"+httpServerConnector.getStaleConnectionCheck());
		}
		if( httpServerConnector.getStaleConnectionCheck() != null && httpServerConnector.getStaleConnectionCheck().length() > 1 ){
			throw new IllegalArgumentException("staleConnectionCheck too long"+httpServerConnector.getStaleConnectionCheck());
		}
		if( httpServerConnector.getTcpNodelay() != null && httpServerConnector.getTcpNodelay().length() < 1 ){
			throw new IllegalArgumentException("tcpNodelay too short"+httpServerConnector.getTcpNodelay());
		}
		if( httpServerConnector.getTcpNodelay() != null && httpServerConnector.getTcpNodelay().length() > 1 ){
			throw new IllegalArgumentException("tcpNodelay too long"+httpServerConnector.getTcpNodelay());
		}
		if( httpServerConnector.getElementCharset() != null && httpServerConnector.getElementCharset().length() > 32 ){
			throw new IllegalArgumentException("elementCharset too long"+httpServerConnector.getElementCharset());
		}
		if( httpServerConnector.getContentCharset() != null && httpServerConnector.getContentCharset().length() > 32 ){
			throw new IllegalArgumentException("contentCharset too long"+httpServerConnector.getContentCharset());
		}
		if( httpServerConnector.getVerifierClassName() != null && httpServerConnector.getVerifierClassName().length() > 255 ){
			throw new IllegalArgumentException("verifierClassName too long"+httpServerConnector.getVerifierClassName());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into http_server_connector(id,port,timeout,socket_buffer_size,stale_connection_check,tcp_nodelay,element_charset,content_charset,backlog,verifier_class_name) values(?,?,?,?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( HttpServerConnector httpServerConnector : httpServerConnectorList){
			statment.setString(1, httpServerConnector.getId());
			statment.setInt(2, httpServerConnector.getPort());
			statment.setInt(3, httpServerConnector.getTimeout());
			statment.setInt(4, httpServerConnector.getSocketBufferSize());
			statment.setString(5, httpServerConnector.getStaleConnectionCheck());
			statment.setString(6, httpServerConnector.getTcpNodelay());
			statment.setString(7, httpServerConnector.getElementCharset());
			statment.setString(8, httpServerConnector.getContentCharset());
			statment.setInt(9, httpServerConnector.getBacklog());
			statment.setString(10, httpServerConnector.getVerifierClassName());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("HttpServerConnectorDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(HttpServerConnector httpServerConnector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (httpServerConnector == null) {
			throw new IllegalArgumentException("httpServerConnector is NULL!");
		}

		PreparedStatement statment = null;
		if( httpServerConnector.getId() != null && httpServerConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+httpServerConnector.getId());
		}
		if( httpServerConnector.getId() != null && httpServerConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+httpServerConnector.getId());
		}
		if( httpServerConnector.getStaleConnectionCheck() != null && httpServerConnector.getStaleConnectionCheck().length() < 1 ){
			throw new IllegalArgumentException("staleConnectionCheck too short"+httpServerConnector.getStaleConnectionCheck());
		}
		if( httpServerConnector.getStaleConnectionCheck() != null && httpServerConnector.getStaleConnectionCheck().length() > 1 ){
			throw new IllegalArgumentException("staleConnectionCheck too long"+httpServerConnector.getStaleConnectionCheck());
		}
		if( httpServerConnector.getTcpNodelay() != null && httpServerConnector.getTcpNodelay().length() < 1 ){
			throw new IllegalArgumentException("tcpNodelay too short"+httpServerConnector.getTcpNodelay());
		}
		if( httpServerConnector.getTcpNodelay() != null && httpServerConnector.getTcpNodelay().length() > 1 ){
			throw new IllegalArgumentException("tcpNodelay too long"+httpServerConnector.getTcpNodelay());
		}
		if( httpServerConnector.getElementCharset() != null && httpServerConnector.getElementCharset().length() > 32 ){
			throw new IllegalArgumentException("elementCharset too long"+httpServerConnector.getElementCharset());
		}
		if( httpServerConnector.getContentCharset() != null && httpServerConnector.getContentCharset().length() > 32 ){
			throw new IllegalArgumentException("contentCharset too long"+httpServerConnector.getContentCharset());
		}
		if( httpServerConnector.getVerifierClassName() != null && httpServerConnector.getVerifierClassName().length() > 255 ){
			throw new IllegalArgumentException("verifierClassName too long"+httpServerConnector.getVerifierClassName());
		}
		try {
			String sql =  "UPDATE http_server_connector SET id=?,port=?,timeout=?,socket_buffer_size=?,stale_connection_check=?,tcp_nodelay=?,element_charset=?,content_charset=?,backlog=?,verifier_class_name=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, httpServerConnector.getId());
			statment.setInt(2, httpServerConnector.getPort());
			statment.setInt(3, httpServerConnector.getTimeout());
			statment.setInt(4, httpServerConnector.getSocketBufferSize());
			statment.setString(5, httpServerConnector.getStaleConnectionCheck());
			statment.setString(6, httpServerConnector.getTcpNodelay());
			statment.setString(7, httpServerConnector.getElementCharset());
			statment.setString(8, httpServerConnector.getContentCharset());
			statment.setInt(9, httpServerConnector.getBacklog());
			statment.setString(10, httpServerConnector.getVerifierClassName());
			statment.setString(11, httpServerConnector.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("HttpServerConnectorDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+httpServerConnector.getId() +"\",port = "+httpServerConnector.getPort()+",timeout = "+httpServerConnector.getTimeout()+",socket_buffer_size = "+httpServerConnector.getSocketBufferSize()+",stale_connection_check = \""+httpServerConnector.getStaleConnectionCheck() +"\",tcp_nodelay = \""+httpServerConnector.getTcpNodelay() +"\",element_charset = \""+httpServerConnector.getElementCharset() +"\",content_charset = \""+httpServerConnector.getContentCharset() +"\",backlog = "+httpServerConnector.getBacklog()+",verifier_class_name = \""+httpServerConnector.getVerifierClassName() +"\" ");
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
			sql.append("UPDATE http_server_connector SET ");
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
				if("port".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("timeout".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("socket_buffer_size".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("stale_connection_check".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("tcp_nodelay".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("element_charset".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("content_charset".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("backlog".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("verifier_class_name".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("HttpServerConnectorDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM http_server_connector where id=? ";
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
				debug("HttpServerConnectorDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public HttpServerConnector selectByPK (String id)  {
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
		HttpServerConnector returnDTO = null;

		try {
			String sql =  "SELECT * FROM http_server_connector where id=? ";
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
				debug("HttpServerConnectorDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new HttpServerConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setSocketBufferSize(resultSet.getInt("socket_buffer_size"));
				returnDTO.setStaleConnectionCheck(resultSet.getString("stale_connection_check"));
				returnDTO.setTcpNodelay(resultSet.getString("tcp_nodelay"));
				returnDTO.setElementCharset(resultSet.getString("element_charset"));
				returnDTO.setContentCharset(resultSet.getString("content_charset"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setVerifierClassName(resultSet.getString("verifier_class_name"));
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
		HttpServerConnector returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from http_server_connector order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HttpServerConnectorDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new HttpServerConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setSocketBufferSize(resultSet.getInt("socket_buffer_size"));
				returnDTO.setStaleConnectionCheck(resultSet.getString("stale_connection_check"));
				returnDTO.setTcpNodelay(resultSet.getString("tcp_nodelay"));
				returnDTO.setElementCharset(resultSet.getString("element_charset"));
				returnDTO.setContentCharset(resultSet.getString("content_charset"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setVerifierClassName(resultSet.getString("verifier_class_name"));
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
		HttpServerConnector returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from http_server_connector ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from http_server_connector					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,port,timeout,socket_buffer_size,stale_connection_check,tcp_nodelay,element_charset,content_charset,backlog,verifier_class_name, rownumber() over(order by id) as rn from http_server_connector ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from http_server_connector order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,port,timeout,socket_buffer_size,stale_connection_check,tcp_nodelay,element_charset,content_charset,backlog,verifier_class_name from http_server_connector ) A where rownumber > "  + startNum ;
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
				debug("HttpServerConnectorDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new HttpServerConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setSocketBufferSize(resultSet.getInt("socket_buffer_size"));
				returnDTO.setStaleConnectionCheck(resultSet.getString("stale_connection_check"));
				returnDTO.setTcpNodelay(resultSet.getString("tcp_nodelay"));
				returnDTO.setElementCharset(resultSet.getString("element_charset"));
				returnDTO.setContentCharset(resultSet.getString("content_charset"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setVerifierClassName(resultSet.getString("verifier_class_name"));
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
		HttpServerConnector returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from http_server_connector where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HttpServerConnectorDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new HttpServerConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setSocketBufferSize(resultSet.getInt("socket_buffer_size"));
				returnDTO.setStaleConnectionCheck(resultSet.getString("stale_connection_check"));
				returnDTO.setTcpNodelay(resultSet.getString("tcp_nodelay"));
				returnDTO.setElementCharset(resultSet.getString("element_charset"));
				returnDTO.setContentCharset(resultSet.getString("content_charset"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setVerifierClassName(resultSet.getString("verifier_class_name"));
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
		HttpServerConnector returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from http_server_connector where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from http_server_connector where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from http_server_connector					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,port,timeout,socket_buffer_size,stale_connection_check,tcp_nodelay,element_charset,content_charset,backlog,verifier_class_name, rownumber() over(order by id) as rn from http_server_connector where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,port,timeout,socket_buffer_size,stale_connection_check,tcp_nodelay,element_charset,content_charset,backlog,verifier_class_name from http_server_connector  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("HttpServerConnectorDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new HttpServerConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setSocketBufferSize(resultSet.getInt("socket_buffer_size"));
				returnDTO.setStaleConnectionCheck(resultSet.getString("stale_connection_check"));
				returnDTO.setTcpNodelay(resultSet.getString("tcp_nodelay"));
				returnDTO.setElementCharset(resultSet.getString("element_charset"));
				returnDTO.setContentCharset(resultSet.getString("content_charset"));
				returnDTO.setBacklog(resultSet.getInt("backlog"));
				returnDTO.setVerifierClassName(resultSet.getString("verifier_class_name"));
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
			String sql =  "select  count(*) from http_server_connector";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HttpServerConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from http_server_connector where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HttpServerConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
