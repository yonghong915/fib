package com.fib.ecny.base.api.trans.dto;

import com.fib.ecny.base.enums.ChannelEnum;
import com.fib.ecny.base.enums.EnumValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class TqaWalletTranDetailDto {

    @NotNull(message = "")
    private BigInteger id;
    private String glSerialNo;
    private String chnlSerialNo;

    /**
     * 渠道号
     */
    @NotBlank(message = "渠道号不能为空")
    @EnumValue(enumClass = ChannelEnum.class, message = "渠道号只能是110|wap|dcep", allowNull = false)
    private String chnlNo;
    private String tranCode;
    private String walletId;
    private String tranDt;
    private String tranWalletId;

    @NotNull(message = "交易金额不能为空")
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
