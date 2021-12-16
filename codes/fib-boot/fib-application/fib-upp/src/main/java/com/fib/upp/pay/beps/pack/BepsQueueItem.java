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

	public Long getQueueId() {
		return queueId;
	}

	public void setQueueId(Long queueId) {
		this.queueId = queueId;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(List<String> recordIds) {
		this.recordIds = recordIds;
	}

}
