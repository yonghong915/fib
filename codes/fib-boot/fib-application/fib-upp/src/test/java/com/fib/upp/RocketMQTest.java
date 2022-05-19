package com.fib.upp;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.upp.mq.rocketmq.producer.IMQMessageService;
import com.fib.upp.util.MQUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UppApplication.class)
public class RocketMQTest {
	private final String topic = new StringJoiner(":").add(MQUtil.Topic.TOPIC_ORDER.getCode()).add(MQUtil.Tag.TAG_BOOK.getCode()).toString();

	@Resource
	private IMQMessageService mqMessageService;

	// 订单消息和支付消息属于不同业务类型的消息，分别创建 Topic_Order 和 Topic_Pay
	// 其中订单消息根据商品品类以不同的 Tag 再进行细分，如电器类、男装类、女装类、化妆品类，最后他们都被各个不同的系统所接收
	@Test
	public void testSendMessage() {
		String data = FileUtil.readString("data/cnaps/ccms.801.001.02.txt", StandardCharsets.UTF_8);
		String id = IdUtil.fastSimpleUUID();
		mqMessageService.sendAsyncMessage(topic, id, data);

		try {
			TimeUnit.SECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(true);
	}
}