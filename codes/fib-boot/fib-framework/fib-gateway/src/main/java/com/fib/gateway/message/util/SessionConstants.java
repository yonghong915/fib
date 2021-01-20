package com.fib.gateway.message.util;

import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

public class SessionConstants {
	// 外部流水号
	public static final String EXTERNAL_SERIAL_NUM = "EXTERNALSERIALNUM";
	// 通讯成功
	public static final String STATE_COMMUNICATE_SUCCESS = "0000";
	public static final String STATE_COMMUNICATE_SUCCESS_TEXT = "COMMUNICATE_SUCCESS";
	// 业务成功
	public static final String STATE_BUSINESS_SUCCESS = "0016";
	public static final String STATE_BUSINESS_SUCCESS_TEXT = "BUSINESS_SUCCESS";
	// 源请求报文解包异常
	public static final String STATE_SRC_REQ_MSG_PARSE = "0002";
	public static final String STATE_SRC_REQ_MSG_PARSE_TEXT = "SRC_REQ_MSG_PARSE";
	// 请求报文映射异常
	public static final String STATE_REQ_MSG_MAPPING = "0004";
	public static final String STATE_REQ_MSG_MAPPING_TEXT = "REQ_MSG_MAPPING";
	// 目的请求报文组包异常
	public static final String STATE_DST_REQ_MSG_PACK = "0005";
	public static final String STATE_DST_REQ_MSG_PACK_TEXT = "DST_REQ_MSG_PACK";
	// 目的通道连接异常
	public static final String STATE_DST_CHN_CONNECT = "0006";
	public static final String STATE_DST_CHN_CONNECT_TEXT = "DST_CHN_CONNECT";
	// 请求报文发送异常
	public static final String STATE_REQ_MSG_SEND = "0007";
	public static final String STATE_REQ_MSG_SEND_TEXT = "REQ_MSG_SEND";
	// 回应报文接收异常
	public static final String STATE_RSP_MSG_RECEIVE = "0008";
	public static final String STATE_RSP_MSG_RECEIVE_TEXT = "RSP_MSG_RECEIVE";
	// 返回码识别异常
	public static final String STATE_RET_CODE_RECOGNIZE = "0009";
	public static final String STATE_RET_CODE_RECOGNIZE_TEXT = "RET_CODE_RECOGNIZE";
	// 目的回应报文解包异常
	public static final String STATE_DST_RSP_MSG_PARSE = "0010";
	public static final String STATE_DST_RSP_MSG_PARSE_TEXT = "DST_RSP_MSG_PARSE";
	// 回应报文映射异常
	public static final String STATE_RSP_MSG_MAPPING = "0011";
	public static final String STATE_RSP_MSG_MAPPING_TEXT = "RSP_MSG_MAPPING";
	// 源回应报文组包异常
	public static final String STATE_SRC_RSP_MSG_PACK = "0012";
	public static final String STATE_SRC_RSP_MSG_PACK_TEXT = "SRC_RSP_MSG_PACK";
	// 源回应报文发送异常
	public static final String STATE_SRC_RSP_MSG_SEND = "0013";
	public static final String STATE_SRC_RSP_MSG_SEND_TEXT = "SRC_RSP_MSG_SEND";
	// 请求报文处理异常
	public static final String STATE_REQ_MSG_HANDLE = "0014";
	public static final String STATE_REQ_MSG_HANDLE_TEXT = "REQ_MSG_HANDLE";
	// 回应报文处理异常
	public static final String STATE_RSP_MSG_HANDLE = "0015";
	public static final String STATE_RSP_MSG_HANDLE_TEXT = "RSP_MSG_HANDLE";
	// 报文类型识别异常
	public static final String STATE_MSG_TYP_RECOGNIZE = "0001";
	public static final String STATE_MSG_TYP_RECOGNIZE_TEXT = "MSG_TYP_RECOGNIZE";
	// 动态路由选择异常
	public static final String STATE_DYNAMIC_ROUTE_DETERMINE = "0003";
	public static final String STATE_DYNAMIC_ROUTE_DETERMINE_TEXT = "DYNAMIC_ROUTE_DETERMINE";
	// 创建会话错误，只有当网关支持数据库时会出现
	public static final String STATE_CREATE_SESSION = "0017";
	public static final String STATE_CREATE_SESSION_TEXT = "CREATE_SESSION";
	// 业务失败
	public static final String STATE_BUSINESS_FAILED = "0018";
	public static final String STATE_BUSINESS_FAILED_TEXT = "BUSINESS_FAILED";
	// 不存在业务失败报文处理规则
	public static final String STATE_NO_ERROR_MSG_TRANSFORMER = "0019";
	public static final String STATE_NO_ERROR_MSG_TRANSFORMER_TEXT = "NO_ERROR_MESSAGE_TRANSFORMER";
	// 未知错误j
	public static final String STATE_UNKOWN_ERROR = "9999";
	public static final String STATE_UNKOWN_ERROR_TEXT = "UNKOWN_ERROR";

