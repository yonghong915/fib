package com.fib.gateway.message.bean;


public class ReturnCodeConstant {
	//成功
	public static final String SUCCESS = "000";
	//请求发送异常
	public static final String REQUEST_SEND_ERROR = "001";
	//应答方连接异常
	public static final String CONNECT_TO_RESPONSE_ERROR = "002";
	//接收应答异常
	public static final String RESPONSE_RECEIVE_ERROR = "003";
	//网关内部异常
	public static final String INTERNAL_ERROR = "004";
	//未知异常
	public static final String UNKOWN_ERROR = "999";
	
	public static boolean isSuccessReturnCode(String retCode){
		if(SUCCESS.equals(retCode)){
			return true;
		}else{
			return false;
		}
	}
	
	public static String getRetCodeBySessionState(int state){
		switch(state){
		default:
			return SUCCESS;
		}
	}
}
