package com.fib.upp.pay.beps.pack;

import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
@Data
public class BepsQueueHeader {
	/***/
	private Long pkId;
	/***/
	private String queueType;
	/***/
	private String status;

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getQueueType() {
		return queueType;
	}

	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
