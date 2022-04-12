package com.fib.upp;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.modules.beps.entity.BepsQueue;
import com.fib.upp.modules.beps.mapper.BepsQueueMapper;
import com.fib.upp.modules.beps.service.IBepsQueueService;
import com.fib.upp.modules.beps.service.impl.BepsQueueServiceImpl;
import com.fib.upp.modules.common.service.ICommonService;

import cn.hutool.core.map.MapUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class BepsQueueServiceTest {

//	@Autowired
//	private IBepsPackService bepsPackService;
//

	private IBepsQueueService bepsQueueService;

	@Autowired
	private ICommonService smallPackService;

	@Mock
	private BepsQueueMapper bepsQueueMapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		when(bepsQueueMapper.getQueueByQueueType("I")).thenReturn(new BepsQueue());
		bepsQueueService = new BepsQueueServiceImpl(bepsQueueMapper);
	}

	@Test
	public void testSmallPack() {
		smallPackService.execute(MapUtil.of("chanlSerialNo", "12334455"));
	}

//	@Test
//	public void testGetQueueByQueueType() {
//		Opt<BepsQueue> bqOpt = bepsQueueService.getQueueByQueueType("I");
//		bqOpt.orElseThrow();
//	}
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
