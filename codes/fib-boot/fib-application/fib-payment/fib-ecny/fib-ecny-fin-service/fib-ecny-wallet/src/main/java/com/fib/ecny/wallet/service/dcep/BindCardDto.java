package com.fib.ecny.wallet.service.dcep;

import lombok.Data;

@Data
public class BindCardDto {
    private String bindStatus;
    private String bindMode;
    private Integer cardOrder;
}
