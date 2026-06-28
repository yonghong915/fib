package com.fib.ecny.common.bus.dto.wallet;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WalletInfoDto {

    /**
     * 数币客户号
     */
    @NotBlank(message = "数币客户号不能为空")
    private String ecnyCustNo;

    /**
     * 钱包编码
     */
    @NotBlank(message = "钱包编码不能为空")
    private String walletId;

    /**
     * 钱包名称
     */
    private String walletName;

    /**
     * 钱包等级
     */
    @NotBlank(message = "钱包等级不能为空")
    private String walletLevel;

    /**
     * 钱包状态
     */
    @NotBlank(message = "钱包状态不能为空")
    private String walletStatus;


}
