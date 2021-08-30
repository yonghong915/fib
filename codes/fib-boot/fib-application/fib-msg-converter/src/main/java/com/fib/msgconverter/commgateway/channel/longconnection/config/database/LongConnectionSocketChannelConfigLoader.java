package com.fib.msgconverter.commgateway.channel.longconnection.config.database;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.base.ConnectionConfig;
import com.fib.msgconverter.commgateway.channel.config.recognizer.RecognizerConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.CodeRecognizerConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.HeartbeatConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.LoginConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.LongConnectionSocketChannelConfig;
import com.fib.msgconverter.commgateway.channel.longconnection.config.MessageSymbol;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractCompositeMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.channel.nio.config.ReaderConfig;
import com.fib.msgconverter.commgateway.channel.nio.config.WriterConfig;
import com.fib.msgconverter.commgateway.dao.connectormessagetypecoderelation.dao.ConnectorMessageTypeCodeRelation;
import com.fib.msgconverter.commgateway.dao.connectormessagetypecoderelation.dao.ConnectorMessageTypeCodeRelationDAO;
import com.fib.msgconverter.commgateway.dao.heartbeat.dao.Heartbeat;
import com.fib.msgconverter.commgateway.dao.heartbeat.dao.HeartbeatDAO;
import com.fib.msgconverter.commgateway.dao.login.dao.Login;
import com.fib.msgconverter.commgateway.dao.login.dao.LoginDAO;
import com.fib.msgconverter.commgateway.dao.loginparameterrelation.dao.LoginParameterRelation;
import com.fib.msgconverter.commgateway.dao.loginparameterrelation.dao.LoginParameterRelationDAO;
import com.fib.msgconverter.commgateway.dao.longconnection.dao.LongConnection;
import com.fib.msgconverter.commgateway.dao.longconnection.dao.LongConnectionDAO;
import com.fib.msgconverter.commgateway.dao.longconnectorconnectionrelation.dao.LongConnectorConnectionRelation;
import com.fib.msgconverter.commgateway.dao.longconnectorconnectionrelation.dao.LongConnectorConnectionRelationDAO;
import com.fib.msgconverter.commgateway.dao.longconnectorheartbeatrelation.dao.LongConnectorHeartbeatRelation;
import com.fib.msgconverter.commgateway.dao.longconnectorheartbeatrelation.dao.LongConnectorHeartbeatRelationDAO;
import com.fib.msgconverter.commgateway.dao.longsocketconnector.dao.LongSocketConnector;
import com.fib.msgconverter.commgateway.dao.longsocketconnector.dao.LongSocketConnectorDAO;
import com.fib.msgconverter.commgateway.dao.messagesymbol.dao.MessageSymbolDAO;
import com.fib.msgconverter.commgateway.dao.messagetypecode.dao.MessageTypeCode;
import com.fib.msgconverter.commgateway.dao.messagetypecode.dao.MessageTypeCodeDAO;
import com.fib.msgconverter.commgateway.dao.parameter.dao.Parameter;
import com.fib.msgconverter.commgateway.dao.parameter.dao.ParameterDAO;
import com.fib.msgconverter.commgateway.util.database.DBConfigUtil;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

public class LongConnectionSocketChannelConfigLoader {
	public static final String TRUE = "0";
	public static final String FALSE = "1";

	private Map msgSymbolCache = new HashMap();

