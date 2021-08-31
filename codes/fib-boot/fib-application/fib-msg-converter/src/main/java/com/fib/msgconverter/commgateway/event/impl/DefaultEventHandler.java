package com.fib.msgconverter.commgateway.event.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.action.AbstractEventAction;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.ChannelConfig;
import com.fib.msgconverter.commgateway.channel.config.ChannelSymbol;
import com.fib.msgconverter.commgateway.channel.config.processor.ErrorMappingConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageHandlerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.MessageTransformerConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ActionConfig;
import com.fib.msgconverter.commgateway.channel.config.processor.event.ScheduleRule;
import com.fib.msgconverter.commgateway.channel.config.route.Determination;
import com.fib.msgconverter.commgateway.channel.config.route.RouteRule;
import com.fib.msgconverter.commgateway.channel.message.recognizer.AbstractMessageRecognizer;
import com.fib.msgconverter.commgateway.dao.job.dao.Job;
import com.fib.msgconverter.commgateway.dao.job.dao.JobDAO;
import com.fib.msgconverter.commgateway.dao.joblog.dao.JobLog;
import com.fib.msgconverter.commgateway.dao.joblog.dao.JobLogDAO;
import com.fib.msgconverter.commgateway.event.Event;
import com.fib.msgconverter.commgateway.event.EventHandler;
import com.fib.msgconverter.commgateway.job.AbstractJob;
import com.fib.msgconverter.commgateway.job.JobConstants;
import com.fib.msgconverter.commgateway.job.JobManager;
import com.fib.msgconverter.commgateway.job.impl.CommunicateJob;
import com.fib.msgconverter.commgateway.mapping.MappingEngine;
import com.fib.msgconverter.commgateway.mapping.config.MappingRuleManager;
import com.fib.msgconverter.commgateway.message.ErrorMessage;
import com.fib.msgconverter.commgateway.message.ReturnCodeConstant;
import com.fib.msgconverter.commgateway.messagehandler.AbstractMessageHandler;
import com.fib.msgconverter.commgateway.messagehandler.ResultAfterHandler;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionConstants;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.EnumConstants;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.commgateway.util.serialnumber.SerialNumberGenerator;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.map.serializer.MapSerializer;
import com.giantstone.common.script.exception.CustomerException;
import com.giantstone.common.util.ClassUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.message.MessagePacker;
import com.giantstone.message.MessageParser;
import com.giantstone.message.bean.MessageBean;
import com.giantstone.message.metadata.MessageMetadataManager;

/**
 * 默认事件处理器
 * 
 * @author 白帆
 * 
 */
public class DefaultEventHandler extends EventHandler {

	public void handleException(Event event) {
		Session session = null;
		if (null != event.getRequestMessage()) {
			session = SessionManager.getSession(event.getRequestMessage());
		} else {
			session = SessionManager.getSession(event.getResponseMessage());
		}

		if (null == session) {
			// 会话超时被清除
			logEvent(event);
			return;
		}

		Channel sourceChannel = session.getSourceChannel();
		String sourceChannelId = sourceChannel.getId();
		Processor processor = session.getProcessor();
		try {
			if (Session.TYP_INTERNAL.equals(session.getType())) {
				// 内部会话
				return;
			}

			if (processor.isSourceAsync()) {
				return;
			}

			// 组织网关内部异常信息MB
			ErrorMessage errorBean = new ErrorMessage();
			errorBean.setErrorCode(session.getErrorType());
			if (null != session.getErrorMessage()) {
				errorBean.setErrorInfo(session.getErrorMessage());
			}
			session.setErrorBean(errorBean);
			ErrorMappingConfig errorMappingConfig = processor.getErrorMappingConfig();
			if (null == errorMappingConfig) {
				// 关闭通讯源
				sourceChannel.closeSource(session.getSource());
				return;
			}
			// 映射为源通道异常MB
			Object mappedObject = objectMapping(sourceChannelId, null, errorMappingConfig.getMappingRuleId(), session,
					errorBean);

			// MessageBean sourceErrorBean = (MessageBean) mappedObject;
			// 组包
			String messageBeanGroupId = sourceChannel.getChannelConfig().getMessageBeanGroupId();
			byte[] sourceErrorMessage = packMessage(messageBeanGroupId, errorMappingConfig.getSoureMessageId(),
					mappedObject, processor.getSourceMapCharset());

			session.setSourceResponseMessage(sourceErrorMessage);
			// 关联异常信息和会话
			SessionManager.attachSession(sourceErrorMessage, session);
			// 设置会话状态为失败
			session.setState(Session.STATE_FAILURE);
			session.setTimeToSendResNanoTime(System.nanoTime());
			// 发送回源系统
			sourceChannel.sendResponseMessage(sourceErrorMessage);
		} catch (Exception e) {
			// e.printStackTrace();
			event.setExcp(e);
			// 关闭通讯源
			sourceChannel.closeSource(session.getSource());
			logEvent(event);
		} finally {
			// 设置会话状态为失败
			session.setState(Session.STATE_FAILURE);
			SessionManager.terminalSession(session);
			if (Session.TYP_INTERNAL.equals(session.getType()) && null != session.getJob()
					&& JobConstants.JOB_STAT_ALIVE.equals(session.getJob().getCurrentJobInfo().getState())) {
				// 内部会话并且有任务存在并且任务为活动状态，需重新注册
				AbstractJob currentJob = session.getJob();
				updateJobLog(session, JobConstants.JOB_LOG_STAT_FAILED);
				if (currentJob.getCurrentJobInfo().getCurrentTimes() == currentJob.getCurrentJobInfo().getMaxTimes()) {
					// 达到最大次数，关闭任务，设置状态为失败
					currentJob.terminate(false);
					return;
				}
				JobManager.addJobSchedule(session.getJob());
				return;
			}
			List eventList = (List) processor.getEventConfig().get(session.getErrorType());
			if (null != eventList) {
				for (int i = 0; i < eventList.size(); i++) {
					triggerEvent((ActionConfig) eventList.get(i), session);
				}
			}
		}
	}

	public void handleResponseSendError(Event event) {
		Session session = SessionManager.getSession(event.getResponseMessage());
		if (null == session) {
			// 会话超时被清除
			logEvent(event);
			return;
		}
		// updateSessionAfterException(session,
		// EventTypeConstants.STATE_SRC_RSP_MSG_SEND,
		// "Send Response Message Failed", event.getExcp(), event);
		updateSessionAfterException(session, SessionConstants.STATE_SRC_RSP_MSG_SEND,
				MultiLanguageResourceBundle.getInstance().getString("DefaultEventHandler.sendResponse.failed"),
				event.getExcp(), event);
		// handleException(event);
		SessionManager.terminalSession(session);

	}

