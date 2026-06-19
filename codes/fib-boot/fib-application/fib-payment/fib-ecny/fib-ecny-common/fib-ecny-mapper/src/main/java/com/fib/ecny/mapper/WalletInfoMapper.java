package com.fib.ecny.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fib.ecny.entity.WalletInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WalletInfoMapper extends InsertBatchMapper<WalletInfoEntity> {
    public int insertWalletInfo(WalletInfoEntity walletInfoEntity);

    int batchInsert(@Param("list") List<WalletInfoEntity> list);
}
