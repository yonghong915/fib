package com.fib.msgconverter.commgateway.dao.readerwriterparameterrelation.dao;

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



public class ReaderWriterParameterRelationDAO extends AbstractDAO {

	public ReaderWriterParameterRelationDAO() {
		super();
	}

	public ReaderWriterParameterRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ReaderWriterParameterRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(ReaderWriterParameterRelation readerWriterParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (readerWriterParameterRelation == null) {
			throw new IllegalArgumentException("readerWriterParameterRelation is NULL!");
		}
		if( readerWriterParameterRelation.getReaderWriterId() != null && readerWriterParameterRelation.getReaderWriterId().length() < 10 ){
			throw new IllegalArgumentException("readerWriterId too short"+readerWriterParameterRelation.getReaderWriterId());
		}
		if( readerWriterParameterRelation.getReaderWriterId() != null && readerWriterParameterRelation.getReaderWriterId().length() > 10 ){
			throw new IllegalArgumentException("readerWriterId too long"+readerWriterParameterRelation.getReaderWriterId());
		}
		if( readerWriterParameterRelation.getParameterId() != null && readerWriterParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+readerWriterParameterRelation.getParameterId());
		}
		if( readerWriterParameterRelation.getParameterId() != null && readerWriterParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+readerWriterParameterRelation.getParameterId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into reader_writer_parameter_relation(reader_writer_id,parameter_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, readerWriterParameterRelation.getReaderWriterId());
			statment.setString(2, readerWriterParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : reader_writer_id = \""+readerWriterParameterRelation.getReaderWriterId() +"\",parameter_id = \""+readerWriterParameterRelation.getParameterId() +"\" ");
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


	public int[] insertBatch(List<ReaderWriterParameterRelation> readerWriterParameterRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (readerWriterParameterRelationList == null) {
			throw new IllegalArgumentException("readerWriterParameterRelationList is NULL!");
		}
		for( ReaderWriterParameterRelation readerWriterParameterRelation : readerWriterParameterRelationList){
		if( readerWriterParameterRelation.getReaderWriterId() != null && readerWriterParameterRelation.getReaderWriterId().length() < 10 ){
			throw new IllegalArgumentException("readerWriterId too short"+readerWriterParameterRelation.getReaderWriterId());
		}
		if( readerWriterParameterRelation.getReaderWriterId() != null && readerWriterParameterRelation.getReaderWriterId().length() > 10 ){
			throw new IllegalArgumentException("readerWriterId too long"+readerWriterParameterRelation.getReaderWriterId());
		}
		if( readerWriterParameterRelation.getParameterId() != null && readerWriterParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+readerWriterParameterRelation.getParameterId());
		}
		if( readerWriterParameterRelation.getParameterId() != null && readerWriterParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+readerWriterParameterRelation.getParameterId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into reader_writer_parameter_relation(reader_writer_id,parameter_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( ReaderWriterParameterRelation readerWriterParameterRelation : readerWriterParameterRelationList){
			statment.setString(1, readerWriterParameterRelation.getReaderWriterId());
			statment.setString(2, readerWriterParameterRelation.getParameterId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(ReaderWriterParameterRelation readerWriterParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (readerWriterParameterRelation == null) {
			throw new IllegalArgumentException("readerWriterParameterRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( readerWriterParameterRelation.getReaderWriterId() != null && readerWriterParameterRelation.getReaderWriterId().length() < 10 ){
			throw new IllegalArgumentException("readerWriterId too short"+readerWriterParameterRelation.getReaderWriterId());
		}
		if( readerWriterParameterRelation.getReaderWriterId() != null && readerWriterParameterRelation.getReaderWriterId().length() > 10 ){
			throw new IllegalArgumentException("readerWriterId too long"+readerWriterParameterRelation.getReaderWriterId());
		}
		if( readerWriterParameterRelation.getParameterId() != null && readerWriterParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+readerWriterParameterRelation.getParameterId());
		}
		if( readerWriterParameterRelation.getParameterId() != null && readerWriterParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+readerWriterParameterRelation.getParameterId());
		}
		try {
			String sql =  "UPDATE reader_writer_parameter_relation SET reader_writer_id=?,parameter_id=? where reader_writer_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, readerWriterParameterRelation.getReaderWriterId());
			statment.setString(2, readerWriterParameterRelation.getParameterId());
			statment.setString(3, readerWriterParameterRelation.getReaderWriterId());			statment.setString(4, readerWriterParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : reader_writer_id = \""+readerWriterParameterRelation.getReaderWriterId() +"\",parameter_id = \""+readerWriterParameterRelation.getParameterId() +"\" ");
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
		if(!primaryKey.containsKey("reader_writer_id") ){
			throw new IllegalArgumentException("reader_writer_id is null ");  
		}
		if(!primaryKey.containsKey("parameter_id") ){
			throw new IllegalArgumentException("parameter_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE reader_writer_parameter_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where reader_writer_id=? and parameter_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("reader_writer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("parameter_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("reader_writer_id"));
			statment.setString(m++, (String)primaryKey.get("parameter_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("ReaderWriterParameterRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String readerWriterId,String parameterId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (readerWriterId == null) {
			throw new IllegalArgumentException("readerWriterId is NULL!");
		}
		if (parameterId == null) {
			throw new IllegalArgumentException("parameterId is NULL!");
		}

		PreparedStatement statment = null;
		if( readerWriterId != null && readerWriterId.length() < 10 ){
			throw new IllegalArgumentException("readerWriterId too short"+readerWriterId);
		}
		if( readerWriterId != null && readerWriterId.length() > 10 ){
			throw new IllegalArgumentException("readerWriterId too long"+readerWriterId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}

		try {
			String sql = "DELETE FROM reader_writer_parameter_relation where reader_writer_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,readerWriterId);
			statment.setString(2,parameterId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : reader_writer_id = \""+readerWriterId+"\",parameter_id = \""+parameterId+"\" ");
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


	public ReaderWriterParameterRelation selectByPK (String readerWriterId,String parameterId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( readerWriterId != null && readerWriterId.length() < 10 ){
			throw new IllegalArgumentException("readerWriterId too short"+readerWriterId);
		}
		if( readerWriterId != null && readerWriterId.length() > 10 ){
			throw new IllegalArgumentException("readerWriterId too long"+readerWriterId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}
		ResultSet resultSet = null;
		ReaderWriterParameterRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM reader_writer_parameter_relation where reader_writer_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,readerWriterId);
			statment.setString(2,parameterId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : reader_writer_id = \""+readerWriterId+"\",parameter_id = \""+parameterId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new ReaderWriterParameterRelation();
				returnDTO.setReaderWriterId(resultSet.getString("reader_writer_id"));
				returnDTO.setParameterId(resultSet.getString("parameter_id"));
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
		ReaderWriterParameterRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from reader_writer_parameter_relation order by reader_writer_id,parameter_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new ReaderWriterParameterRelation();
				returnDTO.setReaderWriterId(resultSet.getString("reader_writer_id"));
				returnDTO.setParameterId(resultSet.getString("parameter_id"));
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
		ReaderWriterParameterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from reader_writer_parameter_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by reader_writer_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from reader_writer_parameter_relation					 ) t1)t2 order by reader_writer_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select reader_writer_id,parameter_id, rownumber() over(order by reader_writer_id,parameter_id) as rn from reader_writer_parameter_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from reader_writer_parameter_relation order by reader_writer_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by reader_writer_id,parameter_id) as rownumber, reader_writer_id,parameter_id from reader_writer_parameter_relation ) A where rownumber > "  + startNum ;
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
				debug("ReaderWriterParameterRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new ReaderWriterParameterRelation();
				returnDTO.setReaderWriterId(resultSet.getString("reader_writer_id"));
				returnDTO.setParameterId(resultSet.getString("parameter_id"));
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
		ReaderWriterParameterRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from reader_writer_parameter_relation where " + where +" order by reader_writer_id,parameter_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new ReaderWriterParameterRelation();
				returnDTO.setReaderWriterId(resultSet.getString("reader_writer_id"));
				returnDTO.setParameterId(resultSet.getString("parameter_id"));
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
		ReaderWriterParameterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from reader_writer_parameter_relation where " + where +" order by reader_writer_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from reader_writer_parameter_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by reader_writer_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from reader_writer_parameter_relation					 where " + where + " ) t1)t2 order by reader_writer_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select reader_writer_id,parameter_id, rownumber() over(order by reader_writer_id,parameter_id) as rn from reader_writer_parameter_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by reader_writer_id,parameter_id) as rownumber, reader_writer_id,parameter_id from reader_writer_parameter_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("ReaderWriterParameterRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new ReaderWriterParameterRelation();
				returnDTO.setReaderWriterId(resultSet.getString("reader_writer_id"));
				returnDTO.setParameterId(resultSet.getString("parameter_id"));
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
			String sql =  "select  count(*) from reader_writer_parameter_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from reader_writer_parameter_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllParameter4ReaderOrWriter(String readerWriterId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( readerWriterId != null && readerWriterId.length() < 10 ){
			throw new IllegalArgumentException("readerWriterId too short"+readerWriterId);
		}
		if( readerWriterId != null && readerWriterId.length() > 10 ){
			throw new IllegalArgumentException("readerWriterId too long"+readerWriterId);
		}
		ResultSet resultSet = null;
		ReaderWriterParameterRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from reader_writer_parameter_relation where reader_writer_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,readerWriterId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("ReaderWriterParameterRelationDAO.getAllParameter4ReaderOrWriter() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : readerWriterId = " + readerWriterId);
			}


			while (resultSet.next()) {
				returnDTO = new ReaderWriterParameterRelation();
				returnDTO.setReaderWriterId(resultSet.getString("reader_writer_id"));
				returnDTO.setParameterId(resultSet.getString("parameter_id"));
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
