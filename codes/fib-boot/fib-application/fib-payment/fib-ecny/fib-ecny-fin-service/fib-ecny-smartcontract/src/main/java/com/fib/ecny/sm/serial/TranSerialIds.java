package com.fib.ecny.sm.serial;

import com.fib.ecny.GrpHdr;
import com.fib.ecny.sm.EsbHead;
import com.fib.ecny.sm.context.MessageContext;

public record TranSerialIds(String glSeqNo, String systemNo, String tranDt, String chnlSeqNo, String chnlDt,
                            String chnlNo,
                            String opTranCode) {
    public static TranSerialIds from(EsbHead esbHead){
       return new TranSerialIds("","","","","","","");
    }
    public static TranSerialIds from(MessageContext ctx, GrpHdr grpHdr){
        return new TranSerialIds("","","","","","","");
    }
}
