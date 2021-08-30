package com.fib.msgconverter.commgateway.dao.recognizerrelation.dao;

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



public class RecognizerRelationDAO extends AbstractDAO {

	public RecognizerRelationDAO() {
		super();
	}

	public RecognizerRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public RecognizerRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(RecognizerRelation recognizerRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (recognizerRelation == null) {
			throw new IllegalArgumentException("recognizerRelation is NULL!");
		}
		if( recognizerRelation.getCompositeRecognizerId() != null && recognizerRelation.getCompositeRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too short"+recognizerRelation.getCompositeRecognizerId());
		}
		if( recognizerRelation.getCompositeRecognizerId() != null && recognizerRelation.getCompositeRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too long"+recognizerRelation.getCompositeRecognizerId());
		}
		if( recognizerRelation.getSubRecognizerId() != null && recognizerRelation.getSubRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("subRecognizerId too short"+recognizerRelation.getSubRecognizerId());
		}
		if( recognizerRelation.getSubRecognizerId() != null && recognizerRelation.getSubRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("subRecognizerId too long"+recognizerRelation.getSubRecognizerId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into recognizer_relation(composite_recognizer_id,sub_recognizer_id,sub_index) values(?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, recognizerRelation.getCompositeRecognizerId());
			statment.setString(2, recognizerRelation.getSubRecognizerId());
			statment.setInt(3, recognizerRelation.getSubIndex());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : composite_recognizer_id = \""+recognizerRelation.getCompositeRecognizerId() +"\",sub_recognizer_id = \""+recognizerRelation.getSubRecognizerId() +"\",sub_index = "+recognizerRelation.getSubIndex()+" ");
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


	public int[] insertBatch(List<RecognizerRelation> recognizerRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (recognizerRelationList == null) {
			throw new IllegalArgumentException("recognizerRelationList is NULL!");
		}
		for( RecognizerRelation recognizerRelation : recognizerRelationList){
		if( recognizerRelation.getCompositeRecognizerId() != null && recognizerRelation.getCompositeRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too short"+recognizerRelation.getCompositeRecognizerId());
		}
		if( recognizerRelation.getCompositeRecognizerId() != null && recognizerRelation.getCompositeRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too long"+recognizerRelation.getCompositeRecognizerId());
		}
		if( recognizerRelation.getSubRecognizerId() != null && recognizerRelation.getSubRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("subRecognizerId too short"+recognizerRelation.getSubRecognizerId());
		}
		if( recognizerRelation.getSubRecognizerId() != null && recognizerRelation.getSubRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("subRecognizerId too long"+recognizerRelation.getSubRecognizerId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into recognizer_relation(composite_recognizer_id,sub_recognizer_id,sub_index) values(?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( RecognizerRelation recognizerRelation : recognizerRelationList){
			statment.setString(1, recognizerRelation.getCompositeRecognizerId());
			statment.setString(2, recognizerRelation.getSubRecognizerId());
			statment.setInt(3, recognizerRelation.getSubIndex());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(RecognizerRelation recognizerRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (recognizerRelation == null) {
			throw new IllegalArgumentException("recognizerRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( recognizerRelation.getCompositeRecognizerId() != null && recognizerRelation.getCompositeRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too short"+recognizerRelation.getCompositeRecognizerId());
		}
		if( recognizerRelation.getCompositeRecognizerId() != null && recognizerRelation.getCompositeRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too long"+recognizerRelation.getCompositeRecognizerId());
		}
		if( recognizerRelation.getSubRecognizerId() != null && recognizerRelation.getSubRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("subRecognizerId too short"+recognizerRelation.getSubRecognizerId());
		}
		if( recognizerRelation.getSubRecognizerId() != null && recognizerRelation.getSubRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("subRecognizerId too long"+recognizerRelation.getSubRecognizerId());
		}
		try {
			String sql =  "UPDATE recognizer_relation SET composite_recognizer_id=?,sub_recognizer_id=?,sub_index=? where composite_recognizer_id=? and sub_recognizer_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, recognizerRelation.getCompositeRecognizerId());
			statment.setString(2, recognizerRelation.getSubRecognizerId());
			statment.setInt(3, recognizerRelation.getSubIndex());
			statment.setString(4, recognizerRelation.getCompositeRecognizerId());			statment.setString(5, recognizerRelation.getSubRecognizerId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : composite_recognizer_id = \""+recognizerRelation.getCompositeRecognizerId() +"\",sub_recognizer_id = \""+recognizerRelation.getSubRecognizerId() +"\",sub_index = "+recognizerRelation.getSubIndex()+" ");
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
		if(!primaryKey.containsKey("composite_recognizer_id") ){
			throw new IllegalArgumentException("composite_recognizer_id is null ");  
		}
		if(!primaryKey.containsKey("sub_recognizer_id") ){
			throw new IllegalArgumentException("sub_recognizer_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE recognizer_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where composite_recognizer_id=? and sub_recognizer_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("composite_recognizer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("sub_recognizer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("sub_index".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("composite_recognizer_id"));
			statment.setString(m++, (String)primaryKey.get("sub_recognizer_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("RecognizerRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String compositeRecognizerId,String subRecognizerId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (compositeRecognizerId == null) {
			throw new IllegalArgumentException("compositeRecognizerId is NULL!");
		}
		if (subRecognizerId == null) {
			throw new IllegalArgumentException("subRecognizerId is NULL!");
		}

		PreparedStatement statment = null;
		if( compositeRecognizerId != null && compositeRecognizerId.length() < 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too short"+compositeRecognizerId);
		}
		if( compositeRecognizerId != null && compositeRecognizerId.length() > 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too long"+compositeRecognizerId);
		}
		if( subRecognizerId != null && subRecognizerId.length() < 10 ){
			throw new IllegalArgumentException("subRecognizerId too short"+subRecognizerId);
		}
		if( subRecognizerId != null && subRecognizerId.length() > 10 ){
			throw new IllegalArgumentException("subRecognizerId too long"+subRecognizerId);
		}

		try {
			String sql = "DELETE FROM recognizer_relation where composite_recognizer_id=? and sub_recognizer_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,compositeRecognizerId);
			statment.setString(2,subRecognizerId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : composite_recognizer_id = \""+compositeRecognizerId+"\",sub_recognizer_id = \""+subRecognizerId+"\" ");
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


	public RecognizerRelation selectByPK (String compositeRecognizerId,String subRecognizerId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( compositeRecognizerId != null && compositeRecognizerId.length() < 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too short"+compositeRecognizerId);
		}
		if( compositeRecognizerId != null && compositeRecognizerId.length() > 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too long"+compositeRecognizerId);
		}
		if( subRecognizerId != null && subRecognizerId.length() < 10 ){
			throw new IllegalArgumentException("subRecognizerId too short"+subRecognizerId);
		}
		if( subRecognizerId != null && subRecognizerId.length() > 10 ){
			throw new IllegalArgumentException("subRecognizerId too long"+subRecognizerId);
		}
		ResultSet resultSet = null;
		RecognizerRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM recognizer_relation where composite_recognizer_id=? and sub_recognizer_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,compositeRecognizerId);
			statment.setString(2,subRecognizerId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : composite_recognizer_id = \""+compositeRecognizerId+"\",sub_recognizer_id = \""+subRecognizerId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new RecognizerRelation();
				returnDTO.setCompositeRecognizerId(resultSet.getString("composite_recognizer_id"));
				returnDTO.setSubRecognizerId(resultSet.getString("sub_recognizer_id"));
				returnDTO.setSubIndex(resultSet.getInt("sub_index"));
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
		RecognizerRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from recognizer_relation order by composite_recognizer_id,sub_recognizer_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerRelation();
				returnDTO.setCompositeRecognizerId(resultSet.getString("composite_recognizer_id"));
				returnDTO.setSubRecognizerId(resultSet.getString("sub_recognizer_id"));
				returnDTO.setSubIndex(resultSet.getInt("sub_index"));
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
		RecognizerRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from recognizer_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by composite_recognizer_id,sub_recognizer_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from recognizer_relation					 ) t1)t2 order by composite_recognizer_id,sub_recognizer_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select composite_recognizer_id,sub_recognizer_id,sub_index, rownumber() over(order by composite_recognizer_id,sub_recognizer_id) as rn from recognizer_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from recognizer_relation order by composite_recognizer_id,sub_recognizer_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by composite_recognizer_id,sub_recognizer_id) as rownumber, composite_recognizer_id,sub_recognizer_id,sub_index from recognizer_relation ) A where rownumber > "  + startNum ;
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
				debug("RecognizerRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerRelation();
				returnDTO.setCompositeRecognizerId(resultSet.getString("composite_recognizer_id"));
				returnDTO.setSubRecognizerId(resultSet.getString("sub_recognizer_id"));
				returnDTO.setSubIndex(resultSet.getInt("sub_index"));
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
		RecognizerRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from recognizer_relation where " + where +" order by composite_recognizer_id,sub_recognizer_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerRelation();
				returnDTO.setCompositeRecognizerId(resultSet.getString("composite_recognizer_id"));
				returnDTO.setSubRecognizerId(resultSet.getString("sub_recognizer_id"));
				returnDTO.setSubIndex(resultSet.getInt("sub_index"));
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
		RecognizerRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from recognizer_relation where " + where +" order by composite_recognizer_id,sub_recognizer_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from recognizer_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by composite_recognizer_id,sub_recognizer_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from recognizer_relation					 where " + where + " ) t1)t2 order by composite_recognizer_id,sub_recognizer_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select composite_recognizer_id,sub_recognizer_id,sub_index, rownumber() over(order by composite_recognizer_id,sub_recognizer_id) as rn from recognizer_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by composite_recognizer_id,sub_recognizer_id) as rownumber, composite_recognizer_id,sub_recognizer_id,sub_index from recognizer_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("RecognizerRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerRelation();
				returnDTO.setCompositeRecognizerId(resultSet.getString("composite_recognizer_id"));
				returnDTO.setSubRecognizerId(resultSet.getString("sub_recognizer_id"));
				returnDTO.setSubIndex(resultSet.getInt("sub_index"));
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
			String sql =  "select  count(*) from recognizer_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from recognizer_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllSubRecognizer4Recognizer(String compositeRecognizerId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( compositeRecognizerId != null && compositeRecognizerId.length() < 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too short"+compositeRecognizerId);
		}
		if( compositeRecognizerId != null && compositeRecognizerId.length() > 10 ){
			throw new IllegalArgumentException("compositeRecognizerId too long"+compositeRecognizerId);
		}
		ResultSet resultSet = null;
		RecognizerRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from recognizer_relation where composite_recognizer_id=? order by sub_index";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,compositeRecognizerId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("RecognizerRelationDAO.getAllSubRecognizer4Recognizer() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : compositeRecognizerId = " + compositeRecognizerId);
			}


			while (resultSet.next()) {
				returnDTO = new RecognizerRelation();
				returnDTO.setCompositeRecognizerId(resultSet.getString("composite_recognizer_id"));
				returnDTO.setSubRecognizerId(resultSet.getString("sub_recognizer_id"));
				returnDTO.setSubIndex(resultSet.getInt("sub_index"));
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
