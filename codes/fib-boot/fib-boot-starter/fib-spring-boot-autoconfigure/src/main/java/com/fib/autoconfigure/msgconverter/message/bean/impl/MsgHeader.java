package com.fib.autoconfigure.msgconverter.message.bean.impl;

import com.fib.autoconfigure.msgconverter.message.bean.MessageBean;

public class MsgHeader extends MessageBean {
	private String beginFlag = "{H:";
	private String versionID;
	private String origSender;
	private String origSenderSID;
	private String origReceiver;
	private String origReceiverSID;
	private String origSendDate;
	private String origSendTime;
	private String structType;
	private String mesgType;
	private String mesgID;
	private String mesgRefID;
	private String mesgPriority;
	private String mesgDirection;
	private String reserve;
	private String endFlag = "}";

	@Override
	public Object getAttribute(String name) {
		if ("BeginFlag".equals(name)) {
			return this.getBeginFlag();
		} else if ("VersionID".equals(name)) {
			return this.getVersionID();
		} else if ("OrigSender".equals(name)) {
			return this.getOrigSender();
		} else if ("OrigSenderSID".equals(name)) {
			return this.getOrigSenderSID();
		} else if ("OrigReceiver".equals(name)) {
			return this.getOrigReceiver();
		} else if ("OrigReceiverSID".equals(name)) {
			return this.getOrigReceiverSID();
		} else if ("OrigSendDate".equals(name)) {
			return this.getOrigSendDate();
		} else if ("OrigSendTime".equals(name)) {
			return this.getOrigSendTime();
		} else if ("StructType".equals(name)) {
			return this.getStructType();
		} else if ("MesgType".equals(name)) {
			return this.getMesgType();
		} else if ("MesgID".equals(name)) {
			return this.getMesgID();
		} else if ("MesgRefID".equals(name)) {
			return this.getMesgRefID();
		} else if ("MesgPriority".equals(name)) {
			return this.getMesgPriority();
		} else if ("MesgDirection".equals(name)) {
			return this.getMesgDirection();
		} else if ("Reserve".equals(name)) {
			return this.getReserve();
		} else if ("EndFlag".equals(name)) {
			return this.getEndFlag();
		}
		return null;
	}

	@Override
	public void setAttribute(String name, Object value) {
		if ("VersionID".equals(name)) {
			this.setVersionID((String) value);
		} else if ("OrigSender".equals(name)) {
			this.setOrigSender((String) value);
		} else if ("OrigSenderSID".equals(name)) {
			this.setOrigSenderSID((String) value);
		} else if ("OrigReceiver".equals(name)) {
			this.setOrigReceiver((String) value);
		} else if ("OrigReceiverSID".equals(name)) {
			this.setOrigReceiverSID((String) value);
		} else if ("OrigSendDate".equals(name)) {
			this.setOrigSendDate((String) value);
		} else if ("OrigSendTime".equals(name)) {
			this.setOrigSendTime((String) value);
		} else if ("StructType".equals(name)) {
			this.setStructType((String) value);
		} else if ("MesgType".equals(name)) {
			this.setMesgType((String) value);
		} else if ("MesgID".equals(name)) {
			this.setMesgID((String) value);
		} else if ("MesgRefID".equals(name)) {
			this.setMesgRefID((String) value);
		} else if ("MesgPriority".equals(name)) {
			this.setMesgPriority((String) value);
		} else if ("MesgDirection".equals(name)) {
			this.setMesgDirection((String) value);
		} else if ("Reserve".equals(name)) {
			this.setReserve((String) value);
		} else if ("EndFlag".equals(name)) {
			this.setEndFlag((String) value);
		}
	}

	public String getBeginFlag() {
		return beginFlag;
	}

	public void setBeginFlag(String beginFlag) {
		this.beginFlag = beginFlag;
	}

	public String getVersionID() {
		return versionID;
	}

	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	public String getOrigSender() {
		return origSender;
	}

	public void setOrigSender(String origSender) {
		this.origSender = origSender;
	}

	public String getOrigSenderSID() {
		return origSenderSID;
	}

	public void setOrigSenderSID(String origSenderSID) {
		this.origSenderSID = origSenderSID;
	}

	public String getOrigReceiver() {
		return origReceiver;
	}

	public void setOrigReceiver(String origReceiver) {
		this.origReceiver = origReceiver;
	}

	public String getOrigReceiverSID() {
		return origReceiverSID;
	}

	public void setOrigReceiverSID(String origReceiverSID) {
		this.origReceiverSID = origReceiverSID;
	}

	public String getOrigSendDate() {
		return origSendDate;
	}

	public void setOrigSendDate(String origSendDate) {
		this.origSendDate = origSendDate;
	}

