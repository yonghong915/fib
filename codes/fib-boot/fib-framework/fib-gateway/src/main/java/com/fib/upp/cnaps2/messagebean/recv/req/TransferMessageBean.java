package com.fib.upp.cnaps2.messagebean.recv.req;

import com.fib.commons.exception.CommonException;
import com.fib.commons.util.CommUtils;
import com.fib.gateway.message.bean.MessageBean;
import com.fib.gateway.message.xml.message.MultiLanguageResourceBundle;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转换MessageBean
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class TransferMessageBean extends MessageBean {

	// 报文编号
	private String msgNo;

	// 账号
	private String accountNo;

	// 账务日期
	private String accountDate;

	// 账户余额
	private String accountBalance;

	// 余额方向
	private String balanceDirection;

	@Override
	public Object getAttribute(String name) {
		if ("msgNo".equals(name)) {
			return this.getMsgNo();
		} else if ("accountNo".equals(name)) {
			return this.getAccountNo();
		} else if ("accountDate".equals(name)) {
			return this.getAccountDate();
		} else if ("accountBalance".equals(name)) {
			return this.getAccountBalance();
		} else if ("balanceDirection".equals(name)) {
			return this.getBalanceDirection();
		}
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		if ("msgNo".equals(name)) {
			this.setMsgNo((String) value);
		} else if ("accountNo".equals(name)) {
			this.setAccountNo((String) value);
		} else if ("accountDate".equals(name)) {
			this.setAccountDate((String) value);
		} else if ("accountBalance".equals(name)) {
			this.setAccountBalance((String) value);
		} else if ("balanceDirection".equals(name)) {
			this.setBalanceDirection((String) value);
		}
	}

	@Override
	public void cover(MessageBean bean) {
		TransferMessageBean newBean = (TransferMessageBean) bean;

		if (CommUtils.isNotEmpty(newBean.getMsgNo())) {
			msgNo = newBean.getMsgNo();
		}

		if (CommUtils.isNotEmpty(newBean.getAccountNo())) {
			accountNo = newBean.getAccountNo();
		}

		if (CommUtils.isNotEmpty(newBean.getAccountDate())) {
			accountDate = newBean.getAccountDate();
		}

		if (CommUtils.isNotEmpty(newBean.getAccountBalance())) {
			accountBalance = newBean.getAccountBalance();
		}

		if (CommUtils.isNotEmpty(newBean.getBalanceDirection())) {
			balanceDirection = newBean.getBalanceDirection();
		}
	}

	@Override
	public void validate() {
		// 报文编号不为空则按以下规则校验

		if (CommUtils.isNotEmpty(this.msgNo) && !CommUtils.validateLen(msgNo, "GBK", 4)) {
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength b",
					new String[] { "报文编号(msgNo)", "4" }));
		}

		if (CommUtils.isNotEmpty(this.accountNo) && !CommUtils.validateLen(accountNo, "GBK", 32)) {
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength b",
					new String[] { "账号(accountNo)", "32" }));
		}

		if (CommUtils.isNotEmpty(this.accountDate) && !CommUtils.validateLen(accountDate, "GBK", 8)) {
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength b",
					new String[] { "账务日期(accountDate)", "8" }));
		}

		if (CommUtils.isNotEmpty(this.accountBalance) && !CommUtils.validateLen(accountBalance, "GBK", 18)) {
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength b",
					new String[] { "账户余额(accountBalance)", "18" }));
		}

		if (CommUtils.isNotEmpty(this.balanceDirection) && !CommUtils.validateLen(balanceDirection, "GBK", 1)) {
			throw new CommonException(MultiLanguageResourceBundle.getInstance().getString("Message.field.maxLength b",
					new String[] { "余额方向(balanceDirection)", "1" }));
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

	@Override
	public String toString(boolean isWrap, boolean isTable) {
		StringBuilder buf = new StringBuilder(10240);
		if (CommUtils.isNotEmpty(msgNo)) {
			buf.append("<a n=\"msgNo\" t=\"str\">");
			buf.append(msgNo);
			buf.append("</a>");
		}
		if (CommUtils.isNotEmpty(accountNo)) {
			buf.append("<a n=\"accountNo\" t=\"str\">");
			buf.append(accountNo);
			buf.append("</a>");
		}
		if (CommUtils.isNotEmpty(accountDate)) {
			buf.append("<a n=\"accountDate\" t=\"str\">");
			buf.append(accountDate);
			buf.append("</a>");
		}
		if (CommUtils.isNotEmpty(accountBalance)) {
			buf.append("<a n=\"accountBalance\" t=\"str\">");
			buf.append(accountBalance);
			buf.append("</a>");
		}
		if (CommUtils.isNotEmpty(balanceDirection)) {
			buf.append("<a n=\"balanceDirection\" t=\"str\">");
			buf.append(balanceDirection);
			buf.append("</a>");
		}

		if (0 == buf.length()) {
			return null;
		} else {
			if (isTable) {
				buf.insert(0, "<b>");
			} else {
				buf.insert(0, "<b c=\"com.fib.upp.cnaps2.messagebean.recv.req.TransferMessageBean\">");
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

	@Override
	public boolean isNull() {
		if (CommUtils.isNotEmpty(msgNo)) {
			return false;
		}

		if (CommUtils.isNotEmpty(accountNo)) {
			return false;
		}

		if (CommUtils.isNotEmpty(accountDate)) {
			return false;
		}

		if (CommUtils.isNotEmpty(accountBalance)) {
			return false;
		}

		if (CommUtils.isNotEmpty(balanceDirection)) {
			return false;
		}
		return true;
	}
}
