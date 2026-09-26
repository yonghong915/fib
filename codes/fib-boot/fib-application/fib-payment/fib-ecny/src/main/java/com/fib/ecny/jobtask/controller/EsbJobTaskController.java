package com.fib.ecny.jobtask.controller;

import com.fib.ecny.jobtask.PayrollDetailDto;
import com.fib.ecny.jobtask.service.PayRollDetailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class EsbJobTaskController {
    @Resource
    private PayRollDetailService payRollDetailService;

    @PostMapping(value = "/payroll", produces = MediaType.APPLICATION_JSON_VALUE)
    public String payroll(@RequestBody String req) {
        List<PayrollDetailDto> list = payRollDetailService.queryList(1l, 2l, 4);
        return "ok";
    }
}
