package com.fib.ecny.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fib.ecny.base.service.MsgHeader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UwapRequest<T> {
    @Valid
    @NotNull(message = "msgHeader must not be null.")
    private MsgHeader msgHeader;

    @Valid
    @NotNull(message = "msgBody must not be null.")
    private T msgBody;

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
}
