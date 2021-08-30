package com.fib.msgconverter.commgateway.module.impl;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.jgroups.Address;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.protocols.TP;
import org.jgroups.protocols.UDP;

import com.fib.msgconverter.commgateway.CommGateway;
import com.fib.msgconverter.commgateway.channel.Channel;
import com.fib.msgconverter.commgateway.channel.config.processor.Processor;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLog;
import com.fib.msgconverter.commgateway.dao.commlog.dao.CommLogDAO;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessage;
import com.fib.msgconverter.commgateway.dao.commlogmessage.dao.CommLogMessageDAO;
import com.fib.msgconverter.commgateway.dao.cronrule.dao.CronRule;
import com.fib.msgconverter.commgateway.dao.cronrule.dao.CronRuleDAO;
import com.fib.msgconverter.commgateway.dao.job.dao.Job;
import com.fib.msgconverter.commgateway.dao.job.dao.JobDAO;
import com.fib.msgconverter.commgateway.dao.monitoraccount.dao.MonitorAccount;
import com.fib.msgconverter.commgateway.dao.monitoraccount.dao.MonitorAccountDAO;
import com.fib.msgconverter.commgateway.job.JobConstants;
import com.fib.msgconverter.commgateway.job.JobManager;
import com.fib.msgconverter.commgateway.module.Module;
import com.fib.msgconverter.commgateway.module.constants.Constants;
import com.fib.msgconverter.commgateway.module.util.SqlUtil;
import com.fib.msgconverter.commgateway.session.Session;
import com.fib.msgconverter.commgateway.session.SessionConstants;
import com.fib.msgconverter.commgateway.session.SessionManager;
import com.fib.msgconverter.commgateway.util.multilang.MultiLanguageResourceBundle;
import com.fib.msgconverter.commgateway.util.serialnumber.SerialNumberGenerator;
import com.giantstone.common.database.ConnectionManager;
import com.giantstone.common.map.serializer.MapSerializer;
import com.giantstone.common.util.ByteBuffer;
import com.giantstone.common.util.CodeUtil;
import com.giantstone.common.util.ExceptionUtil;
import com.giantstone.security.dao.common.encryptor.Encryptor;
import com.giantstone.security.dao.common.encryptor.EncryptorDAO;
import com.giantstone.security.dao.common.secretkey.SecretKey;
import com.giantstone.security.dao.common.secretkey.SecretKeyDAO;
import com.giantstone.security.dao.common.securityarithmetic.SecurityArithmetic;
import com.giantstone.security.dao.common.securityarithmetic.SecurityArithmeticDAO;
import com.giantstone.security.dao.common.securitycontrolrule.SecurityControlRule;
import com.giantstone.security.dao.common.securitycontrolrule.SecurityControlRuleDAO;
import com.giantstone.security.dao.common.securityrulebussinessrelation.SecurityRuleBussinessRelation;
import com.giantstone.security.dao.common.securityrulebussinessrelation.SecurityRuleBussinessRelationDAO;
import com.giantstone.security.dao.common.serviceparameters.ServiceParameters;
import com.giantstone.security.dao.common.serviceparameters.ServiceParametersDAO;

/**
 * 监控模块
 * 
 */
public class MonitorModule extends Module {

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss SSS");
	private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");
	private static final SimpleDateFormat DAY_TIME_FORMAT = new SimpleDateFormat(
			"HHmmss");
	private static final String SERIAL_NUM_SECRET_KEY = "SECRET_KEY";
	private static final String SERIAL_NUM_ENCRYPTOR = "ENCRYPTOR";
	private static final String SERIAL_NUM_SECURITY_CTRL_RULE = "SECURITY_CONTROL_RULE";
	private static final String SERIAL_NUM_SECURITY_ARITHMETIC = "SECURITY_ARITHMETIC";
	private static final String SERIAL_NUM_SERVICE_PARAMETER = "SERVICE_PARAMETER";

	private static JChannel channel = null;
	private static String accountName = null;
	private static String password = null;

