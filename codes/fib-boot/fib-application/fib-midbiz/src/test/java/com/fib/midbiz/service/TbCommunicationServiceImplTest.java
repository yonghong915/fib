package com.fib.midbiz.service;

import java.math.BigInteger;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fib.midbiz.taxbank.entity.TbCommunicationEntity;
import com.fib.midbiz.taxbank.mapper.TbCommunicationMapper;
import com.fib.midbiz.taxbank.mapstruct.converter.TbCommunicationConverter;
import com.fib.midbiz.taxbank.vo.TbCommunicationVO;

@SpringBootTest
public class TbCommunicationServiceImplTest {
	@Autowired
	TbCommunicationMapper tbCommunicationMapper;

	@Test
	public void test() {
		List<TbCommunicationEntity> list = tbCommunicationMapper.selectList(null);
		for (TbCommunicationEntity tbc : list) {
			System.out.println(tbc.getId() + "  " + tbc.getName() + "  " + tbc.getCreateBy() + "  " + tbc.getCreateDt());
		}
		
//		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
//		int taskNum = 1000;
//		AtomicInteger ai = new AtomicInteger();
//		for (int i = 0; i < taskNum; i++) {
//			executor.submit(() -> {
//				try {
//					TimeUnit.MILLISECONDS.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			});
//			Thread.ofVirtual().name("ifssf").start(() -> {
//				System.out.println("aaaaa");
//			});
//
//		}
//		executor.shutdown();

		TbCommunicationConverter converter = TbCommunicationConverter.INSTANCE;
		TbCommunicationEntity entity = new TbCommunicationEntity();
		entity.setId(new BigInteger("1"));
		TbCommunicationVO vo = converter.TbCommunicationEntity2VO(entity);
		System.out.println("vo=" + vo.getId());
		
		Assertions.assertThat(list.size()).isEqualTo(0);

	}
}
