package com.fib.msgconverter.commgateway.module.constants;

public class Constants {
	
	/**
	 * MonitorModule
	 */
	public static final String CONFIG_FILE_NAME = "config-file-name";
	public static final String CLUSTER_NAME = "cluster-name";
	public static final String ACCOUNT_NAME = "account-name";
	public static final String PASSWORD = "password";
	public static final byte LOGIN_SUCCESS = (byte) 0x30;
	public static final byte LOGIN_FAILED = (byte) 0x31;
	public static final byte UNSUPPORT_MONITOR = (byte) 0x32;
	public static final String CMD_SEPERATOR = "|";
	public static final String CHANNEL_ACTION_REQ_SEND = "1";
	public static final String CHANNEL_ACTION_RES_SEND = "2";
	public static final String CHANNEL_ACTION_REQ_RECV = "3";
	public static final String CHANNEL_ACTION_RES_RECV = "4";
	public static final String CHANNEL_ACTION_INTERNAL_TRANSFER = "5";
	public static final String CHANNEL_ACTION_STATE_SUCCESS = "0";
	public static final String CHANNEL_ACTION_STATE_FAILED = "1";
	public static final byte PROTOCOL_TCP = (byte) 0x30;
	public static final byte PROTOCOL_UDP = (byte) 0x31;

	public static final String LOGIN_REQ_PARAM_ACC_NAME = "accountName";
	public static final String LOGIN_REQ_PARAM_PASS = "password";
	public static final String LOGIN_RES_RESULT = "result";
	public static final String LOGIN_RES_PROTOCOL_TYP = "protocolType";
	public static final String LOGIN_RES_HOST = "host";
	public static final String LOGIN_RES_PORT = "port";
	public static final String LOGIN_RES_CLUSTER_NAME = "clusterName";
	public static final String LOGIN_RES_ERROR_INFO = "errorInfo";

	public static final String RET_TYPE = "TYPE";
	public static final String RET_PARAMS = "PARAMETERS";
	public static final String RET_PARAMS_RET_CODE = "RETURN_CODE";
	public static final String RET_PARAMS_ERR_MSG = "ERROR_MESSAGE";

	public static final String RETURN_CODE_SUCCESS = "0000";
	public static final String RETURN_CODE_FAILED = "9999";

	public static final String TYPE_SESSION_INFO = "01";
	public static final String TYPE_CHANNEL_STATE = "02";
	public static final String TYPE_COMM_LOG_INFO = "03";
	public static final String TYPE_GW_STOP = "04";
	public static final String TYPE_RESEND = "05";
	public static final String TYPE_COMM_LOG_MSG = "06";
	public static final String TYPE_CHECK_SESSION = "07";
	public static final String TYPE_END_SESSION = "08";
	public static final String TYPE_ADD_JOB = "09";
	public static final String TYPE_DEL_JOB = "10";
	public static final String TYPE_CANCEL_JOB = "11";
	public static final String TYPE_START_JOB = "12";
	public static final String TYPE_ADD_SECURITY = "13";
	public static final String TYPE_DEL_SECURITY = "14";
	public static final String TYPE_SEARCH_SECURITY = "15";
	public static final String TYPE_MODIFY_SECURITY = "16";

	public static final String BROADCAST_SESSION_ID = "sessionId";
	public static final String BROADCAST_SESSION_START_TIME = "sessionStartTime";
	public static final String BROADCAST_SESSION_END_TIME = "sessionEndTime";
	public static final String BROADCAST_SESSION_STATE = "sessionState";
	public static final String BROADCAST_SESSION_ERROR_TYPE = "sessionErrorType";
	public static final String BROADCAST_SESSION_INFO = "sessionInfo";

	public static final String BROADCAST_CHANNEL_ID = "channelId";
	public static final String BROADCAST_CHANNEL_NAME = "channelName";
	public static final String BROADCAST_CHANNEL_ACTION_STATE = "channelActionState";
	public static final String BROADCAST_CHANNEL_ACTION = "channelAction";

