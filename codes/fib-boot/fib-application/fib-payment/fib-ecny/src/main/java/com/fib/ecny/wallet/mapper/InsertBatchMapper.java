package com.fib.ecny.wallet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InsertBatchMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入（仅插入非逻辑删除、非自动填充默认字段，性能高）
     *
     * @param list 数据集合
     * @return 影响行数
     */
    int insertBatchSomeColumn(@Param("list") List<T> list);
}
