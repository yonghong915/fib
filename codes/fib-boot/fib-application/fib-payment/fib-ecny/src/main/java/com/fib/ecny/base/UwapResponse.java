package com.fib.ecny.base;

import com.fib.ecny.base.service.MsgHeader;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UwapResponse<T> {

    @NotNull(message = "msgHeader must not be null.")
    private MsgHeader msgHeader;

    @NotNull(message = "msgBody must not be null.")
    private T msgBody;

    private String procSts;

    private String procCd;

    private String procInf;

    public UwapResponse() {

    }

    public UwapResponse(T response) {
        this.msgBody = response;
        this.procSts = "PGO000";
        this.procCd = "000000";
        this.procInf = "success";
    }

    public UwapResponse(MsgHeader header, UwapResponse respData) {
    }

    public static UwapResponse<?> fail() {
        return new UwapResponse();
    }
}
