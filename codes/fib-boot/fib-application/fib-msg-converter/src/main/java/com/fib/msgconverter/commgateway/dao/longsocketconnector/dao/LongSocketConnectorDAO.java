package com.fib.msgconverter.commgateway.dao.longsocketconnector.dao;

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



public class LongSocketConnectorDAO extends AbstractDAO {

	public LongSocketConnectorDAO() {
		super();
	}

	public LongSocketConnectorDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public LongSocketConnectorDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(LongSocketConnector longSocketConnector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longSocketConnector == null) {
			throw new IllegalArgumentException("longSocketConnector is NULL!");
		}
		if( longSocketConnector.getId() != null && longSocketConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+longSocketConnector.getId());
		}
		if( longSocketConnector.getId() != null && longSocketConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+longSocketConnector.getId());
		}
		if( longSocketConnector.getLoginId() != null && longSocketConnector.getLoginId().length() < 10 ){
			throw new IllegalArgumentException("loginId too short"+longSocketConnector.getLoginId());
		}
		if( longSocketConnector.getLoginId() != null && longSocketConnector.getLoginId().length() > 10 ){
			throw new IllegalArgumentException("loginId too long"+longSocketConnector.getLoginId());
		}
		if( longSocketConnector.getRequestSerialNumberRecognizerId() != null && longSocketConnector.getRequestSerialNumberRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("requestSerialNumberRecognizerId too short"+longSocketConnector.getRequestSerialNumberRecognizerId());
		}
		if( longSocketConnector.getRequestSerialNumberRecognizerId() != null && longSocketConnector.getRequestSerialNumberRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("requestSerialNumberRecognizerId too long"+longSocketConnector.getRequestSerialNumberRecognizerId());
		}
		if( longSocketConnector.getResponseSerialNumberRecognizerId() != null && longSocketConnector.getResponseSerialNumberRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("responseSerialNumberRecognizerId too short"+longSocketConnector.getResponseSerialNumberRecognizerId());
		}
		if( longSocketConnector.getResponseSerialNumberRecognizerId() != null && longSocketConnector.getResponseSerialNumberRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("responseSerialNumberRecognizerId too long"+longSocketConnector.getResponseSerialNumberRecognizerId());
		}
		if( longSocketConnector.getCodeRecognizerId() != null && longSocketConnector.getCodeRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("codeRecognizerId too short"+longSocketConnector.getCodeRecognizerId());
		}
		if( longSocketConnector.getCodeRecognizerId() != null && longSocketConnector.getCodeRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("codeRecognizerId too long"+longSocketConnector.getCodeRecognizerId());
		}
		if( longSocketConnector.getReaderId() != null && longSocketConnector.getReaderId().length() < 10 ){
			throw new IllegalArgumentException("readerId too short"+longSocketConnector.getReaderId());
		}
		if( longSocketConnector.getReaderId() != null && longSocketConnector.getReaderId().length() > 10 ){
			throw new IllegalArgumentException("readerId too long"+longSocketConnector.getReaderId());
		}
		if( longSocketConnector.getWriterId() != null && longSocketConnector.getWriterId().length() < 10 ){
			throw new IllegalArgumentException("writerId too short"+longSocketConnector.getWriterId());
		}
		if( longSocketConnector.getWriterId() != null && longSocketConnector.getWriterId().length() > 10 ){
			throw new IllegalArgumentException("writerId too long"+longSocketConnector.getWriterId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into long_socket_connector(id,login_id,request_serial_number_recognizer_id,response_serial_number_recognizer_id,code_recognizer_id,reader_id,writer_id) values(?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, longSocketConnector.getId());
			statment.setString(2, longSocketConnector.getLoginId());
			statment.setString(3, longSocketConnector.getRequestSerialNumberRecognizerId());
			statment.setString(4, longSocketConnector.getResponseSerialNumberRecognizerId());
			statment.setString(5, longSocketConnector.getCodeRecognizerId());
			statment.setString(6, longSocketConnector.getReaderId());
			statment.setString(7, longSocketConnector.getWriterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongSocketConnectorDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+longSocketConnector.getId() +"\",login_id = \""+longSocketConnector.getLoginId() +"\",request_serial_number_recognizer_id = \""+longSocketConnector.getRequestSerialNumberRecognizerId() +"\",response_serial_number_recognizer_id = \""+longSocketConnector.getResponseSerialNumberRecognizerId() +"\",code_recognizer_id = \""+longSocketConnector.getCodeRecognizerId() +"\",reader_id = \""+longSocketConnector.getReaderId() +"\",writer_id = \""+longSocketConnector.getWriterId() +"\" ");
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


	public int[] insertBatch(List<LongSocketConnector> longSocketConnectorList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (longSocketConnectorList == null) {
			throw new IllegalArgumentException("longSocketConnectorList is NULL!");
		}
		for( LongSocketConnector longSocketConnector : longSocketConnectorList){
		if( longSocketConnector.getId() != null && longSocketConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+longSocketConnector.getId());
		}
		if( longSocketConnector.getId() != null && longSocketConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+longSocketConnector.getId());
		}
		if( longSocketConnector.getLoginId() != null && longSocketConnector.getLoginId().length() < 10 ){
			throw new IllegalArgumentException("loginId too short"+longSocketConnector.getLoginId());
		}
		if( longSocketConnector.getLoginId() != null && longSocketConnector.getLoginId().length() > 10 ){
			throw new IllegalArgumentException("loginId too long"+longSocketConnector.getLoginId());
		}
		if( longSocketConnector.getRequestSerialNumberRecognizerId() != null && longSocketConnector.getRequestSerialNumberRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("requestSerialNumberRecognizerId too short"+longSocketConnector.getRequestSerialNumberRecognizerId());
		}
		if( longSocketConnector.getRequestSerialNumberRecognizerId() != null && longSocketConnector.getRequestSerialNumberRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("requestSerialNumberRecognizerId too long"+longSocketConnector.getRequestSerialNumberRecognizerId());
		}
		if( longSocketConnector.getResponseSerialNumberRecognizerId() != null && longSocketConnector.getResponseSerialNumberRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("responseSerialNumberRecognizerId too short"+longSocketConnector.getResponseSerialNumberRecognizerId());
		}
		if( longSocketConnector.getResponseSerialNumberRecognizerId() != null && longSocketConnector.getResponseSerialNumberRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("responseSerialNumberRecognizerId too long"+longSocketConnector.getResponseSerialNumberRecognizerId());
		}
		if( longSocketConnector.getCodeRecognizerId() != null && longSocketConnector.getCodeRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("codeRecognizerId too short"+longSocketConnector.getCodeRecognizerId());
		}
		if( longSocketConnector.getCodeRecognizerId() != null && longSocketConnector.getCodeRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("codeRecognizerId too long"+longSocketConnector.getCodeRecognizerId());
		}
		if( longSocketConnector.getReaderId() != null && longSocketConnector.getReaderId().length() < 10 ){
			throw new IllegalArgumentException("readerId too short"+longSocketConnector.getReaderId());
		}
		if( longSocketConnector.getReaderId() != null && longSocketConnector.getReaderId().length() > 10 ){
			throw new IllegalArgumentException("readerId too long"+longSocketConnector.getReaderId());
		}
		if( longSocketConnector.getWriterId() != null && longSocketConnector.getWriterId().length() < 10 ){
			throw new IllegalArgumentException("writerId too short"+longSocketConnector.getWriterId());
		}
		if( longSocketConnector.getWriterId() != null && longSocketConnector.getWriterId().length() > 10 ){
			throw new IllegalArgumentException("writerId too long"+longSocketConnector.getWriterId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into long_socket_connector(id,login_id,request_serial_number_recognizer_id,response_serial_number_recognizer_id,code_recognizer_id,reader_id,writer_id) values(?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( LongSocketConnector longSocketConnector : longSocketConnectorList){
			statment.setString(1, longSocketConnector.getId());
			statment.setString(2, longSocketConnector.getLoginId());
			statment.setString(3, longSocketConnector.getRequestSerialNumberRecognizerId());
			statment.setString(4, longSocketConnector.getResponseSerialNumberRecognizerId());
			statment.setString(5, longSocketConnector.getCodeRecognizerId());
			statment.setString(6, longSocketConnector.getReaderId());
			statment.setString(7, longSocketConnector.getWriterId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongSocketConnectorDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(LongSocketConnector longSocketConnector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (longSocketConnector == null) {
			throw new IllegalArgumentException("longSocketConnector is NULL!");
		}

		PreparedStatement statment = null;
		if( longSocketConnector.getId() != null && longSocketConnector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+longSocketConnector.getId());
		}
		if( longSocketConnector.getId() != null && longSocketConnector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+longSocketConnector.getId());
		}
		if( longSocketConnector.getLoginId() != null && longSocketConnector.getLoginId().length() < 10 ){
			throw new IllegalArgumentException("loginId too short"+longSocketConnector.getLoginId());
		}
		if( longSocketConnector.getLoginId() != null && longSocketConnector.getLoginId().length() > 10 ){
			throw new IllegalArgumentException("loginId too long"+longSocketConnector.getLoginId());
		}
		if( longSocketConnector.getRequestSerialNumberRecognizerId() != null && longSocketConnector.getRequestSerialNumberRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("requestSerialNumberRecognizerId too short"+longSocketConnector.getRequestSerialNumberRecognizerId());
		}
		if( longSocketConnector.getRequestSerialNumberRecognizerId() != null && longSocketConnector.getRequestSerialNumberRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("requestSerialNumberRecognizerId too long"+longSocketConnector.getRequestSerialNumberRecognizerId());
		}
		if( longSocketConnector.getResponseSerialNumberRecognizerId() != null && longSocketConnector.getResponseSerialNumberRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("responseSerialNumberRecognizerId too short"+longSocketConnector.getResponseSerialNumberRecognizerId());
		}
		if( longSocketConnector.getResponseSerialNumberRecognizerId() != null && longSocketConnector.getResponseSerialNumberRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("responseSerialNumberRecognizerId too long"+longSocketConnector.getResponseSerialNumberRecognizerId());
		}
		if( longSocketConnector.getCodeRecognizerId() != null && longSocketConnector.getCodeRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("codeRecognizerId too short"+longSocketConnector.getCodeRecognizerId());
		}
		if( longSocketConnector.getCodeRecognizerId() != null && longSocketConnector.getCodeRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("codeRecognizerId too long"+longSocketConnector.getCodeRecognizerId());
		}
		if( longSocketConnector.getReaderId() != null && longSocketConnector.getReaderId().length() < 10 ){
			throw new IllegalArgumentException("readerId too short"+longSocketConnector.getReaderId());
		}
		if( longSocketConnector.getReaderId() != null && longSocketConnector.getReaderId().length() > 10 ){
			throw new IllegalArgumentException("readerId too long"+longSocketConnector.getReaderId());
		}
		if( longSocketConnector.getWriterId() != null && longSocketConnector.getWriterId().length() < 10 ){
			throw new IllegalArgumentException("writerId too short"+longSocketConnector.getWriterId());
		}
		if( longSocketConnector.getWriterId() != null && longSocketConnector.getWriterId().length() > 10 ){
			throw new IllegalArgumentException("writerId too long"+longSocketConnector.getWriterId());
		}
		try {
			String sql =  "UPDATE long_socket_connector SET id=?,login_id=?,request_serial_number_recognizer_id=?,response_serial_number_recognizer_id=?,code_recognizer_id=?,reader_id=?,writer_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, longSocketConnector.getId());
			statment.setString(2, longSocketConnector.getLoginId());
			statment.setString(3, longSocketConnector.getRequestSerialNumberRecognizerId());
			statment.setString(4, longSocketConnector.getResponseSerialNumberRecognizerId());
			statment.setString(5, longSocketConnector.getCodeRecognizerId());
			statment.setString(6, longSocketConnector.getReaderId());
			statment.setString(7, longSocketConnector.getWriterId());
			statment.setString(8, longSocketConnector.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("LongSocketConnectorDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+longSocketConnector.getId() +"\",login_id = \""+longSocketConnector.getLoginId() +"\",request_serial_number_recognizer_id = \""+longSocketConnector.getRequestSerialNumberRecognizerId() +"\",response_serial_number_recognizer_id = \""+longSocketConnector.getResponseSerialNumberRecognizerId() +"\",code_recognizer_id = \""+longSocketConnector.getCodeRecognizerId() +"\",reader_id = \""+longSocketConnector.getReaderId() +"\",writer_id = \""+longSocketConnector.getWriterId() +"\" ");
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
			sql.append("UPDATE long_socket_connector SET ");
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
				if("login_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("request_serial_number_recognizer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_serial_number_recognizer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("code_recognizer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
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
				sbDebug.append("LongSocketConnectorDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM long_socket_connector where id=? ";
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
				debug("LongSocketConnectorDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public LongSocketConnector selectByPK (String id)  {
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
		LongSocketConnector returnDTO = null;

		try {
			String sql =  "SELECT * FROM long_socket_connector where id=? ";
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
				debug("LongSocketConnectorDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new LongSocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLoginId(resultSet.getString("login_id"));
				returnDTO.setRequestSerialNumberRecognizerId(resultSet.getString("request_serial_number_recognizer_id"));
				returnDTO.setResponseSerialNumberRecognizerId(resultSet.getString("response_serial_number_recognizer_id"));
				returnDTO.setCodeRecognizerId(resultSet.getString("code_recognizer_id"));
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
		LongSocketConnector returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from long_socket_connector order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongSocketConnectorDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongSocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLoginId(resultSet.getString("login_id"));
				returnDTO.setRequestSerialNumberRecognizerId(resultSet.getString("request_serial_number_recognizer_id"));
				returnDTO.setResponseSerialNumberRecognizerId(resultSet.getString("response_serial_number_recognizer_id"));
				returnDTO.setCodeRecognizerId(resultSet.getString("code_recognizer_id"));
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
		LongSocketConnector returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_socket_connector ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from long_socket_connector					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,login_id,request_serial_number_recognizer_id,response_serial_number_recognizer_id,code_recognizer_id,reader_id,writer_id, rownumber() over(order by id) as rn from long_socket_connector ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from long_socket_connector order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,login_id,request_serial_number_recognizer_id,response_serial_number_recognizer_id,code_recognizer_id,reader_id,writer_id from long_socket_connector ) A where rownumber > "  + startNum ;
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
				debug("LongSocketConnectorDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new LongSocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLoginId(resultSet.getString("login_id"));
				returnDTO.setRequestSerialNumberRecognizerId(resultSet.getString("request_serial_number_recognizer_id"));
				returnDTO.setResponseSerialNumberRecognizerId(resultSet.getString("response_serial_number_recognizer_id"));
				returnDTO.setCodeRecognizerId(resultSet.getString("code_recognizer_id"));
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
		LongSocketConnector returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from long_socket_connector where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongSocketConnectorDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new LongSocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLoginId(resultSet.getString("login_id"));
				returnDTO.setRequestSerialNumberRecognizerId(resultSet.getString("request_serial_number_recognizer_id"));
				returnDTO.setResponseSerialNumberRecognizerId(resultSet.getString("response_serial_number_recognizer_id"));
				returnDTO.setCodeRecognizerId(resultSet.getString("code_recognizer_id"));
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
		LongSocketConnector returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from long_socket_connector where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from long_socket_connector where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from long_socket_connector					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,login_id,request_serial_number_recognizer_id,response_serial_number_recognizer_id,code_recognizer_id,reader_id,writer_id, rownumber() over(order by id) as rn from long_socket_connector where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,login_id,request_serial_number_recognizer_id,response_serial_number_recognizer_id,code_recognizer_id,reader_id,writer_id from long_socket_connector  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("LongSocketConnectorDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new LongSocketConnector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setLoginId(resultSet.getString("login_id"));
				returnDTO.setRequestSerialNumberRecognizerId(resultSet.getString("request_serial_number_recognizer_id"));
				returnDTO.setResponseSerialNumberRecognizerId(resultSet.getString("response_serial_number_recognizer_id"));
				returnDTO.setCodeRecognizerId(resultSet.getString("code_recognizer_id"));
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
			String sql =  "select  count(*) from long_socket_connector";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongSocketConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from long_socket_connector where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("LongSocketConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
