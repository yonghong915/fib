package com.fib.common.bus.base;

import lombok.Data;

@Data
public class UwapResponse<T> {

    MsgHeader msgHeader;

    T respData;
}
