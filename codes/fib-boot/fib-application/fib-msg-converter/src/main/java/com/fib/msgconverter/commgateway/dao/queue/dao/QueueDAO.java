package com.fib.msgconverter.commgateway.dao.queue.dao;

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



public class QueueDAO extends AbstractDAO {

	public QueueDAO() {
		super();
	}

	public QueueDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public QueueDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Queue queue) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (queue == null) {
			throw new IllegalArgumentException("queue is NULL!");
		}
		if( queue.getId() != null && queue.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+queue.getId());
		}
		if( queue.getId() != null && queue.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+queue.getId());
		}
		if( queue.getName()==null || "".equals(queue.getName())){
			throw new IllegalArgumentException("name is null");
		}
		if( queue.getName() != null && queue.getName().length() > 255 ){
			throw new IllegalArgumentException("name too long"+queue.getName());
		}
		if( queue.getDirection()==null || "".equals(queue.getDirection())){
			throw new IllegalArgumentException("direction is null");
		}
		if( queue.getDirection() != null && queue.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+queue.getDirection());
		}
		if( queue.getDirection() != null && queue.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+queue.getDirection());
		}
		if( queue.getServerAddress()==null || "".equals(queue.getServerAddress())){
			throw new IllegalArgumentException("serverAddress is null");
		}
		if( queue.getServerAddress() != null && queue.getServerAddress().length() > 255 ){
			throw new IllegalArgumentException("serverAddress too long"+queue.getServerAddress());
		}
		if( queue.getQueueManager()==null || "".equals(queue.getQueueManager())){
			throw new IllegalArgumentException("queueManager is null");
		}
		if( queue.getQueueManager() != null && queue.getQueueManager().length() > 255 ){
			throw new IllegalArgumentException("queueManager too long"+queue.getQueueManager());
		}
		if( queue.getMqChannelName()==null || "".equals(queue.getMqChannelName())){
			throw new IllegalArgumentException("mqChannelName is null");
		}
		if( queue.getMqChannelName() != null && queue.getMqChannelName().length() > 255 ){
			throw new IllegalArgumentException("mqChannelName too long"+queue.getMqChannelName());
		}
		if( queue.getMessageKeyRecognizerId() != null && queue.getMessageKeyRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("messageKeyRecognizerId too short"+queue.getMessageKeyRecognizerId());
		}
		if( queue.getMessageKeyRecognizerId() != null && queue.getMessageKeyRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("messageKeyRecognizerId too long"+queue.getMessageKeyRecognizerId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into queue(id,name,direction,server_address,port,queue_manager,mq_channel_name,message_key_recognizer_id) values(?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, queue.getId());
			statment.setString(2, queue.getName());
			statment.setString(3, queue.getDirection());
			statment.setString(4, queue.getServerAddress());
			statment.setInt(5, queue.getPort());
			statment.setString(6, queue.getQueueManager());
			statment.setString(7, queue.getMqChannelName());
			statment.setString(8, queue.getMessageKeyRecognizerId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("QueueDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+queue.getId() +"\",name = \""+queue.getName() +"\",direction = \""+queue.getDirection() +"\",server_address = \""+queue.getServerAddress() +"\",port = "+queue.getPort()+",queue_manager = \""+queue.getQueueManager() +"\",mq_channel_name = \""+queue.getMqChannelName() +"\",message_key_recognizer_id = \""+queue.getMessageKeyRecognizerId() +"\" ");
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


	public int[] insertBatch(List<Queue> queueList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (queueList == null) {
			throw new IllegalArgumentException("queueList is NULL!");
		}
		for( Queue queue : queueList){
		if( queue.getId() != null && queue.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+queue.getId());
		}
		if( queue.getId() != null && queue.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+queue.getId());
		}
		if( queue.getName()==null || "".equals(queue.getName())){
			throw new IllegalArgumentException("name is null");
		}
		if( queue.getName() != null && queue.getName().length() > 255 ){
			throw new IllegalArgumentException("name too long"+queue.getName());
		}
		if( queue.getDirection()==null || "".equals(queue.getDirection())){
			throw new IllegalArgumentException("direction is null");
		}
		if( queue.getDirection() != null && queue.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+queue.getDirection());
		}
		if( queue.getDirection() != null && queue.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+queue.getDirection());
		}
		if( queue.getServerAddress()==null || "".equals(queue.getServerAddress())){
			throw new IllegalArgumentException("serverAddress is null");
		}
		if( queue.getServerAddress() != null && queue.getServerAddress().length() > 255 ){
			throw new IllegalArgumentException("serverAddress too long"+queue.getServerAddress());
		}
		if( queue.getQueueManager()==null || "".equals(queue.getQueueManager())){
			throw new IllegalArgumentException("queueManager is null");
		}
		if( queue.getQueueManager() != null && queue.getQueueManager().length() > 255 ){
			throw new IllegalArgumentException("queueManager too long"+queue.getQueueManager());
		}
		if( queue.getMqChannelName()==null || "".equals(queue.getMqChannelName())){
			throw new IllegalArgumentException("mqChannelName is null");
		}
		if( queue.getMqChannelName() != null && queue.getMqChannelName().length() > 255 ){
			throw new IllegalArgumentException("mqChannelName too long"+queue.getMqChannelName());
		}
		if( queue.getMessageKeyRecognizerId() != null && queue.getMessageKeyRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("messageKeyRecognizerId too short"+queue.getMessageKeyRecognizerId());
		}
		if( queue.getMessageKeyRecognizerId() != null && queue.getMessageKeyRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("messageKeyRecognizerId too long"+queue.getMessageKeyRecognizerId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into queue(id,name,direction,server_address,port,queue_manager,mq_channel_name,message_key_recognizer_id) values(?,?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( Queue queue : queueList){
			statment.setString(1, queue.getId());
			statment.setString(2, queue.getName());
			statment.setString(3, queue.getDirection());
			statment.setString(4, queue.getServerAddress());
			statment.setInt(5, queue.getPort());
			statment.setString(6, queue.getQueueManager());
			statment.setString(7, queue.getMqChannelName());
			statment.setString(8, queue.getMessageKeyRecognizerId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("QueueDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(Queue queue) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (queue == null) {
			throw new IllegalArgumentException("queue is NULL!");
		}

		PreparedStatement statment = null;
		if( queue.getId() != null && queue.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+queue.getId());
		}
		if( queue.getId() != null && queue.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+queue.getId());
		}
		if( queue.getName()==null || "".equals(queue.getName())){
			throw new IllegalArgumentException("name is null");
		}
		if( queue.getName() != null && queue.getName().length() > 255 ){
			throw new IllegalArgumentException("name too long"+queue.getName());
		}
		if( queue.getDirection()==null || "".equals(queue.getDirection())){
			throw new IllegalArgumentException("direction is null");
		}
		if( queue.getDirection() != null && queue.getDirection().length() < 4 ){
			throw new IllegalArgumentException("direction too short"+queue.getDirection());
		}
		if( queue.getDirection() != null && queue.getDirection().length() > 4 ){
			throw new IllegalArgumentException("direction too long"+queue.getDirection());
		}
		if( queue.getServerAddress()==null || "".equals(queue.getServerAddress())){
			throw new IllegalArgumentException("serverAddress is null");
		}
		if( queue.getServerAddress() != null && queue.getServerAddress().length() > 255 ){
			throw new IllegalArgumentException("serverAddress too long"+queue.getServerAddress());
		}
		if( queue.getQueueManager()==null || "".equals(queue.getQueueManager())){
			throw new IllegalArgumentException("queueManager is null");
		}
		if( queue.getQueueManager() != null && queue.getQueueManager().length() > 255 ){
			throw new IllegalArgumentException("queueManager too long"+queue.getQueueManager());
		}
		if( queue.getMqChannelName()==null || "".equals(queue.getMqChannelName())){
			throw new IllegalArgumentException("mqChannelName is null");
		}
		if( queue.getMqChannelName() != null && queue.getMqChannelName().length() > 255 ){
			throw new IllegalArgumentException("mqChannelName too long"+queue.getMqChannelName());
		}
		if( queue.getMessageKeyRecognizerId() != null && queue.getMessageKeyRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("messageKeyRecognizerId too short"+queue.getMessageKeyRecognizerId());
		}
		if( queue.getMessageKeyRecognizerId() != null && queue.getMessageKeyRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("messageKeyRecognizerId too long"+queue.getMessageKeyRecognizerId());
		}
		try {
			String sql =  "UPDATE queue SET id=?,name=?,direction=?,server_address=?,port=?,queue_manager=?,mq_channel_name=?,message_key_recognizer_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, queue.getId());
			statment.setString(2, queue.getName());
			statment.setString(3, queue.getDirection());
			statment.setString(4, queue.getServerAddress());
			statment.setInt(5, queue.getPort());
			statment.setString(6, queue.getQueueManager());
			statment.setString(7, queue.getMqChannelName());
			statment.setString(8, queue.getMessageKeyRecognizerId());
			statment.setString(9, queue.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("QueueDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+queue.getId() +"\",name = \""+queue.getName() +"\",direction = \""+queue.getDirection() +"\",server_address = \""+queue.getServerAddress() +"\",port = "+queue.getPort()+",queue_manager = \""+queue.getQueueManager() +"\",mq_channel_name = \""+queue.getMqChannelName() +"\",message_key_recognizer_id = \""+queue.getMessageKeyRecognizerId() +"\" ");
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
			sql.append("UPDATE queue SET ");
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
				if("direction".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("server_address".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("port".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("queue_manager".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("mq_channel_name".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("message_key_recognizer_id".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("QueueDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM queue where id=? ";
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
				debug("QueueDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public Queue selectByPK (String id)  {
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
		Queue returnDTO = null;

		try {
			String sql =  "SELECT * FROM queue where id=? ";
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
				debug("QueueDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Queue();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setQueueManager(resultSet.getString("queue_manager"));
				returnDTO.setMqChannelName(resultSet.getString("mq_channel_name"));
				returnDTO.setMessageKeyRecognizerId(resultSet.getString("message_key_recognizer_id"));
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
		Queue returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from queue order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("QueueDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Queue();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setQueueManager(resultSet.getString("queue_manager"));
				returnDTO.setMqChannelName(resultSet.getString("mq_channel_name"));
				returnDTO.setMessageKeyRecognizerId(resultSet.getString("message_key_recognizer_id"));
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
		Queue returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from queue ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from queue					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,name,direction,server_address,port,queue_manager,mq_channel_name,message_key_recognizer_id, rownumber() over(order by id) as rn from queue ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from queue order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,name,direction,server_address,port,queue_manager,mq_channel_name,message_key_recognizer_id from queue ) A where rownumber > "  + startNum ;
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
				debug("QueueDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Queue();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setQueueManager(resultSet.getString("queue_manager"));
				returnDTO.setMqChannelName(resultSet.getString("mq_channel_name"));
				returnDTO.setMessageKeyRecognizerId(resultSet.getString("message_key_recognizer_id"));
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
		Queue returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from queue where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("QueueDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Queue();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setQueueManager(resultSet.getString("queue_manager"));
				returnDTO.setMqChannelName(resultSet.getString("mq_channel_name"));
				returnDTO.setMessageKeyRecognizerId(resultSet.getString("message_key_recognizer_id"));
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
		Queue returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from queue where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from queue where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from queue					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,name,direction,server_address,port,queue_manager,mq_channel_name,message_key_recognizer_id, rownumber() over(order by id) as rn from queue where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,name,direction,server_address,port,queue_manager,mq_channel_name,message_key_recognizer_id from queue  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("QueueDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Queue();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setName(resultSet.getString("name"));
				returnDTO.setDirection(resultSet.getString("direction"));
				returnDTO.setServerAddress(resultSet.getString("server_address"));
				returnDTO.setPort(resultSet.getInt("port"));
				returnDTO.setQueueManager(resultSet.getString("queue_manager"));
				returnDTO.setMqChannelName(resultSet.getString("mq_channel_name"));
				returnDTO.setMessageKeyRecognizerId(resultSet.getString("message_key_recognizer_id"));
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
			String sql =  "select  count(*) from queue";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("QueueDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from queue where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("QueueDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
