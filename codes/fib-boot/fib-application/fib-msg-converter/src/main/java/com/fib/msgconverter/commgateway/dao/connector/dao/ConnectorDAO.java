package com.fib.msgconverter.commgateway.dao.connector.dao;

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



public class ConnectorDAO extends AbstractDAO {

	public ConnectorDAO() {
		super();
	}

	public ConnectorDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ConnectorDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Connector connector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (connector == null) {
			throw new IllegalArgumentException("connector is NULL!");
		}
		if( connector.getId() != null && connector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+connector.getId());
		}
		if( connector.getId() != null && connector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+connector.getId());
		}
		if( connector.getConnectorType() != null && connector.getConnectorType().length() < 2 ){
			throw new IllegalArgumentException("connectorType too short"+connector.getConnectorType());
		}
		if( connector.getConnectorType() != null && connector.getConnectorType().length() > 2 ){
			throw new IllegalArgumentException("connectorType too long"+connector.getConnectorType());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into connector(id,connector_type) values(?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, connector.getId());
			statment.setString(2, connector.getConnectorType());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+connector.getId() +"\",connector_type = \""+connector.getConnectorType() +"\" ");
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


	public int[] insertBatch(List<Connector> connectorList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (connectorList == null) {
			throw new IllegalArgumentException("connectorList is NULL!");
		}
		for( Connector connector : connectorList){
		if( connector.getId() != null && connector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+connector.getId());
		}
		if( connector.getId() != null && connector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+connector.getId());
		}
		if( connector.getConnectorType() != null && connector.getConnectorType().length() < 2 ){
			throw new IllegalArgumentException("connectorType too short"+connector.getConnectorType());
		}
		if( connector.getConnectorType() != null && connector.getConnectorType().length() > 2 ){
			throw new IllegalArgumentException("connectorType too long"+connector.getConnectorType());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into connector(id,connector_type) values(?,?)";
			statment = conn.prepareStatement(sql); 
			for( Connector connector : connectorList){
			statment.setString(1, connector.getId());
			statment.setString(2, connector.getConnectorType());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(Connector connector) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (connector == null) {
			throw new IllegalArgumentException("connector is NULL!");
		}

		PreparedStatement statment = null;
		if( connector.getId() != null && connector.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+connector.getId());
		}
		if( connector.getId() != null && connector.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+connector.getId());
		}
		if( connector.getConnectorType() != null && connector.getConnectorType().length() < 2 ){
			throw new IllegalArgumentException("connectorType too short"+connector.getConnectorType());
		}
		if( connector.getConnectorType() != null && connector.getConnectorType().length() > 2 ){
			throw new IllegalArgumentException("connectorType too long"+connector.getConnectorType());
		}
		try {
			String sql =  "UPDATE connector SET id=?,connector_type=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, connector.getId());
			statment.setString(2, connector.getConnectorType());
			statment.setString(3, connector.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ConnectorDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+connector.getId() +"\",connector_type = \""+connector.getConnectorType() +"\" ");
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
			sql.append("UPDATE connector SET ");
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
				if("connector_type".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("ConnectorDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM connector where id=? ";
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
				debug("ConnectorDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public Connector selectByPK (String id)  {
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
		Connector returnDTO = null;

		try {
			String sql =  "SELECT * FROM connector where id=? ";
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
				debug("ConnectorDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Connector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
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


	public List<Connector> findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Connector returnDTO = null;
		List<Connector> list = new ArrayList<>();


		try {
			String sql = "select * from connector order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Connector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
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


	public List<Connector> findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Connector returnDTO = null;
		List<Connector> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from connector ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from connector					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,connector_type, rownumber() over(order by id) as rn from connector ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from connector order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connector_type from connector ) A where rownumber > "  + startNum ;
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
				debug("ConnectorDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Connector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
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


	public List<Connector> findByWhere (String where) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Connector returnDTO = null;
		List<Connector> list = new ArrayList<>();

		try {
			String sql = "select * from connector where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Connector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
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


	public List<Connector> findByWhere (String where, int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (where == null) {
			throw new IllegalArgumentException("where is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		Connector returnDTO = null;
		List<Connector> list = new ArrayList<>();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from connector where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from connector where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from connector					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,connector_type, rownumber() over(order by id) as rn from connector where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,connector_type from connector  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("ConnectorDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Connector();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setConnectorType(resultSet.getString("connector_type"));
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
			String sql =  "select  count(*) from connector";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from connector where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ConnectorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
