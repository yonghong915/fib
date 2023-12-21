package com.fib.uias.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fib.core.base.entity.BaseEntity;

import lombok.EqualsAndHashCode;

/**
 * 用户实体
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-18
 */
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

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
}