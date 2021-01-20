package com.fib.gateway.message.bean;

/**
 * 测试报文体-转帐
 */
public class ErrorMessage extends MessageBean{
	//测试用
	private int length;
	
	public int getLength(){
		return length;
	}
	
	public void setLength(int l){
		this.length = l;
	}
	//测试用

	//错误码
	private String errorCode;

	public String getErrorCode(){
		return errorCode;
	}

	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	//错误信息
	private String errorInfo;

	public String getErrorInfo(){
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo){
		this.errorInfo = errorInfo;
	}

	public Object getAttribute(String name){

		if("errorCode".equals(name)){
			return  this.getErrorCode();
		}else if("errorInfo".equals(name)){
			return  this.getErrorInfo();
		}
		return null;
}
	public void validate(){
		//错误码非空检查
		if ( null == errorCode){
			throw new RuntimeException(
				"errorCode must not be null!");
		}

			//错误码数据长度检查
			if ( errorCode.getBytes().length > 1 ) {
				throw new RuntimeException(
					"errorCode's max length is 1!");
			}

		//错误信息非空检查
		if ( null == errorInfo){
			throw new RuntimeException(
				"errorInfo must not be null!");
		}

			//错误信息长度检查
			if ( errorInfo.getBytes().length > 9999 ) {
				throw new RuntimeException(
					"errorInfo's max length is 9999!");
			}
		
	}

	public String toString(){
		return toString(false);
	}

	public String toString(boolean isWrap){
		return toString(isWrap,false);
	}

	public String toString(boolean isWrap,boolean isTable){
		StringBuffer buf = new StringBuffer(10240);
		if( null != errorCode){
			buf.append("<a n=\"errorCode\" t=\"str\">");
			buf.append(errorCode);
			buf.append("</a>");
		}
		if( null != errorInfo){
			buf.append("<a n=\"errorInfo\" t=\"str\">");
			buf.append(errorInfo);
			buf.append("</a>");
		}
		if( 0 == buf.length()){
			return null;
		}else{
			if ( isTable ){
				buf.insert(0,"<b>");
			}else{
				buf.insert(0,"<b c=\"com.giantstone.commgateway.message.ErrorMessage\">");
			}
			buf.append("</b>");
			if( !isWrap ){
				buf = new StringBuffer(buf.toString());
				buf.insert(0,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				return buf.toString();
			}else{
				return buf.toString();
			}
		}
	}
}
