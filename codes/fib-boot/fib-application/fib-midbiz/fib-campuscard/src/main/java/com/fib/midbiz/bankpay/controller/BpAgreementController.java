package com.fib.midbiz.bankpay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fib.midbiz.taxbank.service.BpAgreementService;

@RestController
@RequestMapping("/bankPay")
public class BpAgreementController {
	BpAgreementService bpAgreementService;

	BpAgreementController(BpAgreementService bpAgreementService) {
		this.bpAgreementService = bpAgreementService;
	}

	@GetMapping("/list")
	public Object list() {
		long s = bpAgreementService.count();
		return s;
	}
}
