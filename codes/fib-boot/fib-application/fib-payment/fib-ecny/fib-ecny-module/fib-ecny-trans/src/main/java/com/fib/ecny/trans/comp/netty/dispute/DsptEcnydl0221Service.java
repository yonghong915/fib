package com.fib.ecny.trans.comp.netty.dispute;

import com.fib.ecny.trans.comp.netty.DisputeServiceType;
import com.fib.ecny.trans.comp.netty.IDisputeService;
import org.springframework.stereotype.Component;

@Component
@DisputeServiceType(values = {"dspt_ecnydl0221_dcep.221.001.01"})
public class DsptEcnydl0221Service implements IDisputeService {
    @Override
    public DisputeRsp execute(DisputeReq disputeReq) {
        return null;
    }
}
