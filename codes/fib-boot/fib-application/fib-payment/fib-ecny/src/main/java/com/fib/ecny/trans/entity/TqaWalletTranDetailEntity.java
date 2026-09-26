package com.fib.ecny.trans.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * 钱包交易明细信息
 */
@Data
@TableName("tqa_wallet_tran_detail")
public class TqaWalletTranDetailEntity {
    @TableId("id")
    private BigInteger id;
    private String glSerialNo;
    private String chnlSerialNo;
    private String tranCode;
    private String walletId;
    private String tranDt;
    private String tranWalletId;
    private BigDecimal tranAmt;
    private String tranCcy;
    private String tranStatus;
    private String reversalFlag;
    private String voucherNo;
    private String refVoucherNo;
    private String remark;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;
}
