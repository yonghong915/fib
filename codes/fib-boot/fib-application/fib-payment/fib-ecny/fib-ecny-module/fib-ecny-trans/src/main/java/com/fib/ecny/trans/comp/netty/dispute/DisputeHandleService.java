package com.fib.ecny.trans.comp.netty.dispute;

import com.fib.ecny.trans.comp.netty.DisputeStrategyFactory;
import com.fib.ecny.trans.comp.netty.IDisputeService;
import io.smallrye.common.constraint.NotNull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class DisputeHandleService {
    @Resource
    private DisputeStrategyFactory disputeStrategyFactory;

    @NotNull
    public void handleService(String serviceName) {

        DisputeReq disputeReq = new DisputeReq();
        try {
            IDisputeService disputeService = disputeStrategyFactory.getDisputeService(serviceName);
            DisputeRsp disputeRsp = disputeService.execute(disputeReq);

            //ValidateUtils.valid
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
