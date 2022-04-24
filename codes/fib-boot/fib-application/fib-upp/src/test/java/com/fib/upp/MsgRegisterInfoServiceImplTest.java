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
import com.fib.upp.modules.payment.entity.MsgRegisterInfo;
import com.fib.upp.modules.payment.service.IMsgRegisterInfoService;
import com.fib.upp.util.Constant;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class MsgRegisterInfoServiceImplTest {
	@Autowired
	IMsgRegisterInfoService msgRegisterInfoService;

	@Test
	@Ignore("test")
	public void testSave() {
		MsgRegisterInfo mri = new MsgRegisterInfo();
		mri.setPkId(Long.valueOf(CommUtils.getRandom("0123456789", 18)));

		String sysType = "02";
		String orgId = "1010";
		String paySerialNo = "501" + "00" + LocalDateTimeUtil.format(LocalDate.now(), DatePattern.PURE_DATE_FORMATTER)
				+ orgId + sysType + CommUtils.getRandom("0123456789", 13);
		mri.setPaySerialNo(paySerialNo);
		mri.setSysCode(Constant.SysCode.HVPS.code());
		mri.setMsgTypeCode(Constant.MsgType.BEPS_121_001_01.code());
		mri.setMsgId(LocalDateTimeUtil.format(LocalDate.now(), DatePattern.PURE_DATE_FORMATTER)
				+ CommUtils.getRandom("0123456789", 8));
		mri.setMsgDirection(Constant.MsgDirection.OUT.code());
		mri.setSysDate(LocalDateTimeUtil.formatNormal(LocalDate.now()));
		mri.setNettingDate(LocalDateTimeUtil.formatNormal(LocalDate.now()));
		mri.setNettingRound("01");
		mri.setProcessStatus(Constant.ProcessStatus.PR03.code());
		mri.setTransAmt(new BigDecimal("1223.45"));
		mri.setTransNum(1);
		mri.setSuccAmt(new BigDecimal("1223.45"));
		mri.setSuccNum(1);
		mri.setSndBankCode("323653010011");
		mri.setRcvBankCode("323653010015");
		mri.setMsgContentId(Long.valueOf(CommUtils.getRandom("0123456789", 18)));

		boolean flag = msgRegisterInfoService.save(mri);
		assertEquals(true, flag);
	}

	@Test
	@Ignore("test")
	public void testQueryById() {
		MsgRegisterInfo mri = msgRegisterInfoService.getById(Long.valueOf("444740973201491393"));
		assertNotNull(mri);
	}

	@Test
	public void testQueryByCon() {
		QueryWrapper<MsgRegisterInfo> wrapper = new QueryWrapper<>();
		wrapper.eq("pay_serial_no", "50100202204241010027612121070845");
		List<MsgRegisterInfo> list = msgRegisterInfoService.list(wrapper);
		assertTrue(list.size() > 0);
	}
}