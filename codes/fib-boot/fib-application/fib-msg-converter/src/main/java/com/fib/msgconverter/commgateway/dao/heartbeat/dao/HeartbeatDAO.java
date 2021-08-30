package com.fib.msgconverter.commgateway.dao.heartbeat.dao;

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



public class HeartbeatDAO extends AbstractDAO {

	public HeartbeatDAO() {
		super();
	}

	public HeartbeatDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public HeartbeatDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Heartbeat heartbeat) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (heartbeat == null) {
			throw new IllegalArgumentException("heartbeat is NULL!");
		}
		if( heartbeat.getId() != null && heartbeat.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+heartbeat.getId());
		}
		if( heartbeat.getId() != null && heartbeat.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+heartbeat.getId());
		}
		if( heartbeat.getConnectionId() != null && heartbeat.getConnectionId().length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+heartbeat.getConnectionId());
		}
		if( heartbeat.getConnectionId() != null && heartbeat.getConnectionId().length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+heartbeat.getConnectionId());
		}
		if( heartbeat.getDirection() != null && heartbeat.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+heartbeat.getDirection());
		}
		if( heartbeat.getDirection() != null && heartbeat.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+heartbeat.getDirection());
		}
		if( heartbeat.getMessageSymbolId() != null && heartbeat.getMessageSymbolId().length() < 10 ){
			throw new IllegalArgumentException("messageSymbolId too short"+heartbeat.getMessageSymbolId());
		}
		if( heartbeat.getMessageSymbolId() != null && heartbeat.getMessageSymbolId().length() > 10 ){
			throw new IllegalArgumentException("messageSymbolId too long"+heartbeat.getMessageSymbolId());
		}
		if( heartbeat.getResponseTurnBack() != null && heartbeat.getResponseTurnBack().length() < 1 ){
			throw new IllegalArgumentException("responseTurnBack too short"+heartbeat.getResponseTurnBack());
		}
		if( heartbeat.getResponseTurnBack() != null && heartbeat.getResponseTurnBack().length() > 1 ){
			throw new IllegalArgumentException("responseTurnBack too long"+heartbeat.getResponseTurnBack());
		}
		if( heartbeat.getResponseMessageSymbolId() != null && heartbeat.getResponseMessageSymbolId().length() < 10 ){
			throw new IllegalArgumentException("responseMessageSymbolId too short"+heartbeat.getResponseMessageSymbolId());
		}
		if( heartbeat.getResponseMessageSymbolId() != null && heartbeat.getResponseMessageSymbolId().length() > 10 ){
			throw new IllegalArgumentException("responseMessageSymbolId too long"+heartbeat.getResponseMessageSymbolId());
		}
		if( heartbeat.getResponseConnectionId() != null && heartbeat.getResponseConnectionId().length() < 10 ){
			throw new IllegalArgumentException("responseConnectionId too short"+heartbeat.getResponseConnectionId());
		}
		if( heartbeat.getResponseConnectionId() != null && heartbeat.getResponseConnectionId().length() > 10 ){
			throw new IllegalArgumentException("responseConnectionId too long"+heartbeat.getResponseConnectionId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into heartbeat(id,connection_id,direction,send_interval,message_symbol_id,response_turn_back,response_message_symbol_id,response_connection_id) values(?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, heartbeat.getId());
			statment.setString(2, heartbeat.getConnectionId());
			statment.setString(3, heartbeat.getDirection());
			statment.setInt(4, heartbeat.getSendInterval());
			statment.setString(5, heartbeat.getMessageSymbolId());
			statment.setString(6, heartbeat.getResponseTurnBack());
			statment.setString(7, heartbeat.getResponseMessageSymbolId());
			statment.setString(8, heartbeat.getResponseConnectionId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("HeartbeatDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+heartbeat.getId() +"\",connection_id = \""+heartbeat.getConnectionId() +"\",direction = \""+heartbeat.getDirection() +"\",send_interval = "+heartbeat.getSendInterval()+",message_symbol_id = \""+heartbeat.getMessageSymbolId() +"\",response_turn_back = \""+heartbeat.getResponseTurnBack() +"\",response_message_symbol_id = \""+heartbeat.getResponseMessageSymbolId() +"\",response_connection_id = \""+heartbeat.getResponseConnectionId() +"\" ");
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


	public int[] insertBatch(List<Heartbeat> heartbeatList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (heartbeatList == null) {
			throw new IllegalArgumentException("heartbeatList is NULL!");
		}
		for( Heartbeat heartbeat : heartbeatList){
		if( heartbeat.getId() != null && heartbeat.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+heartbeat.getId());
		}
		if( heartbeat.getId() != null && heartbeat.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+heartbeat.getId());
		}
		if( heartbeat.getConnectionId() != null && heartbeat.getConnectionId().length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+heartbeat.getConnectionId());
		}
		if( heartbeat.getConnectionId() != null && heartbeat.getConnectionId().length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+heartbeat.getConnectionId());
		}
		if( heartbeat.getDirection() != null && heartbeat.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+heartbeat.getDirection());
		}
		if( heartbeat.getDirection() != null && heartbeat.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+heartbeat.getDirection());
		}
		if( heartbeat.getMessageSymbolId() != null && heartbeat.getMessageSymbolId().length() < 10 ){
			throw new IllegalArgumentException("messageSymbolId too short"+heartbeat.getMessageSymbolId());
		}
		if( heartbeat.getMessageSymbolId() != null && heartbeat.getMessageSymbolId().length() > 10 ){
			throw new IllegalArgumentException("messageSymbolId too long"+heartbeat.getMessageSymbolId());
		}
		if( heartbeat.getResponseTurnBack() != null && heartbeat.getResponseTurnBack().length() < 1 ){
			throw new IllegalArgumentException("responseTurnBack too short"+heartbeat.getResponseTurnBack());
		}
		if( heartbeat.getResponseTurnBack() != null && heartbeat.getResponseTurnBack().length() > 1 ){
			throw new IllegalArgumentException("responseTurnBack too long"+heartbeat.getResponseTurnBack());
		}
		if( heartbeat.getResponseMessageSymbolId() != null && heartbeat.getResponseMessageSymbolId().length() < 10 ){
			throw new IllegalArgumentException("responseMessageSymbolId too short"+heartbeat.getResponseMessageSymbolId());
		}
		if( heartbeat.getResponseMessageSymbolId() != null && heartbeat.getResponseMessageSymbolId().length() > 10 ){
			throw new IllegalArgumentException("responseMessageSymbolId too long"+heartbeat.getResponseMessageSymbolId());
		}
		if( heartbeat.getResponseConnectionId() != null && heartbeat.getResponseConnectionId().length() < 10 ){
			throw new IllegalArgumentException("responseConnectionId too short"+heartbeat.getResponseConnectionId());
		}
		if( heartbeat.getResponseConnectionId() != null && heartbeat.getResponseConnectionId().length() > 10 ){
			throw new IllegalArgumentException("responseConnectionId too long"+heartbeat.getResponseConnectionId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into heartbeat(id,connection_id,direction,send_interval,message_symbol_id,response_turn_back,response_message_symbol_id,response_connection_id) values(?,?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( Heartbeat heartbeat : heartbeatList){
			statment.setString(1, heartbeat.getId());
			statment.setString(2, heartbeat.getConnectionId());
			statment.setString(3, heartbeat.getDirection());
			statment.setInt(4, heartbeat.getSendInterval());
			statment.setString(5, heartbeat.getMessageSymbolId());
			statment.setString(6, heartbeat.getResponseTurnBack());
			statment.setString(7, heartbeat.getResponseMessageSymbolId());
			statment.setString(8, heartbeat.getResponseConnectionId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("HeartbeatDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(Heartbeat heartbeat) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (heartbeat == null) {
			throw new IllegalArgumentException("heartbeat is NULL!");
		}

		PreparedStatement statment = null;
		if( heartbeat.getId() != null && heartbeat.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+heartbeat.getId());
		}
		if( heartbeat.getId() != null && heartbeat.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+heartbeat.getId());
		}
		if( heartbeat.getConnectionId() != null && heartbeat.getConnectionId().length() < 10 ){
			throw new IllegalArgumentException("connectionId too short"+heartbeat.getConnectionId());
		}
		if( heartbeat.getConnectionId() != null && heartbeat.getConnectionId().length() > 10 ){
			throw new IllegalArgumentException("connectionId too long"+heartbeat.getConnectionId());
		}
		if( heartbeat.getDirection() != null && heartbeat.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+heartbeat.getDirection());
		}
		if( heartbeat.getDirection() != null && heartbeat.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+heartbeat.getDirection());
		}
		if( heartbeat.getMessageSymbolId() != null && heartbeat.getMessageSymbolId().length() < 10 ){
			throw new IllegalArgumentException("messageSymbolId too short"+heartbeat.getMessageSymbolId());
		}
		if( heartbeat.getMessageSymbolId() != null && heartbeat.getMessageSymbolId().length() > 10 ){
			throw new IllegalArgumentException("messageSymbolId too long"+heartbeat.getMessageSymbolId());
		}
		if( heartbeat.getResponseTurnBack() != null && heartbeat.getResponseTurnBack().length() < 1 ){
			throw new IllegalArgumentException("responseTurnBack too short"+heartbeat.getResponseTurnBack());
		}
		if( heartbeat.getResponseTurnBack() != null && heartbeat.getResponseTurnBack().length() > 1 ){
			throw new IllegalArgumentException("responseTurnBack too long"+heartbeat.getResponseTurnBack());
		}
		if( heartbeat.getResponseMessageSymbolId() != null && heartbeat.getResponseMessageSymbolId().length() < 10 ){
			throw new IllegalArgumentException("responseMessageSymbolId too short"+heartbeat.getResponseMessageSymbolId());
		}
		if( heartbeat.getResponseMessageSymbolId() != null && heartbeat.getResponseMessageSymbolId().length() > 10 ){
			throw new IllegalArgumentException("responseMessageSymbolId too long"+heartbeat.getResponseMessageSymbolId());
		}
		if( heartbeat.getResponseConnectionId() != null && heartbeat.getResponseConnectionId().length() < 10 ){
			throw new IllegalArgumentException("responseConnectionId too short"+heartbeat.getResponseConnectionId());
		}
		if( heartbeat.getResponseConnectionId() != null && heartbeat.getResponseConnectionId().length() > 10 ){
			throw new IllegalArgumentException("responseConnectionId too long"+heartbeat.getResponseConnectionId());
		}
		try {
			String sql =  "UPDATE heartbeat SET id=?,connection_id=?,direction=?,send_interval=?,message_symbol_id=?,response_turn_back=?,response_message_symbol_id=?,response_connection_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, heartbeat.getId());
			statment.setString(2, heartbeat.getConnectionId());
			statment.setString(3, heartbeat.getDirection());
			statment.setInt(4, heartbeat.getSendInterval());
			statment.setString(5, heartbeat.getMessageSymbolId());
			statment.setString(6, heartbeat.getResponseTurnBack());
			statment.setString(7, heartbeat.getResponseMessageSymbolId());
			statment.setString(8, heartbeat.getResponseConnectionId());
			statment.setString(9, heartbeat.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("HeartbeatDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+heartbeat.getId() +"\",connection_id = \""+heartbeat.getConnectionId() +"\",direction = \""+heartbeat.getDirection() +"\",send_interval = "+heartbeat.getSendInterval()+",message_symbol_id = \""+heartbeat.getMessageSymbolId() +"\",response_turn_back = \""+heartbeat.getResponseTurnBack() +"\",response_message_symbol_id = \""+heartbeat.getResponseMessageSymbolId() +"\",response_connection_id = \""+heartbeat.getResponseConnectionId() +"\" ");
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
			sql.append("UPDATE heartbeat SET ");
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
				if("send_interval".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("message_symbol_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_turn_back".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_message_symbol_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_connection_id".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("HeartbeatDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM heartbeat where id=? ";
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
				debug("HeartbeatDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public Heartbeat selectByPK (String id)  {
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
		Heartbeat returnDTO = null;

		try {
			String sql =  "SELECT * FROM heartbeat where id=? ";
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
				debug("HeartbeatDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Heartbeat();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setSendInterval(resultSet.getInt("send_interval"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setResponseTurnBack(resultSet.getString("response_turn_back"));
				returnDTO.setResponseMessageSymbolId(resultSet.getString("response_message_symbol_id"));
				returnDTO.setResponseConnectionId(resultSet.getString("response_connection_id"));
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
		Heartbeat returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from heartbeat order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HeartbeatDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Heartbeat();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setSendInterval(resultSet.getInt("send_interval"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setResponseTurnBack(resultSet.getString("response_turn_back"));
				returnDTO.setResponseMessageSymbolId(resultSet.getString("response_message_symbol_id"));
				returnDTO.setResponseConnectionId(resultSet.getString("response_connection_id"));
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
		Heartbeat returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from heartbeat ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from heartbeat					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,connection_id,direction,send_interval,message_symbol_id,response_turn_back,response_message_symbol_id,response_connection_id, rownumber() over(order by id) as rn from heartbeat ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from heartbeat order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connection_id,direction,send_interval,message_symbol_id,response_turn_back,response_message_symbol_id,response_connection_id from heartbeat ) A where rownumber > "  + startNum ;
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
				debug("HeartbeatDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Heartbeat();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setSendInterval(resultSet.getInt("send_interval"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setResponseTurnBack(resultSet.getString("response_turn_back"));
				returnDTO.setResponseMessageSymbolId(resultSet.getString("response_message_symbol_id"));
				returnDTO.setResponseConnectionId(resultSet.getString("response_connection_id"));
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
		Heartbeat returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from heartbeat where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HeartbeatDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Heartbeat();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setSendInterval(resultSet.getInt("send_interval"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setResponseTurnBack(resultSet.getString("response_turn_back"));
				returnDTO.setResponseMessageSymbolId(resultSet.getString("response_message_symbol_id"));
				returnDTO.setResponseConnectionId(resultSet.getString("response_connection_id"));
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
		Heartbeat returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from heartbeat where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from heartbeat where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from heartbeat					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,connection_id,direction,send_interval,message_symbol_id,response_turn_back,response_message_symbol_id,response_connection_id, rownumber() over(order by id) as rn from heartbeat where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connection_id,direction,send_interval,message_symbol_id,response_turn_back,response_message_symbol_id,response_connection_id from heartbeat  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("HeartbeatDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Heartbeat();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectionId(resultSet.getString("connection_id"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setSendInterval(resultSet.getInt("send_interval"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setResponseTurnBack(resultSet.getString("response_turn_back"));
				returnDTO.setResponseMessageSymbolId(resultSet.getString("response_message_symbol_id"));
				returnDTO.setResponseConnectionId(resultSet.getString("response_connection_id"));
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
			String sql =  "select  count(*) from heartbeat";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HeartbeatDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from heartbeat where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("HeartbeatDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