	public static String getStateText(String state) {
		if (STATE_SRC_REQ_MSG_PARSE.equals(state)) {
			return STATE_SRC_REQ_MSG_PARSE_TEXT;
		} else if (STATE_REQ_MSG_MAPPING.equals(state)) {
			return STATE_REQ_MSG_MAPPING_TEXT;
		} else if (STATE_DST_REQ_MSG_PACK.equals(state)) {
			return STATE_DST_REQ_MSG_PACK_TEXT;
		} else if (STATE_DST_CHN_CONNECT.equals(state)) {
			return STATE_DST_CHN_CONNECT_TEXT;
		} else if (STATE_REQ_MSG_SEND.equals(state)) {
			return STATE_REQ_MSG_SEND_TEXT;
		} else if (STATE_RSP_MSG_RECEIVE.equals(state)) {
			return STATE_RSP_MSG_RECEIVE_TEXT;
		} else if (STATE_RET_CODE_RECOGNIZE.equals(state)) {
			return STATE_RET_CODE_RECOGNIZE_TEXT;
		} else if (STATE_DST_RSP_MSG_PARSE.equals(state)) {
			return STATE_DST_RSP_MSG_PARSE_TEXT;
		} else if (STATE_RSP_MSG_MAPPING.equals(state)) {
			return STATE_RSP_MSG_MAPPING_TEXT;
		} else if (STATE_SRC_RSP_MSG_PACK.equals(state)) {
			return STATE_SRC_RSP_MSG_PACK_TEXT;
		} else if (STATE_SRC_RSP_MSG_SEND.equals(state)) {
			return STATE_SRC_RSP_MSG_SEND_TEXT;
		} else if (STATE_REQ_MSG_HANDLE.equals(state)) {
			return STATE_REQ_MSG_HANDLE_TEXT;
		} else if (STATE_RSP_MSG_HANDLE.equals(state)) {
			return STATE_RSP_MSG_HANDLE_TEXT;
		} else if (STATE_MSG_TYP_RECOGNIZE.equals(state)) {
			return STATE_MSG_TYP_RECOGNIZE_TEXT;
		} else if (STATE_DYNAMIC_ROUTE_DETERMINE.equals(state)) {
			return STATE_DYNAMIC_ROUTE_DETERMINE_TEXT;
		} else if (STATE_COMMUNICATE_SUCCESS.equals(state)) {
			return STATE_COMMUNICATE_SUCCESS_TEXT;
		} else if (STATE_BUSINESS_SUCCESS.equals(state)) {
			return STATE_BUSINESS_SUCCESS_TEXT;
		} else if (STATE_CREATE_SESSION.equals(state)) {
			return STATE_CREATE_SESSION_TEXT;
		} else if (STATE_BUSINESS_FAILED.equals(state)) {
			return STATE_BUSINESS_FAILED_TEXT;
		} else {
			// throw new IllegalArgumentException("Unkown Event Type: " +
			// state);
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("EventTypeConstants.eventType.unkown", new String[] { state }));
		}
	}

	public static String getStateByText(String state) {
		if (STATE_SRC_REQ_MSG_PARSE_TEXT.equals(state)) {
			return STATE_SRC_REQ_MSG_PARSE;
		} else if (STATE_REQ_MSG_MAPPING_TEXT.equals(state)) {
			return STATE_REQ_MSG_MAPPING;
		} else if (STATE_DST_REQ_MSG_PACK_TEXT.equals(state)) {
			return STATE_DST_REQ_MSG_PACK;
		} else if (STATE_DST_CHN_CONNECT_TEXT.equals(state)) {
			return STATE_DST_CHN_CONNECT;
		} else if (STATE_REQ_MSG_SEND_TEXT.equals(state)) {
			return STATE_REQ_MSG_SEND;
		} else if (STATE_RSP_MSG_RECEIVE_TEXT.equals(state)) {
			return STATE_RSP_MSG_RECEIVE;
		} else if (STATE_RET_CODE_RECOGNIZE_TEXT.equals(state)) {
			return STATE_RET_CODE_RECOGNIZE;
		} else if (STATE_DST_RSP_MSG_PARSE_TEXT.equals(state)) {
			return STATE_DST_RSP_MSG_PARSE;
		} else if (STATE_RSP_MSG_MAPPING_TEXT.equals(state)) {
			return STATE_RSP_MSG_MAPPING;
		} else if (STATE_SRC_RSP_MSG_PACK_TEXT.equals(state)) {
			return STATE_SRC_RSP_MSG_PACK;
		} else if (STATE_SRC_RSP_MSG_SEND_TEXT.equals(state)) {
			return STATE_SRC_RSP_MSG_SEND;
		} else if (STATE_REQ_MSG_HANDLE_TEXT.equals(state)) {
			return STATE_REQ_MSG_HANDLE;
		} else if (STATE_RSP_MSG_HANDLE_TEXT.equals(state)) {
			return STATE_RSP_MSG_HANDLE;
		} else if (STATE_MSG_TYP_RECOGNIZE_TEXT.equals(state)) {
			return STATE_MSG_TYP_RECOGNIZE;
		} else if (STATE_DYNAMIC_ROUTE_DETERMINE_TEXT.equals(state)) {
			return STATE_DYNAMIC_ROUTE_DETERMINE;
		} else if (STATE_COMMUNICATE_SUCCESS_TEXT.equals(state)) {
			return STATE_COMMUNICATE_SUCCESS;
		} else if (STATE_BUSINESS_SUCCESS_TEXT.equals(state)) {
			return STATE_BUSINESS_SUCCESS;
		} else if (STATE_CREATE_SESSION_TEXT.equals(state)) {
			return STATE_CREATE_SESSION;
		} else if (STATE_BUSINESS_FAILED_TEXT.equals(state)) {
			return STATE_BUSINESS_FAILED;
		} else {
			// throw new IllegalArgumentException("Unkown Event Type: " +
			// state);
			throw new IllegalArgumentException(MultiLanguageResourceBundle.getInstance()
					.getString("EventTypeConstants.eventType.unkown", new String[] { state }));
		}
	}

	// 通讯日志报文表-报文种类 源请求报文
	public static final String SRC_REQ = "10";
	// 通讯日志报文表-报文种类 目的请求报文
	public static final String DST_REQ = "11";
	// 通讯日志报文表-报文种类 源回应报文
	public static final String SRC_RES = "20";
	// 通讯日志报文表-报文种类 目的回应报文
	public static final String DST_RES = "21";
	// 通讯日志报文表-报文种类 错误信息
	public static final String ERR_MSG = "99";
}
