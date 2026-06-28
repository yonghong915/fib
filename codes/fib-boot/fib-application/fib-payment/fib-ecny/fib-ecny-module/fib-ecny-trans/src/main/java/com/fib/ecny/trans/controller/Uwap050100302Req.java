package com.fib.ecny.trans.controller;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Uwap050100302Req {

    @NotBlank(message = "walletId must not be null.")
    private String walletId;
}
