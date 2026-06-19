package com.fib.ecny.trans.controller;

import com.fib.common.bus.base.MsgHeader;
import jakarta.validation.constraints.NotNull;

public class UwapResponse<T> {

    @NotNull(message = "msgHeader must not be null.")
    private MsgHeader msgHeader;

    @NotNull(message = "msgBody must not be null.")
    private T msgBody;

    private String procSts;

    private String retSts;

    private String retInf;

    public UwapResponse(T response) {
        this.msgBody = response;
        this.procSts = "PGO000";
        this.retSts = "000000";
        this.retInf = "success";
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public T getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(T msgBody) {
        this.msgBody = msgBody;
    }

    public String getProcSts() {
        return procSts;
    }

    public void setProcSts(String procSts) {
        this.procSts = procSts;
    }

    public String getRetSts() {
        return retSts;
    }

    public void setRetSts(String retSts) {
        this.retSts = retSts;
    }

    public String getRetInf() {
        return retInf;
    }

    public void setRetInf(String retInf) {
        this.retInf = retInf;
    }
}
