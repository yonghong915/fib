package com.fib.ecny.trans.comp.netty;

import cn.hutool.core.util.ObjectUtil;
import com.fib.common.bus.base.BizException;
import com.fib.common.bus.base.annotation.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DisputeStrategyFactory {
    private final Map<String, IDisputeService> strategeMap = new HashMap<>();

    public DisputeStrategyFactory(List<IDisputeService> disputeServices) {
        for (IDisputeService disputeService : disputeServices) {
            DisputeServiceType disputeServiceType = disputeService.getClass().getAnnotation(DisputeServiceType.class);
            if (ObjectUtil.isEmpty(disputeServiceType)) {
                continue;
            }
            String[] values = disputeServiceType.values();
            for (String value : values) {
                if (ObjectUtil.isEmpty(value)) {
                    continue;
                }
                strategeMap.put(value, disputeService);
            }
        }
    }

    /**
     * 获取差错处理服务
     *
     * @param serviceName 服务名
     * @return 差错处理服务
     */
    public IDisputeService getDisputeService(String serviceName) {
        IDisputeService disputeService = strategeMap.get(serviceName);
        if (ObjectUtil.isEmpty(disputeService)) {
            throw new BizException(ErrorCode.FAIL, "Not Find mapping DisputeService");
        }
        return disputeService;
    }
}
