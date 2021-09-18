package com.fib.msgconverter.commgateway.dao.determinationcaserelation.dao;

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



public class DeterminationCaseRelationDAO extends AbstractDAO {

	public DeterminationCaseRelationDAO() {
		super();
	}

	public DeterminationCaseRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public DeterminationCaseRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(DeterminationCaseRelation determinationCaseRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (determinationCaseRelation == null) {
			throw new IllegalArgumentException("determinationCaseRelation is NULL!");
		}
		if( determinationCaseRelation.getDeterminationId() != null && determinationCaseRelation.getDeterminationId().length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+determinationCaseRelation.getDeterminationId());
		}
		if( determinationCaseRelation.getDeterminationId() != null && determinationCaseRelation.getDeterminationId().length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+determinationCaseRelation.getDeterminationId());
		}
		if( determinationCaseRelation.getCaseId() != null && determinationCaseRelation.getCaseId().length() < 10 ){
			throw new IllegalArgumentException("caseId too short"+determinationCaseRelation.getCaseId());
		}
		if( determinationCaseRelation.getCaseId() != null && determinationCaseRelation.getCaseId().length() > 10 ){
			throw new IllegalArgumentException("caseId too long"+determinationCaseRelation.getCaseId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into determination_case_relation(determination_id,case_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, determinationCaseRelation.getDeterminationId());
			statment.setString(2, determinationCaseRelation.getCaseId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : determination_id = \""+determinationCaseRelation.getDeterminationId() +"\",case_id = \""+determinationCaseRelation.getCaseId() +"\" ");
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


	public int[] insertBatch(List<DeterminationCaseRelation> determinationCaseRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (determinationCaseRelationList == null) {
			throw new IllegalArgumentException("determinationCaseRelationList is NULL!");
		}
		for( DeterminationCaseRelation determinationCaseRelation : determinationCaseRelationList){
		if( determinationCaseRelation.getDeterminationId() != null && determinationCaseRelation.getDeterminationId().length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+determinationCaseRelation.getDeterminationId());
		}
		if( determinationCaseRelation.getDeterminationId() != null && determinationCaseRelation.getDeterminationId().length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+determinationCaseRelation.getDeterminationId());
		}
		if( determinationCaseRelation.getCaseId() != null && determinationCaseRelation.getCaseId().length() < 10 ){
			throw new IllegalArgumentException("caseId too short"+determinationCaseRelation.getCaseId());
		}
		if( determinationCaseRelation.getCaseId() != null && determinationCaseRelation.getCaseId().length() > 10 ){
			throw new IllegalArgumentException("caseId too long"+determinationCaseRelation.getCaseId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into determination_case_relation(determination_id,case_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( DeterminationCaseRelation determinationCaseRelation : determinationCaseRelationList){
			statment.setString(1, determinationCaseRelation.getDeterminationId());
			statment.setString(2, determinationCaseRelation.getCaseId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(DeterminationCaseRelation determinationCaseRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (determinationCaseRelation == null) {
			throw new IllegalArgumentException("determinationCaseRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( determinationCaseRelation.getDeterminationId() != null && determinationCaseRelation.getDeterminationId().length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+determinationCaseRelation.getDeterminationId());
		}
		if( determinationCaseRelation.getDeterminationId() != null && determinationCaseRelation.getDeterminationId().length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+determinationCaseRelation.getDeterminationId());
		}
		if( determinationCaseRelation.getCaseId() != null && determinationCaseRelation.getCaseId().length() < 10 ){
			throw new IllegalArgumentException("caseId too short"+determinationCaseRelation.getCaseId());
		}
		if( determinationCaseRelation.getCaseId() != null && determinationCaseRelation.getCaseId().length() > 10 ){
			throw new IllegalArgumentException("caseId too long"+determinationCaseRelation.getCaseId());
		}
		try {
			String sql =  "UPDATE determination_case_relation SET determination_id=?,case_id=? where determination_id=? and case_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, determinationCaseRelation.getDeterminationId());
			statment.setString(2, determinationCaseRelation.getCaseId());
			statment.setString(3, determinationCaseRelation.getDeterminationId());			statment.setString(4, determinationCaseRelation.getCaseId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : determination_id = \""+determinationCaseRelation.getDeterminationId() +"\",case_id = \""+determinationCaseRelation.getCaseId() +"\" ");
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
		if(!primaryKey.containsKey("determination_id") ){
			throw new IllegalArgumentException("determination_id is null ");  
		}
		if(!primaryKey.containsKey("case_id") ){
			throw new IllegalArgumentException("case_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE determination_case_relation SET ");
			Iterator<String> it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where determination_id=? and case_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("determination_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("case_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("determination_id"));
			statment.setString(m++, (String)primaryKey.get("case_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("DeterminationCaseRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String determinationId,String caseId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (determinationId == null) {
			throw new IllegalArgumentException("determinationId is NULL!");
		}
		if (caseId == null) {
			throw new IllegalArgumentException("caseId is NULL!");
		}

		PreparedStatement statment = null;
		if( determinationId != null && determinationId.length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+determinationId);
		}
		if( determinationId != null && determinationId.length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+determinationId);
		}
		if( caseId != null && caseId.length() < 10 ){
			throw new IllegalArgumentException("caseId too short"+caseId);
		}
		if( caseId != null && caseId.length() > 10 ){
			throw new IllegalArgumentException("caseId too long"+caseId);
		}

		try {
			String sql = "DELETE FROM determination_case_relation where determination_id=? and case_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,determinationId);
			statment.setString(2,caseId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : determination_id = \""+determinationId+"\",case_id = \""+caseId+"\" ");
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


	public DeterminationCaseRelation selectByPK (String determinationId,String caseId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( determinationId != null && determinationId.length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+determinationId);
		}
		if( determinationId != null && determinationId.length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+determinationId);
		}
		if( caseId != null && caseId.length() < 10 ){
			throw new IllegalArgumentException("caseId too short"+caseId);
		}
		if( caseId != null && caseId.length() > 10 ){
			throw new IllegalArgumentException("caseId too long"+caseId);
		}
		ResultSet resultSet = null;
		DeterminationCaseRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM determination_case_relation where determination_id=? and case_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,determinationId);
			statment.setString(2,caseId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : determination_id = \""+determinationId+"\",case_id = \""+caseId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new DeterminationCaseRelation();
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
				returnDTO.setCaseId(resultSet.getString("case_id"));
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


	public List<DeterminationCaseRelation> findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCaseRelation returnDTO = null;
		List<DeterminationCaseRelation> list = new ArrayList<>();


		try {
			String sql = "select * from determination_case_relation order by determination_id,case_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCaseRelation();
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
				returnDTO.setCaseId(resultSet.getString("case_id"));
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


	public List<DeterminationCaseRelation> findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCaseRelation returnDTO = null;
		List<DeterminationCaseRelation> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from determination_case_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by determination_id,case_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from determination_case_relation					 ) t1)t2 order by determination_id,case_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select determination_id,case_id, rownumber() over(order by determination_id,case_id) as rn from determination_case_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from determination_case_relation order by determination_id,case_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by determination_id,case_id) as rownumber, determination_id,case_id from determination_case_relation ) A where rownumber > "  + startNum ;
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
				debug("DeterminationCaseRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCaseRelation();
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
				returnDTO.setCaseId(resultSet.getString("case_id"));
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


	public List<DeterminationCaseRelation> findByWhere (String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCaseRelation returnDTO = null;
		List<DeterminationCaseRelation> list = new ArrayList<>();

		try {
			String sql = "select * from determination_case_relation where " + where +" order by determination_id,case_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCaseRelation();
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
				returnDTO.setCaseId(resultSet.getString("case_id"));
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


	public List<DeterminationCaseRelation> findByWhere (String where, int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCaseRelation returnDTO = null;
		List<DeterminationCaseRelation> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from determination_case_relation where " + where +" order by determination_id,case_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from determination_case_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by determination_id,case_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from determination_case_relation					 where " + where + " ) t1)t2 order by determination_id,case_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select determination_id,case_id, rownumber() over(order by determination_id,case_id) as rn from determination_case_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by determination_id,case_id) as rownumber, determination_id,case_id from determination_case_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("DeterminationCaseRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCaseRelation();
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
				returnDTO.setCaseId(resultSet.getString("case_id"));
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
			String sql =  "select  count(*) from determination_case_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from determination_case_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List<DeterminationCaseRelation> getAllCase4Determination(String determinationId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( determinationId != null && determinationId.length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+determinationId);
		}
		if( determinationId != null && determinationId.length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+determinationId);
		}
		ResultSet resultSet = null;
		DeterminationCaseRelation returnDTO = null;
		List<DeterminationCaseRelation> list = new ArrayList<>();
		try {
			String sql = "select * from determination_case_relation where determination_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,determinationId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("DeterminationCaseRelationDAO.getAllCase4Determination() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : determinationId = " + determinationId);
			}


			while (resultSet.next()) {
				returnDTO = new DeterminationCaseRelation();
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
				returnDTO.setCaseId(resultSet.getString("case_id"));
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
