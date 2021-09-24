package com.fib.upp.service.rulecheck.dto;

import java.util.Date;

public class MapstructTest {

	public static void main(String[] args) {
		BankOrgDTO bankDto = new BankOrgDTO();
		bankDto.setChannel("12334");
		bankDto.setChannelDate(new Date());
		bankDto.setChannelSerialNo("111111111111");
		bankDto.setSystemCode("HVPS");
		bankDto.setTransactionId("222222");

		System.out.println("bankDto=" + bankDto);

		RuleCheckDTO ruleDto = RuleCheckMapping.MAPPER.convert(bankDto);

		System.out.println("ruleDto=" + ruleDto);
	}

}
