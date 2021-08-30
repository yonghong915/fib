package com.fib.msgconverter.commgateway.dao.sessiondigest.dao;

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



public class SessionDigestDAO extends AbstractDAO {

	public SessionDigestDAO() {
		super();
	}

	public SessionDigestDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public SessionDigestDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public void insert(SessionDigest sessionDigest) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (sessionDigest == null) {
			throw new IllegalArgumentException("sessionDigest is NULL!");
		}
		if( sessionDigest.getId() != null && sessionDigest.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+sessionDigest.getId());
		}
		if( sessionDigest.getGatewayId() != null && sessionDigest.getGatewayId().length() > 80 ){
			throw new IllegalArgumentException("gatewayId too long"+sessionDigest.getGatewayId());
		}
		if( sessionDigest.getExternalSerialNum() != null && sessionDigest.getExternalSerialNum().length() > 32 ){
			throw new IllegalArgumentException("externalSerialNum too long"+sessionDigest.getExternalSerialNum());
		}
		if( sessionDigest.getStartDate() != null && sessionDigest.getStartDate().length() < 8 ){
			throw new IllegalArgumentException("startDate too short"+sessionDigest.getStartDate());
		}
		if( sessionDigest.getStartDate() != null && sessionDigest.getStartDate().length() > 8 ){
			throw new IllegalArgumentException("startDate too long"+sessionDigest.getStartDate());
		}
		if( sessionDigest.getStartTime() != null && sessionDigest.getStartTime().length() < 6 ){
			throw new IllegalArgumentException("startTime too short"+sessionDigest.getStartTime());
		}
		if( sessionDigest.getStartTime() != null && sessionDigest.getStartTime().length() > 6 ){
			throw new IllegalArgumentException("startTime too long"+sessionDigest.getStartTime());
		}
		if( sessionDigest.getEndDate() != null && sessionDigest.getEndDate().length() < 8 ){
			throw new IllegalArgumentException("endDate too short"+sessionDigest.getEndDate());
		}
		if( sessionDigest.getEndDate() != null && sessionDigest.getEndDate().length() > 8 ){
			throw new IllegalArgumentException("endDate too long"+sessionDigest.getEndDate());
		}
		if( sessionDigest.getEndTime() != null && sessionDigest.getEndTime().length() < 6 ){
			throw new IllegalArgumentException("endTime too short"+sessionDigest.getEndTime());
		}
		if( sessionDigest.getEndTime() != null && sessionDigest.getEndTime().length() > 6 ){
			throw new IllegalArgumentException("endTime too long"+sessionDigest.getEndTime());
		}
		if( sessionDigest.getRequestSendDate() != null && sessionDigest.getRequestSendDate().length() < 8 ){
			throw new IllegalArgumentException("requestSendDate too short"+sessionDigest.getRequestSendDate());
		}
		if( sessionDigest.getRequestSendDate() != null && sessionDigest.getRequestSendDate().length() > 8 ){
			throw new IllegalArgumentException("requestSendDate too long"+sessionDigest.getRequestSendDate());
		}
		if( sessionDigest.getRequestSendTime() != null && sessionDigest.getRequestSendTime().length() < 6 ){
			throw new IllegalArgumentException("requestSendTime too short"+sessionDigest.getRequestSendTime());
		}
		if( sessionDigest.getRequestSendTime() != null && sessionDigest.getRequestSendTime().length() > 6 ){
			throw new IllegalArgumentException("requestSendTime too long"+sessionDigest.getRequestSendTime());
		}
		if( sessionDigest.getResponseArrivedDate() != null && sessionDigest.getResponseArrivedDate().length() < 8 ){
			throw new IllegalArgumentException("responseArrivedDate too short"+sessionDigest.getResponseArrivedDate());
		}
		if( sessionDigest.getResponseArrivedDate() != null && sessionDigest.getResponseArrivedDate().length() > 8 ){
			throw new IllegalArgumentException("responseArrivedDate too long"+sessionDigest.getResponseArrivedDate());
		}
		if( sessionDigest.getResponseArrivedTime() != null && sessionDigest.getResponseArrivedTime().length() < 6 ){
			throw new IllegalArgumentException("responseArrivedTime too short"+sessionDigest.getResponseArrivedTime());
		}
		if( sessionDigest.getResponseArrivedTime() != null && sessionDigest.getResponseArrivedTime().length() > 6 ){
			throw new IllegalArgumentException("responseArrivedTime too long"+sessionDigest.getResponseArrivedTime());
		}
		if( sessionDigest.getState() != null && sessionDigest.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+sessionDigest.getState());
		}
		if( sessionDigest.getState() != null && sessionDigest.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+sessionDigest.getState());
		}
		if( sessionDigest.getType() != null && sessionDigest.getType().length() < 2 ){
			throw new IllegalArgumentException("type too short"+sessionDigest.getType());
		}
		if( sessionDigest.getType() != null && sessionDigest.getType().length() > 2 ){
			throw new IllegalArgumentException("type too long"+sessionDigest.getType());
		}
		if( sessionDigest.getProcessorId() != null && sessionDigest.getProcessorId().length() > 80 ){
			throw new IllegalArgumentException("processorId too long"+sessionDigest.getProcessorId());
		}
		if( sessionDigest.getSourceChannelId() != null && sessionDigest.getSourceChannelId().length() > 80 ){
			throw new IllegalArgumentException("sourceChannelId too long"+sessionDigest.getSourceChannelId());
		}
		if( sessionDigest.getSourceChannelName() != null && sessionDigest.getSourceChannelName().length() > 80 ){
			throw new IllegalArgumentException("sourceChannelName too long"+sessionDigest.getSourceChannelName());
		}
		if( sessionDigest.getDestChannelId() != null && sessionDigest.getDestChannelId().length() > 80 ){
			throw new IllegalArgumentException("destChannelId too long"+sessionDigest.getDestChannelId());
		}
		if( sessionDigest.getDestChannelName() != null && sessionDigest.getDestChannelName().length() > 80 ){
			throw new IllegalArgumentException("destChannelName too long"+sessionDigest.getDestChannelName());
		}
		if( sessionDigest.getErrorType() != null && sessionDigest.getErrorType().length() < 4 ){
			throw new IllegalArgumentException("errorType too short"+sessionDigest.getErrorType());
		}
		if( sessionDigest.getErrorType() != null && sessionDigest.getErrorType().length() > 4 ){
			throw new IllegalArgumentException("errorType too long"+sessionDigest.getErrorType());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into session_digest(id,gateway_id,external_serial_num,start_date,start_time,end_date,end_time,request_send_date,request_send_time,response_arrived_date,response_arrived_time,state,type,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, sessionDigest.getId());
			statment.setString(2, sessionDigest.getGatewayId());
			statment.setString(3, sessionDigest.getExternalSerialNum());
			statment.setString(4, sessionDigest.getStartDate());
			statment.setString(5, sessionDigest.getStartTime());
			statment.setString(6, sessionDigest.getEndDate());
			statment.setString(7, sessionDigest.getEndTime());
			statment.setString(8, sessionDigest.getRequestSendDate());
			statment.setString(9, sessionDigest.getRequestSendTime());
			statment.setString(10, sessionDigest.getResponseArrivedDate());
			statment.setString(11, sessionDigest.getResponseArrivedTime());
			statment.setString(12, sessionDigest.getState());
			statment.setString(13, sessionDigest.getType());
			statment.setString(14, sessionDigest.getProcessorId());
			statment.setString(15, sessionDigest.getSourceChannelId());
			statment.setString(16, sessionDigest.getSourceChannelName());
			statment.setString(17, sessionDigest.getDestChannelId());
			statment.setString(18, sessionDigest.getDestChannelName());
			statment.setString(19, sessionDigest.getErrorType());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("SessionDigestDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+sessionDigest.getId() +"\",gateway_id = \""+sessionDigest.getGatewayId() +"\",external_serial_num = \""+sessionDigest.getExternalSerialNum() +"\",start_date = \""+sessionDigest.getStartDate() +"\",start_time = \""+sessionDigest.getStartTime() +"\",end_date = \""+sessionDigest.getEndDate() +"\",end_time = \""+sessionDigest.getEndTime() +"\",request_send_date = \""+sessionDigest.getRequestSendDate() +"\",request_send_time = \""+sessionDigest.getRequestSendTime() +"\",response_arrived_date = \""+sessionDigest.getResponseArrivedDate() +"\",response_arrived_time = \""+sessionDigest.getResponseArrivedTime() +"\",state = \""+sessionDigest.getState() +"\",type = \""+sessionDigest.getType() +"\",processor_id = \""+sessionDigest.getProcessorId() +"\",source_channel_id = \""+sessionDigest.getSourceChannelId() +"\",source_channel_name = \""+sessionDigest.getSourceChannelName() +"\",dest_channel_id = \""+sessionDigest.getDestChannelId() +"\",dest_channel_name = \""+sessionDigest.getDestChannelName() +"\",error_type = \""+sessionDigest.getErrorType() +"\" ");
			}
			if( retFlag == 0){
				throw new RuntimeException("insert failed! ");  
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}


	public void update(SessionDigest sessionDigest) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (sessionDigest == null) {
			throw new IllegalArgumentException("sessionDigest is NULL!");
		}

		PreparedStatement statment = null;
		if( sessionDigest.getId() != null && sessionDigest.getId().length() > 20 ){
			throw new IllegalArgumentException("id too long"+sessionDigest.getId());
		}
		if( sessionDigest.getGatewayId() != null && sessionDigest.getGatewayId().length() > 80 ){
			throw new IllegalArgumentException("gatewayId too long"+sessionDigest.getGatewayId());
		}
		if( sessionDigest.getExternalSerialNum() != null && sessionDigest.getExternalSerialNum().length() > 32 ){
			throw new IllegalArgumentException("externalSerialNum too long"+sessionDigest.getExternalSerialNum());
		}
		if( sessionDigest.getStartDate() != null && sessionDigest.getStartDate().length() < 8 ){
			throw new IllegalArgumentException("startDate too short"+sessionDigest.getStartDate());
		}
		if( sessionDigest.getStartDate() != null && sessionDigest.getStartDate().length() > 8 ){
			throw new IllegalArgumentException("startDate too long"+sessionDigest.getStartDate());
		}
		if( sessionDigest.getStartTime() != null && sessionDigest.getStartTime().length() < 6 ){
			throw new IllegalArgumentException("startTime too short"+sessionDigest.getStartTime());
		}
		if( sessionDigest.getStartTime() != null && sessionDigest.getStartTime().length() > 6 ){
			throw new IllegalArgumentException("startTime too long"+sessionDigest.getStartTime());
		}
		if( sessionDigest.getEndDate() != null && sessionDigest.getEndDate().length() < 8 ){
			throw new IllegalArgumentException("endDate too short"+sessionDigest.getEndDate());
		}
		if( sessionDigest.getEndDate() != null && sessionDigest.getEndDate().length() > 8 ){
			throw new IllegalArgumentException("endDate too long"+sessionDigest.getEndDate());
		}
		if( sessionDigest.getEndTime() != null && sessionDigest.getEndTime().length() < 6 ){
			throw new IllegalArgumentException("endTime too short"+sessionDigest.getEndTime());
		}
		if( sessionDigest.getEndTime() != null && sessionDigest.getEndTime().length() > 6 ){
			throw new IllegalArgumentException("endTime too long"+sessionDigest.getEndTime());
		}
		if( sessionDigest.getRequestSendDate() != null && sessionDigest.getRequestSendDate().length() < 8 ){
			throw new IllegalArgumentException("requestSendDate too short"+sessionDigest.getRequestSendDate());
		}
		if( sessionDigest.getRequestSendDate() != null && sessionDigest.getRequestSendDate().length() > 8 ){
			throw new IllegalArgumentException("requestSendDate too long"+sessionDigest.getRequestSendDate());
		}
		if( sessionDigest.getRequestSendTime() != null && sessionDigest.getRequestSendTime().length() < 6 ){
			throw new IllegalArgumentException("requestSendTime too short"+sessionDigest.getRequestSendTime());
		}
		if( sessionDigest.getRequestSendTime() != null && sessionDigest.getRequestSendTime().length() > 6 ){
			throw new IllegalArgumentException("requestSendTime too long"+sessionDigest.getRequestSendTime());
		}
		if( sessionDigest.getResponseArrivedDate() != null && sessionDigest.getResponseArrivedDate().length() < 8 ){
			throw new IllegalArgumentException("responseArrivedDate too short"+sessionDigest.getResponseArrivedDate());
		}
		if( sessionDigest.getResponseArrivedDate() != null && sessionDigest.getResponseArrivedDate().length() > 8 ){
			throw new IllegalArgumentException("responseArrivedDate too long"+sessionDigest.getResponseArrivedDate());
		}
		if( sessionDigest.getResponseArrivedTime() != null && sessionDigest.getResponseArrivedTime().length() < 6 ){
			throw new IllegalArgumentException("responseArrivedTime too short"+sessionDigest.getResponseArrivedTime());
		}
		if( sessionDigest.getResponseArrivedTime() != null && sessionDigest.getResponseArrivedTime().length() > 6 ){
			throw new IllegalArgumentException("responseArrivedTime too long"+sessionDigest.getResponseArrivedTime());
		}
		if( sessionDigest.getState() != null && sessionDigest.getState().length() < 2 ){
			throw new IllegalArgumentException("state too short"+sessionDigest.getState());
		}
		if( sessionDigest.getState() != null && sessionDigest.getState().length() > 2 ){
			throw new IllegalArgumentException("state too long"+sessionDigest.getState());
		}
		if( sessionDigest.getType() != null && sessionDigest.getType().length() < 2 ){
			throw new IllegalArgumentException("type too short"+sessionDigest.getType());
		}
		if( sessionDigest.getType() != null && sessionDigest.getType().length() > 2 ){
			throw new IllegalArgumentException("type too long"+sessionDigest.getType());
		}
		if( sessionDigest.getProcessorId() != null && sessionDigest.getProcessorId().length() > 80 ){
			throw new IllegalArgumentException("processorId too long"+sessionDigest.getProcessorId());
		}
		if( sessionDigest.getSourceChannelId() != null && sessionDigest.getSourceChannelId().length() > 80 ){
			throw new IllegalArgumentException("sourceChannelId too long"+sessionDigest.getSourceChannelId());
		}
		if( sessionDigest.getSourceChannelName() != null && sessionDigest.getSourceChannelName().length() > 80 ){
			throw new IllegalArgumentException("sourceChannelName too long"+sessionDigest.getSourceChannelName());
		}
		if( sessionDigest.getDestChannelId() != null && sessionDigest.getDestChannelId().length() > 80 ){
			throw new IllegalArgumentException("destChannelId too long"+sessionDigest.getDestChannelId());
		}
		if( sessionDigest.getDestChannelName() != null && sessionDigest.getDestChannelName().length() > 80 ){
			throw new IllegalArgumentException("destChannelName too long"+sessionDigest.getDestChannelName());
		}
		if( sessionDigest.getErrorType() != null && sessionDigest.getErrorType().length() < 4 ){
			throw new IllegalArgumentException("errorType too short"+sessionDigest.getErrorType());
		}
		if( sessionDigest.getErrorType() != null && sessionDigest.getErrorType().length() > 4 ){
			throw new IllegalArgumentException("errorType too long"+sessionDigest.getErrorType());
		}
		try {
			String sql =  "UPDATE session_digest SET id=?,gateway_id=?,external_serial_num=?,start_date=?,start_time=?,end_date=?,end_time=?,request_send_date=?,request_send_time=?,response_arrived_date=?,response_arrived_time=?,state=?,type=?,processor_id=?,source_channel_id=?,source_channel_name=?,dest_channel_id=?,dest_channel_name=?,error_type=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, sessionDigest.getId());
			statment.setString(2, sessionDigest.getGatewayId());
			statment.setString(3, sessionDigest.getExternalSerialNum());
			statment.setString(4, sessionDigest.getStartDate());
			statment.setString(5, sessionDigest.getStartTime());
			statment.setString(6, sessionDigest.getEndDate());
			statment.setString(7, sessionDigest.getEndTime());
			statment.setString(8, sessionDigest.getRequestSendDate());
			statment.setString(9, sessionDigest.getRequestSendTime());
			statment.setString(10, sessionDigest.getResponseArrivedDate());
			statment.setString(11, sessionDigest.getResponseArrivedTime());
			statment.setString(12, sessionDigest.getState());
			statment.setString(13, sessionDigest.getType());
			statment.setString(14, sessionDigest.getProcessorId());
			statment.setString(15, sessionDigest.getSourceChannelId());
			statment.setString(16, sessionDigest.getSourceChannelName());
			statment.setString(17, sessionDigest.getDestChannelId());
			statment.setString(18, sessionDigest.getDestChannelName());
			statment.setString(19, sessionDigest.getErrorType());
			statment.setString(20, sessionDigest.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("SessionDigestDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+sessionDigest.getId() +"\",gateway_id = \""+sessionDigest.getGatewayId() +"\",external_serial_num = \""+sessionDigest.getExternalSerialNum() +"\",start_date = \""+sessionDigest.getStartDate() +"\",start_time = \""+sessionDigest.getStartTime() +"\",end_date = \""+sessionDigest.getEndDate() +"\",end_time = \""+sessionDigest.getEndTime() +"\",request_send_date = \""+sessionDigest.getRequestSendDate() +"\",request_send_time = \""+sessionDigest.getRequestSendTime() +"\",response_arrived_date = \""+sessionDigest.getResponseArrivedDate() +"\",response_arrived_time = \""+sessionDigest.getResponseArrivedTime() +"\",state = \""+sessionDigest.getState() +"\",type = \""+sessionDigest.getType() +"\",processor_id = \""+sessionDigest.getProcessorId() +"\",source_channel_id = \""+sessionDigest.getSourceChannelId() +"\",source_channel_name = \""+sessionDigest.getSourceChannelName() +"\",dest_channel_id = \""+sessionDigest.getDestChannelId() +"\",dest_channel_name = \""+sessionDigest.getDestChannelName() +"\",error_type = \""+sessionDigest.getErrorType() +"\" ");
			}
			if( retFlag == 0){
				throw new RuntimeException("update failed! ");  
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}


	public void dynamicUpdate(Map primaryKey, Map updateFields) {
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
			sql.append("UPDATE session_digest SET ");
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
				if("gateway_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("external_serial_num".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("start_date".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("start_time".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("end_date".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("end_time".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("request_send_date".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("request_send_time".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_arrived_date".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_arrived_time".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("state".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("processor_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("source_channel_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("source_channel_name".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("dest_channel_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("dest_channel_name".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("error_type".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("SessionDigestDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			if(retFlag == 0){
				throw new RuntimeException("update failed! ");
			}

			if (!isInTransaction()) {
				conn.commit();
			}
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

	}


	public void deleteByPK(String id) { 
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (id == null) {
			throw new IllegalArgumentException("id is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}

		try {
			String sql = "DELETE FROM session_digest where id=? ";
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
				debug("SessionDigestDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if( retFlag == 0){
				throw new RuntimeException("delete failed! ");  
			}

			if (!isInTransaction()) {
				conn.commit();
			}

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

	}


	public SessionDigest selectByPK (String id)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		if( id != null && id.length() > 20 ){
			throw new IllegalArgumentException("id too long"+id);
		}
		ResultSet resultSet = null;
		SessionDigest returnDTO = null;

		try {
			String sql =  "SELECT * FROM session_digest where id=? ";
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
				debug("SessionDigestDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new SessionDigest();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setExternalSerialNum(resultSet.getString("external_serial_num"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setRequestSendDate(resultSet.getString("request_send_date"));
				returnDTO.setRequestSendTime(resultSet.getString("request_send_time"));
				returnDTO.setResponseArrivedDate(resultSet.getString("response_arrived_date"));
				returnDTO.setResponseArrivedTime(resultSet.getString("response_arrived_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return returnDTO;
	}


	public List findAll ( )  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		SessionDigest returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from session_digest order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SessionDigestDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new SessionDigest();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setExternalSerialNum(resultSet.getString("external_serial_num"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setRequestSendDate(resultSet.getString("request_send_date"));
				returnDTO.setRequestSendTime(resultSet.getString("request_send_time"));
				returnDTO.setResponseArrivedDate(resultSet.getString("response_arrived_date"));
				returnDTO.setResponseArrivedTime(resultSet.getString("response_arrived_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}


	public List findAll (int pageNum, int pageLength)  {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}

		PreparedStatement statment = null;
		ResultSet resultSet = null;
		SessionDigest returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from session_digest ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from session_digest					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,gateway_id,external_serial_num,start_date,start_time,end_date,end_time,request_send_date,request_send_time,response_arrived_date,response_arrived_time,state,type,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type, rownumber() over(order by id) as rn from session_digest ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from session_digest order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,gateway_id,external_serial_num,start_date,start_time,end_date,end_time,request_send_date,request_send_time,response_arrived_date,response_arrived_time,state,type,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type from session_digest ) A where rownumber > "  + startNum ;
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
				debug("SessionDigestDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new SessionDigest();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setExternalSerialNum(resultSet.getString("external_serial_num"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setRequestSendDate(resultSet.getString("request_send_date"));
				returnDTO.setRequestSendTime(resultSet.getString("request_send_time"));
				returnDTO.setResponseArrivedDate(resultSet.getString("response_arrived_date"));
				returnDTO.setResponseArrivedTime(resultSet.getString("response_arrived_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
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
		SessionDigest returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from session_digest where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SessionDigestDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new SessionDigest();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setExternalSerialNum(resultSet.getString("external_serial_num"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setRequestSendDate(resultSet.getString("request_send_date"));
				returnDTO.setRequestSendTime(resultSet.getString("request_send_time"));
				returnDTO.setResponseArrivedDate(resultSet.getString("response_arrived_date"));
				returnDTO.setResponseArrivedTime(resultSet.getString("response_arrived_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
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
		SessionDigest returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from session_digest where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from session_digest where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from session_digest					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,gateway_id,external_serial_num,start_date,start_time,end_date,end_time,request_send_date,request_send_time,response_arrived_date,response_arrived_time,state,type,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type, rownumber() over(order by id) as rn from session_digest where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,gateway_id,external_serial_num,start_date,start_time,end_date,end_time,request_send_date,request_send_time,response_arrived_date,response_arrived_time,state,type,processor_id,source_channel_id,source_channel_name,dest_channel_id,dest_channel_name,error_type from session_digest  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("SessionDigestDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new SessionDigest();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setGatewayId(resultSet.getString("gateway_id"));
				returnDTO.setExternalSerialNum(resultSet.getString("external_serial_num"));
				returnDTO.setStartDate(resultSet.getString("start_date"));
				returnDTO.setStartTime(resultSet.getString("start_time"));
				returnDTO.setEndDate(resultSet.getString("end_date"));
				returnDTO.setEndTime(resultSet.getString("end_time"));
				returnDTO.setRequestSendDate(resultSet.getString("request_send_date"));
				returnDTO.setRequestSendTime(resultSet.getString("request_send_time"));
				returnDTO.setResponseArrivedDate(resultSet.getString("response_arrived_date"));
				returnDTO.setResponseArrivedTime(resultSet.getString("response_arrived_time"));
				returnDTO.setState(resultSet.getString("state"));
				returnDTO.setType(resultSet.getString("type"));
				returnDTO.setProcessorId(resultSet.getString("processor_id"));
				returnDTO.setSourceChannelId(resultSet.getString("source_channel_id"));
				returnDTO.setSourceChannelName(resultSet.getString("source_channel_name"));
				returnDTO.setDestChannelId(resultSet.getString("dest_channel_id"));
				returnDTO.setDestChannelName(resultSet.getString("dest_channel_name"));
				returnDTO.setErrorType(resultSet.getString("error_type"));
				list.add(returnDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
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
			String sql =  "select  count(*) from session_digest";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SessionDigestDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}

			 if(resultSet.next()){
					totalRecords=resultSet.getInt(1);
				}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
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
			String sql =  "select  count(*) from session_digest where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("SessionDigestDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}

			 if(resultSet.next()){
					totalRecords=resultSet.getInt(1);
				}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != statment) {
				try {
					statment.close();
					} catch (SQLException e1) {
					e1.printStackTrace(System.err);
				}
			}
			if (null != resultSet) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return totalRecords;
	}


}
