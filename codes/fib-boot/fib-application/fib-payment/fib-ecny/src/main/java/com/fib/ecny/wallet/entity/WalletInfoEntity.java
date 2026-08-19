package com.fib.ecny.wallet.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 钱包信息实体
 */
@Data
@Accessors(chain = true)
@TableName("t_wallet_info")
public class WalletInfoEntity {
    /**
     * 数币客户号
     */
    @TableId(type = IdType.INPUT)
    private String ecnyCustNo;

    /**
     * 钱包ID
     */
    private String walletId;

    /**
     * 钱包名称
     */
    private String walletName;

    /**
     * 钱包等级
     */
    private String walletLevel;

    /**
     * 钱包状态
     */
    private String walletStatus;

    /**
     * 备注
     */
    private String remark;
}
