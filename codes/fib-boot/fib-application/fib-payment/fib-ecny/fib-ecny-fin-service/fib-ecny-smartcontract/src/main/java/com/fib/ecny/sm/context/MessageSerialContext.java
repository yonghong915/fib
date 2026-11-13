package com.fib.ecny.sm.context;

import com.fib.ecny.sm.serial.SerialContext;

public  record MessageSerialContext(SerialContext serialContext, String chnlNo, String tranTm, String reqTm, String reqAddr,
                                    long startMills) {
}
