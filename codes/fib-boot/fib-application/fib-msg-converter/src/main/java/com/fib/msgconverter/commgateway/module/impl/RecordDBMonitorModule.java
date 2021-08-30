package com.fib.msgconverter.commgateway.module.impl;

import java.sql.Connection;
import java.util.Date;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.dao.sessiondigest.dao.SessionDigest;
import com.fib.msgconverter.commgateway.dao.sessiondigest.dao.SessionDigestDAO;
import com.fib.msgconverter.commgateway.module.Module;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.util.ExceptionUtil;

public class RecordDBMonitorModule extends Module {

	public void initialize() {
		CommGateway.setRecordSessionDigest(true);
	}

	public static void recordDB(Session session) {
		Connection conn = null;
		try {
			Channel sourceChannel = session.getSourceChannel();
			Channel destChannel = session.getDestChannel();
			SessionDigest digest = new SessionDigest();
			digest.setId(session.getId());
			digest.setGatewayId(CommGateway.getId());

			Date start = new Date(session.getStartTime());
			Date end = new Date(session.getEndTime());
			digest.setStartDate(SessionManager.DATE_FORMAT.format(start));
			digest.setEndDate(SessionManager.DATE_FORMAT.format(end));
			digest.setStartTime(SessionManager.TIME_FORMAT.format(start));
			digest.setEndTime(SessionManager.TIME_FORMAT.format(end));

			if (0 != session.getRequestSentTime()) {
				Date reqSendDate = new Date(session.getRequestSentTime());
				digest.setRequestSendDate(SessionManager.DATE_FORMAT
						.format(reqSendDate));
				digest.setRequestSendTime(SessionManager.TIME_FORMAT
						.format(reqSendDate));
			}
			if (0 != session.getResponseArrivedTime()) {
				Date resArrivedDate = new Date(session.getResponseArrivedTime());

				digest.setResponseArrivedDate(SessionManager.DATE_FORMAT
						.format(resArrivedDate));
				digest.setResponseArrivedTime(SessionManager.TIME_FORMAT
						.format(resArrivedDate));

			}

			digest.setType(session.getType());
			digest.setState(session.getState());
			digest.setErrorType(session.getErrorType());

			if (null != session.getProcessor()) {
				digest.setProcessorId(session.getProcessor().getId());
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

			digest.setSourceChannelId(sourceChannel.getId());
			digest
					.setSourceChannelName(sourceChannel.getMainConfig()
							.getName());

			if (null != destChannel) {
				digest.setDestChannelId(destChannel.getId());
				digest
						.setDestChannelName(destChannel.getMainConfig()
								.getName());
			}

			SessionDigestDAO dao = new SessionDigestDAO();

			// 插入通讯日志表
			conn = ConnectionManager.getInstance().getConnection();
			dao.setConnection(conn);
			dao.insert(digest);

		} catch (Exception e) {
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

	public void shutdown() {
	}

}
