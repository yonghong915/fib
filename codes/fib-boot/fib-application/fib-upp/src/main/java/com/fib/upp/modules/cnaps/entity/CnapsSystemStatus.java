package com.fib.upp.modules.cnaps.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fib.core.base.entity.BaseEntity;

import cn.hutool.core.builder.EqualsBuilder;
import cn.hutool.core.builder.HashCodeBuilder;

/**
 * 人行系统状态
 * 
 * @author fangyh
 * @version 1.0
 * @date 2022-03-25 09:17:18
 */
public class CnapsSystemStatus extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7517082706903486677L;

	/** 主键 */
	@TableId("pk_id")
	private Long pkId;

	/** 系统编码 */
	private String sysCode;

	/** 原系统日期 */
	private String origSysDate;

	/** 原系统状态 */
	private String origSysStatus;

	/** 清算行号 */
	private String clearBankCode;

	/** 系统当前日期 */
	private String currentSysDate;

	/** 系统当前状态 */
	private String currentSysStatus;

	/** 节假日标志 */
	private String holidayFlag;

	private String specialWorkDayFlag;

	/** 系统下一日期 */
	private String nextSysDate;

	/** 登录标志 */
	private String loginOperType;

	private String processCode;

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
		return new EqualsBuilder().append(this.sysCode, rhs.sysCode).append(this.clearBankCode, rhs.clearBankCode)
				.append(this.currentSysDate, rhs.currentSysDate)
				.append(this.currentSysStatus, rhs.getCurrentSysStatus()).append(this.holidayFlag, rhs.holidayFlag)
				.append(this.loginOperType, rhs.loginOperType).isEquals();
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Long getPkId() {
		return pkId;
	}

	public void setPkId(Long pkId) {
		this.pkId = pkId;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getOrigSysDate() {
		return origSysDate;
	}

	public void setOrigSysDate(String origSysDate) {
		this.origSysDate = origSysDate;
	}

	public String getOrigSysStatus() {
		return origSysStatus;
	}

	public void setOrigSysStatus(String origSysStatus) {
		this.origSysStatus = origSysStatus;
	}

	public String getClearBankCode() {
		return clearBankCode;
	}

	public void setClearBankCode(String clearBankCode) {
		this.clearBankCode = clearBankCode;
	}

	public String getCurrentSysDate() {
		return currentSysDate;
	}

	public void setCurrentSysDate(String currentSysDate) {
		this.currentSysDate = currentSysDate;
	}

	public String getCurrentSysStatus() {
		return currentSysStatus;
	}

	public void setCurrentSysStatus(String currentSysStatus) {
		this.currentSysStatus = currentSysStatus;
	}

	public String getHolidayFlag() {
		return holidayFlag;
	}

	public void setHolidayFlag(String holidayFlag) {
		this.holidayFlag = holidayFlag;
	}

	public String getSpecialWorkDayFlag() {
		return specialWorkDayFlag;
	}

	public void setSpecialWorkDayFlag(String specialWorkDayFlag) {
		this.specialWorkDayFlag = specialWorkDayFlag;
	}

	public String getNextSysDate() {
		return nextSysDate;
	}

	public void setNextSysDate(String nextSysDate) {
		this.nextSysDate = nextSysDate;
	}

	public String getLoginOperType() {
		return loginOperType;
	}

	public String getProcessCode() {
		return processCode;
	}

	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}

	public void setLoginOperType(String loginOperType) {
		this.loginOperType = loginOperType;
	}
}