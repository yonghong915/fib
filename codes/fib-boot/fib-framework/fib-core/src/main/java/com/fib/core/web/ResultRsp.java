package com.fib.core.web;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author fangyh
 * @since 2020-12-14
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultRsp<T> implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -555255018658481687L;
	/**
	 * 返回码值,默认值Const.FAIL
	 */
	@JsonProperty("rspCode")
	private String rspCode;
	/**
	 * 返回码值解析
	 */
	@JsonProperty("rspMsg")
	private String rspMsg;
	/**
	 * 返回对象
	 */
	@JsonProperty("data")
	private transient T rspObj;

	/**
	 * 时间戳
	 */
	@JsonProperty("timestamp")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public ResultRsp() {
		// do nothing
	}

	public ResultRsp<T> setRsp(String code, String msg, T data) {
		this.setRspCode(code);
		this.setRspMsg(msg);
		if (null != data) {
			this.setRspObj(data);
		}
		return this;
	}

	public ResultRsp<T> success(String message) {
		this.rspMsg = message;
		// this.rspCode = CommonConstant.SC_OK_200;
		// this.success = true;
		return this;
	}

	public ResultRsp<T> message(RestStatus statusCodes, T data) {
		this.setRspCode(statusCodes.code());
		this.setRspMsg(statusCodes.message());
		if (null != data) {
			this.setRspObj(data);
		}
		return this;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public T getRspObj() {
		return rspObj;
	}

	public void setRspObj(T rspObj) {
		this.rspObj = rspObj;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ResultRsp [rspCode=" + rspCode + ", rspMsg=" + rspMsg + ", rspObj=" + rspObj + "]";
	}
}
