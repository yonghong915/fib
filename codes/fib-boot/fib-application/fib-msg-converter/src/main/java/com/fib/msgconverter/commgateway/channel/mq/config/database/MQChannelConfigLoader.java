package com.fib.msgconverter.commgateway.channel.mq.config.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.CodeRecognizerConfig;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.mq.config.MQChannelConfig;
import com.fib.msgconverter.commgateway.channel.mq.config.QueueConfig;
import com.fib.msgconverter.commgateway.dao.connectormessagetypecoderelation.dao.ConnectorMessageTypeCodeRelation;
import com.fib.msgconverter.commgateway.dao.connectormessagetypecoderelation.dao.ConnectorMessageTypeCodeRelationDAO;
import com.fib.msgconverter.commgateway.dao.filter.dao.Filter;
import com.fib.msgconverter.commgateway.dao.filter.dao.FilterDAO;
import com.fib.msgconverter.commgateway.dao.messagetypecode.dao.MessageTypeCode;
import com.fib.msgconverter.commgateway.dao.messagetypecode.dao.MessageTypeCodeDAO;
import com.fib.msgconverter.commgateway.dao.mqconnector.dao.MqConnector;
import com.fib.msgconverter.commgateway.dao.mqconnector.dao.MqConnectorDAO;
import com.fib.msgconverter.commgateway.dao.mqconnectorfilterrelation.dao.MqConnectorFilterRelation;
import com.fib.msgconverter.commgateway.dao.mqconnectorfilterrelation.dao.MqConnectorFilterRelationDAO;
import com.fib.msgconverter.commgateway.dao.mqconnectorqueuerelation.dao.MqConnectorQueueRelation;
import com.fib.msgconverter.commgateway.dao.mqconnectorqueuerelation.dao.MqConnectorQueueRelationDAO;
import com.fib.msgconverter.commgateway.dao.queue.dao.Queue;
import com.fib.msgconverter.commgateway.dao.queue.dao.QueueDAO;
import com.fib.msgconverter.commgateway.util.database.DBConfigUtil;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.ExceptionUtil;

