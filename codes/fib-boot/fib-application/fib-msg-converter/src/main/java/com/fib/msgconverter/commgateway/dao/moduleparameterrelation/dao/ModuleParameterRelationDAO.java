package com.fib.msgconverter.commgateway.dao.moduleparameterrelation.dao;

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



public class ModuleParameterRelationDAO extends AbstractDAO {

	public ModuleParameterRelationDAO() {
		super();
	}

	public ModuleParameterRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ModuleParameterRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(ModuleParameterRelation moduleParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (moduleParameterRelation == null) {
			throw new IllegalArgumentException("moduleParameterRelation is NULL!");
		}
		if( moduleParameterRelation.getModuleId() != null && moduleParameterRelation.getModuleId().length() < 10 ){
			throw new IllegalArgumentException("moduleId too short"+moduleParameterRelation.getModuleId());
		}
		if( moduleParameterRelation.getModuleId() != null && moduleParameterRelation.getModuleId().length() > 10 ){
			throw new IllegalArgumentException("moduleId too long"+moduleParameterRelation.getModuleId());
		}
		if( moduleParameterRelation.getParameterId() != null && moduleParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+moduleParameterRelation.getParameterId());
		}
		if( moduleParameterRelation.getParameterId() != null && moduleParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+moduleParameterRelation.getParameterId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into module_parameter_relation(module_id,parameter_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, moduleParameterRelation.getModuleId());
			statment.setString(2, moduleParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : module_id = \""+moduleParameterRelation.getModuleId() +"\",parameter_id = \""+moduleParameterRelation.getParameterId() +"\" ");
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


	public int[] insertBatch(List<ModuleParameterRelation> moduleParameterRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (moduleParameterRelationList == null) {
			throw new IllegalArgumentException("moduleParameterRelationList is NULL!");
		}
		for( ModuleParameterRelation moduleParameterRelation : moduleParameterRelationList){
		if( moduleParameterRelation.getModuleId() != null && moduleParameterRelation.getModuleId().length() < 10 ){
			throw new IllegalArgumentException("moduleId too short"+moduleParameterRelation.getModuleId());
		}
		if( moduleParameterRelation.getModuleId() != null && moduleParameterRelation.getModuleId().length() > 10 ){
			throw new IllegalArgumentException("moduleId too long"+moduleParameterRelation.getModuleId());
		}
		if( moduleParameterRelation.getParameterId() != null && moduleParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+moduleParameterRelation.getParameterId());
		}
		if( moduleParameterRelation.getParameterId() != null && moduleParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+moduleParameterRelation.getParameterId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into module_parameter_relation(module_id,parameter_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( ModuleParameterRelation moduleParameterRelation : moduleParameterRelationList){
			statment.setString(1, moduleParameterRelation.getModuleId());
			statment.setString(2, moduleParameterRelation.getParameterId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(ModuleParameterRelation moduleParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (moduleParameterRelation == null) {
			throw new IllegalArgumentException("moduleParameterRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( moduleParameterRelation.getModuleId() != null && moduleParameterRelation.getModuleId().length() < 10 ){
			throw new IllegalArgumentException("moduleId too short"+moduleParameterRelation.getModuleId());
		}
		if( moduleParameterRelation.getModuleId() != null && moduleParameterRelation.getModuleId().length() > 10 ){
			throw new IllegalArgumentException("moduleId too long"+moduleParameterRelation.getModuleId());
		}
		if( moduleParameterRelation.getParameterId() != null && moduleParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+moduleParameterRelation.getParameterId());
		}
		if( moduleParameterRelation.getParameterId() != null && moduleParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+moduleParameterRelation.getParameterId());
		}
		try {
			String sql =  "UPDATE module_parameter_relation SET module_id=?,parameter_id=? where module_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, moduleParameterRelation.getModuleId());
			statment.setString(2, moduleParameterRelation.getParameterId());
			statment.setString(3, moduleParameterRelation.getModuleId());			statment.setString(4, moduleParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : module_id = \""+moduleParameterRelation.getModuleId() +"\",parameter_id = \""+moduleParameterRelation.getParameterId() +"\" ");
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
		if(!primaryKey.containsKey("module_id") ){
			throw new IllegalArgumentException("module_id is null ");  
		}
		if(!primaryKey.containsKey("parameter_id") ){
			throw new IllegalArgumentException("parameter_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE module_parameter_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where module_id=? and parameter_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("module_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("parameter_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("module_id"));
			statment.setString(m++, (String)primaryKey.get("parameter_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("ModuleParameterRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String moduleId,String parameterId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (moduleId == null) {
			throw new IllegalArgumentException("moduleId is NULL!");
		}
		if (parameterId == null) {
			throw new IllegalArgumentException("parameterId is NULL!");
		}

		PreparedStatement statment = null;
		if( moduleId != null && moduleId.length() < 10 ){
			throw new IllegalArgumentException("moduleId too short"+moduleId);
		}
		if( moduleId != null && moduleId.length() > 10 ){
			throw new IllegalArgumentException("moduleId too long"+moduleId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}

		try {
			String sql = "DELETE FROM module_parameter_relation where module_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,moduleId);
			statment.setString(2,parameterId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : module_id = \""+moduleId+"\",parameter_id = \""+parameterId+"\" ");
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


	public ModuleParameterRelation selectByPK (String moduleId,String parameterId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( moduleId != null && moduleId.length() < 10 ){
			throw new IllegalArgumentException("moduleId too short"+moduleId);
		}
		if( moduleId != null && moduleId.length() > 10 ){
			throw new IllegalArgumentException("moduleId too long"+moduleId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}
		ResultSet resultSet = null;
		ModuleParameterRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM module_parameter_relation where module_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,moduleId);
			statment.setString(2,parameterId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : module_id = \""+moduleId+"\",parameter_id = \""+parameterId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new ModuleParameterRelation();
				returnDTO.setModuleId(resultSet.getString("module_id"));
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
		ModuleParameterRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from module_parameter_relation order by module_id,parameter_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new ModuleParameterRelation();
				returnDTO.setModuleId(resultSet.getString("module_id"));
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
		ModuleParameterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from module_parameter_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by module_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from module_parameter_relation					 ) t1)t2 order by module_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select module_id,parameter_id, rownumber() over(order by module_id,parameter_id) as rn from module_parameter_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from module_parameter_relation order by module_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by module_id,parameter_id) as rownumber, module_id,parameter_id from module_parameter_relation ) A where rownumber > "  + startNum ;
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
				debug("ModuleParameterRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new ModuleParameterRelation();
				returnDTO.setModuleId(resultSet.getString("module_id"));
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
		ModuleParameterRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from module_parameter_relation where " + where +" order by module_id,parameter_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new ModuleParameterRelation();
				returnDTO.setModuleId(resultSet.getString("module_id"));
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
		ModuleParameterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from module_parameter_relation where " + where +" order by module_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from module_parameter_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by module_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from module_parameter_relation					 where " + where + " ) t1)t2 order by module_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select module_id,parameter_id, rownumber() over(order by module_id,parameter_id) as rn from module_parameter_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by module_id,parameter_id) as rownumber, module_id,parameter_id from module_parameter_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("ModuleParameterRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new ModuleParameterRelation();
				returnDTO.setModuleId(resultSet.getString("module_id"));
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
			String sql =  "select  count(*) from module_parameter_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from module_parameter_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllParameter4Module(String moduleId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( moduleId != null && moduleId.length() < 10 ){
			throw new IllegalArgumentException("moduleId too short"+moduleId);
		}
		if( moduleId != null && moduleId.length() > 10 ){
			throw new IllegalArgumentException("moduleId too long"+moduleId);
		}
		ResultSet resultSet = null;
		ModuleParameterRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from module_parameter_relation where module_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,moduleId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("ModuleParameterRelationDAO.getAllParameter4Module() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : moduleId = " + moduleId);
			}


			while (resultSet.next()) {
				returnDTO = new ModuleParameterRelation();
				returnDTO.setModuleId(resultSet.getString("module_id"));
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
