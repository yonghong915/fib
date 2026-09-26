package com.fib.ecny.jobtask.service;

import com.fib.ecny.jobtask.mapper.PayrollMapper;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {
    private final PayrollMapper payrollMapper;

    public PayrollService(PayrollMapper payrollMapper) {
        this.payrollMapper = payrollMapper;
    }
}
