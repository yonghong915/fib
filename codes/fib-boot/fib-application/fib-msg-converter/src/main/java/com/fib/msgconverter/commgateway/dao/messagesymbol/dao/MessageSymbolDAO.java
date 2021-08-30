package com.fib.msgconverter.commgateway.dao.messagesymbol.dao;

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



public class MessageSymbolDAO extends AbstractDAO {

	public MessageSymbolDAO() {
		super();
	}

	public MessageSymbolDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MessageSymbolDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(MessageSymbol messageSymbol) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (messageSymbol == null) {
			throw new IllegalArgumentException("messageSymbol is NULL!");
		}
		if( messageSymbol.getId() != null && messageSymbol.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+messageSymbol.getId());
		}
		if( messageSymbol.getId() != null && messageSymbol.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+messageSymbol.getId());
		}
		if( messageSymbol.getMessageSymbolId()==null || "".equals(messageSymbol.getMessageSymbolId())){
			throw new IllegalArgumentException("messageSymbolId is null");
		}
		if( messageSymbol.getMessageSymbolId() != null && messageSymbol.getMessageSymbolId().length() > 255 ){
			throw new IllegalArgumentException("messageSymbolId too long"+messageSymbol.getMessageSymbolId());
		}
		if( messageSymbol.getSymbolType() != null && messageSymbol.getSymbolType().length() < 2 ){
			throw new IllegalArgumentException("symbolType too short"+messageSymbol.getSymbolType());
		}
		if( messageSymbol.getSymbolType() != null && messageSymbol.getSymbolType().length() > 2 ){
			throw new IllegalArgumentException("symbolType too long"+messageSymbol.getSymbolType());
		}
		if( messageSymbol.getDataType() != null && messageSymbol.getDataType().length() < 3 ){
			throw new IllegalArgumentException("dataType too short"+messageSymbol.getDataType());
		}
		if( messageSymbol.getDataType() != null && messageSymbol.getDataType().length() > 3 ){
			throw new IllegalArgumentException("dataType too long"+messageSymbol.getDataType());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into message_symbol(id,message_symbol_id,symbol_type,data_type,value_or_script) values(?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, messageSymbol.getId());
			statment.setString(2, messageSymbol.getMessageSymbolId());
			statment.setString(3, messageSymbol.getSymbolType());
			statment.setString(4, messageSymbol.getDataType());
			statment.setBytes(5, messageSymbol.getValueOrScript());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MessageSymbolDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+messageSymbol.getId() +"\",message_symbol_id = \""+messageSymbol.getMessageSymbolId() +"\",symbol_type = \""+messageSymbol.getSymbolType() +"\",data_type = \""+messageSymbol.getDataType() +"\",value_or_script = "+messageSymbol.getValueOrScript()+" ");
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


	public int[] insertBatch(List<MessageSymbol> messageSymbolList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (messageSymbolList == null) {
			throw new IllegalArgumentException("messageSymbolList is NULL!");
		}
		for( MessageSymbol messageSymbol : messageSymbolList){
		if( messageSymbol.getId() != null && messageSymbol.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+messageSymbol.getId());
		}
		if( messageSymbol.getId() != null && messageSymbol.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+messageSymbol.getId());
		}
		if( messageSymbol.getMessageSymbolId()==null || "".equals(messageSymbol.getMessageSymbolId())){
			throw new IllegalArgumentException("messageSymbolId is null");
		}
		if( messageSymbol.getMessageSymbolId() != null && messageSymbol.getMessageSymbolId().length() > 255 ){
			throw new IllegalArgumentException("messageSymbolId too long"+messageSymbol.getMessageSymbolId());
		}
		if( messageSymbol.getSymbolType() != null && messageSymbol.getSymbolType().length() < 2 ){
			throw new IllegalArgumentException("symbolType too short"+messageSymbol.getSymbolType());
		}
		if( messageSymbol.getSymbolType() != null && messageSymbol.getSymbolType().length() > 2 ){
			throw new IllegalArgumentException("symbolType too long"+messageSymbol.getSymbolType());
		}
		if( messageSymbol.getDataType() != null && messageSymbol.getDataType().length() < 3 ){
			throw new IllegalArgumentException("dataType too short"+messageSymbol.getDataType());
		}
		if( messageSymbol.getDataType() != null && messageSymbol.getDataType().length() > 3 ){
			throw new IllegalArgumentException("dataType too long"+messageSymbol.getDataType());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into message_symbol(id,message_symbol_id,symbol_type,data_type,value_or_script) values(?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( MessageSymbol messageSymbol : messageSymbolList){
			statment.setString(1, messageSymbol.getId());
			statment.setString(2, messageSymbol.getMessageSymbolId());
			statment.setString(3, messageSymbol.getSymbolType());
			statment.setString(4, messageSymbol.getDataType());
			statment.setBytes(5, messageSymbol.getValueOrScript());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MessageSymbolDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(MessageSymbol messageSymbol) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (messageSymbol == null) {
			throw new IllegalArgumentException("messageSymbol is NULL!");
		}

		PreparedStatement statment = null;
		if( messageSymbol.getId() != null && messageSymbol.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+messageSymbol.getId());
		}
		if( messageSymbol.getId() != null && messageSymbol.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+messageSymbol.getId());
		}
		if( messageSymbol.getMessageSymbolId()==null || "".equals(messageSymbol.getMessageSymbolId())){
			throw new IllegalArgumentException("messageSymbolId is null");
		}
		if( messageSymbol.getMessageSymbolId() != null && messageSymbol.getMessageSymbolId().length() > 255 ){
			throw new IllegalArgumentException("messageSymbolId too long"+messageSymbol.getMessageSymbolId());
		}
		if( messageSymbol.getSymbolType() != null && messageSymbol.getSymbolType().length() < 2 ){
			throw new IllegalArgumentException("symbolType too short"+messageSymbol.getSymbolType());
		}
		if( messageSymbol.getSymbolType() != null && messageSymbol.getSymbolType().length() > 2 ){
			throw new IllegalArgumentException("symbolType too long"+messageSymbol.getSymbolType());
		}
		if( messageSymbol.getDataType() != null && messageSymbol.getDataType().length() < 3 ){
			throw new IllegalArgumentException("dataType too short"+messageSymbol.getDataType());
		}
		if( messageSymbol.getDataType() != null && messageSymbol.getDataType().length() > 3 ){
			throw new IllegalArgumentException("dataType too long"+messageSymbol.getDataType());
		}
		try {
			String sql =  "UPDATE message_symbol SET id=?,message_symbol_id=?,symbol_type=?,data_type=?,value_or_script=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, messageSymbol.getId());
			statment.setString(2, messageSymbol.getMessageSymbolId());
			statment.setString(3, messageSymbol.getSymbolType());
			statment.setString(4, messageSymbol.getDataType());
			statment.setBytes(5, messageSymbol.getValueOrScript());
			statment.setString(6, messageSymbol.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MessageSymbolDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+messageSymbol.getId() +"\",message_symbol_id = \""+messageSymbol.getMessageSymbolId() +"\",symbol_type = \""+messageSymbol.getSymbolType() +"\",data_type = \""+messageSymbol.getDataType() +"\",value_or_script = "+messageSymbol.getValueOrScript()+" ");
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
			sql.append("UPDATE message_symbol SET ");
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
				if("message_symbol_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("symbol_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("data_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("value_or_script".equalsIgnoreCase(tmpKey)){
					statment.setBytes(m++, (byte[])updateFields.get(tmpKey));
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
				sbDebug.append("MessageSymbolDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM message_symbol where id=? ";
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
				debug("MessageSymbolDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public MessageSymbol selectByPK (String id)  {
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
		MessageSymbol returnDTO = null;

		try {
			String sql =  "SELECT * FROM message_symbol where id=? ";
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
				debug("MessageSymbolDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new MessageSymbol();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setSymbolType(resultSet.getString("symbol_type"));
				returnDTO.setDataType(resultSet.getString("data_type"));
				returnDTO.setValueOrScript(resultSet.getBytes("value_or_script"));
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
		MessageSymbol returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from message_symbol order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MessageSymbolDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new MessageSymbol();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setSymbolType(resultSet.getString("symbol_type"));
				returnDTO.setDataType(resultSet.getString("data_type"));
				returnDTO.setValueOrScript(resultSet.getBytes("value_or_script"));
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
		MessageSymbol returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from message_symbol ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from message_symbol					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,message_symbol_id,symbol_type,data_type,value_or_script, rownumber() over(order by id) as rn from message_symbol ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from message_symbol order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,message_symbol_id,symbol_type,data_type,value_or_script from message_symbol ) A where rownumber > "  + startNum ;
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
				debug("MessageSymbolDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new MessageSymbol();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setSymbolType(resultSet.getString("symbol_type"));
				returnDTO.setDataType(resultSet.getString("data_type"));
				returnDTO.setValueOrScript(resultSet.getBytes("value_or_script"));
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
		MessageSymbol returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from message_symbol where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MessageSymbolDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new MessageSymbol();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setSymbolType(resultSet.getString("symbol_type"));
				returnDTO.setDataType(resultSet.getString("data_type"));
				returnDTO.setValueOrScript(resultSet.getBytes("value_or_script"));
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
		MessageSymbol returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from message_symbol where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from message_symbol where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from message_symbol					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,message_symbol_id,symbol_type,data_type,value_or_script, rownumber() over(order by id) as rn from message_symbol where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,message_symbol_id,symbol_type,data_type,value_or_script from message_symbol  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("MessageSymbolDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new MessageSymbol();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setMessageSymbolId(resultSet.getString("message_symbol_id"));
				returnDTO.setSymbolType(resultSet.getString("symbol_type"));
				returnDTO.setDataType(resultSet.getString("data_type"));
				returnDTO.setValueOrScript(resultSet.getBytes("value_or_script"));
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
			String sql =  "select  count(*) from message_symbol";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MessageSymbolDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from message_symbol where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MessageSymbolDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
