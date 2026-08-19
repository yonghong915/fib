package com.fib.ecny.wallet.mapper;

import com.fib.ecny.wallet.entity.WalletInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WalletInfoMapper extends InsertBatchMapper<WalletInfoEntity> {
    int insertWalletInfo(WalletInfoEntity walletInfoEntity);

    int batchInsert(@Param("list") List<WalletInfoEntity> list);
}
