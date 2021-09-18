package com.fib.msgconverter.commgateway.dao.recognizerparameterrelation.dao;

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



public class RecognizerParameterRelationDAO extends AbstractDAO {

	public RecognizerParameterRelationDAO() {
		super();
	}

	public RecognizerParameterRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public RecognizerParameterRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(RecognizerParameterRelation recognizerParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (recognizerParameterRelation == null) {
			throw new IllegalArgumentException("recognizerParameterRelation is NULL!");
		}
		if( recognizerParameterRelation.getRecognizerId() != null && recognizerParameterRelation.getRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("recognizerId too short"+recognizerParameterRelation.getRecognizerId());
		}
		if( recognizerParameterRelation.getRecognizerId() != null && recognizerParameterRelation.getRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("recognizerId too long"+recognizerParameterRelation.getRecognizerId());
		}
		if( recognizerParameterRelation.getParameterId() != null && recognizerParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+recognizerParameterRelation.getParameterId());
		}
		if( recognizerParameterRelation.getParameterId() != null && recognizerParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+recognizerParameterRelation.getParameterId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into recognizer_parameter_relation(recognizer_id,parameter_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, recognizerParameterRelation.getRecognizerId());
			statment.setString(2, recognizerParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : recognizer_id = \""+recognizerParameterRelation.getRecognizerId() +"\",parameter_id = \""+recognizerParameterRelation.getParameterId() +"\" ");
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


	public int[] insertBatch(List<RecognizerParameterRelation> recognizerParameterRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (recognizerParameterRelationList == null) {
			throw new IllegalArgumentException("recognizerParameterRelationList is NULL!");
		}
		for( RecognizerParameterRelation recognizerParameterRelation : recognizerParameterRelationList){
		if( recognizerParameterRelation.getRecognizerId() != null && recognizerParameterRelation.getRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("recognizerId too short"+recognizerParameterRelation.getRecognizerId());
		}
		if( recognizerParameterRelation.getRecognizerId() != null && recognizerParameterRelation.getRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("recognizerId too long"+recognizerParameterRelation.getRecognizerId());
		}
		if( recognizerParameterRelation.getParameterId() != null && recognizerParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+recognizerParameterRelation.getParameterId());
		}
		if( recognizerParameterRelation.getParameterId() != null && recognizerParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+recognizerParameterRelation.getParameterId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into recognizer_parameter_relation(recognizer_id,parameter_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( RecognizerParameterRelation recognizerParameterRelation : recognizerParameterRelationList){
			statment.setString(1, recognizerParameterRelation.getRecognizerId());
			statment.setString(2, recognizerParameterRelation.getParameterId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(RecognizerParameterRelation recognizerParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (recognizerParameterRelation == null) {
			throw new IllegalArgumentException("recognizerParameterRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( recognizerParameterRelation.getRecognizerId() != null && recognizerParameterRelation.getRecognizerId().length() < 10 ){
			throw new IllegalArgumentException("recognizerId too short"+recognizerParameterRelation.getRecognizerId());
		}
		if( recognizerParameterRelation.getRecognizerId() != null && recognizerParameterRelation.getRecognizerId().length() > 10 ){
			throw new IllegalArgumentException("recognizerId too long"+recognizerParameterRelation.getRecognizerId());
		}
		if( recognizerParameterRelation.getParameterId() != null && recognizerParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+recognizerParameterRelation.getParameterId());
		}
		if( recognizerParameterRelation.getParameterId() != null && recognizerParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+recognizerParameterRelation.getParameterId());
		}
		try {
			String sql =  "UPDATE recognizer_parameter_relation SET recognizer_id=?,parameter_id=? where recognizer_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, recognizerParameterRelation.getRecognizerId());
			statment.setString(2, recognizerParameterRelation.getParameterId());
			statment.setString(3, recognizerParameterRelation.getRecognizerId());			statment.setString(4, recognizerParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : recognizer_id = \""+recognizerParameterRelation.getRecognizerId() +"\",parameter_id = \""+recognizerParameterRelation.getParameterId() +"\" ");
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
		if(!primaryKey.containsKey("recognizer_id") ){
			throw new IllegalArgumentException("recognizer_id is null ");  
		}
		if(!primaryKey.containsKey("parameter_id") ){
			throw new IllegalArgumentException("parameter_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE recognizer_parameter_relation SET ");
			Iterator<String> it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where recognizer_id=? and parameter_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("recognizer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("parameter_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("recognizer_id"));
			statment.setString(m++, (String)primaryKey.get("parameter_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("RecognizerParameterRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String recognizerId,String parameterId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (recognizerId == null) {
			throw new IllegalArgumentException("recognizerId is NULL!");
		}
		if (parameterId == null) {
			throw new IllegalArgumentException("parameterId is NULL!");
		}

		PreparedStatement statment = null;
		if( recognizerId != null && recognizerId.length() < 10 ){
			throw new IllegalArgumentException("recognizerId too short"+recognizerId);
		}
		if( recognizerId != null && recognizerId.length() > 10 ){
			throw new IllegalArgumentException("recognizerId too long"+recognizerId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}

		try {
			String sql = "DELETE FROM recognizer_parameter_relation where recognizer_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,recognizerId);
			statment.setString(2,parameterId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : recognizer_id = \""+recognizerId+"\",parameter_id = \""+parameterId+"\" ");
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


	public RecognizerParameterRelation selectByPK (String recognizerId,String parameterId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( recognizerId != null && recognizerId.length() < 10 ){
			throw new IllegalArgumentException("recognizerId too short"+recognizerId);
		}
		if( recognizerId != null && recognizerId.length() > 10 ){
			throw new IllegalArgumentException("recognizerId too long"+recognizerId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}
		ResultSet resultSet = null;
		RecognizerParameterRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM recognizer_parameter_relation where recognizer_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,recognizerId);
			statment.setString(2,parameterId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : recognizer_id = \""+recognizerId+"\",parameter_id = \""+parameterId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new RecognizerParameterRelation();
				returnDTO.setRecognizerId(resultSet.getString("recognizer_id"));
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


	public List<RecognizerParameterRelation> findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		RecognizerParameterRelation returnDTO = null;
		List<RecognizerParameterRelation> list = new ArrayList<>();


		try {
			String sql = "select * from recognizer_parameter_relation order by recognizer_id,parameter_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerParameterRelation();
				returnDTO.setRecognizerId(resultSet.getString("recognizer_id"));
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


	public List<RecognizerParameterRelation> findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		RecognizerParameterRelation returnDTO = null;
		List<RecognizerParameterRelation> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from recognizer_parameter_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by recognizer_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from recognizer_parameter_relation					 ) t1)t2 order by recognizer_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select recognizer_id,parameter_id, rownumber() over(order by recognizer_id,parameter_id) as rn from recognizer_parameter_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from recognizer_parameter_relation order by recognizer_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by recognizer_id,parameter_id) as rownumber, recognizer_id,parameter_id from recognizer_parameter_relation ) A where rownumber > "  + startNum ;
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
				debug("RecognizerParameterRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerParameterRelation();
				returnDTO.setRecognizerId(resultSet.getString("recognizer_id"));
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


	public List<RecognizerParameterRelation> findByWhere (String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		RecognizerParameterRelation returnDTO = null;
		List<RecognizerParameterRelation> list = new ArrayList<RecognizerParameterRelation>();

		try {
			String sql = "select * from recognizer_parameter_relation where " + where +" order by recognizer_id,parameter_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerParameterRelation();
				returnDTO.setRecognizerId(resultSet.getString("recognizer_id"));
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


	public List<RecognizerParameterRelation> findByWhere (String where, int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		RecognizerParameterRelation returnDTO = null;
		List<RecognizerParameterRelation> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from recognizer_parameter_relation where " + where +" order by recognizer_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from recognizer_parameter_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by recognizer_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from recognizer_parameter_relation					 where " + where + " ) t1)t2 order by recognizer_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select recognizer_id,parameter_id, rownumber() over(order by recognizer_id,parameter_id) as rn from recognizer_parameter_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by recognizer_id,parameter_id) as rownumber, recognizer_id,parameter_id from recognizer_parameter_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("RecognizerParameterRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new RecognizerParameterRelation();
				returnDTO.setRecognizerId(resultSet.getString("recognizer_id"));
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
			String sql =  "select  count(*) from recognizer_parameter_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from recognizer_parameter_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List<RecognizerParameterRelation> getAllParameter4Recognizer(String recognizerId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( recognizerId != null && recognizerId.length() < 10 ){
			throw new IllegalArgumentException("recognizerId too short"+recognizerId);
		}
		if( recognizerId != null && recognizerId.length() > 10 ){
			throw new IllegalArgumentException("recognizerId too long"+recognizerId);
		}
		ResultSet resultSet = null;
		RecognizerParameterRelation returnDTO = null;
		List<RecognizerParameterRelation> list = new ArrayList<>();
		try {
			String sql = "select * from recognizer_parameter_relation where recognizer_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,recognizerId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("RecognizerParameterRelationDAO.getAllParameter4Recognizer() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : recognizerId = " + recognizerId);
			}


			while (resultSet.next()) {
				returnDTO = new RecognizerParameterRelation();
				returnDTO.setRecognizerId(resultSet.getString("recognizer_id"));
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
