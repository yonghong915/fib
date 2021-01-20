package com.fib.gateway.message.xml.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fib.gateway.CommGateway;
import com.fib.gateway.message.util.ExceptionUtil;
import com.fib.gateway.message.xml.channel.Channel;
import com.fib.gateway.session.Session;
import com.fib.gateway.session.SessionManager;

import lombok.Data;
@Data
public class EventHandlerThread extends Thread {
	private Logger logger = LoggerFactory.getLogger(EventHandlerThread.class);
	protected EventQueue queue = null;

	/**
	 * 运行标志
	 */
	protected boolean run = true;

	@Override
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
				logger.error("EventHandlerThread:run Cause Exception [" + e + "] ["
						+ ExceptionUtil.getExceptionDetail(e) + "]");
				logger.error("EventHandlerThread:run Cause Exception :Event [" + event + " ]");

			}

		}
	}

	protected void handleEvent(Event event) {
		logger.info("处理事件");
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
		//eventHandler.setLogger(logger);

		switch (event.getEventType()) {
		// Error Event
		case Event.EVENT_ACCEPT_ERROR:
		case Event.EVENT_REQUEST_RECEIVE_ERROR:
			logEvent(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_REQ_RECV,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_FAILED,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_REQUEST_SEND_ERROR:
			// session.setErrorMessage("Request Message Send Error!SourceChannel["
			// + event.getSourceChannel().getId() + "]");
//			session.setErrorMessage(MultiLanguageResourceBundle.getInstance().getString(
//					"EventHandlerThread.sendRequest.error", new String[] { event.getSourceChannel().getId() }));
//			session.setException(event.getExcp());
//			session.setErrorType(SessionConstants.STATE_REQ_MSG_SEND);
			eventHandler.handleException(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_REQ_SEND,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_FAILED,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_CONNECT_ERROR:
			//session.setErrorType(SessionConstants.STATE_DST_CHN_CONNECT);
			// session
			// .setErrorMessage(
			// "Connect to Destination Error!Destination Channel["
			// + event.getSourceChannel().getId() + "]");
//			session.setErrorMessage(MultiLanguageResourceBundle.getInstance().getString(
//					"EventHandlerThread.connectDest.error", new String[] { event.getSourceChannel().getId() }));
//			session.setException(event.getExcp());
			eventHandler.handleException(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_REQ_SEND,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_FAILED,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_RESPONSE_RECEIVE_ERROR:
			//session.setErrorType(SessionConstants.STATE_RSP_MSG_RECEIVE);
			// session
			// .setErrorMessage(
			// "Reponse Message Receive Error!Destination Channel["
			// + event.getSourceChannel().getId() + "]");
//			session.setErrorMessage(MultiLanguageResourceBundle.getInstance().getString(
//					"EventHandlerThread.receiveResponse.error", new String[] { event.getSourceChannel().getId() }));
//			session.setException(event.getExcp());
			eventHandler.handleException(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_RES_RECV,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_FAILED,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_EXCEPTION:
//			session.setErrorType(SessionConstants.STATE_UNKOWN_ERROR);
//			// session.setErrorMessage("Some Exception Occurred!");
//			session.setErrorMessage(
//					MultiLanguageResourceBundle.getInstance().getString("EventHandlerThread.exception"));
//			session.setException(event.getExcp());
			eventHandler.handleException(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(),
//						Constants.CHANNEL_ACTION_INTERNAL_TRANSFER, null == session ? null : session.getId(),
//						Constants.CHANNEL_ACTION_STATE_FAILED, null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_RESPONSE_SEND_ERROR:
			eventHandler.handleResponseSendError(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_RES_SEND,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_FAILED,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_REQUEST_SENT:
			// 请求报文已发送
			eventHandler.handleRequestSent(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_REQ_SEND,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_SUCCESS,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_RESPONSE_ARRIVED:
			// 应答报文已到达
			eventHandler.handleResponseArrived(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_RES_RECV,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_SUCCESS,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_REQUEST_ARRIVED:
			// 请求报文到达
			eventHandler.handleRequestArrived(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_REQ_RECV,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_SUCCESS,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		case Event.EVENT_RESPONSE_SENT:
			// 应答报文已发送
			// 1. 取得会话，记录日志
			// 2. 结束会话
			eventHandler.handleResponseSent(event);
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(), Constants.CHANNEL_ACTION_RES_SEND,
//						null == session ? null : session.getId(), Constants.CHANNEL_ACTION_STATE_SUCCESS,
//						null == session ? 0 : session.getStartTime());
//			}
			break;
		default:
//			if (CommGateway.isMonitorSupport()) {
//				MonitorModule.broadcastChannelState(event.getSourceChannel().getId(),
//						event.getSourceChannel().getChannelConfig().getDescription(),
//						Constants.CHANNEL_ACTION_INTERNAL_TRANSFER, null == session ? null : session.getId(),
//						Constants.CHANNEL_ACTION_STATE_FAILED, null == session ? 0 : session.getStartTime());
//			}

		}
	}

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
			logger.error(event.toString());
			break;
		default:
			logger.debug(event.toString());
		}
	}
}