	public LongConnectionSocketChannelConfig loadConfig(String connectorId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			// ChannelDAO channelDao = new ChannelDAO();
			// channelDao.setConnection(conn);
			//
			// com.giantstone.commgateway.dao.channel.dao.Channel channelDto =
			// channelDao
			// .selectByPK(channelId);
			LongSocketConnectorDAO longSocketConnectorDao = new LongSocketConnectorDAO();
			longSocketConnectorDao.setConnection(conn);
			LongSocketConnector longSocketConnector = longSocketConnectorDao
					.selectByPK(connectorId);

			LongConnectionSocketChannelConfig config = new LongConnectionSocketChannelConfig();
			// code recognizer
			if (null != longSocketConnector.getCodeRecognizerId()) {
				config.setCodeRecognizerConfig(transformCodeRecognizerConfig(
						longSocketConnector, conn));
				config.setCodeRecognizer(createRecognizer(DBConfigUtil
						.transformRecognizerConfig(longSocketConnector
								.getCodeRecognizerId(), conn)));
			}
			if (null != longSocketConnector
					.getRequestSerialNumberRecognizerId()) {
				config.setRequestSerialNumberRecognizerId(longSocketConnector
						.getRequestSerialNumberRecognizerId()
						+ "");
				config
						.setRequestSerialNumberRecognizer(createRecognizer(DBConfigUtil
								.transformRecognizerConfig(longSocketConnector
										.getRequestSerialNumberRecognizerId(),
										conn)));
			}
			if (null != longSocketConnector
					.getResponseSerialNumberRecognizerId()) {
				config.setResponseSerialNumberRecognizerId(longSocketConnector
						.getResponseSerialNumberRecognizerId()
						+ "");
				config
						.setResponseSerialNumberRecognizer(createRecognizer(DBConfigUtil
								.transformRecognizerConfig(longSocketConnector
										.getResponseSerialNumberRecognizerId(),
										conn)));
			}
			if (null != longSocketConnector.getLoginId()) {
				config.setLoginConfig(transformLoginConfig(longSocketConnector
						.getLoginId(), conn));
			}
			if (null != longSocketConnector.getReaderId()) {
				config.setReaderConfig((ReaderConfig) DBConfigUtil
						.transformReaderOrWriterConfig(longSocketConnector
								.getReaderId(), true, conn));
			}
			if (null != longSocketConnector.getWriterId()) {
				config.setWriterConfig((WriterConfig) DBConfigUtil
						.transformReaderOrWriterConfig(longSocketConnector
								.getWriterId(), false, conn));
			}

			config.setConnectionConfigs(transformConnections(
					longSocketConnector.getId(), conn));

			config.setHeartbeatConfigs(transformHeartbeats(longSocketConnector
					.getId(), conn));

			conn.commit();
			return config;
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

	private List<HeartbeatConfig> transformHeartbeats(String connectorId,
			Connection conn) {
		LongConnectorHeartbeatRelationDAO lchrDao = new LongConnectorHeartbeatRelationDAO();
		lchrDao.setConnection(conn);

		List<HeartbeatConfig> heartbeatList = null;
		List<LongConnectorHeartbeatRelation> lchrList = lchrDao
				.getAllHeartbeat4LongConnector(connectorId);
		HeartbeatDAO heartbeatDao = new HeartbeatDAO();
		heartbeatDao.setConnection(conn);
		if (null != lchrList && 0 < lchrList.size()) {
			heartbeatList = new ArrayList<HeartbeatConfig>();
			for (int i = 0; i < lchrList.size(); i++) {
				heartbeatList.add(transformHeartbeat(heartbeatDao
						.selectByPK(lchrList.get(i).getHeartbeatId()), conn));
			}
		}

		return heartbeatList;
	}

	private HeartbeatConfig transformHeartbeat(Heartbeat heartbeatDto,
			Connection conn) {
		HeartbeatConfig heartbeatConfig = new HeartbeatConfig();
		if (null != heartbeatDto.getConnectionId()) {
			LongConnectionDAO longConnDao = new LongConnectionDAO();
			longConnDao.setConnection(conn);
			heartbeatConfig.setConnectionId(longConnDao.selectByPK(
					heartbeatDto.getConnectionId()).getConnectionId());
		}
		if (null != heartbeatDto.getResponseConnectionId()) {
			LongConnectionDAO longConnDao = new LongConnectionDAO();
			longConnDao.setConnection(conn);
			heartbeatConfig.setResponseConnectionId(longConnDao.selectByPK(
					heartbeatDto.getResponseConnectionId()).getConnectionId());
		}
		if (null != heartbeatDto.getDirection()) {
			heartbeatConfig.setDirection(Integer.parseInt(heartbeatDto
					.getDirection()));
		}
		heartbeatConfig.setInterval(heartbeatDto.getSendInterval());
		if (null != heartbeatDto.getMessageSymbolId()) {
			heartbeatConfig.setMessageSymbol(transformMessageSymbol(
					heartbeatDto.getMessageSymbolId(), conn));
			heartbeatConfig.setMessageSymbolId(heartbeatConfig
					.getMessageSymbol().getId());
		}
		if (null != heartbeatDto.getResponseMessageSymbolId()) {
			heartbeatConfig.setResponseMessageSymbol(transformMessageSymbol(
					heartbeatDto.getResponseMessageSymbolId(), conn));
			heartbeatConfig.setResponseMessageSymbolId(heartbeatConfig
					.getResponseMessageSymbol().getId());
		}
		if (TRUE.equals(heartbeatDto.getResponseTurnBack())) {
			heartbeatConfig.setResponseTurnBack(true);
		} else if (FALSE.equals(heartbeatDto.getResponseTurnBack())) {
			heartbeatConfig.setResponseTurnBack(false);
		}

		return heartbeatConfig;
	}

	private MessageSymbol transformMessageSymbol(String msgSymbolId,
			Connection conn) {
		if (msgSymbolCache.containsKey(msgSymbolId)) {
			return (MessageSymbol) msgSymbolCache.get(msgSymbolId);
		}

		MessageSymbol msgSymbol = new MessageSymbol();
		MessageSymbolDAO msgSymbolDao = new MessageSymbolDAO();
		msgSymbolDao.setConnection(conn);

		com.fib.msgconverter.commgateway.dao.messagesymbol.dao.MessageSymbol msgSymbolDto = msgSymbolDao
				.selectByPK(msgSymbolId);
		msgSymbol.setId(msgSymbolDto.getMessageSymbolId());
		if (null != msgSymbolDto.getDataType()) {
			msgSymbol.setDataType(Integer.parseInt(msgSymbolDto.getDataType()));
		}
		if (null != msgSymbolDto.getSymbolType()) {
			msgSymbol.setType(Integer.parseInt(msgSymbolDto.getSymbolType()));
		}
		if (null != msgSymbolDto.getValueOrScript()) {
			try {
				msgSymbol.setValue(new String(msgSymbolDto.getValueOrScript(),
						"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		msgSymbolCache.put(msgSymbolId, msgSymbol);

		return msgSymbol;
	}

	private Map<String, ConnectionConfig> transformConnections(
			String connectorId, Connection conn) {
		LongConnectorConnectionRelationDAO lccrDao = new LongConnectorConnectionRelationDAO();
		lccrDao.setConnection(conn);

		Map<String, ConnectionConfig> connectionMap = null;
		List<LongConnectorConnectionRelation> lccrList = lccrDao
				.getAllConnection4LongConnector(connectorId);
		LongConnectionDAO longConnectionDao = new LongConnectionDAO();
		longConnectionDao.setConnection(conn);
		if (null != lccrList && 0 < lccrList.size()) {
			connectionMap = new HashMap<String, ConnectionConfig>();
			for (int i = 0; i < lccrList.size(); i++) {
				ConnectionConfig connectionConfig = transformConnectionConfig(longConnectionDao
						.selectByPK(lccrList.get(i).getConnectionId()));
				connectionMap.put(connectionConfig.getId(), connectionConfig);
			}
		}
		return connectionMap;
	}

	private ConnectionConfig transformConnectionConfig(
			LongConnection longConnectionDto) {
		ConnectionConfig connectionConfig = new ConnectionConfig();
		if (null != longConnectionDto.getDirection()) {
			connectionConfig.setDirection(Integer.parseInt(longConnectionDto
					.getDirection()));
		}
		connectionConfig.setId(longConnectionDto.getConnectionId());
		connectionConfig.setLocalPort(longConnectionDto.getLocalPort());
		connectionConfig.setLocalServerAddress(longConnectionDto
				.getLocalServerAddress());
		connectionConfig.setPort(longConnectionDto.getPort());
		connectionConfig.setServerAddress(longConnectionDto.getServerAddress());

		return connectionConfig;
	}

	private CodeRecognizerConfig transformCodeRecognizerConfig(
			LongSocketConnector longSocketConnector, Connection conn) {
		CodeRecognizerConfig codeRecognizerConfig = new CodeRecognizerConfig();
		codeRecognizerConfig.setRecognizerId(longSocketConnector
				.getCodeRecognizerId()
				+ "");

		ConnectorMessageTypeCodeRelationDAO cmtcrDao = new ConnectorMessageTypeCodeRelationDAO();
		cmtcrDao.setConnection(conn);

		List<ConnectorMessageTypeCodeRelation> cmtcrList = cmtcrDao
				.getAllMsgTypCode4Connector(longSocketConnector.getId());
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

	private LoginConfig transformLoginConfig(String loginId, Connection conn) {
		LoginDAO loginDao = new LoginDAO();
		loginDao.setConnection(conn);

		Login loginDto = loginDao.selectByPK(loginId);
		LoginConfig loginConfig = new LoginConfig();
		loginConfig.setClassName(loginDto.getClassName());

		LoginParameterRelationDAO lprDao = new LoginParameterRelationDAO();
		lprDao.setConnection(conn);

		List<LoginParameterRelation> lprList = lprDao
				.getAllParameter4Login(loginDto.getId());
		ParameterDAO paramDao = new ParameterDAO();
		paramDao.setConnection(conn);
		Parameter param = null;
		for (int i = 0; i < lprList.size(); i++) {
			param = paramDao.selectByPK(lprList.get(i).getParameterId());
			loginConfig.setParameter(param.getParameterName(), param
					.getParameterValue());
		}
		return loginConfig;
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
