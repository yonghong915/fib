package com.fib.upp.service;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.commons.util.CommUtils;
import com.fib.upp.entity.BizTbClobEntity;
import com.fib.upp.entity.CommunicationEventEntity;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ZipUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommunicationEventServiceTest {

	@Autowired
	private ICommunicationEventService communicationEventService;

	@Autowired
	private IBizTbClobService bizTbClobService;

	@Test
	public void testSave() {
		Snowflake sf = IdUtil.getSnowflake();

		String co = FileUtil.readUtf8String("data/data.txt");
		System.out.println(co.getBytes().length);
		try {
			BizTbClobEntity btce = new BizTbClobEntity();
			String id = sf.nextIdStr();
			btce.setId(id);
			String cont = CommUtils.gzip(co, "UTF-8");
			System.out.println(cont.getBytes().length);
			btce.setContent(cont);
			bizTbClobService.save(btce);

			CommunicationEventEntity ce = new CommunicationEventEntity();
			ce.setCommEventId(sf.nextIdStr());
			ce.setCommEventTypeId("MESSAGE_IN_COM");
			ce.setContentId(id);
			communicationEventService.save(ce);

			List<CommunicationEventEntity> list = communicationEventService.list();
			for (CommunicationEventEntity cee : list) {
				BizTbClobEntity bce = bizTbClobService.getById(cee.getContentId());
				System.out.println("aaaaa=" + bce.getContent());
				System.out.println("bbbbb=" + CommUtils.unzip(bce.getContent(), "UTF-8"));
			}
			System.out.println(list.size());
		} catch (UtilException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testList() {
		List<CommunicationEventEntity> list = communicationEventService.list();
		for (CommunicationEventEntity cee : list) {
			BizTbClobEntity bce = bizTbClobService.getById(cee.getContentId());
			System.out.println(bce.getContent());
		}
		System.out.println("aaa");
	}
}
