package com.fib.upp.entity.cnaps;

import java.math.BigInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fib.core.base.dto.BaseDTO;

import cn.hutool.core.builder.EqualsBuilder;
import cn.hutool.core.builder.HashCodeBuilder;

/**
 * 人行系统状态
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-25 09:17:18
 */
public class CnapsSystemStatus extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7517082706903486677L;

	/** 主键 */
	@TableId("pk_id")
	private BigInteger pkId;

	/** 系统编码 */
	private String systemCode;

	/** 清算行号 */
	private String clearBankNo;

	/** 系统当前日期 */
	private String currentSystemDate;

	/** 系统当前状态 */
	private String currentSystemStatus;

	/** 节假日标志 */
	private String holidayFlag;

	/** 登录标志 */
	private String loginOperationType;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		CnapsSystemStatus rhs = (CnapsSystemStatus) obj;
		return new EqualsBuilder().append(this.systemCode, rhs.systemCode).append(this.clearBankNo, rhs.clearBankNo)
				.append(this.currentSystemDate, rhs.currentSystemDate)
				.append(this.currentSystemStatus, rhs.getCurrentSystemStatus())
				.append(this.holidayFlag, rhs.holidayFlag).append(this.loginOperationType, rhs.loginOperationType)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getClearBankNo() {
		return clearBankNo;
	}

	public void setClearBankNo(String clearBankNo) {
		this.clearBankNo = clearBankNo;
	}

	public String getCurrentSystemDate() {
		return currentSystemDate;
	}

	public void setCurrentSystemDate(String currentSystemDate) {
		this.currentSystemDate = currentSystemDate;
	}

	public String getCurrentSystemStatus() {
		return currentSystemStatus;
	}

	public void setCurrentSystemStatus(String currentSystemStatus) {
		this.currentSystemStatus = currentSystemStatus;
	}

	public String getHolidayFlag() {
		return holidayFlag;
	}

	public void setHolidayFlag(String holidayFlag) {
		this.holidayFlag = holidayFlag;
	}

	public String getLoginOperationType() {
		return loginOperationType;
	}

	public void setLoginOperationType(String loginOperationType) {
		this.loginOperationType = loginOperationType;
	}
}
