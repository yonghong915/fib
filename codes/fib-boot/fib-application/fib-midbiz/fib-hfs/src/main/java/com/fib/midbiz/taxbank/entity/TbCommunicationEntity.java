package com.fib.midbiz.taxbank.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("t_communication")
public class TbCommunicationEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6406223608084661527L;
	private String name;
    public TbCommunicationEntity(){
		
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