public class MQChannelConfigLoader {
	public MQChannelConfig loadConfig(String connectorId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			MqConnectorDAO mqConnectorDao = new MqConnectorDAO();
			mqConnectorDao.setConnection(conn);
			MqConnector mqConnector = mqConnectorDao.selectByPK(connectorId);
			MQChannelConfig mqChannelConfig = new MQChannelConfig();
			mqChannelConfig.setCcsid(mqConnector.getCcsid());
			mqChannelConfig.setTimeout(mqConnector.getTimeout());
			if (null != mqConnector.getMqType()) {
				mqChannelConfig.setType(Integer.parseInt(mqConnector
						.getMqType()));
			}
			if (null != mqConnector.getCodeRecognizerId()) {
				mqChannelConfig
						.setCodeRecognizerConfig(transformCodeRecognizerConfig(
								mqConnector, conn));
				mqChannelConfig.setCodeRecognizer(createRecognizer(DBConfigUtil
						.transformRecognizerConfig(mqConnector
								.getCodeRecognizerId(), conn)));
			}
			mqChannelConfig.setQuqueConfig(transformQueueConfigs(connectorId,
					conn));
			// TODO
//			mqChannelConfig
//					.setReaderFilterList(transformReaderOrWriterFilterList(
//							connectorId, true, conn));
//			mqChannelConfig
//					.setWriterFilterList(transformReaderOrWriterFilterList(
//							connectorId, false, conn));
			conn.commit();
			return mqChannelConfig;
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private List transformReaderOrWriterFilterList(String connectorId,
			boolean isReaderFilter, Connection conn) {
		MqConnectorFilterRelationDAO mcfrDao = new MqConnectorFilterRelationDAO();
		mcfrDao.setConnection(conn);

		List<MqConnectorFilterRelation> mcfrList = null;
		if (isReaderFilter) {
			mcfrList = mcfrDao.getReaderFilter4MQConnector(connectorId);
		} else {
			mcfrList = mcfrDao.getWriterFilter4MQConnector(connectorId);
		}

		List filterList = null;
		if (null != mcfrList && 0 < mcfrList.size()) {
			filterList = new ArrayList();
			FilterDAO filterDao = new FilterDAO();
			filterDao.setConnection(conn);
			for (int i = 0; i < mcfrList.size(); i++) {
				Filter filterDto = filterDao.selectByPK(mcfrList.get(i)
						.getFilterId());
				filterList.add(ClassUtil.createClassInstance(filterDto
						.getClassName()));
			}
		}
		return filterList;
	}

	private Map transformQueueConfigs(String connectorId, Connection conn) {
		MqConnectorQueueRelationDAO mcqrDao = new MqConnectorQueueRelationDAO();
		mcqrDao.setConnection(conn);

		List<MqConnectorQueueRelation> mcqrList = mcqrDao
				.getAllQueue4Connector(connectorId);
		Map queueMap = null;
		if (null != mcqrList && 0 < mcqrList.size()) {
			queueMap = new HashMap();
			QueueDAO queueDao = new QueueDAO();
			queueDao.setConnection(conn);
			for (int i = 0; i < mcqrList.size(); i++) {
				QueueConfig queueConfig = transformQueueConfig(queueDao
						.selectByPK(mcqrList.get(i).getQueueId()), conn);
				queueMap.put(queueConfig.getName(), queueConfig);
			}
		}
		return queueMap;
	}

	private QueueConfig transformQueueConfig(Queue queueDto, Connection conn) {
		QueueConfig queueConfig = new QueueConfig();
		queueConfig.setChannel(queueDto.getMqChannelName());
		queueConfig.setName(queueDto.getName());
		queueConfig.setPort(queueDto.getPort());
		queueConfig.setQueueManager(queueDto.getQueueManager());
		queueConfig.setServerAddress(queueDto.getServerAddress());
		if (null != queueDto.getDirection()) {
			queueConfig.setType(Integer.parseInt(queueDto.getDirection()));
		}

		if (null != queueDto.getMessageKeyRecognizerId()) {
			queueConfig.setMessageKeyRecognizer(createRecognizer(DBConfigUtil
					.transformRecognizerConfig(queueDto
							.getMessageKeyRecognizerId(), conn)));
		}

		return queueConfig;
	}

	private CodeRecognizerConfig transformCodeRecognizerConfig(
			MqConnector mqConnector, Connection conn) {
		CodeRecognizerConfig codeRecognizerConfig = new CodeRecognizerConfig();
		codeRecognizerConfig.setRecognizerId(mqConnector.getCodeRecognizerId()
				+ "");

		ConnectorMessageTypeCodeRelationDAO cmtcrDao = new ConnectorMessageTypeCodeRelationDAO();
		cmtcrDao.setConnection(conn);

		List<ConnectorMessageTypeCodeRelation> cmtcrList = cmtcrDao
				.getAllMsgTypCode4Connector(mqConnector.getId());
		MessageTypeCodeDAO mtcDao = new MessageTypeCodeDAO();
		mtcDao.setConnection(conn);
		MessageTypeCode mtc = null;
		for (int i = 0; i < cmtcrList.size(); i++) {
			mtc = mtcDao.selectByPK(cmtcrList.get(i).getMessageTypeCodeId());
			if ("0".equals(mtc.getMessageType())) {
				codeRecognizerConfig.getRequestCodeSet().add(mtc.getCode());
			} else if ("1".equals(mtc.getMessageType())) {
				codeRecognizerConfig.getResponseCodeSet().add(mtc.getCode());
			}
		}
		return codeRecognizerConfig;
	}

	private AbstractMessageRecognizer createRecognizer(
			RecognizerConfig recognizerConfig) {
		AbstractMessageRecognizer recognizer = null;
		try {
			Class clazz = Class.forName(recognizerConfig.getClassName());
			recognizer = (AbstractMessageRecognizer) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			ExceptionUtil.throwActualException(e);
		} catch (InstantiationException e) {
			ExceptionUtil.throwActualException(e);
		} catch (IllegalAccessException e) {
			ExceptionUtil.throwActualException(e);
		}
		recognizer.setParameters(recognizerConfig.getParameters());

		if (recognizer instanceof AbstractCompositeMessageRecognizer) {
			AbstractCompositeMessageRecognizer compositeRecognizer = (AbstractCompositeMessageRecognizer) recognizer;
			RecognizerConfig componentConfig = null;
			Iterator it = recognizerConfig.getComponentList().iterator();
			while (it.hasNext()) {
				componentConfig = (RecognizerConfig) it.next();
				compositeRecognizer.add(createRecognizer(componentConfig));
			}
		}

		return recognizer;
	}
}
