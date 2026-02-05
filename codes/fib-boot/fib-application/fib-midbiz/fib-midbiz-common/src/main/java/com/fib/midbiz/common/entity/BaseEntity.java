package com.fib.midbiz.common.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;

public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = -1245568151207138232L;

	/** 主键ID */
	@TableId
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
