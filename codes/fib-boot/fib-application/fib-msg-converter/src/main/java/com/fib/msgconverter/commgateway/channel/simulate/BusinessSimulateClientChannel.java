package com.fib.msgconverter.commgateway.channel.simulate;

import java.io.InputStream;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.Map;

import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.simulate.config.BusinessSimulateClientChannelConfig;
import com.fib.msgconverter.commgateway.dao.businesssimulateresponsedata.dao.BusinessSimulateResponseData;
import com.fib.msgconverter.commgateway.dao.businesssimulateresponsedata.dao.BusinessSimulateResponseDataDAO;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.giantstone.common.database.ConnectionManager;

public class BusinessSimulateClientChannel extends Channel {
	private LinkedList waitQueue = null;
	private boolean run = true;
	private BusinessSimulateClientChannelConfig config = null;
	private RequestHandler[] handlers = null;

	private static final String SERIAL_ID = "SerialId";

	public void closeSource(Object source) {
		// need nothing
	}

	public void loadConfig(InputStream in) {
		BusinessSimulateClientChannelConfigParser parser = new BusinessSimulateClientChannelConfigParser();
		config = parser.parse(in);
	}

	public void sendRequestMessage(byte[] requestMessage, boolean isASync,
			int timeout) {
		if (!isASync) {
			synchronized (waitQueue) {
				waitQueue.add(requestMessage);
				waitQueue.notify();
			}
		}

		onRequestMessageSent("BusinessSimulateClientChannel", requestMessage);
	}

	public void sendResponseMessage(byte[] responseMessage) {
		// need nothing
	}

	public void shutdown() {
		run = false;
		for (int i = 0; i < handlers.length; i++) {
			try {
				handlers[i].join(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				handlers[i].interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		run = true;

		waitQueue = new LinkedList();
		handlers = new RequestHandler[config.getMaxNumber()];
		for (int i = 0; i < handlers.length; i++) {
			handlers[i] = new RequestHandler();
			handlers[i].start();
		}
	}

	private byte[] getRequest() {
		synchronized (waitQueue) {
			if (0 == waitQueue.size()) {
				try {
					waitQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (0 == waitQueue.size()) {
				return null;
			}

			return (byte[]) waitQueue.remove();
		}
	}

	private class RequestHandler extends Thread {
		public void run() {
			while (run) {
				byte[] reqMessage = getRequest();

				Session session = SessionManager.getSession(reqMessage);
				if (null == session) {
					onResponseMessageReceiveException(
							"BusinessSimulateClientChannel", reqMessage,
							new RuntimeException("Can not find session!"));
					continue;
				}

				Map map = (Map) session.getSourceRequestObject();
				String serialId = (String) map.get(SERIAL_ID);
				if (null == serialId) {
					onResponseMessageReceiveException(
							"BusinessSimulateClientChannel", reqMessage,
							new RuntimeException("Can not find '" + SERIAL_ID
									+ "' in map!"));
					continue;
				}

				Connection conn = null;
				try {
					conn = ConnectionManager.getInstance().getConnection();
					BusinessSimulateResponseDataDAO dao = new BusinessSimulateResponseDataDAO();
					dao.setConnection(conn);

					BusinessSimulateResponseData data = dao
							.selectByPK(serialId);
					if (null == data) {
						onResponseMessageReceiveException(
								"BusinessSimulateClientChannel",
								reqMessage,
								new RuntimeException(
										"Can not find data record in table 'business_simulate_response_data' with serialId '"
												+ serialId + "'"));
						continue;
					}

					onResponseMessageArrived("BusinessSimulateClientChannel",
							reqMessage, data.getMessageData());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				} finally {
					if (null != conn) {
						try {
							conn.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}
		}
	}

}
