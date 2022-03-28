package com.fib.upp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fib.upp.entity.BepsQueue;
import com.fib.upp.mapper.BepsQueueMapper;
import com.fib.upp.service.IBepsQueueService;
import com.fib.upp.service.beps.impl.BepsQueueServiceImpl;

public class BepsServiceTest {

//	@Autowired
//	private IBepsPackService bepsPackService;
//

	private IBepsQueueService bepsQueueService;

	@Mock
	private BepsQueueMapper bepsQueueMapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		when(bepsQueueMapper.getQueueByQueueType("I")).thenReturn(new BepsQueue());
		bepsQueueService = new BepsQueueServiceImpl(bepsQueueMapper);
	}

	@Test
	public void testGetQueueByQueueType() {
		BepsQueue bq = bepsQueueService.getQueueByQueueType("I");
		assertEquals(null, bq.getId());
	}
//
//	@Test
//	@Ignore("stop tmp")
//	public void queryBepsPackRuleListTest() throws Exception {
//		List<BepsMessagePackRule> packRuleLst = bepsPackService.queryBepsPackRuleList();
//		logger.info("list size is:{}", packRuleLst.size());
//		assertNotNull(packRuleLst);
//	}
//
//	@Test
//	@Ignore("stop tmp")
//	public void getUserByIdTest() throws Exception {
//		String messageType = "beps.121.001.01";
//		List<String> orderIdList = new ArrayList<String>();
//		orderIdList.add("111");
//		orderIdList.add("222");
//		orderIdList.add("333");
//		List<List<String>> packRuleLst = BepsPackUtil.packPaymentOrder(messageType, orderIdList);
//		logger.info("list size is:{}", packRuleLst.toString());
//		assertNotNull(packRuleLst);
//	}
//
//	@Test
//	@Ignore("stop tmp")
//	public void sendMessageTest() throws Exception {
//		Long queueId = 100001l;
//		bepsQueueService.sendMessage(queueId);
//	}
//
//	@Test
//	@Ignore("stop tmp")
//	public void updateQueueItemStatusTest() throws Exception {
//		Long queueId = 100000l;
//		BepsQueueItem queueItem = new BepsQueueItem();
//		queueItem.setStatus("END");
//		queueItem.setQueueId(queueId);
//		//queueItem.setRecordIds(Lists.newArrayList("111","222","333"));
//		bepsQueueService.updateQueueItemStatus(queueItem);
//	}
//	
//	@Test
//	public void packBepsMessageTest() throws Exception {
//		logger.info("packBepsMessageTest start");
//		bepsPackService.packBepsMessage();
//		Thread.sleep(1000);
//		logger.info("packBepsMessageTest end");
//	}
}
