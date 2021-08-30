/**
 * 北京长信通信息技术有限公司
 * 2008-8-29 上午09:30:54
 */
package com.fib.msgconverter.commgateway.session;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLog;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLogDAO;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessage;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessageDAO;
import com.fib.msgconverter.commgateway.module.impl.MonitorModule;
import com.fib.msgconverter.commgateway.module.impl.RecordDBMonitorModule;
import com.fib.msgconverter.commgateway.session.config.SessionConfig;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.commgateway.util.serialnumber.SerialNumberGenerator;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

/**
 * 会话管理器
 * 
 * @author 刘恭亮
 * 
 */
public class SessionManager {

	public static final SimpleDateFormat FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd  HH:mm:ss SSS");
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HHmmss");

	private static Map sessions = new ConcurrentHashMap(1024);

	private static Logger logger;

	private static Logger clock = Logger.getLogger("clock");

	private static Logger exceptionManagerLogger = Logger
			.getLogger("exception-manager");

	private static SessionConfig sessionConfig;

	private static boolean run = false;

	private static MonitorHandler monitor;

	// add finished
	public static void start() {
		if (null == sessionConfig) {
			// throw new RuntimeException("Please Set SessionConfig First!");
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"SessionManager.sessionConfig.null"));
		}
		if (!run) {
			monitor = new MonitorHandler();
			monitor.start();

			run = true;
		}
	}

	public static Session createSession() {
		Session session = new Session();
		session.setTimeout(sessionConfig.getTimeout());
		if (CommGateway.isDatabaseSupport()) {
			String id = SerialNumberGenerator.getInstance().next("SESSION");

			for (int i = id.length(); i < 20; i++) {
				id = "0" + id;
			}
			session.setId(id);
		}

		session.sessionCreateTime = System.nanoTime();
		return session;
	}

	public static void stop() {
		if (!run) {
			return;
		}
		run = false;
		try {
			monitor.interrupt();
			monitor.join(6000);
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		monitor = null;

		Iterator iterator = getDistinctSessions().iterator();
		while (iterator.hasNext()) {
			Session session = (Session) iterator.next();
			// session.setErrorMessage("Gateway Closed!");
			session.setErrorMessage(MultiLanguageResourceBundle.getInstance()
					.getString("SessionManager.gateway.closed"));
			session.setState(Session.STATE_UNFINISHED);
			terminalSession(session);
		}

		sessions.clear();
	}

	/**
	 * 根据关联对象取得会话
	 * 
	 * @param correlative
	 *            关联对象
	 * @return
	 */
	public static Session getSession(Object correlative) {
		Session session = null;
		synchronized (sessions) {
			session = (Session) sessions.get(correlative);
			if (null != session) {
				session.setLastAliveTime(System.currentTimeMillis());
			}
		}
		if (null != session && !Session.STATE_CANCEL.equals(session.getState())) {
			return session;
		} else {
			return null;
		}
	}

	/**
	 * 建立关联对象和会话的关系
	 * 
	 * @param correlative
	 *            关联对象
	 * @param session
	 */
	public static void attachSession(Object correlative, Session session) {
		synchronized (sessions) {
			session.setLastAliveTime(System.currentTimeMillis());
			sessions.put(correlative, session);
		}
	}

	/**
	 * 删除会话及其与所有关联对象的关系
	 * 
	 * @param session
	 */
	public static void removeSession(Session session) {
		synchronized (sessions) {
			List keys = new ArrayList();
			Map.Entry en = null;
			Iterator it = sessions.entrySet().iterator();
			while (it.hasNext()) {
				en = (Entry) it.next();
				if (session == en.getValue()) {
					// sessions.remove(en.getKey());
					keys.add(en.getKey());
				}
			}
			for (int i = 0; i < keys.size(); i++) {
				sessions.remove(keys.get(i));
			}
		}
	}

	public static void setSessionConfig(SessionConfig newSessionConfig) {
		sessionConfig = newSessionConfig;
	}

	public static SessionConfig getSessionConfig() {
		return sessionConfig;
	}

	private static class MonitorHandler extends Thread {
		public void run() {
			while (run) {
				// if (0 == sessions.size()) {
				int size = 0;
				synchronized (sessions) {
					size = sessions.size();
				}
				if (0 == size) {
					try {
						Thread.sleep(sessionConfig.getTimeout());
					} catch (InterruptedException e) {
						// e.printStackTrace();
					}
					continue;
				}
				// }
				Set set = new HashSet();
				synchronized (sessions) {
					Iterator iterator = sessions.keySet().iterator();
					while (iterator.hasNext()) {
						Object key = iterator.next();
						Session session = (Session) sessions.get(key);
						if (System.currentTimeMillis()
								- session.getLastAliveTime() >= session
								.getTimeout()) {
							session.setState(Session.STATE_CANCEL);
							// logger.error("Session Timeout! Session: \n"
							// + session.toString());
							set.add(key);
						}
					}

					iterator = set.iterator();
					while (iterator.hasNext()) {
						Object key = iterator.next();
						Session session = (Session) sessions.get(key);
						sessions.remove(key);
						if (null != session) {
							terminalSession(session);
						}
					}
				}

				try {
					Thread.sleep(sessionConfig.getTimeout() / 2);
				} catch (InterruptedException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private static void log(Session session) {
		String serialString = session.toString4ExceptionManager();
		String sessionString = null;
		if (CommGateway.isShieldSensitiveFields()) {
			sessionString = session.toString4ShieldSensitiveInfo();
		} else {
			sessionString = session.toString();
			;
		}
		if (null == exceptionManagerLogger) {
			System.out.println(serialString);
			return;
		}
		if (Session.STATE_SUCCESS.equals(session.getState())) {
			exceptionManagerLogger.info(serialString);
		} else {
			exceptionManagerLogger.error(serialString);
		}

		if (null == logger) {
			System.out.println(sessionString);
			return;
		}
		if (Session.STATE_SUCCESS.equals(session.getState())) {
			logger.info(sessionString);
		} else {
			logger.error(sessionString);
		}
		String clockString = session.toString4TimeDebugRecord();
		if (null == clock) {
			System.out.println(clockString);
			return;
		}
		if (Session.STATE_SUCCESS.equals(session.getState())) {
			clock.info(clockString);
		} else {
			clock.error(clockString);
		}

	}

	private static void closeConnection(Object source, Channel channel) {

		if (null == source || null == channel) {
			return;
		}
		channel.closeSource(source);
	}

	public static final String UDP_SEND_ENCODING = "UTF-8";

	public static void terminalSession(Session session) {
		session.setEndTime(System.currentTimeMillis());
		session.setEndNanoTime(System.nanoTime());
		// if (Session.STATE_CANCEL.equals(session.getState())) {
		// logger.error(MultiLanguageResourceBundle.getInstance().getString(
		// "SessionManager.session.timeout",
		// new String[] { session.toString() }));
		// } else {
		log(session);
		// }
		SessionManager.removeSession(session);

		// // 关闭连接
		closeConnection(session.getSource(), session.getSourceChannel());
		closeConnection(session.getDestSource(), session.getDestChannel());

		if (CommGateway.isDatabaseSupport()) {
			if (SessionConfig.SESSION_INTO_DB_LEVEL_SUCCESS_TXT
					.equals(sessionConfig.getLevelIntoDBText())
					|| !Session.STATE_SUCCESS.equals(session.getState())) {
				recordSessionLog(session);
			}
		}

		if (CommGateway.isRecordSessionDigest()) {
			try {
				RecordDBMonitorModule.recordDB(session);
			} catch (Exception e) {
				if (null != logger) {
					logger
							.error(
									MultiLanguageResourceBundle
											.getInstance()
											.getString(
													"SessionManager.recordSessionDigest.failed"),
									e);
				}
			}
		}

		if (CommGateway.isMonitorSupport()) {
			MonitorModule.broadcastSessionInfo(session);
		}
	}

	/**
	 * 数据库记录会话日志
	 * 
	 * @param session
	 */
	private static void recordSessionLog(Session session) {
		Channel sourceChannel = session.getSourceChannel();
		Channel destChannel = session.getDestChannel();
		CommLog log = new CommLog();

		Date start = new Date(session.getStartTime());
		Date end = new Date(session.getEndTime());

		log.setStartDate(DATE_FORMAT.format(start));
		log.setGatewayId(CommGateway.getId());

		log.setEndDate(DATE_FORMAT.format(end));

		// session.setId(log.getEndDate() + id);
		log.setId(session.getId());
		// log.setExternalSerialNum(session.getExternalSerialNum());

		log.setStartTime(TIME_FORMAT.format(start));
		log.setEndTime(TIME_FORMAT.format(end));

		log.setType(session.getType());
		log.setState(session.getState());
		log.setErrorType(session.getErrorType());

		if (null != session.getProcessor()) {
			log.setProcessorId(session.getProcessor().getId());
		}

		String errorMessage = session.getErrorMessage();
		if (null != session.getException()) {
			if (null == errorMessage) {
				errorMessage = ExceptionUtil.getExceptionDetail(session
						.getException());
			} else {
				errorMessage = errorMessage
						+ ":"
						+ ExceptionUtil.getExceptionDetail(session
								.getException());
			}
		}

		log.setSourceChannelId(sourceChannel.getId());
		log.setSourceChannelName(sourceChannel.getMainConfig().getName());
		// log.setSourceRequestMessage(session.getSourceRequestMessage());
		// log.setSourceResponseMessage(session.getSourceResponseMessage());

		if (null != destChannel) {
			log.setDestChannelId(destChannel.getId());
			log.setDestChannelName(destChannel.getMainConfig().getName());
			// log.setDestRequestMessage(session.getDestRequestMessage());
			// log.setDestResponseMessage(session.getDestResponseMessage());
		}

		CommLogMessage message = new CommLogMessage();
		message.setLogId(session.getId());

		Connection conn = null;
		CommLogDAO dao = new CommLogDAO();
		CommLogMessageDAO messageDao = new CommLogMessageDAO();
		try {
			// 插入通讯日志表
			conn = ConnectionManager.getInstance().getConnection();
			dao.setConnection(conn);
			dao.insert(log);

			messageDao.setConnection(conn);
			if (null != session.getSourceRequestMessage()) {
				// 源请求报文插入通讯日志报文表
				message.setMessageType(SessionConstants.SRC_REQ);
				message.setMessage(session.getSourceRequestMessage());
				messageDao.insert(message);
			}
			if (null != session.getSourceResponseMessage()) {
				// 源回应报文插入通讯日志报文表
				message.setMessageType(SessionConstants.SRC_RES);
				message.setMessage(session.getSourceResponseMessage());
				messageDao.insert(message);
			}
			if (null != session.getDestRequestMessage()) {
				// 目的请求报文插入通讯日志报文表
				message.setMessageType(SessionConstants.DST_REQ);
				message.setMessage(session.getDestRequestMessage());
				messageDao.insert(message);
			}
			if (null != session.getDestResponseMessage()) {
				// 目的回应报文插入通讯日志报文表
				message.setMessageType(SessionConstants.DST_RES);
				message.setMessage(session.getDestResponseMessage());
				messageDao.insert(message);
			}

			if (null != errorMessage) {
				// 错误信息插入通讯日志报文表
				message.setMessageType(SessionConstants.ERR_MSG);
				message.setMessage(errorMessage.getBytes());
				messageDao.insert(message);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			if (logger != null) {
				// logger.error("record failed session to database failed :",
				// e);
				logger.error(MultiLanguageResourceBundle.getInstance()
						.getString("SessionManager.recordSession.failed"), e);
			}
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

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SessionManager.logger = logger;
	}

	public static Set getDistinctSessions() {
		Set set = new HashSet();

		Iterator<Map.Entry> iter = sessions.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry en = iter.next();
			if (!set.contains(en.getValue())) {
				set.add(en.getValue());
			}
		}

		return set;
	}

	public static Map getSessions() {
		return sessions;
	}

	public static void main(String[] args) {

	}
}
