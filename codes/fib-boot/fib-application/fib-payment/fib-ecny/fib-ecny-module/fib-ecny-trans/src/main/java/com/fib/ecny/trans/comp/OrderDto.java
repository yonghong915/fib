package com.fib.ecny.trans.comp;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
    @NotBlank(message = "订单编号不能为空")
    @Size(max = 20, message = "订单编号不能超过20字")
    private String orderId;

    @NotNull
    @DecimalMin(value = "0.01", message = "交易金额必须大于0")
    @AmtMinUnitJiao(message = "交易金额最小单位为角，仅允许最多一位小数，不可精确到分")
    private BigDecimal tranAmt;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String phone;
}
