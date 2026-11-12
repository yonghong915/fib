package com.fib.ecny.sm;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoanProcessorRegistry {
    private Map<String, LoanProcessor<?>> byTranCode = new HashMap<>();

    public LoanProcessorRegistry(List<LoanProcessor<?>> processors) {
        for (LoanProcessor<?> processor : processors) {
            String tranCode = processor.tranCode();
            if (ObjectUtil.isEmpty(tranCode)) {
                throw new RuntimeException();
            }
            LoanProcessor<?> exist = byTranCode.putIfAbsent(tranCode, processor);
            if (Objects.nonNull(exist)) {
                throw new RuntimeException();
            }
        }
    }

    public LoanProcessor find(String tranCode) {
        return byTranCode.get(tranCode);
    }
}
