package com.fib.msgconverter.commgateway.dao.filterparameterrelation.dao;

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



public class FilterParameterRelationDAO extends AbstractDAO {

	public FilterParameterRelationDAO() {
		super();
	}

	public FilterParameterRelationDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public FilterParameterRelationDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(FilterParameterRelation filterParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (filterParameterRelation == null) {
			throw new IllegalArgumentException("filterParameterRelation is NULL!");
		}
		if( filterParameterRelation.getFilterId() != null && filterParameterRelation.getFilterId().length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterParameterRelation.getFilterId());
		}
		if( filterParameterRelation.getFilterId() != null && filterParameterRelation.getFilterId().length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterParameterRelation.getFilterId());
		}
		if( filterParameterRelation.getParameterId() != null && filterParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+filterParameterRelation.getParameterId());
		}
		if( filterParameterRelation.getParameterId() != null && filterParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+filterParameterRelation.getParameterId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into filter_parameter_relation(filter_id,parameter_id) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, filterParameterRelation.getFilterId());
			statment.setString(2, filterParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("FilterParameterRelationDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : filter_id = \""+filterParameterRelation.getFilterId() +"\",parameter_id = \""+filterParameterRelation.getParameterId() +"\" ");
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


	public int[] insertBatch(List<FilterParameterRelation> filterParameterRelationList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (filterParameterRelationList == null) {
			throw new IllegalArgumentException("filterParameterRelationList is NULL!");
		}
		for( FilterParameterRelation filterParameterRelation : filterParameterRelationList){
		if( filterParameterRelation.getFilterId() != null && filterParameterRelation.getFilterId().length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterParameterRelation.getFilterId());
		}
		if( filterParameterRelation.getFilterId() != null && filterParameterRelation.getFilterId().length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterParameterRelation.getFilterId());
		}
		if( filterParameterRelation.getParameterId() != null && filterParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+filterParameterRelation.getParameterId());
		}
		if( filterParameterRelation.getParameterId() != null && filterParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+filterParameterRelation.getParameterId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into filter_parameter_relation(filter_id,parameter_id) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( FilterParameterRelation filterParameterRelation : filterParameterRelationList){
			statment.setString(1, filterParameterRelation.getFilterId());
			statment.setString(2, filterParameterRelation.getParameterId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("FilterParameterRelationDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(FilterParameterRelation filterParameterRelation) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (filterParameterRelation == null) {
			throw new IllegalArgumentException("filterParameterRelation is NULL!");
		}

		PreparedStatement statment = null;
		if( filterParameterRelation.getFilterId() != null && filterParameterRelation.getFilterId().length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterParameterRelation.getFilterId());
		}
		if( filterParameterRelation.getFilterId() != null && filterParameterRelation.getFilterId().length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterParameterRelation.getFilterId());
		}
		if( filterParameterRelation.getParameterId() != null && filterParameterRelation.getParameterId().length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+filterParameterRelation.getParameterId());
		}
		if( filterParameterRelation.getParameterId() != null && filterParameterRelation.getParameterId().length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+filterParameterRelation.getParameterId());
		}
		try {
			String sql =  "UPDATE filter_parameter_relation SET filter_id=?,parameter_id=? where filter_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, filterParameterRelation.getFilterId());
			statment.setString(2, filterParameterRelation.getParameterId());
			statment.setString(3, filterParameterRelation.getFilterId());			statment.setString(4, filterParameterRelation.getParameterId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("FilterParameterRelationDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : filter_id = \""+filterParameterRelation.getFilterId() +"\",parameter_id = \""+filterParameterRelation.getParameterId() +"\" ");
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
		if(!primaryKey.containsKey("filter_id") ){
			throw new IllegalArgumentException("filter_id is null ");  
		}
		if(!primaryKey.containsKey("parameter_id") ){
			throw new IllegalArgumentException("parameter_id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE filter_parameter_relation SET ");
			Iterator it = updateFields.keySet().iterator();
			String tmpKey = null;
			while (it.hasNext()){
				sql.append(it.next());
				sql.append("=?,");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where filter_id=? and parameter_id=? ");
			statment =
				conn.prepareStatement(sql.toString());
			it = updateFields.keySet().iterator();
			String tmpStr = null;
			int m = 1;
			while (it.hasNext()){
				tmpKey = (String) it.next();
				if("filter_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("parameter_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
			}
			statment.setString(m++, (String)primaryKey.get("filter_id"));
			statment.setString(m++, (String)primaryKey.get("parameter_id"));


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				StringBuffer sbDebug = new StringBuffer(64);
				sbDebug.append("FilterParameterRelationDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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


	public int deleteByPK(String filterId,String parameterId) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (filterId == null) {
			throw new IllegalArgumentException("filterId is NULL!");
		}
		if (parameterId == null) {
			throw new IllegalArgumentException("parameterId is NULL!");
		}

		PreparedStatement statment = null;
		if( filterId != null && filterId.length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterId);
		}
		if( filterId != null && filterId.length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}

		try {
			String sql = "DELETE FROM filter_parameter_relation where filter_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,filterId);
			statment.setString(2,parameterId);


			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();


			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("FilterParameterRelationDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : filter_id = \""+filterId+"\",parameter_id = \""+parameterId+"\" ");
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


	public FilterParameterRelation selectByPK (String filterId,String parameterId)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( filterId != null && filterId.length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterId);
		}
		if( filterId != null && filterId.length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterId);
		}
		if( parameterId != null && parameterId.length() < 10 ){
			throw new IllegalArgumentException("parameterId too short"+parameterId);
		}
		if( parameterId != null && parameterId.length() > 10 ){
			throw new IllegalArgumentException("parameterId too long"+parameterId);
		}
		ResultSet resultSet = null;
		FilterParameterRelation returnDTO = null;

		try {
			String sql =  "SELECT * FROM filter_parameter_relation where filter_id=? and parameter_id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1,filterId);
			statment.setString(2,parameterId);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("FilterParameterRelationDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : filter_id = \""+filterId+"\",parameter_id = \""+parameterId+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new FilterParameterRelation();
				returnDTO.setFilterId(resultSet.getString("filter_id"));
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
		FilterParameterRelation returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from filter_parameter_relation order by filter_id,parameter_id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("FilterParameterRelationDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new FilterParameterRelation();
				returnDTO.setFilterId(resultSet.getString("filter_id"));
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
		FilterParameterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from filter_parameter_relation ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by filter_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from filter_parameter_relation					 ) t1)t2 order by filter_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select filter_id,parameter_id, rownumber() over(order by filter_id,parameter_id) as rn from filter_parameter_relation ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from filter_parameter_relation order by filter_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by filter_id,parameter_id) as rownumber, filter_id,parameter_id from filter_parameter_relation ) A where rownumber > "  + startNum ;
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
				debug("FilterParameterRelationDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new FilterParameterRelation();
				returnDTO.setFilterId(resultSet.getString("filter_id"));
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
		FilterParameterRelation returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from filter_parameter_relation where " + where +" order by filter_id,parameter_id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("FilterParameterRelationDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new FilterParameterRelation();
				returnDTO.setFilterId(resultSet.getString("filter_id"));
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
		FilterParameterRelation returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from filter_parameter_relation where " + where +" order by filter_id,parameter_id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from filter_parameter_relation where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by filter_id,parameter_id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from filter_parameter_relation					 where " + where + " ) t1)t2 order by filter_id,parameter_id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select filter_id,parameter_id, rownumber() over(order by filter_id,parameter_id) as rn from filter_parameter_relation where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by filter_id,parameter_id) as rownumber, filter_id,parameter_id from filter_parameter_relation  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("FilterParameterRelationDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new FilterParameterRelation();
				returnDTO.setFilterId(resultSet.getString("filter_id"));
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
			String sql =  "select  count(*) from filter_parameter_relation";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("FilterParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from filter_parameter_relation where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("FilterParameterRelationDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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


	public List getAllParameter4Filter(String filterId ) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( filterId != null && filterId.length() < 10 ){
			throw new IllegalArgumentException("filterId too short"+filterId);
		}
		if( filterId != null && filterId.length() > 10 ){
			throw new IllegalArgumentException("filterId too long"+filterId);
		}
		ResultSet resultSet = null;
		FilterParameterRelation returnDTO = null;
		List list = new ArrayList();
		try {
			String sql = "select * from filter_parameter_relation where filter_id=?";
			statment =
				conn.prepareStatement(sql);

			statment.setString(1,filterId);
			long _startTime=0, _endTime=0;
			if(DAOConfiguration.DEBUG){
				_startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				_endTime = System.currentTimeMillis();
				debug("FilterParameterRelationDAO.getAllParameter4Filter() spend "+(_endTime - _startTime)+"ms. SQL:"+sql+"; parameter : filterId = " + filterId);
			}


			while (resultSet.next()) {
				returnDTO = new FilterParameterRelation();
				returnDTO.setFilterId(resultSet.getString("filter_id"));
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
