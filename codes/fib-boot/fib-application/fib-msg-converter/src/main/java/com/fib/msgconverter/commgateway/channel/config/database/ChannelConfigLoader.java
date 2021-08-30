package com.fib.msgconverter.commgateway.channel.config.database;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.MessageTypeRecognizerConfig;
import com.fib.msgconverter.commgateway.channel.config.ReturnCodeRecognizerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.ErrorMappingConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageHandlerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageTransformerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.channel.config.route.RouteRule;
import com.fib.msgconverter.commgateway.dao.channel.dao.Channel;
import com.fib.msgconverter.commgateway.dao.channel.dao.ChannelDAO;
import com.fib.msgconverter.commgateway.dao.channelreturncoderelation.dao.ChannelReturnCodeRelation;
import com.fib.msgconverter.commgateway.dao.channelreturncoderelation.dao.ChannelReturnCodeRelationDAO;
import com.fib.msgconverter.commgateway.dao.determination.dao.Determination;
import com.fib.msgconverter.commgateway.dao.determination.dao.DeterminationDAO;
import com.fib.msgconverter.commgateway.dao.determinationcase.dao.DeterminationCase;
import com.fib.msgconverter.commgateway.dao.determinationcase.dao.DeterminationCaseDAO;
import com.fib.msgconverter.commgateway.dao.determinationcaserelation.dao.DeterminationCaseRelation;
import com.fib.msgconverter.commgateway.dao.determinationcaserelation.dao.DeterminationCaseRelationDAO;
import com.fib.msgconverter.commgateway.dao.mapping.dao.MappingDAO;
import com.fib.msgconverter.commgateway.dao.messagecodeprocessormapping.dao.MessageCodeProcessorMapping;
import com.fib.msgconverter.commgateway.dao.messagecodeprocessormapping.dao.MessageCodeProcessorMappingDAO;
import com.fib.msgconverter.commgateway.dao.processor.dao.ProcessorDAO;
import com.fib.msgconverter.commgateway.dao.returncode.dao.ReturnCode;
import com.fib.msgconverter.commgateway.dao.returncode.dao.ReturnCodeDAO;
import com.fib.msgconverter.commgateway.dao.routerule.dao.RouteRuleDAO;
import com.fib.msgconverter.commgateway.dao.transformer.dao.Transformer;
import com.fib.msgconverter.commgateway.dao.transformer.dao.TransformerDAO;
import com.fib.msgconverter.commgateway.util.database.DBConfigUtil;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.message.dao.messagebean.dao.message.dao.MessageDAO;

public class ChannelConfigLoader {
	public static final String TRUE = "0";
	public static final String FALSE = "1";

