package com.fib.common.bus.base.exchange.trans;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RedeemAcctReq {
    @NotBlank(message = "")
    @Size(min = 3,max = 34,message = "")
    private String walletId;

    @NotNull(message = "")
    @DecimalMin(value = "0.00",message = "")
    private BigDecimal tranAmt;

    @NotEmpty(message = "")
    private List<String> arrList;

    @Positive(message = "")
    @Min(value = 0, message = "")
    @Max(value = 100,message = "")
    private int cnt;

    @Past
    private LocalDateTime startTime;

    @FutureOrPresent
    private LocalDateTime beginTime;

    @Email(message = "")
    @NotBlank(message = "")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$",message = "")
    private String phone;
}
