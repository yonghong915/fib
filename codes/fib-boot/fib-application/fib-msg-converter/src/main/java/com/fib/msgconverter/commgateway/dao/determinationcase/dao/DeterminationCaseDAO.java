package com.fib.msgconverter.commgateway.dao.determinationcase.dao;

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

public class DeterminationCaseDAO extends AbstractDAO {

	public DeterminationCaseDAO() {
		super();
	}

	public DeterminationCaseDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public DeterminationCaseDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(DeterminationCase determinationCase) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (determinationCase == null) {
			throw new IllegalArgumentException("determinationCase is NULL!");
		}
		if( determinationCase.getId() != null && determinationCase.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+determinationCase.getId());
		}
		if( determinationCase.getId() != null && determinationCase.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+determinationCase.getId());
		}
		if( determinationCase.getResult()==null || "".equals(determinationCase.getResult())){
			throw new IllegalArgumentException("result is null");
		}
		if( determinationCase.getResult() != null && determinationCase.getResult().length() > 255 ){
			throw new IllegalArgumentException("result too long"+determinationCase.getResult());
		}
		if( determinationCase.getChannelId() != null && determinationCase.getChannelId().length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+determinationCase.getChannelId());
		}
		if( determinationCase.getChannelId() != null && determinationCase.getChannelId().length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+determinationCase.getChannelId());
		}
		if( determinationCase.getProcessorOverride() != null && determinationCase.getProcessorOverride().length() < 10 ){
			throw new IllegalArgumentException("processorOverride too short"+determinationCase.getProcessorOverride());
		}
		if( determinationCase.getProcessorOverride() != null && determinationCase.getProcessorOverride().length() > 10 ){
			throw new IllegalArgumentException("processorOverride too long"+determinationCase.getProcessorOverride());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into determination_case(id,result,channel_id,processor_override) values(?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, determinationCase.getId());
			statment.setString(2, determinationCase.getResult());
			statment.setString(3, determinationCase.getChannelId());
			statment.setString(4, determinationCase.getProcessorOverride());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+determinationCase.getId() +"\",result = \""+determinationCase.getResult() +"\",channel_id = \""+determinationCase.getChannelId() +"\",processor_override = \""+determinationCase.getProcessorOverride() +"\" ");
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


	public int[] insertBatch(List<DeterminationCase> determinationCaseList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (determinationCaseList == null) {
			throw new IllegalArgumentException("determinationCaseList is NULL!");
		}
		for( DeterminationCase determinationCase : determinationCaseList){
		if( determinationCase.getId() != null && determinationCase.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+determinationCase.getId());
		}
		if( determinationCase.getId() != null && determinationCase.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+determinationCase.getId());
		}
		if( determinationCase.getResult()==null || "".equals(determinationCase.getResult())){
			throw new IllegalArgumentException("result is null");
		}
		if( determinationCase.getResult() != null && determinationCase.getResult().length() > 255 ){
			throw new IllegalArgumentException("result too long"+determinationCase.getResult());
		}
		if( determinationCase.getChannelId() != null && determinationCase.getChannelId().length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+determinationCase.getChannelId());
		}
		if( determinationCase.getChannelId() != null && determinationCase.getChannelId().length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+determinationCase.getChannelId());
		}
		if( determinationCase.getProcessorOverride() != null && determinationCase.getProcessorOverride().length() < 10 ){
			throw new IllegalArgumentException("processorOverride too short"+determinationCase.getProcessorOverride());
		}
		if( determinationCase.getProcessorOverride() != null && determinationCase.getProcessorOverride().length() > 10 ){
			throw new IllegalArgumentException("processorOverride too long"+determinationCase.getProcessorOverride());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into determination_case(id,result,channel_id,processor_override) values(?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( DeterminationCase determinationCase : determinationCaseList){
			statment.setString(1, determinationCase.getId());
			statment.setString(2, determinationCase.getResult());
			statment.setString(3, determinationCase.getChannelId());
			statment.setString(4, determinationCase.getProcessorOverride());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(DeterminationCase determinationCase) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (determinationCase == null) {
			throw new IllegalArgumentException("determinationCase is NULL!");
		}

		PreparedStatement statment = null;
		if( determinationCase.getId() != null && determinationCase.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+determinationCase.getId());
		}
		if( determinationCase.getId() != null && determinationCase.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+determinationCase.getId());
		}
		if( determinationCase.getResult()==null || "".equals(determinationCase.getResult())){
			throw new IllegalArgumentException("result is null");
		}
		if( determinationCase.getResult() != null && determinationCase.getResult().length() > 255 ){
			throw new IllegalArgumentException("result too long"+determinationCase.getResult());
		}
		if( determinationCase.getChannelId() != null && determinationCase.getChannelId().length() < 10 ){
			throw new IllegalArgumentException("channelId too short"+determinationCase.getChannelId());
		}
		if( determinationCase.getChannelId() != null && determinationCase.getChannelId().length() > 10 ){
			throw new IllegalArgumentException("channelId too long"+determinationCase.getChannelId());
		}
		if( determinationCase.getProcessorOverride() != null && determinationCase.getProcessorOverride().length() < 10 ){
			throw new IllegalArgumentException("processorOverride too short"+determinationCase.getProcessorOverride());
		}
		if( determinationCase.getProcessorOverride() != null && determinationCase.getProcessorOverride().length() > 10 ){
			throw new IllegalArgumentException("processorOverride too long"+determinationCase.getProcessorOverride());
		}
		try {
			String sql =  "UPDATE determination_case SET id=?,result=?,channel_id=?,processor_override=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, determinationCase.getId());
			statment.setString(2, determinationCase.getResult());
			statment.setString(3, determinationCase.getChannelId());
			statment.setString(4, determinationCase.getProcessorOverride());
			statment.setString(5, determinationCase.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("DeterminationCaseDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+determinationCase.getId() +"\",result = \""+determinationCase.getResult() +"\",channel_id = \""+determinationCase.getChannelId() +"\",processor_override = \""+determinationCase.getProcessorOverride() +"\" ");
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
		if(!primaryKey.containsKey("id") ){
			throw new IllegalArgumentException("id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE determination_case SET ");
			Iterator<String> it = updateFields.keySet().iterator();
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
				if("result".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("channel_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("processor_override".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("DeterminationCaseDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM determination_case where id=? ";
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
				debug("DeterminationCaseDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public DeterminationCase selectByPK (String id)  {
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
		DeterminationCase returnDTO = null;

		try {
			String sql =  "SELECT * FROM determination_case where id=? ";
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
				debug("DeterminationCaseDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new DeterminationCase();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setResult(resultSet.getString("result"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setProcessorOverride(resultSet.getString("processor_override"));
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


	public List<DeterminationCase> findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCase returnDTO = null;
		List<DeterminationCase> list = new ArrayList<>();


		try {
			String sql = "select * from determination_case order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCase();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setResult(resultSet.getString("result"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setProcessorOverride(resultSet.getString("processor_override"));
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


	public List<DeterminationCase> findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCase returnDTO = null;
		List<DeterminationCase> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from determination_case ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from determination_case					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,result,channel_id,processor_override, rownumber() over(order by id) as rn from determination_case ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from determination_case order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,result,channel_id,processor_override from determination_case ) A where rownumber > "  + startNum ;
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
				debug("DeterminationCaseDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCase();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setResult(resultSet.getString("result"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setProcessorOverride(resultSet.getString("processor_override"));
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


	public List<DeterminationCase> findByWhere (String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCase returnDTO = null;
		List<DeterminationCase> list = new ArrayList<>();

		try {
			String sql = "select * from determination_case where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCase();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setResult(resultSet.getString("result"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setProcessorOverride(resultSet.getString("processor_override"));
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


	public List<DeterminationCase> findByWhere (String where, int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		DeterminationCase returnDTO = null;
		List<DeterminationCase> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from determination_case where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from determination_case where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from determination_case					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,result,channel_id,processor_override, rownumber() over(order by id) as rn from determination_case where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,result,channel_id,processor_override from determination_case  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("DeterminationCaseDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new DeterminationCase();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setResult(resultSet.getString("result"));
				returnDTO.setChannelId(resultSet.getString("channel_id"));
				returnDTO.setProcessorOverride(resultSet.getString("processor_override"));
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
			String sql =  "select  count(*) from determination_case";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from determination_case where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("DeterminationCaseDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
