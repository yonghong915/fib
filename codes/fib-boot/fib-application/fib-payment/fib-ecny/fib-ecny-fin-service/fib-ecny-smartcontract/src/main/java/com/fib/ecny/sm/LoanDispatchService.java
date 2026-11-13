package com.fib.ecny.sm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fib.ecny.sm.serial.TranSerialIds;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanDispatchService {
    private final LoanProcessorRegistry loanProcessorRegistry;
    private final ObjectMapper objectMapper;
    public LoanResponse dispatch(LoanRequest request) {
        LoanProcessor loanProcessor = loanProcessorRegistry.find(request.getTranCode());
        TranSerialIds ids = minIds(request);
        return invoke(loanProcessor,request,ids);
    }

    private TranSerialIds minIds(LoanRequest request) {
        return new TranSerialIds("","","","","","","");
    }

    private <B> LoanResponse invoke(LoanProcessor<B> processor, LoanRequest request, TranSerialIds ids) {
        try {
            B body = objectMapper.treeToValue(request.getMsgBody(),processor.bodyType());
            return processor.process(request,body,ids);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
