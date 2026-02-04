package com.fib.midbiz.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fib.midbiz.taxbank.entity.BpAgreementEntity;
import com.fib.midbiz.taxbank.mapper.BpAgreementMapper;
import com.fib.midbiz.taxbank.service.BpAgreementService;

@SpringBootTest
public class BpAgreementServiceImplTest {
	@Autowired
	BpAgreementMapper bpAgreementMapper;

	@Autowired
	private BpAgreementService bpAgreementService;

	@Test
	public void test() {
		List<BpAgreementEntity> list = bpAgreementMapper.selectList(null);
		for (BpAgreementEntity ag : list) {
			IO.println("aaaaa=" + ag.getOrgCode() + "  " + ag.getProtocolNum());
		}
		assertNotNull(list);
	}

	@Test
	public void test1() {
		List<BpAgreementEntity> list = bpAgreementService.list();
		for (BpAgreementEntity ag : list) {
			IO.println("aaaaa=" + ag.getOrgCode() + "  " + ag.getProtocolNum());
		}
		assertNotNull(list);
	}
}
