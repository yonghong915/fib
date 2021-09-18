package com.fib.msgconverter.commgateway.dao.connectormessagetypecoderelation.dao;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.giantstone.base.dao.AbstractDAO;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.base.config.DAOConfiguration;



public class ConnectorMessageTypeCodeRelationDAO extends AbstractDAO {

	public ConnectorMessageTypeCodeRelationDAO() {
		super();
	}

	public ConnectorMessageTypeCodeRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ConnectorMessageTypeCodeRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(ConnectorMessageTypeCodeRelation connectorMessageTypeCodeRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (connectorMessageTypeCodeRelation == null) {
			throw new IllegalArgumentException("connectorMessageTypeCodeRelation is NULL!");
		}
		if( connectorMessageTypeCodeRelation.getConnectorId() != null && connectorMessageTypeCodeRelation.getConnectorId().length() < 10 ){
			throw new IllegalArgumentException("connectorId too short"+connectorMessageTypeCodeRelation.getConnectorId());
		}
		if( connectorMessageTypeCodeRelation.getConnectorId() != null && connectorMessageTypeCodeRelation.getConnectorId().length() > 10 ){
			throw new IllegalArgumentException("connectorId too long"+connectorMessageTypeCodeRelation.getConnectorId());
		}
		if( connectorMessageTypeCodeRelation.getMessageTypeCodeId() != null && connectorMessageTypeCodeRelation.getMessageTypeCodeId().length() < 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too short"+connectorMessageTypeCodeRelation.getMessageTypeCodeId());
		}
		if( connectorMessageTypeCodeRelation.getMessageTypeCodeId() != null && connectorMessageTypeCodeRelation.getMessageTypeCodeId().length() > 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too long"+connectorMessageTypeCodeRelation.getMessageTypeCodeId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into connector_message_type_code_relation(connector_id,message_type_code_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, connectorMessageTypeCodeRelation.getConnectorId());
			statment.setString(2, connectorMessageTypeCodeRelation.getMessageTypeCodeId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : connector_id = \""+connectorMessageTypeCodeRelation.getConnectorId() +"\",message_type_code_id = \""+connectorMessageTypeCodeRelation.getMessageTypeCodeId() +"\" ");
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


	public int[] insertBatch(List<ConnectorMessageTypeCodeRelation> connectorMessageTypeCodeRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (connectorMessageTypeCodeRelationList == null) {
			throw new IllegalArgumentException("connectorMessageTypeCodeRelationList is NULL!");
		}
		for( ConnectorMessageTypeCodeRelation connectorMessageTypeCodeRelation : connectorMessageTypeCodeRelationList){
		if( connectorMessageTypeCodeRelation.getConnectorId() != null && connectorMessageTypeCodeRelation.getConnectorId().length() < 10 ){
			throw new IllegalArgumentException("connectorId too short"+connectorMessageTypeCodeRelation.getConnectorId());
		}
		if( connectorMessageTypeCodeRelation.getConnectorId() != null && connectorMessageTypeCodeRelation.getConnectorId().length() > 10 ){
			throw new IllegalArgumentException("connectorId too long"+connectorMessageTypeCodeRelation.getConnectorId());
		}
		if( connectorMessageTypeCodeRelation.getMessageTypeCodeId() != null && connectorMessageTypeCodeRelation.getMessageTypeCodeId().length() < 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too short"+connectorMessageTypeCodeRelation.getMessageTypeCodeId());
		}
		if( connectorMessageTypeCodeRelation.getMessageTypeCodeId() != null && connectorMessageTypeCodeRelation.getMessageTypeCodeId().length() > 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too long"+connectorMessageTypeCodeRelation.getMessageTypeCodeId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into connector_message_type_code_relation(connector_id,message_type_code_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( ConnectorMessageTypeCodeRelation connectorMessageTypeCodeRelation : connectorMessageTypeCodeRelationList){
			statment.setString(1, connectorMessageTypeCodeRelation.getConnectorId());
			statment.setString(2, connectorMessageTypeCodeRelation.getMessageTypeCodeId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(ConnectorMessageTypeCodeRelation connectorMessageTypeCodeRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (connectorMessageTypeCodeRelation == null) {
			throw new IllegalArgumentException("connectorMessageTypeCodeRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( connectorMessageTypeCodeRelation.getConnectorId() != null && connectorMessageTypeCodeRelation.getConnectorId().length() < 10 ){
			throw new IllegalArgumentException("connectorId too short"+connectorMessageTypeCodeRelation.getConnectorId());
		}
		if( connectorMessageTypeCodeRelation.getConnectorId() != null && connectorMessageTypeCodeRelation.getConnectorId().length() > 10 ){
			throw new IllegalArgumentException("connectorId too long"+connectorMessageTypeCodeRelation.getConnectorId());
		}
		if( connectorMessageTypeCodeRelation.getMessageTypeCodeId() != null && connectorMessageTypeCodeRelation.getMessageTypeCodeId().length() < 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too short"+connectorMessageTypeCodeRelation.getMessageTypeCodeId());
		}
		if( connectorMessageTypeCodeRelation.getMessageTypeCodeId() != null && connectorMessageTypeCodeRelation.getMessageTypeCodeId().length() > 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too long"+connectorMessageTypeCodeRelation.getMessageTypeCodeId());
		}
		try {
			String sql =  "UPDATE connector_message_type_code_relation SET connector_id=?,message_type_code_id=? where connector_id=? and message_type_code_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, connectorMessageTypeCodeRelation.getConnectorId());
			statment.setString(2, connectorMessageTypeCodeRelation.getMessageTypeCodeId());
			statment.setString(3, connectorMessageTypeCodeRelation.getConnectorId());			statment.setString(4, connectorMessageTypeCodeRelation.getMessageTypeCodeId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : connector_id = \""+connectorMessageTypeCodeRelation.getConnectorId() +"\",message_type_code_id = \""+connectorMessageTypeCodeRelation.getMessageTypeCodeId() +"\" ");
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


	public int dynamicUpdate(Map<String,String> primaryKey, Map<String,Object> updateFields) {
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
		if(!primaryKey.containsKey("connector_id") ){
			throw new IllegalArgumentException("connector_id is null ");  
		}
		if(!primaryKey.containsKey("message_type_code_id") ){
			throw new IllegalArgumentException("message_type_code_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE connector_message_type_code_relation SET ");
			Iterator<String> it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where connector_id=? and message_type_code_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("connector_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("message_type_code_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("connector_id"));
			statment.setString(m++, (String)primaryKey.get("message_type_code_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("ConnectorMessageTypeCodeRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
				Iterator<String> priIt = updateFields.keySet().iterator();
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


	public int deleteByPK(String connectorId,String messageTypeCodeId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (connectorId == null) {
			throw new IllegalArgumentException("connectorId is NULL!");
		}
		if (messageTypeCodeId == null) {
			throw new IllegalArgumentException("messageTypeCodeId is NULL!");
		}

		PreparedStatement statment = null;
		if( connectorId != null && connectorId.length() < 10 ){
			throw new IllegalArgumentException("connectorId too short"+connectorId);
		}
		if( connectorId != null && connectorId.length() > 10 ){
			throw new IllegalArgumentException("connectorId too long"+connectorId);
		}
		if( messageTypeCodeId != null && messageTypeCodeId.length() < 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too short"+messageTypeCodeId);
		}
		if( messageTypeCodeId != null && messageTypeCodeId.length() > 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too long"+messageTypeCodeId);
		}

		try {
			String sql = "DELETE FROM connector_message_type_code_relation where connector_id=? and message_type_code_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,connectorId);
			statment.setString(2,messageTypeCodeId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : connector_id = \""+connectorId+"\",message_type_code_id = \""+messageTypeCodeId+"\" ");
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


	public ConnectorMessageTypeCodeRelation selectByPK (String connectorId,String messageTypeCodeId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( connectorId != null && connectorId.length() < 10 ){
			throw new IllegalArgumentException("connectorId too short"+connectorId);
		}
		if( connectorId != null && connectorId.length() > 10 ){
			throw new IllegalArgumentException("connectorId too long"+connectorId);
		}
		if( messageTypeCodeId != null && messageTypeCodeId.length() < 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too short"+messageTypeCodeId);
		}
		if( messageTypeCodeId != null && messageTypeCodeId.length() > 10 ){
			throw new IllegalArgumentException("messageTypeCodeId too long"+messageTypeCodeId);
		}
		ResultSet resultSet = null;
		ConnectorMessageTypeCodeRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM connector_message_type_code_relation where connector_id=? and message_type_code_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,connectorId);
			statment.setString(2,messageTypeCodeId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : connector_id = \""+connectorId+"\",message_type_code_id = \""+messageTypeCodeId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new ConnectorMessageTypeCodeRelation();
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setMessageTypeCodeId(resultSet.getString("message_type_code_id"));
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


	public List<ConnectorMessageTypeCodeRelation> findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ConnectorMessageTypeCodeRelation returnDTO = null;
		List<ConnectorMessageTypeCodeRelation> list = new ArrayList<>();


		try {
			String sql = "select * from connector_message_type_code_relation order by connector_id,message_type_code_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new ConnectorMessageTypeCodeRelation();
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setMessageTypeCodeId(resultSet.getString("message_type_code_id"));
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


	public List<ConnectorMessageTypeCodeRelation> findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ConnectorMessageTypeCodeRelation returnDTO = null;
		List<ConnectorMessageTypeCodeRelation> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from connector_message_type_code_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by connector_id,message_type_code_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from connector_message_type_code_relation					 ) t1)t2 order by connector_id,message_type_code_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select connector_id,message_type_code_id, rownumber() over(order by connector_id,message_type_code_id) as rn from connector_message_type_code_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from connector_message_type_code_relation order by connector_id,message_type_code_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by connector_id,message_type_code_id) as rownumber, connector_id,message_type_code_id from connector_message_type_code_relation ) A where rownumber > "  + startNum ;
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
				debug("ConnectorMessageTypeCodeRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new ConnectorMessageTypeCodeRelation();
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setMessageTypeCodeId(resultSet.getString("message_type_code_id"));
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


	public List<ConnectorMessageTypeCodeRelation> findByWhere (String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ConnectorMessageTypeCodeRelation returnDTO = null;
		List<ConnectorMessageTypeCodeRelation> list = new ArrayList<>();

		try {
			String sql = "select * from connector_message_type_code_relation where " + where +" order by connector_id,message_type_code_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new ConnectorMessageTypeCodeRelation();
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setMessageTypeCodeId(resultSet.getString("message_type_code_id"));
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


	public List<ConnectorMessageTypeCodeRelation> findByWhere (String where, int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		ConnectorMessageTypeCodeRelation returnDTO = null;
		List<ConnectorMessageTypeCodeRelation> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from connector_message_type_code_relation where " + where +" order by connector_id,message_type_code_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from connector_message_type_code_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by connector_id,message_type_code_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from connector_message_type_code_relation					 where " + where + " ) t1)t2 order by connector_id,message_type_code_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select connector_id,message_type_code_id, rownumber() over(order by connector_id,message_type_code_id) as rn from connector_message_type_code_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by connector_id,message_type_code_id) as rownumber, connector_id,message_type_code_id from connector_message_type_code_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("ConnectorMessageTypeCodeRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new ConnectorMessageTypeCodeRelation();
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setMessageTypeCodeId(resultSet.getString("message_type_code_id"));
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
			String sql =  "select  count(*) from connector_message_type_code_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from connector_message_type_code_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List<ConnectorMessageTypeCodeRelation> getAllMsgTypCode4Connector(String connectorId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( connectorId != null && connectorId.length() < 10 ){
			throw new IllegalArgumentException("connectorId too short"+connectorId);
		}
		if( connectorId != null && connectorId.length() > 10 ){
			throw new IllegalArgumentException("connectorId too long"+connectorId);
		}
		ResultSet resultSet = null;
		ConnectorMessageTypeCodeRelation returnDTO = null;
		List<ConnectorMessageTypeCodeRelation> list = new ArrayList<>();
		try {
			String sql = "select * from connector_message_type_code_relation where connector_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,connectorId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("ConnectorMessageTypeCodeRelationDAO.getAllMsgTypCode4Connector() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : connectorId = " + connectorId);
			}


			while (resultSet.next()) {
				returnDTO = new ConnectorMessageTypeCodeRelation();
				returnDTO.setConnectorId(resultSet.getString("connector_id"));
				returnDTO.setMessageTypeCodeId(resultSet.getString("message_type_code_id"));
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
