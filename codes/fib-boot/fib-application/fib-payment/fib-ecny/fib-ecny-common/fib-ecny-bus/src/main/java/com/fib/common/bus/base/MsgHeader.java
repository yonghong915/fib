package com.fib.common.bus.base;

import lombok.Data;

/**
 * 报文头
 */
public class MsgHeader {
    /**
     *
     */
    private String msgId;

    /**
     *
     */
    private String msgType;

    /**
     *
     */
    private String origSendDateTime;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getOrigSendDateTime() {
        return origSendDateTime;
    }

    public void setOrigSendDateTime(String origSendDateTime) {
        this.origSendDateTime = origSendDateTime;
    }
}