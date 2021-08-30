package com.fib.msgconverter.commgateway.dao.mqconnectorfilterrelation.dao;

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



public class MqConnectorFilterRelationDAO extends AbstractDAO {

	public MqConnectorFilterRelationDAO() {
		super();
	}

	public MqConnectorFilterRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public MqConnectorFilterRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(MqConnectorFilterRelation mqConnectorFilterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mqConnectorFilterRelation == null) {
			throw new IllegalArgumentException("mqConnectorFilterRelation is NULL!");
		}
		if( mqConnectorFilterRelation.getMqConnectorId() != null && mqConnectorFilterRelation.getMqConnectorId().length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorFilterRelation.getMqConnectorId());
		}
		if( mqConnectorFilterRelation.getMqConnectorId() != null && mqConnectorFilterRelation.getMqConnectorId().length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorFilterRelation.getMqConnectorId());
		}
		if( mqConnectorFilterRelation.getFilterId() != null && mqConnectorFilterRelation.getFilterId().length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+mqConnectorFilterRelation.getFilterId());
		}
		if( mqConnectorFilterRelation.getFilterId() != null && mqConnectorFilterRelation.getFilterId().length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+mqConnectorFilterRelation.getFilterId());
		}
		if( mqConnectorFilterRelation.getReaderOrWriterFilter() != null && mqConnectorFilterRelation.getReaderOrWriterFilter().length() < 1 ){
			throw new IllegalArgumentException("readerOrWriterFilter too short"+mqConnectorFilterRelation.getReaderOrWriterFilter());
		}
		if( mqConnectorFilterRelation.getReaderOrWriterFilter() != null && mqConnectorFilterRelation.getReaderOrWriterFilter().length() > 1 ){
			throw new IllegalArgumentException("readerOrWriterFilter too long"+mqConnectorFilterRelation.getReaderOrWriterFilter());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into mq_connector_filter_relation(mq_connector_id,filter_id,reader_or_writer_filter) values(?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, mqConnectorFilterRelation.getMqConnectorId());
			statment.setString(2, mqConnectorFilterRelation.getFilterId());
			statment.setString(3, mqConnectorFilterRelation.getReaderOrWriterFilter());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorFilterRelation.getMqConnectorId() +"\",filter_id = \""+mqConnectorFilterRelation.getFilterId() +"\",reader_or_writer_filter = \""+mqConnectorFilterRelation.getReaderOrWriterFilter() +"\" ");
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


	public int[] insertBatch(List<MqConnectorFilterRelation> mqConnectorFilterRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (mqConnectorFilterRelationList == null) {
			throw new IllegalArgumentException("mqConnectorFilterRelationList is NULL!");
		}
		for( MqConnectorFilterRelation mqConnectorFilterRelation : mqConnectorFilterRelationList){
		if( mqConnectorFilterRelation.getMqConnectorId() != null && mqConnectorFilterRelation.getMqConnectorId().length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorFilterRelation.getMqConnectorId());
		}
		if( mqConnectorFilterRelation.getMqConnectorId() != null && mqConnectorFilterRelation.getMqConnectorId().length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorFilterRelation.getMqConnectorId());
		}
		if( mqConnectorFilterRelation.getFilterId() != null && mqConnectorFilterRelation.getFilterId().length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+mqConnectorFilterRelation.getFilterId());
		}
		if( mqConnectorFilterRelation.getFilterId() != null && mqConnectorFilterRelation.getFilterId().length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+mqConnectorFilterRelation.getFilterId());
		}
		if( mqConnectorFilterRelation.getReaderOrWriterFilter() != null && mqConnectorFilterRelation.getReaderOrWriterFilter().length() < 1 ){
			throw new IllegalArgumentException("readerOrWriterFilter too short"+mqConnectorFilterRelation.getReaderOrWriterFilter());
		}
		if( mqConnectorFilterRelation.getReaderOrWriterFilter() != null && mqConnectorFilterRelation.getReaderOrWriterFilter().length() > 1 ){
			throw new IllegalArgumentException("readerOrWriterFilter too long"+mqConnectorFilterRelation.getReaderOrWriterFilter());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into mq_connector_filter_relation(mq_connector_id,filter_id,reader_or_writer_filter) values(?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( MqConnectorFilterRelation mqConnectorFilterRelation : mqConnectorFilterRelationList){
			statment.setString(1, mqConnectorFilterRelation.getMqConnectorId());
			statment.setString(2, mqConnectorFilterRelation.getFilterId());
			statment.setString(3, mqConnectorFilterRelation.getReaderOrWriterFilter());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(MqConnectorFilterRelation mqConnectorFilterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mqConnectorFilterRelation == null) {
			throw new IllegalArgumentException("mqConnectorFilterRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( mqConnectorFilterRelation.getMqConnectorId() != null && mqConnectorFilterRelation.getMqConnectorId().length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorFilterRelation.getMqConnectorId());
		}
		if( mqConnectorFilterRelation.getMqConnectorId() != null && mqConnectorFilterRelation.getMqConnectorId().length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorFilterRelation.getMqConnectorId());
		}
		if( mqConnectorFilterRelation.getFilterId() != null && mqConnectorFilterRelation.getFilterId().length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+mqConnectorFilterRelation.getFilterId());
		}
		if( mqConnectorFilterRelation.getFilterId() != null && mqConnectorFilterRelation.getFilterId().length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+mqConnectorFilterRelation.getFilterId());
		}
		if( mqConnectorFilterRelation.getReaderOrWriterFilter() != null && mqConnectorFilterRelation.getReaderOrWriterFilter().length() < 1 ){
			throw new IllegalArgumentException("readerOrWriterFilter too short"+mqConnectorFilterRelation.getReaderOrWriterFilter());
		}
		if( mqConnectorFilterRelation.getReaderOrWriterFilter() != null && mqConnectorFilterRelation.getReaderOrWriterFilter().length() > 1 ){
			throw new IllegalArgumentException("readerOrWriterFilter too long"+mqConnectorFilterRelation.getReaderOrWriterFilter());
		}
		try {
			String sql =  "UPDATE mq_connector_filter_relation SET mq_connector_id=?,filter_id=?,reader_or_writer_filter=? where mq_connector_id=? and filter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, mqConnectorFilterRelation.getMqConnectorId());
			statment.setString(2, mqConnectorFilterRelation.getFilterId());
			statment.setString(3, mqConnectorFilterRelation.getReaderOrWriterFilter());
			statment.setString(4, mqConnectorFilterRelation.getMqConnectorId());			statment.setString(5, mqConnectorFilterRelation.getFilterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorFilterRelation.getMqConnectorId() +"\",filter_id = \""+mqConnectorFilterRelation.getFilterId() +"\",reader_or_writer_filter = \""+mqConnectorFilterRelation.getReaderOrWriterFilter() +"\" ");
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
		if(!primaryKey.containsKey("filter_id") ){
			throw new IllegalArgumentException("filter_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE mq_connector_filter_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where mq_connector_id=? and filter_id=? ");
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
				if("filter_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("reader_or_writer_filter".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("mq_connector_id"));
			statment.setString(m++, (String)primaryKey.get("filter_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("MqConnectorFilterRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String mqConnectorId,String filterId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (mqConnectorId == null) {
			throw new IllegalArgumentException("mqConnectorId is NULL!");
		}
		if (filterId == null) {
			throw new IllegalArgumentException("filterId is NULL!");
		}

		PreparedStatement statment = null;
		if( mqConnectorId != null && mqConnectorId.length() < 10 ){
			throw new IllegalArgumentException("mqConnectorId too short"+mqConnectorId);
		}
		if( mqConnectorId != null && mqConnectorId.length() > 10 ){
			throw new IllegalArgumentException("mqConnectorId too long"+mqConnectorId);
		}
		if( filterId != null && filterId.length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterId);
		}
		if( filterId != null && filterId.length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterId);
		}

		try {
			String sql = "DELETE FROM mq_connector_filter_relation where mq_connector_id=? and filter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,mqConnectorId);
			statment.setString(2,filterId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorId+"\",filter_id = \""+filterId+"\" ");
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


	public MqConnectorFilterRelation selectByPK (String mqConnectorId,String filterId)  {
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
		if( filterId != null && filterId.length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterId);
		}
		if( filterId != null && filterId.length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterId);
		}
		ResultSet resultSet = null;
		MqConnectorFilterRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM mq_connector_filter_relation where mq_connector_id=? and filter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,mqConnectorId);
			statment.setString(2,filterId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : mq_connector_id = \""+mqConnectorId+"\",filter_id = \""+filterId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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
		MqConnectorFilterRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from mq_connector_filter_relation order by mq_connector_id,filter_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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
		MqConnectorFilterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mq_connector_filter_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by mq_connector_id,filter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from mq_connector_filter_relation					 ) t1)t2 order by mq_connector_id,filter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select mq_connector_id,filter_id,reader_or_writer_filter, rownumber() over(order by mq_connector_id,filter_id) as rn from mq_connector_filter_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from mq_connector_filter_relation order by mq_connector_id,filter_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by mq_connector_id,filter_id) as rownumber, mq_connector_id,filter_id,reader_or_writer_filter from mq_connector_filter_relation ) A where rownumber > "  + startNum ;
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
				debug("MqConnectorFilterRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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
		MqConnectorFilterRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from mq_connector_filter_relation where " + where +" order by mq_connector_id,filter_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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
		MqConnectorFilterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from mq_connector_filter_relation where " + where +" order by mq_connector_id,filter_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from mq_connector_filter_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by mq_connector_id,filter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from mq_connector_filter_relation					 where " + where + " ) t1)t2 order by mq_connector_id,filter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select mq_connector_id,filter_id,reader_or_writer_filter, rownumber() over(order by mq_connector_id,filter_id) as rn from mq_connector_filter_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by mq_connector_id,filter_id) as rownumber, mq_connector_id,filter_id,reader_or_writer_filter from mq_connector_filter_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("MqConnectorFilterRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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
			String sql =  "select  count(*) from mq_connector_filter_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from mq_connector_filter_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("MqConnectorFilterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getReaderFilter4MQConnector(String mqConnectorId ) {
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
		MqConnectorFilterRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from mq_connector_filter_relation where mq_connector_id=? and reader_or_writer_filter='0'";
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
				debug("MqConnectorFilterRelationDAO.getReaderFilter4MQConnector() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : mqConnectorId = " + mqConnectorId);
			}


			while (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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

	public List getWriterFilter4MQConnector(String mqConnectorId ) {
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
		MqConnectorFilterRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from mq_connector_filter_relation where mq_connector_id=? and reader_or_writer_filter='1'";
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
				debug("MqConnectorFilterRelationDAO.getWriterFilter4MQConnector() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : mqConnectorId = " + mqConnectorId);
			}


			while (resultSet.next()) {
				returnDTO = new MqConnectorFilterRelation();
				returnDTO.setMqConnectorId(resultSet.getString("mq_connector_id"));
				returnDTO.setFilterId(resultSet.getString("filter_id"));
				returnDTO.setReaderOrWriterFilter(resultSet.getString("reader_or_writer_filter"));
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
