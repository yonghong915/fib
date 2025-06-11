package com.fib.midbiz.taxbank.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName("t_communication")
public class TbCommunicationEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6406223608084661527L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
