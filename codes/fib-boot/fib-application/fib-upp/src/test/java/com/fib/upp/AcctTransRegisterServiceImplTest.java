package com.fib.upp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fib.commons.util.CommUtils;
import com.fib.upp.modules.payment.entity.AcctTransRegister;
import com.fib.upp.modules.payment.service.IAcctTransRegisterService;
import com.fib.upp.util.Constant;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class AcctTransRegisterServiceImplTest {
	@Autowired
	IAcctTransRegisterService acctTransRegisterService;

	@Test

	public void testSave() {
		AcctTransRegister atr = new AcctTransRegister();
		atr.setPkId(Long.valueOf(CommUtils.getRandom("0123456789", 18)));

		String sysType = "02";
		String orgId = "1010";
		String paySerialNo = "501" + "00" + LocalDateTimeUtil.format(LocalDate.now(), DatePattern.PURE_DATE_FORMATTER)
				+ orgId + sysType + CommUtils.getRandom("0123456789", 13);
		atr.setChanlSerialNo(paySerialNo);
		atr.setDrAcctNo("9999014012010001");
		atr.setDrAcctName("大额支付系统往来轧差清算款项");
		atr.setCrAcctNo("9999014010010001");
		atr.setCrAcctName("凉山州商业银行大额支付系统往来账");
		atr.setCurrencyCode(Constant.CurrencyCode.CNY.code());
		atr.setTranAmt(new BigDecimal("1223.45"));
		atr.setOrderId(Long.valueOf(CommUtils.getRandom("0123456789", 18)));
		atr.setOpId(Long.valueOf(CommUtils.getRandom("0123456789", 18)));
		atr.setSysCode(Constant.SysCode.HVPS.code());
		atr.setTransOrgCode("0101");
		atr.setBillingDate(LocalDateTimeUtil.formatNormal(LocalDate.now()));
		atr.setCoreSerialNo(CommUtils.getRandom("0123456789", 8));
		atr.setAcctRegType("POSTING");
		atr.setStatusId("RECEIVED");
		atr.setRemark("大额记账");

		boolean flag = acctTransRegisterService.save(atr);
		assertEquals(true, flag);
	}

	@Test
	@Ignore("test")
	public void testQueryById() {
		AcctTransRegister mri = acctTransRegisterService.getById(Long.valueOf("304780082092070090"));
		assertNotNull(mri);
	}

	@Test
	@Ignore("test")
	public void testQueryByCon() {
		QueryWrapper<AcctTransRegister> wrapper = new QueryWrapper<>();
		wrapper.eq("chanl_serial_no", "50100202204251010028937350741367");
		List<AcctTransRegister> list = acctTransRegisterService.list(wrapper);
		assertTrue(list.size() > 0);
	}
}