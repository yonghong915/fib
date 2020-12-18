package com.fib.uias.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fib.core.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_USER")
public class UserEntity extends BaseEntity {
	private static final long serialVersionUID = -7657083983061154971L;
	/** 用户编号 */
	private String userCode;

	/** 用户真实姓名 */
	private String realName;

	/** 用户描述 */
	private String userDesc;

	/** 用户类型 */
	private Integer userType;

	/** 用户状态 */
	private Integer userStatus;
}
