package com.fib.upp.pay.beps.pack;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2021-01-25
 */
@Data
@AllArgsConstructor
public class BepsQueue {
	/***/
	private String queueType;
	/***/
	private String queueStatus;

	public BepsQueue() {
	}

	public BepsQueue(String queueType, String queueStatus) {
		this.queueType = queueType;
		this.queueStatus = queueStatus;
	}

	public String getQueueType() {
		return queueType;
	}

	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

	public String getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}
}
