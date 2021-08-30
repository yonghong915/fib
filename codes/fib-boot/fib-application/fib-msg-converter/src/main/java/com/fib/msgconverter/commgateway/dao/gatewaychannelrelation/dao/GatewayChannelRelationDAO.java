package com.fib.msgconverter.commgateway.dao.gatewaychannelrelation.dao;

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



public class GatewayChannelRelationDAO extends AbstractDAO {

	public GatewayChannelRelationDAO() {
		super();
	}

	public GatewayChannelRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public GatewayChannelRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(GatewayChannelRelation gatewayChannelRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (gatewayChannelRelation == null) {
			throw new IllegalArgumentException("gatewayChannelRelation is NULL!");
		}
		if( gatewayChannelRelation.getGatewayId() != null && gatewayChannelRelation.getGatewayId().length() > 255 ){
			throw new IllegalArgumentException("gatewayId too long"+gatewayChannelRelation.getGatewayId());
		}
		if( gatewayChannelRelation.getChannelId() != null && gatewayChannelRelation.getChannelId().length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+gatewayChannelRelation.getChannelId());
		}
		if( gatewayChannelRelation.getChannelId() != null && gatewayChannelRelation.getChannelId().length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+gatewayChannelRelation.getChannelId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into gateway_channel_relation(gateway_id,channel_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, gatewayChannelRelation.getGatewayId());
			statment.setString(2, gatewayChannelRelation.getChannelId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : gateway_id = \""+gatewayChannelRelation.getGatewayId() +"\",channel_id = \""+gatewayChannelRelation.getChannelId() +"\" ");
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


	public int[] insertBatch(List<GatewayChannelRelation> gatewayChannelRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (gatewayChannelRelationList == null) {
			throw new IllegalArgumentException("gatewayChannelRelationList is NULL!");
		}
		for( GatewayChannelRelation gatewayChannelRelation : gatewayChannelRelationList){
		if( gatewayChannelRelation.getGatewayId() != null && gatewayChannelRelation.getGatewayId().length() > 255 ){
			throw new IllegalArgumentException("gatewayId too long"+gatewayChannelRelation.getGatewayId());
		}
		if( gatewayChannelRelation.getChannelId() != null && gatewayChannelRelation.getChannelId().length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+gatewayChannelRelation.getChannelId());
		}
		if( gatewayChannelRelation.getChannelId() != null && gatewayChannelRelation.getChannelId().length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+gatewayChannelRelation.getChannelId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into gateway_channel_relation(gateway_id,channel_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( GatewayChannelRelation gatewayChannelRelation : gatewayChannelRelationList){
			statment.setString(1, gatewayChannelRelation.getGatewayId());
			statment.setString(2, gatewayChannelRelation.getChannelId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(GatewayChannelRelation gatewayChannelRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (gatewayChannelRelation == null) {
			throw new IllegalArgumentException("gatewayChannelRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( gatewayChannelRelation.getGatewayId() != null && gatewayChannelRelation.getGatewayId().length() > 255 ){
			throw new IllegalArgumentException("gatewayId too long"+gatewayChannelRelation.getGatewayId());
		}
		if( gatewayChannelRelation.getChannelId() != null && gatewayChannelRelation.getChannelId().length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+gatewayChannelRelation.getChannelId());
		}
		if( gatewayChannelRelation.getChannelId() != null && gatewayChannelRelation.getChannelId().length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+gatewayChannelRelation.getChannelId());
		}
		try {
			String sql =  "UPDATE gateway_channel_relation SET gateway_id=?,channel_id=? where gateway_id=? and channel_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, gatewayChannelRelation.getGatewayId());
			statment.setString(2, gatewayChannelRelation.getChannelId());
			statment.setString(3, gatewayChannelRelation.getGatewayId());			statment.setString(4, gatewayChannelRelation.getChannelId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : gateway_id = \""+gatewayChannelRelation.getGatewayId() +"\",channel_id = \""+gatewayChannelRelation.getChannelId() +"\" ");
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
		if(!primaryKey.containsKey("gateway_id") ){
			throw new IllegalArgumentException("gateway_id is null ");  
		}
		if(!primaryKey.containsKey("channel_id") ){
			throw new IllegalArgumentException("channel_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE gateway_channel_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where gateway_id=? and channel_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("gateway_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("channel_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("gateway_id"));
			statment.setString(m++, (String)primaryKey.get("channel_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("GatewayChannelRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String gatewayId,String channelId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (gatewayId == null) {
			throw new IllegalArgumentException("gatewayId is NULL!");
		}
		if (channelId == null) {
			throw new IllegalArgumentException("channelId is NULL!");
		}

		PreparedStatement statment = null;
		if( gatewayId != null && gatewayId.length() > 255 ){
			throw new IllegalArgumentException("gatewayId too long"+gatewayId);
		}
		if( channelId != null && channelId.length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+channelId);
		}
		if( channelId != null && channelId.length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+channelId);
		}

		try {
			String sql = "DELETE FROM gateway_channel_relation where gateway_id=? and channel_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,gatewayId);
			statment.setString(2,channelId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : gateway_id = \""+gatewayId+"\",channel_id = \""+channelId+"\" ");
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


	public GatewayChannelRelation selectByPK (String gatewayId,String channelId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( gatewayId != null && gatewayId.length() > 255 ){
			throw new IllegalArgumentException("gatewayId too long"+gatewayId);
		}
		if( channelId != null && channelId.length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+channelId);
		}
		if( channelId != null && channelId.length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+channelId);
		}
		ResultSet resultSet = null;
		GatewayChannelRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM gateway_channel_relation where gateway_id=? and channel_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,gatewayId);
			statment.setString(2,channelId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : gateway_id = \""+gatewayId+"\",channel_id = \""+channelId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new GatewayChannelRelation();
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
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
		GatewayChannelRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from gateway_channel_relation order by gateway_id,channel_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new GatewayChannelRelation();
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
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
		GatewayChannelRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from gateway_channel_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by gateway_id,channel_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from gateway_channel_relation					 ) t1)t2 order by gateway_id,channel_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select gateway_id,channel_id, rownumber() over(order by gateway_id,channel_id) as rn from gateway_channel_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from gateway_channel_relation order by gateway_id,channel_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by gateway_id,channel_id) as rownumber, gateway_id,channel_id from gateway_channel_relation ) A where rownumber > "  + startNum ;
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
				debug("GatewayChannelRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new GatewayChannelRelation();
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
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
		GatewayChannelRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from gateway_channel_relation where " + where +" order by gateway_id,channel_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new GatewayChannelRelation();
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
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
		GatewayChannelRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from gateway_channel_relation where " + where +" order by gateway_id,channel_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from gateway_channel_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by gateway_id,channel_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from gateway_channel_relation					 where " + where + " ) t1)t2 order by gateway_id,channel_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select gateway_id,channel_id, rownumber() over(order by gateway_id,channel_id) as rn from gateway_channel_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by gateway_id,channel_id) as rownumber, gateway_id,channel_id from gateway_channel_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("GatewayChannelRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new GatewayChannelRelation();
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
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
			String sql =  "select  count(*) from gateway_channel_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from gateway_channel_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllChannelInGw(String gatewayId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( gatewayId != null && gatewayId.length() > 255 ){
			throw new IllegalArgumentException("gatewayId too long"+gatewayId);
		}
		ResultSet resultSet = null;
		GatewayChannelRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from gateway_channel_relation where gateway_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,gatewayId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("GatewayChannelRelationDAO.getAllChannelInGw() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : gatewayId = " + gatewayId);
			}


			while (resultSet.next()) {
				returnDTO = new GatewayChannelRelation();
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
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