	public void handleRequestSent(Event event) {
		// 1. 更新会话
		Session session = SessionManager.getSession(event.getRequestMessage());
		if (null == session) {
			// 会话可能已超时被清除
			// log 超时终止会话时已记录数据库流水日志
			// 关闭通讯源
			// 不必作 event.getSourceChannel().closeSource(event.getSource());
			return;
		}
		session.getEventList().add(event);
		session.setDestSource(event.getSource());
		// session.setDestSourceInfo(event.getSource().toString());
		session.setRequestSentTime(System.currentTimeMillis());
		session.setRequestSentNanoTime(System.nanoTime());

		Processor processor = session.getProcessor();
		if (!processor.isDestAsync()) {
			// 目标系统通讯为同步
			return;
		} else {
			if (Session.TYP_INTERNAL.equals(session.getType())) {
				if (null != session.getJob()) {
					session.getJob().terminate(true);
				}
			}
		}
		if (processor.isSourceAsync()) {
			// 源系统通讯为异步
			// 关闭通讯源
			// event.getSourceChannel().closeSource(event.getSource());
			// 结束会话
			session.setState(Session.STATE_SUCCESS);
			SessionManager.terminalSession(session);
			return;
		} else {

			// 源系统同步
			Channel sourceChannel = session.getSourceChannel();
			String sourceChannelId = sourceChannel.getId();
			String messageBeanGroupId = sourceChannel.getChannelConfig().getMessageBeanGroupId();

			String messageId = null;
			if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor
					.getSourceChannelMessageObjectType()) {
				messageId = processor.getRequestMessageConfig().getSourceMessageId();
			}
			// 源请求报文解包
			Object sourceRequestObject = session.getSourceRequestObject();
			// byte[] sourceRequestMessage = session.getSourceRequestMessage();
			// try {
			// // if (Processor.MSG_OBJ_TYP_MB == processor
			// // .getSourceChannelMessageObjectType()) {
			// // sourceRequestObject = parseMessage(messageBeanGroupId,
			// // messageId, sourceRequestMessage);
			// // } else {
			// // sourceRequestObject = MapSerializer.deserialize(new String(
			// // sourceRequestMessage, processor
			// // .getSourceMapCharset()));
			// // }
			//
			// sourceRequestObject = parseMessage(processor
			// .getSourceChannelMessageObjectType(),
			// messageBeanGroupId, messageId, processor
			// .getSourceMapCharset(), sourceRequestMessage);
			// } catch (Exception e) {
			// // updateSessionAfterException(session,
			// // EventTypeConstants.STATE_SRC_REQ_MSG_PARSE,
			// // "Parse SourceRequest Message Error!"
			// // + "SourceChannelId[" + sourceChannelId + "], "
			// // + "ProcessorId[" + processor.getId() + "]", e,
			// // event);
			// updateSessionAfterException(session,
			// SessionConstants.STATE_SRC_REQ_MSG_PARSE,
			// MultiLanguageResourceBundle.getInstance().getString(
			// "DefaultEventHandler.parseSourceRequest.error",
			// new String[] { sourceChannelId,
			// processor.getId(), e.getMessage() }),
			// e, event);
			// handleException(event);
			// return;
			// }
			// 通过映射得到源回应对象
			Object sourceResponseObject = null;
			try {
				long time = System.nanoTime();
				sourceResponseObject = objectMapping(sourceChannelId, session.getDestRequestMessage(),
						processor.getResponseMessageConfig().getMappingId(), session, sourceRequestObject);
				session.resMappingSpendTime = (System.nanoTime() - time) / 1000000;
			} catch (Exception e) {
				// updateSessionAfterException(
				// session,
				// EventTypeConstants.STATE_RSP_MSG_MAPPING,
				// "Mapping Error!From Source Request MessageObject To Source Response
				// MessageObject."
				// + "SourceChannelId["
				// + sourceChannelId
				// + "], ProcessorId["
				// + processor.getId()
				// + "], MappingRuleId["
				// + processor.getResponseMessageConfig()
				// .getMappingId() + "]", e, event);
				updateSessionAfterException(session, SessionConstants.STATE_RSP_MSG_MAPPING,
						MultiLanguageResourceBundle.getInstance().getString("DefaultEventHandler.requestMappingError",
								new String[] { sourceChannelId, processor.getId(),
										processor.getResponseMessageConfig().getMappingId(), e.getMessage() }),
						e, event);
				handleException(event);
				return;
			}

			messageId = null;
			if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor
					.getSourceChannelMessageObjectType()) {
				messageId = processor.getResponseMessageConfig().getSourceMessageId();
			}
			// 源回应对象组包
			byte[] sourceResponseMessage = null;
			try {
				long time = System.nanoTime();
				sourceResponseMessage = packMessage(messageBeanGroupId, messageId, sourceResponseObject,
						processor.getSourceMapCharset());
				session.resPackSpendTime = (System.nanoTime() - time) / 1000000;
			} catch (Exception e) {
				// updateSessionAfterException(session,
				// EventTypeConstants.STATE_SRC_RSP_MSG_PACK,
				// "Pack Source Response Message Error!Channel["
				// + sourceChannelId + "], Processor["
				// + processor.getId() + "]", e, event);
				updateSessionAfterException(session, SessionConstants.STATE_SRC_RSP_MSG_PACK,
						MultiLanguageResourceBundle.getInstance().getString(
								"DefaultEventHandler.packSourceResponse.error",
								new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
						e, event);
				handleException(event);
				return;
			}
			// 关联会话
			SessionManager.attachSession(sourceResponseMessage, session);
			// 更新会话
			session.setSourceResponseMessage(sourceResponseMessage);
			session.setTimeToSendResNanoTime(System.nanoTime());
			// 发送源回应报文
			sourceChannel.sendResponseMessage(sourceResponseMessage);
		}
	}

	public void handleResponseArrived(Event event) {
		byte[] destResponseMessage = event.getResponseMessage();
		// 1. 取得并更新会话
		Session session = SessionManager.getSession(event.getRequestMessage());

		if (null == session) {
			// 会话可能已超时被清除
			logEvent(event);
			return;
		}

		session.setDestResponseMessage(destResponseMessage);
		session.setResponseArrivedTime(System.currentTimeMillis());
		session.setResponseArrivedNanoTime(System.nanoTime());
		// 2. 关联目的回应报文和会话
		SessionManager.attachSession(destResponseMessage, session);

		Processor processor = session.getProcessor();

		ScheduleRule scheduleRule = null;

		if (Session.TYP_INTERNAL.equals(session.getType())) {
			scheduleRule = new ScheduleRule();
			scheduleRule.setLoop(session.getJob().getCurrentJobInfo().getMaxTimes());
			scheduleRule.setInterval(session.getJob().getCurrentJobInfo().getJobInterval());
			scheduleRule.setEndFlag(session.getJob().getCurrentJobInfo().getScheduleEndFlag());

			// 内部会话且以通讯成功为结束标志，目前由于内部会话是由通讯任务产生，因此不做任务不存在的校验
			if (SessionConstants.STATE_COMMUNICATE_SUCCESS.equals(scheduleRule.getEndFlag())) {
				session.getJob().terminate(true);
				// 成功结束
				// 设置会话状态为成功
				session.setState(Session.STATE_SUCCESS);
				// 结束会话
				SessionManager.terminalSession(session);

				// 更新任务日志
				updateJobLog(session, JobConstants.JOB_LOG_STAT_SUCCESS);

				if (null != processor.getEventConfig()) {
					List list = (List) processor.getEventConfig().get(SessionConstants.STATE_COMMUNICATE_SUCCESS);
					if (null != list) {
						for (int i = 0; i < list.size(); i++) {
							ActionConfig actionConfig = (ActionConfig) list.get(i);
							triggerEvent(actionConfig, session);
						}
					}
				}
				return;
			}
		} else {
			// 外部会话，表明该会话是由外部系统发起
			session.getEventList().add(event);
			session.setDestSource(event.getSource());
			// session.setDestSourceInfo(event.getSource().toString());
		}

		// 3. 取得源通道及源通道配置元数据
		Channel sourceChannel = session.getSourceChannel();

		String sourceChannelId = sourceChannel.getId();

		boolean businessIsTrue = true;
		byte[] sourceResponseMessage = null;
		if (EnumConstants.ProcessorType.TRANSMIT.getCode() == processor.getType()
				&& !Session.TYP_INTERNAL.equals(session.getType())) {
			sourceResponseMessage = destResponseMessage;
		} else {

			// 5. 判断是否需识别返回码
			AbstractMessageRecognizer recognizer = session.getDestChannel().getReturnCodeRecognizer();

			MessageTransformerConfig messageConfig = null;
			// MessageHandlerConfig messageHandlerConfig = null;
			if (null == recognizer) {
				// 不需识别返回码
				messageConfig = processor.getResponseMessageConfig();
				if (Session.TYP_INTERNAL.equals(session.getType())
						&& SessionConstants.STATE_BUSINESS_SUCCESS.equals(scheduleRule.getEndFlag())) {
					String errorMessage = MultiLanguageResourceBundle.getInstance().getString(
							"DefaultEventHandler.endFlag.false",
							new String[] { session.getJob().getCurrentJobInfo().getId(), processor.getId() });
					updateSessionAfterException(session, SessionConstants.STATE_RET_CODE_RECOGNIZE, errorMessage,
							new RuntimeException(errorMessage), event);
					handleException(event);
					return;
				}
				// messageHandlerConfig =
				// processor.getResponseMessageHandlerConfig();
			} else {
				// 需识别返回码
				long time = System.nanoTime();
				String returnCode = null;
				try {
					returnCode = recognizer.recognize(destResponseMessage);
				} catch (Exception e) {
					// 更新会话
					// updateSessionAfterException(session,
					// EventTypeConstants.STATE_RET_CODE_RECOGNIZE,
					// "Recognizer Return Code Error!SourceChannelId["
					// + sourceChannelId + "], ProcessorId["
					// + processor.getId() + "]", e, event);
					updateSessionAfterException(session, SessionConstants.STATE_RET_CODE_RECOGNIZE,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.returnCodeRecognize.error",
									new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
							e, event);
					// 处理异常
					handleException(event);
					return;
				}
				session.resMsgRecognizeSpendTime = (System.nanoTime() - time) / 1000000;

				ChannelConfig channelConfig = session.getDestChannel().getChannelConfig();
				if (!channelConfig.getReturnCodeRecognizerConfig().getSuccessCodeSet().contains(returnCode)) {
					// 返回码属于错误返回码集合，说明交易业务出错
					messageConfig = processor.getErrorMessageConfig();

					if (Session.TYP_INTERNAL.equals(session.getType())) {
						if (SessionConstants.STATE_BUSINESS_SUCCESS.equals(scheduleRule.getEndFlag())) {
							// 内部会话，任务失败
							session.setState(Session.STATE_FAILURE);

							SessionManager.terminalSession(session);

							updateJobLog(session, JobConstants.JOB_LOG_STAT_FAILED);
							// 重新注册任务
							JobManager.addJobSchedule(session.getJob());
							return;
						}
					} else {
						if (null == messageConfig && !processor.isSourceAsync()) {
							String errorMessage = MultiLanguageResourceBundle.getInstance()
									.getString("DefaultEventHandler.returnCodeRecognize.returnCodeRecognizeNotExist");
							updateSessionAfterException(session, SessionConstants.STATE_NO_ERROR_MSG_TRANSFORMER,
									errorMessage, new RuntimeException(errorMessage), event);
							handleException(event);
							return;
						}
					}
					businessIsTrue = false;

					// 处理业务失败事件
					if (null != processor.getEventConfig()) {
						List eventList = (List) processor.getEventConfig().get(SessionConstants.STATE_BUSINESS_FAILED);
						if (null != eventList) {
							for (int i = 0; i < eventList.size(); i++) {
								triggerEvent((ActionConfig) eventList.get(i), session);
							}
						}
					}

					// messageHandlerConfig =
					// processor.getErrorMessageHandlerConfig();
					// } else if (channelConfig.getReturnCodeRecognizerConfig()
					// .getSuccessCodeSet().contains(returnCode)) {
				} else {
					// 返回码属于正确返回码集合，说明交易业务成功
					messageConfig = processor.getResponseMessageConfig();
					if (Session.TYP_INTERNAL.equals(session.getType())
							&& SessionConstants.STATE_BUSINESS_SUCCESS.equals(scheduleRule.getEndFlag())) {
						// 内部会话且以业务成功为结束标志
						session.getJob().terminate(true);
						// 成功结束
						// 设置会话状态为成功
						session.setState(Session.STATE_SUCCESS);
						// 结束会话
						SessionManager.terminalSession(session);

						// 更新任务日志
						updateJobLog(session, JobConstants.JOB_LOG_STAT_SUCCESS);

						if (null != processor.getEventConfig()) {
							List list = (List) processor.getEventConfig()
									.get(SessionConstants.STATE_COMMUNICATE_SUCCESS);
							if (null != list) {
								for (int i = 0; i < list.size(); i++) {
									ActionConfig actionConfig = (ActionConfig) list.get(i);
									triggerEvent(actionConfig, session);
								}
							}
						}
						return;
					}
					// messageHandlerConfig =
					// processor.getResponseMessageHandlerConfig();
				}
				// else {
				// 更新会话
				// updateSessionAfterException(session,
				// EventTypeConstants.STATE_RET_CODE_RECOGNIZE,
				// "Unkown Return Code: " + returnCode + "!",
				// new RuntimeException("Unkown Return Code: "
				// + returnCode + "!"), event);
				// String errorMessage = MultiLanguageResourceBundle
				// .getInstance()
				// .getString(
				// "DefaultEventHandler.returnCodeRecognize.unkownReturnCode",
				// new String[] { returnCode });
				// updateSessionAfterException(session,
				// SessionConstants.STATE_RET_CODE_RECOGNIZE,
				// errorMessage, new RuntimeException(errorMessage),
				// event);
				// handleException(event);
				// return;
				// }
			}

			if (processor.isSourceAsync()) {
				// 异步通讯，更新会话状态，结束会话
				session.setState(Session.STATE_SUCCESS);
				SessionManager.terminalSession(session);
				return;
			}

			// 6. 目标回应报文解包
			String messageBeanGroupId = null;
			Object destResponseData = null;
			// if (null == messageConfig) {
			if (EnumConstants.ProcessorType.TRANSMIT.getCode() == processor.getType()) {
				sourceResponseMessage = destResponseMessage;
			} else {
				String messageId = null;
				if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor
						.getDestChannelMessageObjectType()) {
					messageId = messageConfig.getDestinationMessageId();
				}

				try {
					// messageBeanGroupId = session.getDestChannel()
					// .getChannelConfig().getMessageBeanGroupId();
					// messageBeanGroupId = messageBeanGroupId == null ? session
					// .getDestChannel().getId() : messageBeanGroupId;
					messageBeanGroupId = sourceChannel.getChannelConfig().getMessageBeanGroupId();
					// if (Processor.MSG_OBJ_TYP_MB == processor
					// .getDestChannelMessageObjectType()) {
					// destResponseData = parseMessage(messageBeanGroupId,
					// messageId, destResponseMessage);
					// } else {
					// destResponseData = MapSerializer
					// .deserialize(new String(destResponseMessage,
					// processor.getDestMapCharset()));
					// }
					long time = System.nanoTime();
					destResponseData = parseMessage(processor.getDestChannelMessageObjectType(), messageBeanGroupId,
							messageId, processor.getDestMapCharset(), destResponseMessage);
					session.resParseSpendTime = (System.nanoTime() - time) / 1000000;
				} catch (Exception e) {
					// 更新会话
					// updateSessionAfterException(
					// session,
					// EventTypeConstants.STATE_DST_RSP_MSG_PARSE,
					// "Parse Destination Response Message Error!ChannelId["
					// + session.getDestChannel().getId()
					// + "], Processor[" + processor.getId() + "]",
					// e, event);
					updateSessionAfterException(session, SessionConstants.STATE_DST_RSP_MSG_PARSE,
							MultiLanguageResourceBundle.getInstance()
									.getString("DefaultEventHandler.parseDestResponse.error", new String[] {
											session.getDestChannel().getId(), processor.getId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}
				session.setDestResponseObject(destResponseData);

				MessageHandlerConfig messageHandlerConfig = null;
				if (businessIsTrue) {
					messageHandlerConfig = processor.getResponseMessageHandlerConfig();
				} else {
					messageHandlerConfig = processor.getErrorMessageHandlerConfig();
				}

				if (null != messageHandlerConfig) {
					// 回应报文处理handler不为空时
					try {
						destResponseData = doMessageHandlerProcess(messageHandlerConfig.getClassName(),
								destResponseData, destResponseMessage, null);
					} catch (Exception e) {
						// updateSessionAfterException(session,
						// SessionConstants.STATE_RSP_MSG_HANDLE,
						// "Destination Response Message handle Error!SourceChannelId["
						// + sourceChannelId + "], ProcessorId["
						// + processor.getId() + "]", e, event);
						updateSessionAfterException(session, SessionConstants.STATE_RSP_MSG_HANDLE,
								MultiLanguageResourceBundle.getInstance().getString(
										"DefaultEventHandler.response.handler.error",
										new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
								e, event);
						handleException(event);
						return;
					}
				}

				// 7. 映射为源回应数据对象
				Object sourceResponseData = null;
				try {
					long time = System.nanoTime();
					sourceResponseData = objectMapping(sourceChannelId, event.getResponseMessage(),
							messageConfig.getMappingId(), session, destResponseData);
					session.resMappingSpendTime = (System.nanoTime() - time) / 1000000;
				} catch (Exception e) {
					// updateSessionAfterException(
					// session,
					// EventTypeConstants.STATE_RSP_MSG_MAPPING,
					// "Mapping Error!From Destination Response MessageObject To Source Response
					// MessageObject."
					// + "SourceChannelId["
					// + sourceChannelId
					// + "], ProcessorId["
					// + processor.getId()
					// + "], MappingRuleId["
					// + messageConfig.getMappingId() + "]", e,
					// event);
					updateSessionAfterException(session, SessionConstants.STATE_RSP_MSG_MAPPING,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.responseMappingError", new String[] { sourceChannelId,
											processor.getId(), messageConfig.getMappingId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}
				session.setSourceResponseObject(sourceResponseData);
				messageId = null;
				if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor
						.getSourceChannelMessageObjectType()) {
					messageId = messageConfig.getSourceMessageId();
				}

				// 8. 源回应MB组包
				try {
					long time = System.nanoTime();
					sourceResponseMessage = packMessage(messageBeanGroupId, messageId, sourceResponseData,
							processor.getSourceMapCharset());
					session.resPackSpendTime = (System.nanoTime() - time) / 1000000;
				} catch (Exception e) {
					// updateSessionAfterException(session,
					// EventTypeConstants.STATE_SRC_RSP_MSG_PACK,
					// "Pack Source Response Message Error!Channel["
					// + sourceChannelId + "], Processor["
					// + processor.getId() + "]", e, event);
					updateSessionAfterException(session, SessionConstants.STATE_SRC_RSP_MSG_PACK,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.packSourceResponse.error",
									new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}
			}
		}
		// 关联会话
		SessionManager.attachSession(sourceResponseMessage, session);
		// 更新会话
		session.setSourceResponseMessage(sourceResponseMessage);

		// if (Session.TYP_INTERNAL.equals(session.getType())) {
		// // 内部会话，任务失败
		// session.setState(Session.STATE_FAILURE);
		//
		// SessionManager.terminalSession(session);
		//
		// updateJobLog(session, JobConstants.JOB_LOG_STAT_FAILED);
		// // 重新注册任务
		// JobManager.addJobSchedule(session.getJob());
		//
		// } else {
		// // 外部会话
		session.setTimeToSendResNanoTime(System.nanoTime());
		// 发送源回应报文
		sourceChannel.sendResponseMessage(sourceResponseMessage);
		// }
	}

	public void handleRequestArrived(Event event) {
		// 1. 请求到达，创建会话
		Channel sourceChannel = event.getSourceChannel();

		String sourceChannelId = sourceChannel.getId();
		byte[] requestMessage = event.getRequestMessage();
		Session session = null;
		try {
			session = SessionManager.createSession();
		} catch (Exception e) {
			// updateSessionAfterException(session,
			// SessionConstants.STATE_CREATE_SESSION,
			// MultiLanguageResourceBundle.getInstance().getString(
			// "DefaultEventHandler.createSession.error",
			// new String[] { sourceChannelId, e.getMessage() }),
			// e, event);
			event.getSourceChannel().closeSource(event.getSource());
			// 因为此时还没有得到处理器，因此直接终止会话
			// SessionManager.terminalSession(session);
			throw new RuntimeException(e);
		}
		session.setSourceChannel(sourceChannel);
		session.setSource(event.getSource());
		// session.setSourceInfo(event.getSource().toString());
		session.setSourceRequestMessage(requestMessage);
		session.getEventList().add(event);

		// 2. 关联源请求报文和会话
		SessionManager.attachSession(requestMessage, session);

		// 3. 取出message-type
		AbstractMessageRecognizer recognizer = sourceChannel.getMessageTypeRecognizer();

		long time = System.nanoTime();
		String messageType = null;
		try {
			messageType = recognizer.recognize(requestMessage);
		} catch (Exception e) {
			// updateSessionAfterException(session,
			// EventTypeConstants.STATE_MSG_TYP_RECOGNIZE,
			// "Recognize Message Type Error!SourceChannelId["
			// + sourceChannelId + "]", e, event);
			updateSessionAfterException(session, SessionConstants.STATE_MSG_TYP_RECOGNIZE,
					MultiLanguageResourceBundle.getInstance().getString(
							"DefaultEventHandler.messageTypeRecognize.error",
							new String[] { sourceChannelId, e.getMessage() }),
					e, event);
			event.getSourceChannel().closeSource(event.getSource());
			// 因为此时还没有得到处理器，因此直接终止会话
			SessionManager.terminalSession(session);
			return;
		}
		session.reqMsgRecognizeSpendTime = (System.nanoTime() - time) / 1000000;
		ChannelConfig channelConfig = sourceChannel.getChannelConfig();
		Processor processor = null;
		if (!channelConfig.getMessageTypeRecognizerConfig().getMessageTypeProcessorMap().containsKey(messageType)) {
			// updateSessionAfterException(session,
			// EventTypeConstants.STATE_MSG_TYP_RECOGNIZE,
			// "Unkown Message Type[" + messageType + "]!SourceChannelId["
			// + sourceChannelId + "]", new RuntimeException(
			// "Unkown Message Type[" + messageType + "]!"), event);
			processor = channelConfig.getDefaultProcessor();
			if (null == processor) {
				String errorMsg = MultiLanguageResourceBundle.getInstance().getString(
						"DefaultEventHandler.messageTypeRecognize.unkownMessageType",
						new String[] { messageType, sourceChannelId });
				updateSessionAfterException(session, SessionConstants.STATE_MSG_TYP_RECOGNIZE, errorMsg,
						new RuntimeException(errorMsg), event);
				event.getSourceChannel().closeSource(event.getSource());
				// 因为此时还没有得到处理器，因此直接终止会话
				SessionManager.terminalSession(session);
				return;
			}
		} else {
			// 4. 确定processor
			// 4.1 确定processorId
			String processorId = (String) channelConfig.getMessageTypeRecognizerConfig().getMessageTypeProcessorMap()
					.get(messageType);

			// 4.2 取出相应processor
			processor = (Processor) channelConfig.getProcessorTable().get(processorId);
		}

		if (processor.isSourceAsync()) {
			// 源系统异步，关闭通讯链路
			event.getSourceChannel().closeSource(event.getSource());
		}

		// 4.3 把processor放入会话
		session.setProcessor(processor);

		// 5. 处理报文
		// 5.1 确定路由规则
		RouteRule rule = (RouteRule) channelConfig.getRouteTable().get(processor.getRouteRuleId());
		// 5.2 确定报文处理类型
		int processorType = processor.getType();
		// 5.3 处理报文，决定是直接转发还是转换后转发还是本地处理
		MessageHandlerConfig requestHandler = processor.getRequestMessageHandlerConfig();
		MessageTransformerConfig requestConfig = processor.getRequestMessageConfig();

		Channel destChannel = null;
		String messageBeanGroupId = sourceChannel.getChannelConfig().getMessageBeanGroupId();

		// 解析报文
		Object sourceRequestData = null;
		try {
			time = System.nanoTime();
			if (!(EnumConstants.ProcessorType.TRANSMIT.getCode() == processorType
					&& EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor
							.getSourceChannelMessageObjectType())) {
				sourceRequestData = parseMessage(processor.getSourceChannelMessageObjectType(), messageBeanGroupId,
						requestConfig.getSourceMessageId(), processor.getSourceMapCharset(), requestMessage);
			}
			session.reqParseSpendTime = (System.nanoTime() - time) / 1000000;
		} catch (Exception e) {
			// updateSessionAfterException(session,
			// EventTypeConstants.STATE_SRC_REQ_MSG_PARSE,
			// "Parse SourceRequest Message Error!"
			// + "SourceChannelId[" + sourceChannelId + "], "
			// + "ProcessorId[" + processor.getId() + "]", e,
			// event);
			updateSessionAfterException(session, SessionConstants.STATE_SRC_REQ_MSG_PARSE,
					MultiLanguageResourceBundle.getInstance().getString("DefaultEventHandler.parseSourceRequest.error",
							new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
					e, event);
			handleException(event);
			return;
		}
		session.setSourceRequestObject(sourceRequestData);
		// 放置外部流水号
		if (EnumConstants.MessageObjectType.MAP.getCode() == processor.getSourceChannelMessageObjectType()) {
			session.setExternalSerialNum((String) ((Map) sourceRequestData).get(SessionConstants.EXTERNAL_SERIAL_NUM));
		}
		if (EnumConstants.ProcessorType.TRANSMIT.getCode() == processorType) {
			// // 处理类型为：转发
			// MessageBean requestBean = null;
			// if(null != requestHandler){
			// // 请求报文处理Handler不为空时
			// // 源请求报文解包
			// try{
			// requestBean = parseMessage(sourceChannelId,
			// requestConfig.getSourceMessageId()
			// , requestMessage);
			// }catch(Exception e){
			// updateSessionAfterException(session,
			// ErrorTypeConstants.STATE_SRC_REQ_MSG_PARSE,
			// "Parse Source Request Message Error!SourceChannelId["
			// + sourceChannelId + "], ProcessorId[" + processor.getId() + "]",
			// e, event);
			// handleException(event);
			// return;
			// }
			// try{
			// requestBean =
			// doMessageHandlerProcess(requestHandler.getClassName(),
			// requestBean, requestMessage, messageType);
			// }catch(Exception e){
			// updateSessionAfterException(session,
			// ErrorTypeConstants.STATE_REQ_MSG_HANDLE,
			// "Source Request Message handle Error!SourceChannelId["
			// + sourceChannelId + "], ProcessorId[" + processor.getId() + "]",
			// e, event);
			// handleException(event);
			// return;
			// }
			// }
			String destChannelId = null;
			if (rule.getType() == RouteRule.TYP_STATIC) {
				// 取目标通道ID
				destChannelId = ((ChannelSymbol) channelConfig.getChannelSymbolTable()
						.get(rule.getDestinationChannelSymbol())).getChannlId();
			} else {
				// 得到动态路由的结果
				Determination determination = null;
				try {
					determination = rule.determine(messageType, requestMessage, null);
					if (null == determination) {
						// throw new RuntimeException(
						// "Dynamic Route's Determination is NULL!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("DefaultEventHandler.dynamicRouteRule.determination.null"));
					}
				} catch (Exception e) {
					// updateSessionAfterException(session,
					// EventTypeConstants.STATE_DYNAMIC_ROUTE_DETERMINE,
					// "Determine From Dynamic Route Error!SourceChannelId["
					// + sourceChannelId + "]ProcessorId["
					// + processor.getId() + "]", e, event);
					updateSessionAfterException(session, SessionConstants.STATE_DYNAMIC_ROUTE_DETERMINE,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.dynamicRouteRule.determine.error",
									new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}

				if (null != determination.getProcessorOverride()) {
					// processor需覆盖
					processor = determination.getProcessorOverride();
				}
				destChannelId = ((ChannelSymbol) channelConfig.getChannelSymbolTable()
						.get(determination.getDestinationChannelSymbol())).getChannlId();
			}

			destChannel = (Channel) CommGateway.getChannels().get(destChannelId);
			// 7. 更新会话
			session.setDestChannel(destChannel);
			session.setDestRequestMessage(requestMessage);
			session.setTimeToSendReqNanoTime(System.nanoTime());

			// 8. 关联目的请求报文和会话
			SessionManager.attachSession(requestMessage, session);

			// 9. 发送目标请求报文
			destChannel.sendRequestMessage(requestMessage, processor.isDestAsync(), processor.getTimeout());

		} else if (EnumConstants.ProcessorType.LOCAL.getCode() == processor.getType()) {
			// 处理类型为：本地处理

			// 处理
			ResultAfterHandler result = null;
			try {
				result = doMessageHandlerProcess(requestHandler.getClassName(), sourceRequestData, requestMessage,
						messageType);
			} catch (Exception e) {
				// updateSessionAfterException(session,
				// EventTypeConstants.STATE_REQ_MSG_HANDLE,
				// "Source Request Message handle Error!"
				// + "SourceChannelId[" + sourceChannelId + "], "
				// + "ProcessorId[" + processor.getId() + "]", e,
				// event);
				updateSessionAfterException(session, SessionConstants.STATE_REQ_MSG_HANDLE,
						MultiLanguageResourceBundle.getInstance().getString(
								"DefaultEventHandler.sourceRequestHandle.error",
								new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
						e, event);
				handleException(event);
				return;
			}
			if (null != result) {
				session.setSourceResponseObject(result.getResultObject());

				if (processor.isSourceAsync()) {
					// 异步处理，结束会话
					session.setState(Session.STATE_SUCCESS);
					event.getSourceChannel().closeSource(event.getSource());
					SessionManager.terminalSession(session);
					return;
				}

				// 组包
				byte[] responseMessage = null;
				try {
					if (AbstractMessageHandler.SUCCESS.equals(result.getResultCode())) {
						responseMessage = packMessage(messageBeanGroupId,
								processor.getResponseMessageConfig().getSourceMessageId(), result.getResultObject(),
								processor.getSourceMapCharset());
					} else {
						responseMessage = packMessage(messageBeanGroupId,
								processor.getErrorMessageConfig().getSourceMessageId(), result.getResultObject(),
								processor.getSourceMapCharset());
					}
				} catch (Exception e) {
					// updateSessionAfterException(session,
					// EventTypeConstants.STATE_SRC_RSP_MSG_PACK,
					// "Pack Source Response Message Error!"
					// + "SourceChannelId[" + sourceChannelId + "], "
					// + "ProcessorId[" + processor.getId() + "]", e,
					// event);
					updateSessionAfterException(session, SessionConstants.STATE_SRC_RSP_MSG_PACK,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.packSourceResponse.error",
									new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}

				session.setTimeToSendResNanoTime(System.nanoTime());
				// 关联会话
				SessionManager.attachSession(responseMessage, session);
				// 发送回应
				sourceChannel.sendResponseMessage(responseMessage);
				// 更新并移除会话
				session.setSourceResponseMessage(responseMessage);
			} else {
				session.setState(Session.STATE_CANCEL);
				event.getSourceChannel().closeSource(event.getSource());
				SessionManager.terminalSession(session);
			}
			// session.setState(Session.STATE_SUCCESS);
			// SessionManager.terminalSession(session);
		} else {
			// 处理类型为：转换
			// 源请求报文解包
			// Object sourceRequestData = null;
			// try {
			// // if (Processor.MSG_OBJ_TYP_MB == processor
			// // .getSourceChannelMessageObjectType()) {
			// // // 解包成MB
			// // sourceRequestData = parseMessage(messageBeanGroupId,
			// // requestConfig.getSourceMessageId(), requestMessage);
			// // } else {
			// // // 解包成Map
			// // sourceRequestData = MapSerializer.deserialize(new String(
			// // requestMessage, processor.getSourceMapCharset()));
			// // }
			// long time = System.nanoTime();
			// sourceRequestData = parseMessage(processor
			// .getSourceChannelMessageObjectType(),
			// messageBeanGroupId, requestConfig.getSourceMessageId(),
			// processor.getSourceMapCharset(), requestMessage);
			// session.reqParseTime = (System.nanoTime() - time) / 1000000;
			// } catch (Exception e) {
			// // updateSessionAfterException(session,
			// // EventTypeConstants.STATE_SRC_REQ_MSG_PARSE,
			// // "Parse SourceRequest Message Error!"
			// // + "SourceChannelId[" + sourceChannelId + "], "
			// // + "ProcessorId[" + processor.getId() + "]", e,
			// // event);
			// updateSessionAfterException(session,
			// SessionConstants.STATE_SRC_REQ_MSG_PARSE,
			// MultiLanguageResourceBundle.getInstance().getString(
			// "DefaultEventHandler.parseSourceRequest.error",
			// new String[] { sourceChannelId,
			// processor.getId(), e.getMessage() }),
			// e, event);
			// handleException(event);
			// return;
			// }
			// session.setSourceRequestObject(sourceRequestData);
			if (null != requestHandler) {
				try {
					sourceRequestData = doMessageHandlerProcess(requestHandler.getClassName(), sourceRequestData,
							requestMessage, messageType).getResultObject();
				} catch (Exception e) {
					// updateSessionAfterException(session,
					// SessionConstants.STATE_REQ_MSG_HANDLE,
					// "Source Request Message handle Error!"
					// + "SourceChannelId[" + sourceChannelId
					// + "], " + "ProcessorId["
					// + processor.getId() + "]", e, event);
					updateSessionAfterException(session, SessionConstants.STATE_REQ_MSG_HANDLE,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.request.handler.error",
									new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}
			}
			String destChannelId = null;
			if (rule.getType() == RouteRule.TYP_STATIC) {
				// 取目标通道ID
				destChannelId = ((ChannelSymbol) channelConfig.getChannelSymbolTable()
						.get(rule.getDestinationChannelSymbol())).getChannlId();
			} else {
				// 得到动态路由的结果
				Determination determination = null;
				try {
					determination = rule.determine(messageType, requestMessage, sourceRequestData);
					if (null == determination) {
						// throw new RuntimeException(
						// "Dynamic Route's Determination is NULL!");
						throw new RuntimeException(MultiLanguageResourceBundle.getInstance()
								.getString("DefaultEventHandler.dynamicRouteRule.determination.null"));
					}
				} catch (Exception e) {
					// updateSessionAfterException(session,
					// EventTypeConstants.STATE_DYNAMIC_ROUTE_DETERMINE,
					// "Determine From Dynamic Route Error!"
					// + "SourceChannelId[" + sourceChannelId
					// + "]" + "ProcessorId[" + processor.getId()
					// + "]", e, event);
					updateSessionAfterException(session, SessionConstants.STATE_DYNAMIC_ROUTE_DETERMINE,
							MultiLanguageResourceBundle.getInstance().getString(
									"DefaultEventHandler.dynamicRouteRule.determine.error",
									new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
							e, event);
					handleException(event);
					return;
				}
				if (null != determination.getProcessorOverride()) {
					// processor需覆盖
					processor = determination.getProcessorOverride();
				}
				destChannelId = ((ChannelSymbol) channelConfig.getChannelSymbolTable()
						.get(determination.getDestinationChannelSymbol())).getChannlId();
			}
			destChannel = (Channel) CommGateway.getChannels().get(destChannelId);
			// 映射为目标请求数据对象
			Object destRequestData = null;
			try {
				// destRequestData = objectMapping(event.getSourceChannel()
				// .getId(), event.getRequestMessage(), requestConfig
				// .getMappingId(), sourceRequestData);
				time = System.nanoTime();
				destRequestData = objectMapping(sourceChannelId, event.getRequestMessage(),
						requestConfig.getMappingId(), session, sourceRequestData);
				session.reqMappingSpendTime = (System.nanoTime() - time) / 1000000;
			} catch (Exception e) {
				// updateSessionAfterException(
				// session,
				// EventTypeConstants.STATE_REQ_MSG_MAPPING,
				// "Mapping Error!From Source Request MessageObject To Destination Request
				// MessageObject."
				// + "SourceChannelId["
				// + sourceChannelId
				// + "], ProcessorId["
				// + processor.getId()
				// + "], MappingRuleId["
				// + requestConfig.getMappingId() + "]", e, event);
				updateSessionAfterException(session, SessionConstants.STATE_RSP_MSG_MAPPING,
						MultiLanguageResourceBundle.getInstance().getString("DefaultEventHandler.requestMappingError",
								new String[] { sourceChannelId, processor.getId(), requestConfig.getMappingId(),
										e.getMessage() }),
						e, event);
				handleException(event);
				return;
			}
			session.setDestRequestObject(destRequestData);
			// 目标请求对象组包
			byte[] destRequestMessage = null;
			String messageId = null;
			if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == processor.getDestChannelMessageObjectType()) {
				messageId = requestConfig.getDestinationMessageId();
			}
			// else {
			// // TODO 因为调用方不肯修改为包含关联流水号,因此此处硬将SESSION加在返回报文中
			// if (!((Map) destRequestData).containsKey(EXTERNALSERIALNUM))
			// {
			// ((Map) destRequestData).put(EXTERNALSERIALNUM, session
			// .getId());
			// }
			// }

			try {
				// messageBeanGroupId = destChannel.getChannelConfig()
				// .getMessageBeanGroupId();
				// messageBeanGroupId = messageBeanGroupId == null ?
				// destChannelId
				// : messageBeanGroupId;
				time = System.nanoTime();
				destRequestMessage = packMessage(messageBeanGroupId, messageId, destRequestData,
						processor.getDestMapCharset());
				session.reqPackSpendTime = (System.nanoTime() - time) / 1000000;
			} catch (Exception e) {
				// updateSessionAfterException(session,
				// EventTypeConstants.STATE_DST_REQ_MSG_PACK,
				// "Pack Destination Request Message Error!"
				// + "SourceChannelId[" + sourceChannelId
				// + "], ProcessorId[" + processor.getId() + "]",
				// e, event);
				updateSessionAfterException(session, SessionConstants.STATE_DST_REQ_MSG_PACK,
						MultiLanguageResourceBundle.getInstance().getString("DefaultEventHandler.packDestRequest.error",
								new String[] { sourceChannelId, processor.getId(), e.getMessage() }),
						e, event);
				handleException(event);
				return;
			}

			// 确定目标通道
			destChannel = (Channel) CommGateway.getChannels().get(destChannelId);

			// 更新会话
			session.setDestChannel(destChannel);
			session.setDestRequestMessage(destRequestMessage);
			session.setTimeToSendReqNanoTime(System.nanoTime());

			// 关联目的请求报文和会话
			SessionManager.attachSession(destRequestMessage, session);

			// 发送目标请求报文
			destChannel.sendRequestMessage(destRequestMessage, processor.isDestAsync(), processor.getTimeout());
		}

	}

	public void handleResponseSent(Event event) {
		// 1. 取得并更新会话
		Session session = SessionManager.getSession(event.getResponseMessage());
		if (null == session) {
			// 会话可能已超时被清除
			logEvent(event);
			return;
		}

		session.setResponseSentNanoTime(System.nanoTime());
		if (Session.STATE_FAILURE.equals(session.getState())) {
			return;
		}

		session.getEventList().add(event);
		// 2. 设置会话状态为成功
		session.setState(Session.STATE_SUCCESS);
		// 3. 结束会话
		SessionManager.terminalSession(session);
	}

	protected String getReturnCodeByEventType(int eventType) {
		switch (eventType) {
		case Event.EVENT_REQUEST_SEND_ERROR:
			return ReturnCodeConstant.REQUEST_SEND_ERROR;
		case Event.EVENT_CONNECT_ERROR:
			return ReturnCodeConstant.CONNECT_TO_RESPONSE_ERROR;
		case Event.EVENT_RESPONSE_RECEIVE_ERROR:
			return ReturnCodeConstant.RESPONSE_RECEIVE_ERROR;
		case Event.EVENT_EXCEPTION:
			return ReturnCodeConstant.UNKOWN_ERROR;
		default:
			return ReturnCodeConstant.INTERNAL_ERROR;
		}
	}

	/**
	 * 报文解包
	 * 
	 * @param messageMetadata
	 * @param message
	 * @return
	 */
	protected Object parseMessage(int type, String messageBeanGroupId, String messageId, String mapCharset,
			byte[] message) throws UnsupportedEncodingException {
		if (EnumConstants.MessageObjectType.MESSAGE_BEAN.getCode() == type) {
			MessageParser parser = new MessageParser();
			parser.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
			parser.setMessageData(message);

			return parser.parse();
		} else {
			return MapSerializer.deserialize(new String(message, mapCharset));
		}
	}

	/**
	 * 报文组包
	 * 
	 * @param channelId
	 * @param messageId
	 * @param data
	 * @return
	 */
	protected byte[] packMessage(String messageBeanGroupId, String messageId, Object data, String encoding) {
		if (data instanceof MessageBean) {
			((MessageBean) data).validate();
			MessagePacker packer = new MessagePacker();
			packer.setMessage(MessageMetadataManager.getMessage(messageBeanGroupId, messageId));
			packer.setMessageBean((MessageBean) data);
			return packer.pack();
		} else if (data instanceof Map) {
			byte[] ret = null;
			try {
				ret = MapSerializer.serialize((Map) data).getBytes(encoding);
			} catch (Exception e) {
				// e.printStackTrace();
				ExceptionUtil.throwActualException(e);
			}
			return ret;
		} else {
			// throw new RuntimeException(
			// "Unkown Data Type!Must be \"MessageBean\"" + " or \"Map\"");
			throw new RuntimeException(
					MultiLanguageResourceBundle.getInstance().getString("DefaultEventHandler.dataType.unkown"));
		}
	}

	/**
	 * 报文处理器处理
	 * 
	 * @param handlerClassName
	 * @param bean
	 * @param message
	 * @param messageType
	 * @return
	 */
	protected ResultAfterHandler doMessageHandlerProcess(String handlerClassName, Object data, byte[] message,
			String messageType) {
		AbstractMessageHandler handler = (AbstractMessageHandler) ClassUtil.createClassInstance(handlerClassName);
		handler.setSource(data);
		handler.setMessageData(message);
		handler.setMessageType(messageType);
		handler.setLogger(getLogger());

		return handler.doProcess();
	}

	/**
	 * 对象映射，根据配置文件由一个对象映射到另一个对象
	 * 
	 * @param sourceChannelId
	 * @param mappingId
	 * @param source
	 * @return
	 */
	protected Object objectMapping(String channelId, byte[] message, String ruleId, Session session, Object source) {
		MappingEngine mapper = new MappingEngine();
		mapper.setParameter("CHANNEL_ID", channelId);
		mapper.setParameter("MESSAGE", message);

		Object o = session.getSourceRequestObject();
		if (null != o) {
			mapper.setParameter("REQUEST", o);
			mapper.setParameter("SOURCE_REQUEST", o);
		}

		o = session.getSourceResponseObject();
		if (null != o) {
			mapper.setParameter("SOURCE_RESPONSE", o);
		}

		o = session.getDestRequestObject();
		if (null != o) {
			mapper.setParameter("DEST_REQUEST", o);
		}

		o = session.getDestResponseObject();
		if (null != o) {
			mapper.setParameter("DEST_RESPONSE", o);
		}

		return mapper.map(source, MappingRuleManager.getMappingRule(channelId, ruleId));
	}

	/**
	 * 出异常后更新会话
	 * 
	 * @param session
	 * @param state
	 * @param errorMessage
	 * @param excp
	 * @param event
	 */
	protected void updateSessionAfterException(Session session, String errorType, String errorMessage, Exception excp,
			Event event) {
		event.setExcp(excp);
		session.setErrorType(errorType);
		if (null != excp && excp instanceof CustomerException) {
			session.setErrorMessage(excp.getMessage());
		} else {
			session.setErrorMessage(errorMessage);
		}
		session.setState(Session.STATE_FAILURE);
		session.setException(excp);
		session.getEventList().add(event);
	}

	protected void triggerEvent(ActionConfig actionConfig, Session session) {
		if (ActionConfig.TYP_CLASS == actionConfig.getType()) {
			processByClass(actionConfig, session.getId());
		} else if (ActionConfig.TYP_JOB == actionConfig.getType()) {
			processByJob(actionConfig, session);
		}
	}

	protected void processByJob(ActionConfig actionConfig, Session session) {
		// 建立重发任务
		Job jobInfo = new Job();
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		jobInfo.setStartDate(format.format(currentDate));
		format = new SimpleDateFormat("HHmmss");
		jobInfo.setStartTime(format.format(currentDate));
		String id = SerialNumberGenerator.getInstance().next("JOB");
		while (12 > id.length()) {
			id = "0" + id;
		}
		jobInfo.setId(jobInfo.getStartDate() + id);
		jobInfo.setCurrentTimes(0);
		jobInfo.setMaxTimes(actionConfig.getScheduleRule().getLoop());
		jobInfo.setJobInterval(actionConfig.getScheduleRule().getInterval());
		jobInfo.setScheduleType(JobConstants.JOB_SCHE_TYP_TIMES_NUM);
		jobInfo.setState(JobConstants.JOB_STAT_ALIVE);
		jobInfo.setType(JobConstants.JOB_TYP_COMMUNICATE);
		jobInfo.setLogId(session.getId());
		jobInfo.setScheduleEndFlag(actionConfig.getScheduleRule().getEndFlag());
		jobInfo.setProcessId(actionConfig.getProcessorRule().getProcessorId());
		jobInfo.setRequestMessageFrom(actionConfig.getProcessorRule().getRequestMessageFrom());

		// jobInfo.setScheduleRule(actionConfig.getScheduleRule());
		// jobInfo.setProcessorRule(actionConfig.getProcessorRule());

		JobDAO jobDAO = new JobDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			jobDAO.setConnection(conn);

			jobDAO.insert(jobInfo);
		} catch (Exception e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception ee) {
					// ee.printStackTrace();
				}
			}
		}

		AbstractJob job = new CommunicateJob();
		job.setCurrentJobInfo(jobInfo);
		job.setLogger(CommGateway.getMainLogger());

		// 注册任务到任务管理器
		JobManager.addJobSchedule(job);
	}

	protected void processByClass(ActionConfig actionConfig, String sessionId) {
		AbstractEventAction action = (AbstractEventAction) ClassUtil.createClassInstance(actionConfig.getClazz());
		action.setLogger(CommGateway.getMainLogger());
		action.execute(sessionId);
	}

	protected void updateJobLog(Session session, String state) {
		// 更新任务日志状态及关联日志ID
		JobLog jobLog = session.getJob().getCurrentJobLog();
		jobLog.setState(state);
		jobLog.setLogId(session.getId());

		JobLogDAO jobLogDAO = new JobLogDAO();
		Connection conn = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			jobLogDAO.setConnection(conn);
			jobLogDAO.update(jobLog);
		} catch (Exception e) {
			// e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (Exception ee) {
					// ee.printStackTrace();
				}
			}
		}
	}
}