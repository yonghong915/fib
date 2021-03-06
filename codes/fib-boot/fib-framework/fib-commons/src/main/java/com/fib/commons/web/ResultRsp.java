package com.fib.commons.web;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @since 2020-12-14
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
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

	public ResultRsp<T> message(RestStatus statusCodes, T data) {
		this.setRspCode(statusCodes.code());
		this.setRspMsg(statusCodes.message());
		if (null != data) {
			this.setRspObj(data);
		}
		return this;
	}

	@Override
	public String toString() {
		return "ResultRsp [rspCode=" + rspCode + ", rspMsg=" + rspMsg + ", rspObj=" + rspObj + "]";
	}
}
