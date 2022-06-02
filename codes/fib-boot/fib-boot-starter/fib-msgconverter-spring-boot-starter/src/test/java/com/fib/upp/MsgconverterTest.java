package com.fib.upp;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fib.autoconfigure.msgconverter.channel.Channel;
import com.fib.autoconfigure.msgconverter.gateway.Lautcher;
import com.fib.msgconverter.MsgconverterApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MsgconverterApp.class)
public class MsgconverterTest {

	@Autowired
	private Channel mqChannel;

	@Autowired
	Lautcher lautcher;

	@Test
	@Ignore("test")
	public void testMQ() {
		mqChannel.start();
		// mqChannel.setEventQueue(new EventQueue());
		try {
			TimeUnit.MILLISECONDS.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}

	@Test
	public void testGateway() {
		lautcher.init();
		try {
			TimeUnit.MILLISECONDS.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(true);
	}
}