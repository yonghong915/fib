package com.fib.ecny.trans.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fib.ecny.base.dto.wallet.trans.ReversalFlagEnum;
import com.fib.ecny.base.exception.BizException;
import com.fib.ecny.entity.trans.TqaMainSerial;
import com.fib.ecny.mapper.TqaMainSerialMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@AllArgsConstructor
public class TqaMainSerialService {
    private final TqaMainSerialMapper tqaMainSerialMapper;

    @Transactional(rollbackFor = Exception.class)
    public void updateReversalFlag(BigInteger txId, ReversalFlagEnum oldFlag, ReversalFlagEnum targetFlag) {
        if (!oldFlag.canTransferTo(targetFlag)) {
            throw new BizException(String.format("调整标记流转非法，当前:%s，目标:%s", oldFlag.getDesc(), targetFlag.getDesc()));
        }
        LambdaUpdateWrapper<TqaMainSerial> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TqaMainSerial::getId, txId)
                .eq(TqaMainSerial::getReversalFlagEnum, oldFlag)
                .set(TqaMainSerial::getReversalFlagEnum, targetFlag);

        int affectRows = tqaMainSerialMapper.update(updateWrapper);
        if (affectRows <= 0) {
            throw new BizException("流水状态已变更或流水不存在，txId:" + txId);
        }
    }
}