	public static final String PARAMS_QUARY_CONDITION = "quaryCondition";
	public static final String PARAMS_PAGE_NUM = "pageNum";
	public static final String PARAMS_PAGE_LEN = "pageLength";
	public static final String COMM_LOG_PARAMS_ID = "id";
	public static final String COMM_LOG_PARAMS_GW_ID = "gatewayId";
	public static final String COMM_LOG_PARAMS_START_DATE = "startDate";
	public static final String COMM_LOG_PARAMS_START_DATE_FROM = "startDateFrom";
	public static final String COMM_LOG_PARAMS_START_DATE_TO = "startDateTo";
	public static final String COMM_LOG_PARAMS_START_TIME = "startTime";
	public static final String COMM_LOG_PARAMS_START_TIME_FROM = "startTimeFrom";
	public static final String COMM_LOG_PARAMS_START_TIME_TO = "startTimeTo";
	public static final String COMM_LOG_PARAMS_END_DATE = "endDate";
	public static final String COMM_LOG_PARAMS_END_DATE_FROM = "endDateFrom";
	public static final String COMM_LOG_PARAMS_END_DATE_TO = "endDateTo";
	public static final String COMM_LOG_PARAMS_END_TIME = "endTime";
	public static final String COMM_LOG_PARAMS_END_TIME_FROM = "endTimeFrom";
	public static final String COMM_LOG_PARAMS_END_TIME_TO = "endTimeTo";
	public static final String COMM_LOG_PARAMS_STATE = "state";
	public static final String COMM_LOG_PARAMS_TYPE = "type";
	public static final String COMM_LOG_PARAMS_SRC_CHANNEL_ID = "sourceChannelId";
	public static final String COMM_LOG_PARAMS_DEST_CHANNEL_ID = "destChannelId";
	public static final String COMM_LOG_PARAMS_ERR_TYP = "errorType";
	public static final String COMM_LOG_PARAMS_PROC_ID = "processorId";
	public static final String COMM_LOG_PARAMS_SRC_CHANNEL_NAME = "sourceChannelName";
	public static final String COMM_LOG_PARAMS_DEST_CHANNEL_NAME = "destChannelName";
	public static final String COMM_LOG_RET_RESULT_LIST = "resultList";

	public static final String COMM_LOG_MSG_PARAMS_SESSION_ID = "sessionId";
	public static final String COMM_LOG_MSG_PARAMS_TYPE = "type";
	public static final String COMM_LOG_MSG_PARAMS_MESSAGE = "message";
	public static final String COMM_LOG_MSG_RET_RECORD = "record";

	public static final String CHECK_SESSION_PARAMS_ALL = "allSessions";
	public static final String CHECK_SESSION_PARAMS_ID = "id";
	public static final String CHECK_SESSION_PARAMS_SESSION_INFO = "sessionInfo";
	public static final String CHECK_SESSION_PARAMS_SESSION_TIME_INFO = "sessionTimeInfo";

	public static final String END_SESSION_PARAMS_SESSION_ID = "sessionId";

	public static final String JOB_PARAMS_ID = "id";
	public static final String JOB_PARAMS_RELATE_LOG_ID = "relateCommLogId";
	public static final String JOB_PARAMS_TYPE = "type";
	public static final String JOB_PARAMS_STATE = "state";
	public static final String JOB_PARAMS_START_DATE = "startDate";
	public static final String JOB_PARAMS_START_TIME = "startTime";
	public static final String JOB_PARAMS_SCHEDULE_TYPE = "scheduleType";
	public static final String JOB_PARAMS_CUR_TIMES = "currentTimes";
	public static final String JOB_PARAMS_INTERVAL = "jobInterval";
	public static final String JOB_PARAMS_MAX_TIMES = "maxTimes";
	public static final String JOB_PARAMS_CLASS_NAME = "jobClassName";
	public static final String JOB_PARAMS_PROC_ID = "processorId";
	public static final String JOB_PARAMS_REQ_MSG_FROM = "reqeustMessageFrom";
	public static final String JOB_PARAMS_END_FLAG = "scheduleEndFlag";
	public static final String JOB_PARAMS_CRON_RULE_EXPR = "cronRuleExpression";

