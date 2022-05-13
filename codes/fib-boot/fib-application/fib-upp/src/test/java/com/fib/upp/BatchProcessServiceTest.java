package com.fib.upp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.commons.util.CommUtils;
import com.fib.upp.modules.cnaps.beps.entity.BatchProcessGroup;
import com.fib.upp.modules.cnaps.beps.service.IBatchProcessService;
import com.fib.upp.util.Constant;

import cn.hutool.core.date.LocalDateTimeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class BatchProcessServiceTest {

	@Autowired
	private IBatchProcessService batchProcessService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testInsertBatchProcessGroup() {
		BatchProcessGroup bpg = new BatchProcessGroup();
		bpg.setBatchType(Constant.BatchType.SUE_BAP_OUT.code());
		bpg.setTransNum(2);
		bpg.setTransAmt(new BigDecimal("1234450.56"));
		bpg.setDraweeAcctNo("99990124320104051000");
		bpg.setDraweeAcctName("临时存款-人力资源开发有限公司");
		bpg.setBizType("E101");
		bpg.setPartyId("1101");
		bpg.setTellerId("1219");
		bpg.setTransId("50100" + LocalDateTimeUtil.format(LocalDateTimeUtil.now(), "yyyyMMddHHmmssSSS")
				+ CommUtils.getRandom("0123456789", 10));
		int row = batchProcessService.insertBatchProcessGroup(bpg);
		assertEquals(1, row);
	}
}
