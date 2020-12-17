package com.fib.uias.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fib.core.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_USER")
public class UserEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7657083983061154971L;

	private String userCode;
}
