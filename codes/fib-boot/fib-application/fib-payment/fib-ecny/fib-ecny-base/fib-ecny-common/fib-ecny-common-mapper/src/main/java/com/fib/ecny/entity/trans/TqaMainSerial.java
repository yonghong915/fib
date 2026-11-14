package com.fib.ecny.entity.trans;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fib.ecny.base.dto.wallet.trans.ReversalFlagEnum;
import com.fib.ecny.base.dto.wallet.trans.TranStatusEnum;
import com.fib.ecny.entity.wallet.BaseEntity;
import lombok.Data;

import java.math.BigInteger;

/**
 * 金融主流水表
 */
@Data
@TableName("tqa_main_serial")
public class TqaMainSerial extends BaseEntity {

    @TableId
    private BigInteger id;
    private TranStatusEnum tranStatusEnum;
    private ReversalFlagEnum reversalFlagEnum;
}
