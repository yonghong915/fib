package com.fib.ecny.jobtask.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fib.ecny.common.exception.BizException;
import com.fib.ecny.jobtask.PayrollDetailDto;
import com.fib.ecny.jobtask.converter.PayrollDetailConverter;
import com.fib.ecny.jobtask.entity.PayrollDetailEntity;
import com.fib.ecny.jobtask.mapper.PayrollDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PayRollDetailService {

    private final PayrollDetailMapper payrollDetailMapper;

    public PayRollDetailService(PayrollDetailMapper payrollDetailMapper) {
        this.payrollDetailMapper = payrollDetailMapper;
    }

    public List<PayrollDetailDto> queryList(Long batchId, Long lastMaxId, int pageSize) {
        if (Objects.isNull(batchId) || Objects.isNull(lastMaxId) || pageSize == 0) {
            throw new BizException("参数不正确");
        }
        LambdaQueryWrapper<PayrollDetailEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(batchId), PayrollDetailEntity::getBatchId, batchId);
        wrapper.gt(PayrollDetailEntity::getSeqId, lastMaxId);
        wrapper.orderByAsc(PayrollDetailEntity::getSeqId);

        Page<PayrollDetailEntity> page = new Page<>(1, pageSize);
        Page<PayrollDetailEntity> result = payrollDetailMapper.selectPage(page, wrapper);
        return PayrollDetailConverter.INSTANCE.toDtoList(result.getRecords());
    }
}