	public static final String SECURITY_RULE_BUSS_RELATION = "securityRuleBusinessRelation";
	public static final String SECURITY_RULE_BUSS_RELATION_ORG_ID = "orgId";
	public static final String SECURITY_RULE_BUSS_RELATION_PRODUCT_ID = "productId";
	public static final String SECURITY_RULE_BUSS_RELATION_RULE_ID = "ruleId";
	public static final String SECURITY_RULE_BUSS_RELATION_SYS_VER = "systemVersion";
	public static final String SECURITY_RULE_BUSS_RELATION_INFO = "info";
	public static final String SECURITY_CTRL_RULE = "securityControlRule";
	public static final String SECURITY_CTRL_RULE_ID = "id";
	public static final String SECURITY_CTRL_RULE_MODE = "mode";
	public static final String SECURITY_CTRL_RULE_SECRET_KEY_ID = "secretKeyId";
	public static final String SECURITY_CTRL_RULE_ENCRYPTOR_ID = "encryptorId";
	public static final String SECURITY_CTRL_RULE_MAC_FUNC = "macFunction";
	public static final String SECURITY_CTRL_RULE_VERIFY_MAC_FUNC = "verifyMacFunction";
	public static final String SECURITY_CTRL_RULE_MSG_ENCRYPT_FUNC = "messageEncryptFunction";
	public static final String SECURITY_CTRL_RULE_MSG_DECRYPT_FUNC = "messageDecryptFunction";
	public static final String SECURITY_CTRL_RULE_PIN_ENCRYPT_FUNC = "pinEncryptFunction";
	public static final String SECURITY_CTRL_RULE_PIN_DECRYPT_FUNC = "pinDecryptFunction";
	public static final String SECURITY_CTRL_RULE_PIN_EXCHANGE_FUNC = "pinExchangeFunction";
	public static final String SECURITY_CTRL_RULE_UPDATE_KEY_FUNC = "updateKeyFunction";
	public static final String SECURITY_CTRL_RULE_GEN_WK_FUNC = "genWkFunction";
	public static final String SECURITY_CTRL_RULE_VERIFY_CVV_FUNC = "verifyCvvFunction";
	public static final String SECURITY_CTRL_RULE_VERIFY_PVV_FUNC = "verifyPvvFunction";
	public static final String SECURITY_CTRL_RULE_VERIFY_CVVPVV_FUNC = "verifyCvvpvvFunction";
	public static final String SECURITY_CTRL_RULE_CHECK_KEY_CHECK_VALUE = "checkKeyCheckValue";
	public static final String SECURITY_CTRL_RULE_MEMO = "memo";
	public static final String ENCRYPTOR = "encryptor";
	public static final String ENCRYPTOR_ID = "id";
	public static final String ENCRYPTOR_MEMO = "memo";
	public static final String ENCRYPTOR_IP = "ip";
	public static final String ENCRYPTOR_PORT = "port";
	public static final String ENCRYPTOR_MODEL = "model";
	public static final String SECRET_KEY = "securityKey";
	public static final String SECRET_KEY_ID = "id";
	public static final String SECRET_KEY_TYPE = "type";
	public static final String SECRET_KEY_BMK_INDEX = "bmkIndex";
	public static final String SECRET_KEY_BANK_PIN_KEY = "bankPinKey";
	public static final String SECRET_KEY_BANK_PIN_TYPE = "bankPinType";
	public static final String SECRET_KEY_BANK_MAC_KEY = "bankMacKey";
	public static final String SECRET_KEY_BANK_MSG_KEY = "bankMsgKey";
	public static final String SECRET_KEY_APP_MASTER_KEY = "appMasterKey";
	public static final String SECRET_KEY_APP_MASTER_KEY_INDEX = "appMasterKeyIndex";
	public static final String SECRET_KEY_APP_PIN_KEY = "appPinKey";
	public static final String SECRET_KEY_APP_PIN_KEY_REPLACE = "appPinKeyReplace";
	public static final String SECRET_KEY_APP_PIN_KEY_STATE = "appPinKeyState";
	public static final String SECRET_KEY_APP_PIN_TYPE = "appPinType";
	public static final String SECRET_KEY_APP_MAC_KEY = "appMacKey";
	public static final String SECRET_KEY_APP_MAC_KEY_REPLACE = "appMacKeyReplace";
	public static final String SECRET_KEY_APP_MAC_KEY_STATE = "appMacKeyState";
	public static final String SECRET_KEY_APP_MSG_KEY = "appMsgKey";
	public static final String SECRET_KEY_APP_MSG_KEY_REPLACE = "appMsgKeyReplace";
	public static final String SECRET_KEY_APP_MSG_KEY_STATE = "appMsgKeyState";
	public static final String SECRET_KEY_APP_KEY1 = "appKey1";
	public static final String SECRET_KEY_APP_KEY2 = "appKey2";
	public static final String SECRET_KEY_RESERVE1 = "reserve1";
	public static final String SECRET_KEY_RESERVE2 = "reserve2";
	public static final String SECURITY_ARITHMETIC = "securityArithmetic";
	public static final String SECURITY_ARITHMETIC_ID = "id";
	public static final String SECURITY_ARITHMETIC_SECURITY_CTRL_RULE_ID = "securityControlRuleId";
	public static final String SECURITY_ARITHMETIC_FUNC_ID = "functionId";
	public static final String SECURITY_ARITHMETIC_TYPE = "type";
	public static final String SECURITY_ARITHMETIC_NAME = "name";
	public static final String SECURITY_ARITHMETIC_SERV_PROXY = "serviceProxy";
	public static final String SECURITY_ARITHMETIC_SERV_TYPE = "serviceType";
	public static final String SECURITY_ARITHMETIC_PARAM_ID = "parameterId";
	public static final String SERV_PARAM = "serviceParameter";
	public static final String SERV_PARAM_ID = "id";
	public static final String SERV_PARAM_SECURITY_PARAM_ID = "securityParameterId";
	public static final String SERV_PARAM_PARAMS_NUM = "parametersNumber";
	public static final String SERV_PARAM_PARAM_NAME = "parameterName";
	public static final String SERV_PARAM_PARAM_CLASS = "parameterClass";
	public static final String SERV_PARAM_PARAM_VALUE = "parameterValue";
	public static final String SECURITY_SEARCH_RESULT = "result";

	public static final String RESEND_SESSION_ID = "sessionId";
	public static final String RESEND_RET_SESSION_ID = "returnSessionId";

}
