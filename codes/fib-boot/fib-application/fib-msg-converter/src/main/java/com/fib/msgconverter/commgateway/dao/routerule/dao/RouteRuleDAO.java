package com.fib.msgconverter.commgateway.dao.routerule.dao;

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



public class RouteRuleDAO extends AbstractDAO {

	public RouteRuleDAO() {
		super();
	}

	public RouteRuleDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public RouteRuleDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(RouteRule routeRule) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (routeRule == null) {
			throw new IllegalArgumentException("routeRule is NULL!");
		}
		if( routeRule.getId() != null && routeRule.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+routeRule.getId());
		}
		if( routeRule.getId() != null && routeRule.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+routeRule.getId());
		}
		if( routeRule.getRouteRuleType() != null && routeRule.getRouteRuleType().length() < 3 ){
			throw new IllegalArgumentException("routeRuleType too short"+routeRule.getRouteRuleType());
		}
		if( routeRule.getRouteRuleType() != null && routeRule.getRouteRuleType().length() > 3 ){
			throw new IllegalArgumentException("routeRuleType too long"+routeRule.getRouteRuleType());
		}
		if( routeRule.getDestChannelId() != null && routeRule.getDestChannelId().length() < 10 ){
			throw new IllegalArgumentException("destChannelId too short"+routeRule.getDestChannelId());
		}
		if( routeRule.getDestChannelId() != null && routeRule.getDestChannelId().length() > 10 ){
			throw new IllegalArgumentException("destChannelId too long"+routeRule.getDestChannelId());
		}
		if( routeRule.getExpression() != null && routeRule.getExpression().length() > 255 ){
			throw new IllegalArgumentException("expression too long"+routeRule.getExpression());
		}
		if( routeRule.getDeterminationId() != null && routeRule.getDeterminationId().length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+routeRule.getDeterminationId());
		}
		if( routeRule.getDeterminationId() != null && routeRule.getDeterminationId().length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+routeRule.getDeterminationId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into route_rule(id,route_rule_type,dest_channel_id,expression,determination_id) values(?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, routeRule.getId());
			statment.setString(2, routeRule.getRouteRuleType());
			statment.setString(3, routeRule.getDestChannelId());
			statment.setString(4, routeRule.getExpression());
			statment.setString(5, routeRule.getDeterminationId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RouteRuleDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+routeRule.getId() +"\",route_rule_type = \""+routeRule.getRouteRuleType() +"\",dest_channel_id = \""+routeRule.getDestChannelId() +"\",expression = \""+routeRule.getExpression() +"\",determination_id = \""+routeRule.getDeterminationId() +"\" ");
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


	public int[] insertBatch(List<RouteRule> routeRuleList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (routeRuleList == null) {
			throw new IllegalArgumentException("routeRuleList is NULL!");
		}
		for( RouteRule routeRule : routeRuleList){
		if( routeRule.getId() != null && routeRule.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+routeRule.getId());
		}
		if( routeRule.getId() != null && routeRule.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+routeRule.getId());
		}
		if( routeRule.getRouteRuleType() != null && routeRule.getRouteRuleType().length() < 3 ){
			throw new IllegalArgumentException("routeRuleType too short"+routeRule.getRouteRuleType());
		}
		if( routeRule.getRouteRuleType() != null && routeRule.getRouteRuleType().length() > 3 ){
			throw new IllegalArgumentException("routeRuleType too long"+routeRule.getRouteRuleType());
		}
		if( routeRule.getDestChannelId() != null && routeRule.getDestChannelId().length() < 10 ){
			throw new IllegalArgumentException("destChannelId too short"+routeRule.getDestChannelId());
		}
		if( routeRule.getDestChannelId() != null && routeRule.getDestChannelId().length() > 10 ){
			throw new IllegalArgumentException("destChannelId too long"+routeRule.getDestChannelId());
		}
		if( routeRule.getExpression() != null && routeRule.getExpression().length() > 255 ){
			throw new IllegalArgumentException("expression too long"+routeRule.getExpression());
		}
		if( routeRule.getDeterminationId() != null && routeRule.getDeterminationId().length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+routeRule.getDeterminationId());
		}
		if( routeRule.getDeterminationId() != null && routeRule.getDeterminationId().length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+routeRule.getDeterminationId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into route_rule(id,route_rule_type,dest_channel_id,expression,determination_id) values(?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( RouteRule routeRule : routeRuleList){
			statment.setString(1, routeRule.getId());
			statment.setString(2, routeRule.getRouteRuleType());
			statment.setString(3, routeRule.getDestChannelId());
			statment.setString(4, routeRule.getExpression());
			statment.setString(5, routeRule.getDeterminationId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RouteRuleDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(RouteRule routeRule) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (routeRule == null) {
			throw new IllegalArgumentException("routeRule is NULL!");
		}

		PreparedStatement statment = null;
		if( routeRule.getId() != null && routeRule.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+routeRule.getId());
		}
		if( routeRule.getId() != null && routeRule.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+routeRule.getId());
		}
		if( routeRule.getRouteRuleType() != null && routeRule.getRouteRuleType().length() < 3 ){
			throw new IllegalArgumentException("routeRuleType too short"+routeRule.getRouteRuleType());
		}
		if( routeRule.getRouteRuleType() != null && routeRule.getRouteRuleType().length() > 3 ){
			throw new IllegalArgumentException("routeRuleType too long"+routeRule.getRouteRuleType());
		}
		if( routeRule.getDestChannelId() != null && routeRule.getDestChannelId().length() < 10 ){
			throw new IllegalArgumentException("destChannelId too short"+routeRule.getDestChannelId());
		}
		if( routeRule.getDestChannelId() != null && routeRule.getDestChannelId().length() > 10 ){
			throw new IllegalArgumentException("destChannelId too long"+routeRule.getDestChannelId());
		}
		if( routeRule.getExpression() != null && routeRule.getExpression().length() > 255 ){
			throw new IllegalArgumentException("expression too long"+routeRule.getExpression());
		}
		if( routeRule.getDeterminationId() != null && routeRule.getDeterminationId().length() < 10 ){
			throw new IllegalArgumentException("determinationId too short"+routeRule.getDeterminationId());
		}
		if( routeRule.getDeterminationId() != null && routeRule.getDeterminationId().length() > 10 ){
			throw new IllegalArgumentException("determinationId too long"+routeRule.getDeterminationId());
		}
		try {
			String sql =  "UPDATE route_rule SET id=?,route_rule_type=?,dest_channel_id=?,expression=?,determination_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, routeRule.getId());
			statment.setString(2, routeRule.getRouteRuleType());
			statment.setString(3, routeRule.getDestChannelId());
			statment.setString(4, routeRule.getExpression());
			statment.setString(5, routeRule.getDeterminationId());
			statment.setString(6, routeRule.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("RouteRuleDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+routeRule.getId() +"\",route_rule_type = \""+routeRule.getRouteRuleType() +"\",dest_channel_id = \""+routeRule.getDestChannelId() +"\",expression = \""+routeRule.getExpression() +"\",determination_id = \""+routeRule.getDeterminationId() +"\" ");
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
		if(!primaryKey.containsKey("id") ){
			throw new IllegalArgumentException("id is null ");  
		}

		try {
			StringBuffer sql = new StringBuffer(64);
			sql.append("UPDATE route_rule SET ");
			Iterator it = updateFields.keySet().iterator();
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
				if("route_rule_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("dest_channel_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("expression".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("determination_id".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("RouteRuleDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM route_rule where id=? ";
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
				debug("RouteRuleDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public RouteRule selectByPK (String id)  {
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
		RouteRule returnDTO = null;

		try {
			String sql =  "SELECT * FROM route_rule where id=? ";
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
				debug("RouteRuleDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new RouteRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setRouteRuleType(resultSet.getString("route_rule_type"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setExpression(resultSet.getString("expression"));
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
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
		RouteRule returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from route_rule order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RouteRuleDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new RouteRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setRouteRuleType(resultSet.getString("route_rule_type"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setExpression(resultSet.getString("expression"));
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
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
		RouteRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from route_rule ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from route_rule					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,route_rule_type,dest_channel_id,expression,determination_id, rownumber() over(order by id) as rn from route_rule ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from route_rule order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,route_rule_type,dest_channel_id,expression,determination_id from route_rule ) A where rownumber > "  + startNum ;
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
				debug("RouteRuleDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new RouteRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setRouteRuleType(resultSet.getString("route_rule_type"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setExpression(resultSet.getString("expression"));
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
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
		RouteRule returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from route_rule where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RouteRuleDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new RouteRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setRouteRuleType(resultSet.getString("route_rule_type"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setExpression(resultSet.getString("expression"));
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
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
		RouteRule returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from route_rule where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from route_rule where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from route_rule					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,route_rule_type,dest_channel_id,expression,determination_id, rownumber() over(order by id) as rn from route_rule where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,route_rule_type,dest_channel_id,expression,determination_id from route_rule  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("RouteRuleDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new RouteRule();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setRouteRuleType(resultSet.getString("route_rule_type"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setExpression(resultSet.getString("expression"));
				returnDTO.setDeterminationId(resultSet.getString("determination_id"));
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
			String sql =  "select  count(*) from route_rule";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RouteRuleDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from route_rule where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("RouteRuleDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
