package com.fib.upp.entity;

import java.io.Serial;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("communication_event")
public class CommunicationEventEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 8599456880045657648L;

	@TableId("comm_event_id")
	private String commEventId;

	@TableField("comm_event_type_id")
	private String commEventTypeId;

	@TableField("content_id")
	private String contentId;

	public String getCommEventId() {
		return commEventId;
	}

	public void setCommEventId(String commEventId) {
		this.commEventId = commEventId;
	}

	public String getCommEventTypeId() {
		return commEventTypeId;
	}

	public void setCommEventTypeId(String commEventTypeId) {
		this.commEventTypeId = commEventTypeId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

}
