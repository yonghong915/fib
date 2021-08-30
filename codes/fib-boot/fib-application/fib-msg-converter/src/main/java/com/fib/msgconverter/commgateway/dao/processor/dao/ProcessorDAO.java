package com.fib.msgconverter.commgateway.dao.processor.dao;

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



public class ProcessorDAO extends AbstractDAO {

	public ProcessorDAO() {
		super();
	}

	public ProcessorDAO(boolean inTransaction) {
		super(inTransaction);
	}

	public ProcessorDAO(boolean inTransaction, Connection conn) {
		super(inTransaction, conn);
	}

	public int insert(Processor processor) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (processor == null) {
			throw new IllegalArgumentException("processor is NULL!");
		}
		if( processor.getId() != null && processor.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+processor.getId());
		}
		if( processor.getId() != null && processor.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+processor.getId());
		}
		if( processor.getProcessorType()==null || "".equals(processor.getProcessorType())){
			throw new IllegalArgumentException("processorType is null");
		}
		if( processor.getProcessorType() != null && processor.getProcessorType().length() < 3 ){
			throw new IllegalArgumentException("processorType too short"+processor.getProcessorType());
		}
		if( processor.getProcessorType() != null && processor.getProcessorType().length() > 3 ){
			throw new IllegalArgumentException("processorType too long"+processor.getProcessorType());
		}
		if( processor.getIsLocal() != null && processor.getIsLocal().length() < 1 ){
			throw new IllegalArgumentException("isLocal too short"+processor.getIsLocal());
		}
		if( processor.getIsLocal() != null && processor.getIsLocal().length() > 1 ){
			throw new IllegalArgumentException("isLocal too long"+processor.getIsLocal());
		}
		if( processor.getRouteRuleId() != null && processor.getRouteRuleId().length() < 10 ){
			throw new IllegalArgumentException("routeRuleId too short"+processor.getRouteRuleId());
		}
		if( processor.getRouteRuleId() != null && processor.getRouteRuleId().length() > 10 ){
			throw new IllegalArgumentException("routeRuleId too long"+processor.getRouteRuleId());
		}
		if( processor.getSourceAsync() != null && processor.getSourceAsync().length() < 1 ){
			throw new IllegalArgumentException("sourceAsync too short"+processor.getSourceAsync());
		}
		if( processor.getSourceAsync() != null && processor.getSourceAsync().length() > 1 ){
			throw new IllegalArgumentException("sourceAsync too long"+processor.getSourceAsync());
		}
		if( processor.getDestAsync() != null && processor.getDestAsync().length() < 1 ){
			throw new IllegalArgumentException("destAsync too short"+processor.getDestAsync());
		}
		if( processor.getDestAsync() != null && processor.getDestAsync().length() > 1 ){
			throw new IllegalArgumentException("destAsync too long"+processor.getDestAsync());
		}
		if( processor.getSourceChannelMessageObjectType() != null && processor.getSourceChannelMessageObjectType().length() < 3 ){
			throw new IllegalArgumentException("sourceChannelMessageObjectType too short"+processor.getSourceChannelMessageObjectType());
		}
		if( processor.getSourceChannelMessageObjectType() != null && processor.getSourceChannelMessageObjectType().length() > 3 ){
			throw new IllegalArgumentException("sourceChannelMessageObjectType too long"+processor.getSourceChannelMessageObjectType());
		}
		if( processor.getSourceMapCharset() != null && processor.getSourceMapCharset().length() > 32 ){
			throw new IllegalArgumentException("sourceMapCharset too long"+processor.getSourceMapCharset());
		}
		if( processor.getDestChannelMessageObjectType() != null && processor.getDestChannelMessageObjectType().length() < 3 ){
			throw new IllegalArgumentException("destChannelMessageObjectType too short"+processor.getDestChannelMessageObjectType());
		}
		if( processor.getDestChannelMessageObjectType() != null && processor.getDestChannelMessageObjectType().length() > 3 ){
			throw new IllegalArgumentException("destChannelMessageObjectType too long"+processor.getDestChannelMessageObjectType());
		}
		if( processor.getDestMapCharset() != null && processor.getDestMapCharset().length() > 32 ){
			throw new IllegalArgumentException("destMapCharset too long"+processor.getDestMapCharset());
		}
		if( processor.getRequestMessageTransformerId() != null && processor.getRequestMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("requestMessageTransformerId too short"+processor.getRequestMessageTransformerId());
		}
		if( processor.getRequestMessageTransformerId() != null && processor.getRequestMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("requestMessageTransformerId too long"+processor.getRequestMessageTransformerId());
		}
		if( processor.getRequestMessageHandlerClass() != null && processor.getRequestMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("requestMessageHandlerClass too long"+processor.getRequestMessageHandlerClass());
		}
		if( processor.getResponseMessageTransformerId() != null && processor.getResponseMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("responseMessageTransformerId too short"+processor.getResponseMessageTransformerId());
		}
		if( processor.getResponseMessageTransformerId() != null && processor.getResponseMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("responseMessageTransformerId too long"+processor.getResponseMessageTransformerId());
		}
		if( processor.getResponseMessageHandlerClass() != null && processor.getResponseMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("responseMessageHandlerClass too long"+processor.getResponseMessageHandlerClass());
		}
		if( processor.getErrorMessageTransformerId() != null && processor.getErrorMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("errorMessageTransformerId too short"+processor.getErrorMessageTransformerId());
		}
		if( processor.getErrorMessageTransformerId() != null && processor.getErrorMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("errorMessageTransformerId too long"+processor.getErrorMessageTransformerId());
		}
		if( processor.getErrorMessageHandlerClass() != null && processor.getErrorMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("errorMessageHandlerClass too long"+processor.getErrorMessageHandlerClass());
		}
		if( processor.getInternalErrorMessageTransformerId() != null && processor.getInternalErrorMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("internalErrorMessageTransformerId too short"+processor.getInternalErrorMessageTransformerId());
		}
		if( processor.getInternalErrorMessageTransformerId() != null && processor.getInternalErrorMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("internalErrorMessageTransformerId too long"+processor.getInternalErrorMessageTransformerId());
		}

		PreparedStatement statment = null;
		try {
			String sql =  "insert into processor(id,processor_type,is_local,route_rule_id,source_async,dest_async,source_channel_message_object_type,source_map_charset,dest_channel_message_object_type,dest_map_charset,timeout,request_message_transformer_id,request_message_handler_class,response_message_transformer_id,response_message_handler_class,error_message_transformer_id,error_message_handler_class,internal_error_message_transformer_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			statment =
				conn.prepareStatement(sql);
			statment.setString(1, processor.getId());
			statment.setString(2, processor.getProcessorType());
			statment.setString(3, processor.getIsLocal());
			statment.setString(4, processor.getRouteRuleId());
			statment.setString(5, processor.getSourceAsync());
			statment.setString(6, processor.getDestAsync());
			statment.setString(7, processor.getSourceChannelMessageObjectType());
			statment.setString(8, processor.getSourceMapCharset());
			statment.setString(9, processor.getDestChannelMessageObjectType());
			statment.setString(10, processor.getDestMapCharset());
			statment.setInt(11, processor.getTimeout());
			statment.setString(12, processor.getRequestMessageTransformerId());
			statment.setString(13, processor.getRequestMessageHandlerClass());
			statment.setString(14, processor.getResponseMessageTransformerId());
			statment.setString(15, processor.getResponseMessageHandlerClass());
			statment.setString(16, processor.getErrorMessageTransformerId());
			statment.setString(17, processor.getErrorMessageHandlerClass());
			statment.setString(18, processor.getInternalErrorMessageTransformerId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ProcessorDAO.insert() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+processor.getId() +"\",processor_type = \""+processor.getProcessorType() +"\",is_local = \""+processor.getIsLocal() +"\",route_rule_id = \""+processor.getRouteRuleId() +"\",source_async = \""+processor.getSourceAsync() +"\",dest_async = \""+processor.getDestAsync() +"\",source_channel_message_object_type = \""+processor.getSourceChannelMessageObjectType() +"\",source_map_charset = \""+processor.getSourceMapCharset() +"\",dest_channel_message_object_type = \""+processor.getDestChannelMessageObjectType() +"\",dest_map_charset = \""+processor.getDestMapCharset() +"\",timeout = "+processor.getTimeout()+",request_message_transformer_id = \""+processor.getRequestMessageTransformerId() +"\",request_message_handler_class = \""+processor.getRequestMessageHandlerClass() +"\",response_message_transformer_id = \""+processor.getResponseMessageTransformerId() +"\",response_message_handler_class = \""+processor.getResponseMessageHandlerClass() +"\",error_message_transformer_id = \""+processor.getErrorMessageTransformerId() +"\",error_message_handler_class = \""+processor.getErrorMessageHandlerClass() +"\",internal_error_message_transformer_id = \""+processor.getInternalErrorMessageTransformerId() +"\" ");
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


	public int[] insertBatch(List<Processor> processorList) {
		//获得连接对象
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		//对输入参数的合法性进行效验
		if (processorList == null) {
			throw new IllegalArgumentException("processorList is NULL!");
		}
		for( Processor processor : processorList){
		if( processor.getId() != null && processor.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+processor.getId());
		}
		if( processor.getId() != null && processor.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+processor.getId());
		}
		if( processor.getProcessorType()==null || "".equals(processor.getProcessorType())){
			throw new IllegalArgumentException("processorType is null");
		}
		if( processor.getProcessorType() != null && processor.getProcessorType().length() < 3 ){
			throw new IllegalArgumentException("processorType too short"+processor.getProcessorType());
		}
		if( processor.getProcessorType() != null && processor.getProcessorType().length() > 3 ){
			throw new IllegalArgumentException("processorType too long"+processor.getProcessorType());
		}
		if( processor.getIsLocal() != null && processor.getIsLocal().length() < 1 ){
			throw new IllegalArgumentException("isLocal too short"+processor.getIsLocal());
		}
		if( processor.getIsLocal() != null && processor.getIsLocal().length() > 1 ){
			throw new IllegalArgumentException("isLocal too long"+processor.getIsLocal());
		}
		if( processor.getRouteRuleId() != null && processor.getRouteRuleId().length() < 10 ){
			throw new IllegalArgumentException("routeRuleId too short"+processor.getRouteRuleId());
		}
		if( processor.getRouteRuleId() != null && processor.getRouteRuleId().length() > 10 ){
			throw new IllegalArgumentException("routeRuleId too long"+processor.getRouteRuleId());
		}
		if( processor.getSourceAsync() != null && processor.getSourceAsync().length() < 1 ){
			throw new IllegalArgumentException("sourceAsync too short"+processor.getSourceAsync());
		}
		if( processor.getSourceAsync() != null && processor.getSourceAsync().length() > 1 ){
			throw new IllegalArgumentException("sourceAsync too long"+processor.getSourceAsync());
		}
		if( processor.getDestAsync() != null && processor.getDestAsync().length() < 1 ){
			throw new IllegalArgumentException("destAsync too short"+processor.getDestAsync());
		}
		if( processor.getDestAsync() != null && processor.getDestAsync().length() > 1 ){
			throw new IllegalArgumentException("destAsync too long"+processor.getDestAsync());
		}
		if( processor.getSourceChannelMessageObjectType() != null && processor.getSourceChannelMessageObjectType().length() < 3 ){
			throw new IllegalArgumentException("sourceChannelMessageObjectType too short"+processor.getSourceChannelMessageObjectType());
		}
		if( processor.getSourceChannelMessageObjectType() != null && processor.getSourceChannelMessageObjectType().length() > 3 ){
			throw new IllegalArgumentException("sourceChannelMessageObjectType too long"+processor.getSourceChannelMessageObjectType());
		}
		if( processor.getSourceMapCharset() != null && processor.getSourceMapCharset().length() > 32 ){
			throw new IllegalArgumentException("sourceMapCharset too long"+processor.getSourceMapCharset());
		}
		if( processor.getDestChannelMessageObjectType() != null && processor.getDestChannelMessageObjectType().length() < 3 ){
			throw new IllegalArgumentException("destChannelMessageObjectType too short"+processor.getDestChannelMessageObjectType());
		}
		if( processor.getDestChannelMessageObjectType() != null && processor.getDestChannelMessageObjectType().length() > 3 ){
			throw new IllegalArgumentException("destChannelMessageObjectType too long"+processor.getDestChannelMessageObjectType());
		}
		if( processor.getDestMapCharset() != null && processor.getDestMapCharset().length() > 32 ){
			throw new IllegalArgumentException("destMapCharset too long"+processor.getDestMapCharset());
		}
		if( processor.getRequestMessageTransformerId() != null && processor.getRequestMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("requestMessageTransformerId too short"+processor.getRequestMessageTransformerId());
		}
		if( processor.getRequestMessageTransformerId() != null && processor.getRequestMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("requestMessageTransformerId too long"+processor.getRequestMessageTransformerId());
		}
		if( processor.getRequestMessageHandlerClass() != null && processor.getRequestMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("requestMessageHandlerClass too long"+processor.getRequestMessageHandlerClass());
		}
		if( processor.getResponseMessageTransformerId() != null && processor.getResponseMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("responseMessageTransformerId too short"+processor.getResponseMessageTransformerId());
		}
		if( processor.getResponseMessageTransformerId() != null && processor.getResponseMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("responseMessageTransformerId too long"+processor.getResponseMessageTransformerId());
		}
		if( processor.getResponseMessageHandlerClass() != null && processor.getResponseMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("responseMessageHandlerClass too long"+processor.getResponseMessageHandlerClass());
		}
		if( processor.getErrorMessageTransformerId() != null && processor.getErrorMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("errorMessageTransformerId too short"+processor.getErrorMessageTransformerId());
		}
		if( processor.getErrorMessageTransformerId() != null && processor.getErrorMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("errorMessageTransformerId too long"+processor.getErrorMessageTransformerId());
		}
		if( processor.getErrorMessageHandlerClass() != null && processor.getErrorMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("errorMessageHandlerClass too long"+processor.getErrorMessageHandlerClass());
		}
		if( processor.getInternalErrorMessageTransformerId() != null && processor.getInternalErrorMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("internalErrorMessageTransformerId too short"+processor.getInternalErrorMessageTransformerId());
		}
		if( processor.getInternalErrorMessageTransformerId() != null && processor.getInternalErrorMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("internalErrorMessageTransformerId too long"+processor.getInternalErrorMessageTransformerId());
		}
		}
		PreparedStatement statment = null;
		try {
			String sql =  "insert into processor(id,processor_type,is_local,route_rule_id,source_async,dest_async,source_channel_message_object_type,source_map_charset,dest_channel_message_object_type,dest_map_charset,timeout,request_message_transformer_id,request_message_handler_class,response_message_transformer_id,response_message_handler_class,error_message_transformer_id,error_message_handler_class,internal_error_message_transformer_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			statment = conn.prepareStatement(sql); 
			for( Processor processor : processorList){
			statment.setString(1, processor.getId());
			statment.setString(2, processor.getProcessorType());
			statment.setString(3, processor.getIsLocal());
			statment.setString(4, processor.getRouteRuleId());
			statment.setString(5, processor.getSourceAsync());
			statment.setString(6, processor.getDestAsync());
			statment.setString(7, processor.getSourceChannelMessageObjectType());
			statment.setString(8, processor.getSourceMapCharset());
			statment.setString(9, processor.getDestChannelMessageObjectType());
			statment.setString(10, processor.getDestMapCharset());
			statment.setInt(11, processor.getTimeout());
			statment.setString(12, processor.getRequestMessageTransformerId());
			statment.setString(13, processor.getRequestMessageHandlerClass());
			statment.setString(14, processor.getResponseMessageTransformerId());
			statment.setString(15, processor.getResponseMessageHandlerClass());
			statment.setString(16, processor.getErrorMessageTransformerId());
			statment.setString(17, processor.getErrorMessageHandlerClass());
			statment.setString(18, processor.getInternalErrorMessageTransformerId());

			statment.addBatch();
			}
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int[] retFlag = statment.executeBatch();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ProcessorDAO.insertBatch() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+";" );
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


	public int update(Processor processor) {
		Connection conn = this.getConnection();
		if (null == conn) {
			throw new RuntimeException("Connection is NULL!");
		}
		if (processor == null) {
			throw new IllegalArgumentException("processor is NULL!");
		}

		PreparedStatement statment = null;
		if( processor.getId() != null && processor.getId().length() < 10 ){
			throw new IllegalArgumentException("id too short"+processor.getId());
		}
		if( processor.getId() != null && processor.getId().length() > 10 ){
			throw new IllegalArgumentException("id too long"+processor.getId());
		}
		if( processor.getProcessorType()==null || "".equals(processor.getProcessorType())){
			throw new IllegalArgumentException("processorType is null");
		}
		if( processor.getProcessorType() != null && processor.getProcessorType().length() < 3 ){
			throw new IllegalArgumentException("processorType too short"+processor.getProcessorType());
		}
		if( processor.getProcessorType() != null && processor.getProcessorType().length() > 3 ){
			throw new IllegalArgumentException("processorType too long"+processor.getProcessorType());
		}
		if( processor.getIsLocal() != null && processor.getIsLocal().length() < 1 ){
			throw new IllegalArgumentException("isLocal too short"+processor.getIsLocal());
		}
		if( processor.getIsLocal() != null && processor.getIsLocal().length() > 1 ){
			throw new IllegalArgumentException("isLocal too long"+processor.getIsLocal());
		}
		if( processor.getRouteRuleId() != null && processor.getRouteRuleId().length() < 10 ){
			throw new IllegalArgumentException("routeRuleId too short"+processor.getRouteRuleId());
		}
		if( processor.getRouteRuleId() != null && processor.getRouteRuleId().length() > 10 ){
			throw new IllegalArgumentException("routeRuleId too long"+processor.getRouteRuleId());
		}
		if( processor.getSourceAsync() != null && processor.getSourceAsync().length() < 1 ){
			throw new IllegalArgumentException("sourceAsync too short"+processor.getSourceAsync());
		}
		if( processor.getSourceAsync() != null && processor.getSourceAsync().length() > 1 ){
			throw new IllegalArgumentException("sourceAsync too long"+processor.getSourceAsync());
		}
		if( processor.getDestAsync() != null && processor.getDestAsync().length() < 1 ){
			throw new IllegalArgumentException("destAsync too short"+processor.getDestAsync());
		}
		if( processor.getDestAsync() != null && processor.getDestAsync().length() > 1 ){
			throw new IllegalArgumentException("destAsync too long"+processor.getDestAsync());
		}
		if( processor.getSourceChannelMessageObjectType() != null && processor.getSourceChannelMessageObjectType().length() < 3 ){
			throw new IllegalArgumentException("sourceChannelMessageObjectType too short"+processor.getSourceChannelMessageObjectType());
		}
		if( processor.getSourceChannelMessageObjectType() != null && processor.getSourceChannelMessageObjectType().length() > 3 ){
			throw new IllegalArgumentException("sourceChannelMessageObjectType too long"+processor.getSourceChannelMessageObjectType());
		}
		if( processor.getSourceMapCharset() != null && processor.getSourceMapCharset().length() > 32 ){
			throw new IllegalArgumentException("sourceMapCharset too long"+processor.getSourceMapCharset());
		}
		if( processor.getDestChannelMessageObjectType() != null && processor.getDestChannelMessageObjectType().length() < 3 ){
			throw new IllegalArgumentException("destChannelMessageObjectType too short"+processor.getDestChannelMessageObjectType());
		}
		if( processor.getDestChannelMessageObjectType() != null && processor.getDestChannelMessageObjectType().length() > 3 ){
			throw new IllegalArgumentException("destChannelMessageObjectType too long"+processor.getDestChannelMessageObjectType());
		}
		if( processor.getDestMapCharset() != null && processor.getDestMapCharset().length() > 32 ){
			throw new IllegalArgumentException("destMapCharset too long"+processor.getDestMapCharset());
		}
		if( processor.getRequestMessageTransformerId() != null && processor.getRequestMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("requestMessageTransformerId too short"+processor.getRequestMessageTransformerId());
		}
		if( processor.getRequestMessageTransformerId() != null && processor.getRequestMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("requestMessageTransformerId too long"+processor.getRequestMessageTransformerId());
		}
		if( processor.getRequestMessageHandlerClass() != null && processor.getRequestMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("requestMessageHandlerClass too long"+processor.getRequestMessageHandlerClass());
		}
		if( processor.getResponseMessageTransformerId() != null && processor.getResponseMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("responseMessageTransformerId too short"+processor.getResponseMessageTransformerId());
		}
		if( processor.getResponseMessageTransformerId() != null && processor.getResponseMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("responseMessageTransformerId too long"+processor.getResponseMessageTransformerId());
		}
		if( processor.getResponseMessageHandlerClass() != null && processor.getResponseMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("responseMessageHandlerClass too long"+processor.getResponseMessageHandlerClass());
		}
		if( processor.getErrorMessageTransformerId() != null && processor.getErrorMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("errorMessageTransformerId too short"+processor.getErrorMessageTransformerId());
		}
		if( processor.getErrorMessageTransformerId() != null && processor.getErrorMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("errorMessageTransformerId too long"+processor.getErrorMessageTransformerId());
		}
		if( processor.getErrorMessageHandlerClass() != null && processor.getErrorMessageHandlerClass().length() > 255 ){
			throw new IllegalArgumentException("errorMessageHandlerClass too long"+processor.getErrorMessageHandlerClass());
		}
		if( processor.getInternalErrorMessageTransformerId() != null && processor.getInternalErrorMessageTransformerId().length() < 10 ){
			throw new IllegalArgumentException("internalErrorMessageTransformerId too short"+processor.getInternalErrorMessageTransformerId());
		}
		if( processor.getInternalErrorMessageTransformerId() != null && processor.getInternalErrorMessageTransformerId().length() > 10 ){
			throw new IllegalArgumentException("internalErrorMessageTransformerId too long"+processor.getInternalErrorMessageTransformerId());
		}
		try {
			String sql =  "UPDATE processor SET id=?,processor_type=?,is_local=?,route_rule_id=?,source_async=?,dest_async=?,source_channel_message_object_type=?,source_map_charset=?,dest_channel_message_object_type=?,dest_map_charset=?,timeout=?,request_message_transformer_id=?,request_message_handler_class=?,response_message_transformer_id=?,response_message_handler_class=?,error_message_transformer_id=?,error_message_handler_class=?,internal_error_message_transformer_id=? where id=? ";
			statment =
				conn.prepareStatement(sql);
			statment.setString(1, processor.getId());
			statment.setString(2, processor.getProcessorType());
			statment.setString(3, processor.getIsLocal());
			statment.setString(4, processor.getRouteRuleId());
			statment.setString(5, processor.getSourceAsync());
			statment.setString(6, processor.getDestAsync());
			statment.setString(7, processor.getSourceChannelMessageObjectType());
			statment.setString(8, processor.getSourceMapCharset());
			statment.setString(9, processor.getDestChannelMessageObjectType());
			statment.setString(10, processor.getDestMapCharset());
			statment.setInt(11, processor.getTimeout());
			statment.setString(12, processor.getRequestMessageTransformerId());
			statment.setString(13, processor.getRequestMessageHandlerClass());
			statment.setString(14, processor.getResponseMessageTransformerId());
			statment.setString(15, processor.getResponseMessageHandlerClass());
			statment.setString(16, processor.getErrorMessageTransformerId());
			statment.setString(17, processor.getErrorMessageHandlerClass());
			statment.setString(18, processor.getInternalErrorMessageTransformerId());
			statment.setString(19, processor.getId());

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			int retFlag = statment.executeUpdate();
			if(DAOConfiguration.DEBUG){
				endTime =System.currentTimeMillis();
				debug("ProcessorDAO.update() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+processor.getId() +"\",processor_type = \""+processor.getProcessorType() +"\",is_local = \""+processor.getIsLocal() +"\",route_rule_id = \""+processor.getRouteRuleId() +"\",source_async = \""+processor.getSourceAsync() +"\",dest_async = \""+processor.getDestAsync() +"\",source_channel_message_object_type = \""+processor.getSourceChannelMessageObjectType() +"\",source_map_charset = \""+processor.getSourceMapCharset() +"\",dest_channel_message_object_type = \""+processor.getDestChannelMessageObjectType() +"\",dest_map_charset = \""+processor.getDestMapCharset() +"\",timeout = "+processor.getTimeout()+",request_message_transformer_id = \""+processor.getRequestMessageTransformerId() +"\",request_message_handler_class = \""+processor.getRequestMessageHandlerClass() +"\",response_message_transformer_id = \""+processor.getResponseMessageTransformerId() +"\",response_message_handler_class = \""+processor.getResponseMessageHandlerClass() +"\",error_message_transformer_id = \""+processor.getErrorMessageTransformerId() +"\",error_message_handler_class = \""+processor.getErrorMessageHandlerClass() +"\",internal_error_message_transformer_id = \""+processor.getInternalErrorMessageTransformerId() +"\" ");
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
			sql.append("UPDATE processor SET ");
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
				if("processor_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("is_local".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("route_rule_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("source_async".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("dest_async".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("source_channel_message_object_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("source_map_charset".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("dest_channel_message_object_type".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("dest_map_charset".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("timeout".equalsIgnoreCase(tmpKey)){
					statment.setInt(m++, (Integer)updateFields.get(tmpKey));
				}
				if("request_message_transformer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("request_message_handler_class".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_message_transformer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("response_message_handler_class".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("error_message_transformer_id".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("error_message_handler_class".equalsIgnoreCase(tmpKey)){
					statment.setString(m++, (String)updateFields.get(tmpKey));
				}
				if("internal_error_message_transformer_id".equalsIgnoreCase(tmpKey)){
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
				sbDebug.append("ProcessorDAO.dynamicUpdate() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql.toString()+"; parameters : ");
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
			String sql = "DELETE FROM processor where id=? ";
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
				debug("ProcessorDAO.deleteByPK() spend "+(endTime - startTime)+"ms. retFlag = " + retFlag + " SQL:"+sql+"; parameters : id = \""+id+"\" ");
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


	public Processor selectByPK (String id)  {
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
		Processor returnDTO = null;

		try {
			String sql =  "SELECT * FROM processor where id=? ";
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
				debug("ProcessorDAO.selectByPK() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameters : id = \""+id+"\" ");
			}
			if (resultSet.next()) {
				returnDTO = new Processor();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setProcessorType(resultSet.getString("processor_type"));
				returnDTO.setIsLocal(resultSet.getString("is_local"));
				returnDTO.setRouteRuleId(resultSet.getString("route_rule_id"));
				returnDTO.setSourceAsync(resultSet.getString("source_async"));
				returnDTO.setDestAsync(resultSet.getString("dest_async"));
				returnDTO.setSourceChannelMessageObjectType(resultSet.getString("source_channel_message_object_type"));
				returnDTO.setSourceMapCharset(resultSet.getString("source_map_charset"));
				returnDTO.setDestChannelMessageObjectType(resultSet.getString("dest_channel_message_object_type"));
				returnDTO.setDestMapCharset(resultSet.getString("dest_map_charset"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setRequestMessageTransformerId(resultSet.getString("request_message_transformer_id"));
				returnDTO.setRequestMessageHandlerClass(resultSet.getString("request_message_handler_class"));
				returnDTO.setResponseMessageTransformerId(resultSet.getString("response_message_transformer_id"));
				returnDTO.setResponseMessageHandlerClass(resultSet.getString("response_message_handler_class"));
				returnDTO.setErrorMessageTransformerId(resultSet.getString("error_message_transformer_id"));
				returnDTO.setErrorMessageHandlerClass(resultSet.getString("error_message_handler_class"));
				returnDTO.setInternalErrorMessageTransformerId(resultSet.getString("internal_error_message_transformer_id"));
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
		Processor returnDTO = null;
		List list = new ArrayList();


		try {
			String sql = "select * from processor order by id";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ProcessorDAO.findAll() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Processor();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setProcessorType(resultSet.getString("processor_type"));
				returnDTO.setIsLocal(resultSet.getString("is_local"));
				returnDTO.setRouteRuleId(resultSet.getString("route_rule_id"));
				returnDTO.setSourceAsync(resultSet.getString("source_async"));
				returnDTO.setDestAsync(resultSet.getString("dest_async"));
				returnDTO.setSourceChannelMessageObjectType(resultSet.getString("source_channel_message_object_type"));
				returnDTO.setSourceMapCharset(resultSet.getString("source_map_charset"));
				returnDTO.setDestChannelMessageObjectType(resultSet.getString("dest_channel_message_object_type"));
				returnDTO.setDestMapCharset(resultSet.getString("dest_map_charset"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setRequestMessageTransformerId(resultSet.getString("request_message_transformer_id"));
				returnDTO.setRequestMessageHandlerClass(resultSet.getString("request_message_handler_class"));
				returnDTO.setResponseMessageTransformerId(resultSet.getString("response_message_transformer_id"));
				returnDTO.setResponseMessageHandlerClass(resultSet.getString("response_message_handler_class"));
				returnDTO.setErrorMessageTransformerId(resultSet.getString("error_message_transformer_id"));
				returnDTO.setErrorMessageHandlerClass(resultSet.getString("error_message_handler_class"));
				returnDTO.setInternalErrorMessageTransformerId(resultSet.getString("internal_error_message_transformer_id"));
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
		Processor returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from processor ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from processor					 ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql ="select * from ( select id,processor_type,is_local,route_rule_id,source_async,dest_async,source_channel_message_object_type,source_map_charset,dest_channel_message_object_type,dest_map_charset,timeout,request_message_transformer_id,request_message_handler_class,response_message_transformer_id,response_message_handler_class,error_message_transformer_id,error_message_handler_class,internal_error_message_transformer_id, rownumber() over(order by id) as rn from processor ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from processor order by id limit "+startNum+","+pageLength;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,processor_type,is_local,route_rule_id,source_async,dest_async,source_channel_message_object_type,source_map_charset,dest_channel_message_object_type,dest_map_charset,timeout,request_message_transformer_id,request_message_handler_class,response_message_transformer_id,response_message_handler_class,error_message_transformer_id,error_message_handler_class,internal_error_message_transformer_id from processor ) A where rownumber > "  + startNum ;
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
				debug("ProcessorDAO.findAll()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
			}
			while (resultSet.next()) {
				returnDTO = new Processor();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setProcessorType(resultSet.getString("processor_type"));
				returnDTO.setIsLocal(resultSet.getString("is_local"));
				returnDTO.setRouteRuleId(resultSet.getString("route_rule_id"));
				returnDTO.setSourceAsync(resultSet.getString("source_async"));
				returnDTO.setDestAsync(resultSet.getString("dest_async"));
				returnDTO.setSourceChannelMessageObjectType(resultSet.getString("source_channel_message_object_type"));
				returnDTO.setSourceMapCharset(resultSet.getString("source_map_charset"));
				returnDTO.setDestChannelMessageObjectType(resultSet.getString("dest_channel_message_object_type"));
				returnDTO.setDestMapCharset(resultSet.getString("dest_map_charset"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setRequestMessageTransformerId(resultSet.getString("request_message_transformer_id"));
				returnDTO.setRequestMessageHandlerClass(resultSet.getString("request_message_handler_class"));
				returnDTO.setResponseMessageTransformerId(resultSet.getString("response_message_transformer_id"));
				returnDTO.setResponseMessageHandlerClass(resultSet.getString("response_message_handler_class"));
				returnDTO.setErrorMessageTransformerId(resultSet.getString("error_message_transformer_id"));
				returnDTO.setErrorMessageHandlerClass(resultSet.getString("error_message_handler_class"));
				returnDTO.setInternalErrorMessageTransformerId(resultSet.getString("internal_error_message_transformer_id"));
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
		Processor returnDTO = null;
		List list = new ArrayList();

		try {
			String sql = "select * from processor where " + where +" order by id";
			statment =
				conn.prepareStatement(sql);

			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ProcessorDAO.findByWhere()() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Processor();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setProcessorType(resultSet.getString("processor_type"));
				returnDTO.setIsLocal(resultSet.getString("is_local"));
				returnDTO.setRouteRuleId(resultSet.getString("route_rule_id"));
				returnDTO.setSourceAsync(resultSet.getString("source_async"));
				returnDTO.setDestAsync(resultSet.getString("dest_async"));
				returnDTO.setSourceChannelMessageObjectType(resultSet.getString("source_channel_message_object_type"));
				returnDTO.setSourceMapCharset(resultSet.getString("source_map_charset"));
				returnDTO.setDestChannelMessageObjectType(resultSet.getString("dest_channel_message_object_type"));
				returnDTO.setDestMapCharset(resultSet.getString("dest_map_charset"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setRequestMessageTransformerId(resultSet.getString("request_message_transformer_id"));
				returnDTO.setRequestMessageHandlerClass(resultSet.getString("request_message_handler_class"));
				returnDTO.setResponseMessageTransformerId(resultSet.getString("response_message_transformer_id"));
				returnDTO.setResponseMessageHandlerClass(resultSet.getString("response_message_handler_class"));
				returnDTO.setErrorMessageTransformerId(resultSet.getString("error_message_transformer_id"));
				returnDTO.setErrorMessageHandlerClass(resultSet.getString("error_message_handler_class"));
				returnDTO.setInternalErrorMessageTransformerId(resultSet.getString("internal_error_message_transformer_id"));
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
		Processor returnDTO = null;
		List list = new ArrayList();
		int startNum = (pageNum-1)*pageLength;

		try {
			String sql = null;
			int endNum = startNum + pageLength -1 ;
			if ("mysql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from processor where " + where +" order by id limit "+startNum+","+pageLength;
			}else if ("oracle".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from(select A.*,ROWNUM RN from ( select * from processor where  "+ where + " ) A where rownum <= "+endNum+" ) where RN >="+startNum+" order by id";
			}else if ("mssql".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select Top "+startNum+" * from (select Top "+endNum+" * from processor					 where " + where + " ) t1)t2 order by id";
			}else if ("db2".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select * from ( select id,processor_type,is_local,route_rule_id,source_async,dest_async,source_channel_message_object_type,source_map_charset,dest_channel_message_object_type,dest_map_charset,timeout,request_message_transformer_id,request_message_handler_class,response_message_transformer_id,response_message_handler_class,error_message_transformer_id,error_message_handler_class,internal_error_message_transformer_id, rownumber() over(order by id) as rn from processor where "+ where + " ) as a1 where a1.rn between "+startNum+" and "+endNum;
			}else if ("sqlserver2005".equalsIgnoreCase(DAOConfiguration.getDATABASE_TYPE())){
				sql = "select top " + pageLength + " * from(select row_number() over (order by id) as rownumber, id,processor_type,is_local,route_rule_id,source_async,dest_async,source_channel_message_object_type,source_map_charset,dest_channel_message_object_type,dest_map_charset,timeout,request_message_transformer_id,request_message_handler_class,response_message_transformer_id,response_message_handler_class,error_message_transformer_id,error_message_handler_class,internal_error_message_transformer_id from processor  where " + where +" ) A where rownumber > "  + startNum ;
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
				debug("ProcessorDAO.findByWhere()(pageNum:"+pageNum+",row count "+pageLength+") spend "+(endTime - startTime)+"ms. SQL:"+sql+"; parameter : "+where);
			}
			while (resultSet.next()) {
				returnDTO = new Processor();
				returnDTO.setId(resultSet.getString("id"));
				returnDTO.setProcessorType(resultSet.getString("processor_type"));
				returnDTO.setIsLocal(resultSet.getString("is_local"));
				returnDTO.setRouteRuleId(resultSet.getString("route_rule_id"));
				returnDTO.setSourceAsync(resultSet.getString("source_async"));
				returnDTO.setDestAsync(resultSet.getString("dest_async"));
				returnDTO.setSourceChannelMessageObjectType(resultSet.getString("source_channel_message_object_type"));
				returnDTO.setSourceMapCharset(resultSet.getString("source_map_charset"));
				returnDTO.setDestChannelMessageObjectType(resultSet.getString("dest_channel_message_object_type"));
				returnDTO.setDestMapCharset(resultSet.getString("dest_map_charset"));
				returnDTO.setTimeout(resultSet.getInt("timeout"));
				returnDTO.setRequestMessageTransformerId(resultSet.getString("request_message_transformer_id"));
				returnDTO.setRequestMessageHandlerClass(resultSet.getString("request_message_handler_class"));
				returnDTO.setResponseMessageTransformerId(resultSet.getString("response_message_transformer_id"));
				returnDTO.setResponseMessageHandlerClass(resultSet.getString("response_message_handler_class"));
				returnDTO.setErrorMessageTransformerId(resultSet.getString("error_message_transformer_id"));
				returnDTO.setErrorMessageHandlerClass(resultSet.getString("error_message_handler_class"));
				returnDTO.setInternalErrorMessageTransformerId(resultSet.getString("internal_error_message_transformer_id"));
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
			String sql =  "select  count(*) from processor";
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ProcessorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
			String sql =  "select  count(*) from processor where "+ where;
			statment =
				conn.prepareStatement(sql);
			long startTime=0, endTime=0;
			if(DAOConfiguration.DEBUG){
				startTime = System.currentTimeMillis();
			}
			resultSet = statment.executeQuery();
			if(DAOConfiguration.DEBUG){
				endTime = System.currentTimeMillis();
				debug("ProcessorDAO.getTotalRecords() spend "+(endTime - startTime)+"ms. SQL:"+sql+"; ");
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
