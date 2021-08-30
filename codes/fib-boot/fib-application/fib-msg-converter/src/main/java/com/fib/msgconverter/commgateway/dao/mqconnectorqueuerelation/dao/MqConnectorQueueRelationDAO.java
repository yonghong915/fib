package com.fib.msgconverter.commgateway.dao.mqconnectorqueuerelation.dao;

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



public class MqConnectorQueueRelationDAO extends AbstractDAO {

	public MqConnectorQueueRelationDAO() {
		super();
	}

	public MqConnectorQueueRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MqConnectorQueueRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(MqConnectorQueueRelation mqConnectorQueueRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mqConnectorQueueRelation == null) {
			throw new IllegalArgumentException("mqConnectorQueueRelation is NULL!");
		}
		if( mqConnectorQueueRelation.getMqConnectorId() != null && mqConnectorQueueRelation.getMqConnectorId().length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorQueueRelation.getMqConnectorId());
		}
		if( mqConnectorQueueRelation.getMqConnectorId() != null && mqConnectorQueueRelation.getMqConnectorId().length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorQueueRelation.getMqConnectorId());
		}
		if( mqConnectorQueueRelation.getQueueId() != null && mqConnectorQueueRelation.getQueueId().length() < 10 ){
			throw new IllegalArgumentException("queueId too short"+mqConnectorQueueRelation.getQueueId());
		}
		if( mqConnectorQueueRelation.getQueueId() != null && mqConnectorQueueRelation.getQueueId().length() > 10 ){
			throw new IllegalArgumentException("queueId too long"+mqConnectorQueueRelation.getQueueId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into mq_connector_queue_relation(mq_connector_id,queue_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, mqConnectorQueueRelation.getMqConnectorId());
			statment.setString(2, mqConnectorQueueRelation.getQueueId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorQueueRelation.getMqConnectorId() +"\",queue_id = \""+mqConnectorQueueRelation.getQueueId() +"\" ");
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


	public int[] insertBatch(List<MqConnectorQueueRelation> mqConnectorQueueRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (mqConnectorQueueRelationList == null) {
			throw new IllegalArgumentException("mqConnectorQueueRelationList is NULL!");
		}
		for( MqConnectorQueueRelation mqConnectorQueueRelation : mqConnectorQueueRelationList){
		if( mqConnectorQueueRelation.getMqConnectorId() != null && mqConnectorQueueRelation.getMqConnectorId().length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorQueueRelation.getMqConnectorId());
		}
		if( mqConnectorQueueRelation.getMqConnectorId() != null && mqConnectorQueueRelation.getMqConnectorId().length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorQueueRelation.getMqConnectorId());
		}
		if( mqConnectorQueueRelation.getQueueId() != null && mqConnectorQueueRelation.getQueueId().length() < 10 ){
			throw new IllegalArgumentException("queueId too short"+mqConnectorQueueRelation.getQueueId());
		}
		if( mqConnectorQueueRelation.getQueueId() != null && mqConnectorQueueRelation.getQueueId().length() > 10 ){
			throw new IllegalArgumentException("queueId too long"+mqConnectorQueueRelation.getQueueId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into mq_connector_queue_relation(mq_connector_id,queue_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( MqConnectorQueueRelation mqConnectorQueueRelation : mqConnectorQueueRelationList){
			statment.setString(1, mqConnectorQueueRelation.getMqConnectorId());
			statment.setString(2, mqConnectorQueueRelation.getQueueId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(MqConnectorQueueRelation mqConnectorQueueRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mqConnectorQueueRelation == null) {
			throw new IllegalArgumentException("mqConnectorQueueRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( mqConnectorQueueRelation.getMqConnectorId() != null && mqConnectorQueueRelation.getMqConnectorId().length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorQueueRelation.getMqConnectorId());
		}
		if( mqConnectorQueueRelation.getMqConnectorId() != null && mqConnectorQueueRelation.getMqConnectorId().length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorQueueRelation.getMqConnectorId());
		}
		if( mqConnectorQueueRelation.getQueueId() != null && mqConnectorQueueRelation.getQueueId().length() < 10 ){
			throw new IllegalArgumentException("queueId too short"+mqConnectorQueueRelation.getQueueId());
		}
		if( mqConnectorQueueRelation.getQueueId() != null && mqConnectorQueueRelation.getQueueId().length() > 10 ){
			throw new IllegalArgumentException("queueId too long"+mqConnectorQueueRelation.getQueueId());
		}
		try {
			String sql =  "UPDATE mq_connector_queue_relation SET mq_connector_id=?,queue_id=? where mq_connector_id=? and queue_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, mqConnectorQueueRelation.getMqConnectorId());
			statment.setString(2, mqConnectorQueueRelation.getQueueId());
			statment.setString(3, mqConnectorQueueRelation.getMqConnectorId());			statment.setString(4, mqConnectorQueueRelation.getQueueId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorQueueRelation.getMqConnectorId() +"\",queue_id = \""+mqConnectorQueueRelation.getQueueId() +"\" ");
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
		if(!primaryKey.containsKey("mq_connector_id") ){
			throw new IllegalArgumentException("mq_connector_id is null ");  
		}
		if(!primaryKey.containsKey("queue_id") ){
			throw new IllegalArgumentException("queue_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE mq_connector_queue_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where mq_connector_id=? and queue_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("mq_connector_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("queue_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("mq_connector_id"));
			statment.setString(m++, (String)primaryKey.get("queue_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("MqConnectorQueueRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String mqConnectorId,String queueId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mqConnectorId == null) {
			throw new IllegalArgumentException("mqConnectorId is NULL!");
		}
		if (queueId == null) {
			throw new IllegalArgumentException("queueId is NULL!");
		}

		PreparedStatement statment = null;
		if( mqConnectorId != null && mqConnectorId.length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorId);
		}
		if( mqConnectorId != null && mqConnectorId.length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorId);
		}
		if( queueId != null && queueId.length() < 10 ){
			throw new IllegalArgumentException("queueId too short"+queueId);
		}
		if( queueId != null && queueId.length() > 10 ){
			throw new IllegalArgumentException("queueId too long"+queueId);
		}

		try {
			String sql = "DELETE FROM mq_connector_queue_relation where mq_connector_id=? and queue_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,mqConnectorId);
			statment.setString(2,queueId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorId+"\",queue_id = \""+queueId+"\" ");
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


	public MqConnectorQueueRelation selectByPK (String mqConnectorId,String queueId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( mqConnectorId != null && mqConnectorId.length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorId);
		}
		if( mqConnectorId != null && mqConnectorId.length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorId);
		}
		if( queueId != null && queueId.length() < 10 ){
			throw new IllegalArgumentException("queueId too short"+queueId);
		}
		if( queueId != null && queueId.length() > 10 ){
			throw new IllegalArgumentException("queueId too long"+queueId);
		}
		ResultSet resultSet = null;
		MqConnectorQueueRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM mq_connector_queue_relation where mq_connector_id=? and queue_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,mqConnectorId);
			statment.setString(2,queueId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorId+"\",queue_id = \""+queueId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new MqConnectorQueueRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setQueueId(resultSet.getString("queue_id"));
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
		MqConnectorQueueRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from mq_connector_queue_relation order by mq_connector_id,queue_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorQueueRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setQueueId(resultSet.getString("queue_id"));
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
		MqConnectorQueueRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mq_connector_queue_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by mq_connector_id,queue_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from mq_connector_queue_relation					 ) t1)t2 order by mq_connector_id,queue_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select mq_connector_id,queue_id, rownumber() over(order by mq_connector_id,queue_id) as rn from mq_connector_queue_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from mq_connector_queue_relation order by mq_connector_id,queue_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by mq_connector_id,queue_id) as rownumber, mq_connector_id,queue_id from mq_connector_queue_relation ) A where rownumber > "  + startNum ;
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
				debug("MqConnectorQueueRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorQueueRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setQueueId(resultSet.getString("queue_id"));
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
		MqConnectorQueueRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from mq_connector_queue_relation where " + where +" order by mq_connector_id,queue_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorQueueRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setQueueId(resultSet.getString("queue_id"));
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
		MqConnectorQueueRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from mq_connector_queue_relation where " + where +" order by mq_connector_id,queue_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mq_connector_queue_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by mq_connector_id,queue_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from mq_connector_queue_relation					 where " + where + " ) t1)t2 order by mq_connector_id,queue_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select mq_connector_id,queue_id, rownumber() over(order by mq_connector_id,queue_id) as rn from mq_connector_queue_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by mq_connector_id,queue_id) as rownumber, mq_connector_id,queue_id from mq_connector_queue_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("MqConnectorQueueRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorQueueRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setQueueId(resultSet.getString("queue_id"));
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
			String sql =  "select  count(*) from mq_connector_queue_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from mq_connector_queue_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllQueue4Connector(String mqConnectorId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( mqConnectorId != null && mqConnectorId.length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorId);
		}
		if( mqConnectorId != null && mqConnectorId.length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorId);
		}
		ResultSet resultSet = null;
		MqConnectorQueueRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from mq_connector_queue_relation where connector_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,mqConnectorId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("MqConnectorQueueRelationDAO.getAllQueue4Connector() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : mqConnectorId = " + mqConnectorId);
			}


			while (resultSet.next()) {
				returnDTO = new MqConnectorQueueRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setQueueId(resultSet.getString("queue_id"));
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
