package com.fib.upp.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("biz_tb_clob")
public class BizTbClobEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1014192614733038033L;

	private String id;
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}