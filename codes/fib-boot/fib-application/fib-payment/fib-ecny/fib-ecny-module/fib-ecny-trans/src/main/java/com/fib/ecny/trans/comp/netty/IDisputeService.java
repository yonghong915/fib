package com.fib.ecny.trans.comp.netty;

import com.fib.ecny.trans.comp.netty.dispute.DisputeReq;
import com.fib.ecny.trans.comp.netty.dispute.DisputeRsp;

public interface IDisputeService {
    DisputeRsp execute(DisputeReq disputeReq);
}