	public ChannelConfig loadConfig(String channelId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			ChannelDAO channelDao = new ChannelDAO();
			channelDao.setConnection(conn);
			Channel channelDto = channelDao.selectByPK(channelId);

			return transformChannelConfig(channelDto, conn);
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

	private ChannelConfig transformChannelConfig(Channel channelDto,
			Connection conn) {
		ChannelConfig channelConfig = new ChannelConfig();
		channelConfig.setClassName(channelDto.getClassName());
		channelConfig.setConnectorId(channelDto.getConnectorId());
		channelConfig.setDescription(channelDto.getDescription());
		channelConfig.setEventHandlerClazz(channelDto
				.getEventHandlerClassName());
		channelConfig.setMessageBeanGroupId(channelDto.getMbMappingGroup());
		if (null != channelDto.getMode()) {
			channelConfig.setMode(Integer.parseInt(channelDto.getMode()));
		}
		if (null != channelDto.getMessageCodeRecognizerId()) {
			MessageTypeRecognizerConfig msgTypeRecognizerConfig = new MessageTypeRecognizerConfig();
			msgTypeRecognizerConfig.setRecognizerConfig(DBConfigUtil
					.transformRecognizerConfig(channelDto
							.getMessageCodeRecognizerId(), conn));
			channelConfig
					.setMessageTypeRecognizerConfig(msgTypeRecognizerConfig);

			MessageCodeProcessorMappingDAO mcpmDao = new MessageCodeProcessorMappingDAO();
			mcpmDao.setConnection(conn);
			List<MessageCodeProcessorMapping> mcpmList = mcpmDao
					.getAllMsgTypProcMapping4Channel(channelDto.getId());
			for (int i = 0; i < mcpmList.size(); i++) {
				MessageCodeProcessorMapping mcpm = mcpmList.get(i);
				msgTypeRecognizerConfig.getMessageTypeProcessorMap().put(
						mcpm.getMessageCode(), mcpm.getProcessorId());
				Processor processorConfig = transformProcessorConfig(
						channelConfig, mcpm.getProcessorId(), conn);
				channelConfig.getProcessorTable().put(processorConfig.getId(),
						processorConfig);
			}
		}
		if (null != channelDto.getReturnCodeRecognizerId()) {
			ReturnCodeRecognizerConfig rcrConfig = new ReturnCodeRecognizerConfig();
			rcrConfig.setRecognizerConfig(DBConfigUtil
					.transformRecognizerConfig(channelDto
							.getReturnCodeRecognizerId(), conn));
			ChannelReturnCodeRelationDAO crcrDao = new ChannelReturnCodeRelationDAO();
			crcrDao.setConnection(conn);

			List<ChannelReturnCodeRelation> crcrList = crcrDao
					.getAllReturnCode4Channel(channelDto.getId());
			ReturnCodeDAO returnCodeDao = new ReturnCodeDAO();
			returnCodeDao.setConnection(conn);
			for (int i = 0; i < crcrList.size(); i++) {
				ReturnCode returnCode = returnCodeDao.selectByPK(crcrList
						.get(i).getReturnCodeId());
				if (TRUE.equals(returnCode.getSuccess())) {
					rcrConfig.getSuccessCodeSet().add(
							returnCode.getReturnCode());
				} else {
					if (null == rcrConfig.getErrorCodeSet()) {
						rcrConfig.setErrorCodeSet(new HashSet<>());
					}
					rcrConfig.getErrorCodeSet().add(returnCode.getReturnCode());
				}
			}
			channelConfig.setReturnCodeRecognizerConfig(rcrConfig);
		}

		if (null != channelDto.getChannelType()
				&& null == channelDto.getClassName()) {
			channelConfig.setType(channelDto.getChannelType());
		}
		if (null != channelDto.getDefaultProcessorId()) {
			channelConfig.setDefaultProcessor(transformProcessorConfig(
					channelConfig, channelDto.getDefaultProcessorId(), conn));
		}
		return channelConfig;
	}

	private Processor transformProcessorConfig(ChannelConfig channelConfig,
			String processorId, Connection conn) {
		if (channelConfig.getProcessorTable().containsKey("" + processorId)) {
			return (Processor) channelConfig.getProcessorTable().get(
					"" + processorId);
		}
		ProcessorDAO processorDao = new ProcessorDAO();
		processorDao.setConnection(conn);
		com.fib.msgconverter.commgateway.dao.processor.dao.Processor processorDto = processorDao
				.selectByPK(processorId);

		Processor processorConfig = new Processor();
		processorConfig.setId(processorDto.getId() + "");
		if (TRUE.equals(processorDto.getDestAsync())) {
			processorConfig.setDestAsync(true);
		} else {
			processorConfig.setDestAsync(false);
		}
		if (null != processorDto.getDestChannelMessageObjectType()) {
			processorConfig.setDestChannelMessageObjectType(Integer
					.parseInt(processorDto.getDestChannelMessageObjectType()));
		}
		processorConfig.setDestMapCharset(processorDto.getDestMapCharset());
		if (null != processorDto.getInternalErrorMessageTransformerId()) {
			processorConfig.setErrorMappingConfig(transformErrorMappingConfig(
					processorDto.getInternalErrorMessageTransformerId(), conn));
		}
		if (null != processorDto.getRequestMessageTransformerId()) {
			processorConfig
					.setRequestMessageConfig(transformMsgTransformerConfig(
							processorDto.getRequestMessageTransformerId(), conn));
		}
		if (null != processorDto.getRequestMessageHandlerClass()) {
			MessageHandlerConfig reqMsgHandlerConfig = new MessageHandlerConfig();
			reqMsgHandlerConfig.setClassName(processorDto
					.getRequestMessageHandlerClass());
			processorConfig.setRequestMessageHandlerConfig(reqMsgHandlerConfig);
		}
		if (null != processorDto.getResponseMessageTransformerId()) {
			processorConfig
					.setResponseMessageConfig(transformMsgTransformerConfig(
							processorDto.getResponseMessageTransformerId(),
							conn));
		}
		if (null != processorDto.getResponseMessageHandlerClass()) {
			MessageHandlerConfig resMsgHandlerConfig = new MessageHandlerConfig();
			resMsgHandlerConfig.setClassName(processorDto
					.getResponseMessageHandlerClass());
			processorConfig
					.setResponseMessageHandlerConfig(resMsgHandlerConfig);
		}
		if (null != processorDto.getErrorMessageTransformerId()) {
			processorConfig
					.setErrorMessageConfig(transformMsgTransformerConfig(
							processorDto.getErrorMessageTransformerId(), conn));
		}
		if (null != processorDto.getErrorMessageHandlerClass()) {
			MessageHandlerConfig errorHandlerConfig = new MessageHandlerConfig();
			errorHandlerConfig.setClassName(processorDto
					.getErrorMessageHandlerClass());
			processorConfig.setErrorMessageHandlerConfig(errorHandlerConfig);
		}
		processorConfig.setId(processorDto.getId() + "");
		if (TRUE.equals(processorDto.getIsLocal())) {
			processorConfig.setLocalSource(true);
		} else {
			processorConfig.setLocalSource(false);
		}
		if (null != processorDto.getRouteRuleId()) {
			processorConfig.setRouteRuleId(processorDto.getRouteRuleId() + "");
			if (!channelConfig.getRouteTable().containsKey(
					processorDto.getRouteRuleId() + "")) {
				RouteRule routeRuleConfig = transformRouteRuleConfig(
						channelConfig, processorDto.getRouteRuleId(), conn);
				channelConfig.getRouteTable().put(routeRuleConfig.getId(),
						routeRuleConfig);
			}
		}
		if (TRUE.equals(processorDto.getSourceAsync())) {
			processorConfig.setSourceAsync(true);
		} else {
			processorConfig.setSourceAsync(false);
		}
		if (null != processorDto.getSourceChannelMessageObjectType()) {
			processorConfig
					.setSourceChannelMessageObjectType(Integer
							.parseInt(processorDto
									.getSourceChannelMessageObjectType()));
		}
		processorConfig.setSourceMapCharset(processorDto.getSourceMapCharset());
		processorConfig.setTimeout(processorDto.getTimeout());
		if (null != processorDto.getProcessorType()) {
			processorConfig.setType(Integer.parseInt(processorDto
					.getProcessorType()));
		}

		return processorConfig;
	}

	private RouteRule transformRouteRuleConfig(ChannelConfig channelConfig,
			String routeRuleId, Connection conn) {
		RouteRuleDAO routeRuleDao = new RouteRuleDAO();
		routeRuleDao.setConnection(conn);

		com.fib.msgconverter.commgateway.dao.routerule.dao.RouteRule routeRuleDto = routeRuleDao
				.selectByPK(routeRuleId);
		RouteRule routeRuleConfig = new RouteRule();
		routeRuleConfig.setId("" + routeRuleDto.getId());
		if (null != routeRuleDto.getRouteRuleType()) {
			routeRuleConfig.setType(Integer.parseInt(routeRuleDto
					.getRouteRuleType()));
		}
		routeRuleConfig.setExpression(routeRuleDto.getExpression());
		if (null != routeRuleDto.getDestChannelId()) {
			routeRuleConfig.setDestinationChannelSymbol(""
					+ routeRuleDto.getDestChannelId());
		}
		if (RouteRule.TYP_DYNAMIC == routeRuleConfig.getType()) {
			DeterminationDAO determinationDao = new DeterminationDAO();
			determinationDao.setConnection(conn);
			Determination determinationDto = determinationDao
					.selectByPK(routeRuleDto.getDeterminationId());
			if (null != determinationDto.getDefaultChannelId()) {
				com.fib.msgconverter.commgateway.channel.config.route.Determination determinationConfig = new com.fib.msgconverter.commgateway.channel.config.route.Determination();
				determinationConfig.setDestinationChannelSymbol(""
						+ determinationDto.getDefaultChannelId());
				routeRuleConfig.setDefaultDetermination(determinationConfig);
				routeRuleConfig
						.setDeterminationTable(transformDeterminationTable(
								channelConfig, routeRuleDto
										.getDeterminationId(), conn));
			}
		}
		return routeRuleConfig;
	}

	private Map<String,com.fib.msgconverter.commgateway.channel.config.route.Determination> transformDeterminationTable(ChannelConfig channelConfig,
			String determinationId, Connection conn) {
		DeterminationCaseRelationDAO dcrDao = new DeterminationCaseRelationDAO();
		dcrDao.setConnection(conn);

		List<DeterminationCaseRelation> dcrList = dcrDao
				.getAllCase4Determination(determinationId);
		Map<String,com.fib.msgconverter.commgateway.channel.config.route.Determination> determinationTable = null;
		if (null != dcrList && 0 < dcrList.size()) {
			determinationTable = new HashMap<>(dcrList.size());
			DeterminationCaseDAO dcDao = new DeterminationCaseDAO();
			dcDao.setConnection(conn);
			for (int i = 0; i < dcrList.size(); i++) {
				DeterminationCase dc = dcDao.selectByPK(dcrList.get(i)
						.getCaseId());
				com.fib.msgconverter.commgateway.channel.config.route.Determination determination = new com.fib.msgconverter.commgateway.channel.config.route.Determination();
				determination.setResult(dc.getResult());
				if (null != dc.getChannelId()) {
					determination.setDestinationChannelSymbol(""
							+ dc.getChannelId());
				}
				if (null != dc.getProcessorOverride()) {
					determination
							.setProcessorOverride(transformProcessorConfig(
									channelConfig, dc.getProcessorOverride(),
									conn));
				}
				determinationTable
						.put(determination.getResult(), determination);
			}
		}
		return determinationTable;
	}

	private ErrorMappingConfig transformErrorMappingConfig(
			String transformerId, Connection conn) {
		MessageTransformerConfig mtc = transformMsgTransformerConfig(
				transformerId, conn);
		ErrorMappingConfig errorMappingConfig = new ErrorMappingConfig();
		errorMappingConfig.setMappingRuleId(mtc.getMappingId());
		errorMappingConfig.setSoureMessageId(mtc.getSourceMessageId());
		return errorMappingConfig;
	}

	private MessageTransformerConfig transformMsgTransformerConfig(
			String transformerId, Connection conn) {
		TransformerDAO transformerDao = new TransformerDAO();
		transformerDao.setConnection(conn);

		Transformer transformerDto = transformerDao.selectByPK(transformerId);
		MessageTransformerConfig msgTransformerConfig = new MessageTransformerConfig();
		MessageDAO msgDao = new MessageDAO();
		msgDao.setConnection(conn);

		if (null != transformerDto.getDestMessageId()) {
			msgTransformerConfig.setDestinationMessageId(msgDao.selectByPK(
					transformerDto.getDestMessageId()).getMessageBeanId());
		}
		if (null != transformerDto.getSourceMessageId()) {
			msgTransformerConfig.setSourceMessageId(msgDao.selectByPK(
					transformerDto.getSourceMessageId()).getMessageBeanId());
		}
		MappingDAO mappingDao = new MappingDAO();
		mappingDao.setConnection(conn);
		if (null != transformerDto.getBeanMapping()) {
			msgTransformerConfig.setMappingId(mappingDao.selectByPK(
					transformerDto.getBeanMapping()).getMappingId());
		}
		return msgTransformerConfig;
	}
}
