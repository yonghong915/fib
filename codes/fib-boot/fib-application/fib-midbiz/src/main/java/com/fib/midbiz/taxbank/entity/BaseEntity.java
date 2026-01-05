package com.fib.midbiz.taxbank.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

public abstract class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigInteger id;
	private Timestamp createDt;
	private BigInteger createBy;
	private Timestamp updateDt;
	private BigInteger updateBy;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Timestamp getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public BigInteger getCreateBy() {
		return createBy;
	}

	public void setCreateBy(BigInteger createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Timestamp updateDt) {
		this.updateDt = updateDt;
	}

	public BigInteger getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(BigInteger updateBy) {
		this.updateBy = updateBy;
	}
}