	public void initialize() {

		String configFileName = null;
		String clusterName = null;
		if (null != getParameters()) {
			configFileName = (String) getParameters().get(
					Constants.CONFIG_FILE_NAME);
			clusterName = (String) getParameters().get(Constants.CLUSTER_NAME);
		}
		if (null == configFileName) {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"GatewayConfigFile.module.parameter.null",
							new String[] { "MONITOR_MODULE",
									Constants.CONFIG_FILE_NAME }));
		}
		if (null == clusterName) {
			throw new RuntimeException(MultiLanguageResourceBundle
					.getInstance().getString(
							"GatewayConfigFile.module.parameter.null",
							new String[] { "MONITOR_MODULE",
									Constants.CLUSTER_NAME }));
		}
		configFileName = configFileName.trim();
		clusterName = clusterName.trim();

		System.setProperty("java.net.preferIPv4Stack", "true");

		accountName = (String) getParameters().get(Constants.ACCOUNT_NAME);
		password = (String) getParameters().get(Constants.PASSWORD);

		try {
			channel = new JChannel(configFileName);
		} catch (ChannelException e) {
			e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		}
		channel.setOpt(org.jgroups.Channel.LOCAL, false);
		channel.setReceiver(new MonitorMessageReceiver());
		try {
			channel.connect(clusterName);
		} catch (ChannelException e) {
			e.printStackTrace();
			if (null != channel) {
				channel.close();
			}
			ExceptionUtil.throwActualException(e);
		}

		CommGateway.setMonitorSupport(true);
	}

	public static void broadcastSessionInfo(Session session) {
		Map map = new HashMap();
		if (null != session.getId()) {
			map.put(Constants.BROADCAST_SESSION_ID, session.getId());
		}
		map.put(Constants.BROADCAST_SESSION_START_TIME, TIME_FORMAT
				.format(new Date(session.getStartTime())));
		map.put(Constants.BROADCAST_SESSION_END_TIME, TIME_FORMAT
				.format(new Date(session.getEndTime())));
		map.put(Constants.BROADCAST_SESSION_STATE, session.getState());

		if (null != session.getErrorType()) {
			map.put(Constants.BROADCAST_SESSION_ERROR_TYPE, session
					.getErrorType());
		}
		try {
			map.put(Constants.BROADCAST_SESSION_INFO, session.toString()
					.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Map msg = new HashMap();
		msg.put(Constants.RET_TYPE, Constants.TYPE_SESSION_INFO);
		msg.put(Constants.RET_PARAMS, map);

		broadcast(MapSerializer.serialize(msg));
	}

	public static void broadcastChannelState(String channelId,
			String channelName, String action, String sessionId, String state,
			long time) {
		Map map = new HashMap();
		map.put(Constants.BROADCAST_CHANNEL_ID, channelId);
		map.put(Constants.BROADCAST_CHANNEL_NAME, channelName);
		map.put(Constants.BROADCAST_CHANNEL_ACTION, action);
		map.put(Constants.BROADCAST_SESSION_ID, sessionId);
		map.put(Constants.BROADCAST_CHANNEL_ACTION_STATE, state);
		if (0 != time) {
			map.put(Constants.BROADCAST_SESSION_START_TIME, TIME_FORMAT
					.format(new Date(time)));
		} else {
			map.put(Constants.BROADCAST_SESSION_START_TIME, "");
		}

		Map msg = new HashMap();
		msg.put(Constants.RET_TYPE, Constants.TYPE_CHANNEL_STATE);
		msg.put(Constants.RET_PARAMS, map);

		broadcast(MapSerializer.serialize(msg));

	}

	private static void broadcast(String message) {
		Message msg = new Message();
		try {
			msg.setBuffer(message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		synchronized (channel) {
			try {
				channel.send(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void reply(Message reqMsg, String resMsg) {
		Message message = reqMsg.makeReply();
		try {
			message.setBuffer(resMsg.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		synchronized (channel) {
			try {
				channel.send(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void shutdown() {
		if (null != channel) {
			Map map = new HashMap();
			map.put(Constants.RET_TYPE, Constants.TYPE_GW_STOP);

			broadcast(MapSerializer.serialize(map));
			channel.close();
		}

	}

	// public static void setMsg2

	public static byte[] checkMonitorLogin(String msg) {
		int index = msg.indexOf(Constants.CMD_SEPERATOR);
		if (-1 == index) {
			return returnLoginFailedMessage("报文格式错误!");
		}
		String name = msg.substring(0, index);
		String password = msg.substring(index + 1);

		// if (32 < name.length()) {
		// return returnFailedMessage("account name format is wrong!");
		// }
		// if (32 < password.length()) {
		// return returnFailedMessage("account password format is wrong!");
		// }
		if (null != accountName) {
			if (!name.equals(accountName)
					|| !password.equals(MonitorModule.password)) {
				return returnLoginFailedMessage("用户名或密码错误!");
			}
		} else {
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				MonitorAccountDAO dao = new MonitorAccountDAO();
				dao.setConnection(conn);

				MonitorAccount account = dao.selectByPK(name);
				if (null == account
						|| !password.equals(account.getAccountPassword())) {
					return returnLoginFailedMessage("用户名或密码错误!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return returnLoginFailedMessage("登录失败!");
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
		return returnLoginSuccessMessage();
	}

	private static byte[] returnLoginFailedMessage(String errorInfo) {
		ByteBuffer buf = new ByteBuffer();
		buf.append(Constants.LOGIN_FAILED);
		byte[] errorInfoBytes = errorInfo.getBytes();
		buf.append(CodeUtil.IntToBytes(errorInfoBytes.length));
		buf.append(errorInfoBytes);
		byte[] msgBody = buf.toBytes();
		byte[] msg = new byte[msgBody.length + 4];
		System.arraycopy(CodeUtil.IntToBytes(msgBody.length), 0, msg, 0, 4);
		System.arraycopy(msgBody, 0, msg, 4, msgBody.length);
		return msg;
	}

	private static byte[] returnLoginSuccessMessage() {
		ByteBuffer buf = new ByteBuffer();
		buf.append(Constants.LOGIN_SUCCESS);

		TP tp = channel.getProtocolStack().getTransport();
		if (tp instanceof UDP) {
			buf.append(Constants.PROTOCOL_UDP);
			byte[] bindAddress = tp.getBindAddress().getBytes();
			buf.append(CodeUtil.IntToBytes(bindAddress.length));
			buf.append(bindAddress);
		} else {
			buf.append(Constants.PROTOCOL_TCP);
			buf.append(new byte[] { 0x00, 0x00, 0x00, 0x00 });
		}
		buf.append(CodeUtil.IntToBytes(tp.getBindPort()));

		byte[] clusterName = channel.getClusterName().getBytes();
		buf.append(CodeUtil.IntToBytes(clusterName.length));
		buf.append(clusterName);

		Address address = channel.getAddress();

		byte[] serializedAddress = null;
		ByteArrayOutputStream out = null;
		ObjectOutputStream objOut = null;
		try {
			out = new ByteArrayOutputStream();
			objOut = new ObjectOutputStream(out);
			objOut.writeObject(address);
			serializedAddress = out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionUtil.throwActualException(e);
		} finally {
			if (null != out) {
				try {
					out.flush();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != objOut) {
				try {
					objOut.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		buf.append(CodeUtil.IntToBytes(serializedAddress.length));
		buf.append(serializedAddress);

		byte[] msgBody = buf.toBytes();
		byte[] msg = new byte[msgBody.length + 4];
		System.arraycopy(CodeUtil.IntToBytes(msgBody.length), 0, msg, 0, 4);
		System.arraycopy(msgBody, 0, msg, 4, msgBody.length);
		return msg;
	}

	private static class MonitorMessageReceiver extends ReceiverAdapter {
		public void receive(Message msg) {
			String message = (String) msg.getObject();
			System.out.println(message);

			Map map = MapSerializer.deserialize(message);
			String type = (String) map.get(Constants.RET_TYPE);
			Map req = (Map) map.get(Constants.RET_PARAMS);

			// TODO
			Map ret = new HashMap();
			Map params = null;
			ret.put(Constants.RET_TYPE, type);
			if (Constants.TYPE_COMM_LOG_INFO.equals(type)) {
				params = searchCommLog(req);
			} else if (Constants.TYPE_RESEND.equals(type)) {
				params = resend(req);
			} else if (Constants.TYPE_COMM_LOG_MSG.equals(type)) {
				params = searchCommLogMsg(req);
			} else if (Constants.TYPE_CHECK_SESSION.equals(type)) {
				params = checkSession(req);
			} else if (Constants.TYPE_END_SESSION.equals(type)) {
				params = endSession(req);
			} else if (Constants.TYPE_ADD_JOB.equals(type)) {
				params = addJob(req);
			} else if (Constants.TYPE_DEL_JOB.equals(type)) {
				params = delJob(req);
			} else if (Constants.TYPE_CANCEL_JOB.equals(type)) {
				params = cancelJob(req);
			} else if (Constants.TYPE_START_JOB.equals(type)) {
				params = startJob(req);
			} else if (Constants.TYPE_ADD_SECURITY.equals(type)) {
				params = addSecurity(req);
			} else if (Constants.TYPE_DEL_SECURITY.equals(type)) {
				params = delSecurity(req);
			} else if (Constants.TYPE_SEARCH_SECURITY.equals(type)) {
				params = searchSecurity(req);
			} else if (Constants.TYPE_MODIFY_SECURITY.equals(type)) {
				params = modifySecurity(req);
			}

			ret.put(Constants.RET_PARAMS, params);
			reply(msg, MapSerializer.serialize(ret));
		}

		// TODO
		private Map modifySecurity(Map map) {
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();

				if (null != map.get(Constants.SECURITY_RULE_BUSS_RELATION)) {
					updateSecurityRuleBusinessRelation((Map) map
							.get(Constants.SECURITY_RULE_BUSS_RELATION), conn);
				}

				if (null != map.get(Constants.SECURITY_CTRL_RULE)) {
					updateSecurityControlRule((Map) map
							.get(Constants.SECURITY_CTRL_RULE), conn);
				}

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				return returnFailedMap("修改安全规则失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return returnSuccessMap();
		}

		private void updateSecurityControlRule(Map map, Connection conn) {
			SecurityControlRule dto = new SecurityControlRule();
			String ruleId = (String) map.get(Constants.SECURITY_CTRL_RULE_ID);
			if (null == ruleId) {
				throw new RuntimeException("安全控制规则ID为空!");
			}
			dto.setId(ruleId);
			if (null != map.get(Constants.SECURITY_CTRL_RULE_MODE)) {
				dto
						.setMode((String) map
								.get(Constants.SECURITY_CTRL_RULE_MODE));
			}
			if (null != map
					.get(Constants.SECURITY_CTRL_RULE_CHECK_KEY_CHECK_VALUE)) {
				dto
						.setCheckKeycheckvalue((String) map
								.get(Constants.SECURITY_CTRL_RULE_CHECK_KEY_CHECK_VALUE));
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_ENCRYPTOR_ID)) {
				dto.setEncryptorId((String) map
						.get(Constants.SECURITY_CTRL_RULE_ENCRYPTOR_ID));
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_GEN_WK_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_GEN_WK_FUNC),
						"生成工作密钥", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_MAC_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_MAC_FUNC), "加MAC",
						conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_MEMO)) {
				dto
						.setMemo((String) map
								.get(Constants.SECURITY_CTRL_RULE_MEMO));
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_MSG_DECRYPT_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_MSG_DECRYPT_FUNC),
						"报文解密", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_MSG_ENCRYPT_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_MSG_ENCRYPT_FUNC),
						"报文加密", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_PIN_DECRYPT_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_PIN_DECRYPT_FUNC),
						"PIN解密", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_PIN_ENCRYPT_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_PIN_ENCRYPT_FUNC),
						"PIN加密", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_PIN_EXCHANGE_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_PIN_EXCHANGE_FUNC),
						"PIN转换", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_SECRET_KEY_ID)) {
				dto.setSecretKeyId((String) map
						.get(Constants.SECURITY_CTRL_RULE_SECRET_KEY_ID));
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_UPDATE_KEY_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_UPDATE_KEY_FUNC),
						"更新密钥", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_VERIFY_CVV_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_CVV_FUNC),
						"校验CVV", conn);
			}
			if (null != map
					.get(Constants.SECURITY_CTRL_RULE_VERIFY_CVVPVV_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_CVVPVV_FUNC),
						"校验CVVPVV", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC),
						"校验MAC", conn);
			}
			if (null != map.get(Constants.SECURITY_CTRL_RULE_VERIFY_PVV_FUNC)) {
				updateSecurityArithmetic((Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_PVV_FUNC),
						"校验PVV", conn);
			}
			if (null != map.get(Constants.ENCRYPTOR)) {
				updateEncryptor((Map) map.get(Constants.ENCRYPTOR), conn);
			}
			if (null != map.get(Constants.SECRET_KEY)) {
				updateSecretKey((Map) map.get(Constants.SECRET_KEY), conn);
			}

			SecurityControlRuleDAO dao = new SecurityControlRuleDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.update(dto);
		}

		private void updateSecretKey(Map map, Connection conn) {
			String id = (String) map.get(Constants.SECRET_KEY_ID);
			if (null == id) {
				throw new RuntimeException("密钥ID为空!");
			}

			SecretKey dto = new SecretKey();
			dto.setId(id);
			if (null != map.get(Constants.SECRET_KEY_APP_KEY1)) {
				dto.setAppKey1((String) map.get(Constants.SECRET_KEY_APP_KEY1));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_KEY2)) {
				dto.setAppKey2((String) map.get(Constants.SECRET_KEY_APP_KEY2));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MAC_KEY)) {
				dto.setAppMacKey((String) map
						.get(Constants.SECRET_KEY_APP_MAC_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MAC_KEY_REPLACE)) {
				dto.setAppMacKeyReplace((String) map
						.get(Constants.SECRET_KEY_APP_MAC_KEY_REPLACE));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MAC_KEY_STATE)) {
				dto.setAppMacKeyState((String) map
						.get(Constants.SECRET_KEY_APP_MAC_KEY_STATE));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MASTER_KEY)) {
				dto.setAppMasterKey((String) map
						.get(Constants.SECRET_KEY_APP_MASTER_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MASTER_KEY_INDEX)) {
				dto.setAppMasterKeyIndex((String) map
						.get(Constants.SECRET_KEY_APP_MASTER_KEY_INDEX));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MSG_KEY)) {
				dto.setAppMsgKey((String) map
						.get(Constants.SECRET_KEY_APP_MSG_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MSG_KEY_REPLACE)) {
				dto.setAppMsgKeyReplace((String) map
						.get(Constants.SECRET_KEY_APP_MSG_KEY_REPLACE));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_MSG_KEY_STATE)) {
				dto.setAppMsgKeyState((String) map
						.get(Constants.SECRET_KEY_APP_MSG_KEY_STATE));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_PIN_KEY)) {
				dto.setAppPinKey((String) map
						.get(Constants.SECRET_KEY_APP_PIN_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_PIN_KEY_REPLACE)) {
				dto.setAppPinKeyReplace((String) map
						.get(Constants.SECRET_KEY_APP_PIN_KEY_REPLACE));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_PIN_KEY_STATE)) {
				dto.setAppPinKeyState((String) map
						.get(Constants.SECRET_KEY_APP_PIN_KEY_STATE));
			}
			if (null != map.get(Constants.SECRET_KEY_APP_PIN_TYPE)) {
				dto.setAppPinType((String) map
						.get(Constants.SECRET_KEY_APP_PIN_TYPE));
			}
			if (null != map.get(Constants.SECRET_KEY_BANK_MAC_KEY)) {
				dto.setBankMacKey((String) map
						.get(Constants.SECRET_KEY_BANK_MAC_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_BANK_MSG_KEY)) {
				dto.setBankMsgKey((String) map
						.get(Constants.SECRET_KEY_BANK_MSG_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_BANK_PIN_KEY)) {
				dto.setBankPinKey((String) map
						.get(Constants.SECRET_KEY_BANK_PIN_KEY));
			}
			if (null != map.get(Constants.SECRET_KEY_BANK_PIN_TYPE)) {
				dto.setBankPinType((String) map
						.get(Constants.SECRET_KEY_BANK_PIN_TYPE));
			}
			if (null != map.get(Constants.SECRET_KEY_BMK_INDEX)) {
				dto.setBmkIndex((String) map
						.get(Constants.SECRET_KEY_BMK_INDEX));
			}
			if (null != map.get(Constants.SECRET_KEY_RESERVE1)) {
				dto
						.setReserve1((String) map
								.get(Constants.SECRET_KEY_RESERVE1));
			}
			if (null != map.get(Constants.SECRET_KEY_RESERVE2)) {
				dto
						.setReserve2((String) map
								.get(Constants.SECRET_KEY_RESERVE2));
			}
			if (null != map.get(Constants.SECRET_KEY_TYPE)) {
				dto.setType((String) map.get(Constants.SECRET_KEY_TYPE));
			}

			SecretKeyDAO dao = new SecretKeyDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.update(dto);
		}

		private void updateEncryptor(Map map, Connection conn) {
			String id = (String) map.get(Constants.ENCRYPTOR_ID);
			if (null == id) {
				throw new RuntimeException("加密机ID为空!");
			}
			Encryptor dto = new Encryptor();
			dto.setId(id);
			if (null != map.get(Constants.ENCRYPTOR_IP)) {
				dto.setIp((String) map.get(Constants.ENCRYPTOR_IP));
			}
			if (null != map.get(Constants.ENCRYPTOR_MEMO)) {
				dto.setMemo((String) map.get(Constants.ENCRYPTOR_MEMO));
			}
			if (null != map.get(Constants.ENCRYPTOR_MODEL)) {
				dto.setModel((String) map.get(Constants.ENCRYPTOR_MODEL));
			}
			if (null != map.get(Constants.ENCRYPTOR_PORT)) {
				dto.setPort(Integer.parseInt((String) map
						.get(Constants.ENCRYPTOR_PORT)));
			}

			EncryptorDAO dao = new EncryptorDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.update(dto);
		}

		private void updateSecurityArithmetic(Map map, String functionName,
				Connection conn) {
			String id = (String) map.get(Constants.SECURITY_ARITHMETIC_ID);
			if (null == id) {
				throw new RuntimeException("安全算法[" + functionName + "]的ID为空!");
			}
			String ruleId = (String) map
					.get(Constants.SECURITY_ARITHMETIC_SECURITY_CTRL_RULE_ID);
			if (null == ruleId) {
				throw new RuntimeException("安全算法[" + functionName
						+ "]的安全控制规则ID为空!");
			}
			String functionId = (String) map
					.get(Constants.SECURITY_ARITHMETIC_FUNC_ID);
			if (null == functionId) {
				throw new RuntimeException("安全算法[" + functionName + "]的算法ID为空!");
			}

			SecurityArithmetic dto = new SecurityArithmetic();
			dto.setId(id);
			dto.setSecurityControlRuleId(ruleId);
			dto.setFunctionId(functionId);
			if (null != map.get(Constants.SECURITY_ARITHMETIC_NAME)) {
				dto.setName((String) map
						.get(Constants.SECURITY_ARITHMETIC_NAME));
			}
			if (null != map.get(Constants.SECURITY_ARITHMETIC_PARAM_ID)) {
				dto.setParameterId((String) map
						.get(Constants.SECURITY_ARITHMETIC_PARAM_ID));
			}
			if (null != map.get(Constants.SECURITY_ARITHMETIC_SERV_PROXY)) {
				dto.setServiceProxy((String) map
						.get(Constants.SECURITY_ARITHMETIC_SERV_PROXY));
			}
			if (null != map.get(Constants.SECURITY_ARITHMETIC_SERV_TYPE)) {
				dto.setServiceType((String) map
						.get(Constants.SECURITY_ARITHMETIC_SERV_TYPE));
			}
			if (null != map.get(Constants.SECURITY_ARITHMETIC_TYPE)) {
				dto.setType((String) map
						.get(Constants.SECURITY_ARITHMETIC_TYPE));
			}

			List list = (List) map.get(Constants.SERV_PARAM);
			if (null != list && 0 < list.size()) {
				for (int i = 0; i < list.size(); i++) {
					updateServiceParameters((Map) list.get(i), functionName,
							conn);
				}
			}

			SecurityArithmeticDAO dao = new SecurityArithmeticDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.update(dto);
		}

		private void updateServiceParameters(Map map, String functionName,
				Connection conn) {
			String id = (String) map.get(Constants.SERV_PARAM_ID);
			if (null == id) {
				throw new RuntimeException("安全算法[" + functionName
						+ "]的算法参数的ID为空!");
			}
			String paramId = (String) map
					.get(Constants.SERV_PARAM_SECURITY_PARAM_ID);
			if (null == paramId) {
				throw new RuntimeException("安全算法[" + functionName
						+ "]的算法参数的参数ID为空!");
			}
			ServiceParameters dto = new ServiceParameters();
			dto.setId(id);
			dto.setSecurityParameterId(paramId);
			if (null != map.get(Constants.SERV_PARAM_PARAM_CLASS)) {
				dto.setParameterClass((String) map
						.get(Constants.SERV_PARAM_PARAM_CLASS));
			}
			if (null != map.get(Constants.SERV_PARAM_PARAM_NAME)) {
				dto.setParameterName((String) map
						.get(Constants.SERV_PARAM_PARAM_NAME));
			}
			if (null != map.get(Constants.SERV_PARAM_PARAM_VALUE)) {
				dto.setParameterValue((String) map
						.get(Constants.SERV_PARAM_PARAM_VALUE));
			}
			if (null != map.get(Constants.SERV_PARAM_PARAMS_NUM)) {
				dto.setParametersNumber(Integer.parseInt((String) map
						.get(Constants.SERV_PARAM_PARAMS_NUM)));
			}

			ServiceParametersDAO dao = new ServiceParametersDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.update(dto);
		}

		private void updateSecurityRuleBusinessRelation(Map map, Connection conn) {
			SecurityRuleBussinessRelation dto = new SecurityRuleBussinessRelation();
			String orgId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_ORG_ID);
			if (null == orgId) {
				throw new RuntimeException("安全规则业务关联关系数据的机构ID为空!");
			}
			String productId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_PRODUCT_ID);
			if (null == productId) {
				throw new RuntimeException("安全规则业务关联关系数据的产品ID为空!");
			}
			dto.setOrgId(orgId);
			dto.setProductId(productId);
			if (null != map.get(Constants.SECURITY_RULE_BUSS_RELATION_INFO)) {
				dto.setInfo((String) map
						.get(Constants.SECURITY_RULE_BUSS_RELATION_INFO));
			}
			if (null != map.get(Constants.SECURITY_RULE_BUSS_RELATION_RULE_ID)) {
				dto.setRuleId((String) map
						.get(Constants.SECURITY_RULE_BUSS_RELATION_RULE_ID));
			}
			if (null != map.get(Constants.SECURITY_RULE_BUSS_RELATION_SYS_VER)) {
				dto.setSystemVersion((String) map
						.get(Constants.SECURITY_RULE_BUSS_RELATION_SYS_VER));
			}

			SecurityRuleBussinessRelationDAO dao = new SecurityRuleBussinessRelationDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.update(dto);
		}

		private Map searchSecurity(Map map) {
			int pageNum = 0;
			if (null != map.get(Constants.PARAMS_PAGE_NUM)) {
				pageNum = Integer.parseInt((String) map
						.get(Constants.PARAMS_PAGE_NUM));
			}
			int pageLength = 0;
			if (null != map.get(Constants.PARAMS_PAGE_LEN)) {
				pageLength = Integer.parseInt((String) map
						.get(Constants.PARAMS_PAGE_LEN));
			}

			String whereClause = SqlUtil.combineSearchCommLogWhereClause(map);
			List list = null;

			Connection conn = null;
			try {
				SecurityRuleBussinessRelationDAO dao = new SecurityRuleBussinessRelationDAO();
				dao.setConnection(conn);
				if (null == whereClause) {
					if (0 < pageNum) {
						list = dao.findAll(pageNum, pageLength);
					} else {
						list = dao.findAll();
					}
				} else {
					if (0 < pageNum) {
						list = dao
								.findByWhere(whereClause, pageNum, pageLength);
					} else {
						list = dao.findByWhere(whereClause);
					}
				}

				List result = new ArrayList();
				for (int i = 0; i < list.size(); i++) {
					result.add(toMap((SecurityRuleBussinessRelation) list
							.get(i), conn));
				}

				conn.commit();

				Map returnMap = returnSuccessMap();
				returnMap.put(Constants.SECURITY_SEARCH_RESULT, result);

				return returnMap;
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {

					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				return returnFailedMap("查询安全规则业务关联表失败!原因是：  " + e.getMessage());
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

		private Map toMap(SecurityRuleBussinessRelation dto, Connection conn) {
			Map returnMap = new HashMap();
			Map ruleBusiRelation = new HashMap();
			ruleBusiRelation.put(Constants.SECURITY_RULE_BUSS_RELATION_INFO,
					dto.getInfo());
			ruleBusiRelation.put(Constants.SECURITY_RULE_BUSS_RELATION_ORG_ID,
					dto.getOrgId());
			ruleBusiRelation.put(
					Constants.SECURITY_RULE_BUSS_RELATION_PRODUCT_ID, dto
							.getProductId());
			ruleBusiRelation.put(Constants.SECURITY_RULE_BUSS_RELATION_RULE_ID,
					dto.getRuleId());
			ruleBusiRelation.put(Constants.SECURITY_RULE_BUSS_RELATION_SYS_VER,
					dto.getSystemVersion());
			returnMap.put(Constants.SECURITY_RULE_BUSS_RELATION,
					ruleBusiRelation);

			SecurityControlRuleDAO scrDao = new SecurityControlRuleDAO();
			scrDao.setConnection(conn);
			SecurityControlRule scrDto = scrDao.selectByPK(dto.getRuleId());
			Map controlRule = new HashMap();
			controlRule.put(Constants.SECURITY_CTRL_RULE_ID, scrDto.getId());
			if (null != scrDto.getEncryptorId()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_ENCRYPTOR_ID,
						scrDto.getEncryptorId());
				controlRule.put(Constants.ENCRYPTOR, encryptorToMap(scrDto
						.getEncryptorId(), conn));
			}
			if (null != scrDto.getSecretKeyId()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_SECRET_KEY_ID,
						scrDto.getSecretKeyId());
				controlRule.put(Constants.SECRET_KEY, secretKeyToMap(scrDto
						.getSecretKeyId(), conn));
			}
			if (null != scrDto.getGenwkFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_GEN_WK_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getGenwkFunction(), conn));
			}
			if (null != scrDto.getMacFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_MAC_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getMacFunction(), conn));
			}
			if (null != scrDto.getMemo()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_MEMO, scrDto
						.getMemo());
			}
			if (null != scrDto.getMessageDecryptFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_MSG_DECRYPT_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getMessageDecryptFunction(), conn));
			}
			if (null != scrDto.getMessageEncryptFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_MSG_ENCRYPT_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getMessageEncryptFunction(), conn));
			}
			if (null != scrDto.getMode()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_MODE, scrDto
						.getMode());
			}
			if (null != scrDto.getPinDecryptFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_PIN_DECRYPT_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getPinDecryptFunction(), conn));
			}
			if (null != scrDto.getPinEncryptFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_PIN_ENCRYPT_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getPinEncryptFunction(), conn));
			}
			if (null != scrDto.getPinExchangeFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_PIN_EXCHANGE_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getPinExchangeFunction(), conn));
			}
			if (null != scrDto.getUpdateKeyFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_UPDATE_KEY_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getUpdateKeyFunction(), conn));
			}
			if (null != scrDto.getVerifyCvvFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_VERIFY_CVV_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getVerifyCvvFunction(), conn));
			}
			if (null != scrDto.getVerifyCvvpvvFunction()) {
				controlRule.put(
						Constants.SECURITY_CTRL_RULE_VERIFY_CVVPVV_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getVerifyCvvpvvFunction(), conn));
			}
			if (null != scrDto.getVerifyMacFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getVerifyMacFunction(), conn));
			}
			if (null != scrDto.getVerifyPvvFunction()) {
				controlRule.put(Constants.SECURITY_CTRL_RULE_VERIFY_PVV_FUNC,
						securityArithmeticToMap(dto.getRuleId(), scrDto
								.getVerifyPvvFunction(), conn));
			}

			return controlRule;
		}

		private Map secretKeyToMap(String secretKeyId, Connection conn) {
			SecretKeyDAO dao = new SecretKeyDAO();
			dao.setConnection(conn);
			SecretKey dto = dao.selectByPK(secretKeyId);
			Map result = new HashMap();
			if (null != dto.getAppKey1()) {
				result.put(Constants.SECRET_KEY_APP_KEY1, dto.getAppKey1());
			}
			if (null != dto.getAppKey2()) {
				result.put(Constants.SECRET_KEY_APP_KEY2, dto.getAppKey2());
			}
			if (null != dto.getAppMacKey()) {
				result
						.put(Constants.SECRET_KEY_APP_MAC_KEY, dto
								.getAppMacKey());
			}
			if (null != dto.getAppMacKeyReplace()) {
				result.put(Constants.SECRET_KEY_APP_MAC_KEY_REPLACE, dto
						.getAppMacKeyReplace());
			}
			if (null != dto.getAppMacKeyState()) {
				result.put(Constants.SECRET_KEY_APP_MAC_KEY_STATE, dto
						.getAppMacKeyState());
			}
			if (null != dto.getAppMasterKey()) {
				result.put(Constants.SECRET_KEY_APP_MASTER_KEY, dto
						.getAppMasterKey());
			}
			if (null != dto.getAppMasterKeyIndex()) {
				result.put(Constants.SECRET_KEY_APP_MASTER_KEY_INDEX, dto
						.getAppMasterKeyIndex());
			}
			if (null != dto.getAppMsgKey()) {
				result
						.put(Constants.SECRET_KEY_APP_MSG_KEY, dto
								.getAppMsgKey());
			}
			if (null != dto.getAppMsgKeyReplace()) {
				result.put(Constants.SECRET_KEY_APP_MSG_KEY_REPLACE, dto
						.getAppMsgKeyReplace());
			}
			if (null != dto.getAppMsgKeyState()) {
				result.put(Constants.SECRET_KEY_APP_MSG_KEY_STATE, dto
						.getAppMsgKeyState());
			}
			if (null != dto.getAppPinKey()) {
				result
						.put(Constants.SECRET_KEY_APP_PIN_KEY, dto
								.getAppPinKey());
			}
			if (null != dto.getAppPinKeyReplace()) {
				result.put(Constants.SECRET_KEY_APP_PIN_KEY_REPLACE, dto
						.getAppPinKeyReplace());
			}
			if (null != dto.getAppPinKeyState()) {
				result.put(Constants.SECRET_KEY_APP_PIN_KEY_STATE, dto
						.getAppPinKeyState());
			}
			if (null != dto.getAppPinType()) {
				result.put(Constants.SECRET_KEY_APP_PIN_TYPE, dto
						.getAppPinType());
			}
			if (null != dto.getBankMacKey()) {
				result.put(Constants.SECRET_KEY_BANK_MAC_KEY, dto
						.getBankMacKey());
			}
			if (null != dto.getBankMsgKey()) {
				result.put(Constants.SECRET_KEY_BANK_MSG_KEY, dto
						.getBankMsgKey());
			}
			if (null != dto.getBankPinKey()) {
				result.put(Constants.SECRET_KEY_BANK_PIN_KEY, dto
						.getBankPinKey());
			}
			if (null != dto.getBankPinType()) {
				result.put(Constants.SECRET_KEY_BANK_PIN_TYPE, dto
						.getBankPinType());
			}
			if (null != dto.getBmkIndex()) {
				result.put(Constants.SECRET_KEY_BMK_INDEX, dto.getBmkIndex());
			}
			if (null != dto.getReserve1()) {
				result.put(Constants.SECRET_KEY_RESERVE1, dto.getReserve1());
			}
			if (null != dto.getReserve2()) {
				result.put(Constants.SECRET_KEY_RESERVE2, dto.getReserve2());
			}
			if (null != dto.getType()) {
				result.put(Constants.SECRET_KEY_TYPE, dto.getType());
			}

			return result;
		}

		private Map encryptorToMap(String encryptorId, Connection conn) {
			EncryptorDAO dao = new EncryptorDAO();
			dao.setConnection(conn);
			Encryptor dto = dao.selectByPK(encryptorId);
			Map result = new HashMap();
			result.put(Constants.ENCRYPTOR_ID, dto.getId());
			if (null != dto.getIp()) {
				result.put(Constants.ENCRYPTOR_IP, dto.getIp());
			}
			if (0 < dto.getPort()) {
				result.put(Constants.ENCRYPTOR_PORT, dto.getPort());
			}
			if (null != dto.getMemo()) {
				result.put(Constants.ENCRYPTOR_MEMO, dto.getMemo());
			}
			if (null != dto.getModel()) {
				result.put(Constants.ENCRYPTOR_MODEL, dto.getModel());
			}

			return result;
		}

		private Map securityArithmeticToMap(String ruleId, String functionId,
				Connection conn) {
			SecurityArithmeticDAO saDao = new SecurityArithmeticDAO();
			saDao.setConnection(conn);
			List list = saDao.findByControlRuleAndFunction(ruleId, functionId);
			if (0 == list.size()) {
				return null;
			}

			SecurityArithmetic saDto = (SecurityArithmetic) list.get(0);
			Map result = new HashMap();
			result.put(Constants.SECURITY_ARITHMETIC_ID, saDto.getId());
			result.put(Constants.SECURITY_ARITHMETIC_FUNC_ID, functionId);
			result.put(Constants.SECURITY_ARITHMETIC_SECURITY_CTRL_RULE_ID,
					ruleId);
			if (null != saDto.getName()) {
				result.put(Constants.SECURITY_ARITHMETIC_NAME, saDto.getName());
			}
			if (null != saDto.getServiceProxy()) {
				result.put(Constants.SECURITY_ARITHMETIC_SERV_PROXY, saDto
						.getServiceProxy());
			}
			if (null != saDto.getServiceType()) {
				result.put(Constants.SECURITY_ARITHMETIC_SERV_TYPE, saDto
						.getServiceType());
			}
			if (null != saDto.getType()) {
				result.put(Constants.SECURITY_ARITHMETIC_TYPE, saDto.getType());
			}

			if (null != saDto.getParameterId()) {
				result.put(Constants.SECURITY_ARITHMETIC_PARAM_ID, saDto
						.getParameterId());
				result.put(Constants.SERV_PARAM, serviceParameterToList(saDto
						.getParameterId(), conn));
			}

			return result;
		}

		private List serviceParameterToList(String parameterId, Connection conn) {
			ServiceParametersDAO dao = new ServiceParametersDAO();
			dao.setConnection(conn);
			List list = dao.findBySecruityParameterId(parameterId);
			if (0 == list.size()) {
				return null;
			}
			List result = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				Map map = new HashMap();
				ServiceParameters dto = (ServiceParameters) list.get(i);
				map.put(Constants.SERV_PARAM_ID, dto.getId());
				map.put(Constants.SERV_PARAM_SECURITY_PARAM_ID, dto
						.getSecurityParameterId());
				map.put(Constants.SERV_PARAM_PARAMS_NUM, dto
						.getParametersNumber());
				if (null != dto.getParameterClass()) {
					map.put(Constants.SERV_PARAM_PARAM_CLASS, dto
							.getParameterClass());
				}
				if (null != dto.getParameterName()) {
					map.put(Constants.SERV_PARAM_PARAM_NAME, dto
							.getParameterName());
				}
				if (null != dto.getParameterValue()) {
					map.put(Constants.SERV_PARAM_PARAM_VALUE, dto
							.getParameterValue());
				}

				result.add(map);
			}
			return result;
		}

		private Map delSecurity(Map map) {
			String orgId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_ORG_ID);
			if (null == orgId) {
				return returnFailedMap("机构ID不存在!");
			}
			String productId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_PRODUCT_ID);
			if (null == productId) {
				return returnFailedMap("产品ID不存在!");
			}
			String ruleId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_RULE_ID);
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				SecurityRuleBussinessRelationDAO dao = new SecurityRuleBussinessRelationDAO();
				dao.setConnection(conn);
				dao.setInTransaction(true);
				if (null == ruleId) {
					SecurityRuleBussinessRelation dto = dao.selectByPK(orgId,
							productId);
					if (null == dto) {
						return returnFailedMap("查询安全规则业务关联表失败!没有查找到相关数据!");

					}
					ruleId = dto.getRuleId();
				}
				List list = dao.findByRuleId(ruleId);
				if (0 == list.size()) {
					return returnFailedMap("删除安全规则业务关联表数据失败!数据不存在或已被删除!");
				} else if (1 == list.size()) {
					dao.deleteByPK(orgId, productId);
					SecurityControlRuleDAO scrDao = new SecurityControlRuleDAO();
					scrDao.setConnection(conn);
					scrDao.setInTransaction(true);
					scrDao.deleteByPK(ruleId);
				} else {
					dao.deleteByPK(orgId, productId);
				}

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				return returnFailedMap("删除安全规则业务关联表数据失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return returnSuccessMap();
		}

		private Map addSecurity(Map map) {
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				String secretKeyId = null;
				Map secretKey = (Map) map.get(Constants.SECRET_KEY);
				if (null != secretKey) {
					secretKeyId = insertSecretKey(secretKey, conn);
				}

				String encryptorId = null;
				Map encryptor = (Map) map.get(Constants.ENCRYPTOR);
				if (null != encryptor) {
					encryptorId = insertEncryptor(encryptor, conn);
				}

				Map securityCtrlRule = (Map) map
						.get(Constants.SECURITY_CTRL_RULE);
				if (null == securityCtrlRule) {
					return returnFailedMap("安全控制规则为空!");
				}
				SecurityControlRule ruleDto = new SecurityControlRule();
				ruleDto.setId(SerialNumberGenerator.getInstance().next(
						SERIAL_NUM_SECURITY_CTRL_RULE));
				ruleDto.setEncryptorId(encryptorId);
				ruleDto.setMemo((String) securityCtrlRule
						.get(Constants.SECURITY_CTRL_RULE_MEMO));
				ruleDto.setMode((String) securityCtrlRule
						.get(Constants.SECURITY_CTRL_RULE_MODE));
				ruleDto.setSecretKeyId(secretKeyId);

				Map macFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_MAC_FUNC);
				if (null != macFunc) {
					ruleDto.setMacFunction(insertSecurityArithmetic(macFunc,
							ruleDto.getId(),
							Constants.SECURITY_CTRL_RULE_MAC_FUNC, conn));
				}

				Map verifyMacFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC);
				if (null != verifyMacFunc) {
					ruleDto
							.setVerifyMacFunction(insertSecurityArithmetic(
									verifyMacFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC,
									conn));
				}

				Map msgEncryptFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_MSG_ENCRYPT_FUNC);
				if (null != msgEncryptFunc) {
					ruleDto
							.setMessageEncryptFunction(insertSecurityArithmetic(
									msgEncryptFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_MSG_ENCRYPT_FUNC,
									conn));
				}

				Map msgDecryptFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_MSG_DECRYPT_FUNC);
				if (null != msgDecryptFunc) {
					ruleDto
							.setMessageDecryptFunction(insertSecurityArithmetic(
									msgDecryptFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_MSG_DECRYPT_FUNC,
									conn));
				}

				Map pinEncryptFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_PIN_ENCRYPT_FUNC);
				if (null != pinEncryptFunc) {
					ruleDto
							.setPinEncryptFunction(insertSecurityArithmetic(
									pinEncryptFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_PIN_ENCRYPT_FUNC,
									conn));
				}

				Map pinDecryptFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_PIN_DECRYPT_FUNC);
				if (null != pinDecryptFunc) {
					ruleDto
							.setPinDecryptFunction(insertSecurityArithmetic(
									pinDecryptFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_PIN_DECRYPT_FUNC,
									conn));
				}

				Map pinExchangeFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_PIN_EXCHANGE_FUNC);
				if (null != pinExchangeFunc) {
					ruleDto.setPinExchangeFunction(insertSecurityArithmetic(
							pinExchangeFunc, ruleDto.getId(),
							Constants.SECURITY_CTRL_RULE_PIN_EXCHANGE_FUNC,
							conn));
				}

				Map updateKeyFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_UPDATE_KEY_FUNC);
				if (null != updateKeyFunc) {
					ruleDto
							.setUpdateKeyFunction(insertSecurityArithmetic(
									updateKeyFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_UPDATE_KEY_FUNC,
									conn));
				}

				Map genwkFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_GEN_WK_FUNC);
				if (null != genwkFunc) {
					ruleDto.setGenwkFunction(insertSecurityArithmetic(
							genwkFunc, ruleDto.getId(),
							Constants.SECURITY_CTRL_RULE_GEN_WK_FUNC, conn));
				}

				Map verifyCvvFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_CVV_FUNC);
				if (null != verifyCvvFunc) {
					ruleDto
							.setVerifyCvvFunction(insertSecurityArithmetic(
									verifyCvvFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_VERIFY_CVV_FUNC,
									conn));
				}

				Map verifyPvvFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_PVV_FUNC);
				if (null != verifyPvvFunc) {
					ruleDto
							.setVerifyPvvFunction(insertSecurityArithmetic(
									verifyPvvFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_VERIFY_PVV_FUNC,
									conn));
				}

				Map verifyCvvpvvFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_CVVPVV_FUNC);
				if (null != verifyCvvpvvFunc) {
					ruleDto.setVerifyCvvpvvFunction(insertSecurityArithmetic(
							verifyCvvpvvFunc, ruleDto.getId(),
							Constants.SECURITY_CTRL_RULE_VERIFY_CVVPVV_FUNC,
							conn));
				}

				Map verfiyMacFunc = (Map) map
						.get(Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC);
				if (null != verifyMacFunc) {
					ruleDto
							.setVerifyMacFunction(insertSecurityArithmetic(
									verifyMacFunc,
									ruleDto.getId(),
									Constants.SECURITY_CTRL_RULE_VERIFY_MAC_FUNC,
									conn));
				}

				SecurityControlRuleDAO dao = new SecurityControlRuleDAO();
				dao.setConnection(conn);
				dao.setInTransaction(true);
				dao.insert(ruleDto);

				Map ruleBusiRelation = (Map) map
						.get(Constants.SECURITY_RULE_BUSS_RELATION);
				if (null == ruleBusiRelation) {
					return returnFailedMap("安全规则业务关联关系为空!");
				}
				insertSecurityRuleBussinessRelation(ruleBusiRelation, ruleDto
						.getId(), conn);

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();

					}
				}

				return returnFailedMap("添加安全规则失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

			return returnSuccessMap();
		}

		private String insertSecurityArithmetic(Map map, String ruleId,
				String functionName, Connection conn) {
			String functionId = (String) map
					.get(Constants.SECURITY_ARITHMETIC_FUNC_ID);
			if (null == functionId) {
				throw new RuntimeException("安全算法[" + functionName + "]的算法ID为空!");
			}
			String type = (String) map.get(Constants.SECURITY_ARITHMETIC_TYPE);
			if (null == type) {
				throw new RuntimeException("安全算法[" + functionName + "]的类型为空!");
			}

			SecurityArithmetic dto = new SecurityArithmetic();
			dto.setFunctionId(functionId);
			dto.setSecurityControlRuleId(ruleId);
			dto.setName((String) map.get(Constants.SECURITY_ARITHMETIC_NAME));
			dto.setParameterId((String) map
					.get(Constants.SECURITY_ARITHMETIC_PARAM_ID));
			dto.setServiceProxy((String) map
					.get(Constants.SECURITY_ARITHMETIC_SERV_PROXY));
			dto.setServiceType((String) map
					.get(Constants.SECURITY_ARITHMETIC_SERV_TYPE));
			dto.setType(type);
			dto.setId(SerialNumberGenerator.getInstance().next(
					SERIAL_NUM_SECURITY_ARITHMETIC));

			SecurityArithmeticDAO dao = new SecurityArithmeticDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);
			dao.insert(dto);

			List serviceParams = (List) map.get(Constants.SERV_PARAM);
			if (null != serviceParams && 0 < serviceParams.size()) {
				for (int i = 0; i < serviceParams.size(); i++) {
					try {
						insertServiceParameters((Map) serviceParams.get(i),
								conn);
					} catch (Exception e) {
						throw new RuntimeException("添加安全算法[" + functionName
								+ "]的算法参数失败!原因是： " + e.getMessage());
					}
				}

			}

			return functionId;
		}

		private void insertServiceParameters(Map map, Connection conn) {
			String securityParamId = (String) map
					.get(Constants.SERV_PARAM_SECURITY_PARAM_ID);
			if (null == securityParamId) {
				throw new RuntimeException("安全算法参数ID为空!");
			}
			String paramNum = (String) map.get(Constants.SERV_PARAM_PARAMS_NUM);
			if (null == paramNum) {
				throw new RuntimeException("安全算法参数序号为空!");
			}

			ServiceParameters dto = new ServiceParameters();
			dto.setSecurityParameterId(securityParamId);
			dto.setParametersNumber(Integer.parseInt(paramNum));
			dto.setParameterClass((String) map
					.get(Constants.SERV_PARAM_PARAM_CLASS));
			dto.setParameterName((String) map
					.get(Constants.SERV_PARAM_PARAM_NAME));
			dto.setParameterValue((String) map
					.get(Constants.SERV_PARAM_PARAM_VALUE));
			dto.setId(SerialNumberGenerator.getInstance().next(
					SERIAL_NUM_SERVICE_PARAMETER));

			ServiceParametersDAO dao = new ServiceParametersDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);
			dao.insert(dto);
		}

		private String insertEncryptor(Map map, Connection conn) {
			String ip = (String) map.get(Constants.ENCRYPTOR_IP);
			if (null == ip) {
				throw new RuntimeException("加密机IP为空!");
			}
			int port = 0;
			String portStr = (String) map.get(Constants.ENCRYPTOR_PORT);
			if (null == portStr) {
				throw new RuntimeException("加密机端口为空!");
			}
			port = Integer.parseInt(portStr);

			Encryptor dto = new Encryptor();
			dto.setIp(ip);
			dto.setPort(port);
			dto.setMemo((String) map.get(Constants.ENCRYPTOR_MEMO));
			dto.setModel((String) map.get(Constants.ENCRYPTOR_MODEL));
			dto.setId(SerialNumberGenerator.getInstance().next(
					SERIAL_NUM_ENCRYPTOR));

			EncryptorDAO dao = new EncryptorDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.insert(dto);

			return dto.getId();
		}

		private String insertSecretKey(Map map, Connection conn) {
			SecretKey dto = new SecretKey();
			dto.setAppKey1((String) map.get(Constants.SECRET_KEY_APP_KEY1));
			dto.setAppKey2((String) map.get(Constants.SECRET_KEY_APP_KEY2));
			dto
					.setAppMacKey((String) map
							.get(Constants.SECRET_KEY_APP_MAC_KEY));
			dto.setAppMacKeyReplace((String) map
					.get(Constants.SECRET_KEY_APP_MAC_KEY_REPLACE));
			dto.setAppMacKeyState((String) map
					.get(Constants.SECRET_KEY_APP_MAC_KEY_STATE));
			dto.setAppMasterKey((String) map
					.get(Constants.SECRET_KEY_APP_MASTER_KEY));
			dto.setAppMasterKeyIndex((String) map
					.get(Constants.SECRET_KEY_APP_MASTER_KEY_INDEX));
			dto
					.setAppMsgKey((String) map
							.get(Constants.SECRET_KEY_APP_MSG_KEY));
			dto.setAppMsgKeyReplace((String) map
					.get(Constants.SECRET_KEY_APP_MSG_KEY_REPLACE));
			dto.setAppMsgKeyState((String) map
					.get(Constants.SECRET_KEY_APP_MSG_KEY_STATE));
			dto
					.setAppPinKey((String) map
							.get(Constants.SECRET_KEY_APP_PIN_KEY));
			dto.setAppPinKeyReplace((String) map
					.get(Constants.SECRET_KEY_APP_PIN_KEY_REPLACE));
			dto.setAppPinKeyState((String) map
					.get(Constants.SECRET_KEY_APP_PIN_KEY_STATE));
			dto.setAppPinType((String) map
					.get(Constants.SECRET_KEY_APP_PIN_TYPE));
			dto.setId(SerialNumberGenerator.getInstance().next(
					SERIAL_NUM_SECRET_KEY));

			SecretKeyDAO dao = new SecretKeyDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.insert(dto);

			return dto.getId();
		}

		private void insertSecurityRuleBussinessRelation(Map map,
				String securityCtrlRuleId, Connection conn) {
			String orgId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_ORG_ID);
			if (null == orgId) {
				throw new RuntimeException("安全规则业务关联关系数据的机构ID为空!");
			}
			String productId = (String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_PRODUCT_ID);
			if (null == productId) {
				throw new RuntimeException("安全规则业务关联关系数据的产品ID为空!");
			}

			SecurityRuleBussinessRelation srbr = new SecurityRuleBussinessRelation();
			srbr.setOrgId(orgId);
			srbr.setProductId(productId);
			srbr.setSystemVersion((String) map
					.get(Constants.SECURITY_RULE_BUSS_RELATION_SYS_VER));
			srbr.setRuleId(securityCtrlRuleId);

			SecurityRuleBussinessRelationDAO dao = new SecurityRuleBussinessRelationDAO();
			dao.setConnection(conn);
			dao.setInTransaction(true);

			dao.insert(srbr);
		}

		private Map startJob(Map map) {
			String jobId = (String) map.get(Constants.JOB_PARAMS_ID);
			if (null == jobId) {
				return returnFailedMap("任务ID为空!");
			}

			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				JobDAO jobDao = new JobDAO();
				jobDao.setConnection(conn);
				Job job = jobDao.selectByPK(jobId);
				if (null == job) {
					return returnFailedMap("任务不存在!");

				}

				if (JobConstants.JOB_STAT_ALIVE.equals(job.getState())) {
					return returnFailedMap("任务已经启动!");
				}

				JobManager.startJob(job, jobDao);

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}

				return returnFailedMap("启动任务失败!原因是： " + e.getMessage());
			}

			return returnSuccessMap();
		}

		private Map cancelJob(Map map) {
			String jobId = (String) map.get(Constants.JOB_PARAMS_ID);
			if (null == jobId) {
				return returnFailedMap("任务ID为空!");
			}
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				JobDAO jobDao = new JobDAO();
				jobDao.setConnection(conn);
				Job job = jobDao.selectByPK(jobId);
				if (null == job) {
					return returnFailedMap("任务不存在!");
				}

				if (!CommGateway.isJobSupport()
						|| JobConstants.JOB_STAT_WAIT.equals(job.getState())) {
					jobDao.updateJobState(JobConstants.JOB_STAT_CANCEL, jobId);
				} else if (JobConstants.JOB_STAT_ALIVE.equals(job.getState())) {
					JobManager.interruptJobSchedule(jobId);
				} else {
					return returnFailedMap("任务已经停止!");
				}

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return returnFailedMap("取消任务失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return returnSuccessMap();

		}

		private Map delJob(Map map) {
			String jobId = (String) map.get(Constants.JOB_PARAMS_ID);
			if (null == jobId) {
				return returnFailedMap("任务ID为空!");
			}

			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				JobDAO jobDao = new JobDAO();
				jobDao.setConnection(conn);
				jobDao.setInTransaction(true);
				jobDao.deleteByPK(jobId);

				CronRuleDAO ruleDao = new CronRuleDAO();
				ruleDao.setConnection(conn);
				ruleDao.setInTransaction(true);
				ruleDao.deleteCronRuleByJobId(jobId);

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return returnFailedMap("删除任务失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return returnSuccessMap();
		}

		private Map addJob(Map map) {
			String type = (String) map.get(Constants.JOB_PARAMS_TYPE);
			if (null == type) {
				return returnFailedMap("任务类型为空!");
			}
			String scheduleType = (String) map
					.get(Constants.JOB_PARAMS_SCHEDULE_TYPE);
			if (null == scheduleType) {
				return returnFailedMap("任务执行类型为空!");
			}

			Job job = new Job();
			job.setType(type);
			job.setScheduleType(scheduleType);

			CronRule rule = null;
			if (JobConstants.JOB_SCHE_TYP_TIME.equals(scheduleType)
					|| JobConstants.JOB_SCHE_TYP_NUMANDTIME
							.equals(scheduleType)) {
				String maxTimes = (String) map
						.get(Constants.JOB_PARAMS_MAX_TIMES);
				if (null == maxTimes) {
					return returnFailedMap("当任务的执行类型是定次执行或定时定次执行时,最大执行次数的值不能为空!");
				}
				job.setMaxTimes(Integer.parseInt(maxTimes));

				String interval = (String) map
						.get(Constants.JOB_PARAMS_INTERVAL);
				if (null != interval) {
					job.setJobInterval(Long.parseLong(interval));
				}
			} else if (JobConstants.JOB_SCHE_TYP_TIMES_NUM.equals(scheduleType)
					|| JobConstants.JOB_SCHE_TYP_NUMANDTIME
							.equals(scheduleType)) {
				String expression = (String) map
						.get(Constants.JOB_PARAMS_CRON_RULE_EXPR);
				if (null == expression) {
					return returnFailedMap("当任务的执行类型是定时执行或定时定次执行时,计划开始时间表达式的值不能为空!");
				}
				rule = new CronRule();
				rule.setExpression(expression);
			} else {
				return returnFailedMap("任务执行类型" + scheduleType + "是非法的!");
			}

			if (JobConstants.JOB_TYP_LOCAL.equals(type)) {
				String jobClassName = (String) map
						.get(Constants.JOB_PARAMS_CLASS_NAME);
				if (null == jobClassName) {
					return returnFailedMap("当任务的类型是本地任务时,任务的执行类的值不能为空!");
				}

				job.setJobClassName(jobClassName);
			} else if (JobConstants.JOB_TYP_COMMUNICATE.equals(type)) {
				String relateLogId = (String) map
						.get(Constants.JOB_PARAMS_RELATE_LOG_ID);
				if (null == relateLogId) {
					return returnFailedMap("当任务的类型是通讯任务时,关联的会话流水ID的值不能为空!");
				}
				String processorId = (String) map
						.get(Constants.JOB_PARAMS_PROC_ID);
				if (null == processorId) {
					return returnFailedMap("当任务的类型是通讯任务时,处理器ID的值不能为空!");
				}
				String reqMsgFrom = (String) map
						.get(Constants.JOB_PARAMS_REQ_MSG_FROM);
				if (null == reqMsgFrom) {
					return returnFailedMap("当任务的类型是通讯任务时,请求报文数据来源的值不能为空!");
				}
				String endFlag = (String) map
						.get(Constants.JOB_PARAMS_END_FLAG);
				if (null == endFlag) {
					return returnFailedMap("当任务的类型是通讯任务时,结束标志的值不能为空!");
				}
				job.setLogId(relateLogId);
				job.setProcessId(processorId);
				job.setRequestMessageFrom(Integer.parseInt(reqMsgFrom));
				job.setScheduleEndFlag(endFlag);
			} else {
				return returnFailedMap("任务类型的值" + type + "是非法的!");
			}
			job.setState(JobConstants.JOB_STAT_WAIT);
			Date date = new Date();
			job.setStartDate(DAY_FORMAT.format(date));
			job.setStartTime(DAY_TIME_FORMAT.format(date));

			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				JobDAO jobDao = new JobDAO();
				jobDao.setConnection(conn);
				jobDao.setInTransaction(true);
				job.setId(SerialNumberGenerator.getInstance().next("JOB"));
				jobDao.insert(job);

				if (null != rule) {
					rule.setId(SerialNumberGenerator.getInstance().next(
							"CRON_RULE"));
					rule.setJobId(job.getId());
					rule.setState(JobConstants.CRON_RULE_STAT_INVOCATE);

					CronRuleDAO ruleDao = new CronRuleDAO();
					ruleDao.setConnection(conn);
					ruleDao.setInTransaction(true);
					ruleDao.insert(rule);
				}

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				return returnFailedMap("添加任务失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			return returnSuccessMap();
		}

		private Map endSession(Map map) {
			String sessionId = (String) map
					.get(Constants.END_SESSION_PARAMS_SESSION_ID);
			if (null == sessionId) {
				return returnFailedMap("会话ID为空!");
			}

			Map sessions = SessionManager.getSessions();
			Session session = null;
			synchronized (sessions) {
				Iterator<Session> iter = sessions.values().iterator();
				while (iter.hasNext()) {
					Session tmp = iter.next();
					if (sessionId.equals(tmp.getId())) {
						session = tmp;
						break;
					}
				}
			}

			if (null == session) {
				return returnFailedMap("会话不存在或已经关闭!");
			}
			try {
				SessionManager.removeSession(session);
				SessionManager.terminalSession(session);
			} catch (Exception e) {
				e.printStackTrace();
				return returnFailedMap("强制结束会话失败!原因是： " + e.getMessage());
			}

			return returnSuccessMap();
		}

		private Map checkSession(Map map) {
			Set set = SessionManager.getDistinctSessions();
			Iterator iter = set.iterator();
			List allSessionsList = new ArrayList();
			while (iter.hasNext()) {
				Session session = (Session) iter.next();
				Map tmp = new HashMap();
				tmp.put(Constants.CHECK_SESSION_PARAMS_ID, session.getId());
				tmp.put(Constants.CHECK_SESSION_PARAMS_SESSION_INFO, session
						.toString());
				tmp.put(Constants.CHECK_SESSION_PARAMS_SESSION_TIME_INFO,
						session.toString4TimeDebugRecord());
				allSessionsList.add(tmp);
			}
			Map returnMap = new HashMap();
			returnMap.put(Constants.RET_PARAMS_RET_CODE,
					Constants.RETURN_CODE_SUCCESS);
			returnMap.put(Constants.CHECK_SESSION_PARAMS_ALL, allSessionsList);

			return returnMap;
		}

		private Map searchCommLogMsg(Map map) {
			String sessionId = (String) map
					.get(Constants.COMM_LOG_MSG_PARAMS_SESSION_ID);
			if (null == sessionId) {
				return returnFailedMap("会话流水ID为空!");
			}
			String type = (String) map.get(Constants.COMM_LOG_MSG_PARAMS_TYPE);
			if (null == type) {
				return returnFailedMap("会话流水报文类型为空!");
			}

			CommLogMessage dto = null;
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();

				CommLogMessageDAO dao = new CommLogMessageDAO();
				dao.setConnection(conn);

				dto = dao.selectByPK(sessionId, type);

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				return returnFailedMap("查询会话流水报文失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (null == dto) {
				return returnFailedMap("指定会话流水报文不存在!");
			}
			Map returnMap = new HashMap();
			returnMap.put(Constants.RET_PARAMS_RET_CODE,
					Constants.RETURN_CODE_SUCCESS);
			returnMap.put(Constants.COMM_LOG_MSG_PARAMS_SESSION_ID, dto
					.getLogId());
			returnMap.put(Constants.COMM_LOG_MSG_PARAMS_TYPE, dto
					.getMessageType());
			returnMap.put(Constants.COMM_LOG_MSG_PARAMS_MESSAGE, dto
					.getMessage());

			return returnMap;
		}

		private Map resend(Map map) {
			String sessionId = (String) map.get(Constants.RESEND_SESSION_ID);
			if (null == sessionId) {
				return returnFailedMap("会话ID为空!");
			}
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				CommLogDAO commLogDAO = new CommLogDAO(false, conn);
				CommLog commLog = commLogDAO.selectByPK(sessionId);
				if (null == commLog) {
					return returnFailedMap("查询会话流水表记录失败!不存在会话ID为 " + sessionId
							+ "的数据!");
				}

				Channel destChannel = (Channel) CommGateway.getChannels().get(
						commLog.getDestChannelId());
				Channel sourceChannel = (Channel) CommGateway.getChannels()
						.get(commLog.getSourceChannelId());
				Processor processor = (Processor) destChannel
						.getChannelConfig().getProcessorTable().get(
								commLog.getProcessorId());
				if (null == processor) {
					processor = (Processor) sourceChannel.getChannelConfig()
							.getProcessorTable().get(commLog.getProcessorId());
				}

				CommLogMessageDAO commLogMessageDAO = new CommLogMessageDAO(
						false, conn);
				CommLogMessage commLogMessage = commLogMessageDAO.selectByPK(
						sessionId, SessionConstants.DST_REQ);

				Processor resendProcessor = new Processor();
				resendProcessor.setId(processor.getId());
				resendProcessor.setSourceAsync(true);
				resendProcessor.setDestAsync(processor.isDestAsync());

				Session resendSession = SessionManager.createSession();
				resendSession.setType(Session.TYP_EXTERNAL);
				resendSession.setSourceChannel(sourceChannel);
				resendSession.setProcessor(resendProcessor);
				resendSession.setDestChannel(destChannel);
				resendSession
						.setDestRequestMessage(commLogMessage.getMessage());
				SessionManager.attachSession(commLogMessage.getMessage(),
						resendSession);

				destChannel.sendRequestMessage(commLogMessage.getMessage(),
						processor.isDestAsync(), processor.getTimeout());

				Map ret = new HashMap();
				ret.put(Constants.RET_PARAMS_RET_CODE,
						Constants.RETURN_CODE_SUCCESS);
				ret.put(Constants.RESEND_RET_SESSION_ID, resendSession.getId());

				conn.commit();
				return ret;

			} catch (Exception e) {
				// e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}

				return returnFailedMap("会话目的请求报文重发失败!原因是： " + e.getMessage());
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

		private Map searchCommLog(Map map) {
			String whereClause = SqlUtil.combineSearchCommLogWhereClause(map);
			int pageNum = 0;
			if (null != map.get(Constants.PARAMS_PAGE_NUM)) {
				pageNum = Integer.parseInt((String) map
						.get(Constants.PARAMS_PAGE_NUM));
			}
			int pageLength = 0;
			if (null != map.get(Constants.PARAMS_PAGE_LEN)) {
				pageLength = Integer.parseInt((String) map
						.get(Constants.PARAMS_PAGE_LEN));
			}

			List list = null;
			Connection conn = null;
			try {
				conn = ConnectionManager.getInstance().getConnection();
				CommLogDAO dao = new CommLogDAO();
				dao.setConnection(conn);
				if (null == whereClause || 0 == whereClause.length()) {
					if (0 < pageNum) {
						list = dao.findAll(pageNum, pageLength);
					} else {
						list = dao.findAll();
					}
				} else {
					if (0 < pageNum) {
						list = dao
								.findByWhere(whereClause, pageNum, pageLength);
					} else {
						list = dao.findByWhere(whereClause);
					}
				}

				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (null != conn) {
					try {
						conn.rollback();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				return returnFailedMap("查询会话流水失败!原因是： " + e.getMessage());
			} finally {
				if (null != conn) {
					try {
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			List resultList = new ArrayList();
			if (null != list && 0 < list.size()) {
				for (int i = 0; i < list.size(); i++) {
					resultList.add(toMap((CommLog) list.get(i)));
				}
			} else {
				return returnFailedMap("无对应数据");
			}

			Map returnMap = new HashMap();
			returnMap.put(Constants.RET_PARAMS_RET_CODE,
					Constants.RETURN_CODE_SUCCESS);
			returnMap.put(Constants.COMM_LOG_RET_RESULT_LIST, resultList);
			returnMap.put(Constants.PARAMS_PAGE_LEN, pageLength);
			returnMap.put(Constants.PARAMS_PAGE_NUM, pageNum);
			returnMap.put(Constants.PARAMS_QUARY_CONDITION, map);

			return returnMap;
		}

		private Map returnFailedMap(String errorInfo) {
			Map returnMap = new HashMap();
			returnMap.put(Constants.RET_PARAMS_RET_CODE,
					Constants.RETURN_CODE_FAILED);
			returnMap.put(Constants.RET_PARAMS_ERR_MSG, errorInfo);

			return returnMap;
		}

		private Map returnSuccessMap() {
			Map returnMap = new HashMap();
			returnMap.put(Constants.RET_PARAMS_RET_CODE,
					Constants.RETURN_CODE_SUCCESS);
			return returnMap;
		}

		private Map<String, String> toMap(CommLog log) {
			Map<String, String> logMap = new HashMap<String, String>();
			logMap.put(Constants.COMM_LOG_PARAMS_DEST_CHANNEL_ID, log
					.getDestChannelId());
			logMap.put(Constants.COMM_LOG_PARAMS_DEST_CHANNEL_NAME, log
					.getDestChannelName());
			logMap.put(Constants.COMM_LOG_PARAMS_END_DATE, log.getEndDate());
			logMap.put(Constants.COMM_LOG_PARAMS_END_TIME, log.getEndTime());
			logMap.put(Constants.COMM_LOG_PARAMS_ERR_TYP, log.getErrorType());
			logMap.put(Constants.COMM_LOG_PARAMS_GW_ID, log.getGatewayId());
			logMap.put(Constants.COMM_LOG_PARAMS_PROC_ID, log.getProcessorId());
			logMap.put(Constants.COMM_LOG_PARAMS_SRC_CHANNEL_ID, log
					.getSourceChannelId());
			logMap.put(Constants.COMM_LOG_PARAMS_SRC_CHANNEL_NAME, log
					.getSourceChannelName());
			logMap
					.put(Constants.COMM_LOG_PARAMS_START_DATE, log
							.getStartDate());
			logMap
					.put(Constants.COMM_LOG_PARAMS_START_TIME, log
							.getStartTime());
			logMap.put(Constants.COMM_LOG_PARAMS_STATE, log.getState());
			logMap.put(Constants.COMM_LOG_PARAMS_TYPE, log.getType());
			logMap.put(Constants.COMM_LOG_PARAMS_ID, log.getId());
			return logMap;
		}
	}

}