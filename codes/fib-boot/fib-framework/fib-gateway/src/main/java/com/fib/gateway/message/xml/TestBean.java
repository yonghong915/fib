package com.fib.gateway.message.xml;

import com.fib.gateway.message.bean.MessageBean;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class TestBean extends MessageBean {

	@Override
	public void validate() {
		//
	}

	private String accountNo;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	// 账务日期
	private String accountDate;

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	@Override
	public Object getAttribute(String name) {
		if ("accountNo".equals(name)) {
			return this.getAccountNo();
		} else if ("accountDate".equals(name)) {
			return this.getAccountDate();
		}
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		if ("accountNo".equals(name)) {
			this.setAccountNo((String) value);
		} else if ("accountDate".equals(name)) {
			this.setAccountDate((String) value);
		}
	}

	@Override
	public String toString() {
		return toString(false);
	}

	@Override
	public String toString(boolean isWrap) {
		return toString(isWrap, false);
	}

	public String toString(boolean isWrap, boolean isTable) {
		StringBuilder buf = new StringBuilder(10240);
		if (null != accountNo) {
			buf.append("<a n=\"accountNo\" t=\"str\">");
			buf.append(accountNo);
			buf.append("</a>");
		}
		if (null != accountDate) {
			buf.append("<a n=\"accountDate\" t=\"str\">");
			buf.append(accountDate);
			buf.append("</a>");
		}
		if (0 == buf.length()) {
			return null;
		} else {
			if (isTable) {
				buf.insert(0, "<b>");
			} else {
				buf.insert(0,
						"<b c=\"cmbcagent.liangshan.acs.MB_cmbcagent_liangshan_acsBusiness_send_bipACSE1006_Req\">");
			}
			buf.append("</b>");
			if (!isWrap) {
				buf = new StringBuilder(buf.toString());
				buf.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				return buf.toString();
			} else {
				return buf.toString();
			}
		}
	}
}
