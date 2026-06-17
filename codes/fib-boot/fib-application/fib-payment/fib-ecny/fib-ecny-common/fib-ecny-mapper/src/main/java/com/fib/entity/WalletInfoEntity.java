package com.fib.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_wallet_info")
public class WalletInfoEntity {
    @TableId
    private String walletId;

    /**
     *
     */
    private String walletName;
}
