package com.fib.upp;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.pay.beps.pack.BepsMessagePackRule;
import com.fib.upp.pay.beps.pack.BepsPackUtil;
import com.fib.upp.pay.beps.pack.BepsQueueItem;
import com.fib.upp.service.IBepsPackService;
import com.fib.upp.service.IBepsQueueService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class BepsServiceTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IBepsPackService bepsPackService;

	@Autowired
	private IBepsQueueService bepsQueueService;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	@Ignore("stop tmp")
	public void queryBepsPackRuleListTest() throws Exception {
		List<BepsMessagePackRule> packRuleLst = bepsPackService.queryBepsPackRuleList();
		logger.info("list size is:{}", packRuleLst.size());
		assertNotNull(packRuleLst);
	}

	@Test
	@Ignore("stop tmp")
	public void getUserByIdTest() throws Exception {
		String messageType = "beps.121.001.01";
		List<String> orderIdList = new ArrayList<String>();
		orderIdList.add("111");
		orderIdList.add("222");
		orderIdList.add("333");
		List<List<String>> packRuleLst = BepsPackUtil.packPaymentOrder(messageType, orderIdList);
		logger.info("list size is:{}", packRuleLst.toString());
		assertNotNull(packRuleLst);
	}

	@Test
	@Ignore("stop tmp")
	public void sendMessageTest() throws Exception {
		Long queueId = 100001l;
		bepsQueueService.sendMessage(queueId);
	}

	@Test
	@Ignore("stop tmp")
	public void updateQueueItemStatusTest() throws Exception {
		Long queueId = 100000l;
		BepsQueueItem queueItem = new BepsQueueItem();
		queueItem.setStatus("END");
		queueItem.setQueueId(queueId);
		//queueItem.setRecordIds(Lists.newArrayList("111","222","333"));
		bepsQueueService.updateQueueItemStatus(queueItem);
	}
	
	@Test
	public void packBepsMessageTest() throws Exception {
		logger.info("packBepsMessageTest start");
		bepsPackService.packBepsMessage();
		Thread.sleep(1000);
		logger.info("packBepsMessageTest end");
	}
}