	public String getOrigSendTime() {
		return origSendTime;
	}

	public void setOrigSendTime(String origSendTime) {
		this.origSendTime = origSendTime;
	}

	public String getStructType() {
		return structType;
	}

	public void setStructType(String structType) {
		this.structType = structType;
	}

	public String getMesgType() {
		return mesgType;
	}

	public void setMesgType(String mesgType) {
		this.mesgType = mesgType;
	}

	public String getMesgID() {
		return mesgID;
	}

	public void setMesgID(String mesgID) {
		this.mesgID = mesgID;
	}

	public String getMesgRefID() {
		return mesgRefID;
	}

	public void setMesgRefID(String mesgRefID) {
		this.mesgRefID = mesgRefID;
	}

	public String getMesgPriority() {
		return mesgPriority;
	}

	public void setMesgPriority(String mesgPriority) {
		this.mesgPriority = mesgPriority;
	}

	public String getMesgDirection() {
		return mesgDirection;
	}

	public void setMesgDirection(String mesgDirection) {
		this.mesgDirection = mesgDirection;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	@Override
	public void validate() {
		//

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
		if (null != versionID) {
			buf.append("<a n=\"versionID\" t=\"num\">");
			buf.append(versionID);
			buf.append("</a>");
		}
		if (null != origSender) {
			buf.append("<a n=\"origSender\" t=\"str\">");
			buf.append(origSender);
			buf.append("</a>");
		}
		if (null != origSenderSID) {
			buf.append("<a n=\"origSenderSID\" t=\"str\">");
			buf.append(origSenderSID);
			buf.append("</a>");
		}
		if (null != origReceiver) {
			buf.append("<a n=\"origReceiver\" t=\"str\">");
			buf.append(origReceiver);
			buf.append("</a>");
		}
		if (null != origReceiverSID) {
			buf.append("<a n=\"origReceiverSID\" t=\"str\">");
			buf.append(origReceiverSID);
			buf.append("</a>");
		}
		if (null != origSendDate) {
			buf.append("<a n=\"origSendDate\" t=\"datetime\">");
			buf.append(origSendDate);
			buf.append("</a>");
		}
		if (null != origSendTime) {
			buf.append("<a n=\"origSendTime\" t=\"datetime\">");
			buf.append(origSendTime);
			buf.append("</a>");
		}
		if (null != structType) {
			buf.append("<a n=\"structType\" t=\"str\">");
			buf.append(structType);
			buf.append("</a>");
		}
		if (null != mesgType) {
			buf.append("<a n=\"mesgType\" t=\"str\">");
			buf.append(mesgType);
			buf.append("</a>");
		}
		if (null != mesgID) {
			buf.append("<a n=\"mesgID\" t=\"str\">");
			buf.append(mesgID);
			buf.append("</a>");
		}
		if (null != mesgRefID) {
			buf.append("<a n=\"mesgRefID\" t=\"str\">");
			buf.append(mesgRefID);
			buf.append("</a>");
		}
		if (null != mesgPriority) {
			buf.append("<a n=\"mesgPriority\" t=\"num\">");
			buf.append(mesgPriority);
			buf.append("</a>");
		}
		if (null != mesgDirection) {
			buf.append("<a n=\"mesgDirection\" t=\"str\">");
			buf.append(mesgDirection);
			buf.append("</a>");
		}
		if (null != reserve) {
			buf.append("<a n=\"reserve\" t=\"str\">");
			buf.append(reserve);
			buf.append("</a>");
		}
		if (null != endFlag) {
			buf.append("<a n=\"endFlag\" t=\"str\">");
			buf.append(endFlag);
			buf.append("</a>");
		}
		if (0 == buf.length()) {
			return null;
		} else {
			if (isTable) {
				buf.insert(0, "<b>");
			} else {
				buf.insert(0, "<b c=\"com.giantstone.cnaps2.messagebean.recv.req.MsgHeader\">");
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
		if (null != beginFlag) {
			return false;
		}
		if (null != versionID) {
			return false;
		}
		if (null != origSender) {
			return false;
		}
		if (null != origSenderSID) {
			return false;
		}
		if (null != origReceiver) {
			return false;
		}
		if (null != origReceiverSID) {
			return false;
		}
		if (null != origSendDate) {
			return false;
		}
		if (null != origSendTime) {
			return false;
		}
		if (null != structType) {
			return false;
		}
		if (null != mesgType) {
			return false;
		}
		if (null != mesgID) {
			return false;
		}
		if (null != mesgRefID) {
			return false;
		}
		if (null != mesgPriority) {
			return false;
		}
		if (null != mesgDirection) {
			return false;
		}
		if (null != reserve) {
			return false;
		}
		if (null != endFlag) {
			return false;
		}
		return true;
	}
}