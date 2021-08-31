package com.fib.msgconverter.commgateway.job.impl;

import java.sql.Connection;
import java.util.Map;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.ChannelSymbol;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageTransformerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ProcessorRule;
import com.fib.msgconverter.commgateway.channel.config.route.Determination;
import com.fib.msgconverter.commgateway.channel.config.route.RouteRule;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLog;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLogDAO;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessage;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessageDAO;
import com.fib.msgconverter.commgateway.job.AbstractJob;
import com.fib.msgconverter.commgateway.mapping.MappingEngine;
import com.fib.msgconverter.commgateway.mapping.config.MappingRuleManager;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionConstants;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.EnumConstants;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.map.serializer.MapSerializer;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.message.MessagePacker;
import com.giantstone.message.MessageParser;
import com.giantstone.message.bean.MessageBean;
import com.giantstone.message.metadata.MessageMetadataManager;

/**
 * 通讯任务
 * 
 * @author 白帆
 * 
 */
public class CommunicateJob extends AbstractJob {
	public static final int REFIND_TIMES = 3;
	public static final int REFIND_INTERVAL = 10000;
	private CommLog commLog;

	private void init() {
		CommLogDAO commLogDAO = new CommLogDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			commLogDAO.setConnection(conn);

			int retryTimes = 0;
			while (REFIND_TIMES > retryTimes) {
				commLog = commLogDAO.selectByPK(currentJobInfo.getLogId());
				if (null == commLog) {
					try {
						Thread.currentThread().sleep(REFIND_INTERVAL);
					} catch (Exception e) {
						e.printStackTrace();
					}
					retryTimes++;
					continue;
				} else {
					break;
				}
			}

			if (null == commLog) {
				// throw new RuntimeException(
				// "Communicate Job["
				// + currentJobInfo.getId()
				// +
				// "] Execute Error!No CommLog in table comm_log with this log_id["
				// + currentJobInfo.getLogId() + "]!");
				throw new RuntimeException(MultiLanguageResourceBundle
						.getInstance().getString(
								"CommunicateJob.init.noCommLog",
								new String[] { currentJobInfo.getId(),
										currentJobInfo.getLogId() }));
			}

			if (null != conn) {
				conn.commit();
			}
		} catch (Exception e) {
			if (null != conn) {
				try {
					conn.rollback();
				} catch (Exception ee) {
					// ee.printStackTrace();
				}
			}
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	protected boolean businessLogic() {
		if (null == commLog) {
			init();
		}

		String sourceChannelId = commLog.getSourceChannelId();
		Channel sourceChannel = (Channel) CommGateway.getChannels().get(
				sourceChannelId);
		if (null == sourceChannel) {
			// throw new RuntimeException(
			// "Communicate Job["
			// + currentJobInfo.getId()
			// + "] Execute Error!Source Channel can not be found, Channel Id:"
			// + sourceChannelId);
			throw new RuntimeException(
					MultiLanguageResourceBundle
							.getInstance()
							.getString(
									"CommunicateJob.businessLogic.sourceChannel.notFound",
									new String[] { currentJobInfo.getId(),
											sourceChannelId }));
		}

		// String destChannelId = commLog.getDestChannelId();
		// Channel destChannel = (Channel) CommGateway.getChannels().get(
		// destChannelId);
		// if (null == destChannel) {
		// throw new RuntimeException(
		// "Communicate Job["
		// + currentJobInfo.getId()
		// + "] Execute Error!Destination Channel can not be found, Channel Id:"
		// + destChannelId);
		// }

		Processor processor = (Processor) sourceChannel.getChannelConfig()
				.getProcessorTable().get(commLog.getProcessorId());
		if (null == processor) {
			// throw new RuntimeException(
			// "Communicate Job["
			// + currentJobInfo.getId()
			// +
			// "] Execute Error!Source Processor can not be found, Channel Id:"
			// + commLog.getSourceChannelId() + ", Processor Id :"
			// + commLog.getProcessorId());
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"CommunicateJob.businessLogic.processor.null",
							new String[] { currentJobInfo.getId(),
									commLog.getSourceChannelId(),
									commLog.getProcessorId() }));
		}

