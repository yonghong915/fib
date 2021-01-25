package com.fib.upp.pay.beps.pack;

import java.util.List;

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
public class BepsQueueItem {
	/***/
	private Long queueId;
	/***/
	private String seqNo;
	/***/
	private String recordType;
	/***/
	private String recordId;
	/***/
	private String messageType;
	/***/
	private String status;
	/***/
	private List<String> recordIds;

	public BepsQueueItem() {
	}

	public BepsQueueItem(String messageType, String recordId) {
		this.messageType = messageType;
		this.recordId = recordId;
	}
}
