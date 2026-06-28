package com.fib.ecny.test;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.fib.ecny.entity.WalletInfoEntity;
import com.fib.ecny.mapper.WalletInfoMapper;
import jakarta.annotation.Resource;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WalletInfoTest {
    @Resource
    private WalletInfoMapper walletInfoMapper;

//    @Ignore
//    @Test
//    public void testInsertWallet() {
//        WalletInfoEntity walletInfoEntity = new WalletInfoEntity();
//        walletInfoEntity.setWalletId(String.valueOf(IdUtil.getSnowflakeNextId()));
//        walletInfoEntity.setWalletName("钱包");
//        walletInfoEntity.setWalletLevel("WL01");
//        walletInfoEntity.setWalletStatus("WS01");
//        walletInfoMapper.insertWalletInfo(walletInfoEntity);
//    }

    @Test
    public void testBatchInsertWallet() {
        List<WalletInfoEntity> list = new ArrayList<>();
        int len = 1000000;
        WalletInfoEntity walletInfoEntity = null;
        AtomicInteger cntAtomic = new AtomicInteger(0);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < len; i++) {
            walletInfoEntity = new WalletInfoEntity();
            String walletId = IdUtil.getSnowflakeNextIdStr();
            walletInfoEntity.setWalletId(walletId);
            walletInfoEntity.setEcnyCustNo(walletId + RandomUtil.randomNumbers(2));
            walletInfoEntity.setWalletName("钱包" + walletId);
            walletInfoEntity.setWalletLevel("WL04");
            walletInfoEntity.setWalletStatus("WS01");
            walletInfoEntity.setRemark("钱包备注" + walletId);
            list.add(walletInfoEntity);
            int cnt = cntAtomic.getAndIncrement();
            if (cnt % 500 == 0) {
                //walletInfoMapper.insertBatchSomeColumn(list);
                walletInfoMapper.batchInsert(list);
                list.clear();
            }
        }
        //walletInfoMapper.insertBatchSomeColumn(list);
        walletInfoMapper.batchInsert(list);
        stopWatch.stop();
        long duration = stopWatch.getTotalTimeMillis();
        System.out.println("总耗时：" + duration + " ms");


    }


    @Test
    public void testFindUserById() {
        WalletInfoEntity walletInfoEntity = walletInfoMapper.selectById("201233444");
        assertNotNull(walletInfoEntity);
        //assertEquals("John Doe", walletInfoEntity.getWalletId());
    }

}
