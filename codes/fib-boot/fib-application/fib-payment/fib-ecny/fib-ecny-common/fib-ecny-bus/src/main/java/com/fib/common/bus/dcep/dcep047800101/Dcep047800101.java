package com.fib.common.bus.dcep.dcep047800101;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Dcep047800101 {

    @NotBlank(message = "钱包编码不能为空")
    @Size(max = 34, message = "钱包表名不能大于于34个字符")
    private String walletId;
}