		ProcessorRule processorRule = new ProcessorRule();
		processorRule.setProcessorId(currentJobInfo.getProcessId());
		processorRule.setRequestMessageFrom(currentJobInfo
				.getRequestMessageFrom());
		if (null == processorRule) {
			// throw new RuntimeException(
			// "Communicate Job["
			// + currentJobInfo.getId()
			// +
			// "] Execute Error!Processor doesn't have <processor-rule>, Processor:"
			// + processor.getId() + ", SourceChannelId :"
			// + commLog.getSourceChannelId());
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"CommunicateJob.businessLogic.processorRule.null",
							new String[] { currentJobInfo.getId(),
									processor.getId(),
									commLog.getSourceChannelId() }));
		}

		byte[] message = getRequestMessage(processorRule
				.getRequestMessageFrom(), currentJobInfo.getLogId());
		// switch (processorRule.getRequestMessageFrom()) {
		// case ProcessorRule.TYP_DEST_REQ:
		// message = commLog.getDestRequestMessage();
		// break;
		// case ProcessorRule.TYP_DEST_RES:
		// message = commLog.getDestResponseMessage();
		// break;
		// case ProcessorRule.TYP_SRC_REQ:
		// message = commLog.getSourceRequestMessage();
		// break;
		// case ProcessorRule.TYP_SRC_RES:
		// message = commLog.getSourceResponseMessage();
		// break;
		// }
		if (null == message) {
			// throw new RuntimeException("Communicate Job["
			// + currentJobInfo.getId()
			// + "] Execute Error!Request Message is NULL!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"CommunicateJob.businessLogic.message.null",
							new String[] { currentJobInfo.getId() }));
		}

		ChannelConfig channelConfig = sourceChannel.getChannelConfig();
		processor = (Processor) channelConfig.getProcessorTable().get(
				processorRule.getProcessorId());

		RouteRule routeRule = (RouteRule) sourceChannel.getChannelConfig()
				.getRouteTable().get(processor.getRouteRuleId());

		String destChannelId = null;
		if (routeRule.getType() == RouteRule.TYP_STATIC) {
			// 取目标通道ID
			destChannelId = ((ChannelSymbol) channelConfig
					.getChannelSymbolTable().get(
							routeRule.getDestinationChannelSymbol()))
					.getChannlId();
		} else {
			// 得到动态路由的结果
			Determination determination = null;

			determination = routeRule.determine(null, message, null);
			if (null == determination) {
				// throw new RuntimeException(
				// "Dynamic Route's Determination is NULL!");
				throw new RuntimeException(
						MultiLanguageResourceBundle
								.getInstance()
								.getString(
										"CommunicateJob.businessLogic.determination.null"));
			}

			if (null != determination.getProcessorOverride()) {
				// processor需覆盖
				processor = determination.getProcessorOverride();
			}
			destChannelId = ((ChannelSymbol) channelConfig
					.getChannelSymbolTable().get(
							determination.getDestinationChannelSymbol()))
					.getChannlId();
		}

		Channel destChannel = (Channel) CommGateway.getChannels().get(
				destChannelId);

		Processor newProcessor = (Processor) sourceChannel.getChannelConfig()
				.getProcessorTable().get(processorRule.getProcessorId());

		String messageGroupId = sourceChannel.getChannelConfig()
				.getMessageBeanGroupId();
		messageGroupId = messageGroupId == null ? sourceChannelId
				: messageGroupId;

		MessageTransformerConfig requestTransformerConfig = newProcessor
				.getRequestMessageConfig();
		if (null != requestTransformerConfig) {
			Object sourceObject = null;
			if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == newProcessor
					.getSourceChannelMessageObjectType()) {
				MessageParser parser = new MessageParser();
				parser.setMessage(MessageMetadataManager.getMessage(
						messageGroupId, requestTransformerConfig
								.getSourceMessageId()));
				parser.setMessageData(message);

				sourceObject = parser.parse();
			} else {
				sourceObject = MapSerializer.deserialize(new String(message));
			}

			MappingEngine mapper = new MappingEngine();
			mapper.setParameter("CHANNEL_ID", sourceChannelId);
			mapper.setParameter("MESSAGE", message);
			mapper.setParameter("REQUEST", sourceObject);
			Object destObject = mapper.map(sourceObject, MappingRuleManager
					.getMappingRule(sourceChannelId, requestTransformerConfig
							.getMappingId()));

			if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == newProcessor
					.getDestChannelMessageObjectType()) {
				MessagePacker packer = new MessagePacker();
				packer.setMessage(MessageMetadataManager.getMessage(
						sourceChannelId, requestTransformerConfig
								.getDestinationMessageId()));
				packer.setMessageBean((MessageBean) destObject);

				message = packer.pack();
			} else {
				message = MapSerializer.serialize((Map) destObject).getBytes();
			}
		}

		Session session = SessionManager.createSession();
		// session.setExternalSerialNum(commLog.getExternalSerialNum());
		if (0 < newProcessor.getTimeout()) {
			session.setTimeout(newProcessor.getTimeout());
		}

		session.setType(Session.TYP_INTERNAL);
		session.setProcessor(newProcessor);
		session.setSourceChannel(sourceChannel);
		session.setDestChannel(destChannel);
		session.setDestRequestMessage(message);
		session.setJob(this);

		SessionManager.attachSession(message, session);

		destChannel.sendRequestMessage(message, newProcessor.isDestAsync(),
				newProcessor.getTimeout());

		return true;
	}

	private byte[] getRequestMessage(int messageType, String logId) {
		String type = null;
		switch (messageType) {
		case ProcessorRule.TYP_DEST_REQ:
			type = SessionConstants.DST_REQ;
			break;
		case ProcessorRule.TYP_DEST_RES:
			type = SessionConstants.DST_RES;
			break;
		case ProcessorRule.TYP_SRC_REQ:
			type = SessionConstants.SRC_REQ;
			break;
		case ProcessorRule.TYP_SRC_RES:
			type = SessionConstants.SRC_RES;
			break;
		}

		CommLogMessage commLogMessage = null;
		CommLogMessageDAO commLogMessageDao = new CommLogMessageDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			commLogMessageDao.setConnection(conn);
			commLogMessage = commLogMessageDao.selectByPK(logId, type);
			conn.commit();
		} catch (Exception e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}

		if (null != commLogMessage) {
			return commLogMessage.getMessage();
		} else {
			return null;
		}
	}
}
