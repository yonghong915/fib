/**
 * 北京长信通信息技术有限公司
 * 2008-8-26 下午07:45:24
 */
package com.fib.msgconverter.commgateway.event;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.module.impl.MonitorModule;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionConstants;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.commgateway.module.constants.Constants;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 事件处理器线程
 * 
 * @author 刘恭亮
 * 
 */
public class EventHandlerThread extends Thread {

	/**
	 * 事件队列
	 */
	protected EventQueue queue = null;

	/**
	 * 运行标志
	 */
	protected boolean run = true;

	/**
	 * 日志记录器
	 */
	protected Logger logger;

	// public EventHandler(EventQueue queue) {
	// this.queue = queue;
	// }

	/**
	 * 处理事件
	 * 
	 * @param event
	 */
	protected void handleEvent(Event event) {
		EventHandler eventHandler = null;
		Session session = null;
		if (Event.EVENT_REQUEST_ARRIVED == event.getEventType()) {
			eventHandler = event.getSourceChannel().getEventHandler();
		} else {
			if (null != event.getRequestMessage()) {
				session = SessionManager.getSession(event.getRequestMessage());
				if (null == session) {
					// 超时会话被清除
					return;
				}
				Channel sourceChannel = session.getSourceChannel();
				eventHandler = sourceChannel.getEventHandler();
			} else {
				session = SessionManager.getSession(event.getResponseMessage());
				if (null == session) {
					// 超时会话被清除
					return;
				}
				Channel sourceChannel = session.getSourceChannel();
				eventHandler = sourceChannel.getEventHandler();
			}
		}
		eventHandler.setLogger(logger);

		switch (event.getEventType()) {
		// Error Event
		case Event.EVENT_ACCEPT_ERROR:
		case Event.EVENT_REQUEST_RECEIVE_ERROR:
			logEvent(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_REQ_RECV,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_REQUEST_SEND_ERROR:
			//session.setErrorMessage("Request Message Send Error!SourceChannel["
			// + event.getSourceChannel().getId() + "]");
			session.setErrorMessage(MultiLanguageResourceBundle.getInstance()
					.getString("EventHandlerThread.sendRequest.error",
							new String[] { event.getSourceChannel().getId() }));
			session.setException(event.getExcp());
			session.setErrorType(SessionConstants.STATE_REQ_MSG_SEND);
			eventHandler.handleException(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_REQ_SEND,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_CONNECT_ERROR:
			session.setErrorType(SessionConstants.STATE_DST_CHN_CONNECT);
			// session
			// .setErrorMessage(
			// "Connect to Destination Error!Destination Channel["
			// + event.getSourceChannel().getId() + "]");
			session.setErrorMessage(MultiLanguageResourceBundle.getInstance()
					.getString("EventHandlerThread.connectDest.error",
							new String[] { event.getSourceChannel().getId() }));
			session.setException(event.getExcp());
			eventHandler.handleException(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_REQ_SEND,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_RESPONSE_RECEIVE_ERROR:
			session.setErrorType(SessionConstants.STATE_RSP_MSG_RECEIVE);
			// session
			// .setErrorMessage(
			// "Reponse Message Receive Error!Destination Channel["
			// + event.getSourceChannel().getId() + "]");
			session.setErrorMessage(MultiLanguageResourceBundle.getInstance()
					.getString("EventHandlerThread.receiveResponse.error",
							new String[] { event.getSourceChannel().getId() }));
			session.setException(event.getExcp());
			eventHandler.handleException(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_RES_RECV,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_EXCEPTION:
			session.setErrorType(SessionConstants.STATE_UNKOWN_ERROR);
			// session.setErrorMessage("Some Exception Occurred!");
			session.setErrorMessage(MultiLanguageResourceBundle.getInstance()
					.getString("EventHandlerThread.exception"));
			session.setException(event.getExcp());
			eventHandler.handleException(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(),
						Constants.CHANNEL_ACTION_INTERNAL_TRANSFER,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_RESPONSE_SEND_ERROR:
			eventHandler.handleResponseSendError(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_RES_SEND,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_REQUEST_SENT:
			// 请求报文已发送
			eventHandler.handleRequestSent(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_REQ_SEND,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_SUCCESS,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_RESPONSE_ARRIVED:
			// 应答报文已到达
			eventHandler.handleResponseArrived(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_RES_RECV,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_SUCCESS,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_REQUEST_ARRIVED:
			// 请求报文到达
			eventHandler.handleRequestArrived(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_REQ_RECV,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_SUCCESS,
						null == session ? 0 : session.getStartTime());
			}
			break;
		case Event.EVENT_RESPONSE_SENT:
			// 应答报文已发送
			// 1. 取得会话，记录日志
			// 2. 结束会话
			eventHandler.handleResponseSent(event);
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(), Constants.CHANNEL_ACTION_RES_SEND,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_SUCCESS,
						null == session ? 0 : session.getStartTime());
			}
			break;
		default:
			if (CommGateway.isMonitorSupport()) {
				MonitorModule.broadcastChannelState(event.getSourceChannel()
						.getId(), event.getSourceChannel().getChannelConfig()
						.getDescription(),
						Constants.CHANNEL_ACTION_INTERNAL_TRANSFER,
						null == session ? null : session.getId(),
						Constants.CHANNEL_ACTION_STATE_FAILED,
						null == session ? 0 : session.getStartTime());
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		Event event = null;
		while (run) {
			try {
				// 1. 取得一个事件
				event = queue.selectEvent();
				// 2. 处理事件
				if (null != event) {
					handleEvent(event);
				}
			} catch (Throwable e) {
				// e.printStackTrace();
				logger.error("EventHandlerThread:run Cause Exception [" + e
						+ "] [" + ExceptionUtil.getExceptionDetail(e) + "]");
				logger.error("EventHandlerThread:run Cause Exception :Event ["
						+ event + " ]");

			}

		}
	}

	/**
	 * 记录事件。当Session超时或已结束时调用
	 * 
	 * @param event
	 */
	protected void logEvent(Event event) {
		if (null == logger) {
			System.out.println(event.toString());
			return;
		}
		switch (event.getEventType()) {
		case Event.EVENT_ACCEPT_ERROR:
		case Event.EVENT_REQUEST_RECEIVE_ERROR:
		case Event.EVENT_RESPONSE_RECEIVE_ERROR:
		case Event.EVENT_REQUEST_SEND_ERROR:
		case Event.EVENT_RESPONSE_SEND_ERROR:
		case Event.EVENT_CONNECT_ERROR:
		case Event.EVENT_EXCEPTION:
			logger.error(event.toString());
			break;
		case Event.EVENT_REQUEST_ARRIVED:
		case Event.EVENT_RESPONSE_ARRIVED:
		case Event.EVENT_RESPONSE_SENT:
		case Event.EVENT_REQUEST_SENT:
			logger.info(event.toString());
			break;
		case Event.EVENT_FATAL_EXCEPTION:
			logger.fatal(event.toString());
			break;
		default:
			logger.debug(event.toString());
		}
	}

	/**
	 * @return the run
	 */
	public boolean isRun() {
		return run;
	}

	/**
	 * @param run
	 *            the run to set
	 */
	public void setRun(boolean run) {
		this.run = run;
	}

	/**
	 * @return the queue
	 */
	public EventQueue getQueue() {
		return queue;
	}

	/**
	 * @param queue
	 *            the queue to set
	 */
	public void setQueue(EventQueue queue) {
		this.queue = queue;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
